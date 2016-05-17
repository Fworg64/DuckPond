package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by fworg on 5/6/2016.
 */
public class DPClientConnection extends Thread
{
    public static final String HOSTNAME = "localhost";
    public static final int PORTNUMBER = 42069;

    Socket DPSocket;
    PrintWriter out;
    BufferedReader in;

    PinPad pinPad;

    boolean greenlight2send;

    public DPClientConnection(PinPad pinPad)
    {
        super("DPClientConnection");
        this.pinPad = pinPad;
    }

    @Override
    public void run()
    {
        greenlight2send = false;

        switch (establishConnection())
        {
            case 0:
                //continue
                communicate();
                break;
            case -1:
                //terminate and error message
                Gdx.app.debug("Failure", "dang");
                break;
            case -2:
                //terminate and error message
                Gdx.app.debug("Failure", "dang2");
                break;
        }

    }

    private int establishConnection()
    {
        try
        {
            DPSocket = new Socket(HOSTNAME, PORTNUMBER);
            out = new PrintWriter(DPSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(DPSocket.getInputStream()));

        }
        catch (UnknownHostException e) {
            Gdx.app.debug("Don't know about host",HOSTNAME);
            return -1;
        }
        catch (IOException e) {
            Gdx.app.debug("Couldn't get I/O for the connection to", HOSTNAME);
            return -2;
        }
        return 0;
    }

    private void communicate()
    {
        switch (sendUserName())
        {
            case -2: //network error
                break;

            case -1: //name2short
                break;

            case 1: //name exists
                //is pin on file??
                if (Options.getSavedPin().equals("\0")) //no stored pin
                {
                    Gdx.app.debug("saved pin is"," null");
                    pinPrompt(); //prompt for initial pin to get pin, this should save the pin to options
                }
                if (checkPin()) greenlight2send = true; //sends pin in options to server
                while (!(greenlight2send || pinPad.isInputcancelled()))
                {
                    pinPrompt(); //blocking
                    if (checkPin()) greenlight2send = true;
                    if (pinPad.isInputcancelled()) //user gave up
                    {
                        Gdx.app.debug("No Pin", "User terminated pin entry");
                    }
                }
                break;

            case 2: //name DNE
                String temppin2;
                while (!(pinPad.isInputcancelled() || greenlight2send)) //basically do-while
                {
                    temppin2 = pinPrompt(); //get pin first time
                    if (pinPad.isInputcancelled()) //user gave up
                    {
                        Gdx.app.debug("No Pin", "User terminated pin entry");
                        continue; //break whatever, exit this loop
                    }
                    pinPad.setConfirmingPin();
                    if (temppin2.equals(pinPrompt())) //prompt for pin again, check if it equals that entered before
                    {
                        pinPad.unsetConfirmingPin();
                        if (setServerPin2PinOnFile() == true) Gdx.app.debug("Pin set to server", "Whoo!");
                        else Gdx.app.debug("Pin", "Not accepterd??");
                        greenlight2send = true;
                    }
                    else if (pinPad.isInputcancelled()) //user gave up
                    {
                        Gdx.app.debug("No Pin", "User terminated pin entry");
                        continue; //break whatever, exit this loop
                    }
                }
                if (pinPad.isInputcancelled())
                {
                    Gdx.app.debug("pin input cancelled for new user", "What now?");
                    pinPad.resetInputcancelled();
                }
                break;
        }

        Gdx.app.debug("green light to send!", "whoo");
    }

    private int sendUserName()
    {
        String tempuser = Options.getUsername();
        String fromserver;
        if (tempuser.length() < 3)
        {
            Gdx.app.debug("Invalid Username", "Too Short");
            return -1;
        }
        else
        {
            out.println(Options.getUsername());
            Gdx.app.debug("ToServer", Options.getUsername());
        }

        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            return  -2;
        }

        try
        {
            fromserver = in.readLine();
            Gdx.app.debug("FromServer", fromserver);
        }
        catch (SocketTimeoutException STE)
        {
            Gdx.app.debug("Server Timeout", "No response after 5? seconds");
            return -2;
        }
        catch (IOException e)
        {
            Gdx.app.debug("InternalError", "Couldn't readline from server");
            //fromserver = "CRAP";
            return -2;
        }

        if (fromserver.equals("EXIST")) return 1;
        if (fromserver.equals("NONEXIST")) return 2;
        else
        {
            Gdx.app.debug("invalid response from server", fromserver);
            return -2; //invalid response from server;
        }
    }

    private String pinPrompt()
    {
        pinPad.setNeedpin();
        while (!pinPad.isGotpin())//wait for other thread to get pin
        {
            if (pinPad.isInputcancelled())
            {
                pinPad.resetInputcancelled();
                pinPad.unsetNeedpin();
                return "CRAP";
            }
        }
        Gdx.app.debug("pinGOT", pinPad.getTempPin());
        pinPad.unsetNeedpin();
        return pinPad.getTempPin();
    }

    private boolean checkPin()
    {
        String fromserver ="";
        out.println(Options.getSavedPin());
        Gdx.app.debug("ToServer", Options.getSavedPin());

        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            return  false;
        }
        try
        {
            fromserver = in.readLine();
            Gdx.app.debug("FromServer", fromserver);
        }
        catch (SocketTimeoutException STE)
        {
            Gdx.app.debug("Server Timeout", "No response after 5? seconds");
            return false;
        }
        catch (IOException e)
        {
            Gdx.app.debug("InternalError", "Couldn't readline from server");
            //fromserver = "CRAP";
            return false;
        }

        if (fromserver.equals("ALLSGOOD")) return true;
        else return false;
    }

    private boolean setServerPin2PinOnFile()
    {
        String fromserver ="";
        out.println(Options.getSavedPin());
        Gdx.app.debug("ToServer", Options.getSavedPin());

        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            return  false;
        }

        try
        {
            fromserver = in.readLine();
            Gdx.app.debug("FromServer", fromserver);
        }
        catch (SocketTimeoutException STE)
        {
            Gdx.app.debug("Server Timeout", "No response after 5? seconds");
            return false;
        }
        catch (IOException e)
        {
            Gdx.app.debug("InternalError", "Couldn't readline from server");
            //fromserver = "CRAP";
            return false;
        }

        if (fromserver.equals("RECIEVED")) return true;
        else return false;
    }
}

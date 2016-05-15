package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;

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

    boolean greenlight2send;

    boolean needpin;
    boolean gotpin;
    boolean needConfirmPin;
    boolean pinConfirmSuccess;


    public DPClientConnection()
    {
        super("DPClientConnection");
    }

    @Override
    public void run()
    {
        greenlight2send = false;
        needpin = false;
        gotpin = false;
        needConfirmPin = false;

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
                //does pin on file??
                if (Options.getSavedPin().equals("\0")) //if no has a stored pin
                {pinPrompt();}
                if (checkPin()) greenlight2send = true;
                while (!greenlight2send) //must be broken through the logic within
                {
                    if (Options.getSavedPin().equals("\0")) //user gave up
                    {
                        Gdx.app.debug("No Pin", "User terminated pin entry");
                        break;
                    }
                    pinPrompt();
                    if (checkPin()) greenlight2send = true;
                }
                break;

            case 2: //name DNE
                boolean pinconfirmed = false;
                while (!pinconfirmed) //basically do-while
                {
                    pinPrompt();
                    if (Options.getSavedPin().equals("\0")) //user gave up
                    {
                        Gdx.app.debug("No Pin", "User terminated pin entry");
                        break;
                    }
                    if (pinConfirmPrompt())
                    {
                        pinconfirmed = true;
                        if (setServerPin2PinOnFile() == true) Gdx.app.debug("Pin set", "Whoo!");
                        else Gdx.app.debug("Pin", "Not accepterd??");
                        greenlight2send = true;
                    }
                }
                break;
        }

        out.println("dirgety");
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

    private void pinPrompt()
    {
        needpin = true;
        while (!gotpin); //wait for other thread to get pin
        needpin = false;
    }

    private boolean pinConfirmPrompt()
    {
        pinConfirmSuccess = false;
        needConfirmPin =true;
        while (!gotpin);
        return pinConfirmSuccess;
    }

    private boolean checkPin()
    {
        String fromserver ="";
        out.println(Options.getSavedPin());

        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            return  false;
        }

        try
        {
            fromserver = in.readLine();
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

        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            return  false;
        }

        try
        {
            fromserver = in.readLine();
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

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
    FileTransferCommunicator fileTransferCommunicator;

    boolean greenlight2send;

    public DPClientConnection(PinPad pinPad, FileTransferCommunicator fileTransferCommunicator)
    {
        super("DPClientConnection");
        this.pinPad = pinPad;
        this.fileTransferCommunicator = fileTransferCommunicator;
    }

    @Override
    public void run()
    {
        greenlight2send = false;

        switch (establishConnection())
        {
            case 0:
                //continue
                switch (communicate())
                {
                    case -3: //user gave up
                        return;
                    case -2: //network error
                        return;
                    case -1: //invalid name
                        return;
                    case 0: //file sent successfully
                        return;
                }
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

    private int communicate()
    {
        switch (sendUserName())
        {
            case -2: //network error
                return -2;
            case -1: //name2short
                return -1;

            case 1: //name exists
                //is pin on file??
                if (Options.getSavedPin().equals("\0")) //no stored pin
                {
                    Gdx.app.debug("saved pin is"," null");
                    pinPad.setMessage("Please enter pin, " + Options.getUsername());
                    pinPrompt(); //prompt for initial pin to get pin, this should save the pin to options
                }
                if (checkPin()) greenlight2send = true; //sends pin in options to server
                while (!(greenlight2send || pinPad.isInputcancelled()))
                {
                    pinPad.setMessage("Incorrect, please try again "+ Options.getUsername());
                    pinPrompt(); //blocking
                    if (checkPin()) greenlight2send = true;
                    //input cancelled handled below
                }
                break;

            case 2: //name DNE
                String temppin2;
                boolean firstattemptfailed = false;
                while (!(pinPad.isInputcancelled() || greenlight2send)) //basically do-while
                {
                    if (!firstattemptfailed) pinPad.setMessage("Please enter new PIN, " + Options.getUsername());
                    else pinPad.setMessage("PINs did not match, try again");
                    temppin2 = pinPrompt(); //get pin first time
                    if (pinPad.isInputcancelled()) //user gave up
                    {
                        //skip the confirm prompt
                        continue;
                    }
                    pinPad.setMessage("Please confirm PIN, " +Options.getUsername());
                    if (temppin2.equals(pinPrompt())) //prompt for pin again, check if it equals that entered before
                    {
                        if (setServerPin2PinOnFile() == true)
                        {
                            Gdx.app.debug("Pin set to server", "Whoo!");
                            greenlight2send = true;
                        }
                        else
                        {
                            Gdx.app.debug("Server Error", "Pin Not accepterd??");
                            return -2;
                        }
                    }
                    firstattemptfailed =true;
                    //loop will break here if input was canclled, to be handeled below
                }
                if (pinPad.isInputcancelled()) // if we exited the loop for input cancelled
                {
                    Gdx.app.debug("pin input cancelled for new user", "not sent to server");
                    out.println("\0");
                    pinPad.resetInputcancelled();
                    return -3;
                }
                break;
        }

        if (greenlight2send)
        {
            Gdx.app.debug("green light to send!", "whoo");
            fileTransferCommunicator.setNeedfile();
            while (!fileTransferCommunicator.isFilegot() && fileTransferCommunicator.isNeedfile()); //dont do this, this is bad
            if (fileTransferCommunicator.isWascancelled())
            {
                Gdx.app.debug("File transfer", "Cancelled!");
                fileTransferCommunicator.resetWascancelled();
                return -3;
            }
            else //we got the file
            {
                String temp = fileTransferCommunicator.getThefile();
                out.println(fileTransferCommunicator.getFilename());
                out.println(temp);
                out.println("\4");
                Gdx.app.debug(fileTransferCommunicator.getFilename() + " sent to server ", Integer.toString(temp.length()) + " chars sent");
                return 0;
            }
        }
        else //shouldn't run these lines
        {
            Gdx.app.debug("unhandled something", "check communicate method");
            return -2;
        }

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
                pinPad.unsetNeedpin();
                return "\0";
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

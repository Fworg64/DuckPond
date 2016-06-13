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
 * Created by fworg on 6/11/2016.
 */
public class DPUploader extends Thread{
    public static final String HOSTNAME = "www.tcupdevelopment.me";
    public static final int PORTNUMBER = 42069;

    Socket DPSocket;
    PrintWriter out;
    BufferedReader in;

    DPUploadCommunicator DPU;

    private String username;
    private boolean needPin;

    DPUploader(DPUploadCommunicator DPU, String username)
    {
        this.DPU = DPU;
        this.username = username;
        needPin = true;
        try
        {
            DPSocket = new Socket(HOSTNAME, PORTNUMBER);
            out = new PrintWriter(DPSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(DPSocket.getInputStream()));
        }
        catch (UnknownHostException e) {
            Gdx.app.debug("Don't know about host",HOSTNAME);
            DPU.setState(DPUploadCommunicator.State.ERROR);
        }
        catch (IOException e) {
            Gdx.app.debug("Couldn't get I/O for the connection to", HOSTNAME);
            DPU.setState(DPUploadCommunicator.State.ERROR);
        }
        sendString(username);
        String temp = recieveString();
        if (temp.equals("EXIST")) DPU.setState(DPUploadCommunicator.State.NEEDCURRPIN);
        else if (temp.equals("NONEXIST")) DPU.setState(DPUploadCommunicator.State.NEEDNEWPIN);
        else DPU.setState(DPUploadCommunicator.State.ERROR);
    }

    @Override
    public void run()
    {
        while (true)
        {
            if (needPin)
            {
                String temppin = DPU.getPin();
                if (!temppin.equals(""))
                {
                    Gdx.app.debug("DPUploader, temppin", temppin);
                    DPU.setState(DPUploadCommunicator.State.BUSY);
                    sendString(temppin);
                    Gdx.app.debug("DPUploader", "Pin Sent");
                    needPin = false;
                    String temp2 = recieveString();
                    if (temp2.equals("ALLSGOOD") || temp2.equals("RECIEVED"))
                    {
                        DPU.setState(DPUploadCommunicator.State.NEEDFILE);
                    }
                    else {
                        Gdx.app.debug("DPUploader", "Pin Sent, but rejected");
                        DPU.sendPin("");
                        DPU.setState(DPUploadCommunicator.State.WRONGPIN);
                        needPin = true;
                    }
                }
            }
            else //pin got
            {
                if (!DPU.getFilename().equals("")) //send the file for the first time
                {
                    DPU.setState(DPUploadCommunicator.State.BUSY);
                    sendString(DPU.getFilename());
                    sendString(DPU.getFilecontents());
                    sendString("\4");
                    DPU.resetFile();
                    DPU.setState(DPUploadCommunicator.State.NEEDFILE);
                }
            }
            if (DPU.getState() == DPUploadCommunicator.State.CLOSE) {
                sendString("\3"); //user left
                return;
            }
        }
    }

    private void sendString(String s)
    {
        out.println(s);
        Gdx.app.debug("ToServer", s);
    }

    private String recieveString()
    {
        String fromserver;
        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            DPU.setState(DPUploadCommunicator.State.ERROR);
            return  "";
        }

        try
        {
            fromserver = in.readLine();
            Gdx.app.debug("FromServer", fromserver);
        }
        catch (SocketTimeoutException STE)
        {
            Gdx.app.debug("Server Timeout", "No response after 5? seconds");
            DPU.setState(DPUploadCommunicator.State.ERROR);
            return "";
        }
        catch (IOException e)
        {
            Gdx.app.debug("InternalError", "Couldn't readline from server");
            DPU.setState(DPUploadCommunicator.State.ERROR);
            //fromserver = "CRAP";
            return "";
        }
        return fromserver;
    }
}

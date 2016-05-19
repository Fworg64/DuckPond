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
 * Created by fworg on 5/18/2016.
 */
public class DPGetMoreConnection extends Thread
{
    public static final String HOSTNAME = "localhost";
    public static final int PORTNUMBER = 42068;

    Socket DPSocket;
    PrintWriter out;
    BufferedReader in;

    GetMoreBrowser getMoreBrowser;

    public DPGetMoreConnection(GetMoreBrowser getMoreBrowser)
    {
        super("DPGetMoreConnection");
        this.getMoreBrowser = getMoreBrowser;
    }

    @Override
    public void run()
    {
        switch (establishConnection())
        {
            case 0:
                //continue, connection established
                Gdx.app.debug("Connection established with server", "no-dang");
                break;
            case -1:
                //terminate and error message
                Gdx.app.debug("Failure", "dang");
                return;
            case -2:
                //terminate and error message
                Gdx.app.debug("Failure", "dang2");
                return;
        }

        switch (getRequest()) //blocking
        {
            case -1: //unknown input
                break;
            case 0: //input was cancelled
                out.println("Q");
                break;
            case 1: //random request
                out.println("R");
                break;
            case 2: //choice options, recomennded
                out.println("C");
                break;
            case 3: //user wants to search
                out.println("S");
                break;
            case 4: //user wants popular
                out.println("P");
                break;
        }

        String fromserver;
        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            return;
        }
        try
        {
            fromserver = in.readLine();
            Gdx.app.debug("FromServer", fromserver);
        }
        catch (SocketTimeoutException STE)
        {
            Gdx.app.debug("Server Timeout", "No response after 5? seconds");
            return;
        }
        catch (IOException e)
        {
            Gdx.app.debug("InternalError", "Couldn't readline from server");
            //fromserver = "CRAP";
            return;
        }
    }

    private int getRequest()
    {
        getMoreBrowser.setNeedRequest();
        while (!getMoreBrowser.isGotRequest())//wait for other thread to get pin
        {
            if (getMoreBrowser.isWasCancelled())
            {
                getMoreBrowser.unsetNeedRequest();
                return 0; //input was cancelled
            }
        }
        String tempRequest = getMoreBrowser.getRequest();
        Gdx.app.debug("requestGOT",tempRequest);
        getMoreBrowser.unsetNeedRequest();
        if (tempRequest.equals("Q")) { //user quit/cancelled
            return 0;
        }
        if (tempRequest.equals("R")) { //random user
            return 1;
        }
        if (tempRequest.equals("C")) { //recommened, choice users
            return 2;
        }
        if (tempRequest.equals("S")) { //user wants to search
            return 3;
        }
        if (tempRequest.equals("P")) { //user wants popular options
            return 4;
        }
        else return -1; //unknown input
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
}

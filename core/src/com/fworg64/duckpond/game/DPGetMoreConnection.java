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
import java.util.ArrayList;
import java.util.List;

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

        String tempRequest = getRequest(); //blocking
        out.println(tempRequest); //send request to server

        //receive path options
        List<String> optionsGot;
        optionsGot = getOptions();
        if (optionsGot.size() == 0)
        {
            Gdx.app.debug("Unable to get options for ", "request");
            return;
        }

        //send options to browser
        getMoreBrowser.setAllOptions(optionsGot);

        //get another request
        tempRequest = getRequest();
        out.println(tempRequest);

        try
        {
            String fromserver = in.readLine();
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

    private List<String> getOptions()
    {
        List<String> options = new ArrayList<String>();
        String fromserver;
        try{DPSocket.setSoTimeout(5000);} //set timeout to 5 sec
        catch (SocketException STE)
        {
            Gdx.app.debug("Unable to set SOCKET TIMEOUT", "We're in Trouble");
            return options;
        }
        try
        {
            fromserver = in.readLine();
            Gdx.app.debug("FromServer", fromserver);
            while (!fromserver.equals("\3"))
            {
                options.add(fromserver);
                Gdx.app.debug("Added to options" ,fromserver);
                fromserver = in.readLine();
            }
        }
        catch (SocketTimeoutException STE)
        {
            Gdx.app.debug("Server Timeout", "No response after 5? seconds");
            return options;
        }
        catch (IOException e)
        {
            Gdx.app.debug("InternalError", "Couldn't readline from server");
            //fromserver = "CRAP";
            return options;
        }
        return options;
    }

    private String getRequest()
    {
        getMoreBrowser.setNeedRequest();
        while (!getMoreBrowser.isGotRequest())//wait for other thread to get pin
        {
            if (getMoreBrowser.isWasCancelled())
            {
                getMoreBrowser.unsetNeedRequest();
                return "Q"; //input was cancelled
            }
        }
        String tempRequest = getMoreBrowser.getRequest();
        Gdx.app.debug("requestGOT",tempRequest);
        getMoreBrowser.unsetNeedRequest();
        getMoreBrowser.unsetGotRequest();
        return tempRequest;
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

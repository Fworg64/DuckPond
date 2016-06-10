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
 * Created by fworg on 6/9/2016.
 */
public class BrowsableDPGetmore implements Browsable{

    public static final String HOSTNAME = "www.tcupdevelopment.me";
    public static final int PORTNUMBER = 42068;

    Socket DPSocket;
    PrintWriter out;
    BufferedReader in;

    private List<String> allOptions;

    private boolean canPageUp;
    private boolean isFinalSelection;

    String selectionName;
    String selectionContents;

    public boolean connectedSucessfully;

    public BrowsableDPGetmore() //should be run in a seperate thread
    {
        connectedSucessfully = true; //will be changed if failed
        canPageUp = false;
        isFinalSelection = false;
        try
        {
            DPSocket = new Socket(HOSTNAME, PORTNUMBER);
            out = new PrintWriter(DPSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(DPSocket.getInputStream()));

        }
        catch (UnknownHostException e) {
            Gdx.app.debug("Don't know about host",HOSTNAME);
            connectedSucessfully = false;
        }
        catch (IOException e) {
            Gdx.app.debug("Couldn't get I/O for the connection to", HOSTNAME);
            connectedSucessfully = false;
        }
        pageInto("");
    }

    @Override
    public List<String> getAllOptions() {
        return allOptions;
    }

    @Override
    public void pageInto(String option) {
        if (!option.equals("")) out.println(option);//send option to server
        allOptions = getBlockFromServer();
        if (allOptions.size() >0)
        {
            String temp = allOptions.get(0);
            if (temp.equals("toplevel")) canPageUp = false;
            if (temp.equals("choices")) canPageUp = true;
            if (temp.equals("level"))
            {
                isFinalSelection = true;
                selectionName = allOptions.get(1);
                selectionContents = allOptions.get(2);
            }
            allOptions.remove(0);
        }
        else
        {
            Gdx.app.debug("BrowserDPGetMore", "Could not get options from server");
        }



        //need a way to determine if a page up is possible
        //also if something was the final selection

    }

    @Override
    public void pageUp() {
        out.println("\5"); //go up signal
        allOptions = getBlockFromServer();

    }

    @Override
    public boolean canPageUp() {
        return canPageUp;
    }

    @Override
    public boolean isFinalSelection() {
        return isFinalSelection;
    }

    @Override
    public String getSelectionName() {
        return selectionName;
    }

    @Override
    public String getSelectionContents() {
        return selectionContents;
    }

    @Override
    public void close() {
        try{
            out.println("\4"); //exit signal
            DPSocket.close();
        }
        catch (IOException e)
        {
            Gdx.app.debug("BrowsableDPGetMore", "Unable to close session gracefully");
        }


    }

    private List<String> getBlockFromServer()
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
}

package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

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
    public static final String HOSTNAME = "www.tcupdevelopment.me";
    public static final int PORTNUMBER = 42068;

    Socket DPSocket;
    PrintWriter out;
    BufferedReader in;

    GetMoreBrowser getMoreBrowser;

    String typeRequest;
    String userRequest;
    String levelRequest;

    private boolean getType; //requests
    private boolean getUser;
    private boolean getLevel;
    private boolean dlLevel;

    private List<String> optionsGot;

    public DPGetMoreConnection(GetMoreBrowser getMoreBrowser)
    {
        super("DPGetMoreConnection");
        this.getMoreBrowser = getMoreBrowser;
        getType = false;
        getUser = false;
        getLevel = false;
        dlLevel = false;
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
        getType = true;

        while(true)
        {
            if (getType)
            {
                //receive type options
                optionsGot = getBlockFromServer();
                getMoreBrowser.disallowPageUp();
                getMoreBrowser.setAllOptions(new ArrayList<String>(optionsGot));

                //select option
                typeRequest = getRequest(); //blocking
                out.println(typeRequest); //send request to server
                //A 4 will be sent if the user cancels/exits the screen and the server will quit the session
                if (typeRequest.equals("\4"))
                {
                    Gdx.app.debug("User exited download session"," from type selection");
                    return;
                }
                // A 5 (pageup) cannot be sent because it has been disallowed
                //next step
                getType = false;
                getUser = true;
            }
            if (getUser)
            {
                //receive path options (users)
                optionsGot = getBlockFromServer(); //get options from server
                if (optionsGot.size() == 0)
                {
                    Gdx.app.debug("Unable to get options for ", typeRequest);
                    return;
                }
                //send user options to browser
                getMoreBrowser.setAllOptions(new ArrayList<String>(optionsGot));
                getMoreBrowser.allowPageUp();
                //get user request from browser
                userRequest = getRequest();
                out.println(userRequest);

                if (typeRequest.equals("\4"))
                {
                    Gdx.app.debug("User exited download session"," from user selection");
                    return;
                }
                else if (userRequest.equals("\5"))//go baack, the server will know
                {
                    getType = true;
                    getUser = false;
                }
                else //should be a valid request
                {
                    getUser = false;
                    getLevel = true;
                }
            }
            if (getLevel)
            {
                //receive path options (levels this time)
                optionsGot = getBlockFromServer(); //getOptions from server
                if (optionsGot.size() == 0)
                {
                    Gdx.app.debug("Unable to get options for ", userRequest);
                    return;
                }
                //send level options to browser
                getMoreBrowser.setAllOptions(new ArrayList<String>(optionsGot));
                //get level request from browser
                levelRequest = getRequest(); //get level request from browser
                out.println(levelRequest); //let the server error check

                if (typeRequest.equals("\4"))
                {
                    Gdx.app.debug("User exited download session"," from level selection");
                    return;
                }
                else if (levelRequest.equals("\5"))//go baack, the server will know
                {
                    getUser = true;
                    getLevel = false;
                }
                else //level should have been gotten
                {
                    getLevel = false;
                    dlLevel = true;
                }
            }
            if (dlLevel) //file selected
            {
                List<String> levelstrings;
                levelstrings = getBlockFromServer();
                writeStringsToFile(levelstrings, Gdx.files.local("LEVELS/DOWNLOADED" + '/' + userRequest + '/' + levelRequest));
                dlLevel = false;
                getLevel = true;
            }
        }
    }

    private void writeStringsToFile(List<String> strings, FileHandle f)
    {
    	f.parent().mkdirs();
        if (f.exists())
        {
            f.delete();
            Gdx.app.debug("Existing file deleated", f.toString());
        }
        for (String s: strings)
        {
            f.writeString(s.toString() + '\n', true);
        }
        Gdx.app.debug(f.toString(), "wrote for " + Integer.toString(strings.size()) + " lines.");
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

    private String getRequest()
    {
        getMoreBrowser.setNeedRequest();
        while (!getMoreBrowser.isGotRequest())//wait for other thread to get pin
        {
            if (getMoreBrowser.isWasCancelled())
            {
                getMoreBrowser.unsetNeedRequest();
                return "\4"; //input was cancelled
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

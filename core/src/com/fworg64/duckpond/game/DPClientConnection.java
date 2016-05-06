package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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


    public DPClientConnection()
    {
        super("DPClientConnection");
    }

    @Override
    public void run()
    {
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
        String fromServer;
        String fromUser;
        fromServer = "";

        while (!fromServer.equals("Bye."))
        {
            try
            {
                fromServer = in.readLine(); //read from server
            }
            catch (IOException e)
            {
                fromServer = "INTERNAL ERROR, COULDNT READ FROM SERVER";
            }


            Gdx.app.debug("Server", fromServer);

            fromUser = "herp";
            if (fromUser != null) {
                Gdx.app.debug("Client", fromUser);
                out.println(fromUser); //send to server
            }
        }
    }

}

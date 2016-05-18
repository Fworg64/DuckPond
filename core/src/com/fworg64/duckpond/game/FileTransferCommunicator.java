package com.fworg64.duckpond.game;

/**
 * Created by fworg on 5/18/2016.
 */
public class FileTransferCommunicator
{
    private String thefile;
    private String filename;
    private boolean needfile;
    private boolean filegot;
    private boolean wascancelled;

    public FileTransferCommunicator()
    {
        needfile = false;
        filegot = false;
        wascancelled = false;
    }

    public synchronized void setNeedfile()
    {
        needfile = true;
    }

    public synchronized boolean isNeedfile()
    {
        return needfile;
    }

    public synchronized void cancelNeedfile()
    {
        needfile = false;
        wascancelled = true;
    }

    public synchronized boolean isWascancelled()
    {
        return wascancelled;
    }

    public synchronized void resetWascancelled()
    {
        wascancelled = false;
    }

    public synchronized void setThefile(String filename1, String file)
    {
        filename = filename1;
        thefile = file;
        filegot = true;
        needfile = false;
    }

    public synchronized String getThefile()
    {
        filegot = false;
        return thefile;
    }

    public synchronized String getFilename()
    {
        return filename;
    }

    public synchronized boolean isFilegot()
    {
        return filegot;
    }
}

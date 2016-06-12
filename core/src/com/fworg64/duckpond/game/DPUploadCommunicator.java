package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by fworg on 6/11/2016.
 */
public class DPUploadCommunicator {
    public enum State {NEEDNEWPIN, NEEDCURRPIN, NEEDFILE, BUSY, ERROR, CLOSE};

    private State state;

    private String pin;
    private String filename;
    private String filecontents;

    DPUploadCommunicator()
    {
        state = State.BUSY;
        pin = "";
        filecontents = "";
        filename = "";
    }

    public synchronized State getState() {
        //Gdx.app.debug("DPUCom", "StateGot: " + state.toString());
        return state;
    }

    public synchronized void setState(State state) {
        Gdx.app.debug("DPUCom", "StateSet: " + state.toString());
        this.state = state;
    }

    public synchronized String getPin() {
        return pin;
    }

    public synchronized void sendPin(String pin) {
        this.pin = pin;
        state = State.BUSY;
    }

    public synchronized String getFilename() {
        return filename;
    }

    public synchronized void setFile(String filename, String filecontents) {
        this.filename = filename;
        this.filecontents = filecontents;
    }

    public synchronized String getFilecontents() {
        return filecontents;
    }

}

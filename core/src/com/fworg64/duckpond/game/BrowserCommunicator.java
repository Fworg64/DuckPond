package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 6/9/2016.
 */
public class BrowserCommunicator {
    private Vector2 touchpoint;
    private String message;
    private boolean selectionMade;
    private String selectionName;
    private String selectionContents;
    private boolean resetSelection;
    private boolean close;
    
    BrowserCommunicator()
    {
        touchpoint = new Vector2();
        message = "";
        selectionMade = false;
        selectionName = "";
        selectionContents = "";
        resetSelection = false;
    }

    public synchronized Vector2 getTouchpoint() {
        return touchpoint;
    }

    public synchronized void setTouchpoint(Vector2 touchpoint) {
        //if (!touchpoint.isZero()) Gdx.app.debug("BrowserCommunicator", "touched at " + touchpoint.toString());
        this.touchpoint = touchpoint;
    }

    public synchronized String getMessage() {
        return message;
    }

    public synchronized void setMessage(String message) {
        this.message = message;
    }

    public synchronized boolean isSelectionMade() {
        return selectionMade;
    }

    public synchronized void setSelectionMade(boolean selectionMade) {
        this.selectionMade = selectionMade;
    }

    public synchronized String getSelectionName() {
        return selectionName;
    }

    public synchronized void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    public synchronized String getSelectionContents() {
        return selectionContents;
    }

    public synchronized void setSelectionContents(String selectionContents) {
        this.selectionContents = selectionContents;
    }

    public synchronized boolean isResetSelection() {
        return resetSelection;
    }

    public synchronized void setResetSelection(boolean resetSelection) {
        this.resetSelection = resetSelection;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}

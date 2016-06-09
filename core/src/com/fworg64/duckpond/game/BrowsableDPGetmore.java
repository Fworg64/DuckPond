package com.fworg64.duckpond.game;

import java.util.List;

/**
 * Created by fworg on 6/9/2016.
 */
public class BrowsableDPGetmore implements Browsable{
    @Override
    public List<String> getAllOptions() {
        return null;
    }

    @Override
    public void pageInto(String option) {

    }

    @Override
    public void pageUp() {

    }

    @Override
    public boolean canPageUp() {
        return false;
    }

    @Override
    public boolean isFinalSelection() {
        return false;
    }

    @Override
    public String getSelectionName() {
        return null;
    }

    @Override
    public String getSelectionContents() {
        return null;
    }

    @Override
    public void close() {

    }
}

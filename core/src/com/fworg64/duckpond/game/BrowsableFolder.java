package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fworg on 6/7/2016.
 */
public class BrowsableFolder implements Browsable {

    List<String> allOptions;
    boolean canPageUp;
    FileHandle fileDir;
    FileHandle fileoptions[];
    boolean isFinalSelection;
    String filename;
    String filecontents;
    FileHandle toplevel;

    boolean interal;

    public BrowsableFolder(String path, boolean internal) //if not internal, then local
    {
        this.interal = internal;
        fileDir = loadPathInternalLocal(path);
        toplevel = loadPathInternalLocal(path);
        canPageUp = false;
        isFinalSelection = false;
        filename = "";
        filecontents = "";
        fileoptions = toplevel.list();
        allOptions = new ArrayList<String>();
        for (FileHandle option : fileoptions)
        {
            allOptions.add(option.nameWithoutExtension());
        }
    }
    @Override
    public List<String> getAllOptions() {
        return allOptions;
    }

    @Override
    public void pageInto(String option) {
        boolean updatealloptions = false;
        for (int i =0; i < allOptions.size(); i++)
        {
            if (option.equals(allOptions.get(i)))
            {
                if (fileoptions[i].isDirectory())
                {
                    fileDir = loadPathInternalLocal(fileoptions[i].path());
                    fileoptions = fileDir.list();
                    updatealloptions = true;
                    canPageUp = true;
                    Gdx.app.debug("BrowsableFolder", "Directory entered");
                    break;
                }
                else
                {
                    fileDir = loadPathInternalLocal(fileoptions[i].path());
                    filename = fileDir.nameWithoutExtension();
                    filecontents = fileoptions[i].readString();
                    isFinalSelection = true;
                    Gdx.app.debug("BrowsableFolder", "File Pressed");
                    break;
                }
            }
        }
        if (updatealloptions)
        {
            allOptions.clear();
            for (FileHandle filee : fileoptions)
            {
                allOptions.add(filee.nameWithoutExtension());
            }
        }



    }

    public FileHandle loadPathInternalLocal(String path)
    {
        if (interal) return Gdx.files.internal(path);
        else return Gdx.files.local(path);
    }

    @Override
    public void pageUp() {
        if (canPageUp) {
            allOptions.clear();
            fileoptions = fileDir.parent().list();
            fileDir = fileDir.parent();
            for (FileHandle filee : fileoptions)
            {
                allOptions.add(filee.nameWithoutExtension());
            }
            if (fileDir.equals(toplevel)) canPageUp = false;
            Gdx.app.debug("BrowsableFolder", "PagedUp");
        }

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
        return filename;
    }

    @Override
    public String getSelectionContents() {
        return filecontents;
    }

    @Override
    public void close() {

    }
}

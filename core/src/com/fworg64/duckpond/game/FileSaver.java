package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by fworg on 6/10/2016.
 */
public class FileSaver {
    public static void saveLocalFile(String locationAndName, String contents)
    {
        FileHandle file = Gdx.files.local(locationAndName);
        file.parent().mkdirs();
        file.writeString(contents, false);
    }
}

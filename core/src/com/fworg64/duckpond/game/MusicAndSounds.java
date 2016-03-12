package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by fworg on 3/11/2016.
 */
public class MusicAndSounds
{
    public Music mainmenuloop;
    public Music gamemusic;
    public Music currMusic;

    public Sound ducks;
    public Sound chomp;

    public float musicVol;
    public float sfxVol;

    public MusicAndSounds()
    {
        musicVol = Options.getMusicVol();
        sfxVol = Options.getSfxVol();

        mainmenuloop = Gdx.audio.newMusic(Gdx.files.internal("SOUNDS\\mainmenuloop.mp3"));
        mainmenuloop.setLooping(true);
        mainmenuloop.setVolume(musicVol);

        gamemusic = Gdx.audio.newMusic(Gdx.files.internal("SOUNDS\\Ragga_Atonal_Island.mp3"));
        gamemusic.setLooping(true);
        gamemusic.setVolume(musicVol);

        currMusic = mainmenuloop;

        ducks = Gdx.audio.newSound(Gdx.files.internal("SOUNDS\\ducks.mp3"));
        chomp = Gdx.audio.newSound(Gdx.files.internal("SOUNDS\\chomp.mp3"));
    }

    public void playMainMenu()
    {
        currMusic = mainmenuloop;
        mainmenuloop.setVolume(musicVol);
        if (!mainmenuloop.isPlaying())
        {
            mainmenuloop.play();
        }
    }

    public void setMusicVol(float musicVol1)
    {
        musicVol = musicVol1;
        currMusic.setVolume(musicVol); //either needs to be all music, or current music
        Gdx.app.debug(Float.toString(mainmenuloop.getVolume()), Float.toString(currMusic.getVolume()));
    }

    public void loadGameMusicAndDisposeMMM()
    {
        mainmenuloop.stop();
        gamemusic = Gdx.audio.newMusic(Gdx.files.internal("SOUNDS\\Ragga_Atonal_Island.mp3"));
        gamemusic.setLooping(true);
        gamemusic.setVolume(musicVol);
        currMusic = gamemusic;
    }

    public void playGameMusic()
    {
        gamemusic.setVolume(musicVol);
        if (!gamemusic.isPlaying())
        {
            gamemusic.play();
        }
        currMusic = gamemusic;
    }

    public void pauseCurrMusic()
    {
        currMusic.pause();
    }

    public void stopCurrMusic()
    {
        currMusic.stop();
    }

    public void playChomp()
    {
        chomp.play(sfxVol);
    }

    public void playDucks()
    {
        ducks.play(sfxVol);
    }

    public void setSfxVol(float sfxVol1)
    {
        sfxVol = sfxVol1;
    }
}

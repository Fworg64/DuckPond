package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by fworg on 3/11/2016.
 */
public class MusicAndSounds
{
    private Music mainmenuloop;
    private Music gamemusic;
    private Music currMusic;
    private Music gameOverMusic;
    private Music victoryMusic;

    public enum CurrSong {MAINMEU, GAME, GAMEOVER, VICTORY};
    public CurrSong currSong;

    public Sound ducks;
    public Sound chomp;

    public float musicVol;
    public float sfxVol;
    public boolean isMuted;

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

        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("SOUNDS\\A_Turn_for_the_Worse.mp3"));
        gameOverMusic.setLooping(true);
        gameOverMusic.setVolume(musicVol);

        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("SOUNDS\\Ragga_ElectroIsland.mp3"));
        gameOverMusic.setLooping(true);
        victoryMusic.setVolume(musicVol);

        currMusic = mainmenuloop;
        currSong = CurrSong.MAINMEU;

        ducks = Gdx.audio.newSound(Gdx.files.internal("SOUNDS\\ducks.mp3"));
        chomp = Gdx.audio.newSound(Gdx.files.internal("SOUNDS\\chomp.mp3"));
    }

    public void playMainMenu()
    {
        currMusic = mainmenuloop;
        currSong = CurrSong.MAINMEU;
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

    public void playGameOverMusic()
    {
        gameOverMusic.setVolume(musicVol);
        if (!gameOverMusic.isPlaying())
        {
            gameOverMusic.play();
        }
        currMusic = gameOverMusic;
        currSong = CurrSong.GAMEOVER;
    }

    public void playGameMusic()
    {
        gamemusic.setVolume(musicVol);
        gamemusic.play();
        currMusic = gamemusic;
        currSong = CurrSong.GAME;
    }

    public void playVictoryMusic()
    {
        victoryMusic.setVolume(musicVol);
        victoryMusic.play();
        currMusic = victoryMusic;
        currSong = CurrSong.VICTORY;
    }

    public void pauseCurrMusic()
    {
        currMusic.pause();
    }

    public void stopCurrMusic()
    {
        currMusic.stop();
    }

    public void pauseSfx(){ducks.pause();chomp.pause();}

    public void resumeSfx(){ducks.resume();chomp.resume();}

    public void stopSfx(){ducks.stop();chomp.stop();}

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

    public void mute() {setMusicVol(0); setSfxVol(0); isMuted = true;}

    public void unmute() {setMusicVol(Options.getMusicVol()); setSfxVol(Options.getSfxVol()); isMuted = false;}
}

package com.mygdx.tools;

import com.badlogic.gdx.audio.Music;
import com.mygdx.game.MyGdxGame;

public class MusicLoader {
    private Music music;

    public MusicLoader(String path){
        music = MyGdxGame.assetManager.get(path, Music.class);
        music.setLooping(true);
    }
   
    /**
     * this starts, stops or pause the generall music within the game
     * @param startStopPause -1 stop, 0 pause, 1 play
     */
    public void playMusic(int startStopPause){
        if(startStopPause == -1){
            music.stop();
        } else if(startStopPause == 0){
            music.pause();
        } else if(startStopPause == 1){
            music.play();
        }
    }

    /**
     * 
     * @param path
     */
    public void loadMusic(String path){
        music.stop();
        music = MyGdxGame.assetManager.get(path, Music.class);
    }

    /**
     * converts the input from a percentage to a float between 0 and 1 
     * @param vol set the volume in percent, 0 being silent and 100 being max volume
     */
    public void setVolume(int vol){
        if(vol >= 0 && vol <= 100){
            music.setVolume(vol/100f);
        }
    }
}

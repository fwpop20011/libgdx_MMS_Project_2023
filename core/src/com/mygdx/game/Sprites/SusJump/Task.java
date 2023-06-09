package com.mygdx.game.Sprites.SusJump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;

public class Task extends Platform {
    private SusJump screen;

    public Task(SusJump screen, Rectangle bounds) {
        super(screen, bounds);
        setCategoryFilter(MyGdxGame.COIN_BIT);
        fixture.setSensor(true);;
        fixture.setUserData(this);
        this.screen = screen;
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("DurablePlatform", "Collect") ;
        MyGdxGame.assetManager.get("assets/audio/sounds/coin.wav", Sound.class).play(0.5F);
        setCategoryFilter(MyGdxGame.DESTROYED_BIT);
        getCell().setTile(null);
        screen.getHud().addPoint(1000);
    }

    @Override
    public void onLandOn() {
        onHeadHit();
    }
}


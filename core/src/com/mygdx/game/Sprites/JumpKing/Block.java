package com.mygdx.game.Sprites.JumpKing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.JumpKing;

public class Block extends Jumpable {

    public Block(JumpKing screen, Rectangle bounds) {
        super(screen, bounds);
        setCategoryFilter(MyGdxGame.BRICK_BIT);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision") ;
        setCategoryFilter(MyGdxGame.DESTROYED_BIT);
        MyGdxGame.assetManager.get("assets/audio/sounds/breakblock.wav", Sound.class).play();
        getCell().setTile(null);
    }
}

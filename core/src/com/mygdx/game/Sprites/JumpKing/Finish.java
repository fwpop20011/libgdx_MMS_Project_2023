package com.mygdx.game.Sprites.JumpKing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.ScreenCoed;

public class Finish extends Jumpable {

    public Finish(ScreenCoed screen, Rectangle bounds) {
        super(screen, bounds);
        setCategoryFilter(MyGdxGame.PIPE_BIT);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Finish", "Collision");
    }
}

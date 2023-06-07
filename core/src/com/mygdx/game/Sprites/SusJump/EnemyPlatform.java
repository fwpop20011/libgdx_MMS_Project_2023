package com.mygdx.game.Sprites.SusJump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;

public class EnemyPlatform extends Platform{

    public EnemyPlatform(SusJump screen, Rectangle bounds) {
        super(screen, bounds, 0.0F);
        setCategoryFilter(MyGdxGame.FLOOR_BIT);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("EnemyPlatform", "Death") ;
        setCategoryFilter(MyGdxGame.FLOOR_BIT);
    }

    @Override
    public void onLandOn() {
        onHeadHit();
    }
}

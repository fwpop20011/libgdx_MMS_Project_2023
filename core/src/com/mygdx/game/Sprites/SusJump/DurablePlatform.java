package com.mygdx.game.Sprites.SusJump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;

public class DurablePlatform extends Platform{

    private SusJump screen;
    private boolean first;

    public DurablePlatform(SusJump screen, Rectangle bounds, float restitution) {
        super(screen, bounds, restitution);
        setCategoryFilter(MyGdxGame.BRICK_BIT);
        fixture.setUserData(this);
        this.first = true;
        this.screen = screen;
    }

    public DurablePlatform(SusJump screen, Rectangle bounds) {
        this(screen, bounds, 1.5F);
    }

    @Override
    public void onHeadHit() {
        //TODO PassThrough
        if(first){
            first = false;
            screen.getHud().addPoint(50);
        }
        Gdx.app.log("DurablePlatform", "JumpOn") ;
    }

    @Override
    public void onLandOn() {
        Gdx.app.log("DurablePlatform", "JumpOn") ;
        setCategoryFilter(MyGdxGame.ENEMY_HEAD_BIT);
    }
}

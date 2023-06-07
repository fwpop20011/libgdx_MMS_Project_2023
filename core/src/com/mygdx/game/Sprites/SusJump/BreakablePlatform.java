package com.mygdx.game.Sprites.SusJump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;

public class BreakablePlatform extends Platform{

    private SusJump screen;

    public BreakablePlatform(SusJump screen, Rectangle bounds, float restitution) {
        super(screen, bounds, restitution);
        setCategoryFilter(MyGdxGame.BRICK_BIT);
        fixture.setUserData(this);
        this.screen = screen;
    }

    public BreakablePlatform(SusJump screen, Rectangle bounds) {
        this(screen, bounds, 1.5F);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("DurablePlatform", "Break") ;
        setCategoryFilter(MyGdxGame.DESTROYED_BIT);
        getCell().setTile(null);
        if(getCellRight() != null){
            getCellRight().setTile(null);
        }
        if(getCellLeft() != null){
            getCellLeft().setTile(null);
        }
        screen.getHud().addPoint(200);
    }

    @Override
    public void onLandOn() {
        onHeadHit();
    }
}

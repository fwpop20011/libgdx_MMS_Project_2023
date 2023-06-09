package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.MarioScreen;

public abstract class Enemy extends Sprite{
    protected World world;
    protected MarioScreen screen;
    public Body body;

    public Enemy(MarioScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }

    public abstract void hitOnHead();

    public abstract void hitObject();

    protected abstract void defineEnemy();
}

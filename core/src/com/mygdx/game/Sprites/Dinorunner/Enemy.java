package com.mygdx.game.Sprites.Dinorunner;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.Dinorunner;

public abstract class Enemy extends Sprite{
    protected World world;
    protected Dinorunner screen;
    public Body body;

    public Enemy(Dinorunner screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }

    public abstract void hitOnHead();

    public abstract void hitObject();

    protected abstract void defineEnemy();
}

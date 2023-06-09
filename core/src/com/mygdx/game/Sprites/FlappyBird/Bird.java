package com.mygdx.game.Sprites.FlappyBird;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;

public class Bird {
    private static final int GRAVITY = -15;
    private Vector2 position;
    private Vector2 velocity;
    private Texture birdTexture;
    private Rectangle bounds;

    public Bird(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        birdTexture = MyGdxGame.assetManager.get("assets/sprites/FlappyBird/bird.png", Texture.class);
        //make a smaller hitbox so that it feels mor natural when getting hit
        bounds = new Rectangle(x, y, birdTexture.getWidth() - 2, birdTexture.getHeight() - 2);
    }

    public void move(float dt){
        if(position.y > 0)
            velocity.add(0, GRAVITY);

        velocity.scl(dt);
        position.add(0, velocity.y);

        if(position.y < 0)
            position.y = 0;

        velocity.scl(dt > 0 ? 1/dt : 0);

        bounds.setPosition(position.x, position.y);
    }

    public void jump(){
        velocity.y = 450;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return birdTexture;
    }

    public void dispose() {
        birdTexture.dispose();
    }
}
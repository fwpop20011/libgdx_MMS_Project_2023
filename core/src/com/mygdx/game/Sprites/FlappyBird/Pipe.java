package com.mygdx.game.Sprites.FlappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;

public class Pipe {
    public static final int PIPE_WIDTH = 80;
    private static final int MIN_PIPE_GAP = 150;
    private static final int MAX_PIPE_GAP = 200;
    private Rectangle upperPipe, lowerPipe;
    private Texture topPipeTexture, bottomPipeTexture;

    public Pipe() {
        topPipeTexture = MyGdxGame.assetManager.get("assets/sprites/FlappyBird/pipes_up.png", Texture.class);
        bottomPipeTexture = MyGdxGame.assetManager.get("assets/sprites/FlappyBird/pipes_down.png", Texture.class);

        int screenHeight = Gdx.graphics.getHeight();
        int gapPosition = MathUtils.random(MIN_PIPE_GAP, screenHeight - MAX_PIPE_GAP); // Randomly select the gap position
        int gapSize = MathUtils.random(MIN_PIPE_GAP, MAX_PIPE_GAP);

        upperPipe = new Rectangle();
        upperPipe.x = Gdx.graphics.getWidth();
        upperPipe.y = gapPosition + gapSize; // Position of the upper pipe should be at the end of the gap
        upperPipe.width = PIPE_WIDTH;
        upperPipe.height = screenHeight - upperPipe.y;

        lowerPipe = new Rectangle();
        lowerPipe.x = Gdx.graphics.getWidth();
        lowerPipe.y = 0;
        lowerPipe.width = PIPE_WIDTH;
        lowerPipe.height = gapPosition;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(upperPipe) || player.overlaps(lowerPipe);
    }

    public void move(){
        upperPipe.x -=5;
        lowerPipe.x -=5;
    }

    public Rectangle getTopRectangle() {
        return upperPipe;
    }

    public Rectangle getBottomRectangle() {
        return lowerPipe;
    }

    public Texture getTopTexture() {
        return topPipeTexture;
    }

    public Texture getBottomTexture() {
        return bottomPipeTexture;
    }

    public void dispose() {
        topPipeTexture.dispose();
        bottomPipeTexture.dispose();
    }
}
package com.mygdx.game.Sprites.BouncingBall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddel {
    private int x, y;
    private int xSize, ySize;

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Paddel(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        x = Gdx.graphics.getWidth() / 2;
        y = 10;
    }

    public void update() {
        x = Gdx.input.getX(); // x pos of mouse pointer
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(Color.WHITE);
        shape.rect(x, y, xSize, ySize);
    }

    public void resize(int width, int height) {

        ySize = height / 40;

        xSize = width / 20;

        x = width / 2;
        y = ySize + 5;
    }
}

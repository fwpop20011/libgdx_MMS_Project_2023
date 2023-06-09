package com.mygdx.game.Sprites.BouncingBall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddel {
    private int x, y;
    private int xSize, ySize;
    private boolean conSwitch;

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
        y = 30;
        conSwitch = false;
    }

    public void update() {
        if(conSwitch){
            x = Gdx.input.getX(); // x pos of mouse pointer
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if(x>= Gdx.graphics.getWidth() - xSize){
                    x = Gdx.graphics.getWidth() - xSize;
                } else {
                    x = x + 10;
                }
            } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if(x <= 0){
                    x = 0;
                } else {
                     x = x - 10;
                }
            } 
        }

        //when right shift is pressed switch between mouse an key input
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
            conSwitch = !conSwitch;
        }
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

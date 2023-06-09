package com.mygdx.game.Sprites.BouncingBall;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Scenes.Hud;

public class Ball {
    public boolean hitBottom;
    private Color color;
    private int x, y;
    private int size;
    private int xSpeed, ySpeed;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        hitBottom = false;
        this.color = new Color(Color.BLUE);
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;
        if (x < size || x > (Gdx.graphics.getWidth() - size)) {
            xSpeed = -xSpeed;
        }
        if (y < size || y > (Gdx.graphics.getHeight() - size)) {
            if (y < size) {
                hitBottom = true;
            }
            ySpeed = -ySpeed;
        }
    }

    public boolean checkCollision(Paddel paddel) {
        if (collidesWith(paddel)) {
            this.color = Color.GREEN;
            ySpeed = -ySpeed;
            return true;
        } else {
            this.color = Color.BLUE;
            return false;
        }
    }

    private boolean collidesWith(Paddel paddel) {
        if (y - 35 <= paddel.getY() && y > 20) {
            if (x >= paddel.getX() && x <= paddel.getX() + 30) {
                return true;
            }
        }

        return false;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, size);
    }

    private Color randColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b, 255);
    }

    public void resize(int width, int height) {
        if (width > height) {
            size = height / 40;
        } else {
            size = width / 40;
        }
        x = 300;
        y = 400;
        xSpeed = width / 200;
        ySpeed = height / 200;
    }

    public boolean hitBottom() {
        return hitBottom;
    }
}
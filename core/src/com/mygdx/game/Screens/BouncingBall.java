package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.BouncingBall.Ball;
import com.mygdx.game.Sprites.BouncingBall.Paddel;

public class BouncingBall implements Screen {
    Hud hud;

    private ShapeRenderer shape;
    private Game game;
    private Ball ball;
    private Paddel paddel;

    public BouncingBall(MyGdxGame game) {
        this.game = game;

        hud = new Hud(game.batch);
        hud.reName("BouncingBall", "", "Points");
        ball = new Ball(5, 30, 5, 2, 2);
        paddel = new Paddel(30, 5);
        this.shape = new ShapeRenderer();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        ball.update();
        ball.draw(shape);
        if(ball.checkCollision(paddel)){
            hud.addPoint(1);
        }

        //if the ball hits the bottom of the screen lose 100 points
        if(ball.hitBottom){
            hud.addPoint(-100);
            ball.hitBottom = false;
        }

        paddel.update();
        paddel.draw(shape);
        shape.end();

        hud.updateTimeAdditive(delta);
        hud.stage.draw();

        if(hud.getScore() < 0){
            game.setScreen(new GameOverScreen(game, 2));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        ball.resize(width, height);
        paddel.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        hud.dispose();
        shape.dispose();
        game.dispose();
    }
}

package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private int miniGameIndex;

    private Game game;

    public GameOverScreen(Game game, int miniGameIndex) {
        this.game = game;
        this.miniGameIndex = miniGameIndex;

        viewport = new FitViewport(MyGdxGame.V_Height, MyGdxGame.V_Width, new OrthographicCamera());
        stage = new Stage(viewport, ((MyGdxGame) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("game over", font);
        Label playLabel = new Label("Click to respawn", font);
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playLabel).expandX().padTop(5);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            switch (miniGameIndex) {
                //mario game
                case 0:
                    game.setScreen(new PlayScreen((MyGdxGame) game));
                    dispose();
                    break;
                //flappy bird
                case 3:
                    game.setScreen(new FlappyBird((MyGdxGame) game));
                    dispose();
                    break;
                
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        stage.dispose();
        game.dispose();
    }
}

package com.mygdx.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;


public class Hud implements Disposable {
    private static final int padding = 2;
    
    public Stage stage;
    private Viewport viewport;

    private Integer wTimer;
    private float timeCount;
    private Integer score;
    private String world = "Level Select";
    private String level = "0-0";
    private String playerName = "Steve";

    Label wTimerLabel;
    Label scoreLabel;
    Label timLabel;
    Label levLabel;
    Label worldLabel;
    Label playerLabel;

    public Hud(SpriteBatch sb, float width, float height) {
        viewport = new FitViewport(width, height, new OrthographicCamera(width, height));
        stage = new Stage(viewport, sb);
        createHud(stage);
    }

    public Hud(SpriteBatch sb){
        viewport = new FitViewport(MyGdxGame.V_Width, MyGdxGame.V_Height, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        wTimer = 0;
        timeCount = 0;
        score = 0;
        createHud(stage);
    }

    private void createHud(Stage stage){
        Table table = new Table();
        table.top();
        table.setFillParent(true); //set table to the size of the stage

        wTimerLabel = new Label(String.format("%4d", wTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel =  new Label(world, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levLabel = new Label(level, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel = new Label("playerName", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(playerLabel).expandX().padTop(padding);
        table.add(worldLabel).expandX().padTop(padding);
        table.add(timLabel).expandX().padTop(padding);
        table.row(); //everything below this will be in a new row
        table.add(scoreLabel).expandX().padTop(padding);
        table.add(levLabel).expandX().padTop(padding);
        table.add(wTimerLabel).expandX().padTop(padding);

        stage.addActor(table);
    }

    public void addPoint(int points){
        score += points;
        scoreLabel.setText(String.format("%06d", score));
    }

    public void reName(String worldName, String levelName, String playerName){
        worldLabel.setText(worldName);
        levLabel.setText(levelName);
        playerLabel.setText(playerName);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.SusJump.Jumper;
import com.mygdx.game.Sprites.SusJump.Tentacle;
import com.mygdx.tools.MusicLoader;
import com.mygdx.tools.SusJump.SusContactListener;
import com.mygdx.tools.SusJump.SusWorldCreator;

public class SusJump implements Screen{

    private MyGdxGame game;
    private TextureAtlas atlas;
    private Texture textureRight;
    private Texture textureLeft;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    // Varibels for tiled maps
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Varibels for Box2d
    private Box2DDebugRenderer b2dr;
    private World world;

    // sprites
    private Jumper player;
    Array<Tentacle> tentacles;

    // music
    MusicLoader musicLoader;

    //Level-Key
    private int level;

    public SusJump(MyGdxGame game){
        this(game, 1, 0);
    }
    public SusJump(MyGdxGame game, int level){
        this(game, level, 0);
    }
    public SusJump(MyGdxGame game, int level, int score){
        this(game, level, 0, 0);
    }

    public SusJump(MyGdxGame game, int level, int score, int worldTime){
        this.game = game;

        //TODO add
        textureRight = new Texture("assets/worlds/SusJump/GFX/sus_player.png");
        textureLeft = new Texture("assets/worlds/SusJump/GFX/sus_playerF.png");
        atlas = new TextureAtlas("assets/worlds/SusJump/GFX/tentacle.pack");
        // camera which follows the player.
        gameCam = new OrthographicCamera();

        // creates a viewport to keep the aspect ration and other objects hidden, that
        // are not meant to be seen.
        gamePort = new FitViewport(MyGdxGame.V_Width, MyGdxGame.V_Height, gameCam);

        // creates our game Hud
        hud = new Hud(game.batch);
        hud.reName("SuS-Jump", null, "Score");
        Gdx.app.log("timeSet", "time:" + worldTime);
        hud.addTimeCount(worldTime);
        // load the map created with tiled
        mapLoader = new TmxMapLoader();

        loadMap(level);
        renderer = new OrthogonalTiledMapRenderer(map);

        // set the game camera to be centered at the start of the level
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -5 * MyGdxGame.PPM), true);
        b2dr = new Box2DDebugRenderer();

        // generates a new map with objects
        SusWorldCreator susWorldCreator = new SusWorldCreator(this);
        tentacles = susWorldCreator.getTentacles();

        // create the sprite in the game world
        player = new Jumper(this);
        //tentacles.add(new Tentacle(this, 64, 32));

        world.setContactListener(new SusContactListener());

        // world music
        musicLoader = new MusicLoader("assets/audio/music/susJump.mp3");
        musicLoader.setVolume(20);
        musicLoader.playMusic(1);
        this.getHud().addPoint(score);
    }

    /**
     * Loads the assets of the level with the designated level key
     * @param level The level key
     */
    private void loadMap(int level) {
        switch(level){
            case 0:
            case 1:
            map = mapLoader.load("assets/worlds/susJump/levels/level1.tmx");
            break;
            case 2:
            map = mapLoader.load("assets/worlds/susJump/levels/level2.tmx");
            break;
            case 3:
            map = mapLoader.load("assets/worlds/susJump/levels/level3.tmx");
            break;
            default:
            map = mapLoader.load("assets/worlds/susJump/levels/level1.tmx");
            break;
        }
    }

    public int getLevel(){
        return this.level;
    }

    public void update(float deltaT) {
        gameCam.position.x = player.body.getPosition().x;
        gameCam.position.y = player.body.getPosition().y;

        // calculates the physics every 60 seconds
        world.step(1 / 60f, 6, 2);

        player.update(deltaT);

        for (Tentacle tentacle : tentacles) {
            tentacle.update(deltaT);
        }

        //world time 
        hud.updateTimeAdditive(deltaT);

        gameCam.update();
        // renders what the game camera can see
        renderer.setView(gameCam);
    }


    @Override
    public void show() {
        //Empty
    }

    @Override
    public void render(float delta) {
        update(delta);

        // clears the scren
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // renders the game map
        renderer.render();

        // render Box2dDebugLines
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);

        for (Tentacle tentacle : tentacles) {
            tentacle.draw(game.batch);
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        NextLevel();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        //Empty
    }

    @Override
    public void resume() {
        //Empty
    }

    @Override
    public void hide() {
        //Empty
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        game.dispose();
        Gdx.app.log("dispose", "Disposed");
    }

    
    public Texture getTextureRight() {
        return textureRight;
    }

    public Texture getTextureLeft() {
        return textureLeft;
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Hud getHud() {
        return hud;
    }

    public void NextLevel() {
        // Game overScreen
        if (player.isPlayerDead()) {
            MyGdxGame.assetManager.get("assets/audio/sounds/SusJump/death.mp3", Sound.class).play(0.5F);
            musicLoader.playMusic(-1);
            game.setScreen(new GameOverScreen(game, 4));
            dispose();
        }

        // other levels
        if (player.nextLevel()) {
            Double randomNum = Math.random();
            Gdx.app.log("nextLevel", "Random Number:" + randomNum);
            Gdx.app.log("timeGet", "time:" + getHud().getWorldTimer());
            if(randomNum < 0.2){ //20% chance
                Gdx.app.log("nextLevel", "SusJumpLevel1");
                    musicLoader.playMusic(0);
                    Gdx.app.log("score", "Went to next level with points:" + getPoints());
                    game.setScreen(new SusJump(game, 1, getPoints(), getHud().getWorldTimer()));
                    dispose();
            } else if(randomNum < 0.6){//40% chance
                Gdx.app.log("nextLevel", "SusJumpLevel2");
                    musicLoader.playMusic(0);
                    Gdx.app.log("score", "Went to next level with points:" + getPoints());
                    game.setScreen(new SusJump(game, 2, getPoints(), getHud().getWorldTimer()));
                    dispose();
            } else{ //40% chance
                Gdx.app.log("nextLevel", "SusJumpLevel3");
                    musicLoader.playMusic(3);
                    Gdx.app.log("score", "Went to next level with points:" + getPoints());
                    game.setScreen(new SusJump(game, 3, getPoints(), getHud().getWorldTimer()));
                    dispose();
            }
             
        }
    }

    public int getPoints(){
        return this.getHud().getScore();
    }
}

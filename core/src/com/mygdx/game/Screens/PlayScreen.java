package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Goomba;
import com.mygdx.game.Sprites.Player;
import com.mygdx.tools.B2WorldCreator;
import com.mygdx.tools.MusicLoader;
import com.mygdx.tools.WorldContactListener;

public class PlayScreen implements Screen {
    private MyGdxGame game;
    private TextureAtlas atlas;

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

    //sprites
    private Player player;
    private Goomba goomba;

    //music
    MusicLoader musicLoader;

    public PlayScreen(MyGdxGame game) {
        this.game = game;

        atlas = new TextureAtlas("assets/MarioAndEnemies.pack");
        // camera which follows the player.
        gameCam = new OrthographicCamera();

        // creates a viewport to keep the aspect ration and other objects hidden, that
        // are not meant to be seen.
        gamePort = new FitViewport(MyGdxGame.V_Width, MyGdxGame.V_Height, gameCam);

        // creates our game Hud
        hud = new Hud(game.batch);

        // load the map created with tiled
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("assets/worlds/marioRecreate/1/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        // set the game camera to be centered at the start of the level
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -7 * MyGdxGame.PPM), true);
        b2dr = new Box2DDebugRenderer();

        //generates a new map
        new B2WorldCreator(this); 

        // create the sprite in the game world
        player = new Player(this);
        goomba = new Goomba(this, 64, 32);

        world.setContactListener(new WorldContactListener());

        //world music
        musicLoader = new MusicLoader("assets/audio/music/mario_music.ogg");
        musicLoader.setVolume(10);
        musicLoader.playMusic(1);
    }

    

    public void update(float deltaT) {
        player.deviceInput(deltaT);

        gameCam.position.x = player.body.getPosition().x;

        // calculates the physics every 60 seconds
        world.step(1 / 60f, 6, 2);

        player.update(deltaT);
        goomba.update(deltaT);

        gameCam.update();
        // renders what the game camera can see
        renderer.setView(gameCam);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void show() {

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
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        goomba.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}

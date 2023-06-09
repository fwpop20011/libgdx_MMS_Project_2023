package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.mygdx.game.Sprites.Goomba;
import com.mygdx.game.Sprites.Player;
import com.mygdx.tools.B2WorldCreator;
import com.mygdx.tools.KeyGen;
import com.mygdx.tools.MusicLoader;
import com.mygdx.tools.WorldContactListener;

public class MarioScreen implements Screen {
    private MyGdxGame game;
    private TextureAtlas atlas;

    private OrthographicCamera cam;
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
    private Player player;
    Array<Goomba> goombas;

    // music
    MusicLoader musicLoader;

    public MarioScreen(MyGdxGame game) {
        this.game = game;

        atlas = new TextureAtlas("assets/MarioAndEnemies.pack");
        // camera which follows the player.
        cam = new OrthographicCamera();

        // creates a viewport to keep the aspect ration and other objects hidden, that
        // are not meant to be seen.
        gamePort = new FitViewport(MyGdxGame.V_Width, MyGdxGame.V_Height, cam);

        // creates our game Hud
        hud = new Hud(game.batch);

        // load the map created with tiled
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("assets/worlds/marioRecreate/1/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        // set the game camera to be centered at the start of the level
        cam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -8 * MyGdxGame.PPM), true);
        b2dr = new Box2DDebugRenderer();

        // generates a new map with objects
        B2WorldCreator b2WorldCreator = new B2WorldCreator(this);
        goombas = b2WorldCreator.getGoombas();

        // create the sprite in the game world
        player = new Player(this);
        goombas.add(new Goomba(this, 100, 32));

        world.setContactListener(new WorldContactListener());

        // world music
        musicLoader = new MusicLoader("assets/audio/music/mario_music.ogg");
        musicLoader.setVolume(20);
        musicLoader.playMusic(1);
    }

    public void update(float deltaT) {
        cam.position.x = player.body.getPosition().x;

        // calculates the physics every 60 seconds
        world.step(1 / 60f, 6, 2);

        player.update(deltaT);
        for (Goomba goomba : goombas) {
            goomba.update(deltaT);
        }

        cam.update();
        // renders what the game camera can see
        renderer.setView(cam);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
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
        b2dr.render(world, cam.combined);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.draw(game.batch);

        for (Goomba goomba : goombas) {
            goomba.draw(game.batch);
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        KeyGen.reset(0);
    }

    public void NextLevel() {
        // Game overScreen
        if (player.isPlayerDead()) {
            MyGdxGame.assetManager.get("assets/audio/sounds/mariodie.wav", Sound.class).play();
            game.setScreen(new GameOverScreen(game, -1));
            dispose();
        }

        // other minigames
        if (player.nextLevel()) {
            musicLoader.playMusic(0);
            switch (player.getPlayerOnPipeKey()) {
                case 1:
                    Gdx.app.log("nextLevel", "Dinorunner");
                    game.setScreen(new Dinorunner(game));
                    dispose();
                    break;
                case 2:
                    Gdx.app.log("nextLevel", "BouncingBall");
                    game.setScreen(new BouncingBall(game));
                    dispose();
                    break;
                case 3:
                    Gdx.app.log("nextLevel", "FlappyBirds");
                    game.setScreen(new FlappyBird(game));
                    dispose();
                    break;
                case 4:
                    Gdx.app.log("nextLevel", "SusJump");
                    game.setScreen(new SusJump(game));
                    dispose();
                    break;
                case 5:
                    Gdx.app.log("nextLevel", "JumpKing");
                    game.setScreen(new JumpKing(game));
                    dispose();
                    break;
            }
        }
    }
}

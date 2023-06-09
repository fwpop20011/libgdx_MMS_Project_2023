package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

import com.mygdx.game.Sprites.PlayerKing;

import com.mygdx.tools.MusicLoader;
import com.mygdx.tools.JumpKing.JumpKingContactListener;
import com.mygdx.tools.JumpKing.JumpKingWorldCreator;

public class ScreenCoed implements Screen {

    private OrthographicCamera camera;

    private Hud hud;

    private TextureAtlas atlas;

    private PlayerKing player;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Viewport viewport;

    private MyGdxGame game;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TmxMapLoader mapLoader;
    private TiledMap map;

    private boolean onFinish;

    private MusicLoader musicLoader;

    // private TileMap tileMaphelper;

    public ScreenCoed(MyGdxGame game) {
        this.game = game;

        atlas = new TextureAtlas("assets/MarioAndEnemies.pack");

        camera = new OrthographicCamera();

        viewport = new FitViewport(MyGdxGame.V_Width, MyGdxGame.V_Height, camera);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("assets/worlds/JumpKing/jumpking.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -4 * MyGdxGame.PPM), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        JumpKingWorldCreator jumpKingWorldCreator = new JumpKingWorldCreator(this);
        world.setContactListener(new JumpKingContactListener());

        player = new PlayerKing(this);

        musicLoader = new MusicLoader("assets/audio/music/Jumper.mp3");
        musicLoader.setVolume(0);
        musicLoader.playMusic(1);

        hud.reName("jump king", "", "Points");
        // this.batch = new SpriteBatch();

        // this.tileMaphelper = new TileMap();
        // this.orthogonalTiledMapRenderer = tileMaphelper.setUpMap();
    }

    private void update(float deltaT) {
        camera.position.x = player.body.getPosition().x;
        camera.position.y = player.body.getPosition().y;
        world.step(1 / 60f, 6, 2);

        player.update(deltaT);

        //set the points to the heightest the player has been
        if (hud.getScore() < player.getY()) {
            hud.setPoints((int) player.getY());
        }

        hud.updateTimeAdditive(deltaT);

        camera.update();

        orthogonalTiledMapRenderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.render();

        box2DDebugRenderer.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();
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
        map.dispose();
        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        hud.dispose();
        game.dispose();
    }

    public void NextLevel() {
        // Game overScreen
        if (player.isPlayerDead()) {
            game.setScreen(new GameOverScreen(game, 0));
            dispose();
        }

        if (player.nextLevel()) {
            game.setScreen(new GameOverScreen(game, 0));
            dispose();
        }
    }
}

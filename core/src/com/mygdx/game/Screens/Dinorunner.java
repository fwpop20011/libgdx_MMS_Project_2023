package com.mygdx.game.Screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.Dinorunner.*;
import com.mygdx.tools.MusicLoader;
import com.mygdx.tools.DinoRunner.WorldContactListener;

public class Dinorunner implements Screen {
    MyGdxGame game;

    // textures
    private TextureAtlas atlas;
    private Sprite backgroundSprite;

    private SpriteBatch batch;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    // box2D
    private Box2DDebugRenderer b2dr;
    private World world;

    // Sprites
    private Runner runner;
    private Array<Goomba> goombas;
    private Array<Bird> birds;
    private Array<Turtle> turtles;

    // Music
    MusicLoader musicLoader;

    public Dinorunner(MyGdxGame game) {
        this.game = game;
        Texture backgroundTexture = new Texture("assets/worlds/Dinorunner/illustration-4908159_960_720.jpg");
        backgroundSprite = new Sprite(backgroundTexture);
        Gdx.app.log("screenSize", String.format("x:%d y:%d", Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        atlas = new TextureAtlas("assets/MarioAndEnemies.pack");
        // camera which follows the player.
        gameCam = new OrthographicCamera();

        // creates a viewport
        gamePort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gameCam);

        // set the game camera to be centered at the start of the level
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // set the world physics
        world = new World(new Vector2(0, -35), true);
        b2dr = new Box2DDebugRenderer();

        // create the sprite in the game world
        runner = new Runner(this);
        goombas = new Array<>();
        birds = new Array<>();
        turtles = new Array<>();

        birds.add(new Bird(this, 1200, 8));
        goombas.add(new Goomba(this, 800, 32));
        turtles.add(new Turtle(this, 400, 8, 1));
        birds.add(new Bird(this, 1600, 8));
        spawnRandom();
        new WorldGen(this);

        world.setContactListener(new WorldContactListener());

        musicLoader = new MusicLoader("assets/audio/music/Jumper.mp3");
        musicLoader.setVolume(10);
        musicLoader.playMusic(1);
    }

    @Override
    public void show() {

    }

    private void update(float delta) {
        world.step(1 / 60f, 6, 2);
        runner.update(delta);

        // upade the goombas (posistion)
        for (Goomba goomba : goombas) {
            goomba.update(delta);
            // if the goomba is outside of the border it will be deleted
            if (goomba.getX() < -50) {
                goombas.removeValue(goomba, false);
                goomba.remove();
                spawnRandom();
            }
        }

        // update the birds (posistion)
        for (Bird bird : birds) {
            bird.update(delta);
            // if the bird is outside of the border it will be deleted
            if (bird.getX() < -50) {
                birds.removeValue(bird, false);
                bird.remove();
                spawnRandom();
            }
        }

        for (Turtle turtle : turtles) {
            turtle.update(delta);
            // if the bird is outside of the border it will be deleted
            if (turtle.getX() < -50) {
                turtles.removeValue(turtle, false);
                turtle.remove();
                spawnRandom();
            }
        }

        if (runner.isPlayerDead()) {
            musicLoader.playMusic(-1);
            MyGdxGame.assetManager.get("assets/audio/sounds/mariodie.wav", Sound.class).play();
            game.setScreen(new GameOverScreen(game, 1));
        }

        gameCam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render Box2dDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        backgroundSprite.draw(game.batch);

        runner.draw(game.batch);

        for (Goomba goomba : goombas) {
            goomba.draw(game.batch);
        }

        for (Bird bird : birds) {
            bird.draw(game.batch);
        }

        for (Turtle turtle : turtles) {
            turtle.draw(game.batch);
        }

        game.batch.end();

        // calculates the physics every 60 seconds
        world.step(1 / 60f, 6, 2);
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
        world.dispose();
        backgroundSprite.getTexture().dispose();
        batch.dispose();
        atlas.dispose();
        runner.dispose();
        game.dispose();
    }

    /**
     * This is need to add objects
     * 
     * @return the world of the screen
     */
    public World getWorld() {
        return world;
    }

    /**
     * 
     * @return the textureAtlas with preloaded textures
     */
    public TextureAtlas getAtlas() {
        return atlas;
    }

    /** spawns a random sprite at x:1500 y:32 */
    private void spawnRandom() {
        Random random = new Random();
        int randNum = random.nextInt(3); // generates a random number between 0 and 3

        switch (randNum) {
            case 0:
                addGoomba(new Goomba((this), 2000, 32));
                Gdx.app.log("goomba", "new goomba added");
                break;
            case 1:
                addBird(new Bird(this, 2000, 32));
                Gdx.app.log("bird", "new bird added");
                break;
            case 2:
                Random random2 = new Random();
                randNum = random.nextInt(3); // generates a random number between 0 and 2
                Gdx.app.log("turtle", "new turtle added");
                addTurtle(new Turtle(this, 2000, 32, randNum));
                break;
            default:
                Gdx.app.log("Error", "nothing was added");
        }
    }

    /**
     * add a goomba to the array of goombas
     * 
     * @param goomba the goomba which will be added
     */
    private void addGoomba(Goomba goomba) {
        goombas.add(goomba);
    }

    /**
     * add a bird to the birds array
     * 
     * @param bird the bird which will be added
     */
    private void addBird(Bird bird) {
        birds.add(bird);
    }

    /**
     * add a bird to the birds array
     * 
     * @param turtle the turtle which will be added
     */
    private void addTurtle(Turtle turtle) {
        turtles.add(turtle);
    }

    public class WorldGen {
        World world;

        protected WorldGen(Dinorunner screen) {
            this.world = screen.getWorld();

            // floor
            defineBorder(MyGdxGame.FLOOR_BIT, new Rectangle(-100, 0, 3000, 1), 0, 0);

            // left world border
            defineBorder(MyGdxGame.WALL_BIT, new Rectangle(0, 0, 1, 1000), 0, 0);

            // right world border
            defineBorder(MyGdxGame.WALL_BIT, new Rectangle(Gdx.graphics.getWidth(), 0, 1, 1000), 0, 1000);
        }

        private void defineBorder(short bit, Rectangle rectangle, int x, int y) {
            BodyDef bDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fDef = new FixtureDef();
            Body body;

            Rectangle rect = rectangle;

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(rect.getX() + (rect.getWidth()) / 2, rect.getY() + (rect.getHeight() / 2));

            body = world.createBody(bDef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

            // id of the player for the contact listener
            fDef.filter.categoryBits = bit;
            // what can the player collid with;
            fDef.filter.maskBits = MyGdxGame.PLAYER_BIT | MyGdxGame.ENEMY_BIT;

            fDef.shape = shape;
            body.createFixture(fDef);
        }
    }
}

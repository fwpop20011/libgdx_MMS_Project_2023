package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.tools.MusicLoader;
import com.mygdx.tools.WorldContactListener;

public class Dinorunner implements Screen {
    Game game;

    public static Sprite backgroundSprite;

    SpriteBatch batch;

    OrthographicCamera cam;

    // box2D
    private Box2DDebugRenderer b2dr;
    private World world;

    // Music
    MusicLoader musicLoader;

    public Dinorunner(Game game) {
        this.game = game;
        Texture backgroundTexture = new Texture("assets/worlds/Dinorunner/illustration-4908159_960_720.jpg");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        world = new World(new Vector2(0, -7), true);
        b2dr = new Box2DDebugRenderer();

        batch = new SpriteBatch();

        world.setContactListener(new WorldContactListener());
        
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        b2dr.render(world, cam.combined);

        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();

        // calculates the physics every 60 seconds
        world.step(1 / 60f, 6, 2);

    }

    @Override
    public void resize(int width, int height) {

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
    }

    public World getWorld() {
        return world;
    }

    public class WorldGen {

        protected WorldGen(Dinorunner screen){
            World world = screen.getWorld();

            BodyDef bDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fDef = new FixtureDef();
            Body body;
        
            Rectangle rect = new Rectangle(10, 10, 1000, 1000);

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(0,0);
            bDef.position.set(rect.getX() + (rect.getWidth()) / 2, rect.getY() + (rect.getHeight() / 2));

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fDef.shape = shape;
            body.createFixture(fDef);
        }

    }
}
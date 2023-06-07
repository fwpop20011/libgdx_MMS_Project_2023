package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.Dinorunner.Goomba;
import com.mygdx.game.Sprites.Dinorunner.Runner;
import com.mygdx.tools.MusicLoader;
import com.mygdx.tools.WorldContactListener;

public class Dinorunner implements Screen {
    MyGdxGame game;

    //textures
    private TextureAtlas atlas;
    public static Sprite backgroundSprite;

    SpriteBatch batch;

    OrthographicCamera cam;

    // box2D
    private Box2DDebugRenderer b2dr;
    private World world;

    //Sprites
    Runner runner;
    Array<Goomba> goombas;

    // Music
    MusicLoader musicLoader;

    public Dinorunner(MyGdxGame game) {
        this.game = game;
        Texture backgroundTexture = new Texture("assets/worlds/Dinorunner/illustration-4908159_960_720.jpg");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        atlas = new TextureAtlas("assets/MarioAndEnemies.pack");

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        world = new World(new Vector2(0, -30), true);
        b2dr = new Box2DDebugRenderer();
        new WorldGen(this);
        batch = new SpriteBatch();
        runner = new Runner(this);

        world.setContactListener(new WorldContactListener());
        
        goombas = new Array<>();
        goombas.add(new Goomba(this, 64, 32));
    }

    @Override
    public void show() {

    }

    private void update(float delta){
        world.step(1 / 60f, 6, 2);
        runner.update(delta);

        for (Goomba goomba : goombas) {
            goomba.update(delta);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        b2dr.render(world, cam.combined);

        game.batch.begin();

        backgroundSprite.draw(game.batch);

        runner.draw(game.batch);

        for(Goomba goomba : goombas){
            goomba.draw(game.batch);
        }

        game.batch.end();

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
        atlas.dispose();
        runner.dispose();
    }

    public World getWorld() {
        return world;
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public class WorldGen {
        World world;

        protected WorldGen(Dinorunner screen){
            this.world = screen.getWorld();

            //floor
            defineBorder(MyGdxGame.FLOOR_BIT, new Rectangle(0, 0, 2000, 1), 0, 0);

            //left world border
            defineBorder(MyGdxGame.WALL_BIT, new Rectangle(0, 0, 1, 1000), 0, 0);

            //right world border
            defineBorder(MyGdxGame.WALL_BIT, new Rectangle(Gdx.graphics.getWidth(), 0, 1, 1000), 0 ,1000);
        }

        private void defineBorder(short bit, Rectangle rectangle, int x, int y){
            BodyDef bDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fDef = new FixtureDef();
            Body body;

            Rectangle rect = rectangle;

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(rect.getX() + (rect.getWidth()) / 2, rect.getY() + (rect.getHeight() / 2));
            
            body = world.createBody(bDef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

            //id of the player for the contact listener
            fDef.filter.categoryBits = bit;
            // what can the player collid with;
            fDef.filter.maskBits = MyGdxGame.PLAYER_BIT | MyGdxGame.ENEMY_BIT;

            fDef.shape = shape;
            body.createFixture(fDef);
        }

    }
}

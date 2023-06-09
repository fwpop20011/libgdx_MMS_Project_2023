package com.mygdx.game.Sprites.Dinorunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.Dinorunner;
import com.mygdx.game.Sprites.Player.State;

public class Runner extends Sprite {
    public State curState;
    public State prevState;
    public World world;
    public Body body;
    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private TextureRegion playerStand;
    private float stateTimer;
    private boolean isDead;

    public Runner(Dinorunner screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        curState = State.STANDING;
        prevState = State.STANDING;
        stateTimer = 0;

        isDead = false;

        Array<TextureRegion> textures = new Array<>();

        // load the running animation from the .pack
        for (int i = 1; i < 4; i++) {
            textures.add(new TextureRegion(getTexture(), i * 16, 11, 16, 16));
        }
        playerRun = new Animation<>(0.1f, textures);
        textures.clear();

        for (int i = 4; i < 6; i++) {
            textures.add(new TextureRegion(getTexture(), i * 16, 11, 16, 16));
        }

        playerJump = new Animation<>(0.3f, textures);
        textures.clear();

        defineRunner();
        playerStand = new TextureRegion(getTexture(), 0, 11, 16, 16);
        setBounds(0, 0, 100, 100);
        setRegion(playerStand);
    }


    public void update(float deltaT) {
        deviceInput(deltaT);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaT));
    }

    public TextureRegion getFrame(float deltaT) {
        curState = getState();

        TextureRegion region;
        switch (curState) {
            case JUMPING:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
                break;

            case RUNNING:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;

            case FALLING:
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        // does the current state equal the previous state if yes then the stateTimer +
        // the delta time else set to 0
        stateTimer = curState == prevState ? stateTimer + deltaT : 0;
        prevState = curState;

        return region;
    }

    /**
     * checks the velocity of the player, which is in turn used to calculate the current state of movement
     * @return the movement state the character is currently in
     */
    private State getState() {
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && prevState == State.JUMPING)) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    /**defines the body of the character sprite in the world */
    public void defineRunner() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(32, 32);
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50);
        //id of the player for the contact listener
        fDef.filter.categoryBits = MyGdxGame.PLAYER_BIT;
        // what can the player collid with;
        fDef.filter.maskBits = MyGdxGame.FLOOR_BIT | 
        MyGdxGame.WALL_BIT |
        MyGdxGame.ENEMY_BIT;

        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2, 7), new Vector2(2, 7));
        fDef.shape = head;
        fDef.isSensor = true;

        body.createFixture(fDef).setUserData("head");
    }

    /** read the inputs and translate them to player movement*/
    public void deviceInput(float deltaT) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && body.getLinearVelocity().y == 0) {
            body.applyLinearImpulse(new Vector2(0, 10 * MyGdxGame.PPM), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 40 * MyGdxGame.PPM) {
            body.applyLinearImpulse(new Vector2(0.2f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x >= -40 * MyGdxGame.PPM) {
            body.applyLinearImpulse(new Vector2(-0.2f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }
        if(body.getPosition().y < 0){
            playerDeath();
        }
    }

    /**set the character sprite to dead */
    public void playerDeath(){
        isDead = true;
    }

    /**
     * 
     * @return
     */
    public boolean isPlayerDead() {
        return isDead;
    }

    public void dispose(){
        world.dispose();
    }
}

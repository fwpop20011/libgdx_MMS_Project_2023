package com.mygdx.game.Sprites;

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
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Screens.ScreenCoed;

public class PlayerKing extends Sprite {
    public enum State {
        FALLING, JUMPING, STANDING, RUNNING
    }

    public State curState;
    public State prevState;
    public World world;
    public Body body;
    private TextureRegion playerStand;
    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private float stateTimer;
    private boolean runningRight;
    private boolean isDead;
    private boolean nextLevel;
    private boolean onTopOfPipe;

    public PlayerKing(ScreenCoed screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        curState = State.STANDING;
        prevState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        onTopOfPipe = false;
        nextLevel = false;

        Array<TextureRegion> textures = new Array<>();
        isDead = false;

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

        definePlayer();
        playerStand = new TextureRegion(getTexture(), 0, 11, 16, 16);
        setBounds(0, 0, 24, 24);
        setRegion(playerStand);
    }

    public void update(float deltaT) {
        deviceInput(deltaT);
        setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight()/2 );
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

        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        // does the current state equal the previous state if yes then the stateTimer +
        // the delta time else set to 0
        stateTimer = curState == prevState ? stateTimer + deltaT : 0;
        prevState = curState;

        return region;
    }

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

    public void definePlayer() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(200, 64);
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);
        fDef.filter.categoryBits = MyGdxGame.PLAYER_BIT;
        // what can the player collid with;
        fDef.filter.maskBits = MyGdxGame.DEFAULT |
                MyGdxGame.COIN_BIT |
                MyGdxGame.BRICK_BIT |
                MyGdxGame.PIPE_BIT |
                MyGdxGame.ENEMY_BIT |
                MyGdxGame.ENEMY_HEAD_BIT |
                MyGdxGame.PIPE_TOP_BIT;

        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2, 7), new Vector2(2, 7));
        fDef.shape = head;
        fDef.isSensor = true;

        body.createFixture(fDef).setUserData("head");
    }

    public void deviceInput(float deltaT) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && body.getLinearVelocity().y == 0) {
            body.applyLinearImpulse(new Vector2(0, 40 * MyGdxGame.PPM), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 6 * MyGdxGame.PPM) {
            body.applyLinearImpulse(new Vector2(0.2f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x >= -6 * MyGdxGame.PPM) {
            body.applyLinearImpulse(new Vector2(-0.2f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && onTopOfPipe){
            nextLevel = true;
        }
        if(body.getPosition().y < 0){
            playerDeath();
        }
    }

    /**
     * sets the player sprite to dead
     */
    public void playerDeath() {
        isDead = true;
    }

    /**
     * 
     * @return true if the player is dead and false if not
     */
    public boolean isPlayerDead() {
        return isDead;
    }

    public void onTopOfPipe(boolean boo){
        nextLevel = boo;
        Gdx.app.log("Player", "on finish: ");
    }

    public boolean nextLevel(){
        return nextLevel;
    }

    public void dispose(){
        world.dispose();
    }

    
}

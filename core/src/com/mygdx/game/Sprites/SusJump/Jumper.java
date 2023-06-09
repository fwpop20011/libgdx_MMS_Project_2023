package com.mygdx.game.Sprites.SusJump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;
import com.mygdx.game.Sprites.Player.State;

public class Jumper extends Sprite {
    public State curState;
    public State prevState;
    public World world;
    public Body body;
    private Texture playerRunLeft;
    private Texture playerRunRight;
    private float stateTimer;
    private boolean isDead;
    private boolean nextLevel;
    private boolean onPortal;

    public Jumper(SusJump susJump){
        //TODO move this to Tentacle.java
        //super(susJump.getAtlas().findRegion("tentacle"));
        super(susJump.getTextureRight());
        this.world = susJump.getWorld();

        curState = State.STANDING;
        prevState = State.STANDING;
        stateTimer = 0;

        isDead = false;
        nextLevel = false;
        onPortal = false;

        // load the running animation from the .pack
        //TODO move this to Tentacle.java
        /* 
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
        */

        playerRunLeft = susJump.getTextureRight();
        playerRunRight = susJump.getTextureLeft();


        definePlayer();
        setBounds(0, 0, playerRunRight.getWidth(), playerRunRight.getHeight());
        setRegion(playerRunRight);
    }


    public void update(float deltaT) {
        deviceInput(deltaT);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaT));
    }

    public Texture getFrame(float deltaT) {
        curState = getState();

        Texture texture;

        if (body.getLinearVelocity().x < 0) {
            texture = playerRunRight;
        } else if (body.getLinearVelocity().x > 0) {
            texture = playerRunLeft;
        } else{
            texture = playerRunLeft;
        }

        // does the current state equal the previous state if yes then the stateTimer +
        // the delta time else set to 0
        stateTimer = curState == prevState ? stateTimer + deltaT : 0;
        prevState = curState;

        return texture;
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
        bDef.position.set(340, 128);
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16);
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
            body.applyLinearImpulse(new Vector2(0, 20 * MyGdxGame.PPM), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 20 * MyGdxGame.PPM) {
            body.applyLinearImpulse(new Vector2(0.2f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x >= -20 * MyGdxGame.PPM) {
            body.applyLinearImpulse(new Vector2(-0.2f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && onPortal){
            MyGdxGame.assetManager.get("assets/audio/sounds/SusJump/vent.mp3", Sound.class).play(0.5F);
            nextLevel = true;
        }
        if(body.getPosition().y < 0){
            playerDeath();
        }
    }

    public void playerDeath(){
        isDead = true;
    }

    public boolean isPlayerDead() {
        return isDead;
    }

    public void dispose(){
        world.dispose();
    }

    public boolean nextLevel(){
        return nextLevel;
    }

    public void onTopOfPortal(boolean on){
       if(on){
        onPortal = true;
        Gdx.app.log("Player", "on portal");
       }else{
        onPortal = false;
        Gdx.app.log("Player", "off portal");
       }
    }
}

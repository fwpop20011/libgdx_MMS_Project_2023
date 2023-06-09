package com.mygdx.game.Sprites.Dinorunner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.Dinorunner;

public class Bird extends Enemy {
    public Body body;
    private float stateTimer;

    private boolean moveLeft;
    Texture birdTexture;

    public Bird(Dinorunner screen, float x, float y) {
        super(screen, x, y);

        moveLeft = true;

        birdTexture = MyGdxGame.assetManager.get("assets/sprites/FlappyBird/bird.png", Texture.class);

        stateTimer = 0;
        
        setBounds(0, 0, 60, 60);
        setRegion(birdTexture);
        flip(true, false);
    }

    public void update(float deltaT) {
        stateTimer += deltaT;

        move(deltaT);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30);
        fDef.filter.categoryBits = MyGdxGame.ENEMY_BIT;
        //what can the bird collid with;
        fDef.filter.maskBits = MyGdxGame.FLOOR_BIT |
        MyGdxGame.PLAYER_BIT;

        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);
    }

    /**
     * moves the bird left until the direction is switched
     * @param deltaT
     */
    public void move(float deltaT){
        if(stateTimer > 1){
            body.applyLinearImpulse(new Vector2(0, 50), body.getWorldCenter(), true);
            stateTimer = 0;
        } else {
            stateTimer += deltaT;
        }

        if(moveLeft && body.getLinearVelocity().x >= -1 * MyGdxGame.PPM){
            //move left
            body.applyLinearImpulse(new Vector2(-1f * MyGdxGame.PPM, 10), body.getWorldCenter(), true);
        } else if(!moveLeft && body.getLinearVelocity().x <= 1 * MyGdxGame.PPM){
            //move right
            body.applyLinearImpulse(new Vector2(1f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }
    }

    /** removes the body of this bird from the world it is in */
    public void remove(){
        world.destroyBody(body);
    }
}

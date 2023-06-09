package com.mygdx.game.Sprites.Dinorunner;

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

public class Turtle extends Enemy {
    public Body body;
    private float stateTimer;
    private Animation<TextureRegion> walkAnimation;
    private boolean moveLeft;
    Array<TextureRegion> textures = new Array<>();
    int ver;

    /**
     * 
     * @param screen 
     * @param x x posistion on the screen 
     * @param y y posistion on the screen
     * @param ver which version of the turle 0 is normal, 1 is flying, 2 is rolling
     */
    public Turtle(Dinorunner screen, float x, float y, int ver) {
        super(screen, x, y);
        this.ver = ver;

        moveLeft = true;
        int pos;
        switch(ver){
            default:
            case 0:
                pos = 339;
                break;
            case 1:
                pos = 371;
                break;
            case 2:
                pos = 403;
                break;
        }
        for (int i = 0; i < 2; i++) {
            textures.add(
                    new TextureRegion(screen.getAtlas().findRegion("turtle").getTexture(), pos + i * 16, 37, 16, 24));
        }

        walkAnimation = new Animation<>(0.3f, textures);

        textures.clear();
        stateTimer = 0;
        setBounds(0, 0, 70, 70);
        setRegion(walkAnimation.getKeyFrame(10));
    }

    public void update(float deltaT) {
        stateTimer += deltaT;

        move(deltaT);

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTimer, true));
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
        // what can the turtle collid with;
        fDef.filter.maskBits = MyGdxGame.FLOOR_BIT |
                MyGdxGame.PLAYER_BIT;

        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);
    }

    /**
     * moves the turtle left until the direction is switched
     * 
     * @param deltaT
     */
    public void move(float deltaT) {
        //check if it is the flying ver and add motion upwards
        if(ver == 1){
            if(stateTimer > 1){
                body.applyLinearImpulse(new Vector2(0, 50), body.getWorldCenter(), true);
                stateTimer = 0;
            } else {
                stateTimer += deltaT;
            }
        }
        
        if (moveLeft && body.getLinearVelocity().x >= -1 * MyGdxGame.PPM) {
            // move left
            body.applyLinearImpulse(new Vector2(-1f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        } else if (!moveLeft && body.getLinearVelocity().x <= 1 * MyGdxGame.PPM) {
            // move right
            body.applyLinearImpulse(new Vector2(1f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }
    }

    /** removes the body of this turtle from the world it is in */
    public void remove(){
        world.destroyBody(body);
    }
}

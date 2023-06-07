package com.mygdx.game.Sprites.SusJump;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;

public class Tentacle extends Sprite{

    protected World world;
    protected SusJump screen;
    public Body body;

    private Animation<TextureRegion> walkAnimation;
    private float stateTimer;
    private boolean moveLeft;
    Array<TextureRegion> textures = new Array<TextureRegion>();


    public Tentacle(SusJump screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();

        //TODO decide how to move enemy
        moveLeft = true;

        for (int i = 0; i <= 5; i++) {
            textures.add(new TextureRegion(screen.getAtlas().findRegion("tentacle").getTexture(), i *32 , 0, 32, 32));
        }

        walkAnimation = new Animation<>(0.3f, textures);
        
        textures.clear();
        stateTimer = 0;
    }

    public void update(float deltaT) {
        stateTimer += deltaT;

        move(deltaT);

        //setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //TODO dont change y position
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y); 
        setRegion(((TextureRegion) walkAnimation.getKeyFrame(stateTimer, true)));
    }

    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16);
        fDef.filter.categoryBits = MyGdxGame.ENEMY_BIT;
        //what can the tentacle collid with;
        //TODO Wall
        fDef.filter.maskBits = MyGdxGame.DEFAULT | 
        MyGdxGame.COIN_BIT | 
        MyGdxGame.BRICK_BIT |
        MyGdxGame.PLAYER_BIT |
        MyGdxGame.PIPE_BIT |
        MyGdxGame.ENEMY_BIT;

        fDef.shape = shape;
        body.createFixture(fDef).setUserData(this);

        PolygonShape head = new PolygonShape(); 
        Vector2[] vertors = {new Vector2(-6, 9), new Vector2(6, 9), new Vector2(-3, 3), new Vector2(3, 3)};
        head.set(vertors);

        fDef.shape = head;
        fDef.restitution = 0.5f;
        fDef.filter.categoryBits = MyGdxGame.ENEMY_BIT;
        
        body.createFixture(fDef).setUserData(this);
        setBounds(getX(), getY(), 16, 16);
    }

    /**
     * moves the tentacle left until the direction is switched
     * @param deltaT
     */
    public void move(float deltaT){
        if(moveLeft && body.getLinearVelocity().x >= -0.5 * MyGdxGame.PPM){
            //move left
            body.applyLinearImpulse(new Vector2(-1f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        } else if(!moveLeft && body.getLinearVelocity().x <= 0.5 * MyGdxGame.PPM){
            //move right
            body.applyLinearImpulse(new Vector2(1f * MyGdxGame.PPM, 0), body.getWorldCenter(), true);
        }
    }

    public void hitObject() {
        moveLeft = !moveLeft;
    }

}

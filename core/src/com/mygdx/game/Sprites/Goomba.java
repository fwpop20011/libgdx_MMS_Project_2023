package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.MarioScreen;

public class Goomba extends Enemy {
    private float stateTimer;
    private Animation<TextureRegion> walkAnimation;
    private boolean isDead;
    private boolean isDestroyed;
    private boolean disapear;
    private float deathTime;
    private boolean moveLeft;
    Array<TextureRegion> textures = new Array<>();

    public Goomba(MarioScreen screen, float x, float y) {
        super(screen, x, y);

        moveLeft = true;
        disapear = false;
        isDestroyed = false;
        isDead = false;

        for (int i = 0; i < 2; i++) {
            textures.add(new TextureRegion(screen.getAtlas().findRegion("goomba").getTexture(), 227 + i *16 , 9, 16, 16));
        }

        walkAnimation = new Animation<>(0.3f, textures);
        
        textures.clear();
        stateTimer = 0;
    }

    public void update(float deltaT) {
        stateTimer += deltaT;

        move(deltaT);
        if(isDead && !isDestroyed){
            world.destroyBody(body);
            isDestroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba").getTexture(), 227 + 32 , 9, 16, 16));
        } else if(!isDead){
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(((TextureRegion) walkAnimation.getKeyFrame(stateTimer, true)));
        } else if(isDead && isDestroyed && (disapear)){
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba").getTexture(), 227 + 100 , 9, 16, 16));
        }

        if(isDead && !disapear){
            deathTime += deltaT;
            if(deathTime > 3){
                disapear = true;
            }
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8);
        fDef.filter.categoryBits = MyGdxGame.ENEMY_BIT;
        //what can the goomba collid with;
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
        fDef.filter.categoryBits = MyGdxGame.ENEMY_HEAD_BIT;
        
        body.createFixture(fDef).setUserData(this);
        setBounds(getX(), getY(), 16, 16);
    }

    /**
     * moves the goomba left until the direction is switched
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
    

    @Override
    public void hitOnHead() {
        isDead = true;
    }

    @Override
    public void hitObject() {
        moveLeft = !moveLeft;
    }
    
}

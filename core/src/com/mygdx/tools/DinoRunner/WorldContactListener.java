package com.mygdx.tools.DinoRunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.Dinorunner.Enemy;
import com.mygdx.game.Sprites.Dinorunner.Goomba;
import com.mygdx.game.Sprites.InteractiveTile;
import com.mygdx.game.Sprites.Dinorunner.Runner;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            // check if a player has been hit by an enemy
            case MyGdxGame.PLAYER_BIT | MyGdxGame.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Runner) fixA.getUserData()).playerDeath();;
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Runner) fixB.getUserData()).playerDeath();
                }
                break;
            
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
                
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}

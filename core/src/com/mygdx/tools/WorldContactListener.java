package com.mygdx.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.Objects.Pipe;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.InteractiveTile;
import com.mygdx.game.Sprites.Player;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            // check if the object is a extension of the interactivTile class
            if (object.getUserData() != null
                    && InteractiveTile.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTile) object.getUserData()).onHeadHit();
            }
        }

        switch (cDef) {
            // check if a player object has hit a enemy head mask
            case MyGdxGame.PLAYER_BIT | MyGdxGame.ENEMY_HEAD_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_HEAD_BIT) {
                    ((Enemy) fixA.getUserData()).hitOnHead();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.ENEMY_HEAD_BIT) {
                    ((Enemy) fixB.getUserData()).hitOnHead();
                }
                break;
            // if the enemy hit a object/pipe the enemey will turn around
            case MyGdxGame.ENEMY_BIT | MyGdxGame.PIPE_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).hitObject();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT) {
                    ((Enemy) fixB.getUserData()).hitObject();
                }
                break;
            //if the enemy hit the player the player will die
            case  MyGdxGame.ENEMY_BIT | MyGdxGame.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).playerDeath();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Player) fixB.getUserData()).playerDeath();
                }
                break;
            //if the player is on top of the pipe
            case MyGdxGame.PLAYER_BIT | MyGdxGame.PIPE_TOP_BIT:

                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).onTopOfPipe(((Pipe) fixB.getUserData()).getKey());
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Player) fixB.getUserData()).onTopOfPipe(((Pipe) fixA.getUserData()).getKey());
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
            //if the player stops touching the top of the pipe
            case MyGdxGame.PLAYER_BIT | MyGdxGame.PIPE_TOP_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).onTopOfPipe(0);
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Player) fixB.getUserData()).onTopOfPipe(0);
                }
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}

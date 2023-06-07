package com.mygdx.tools.SusJump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.Objects.SusJump.Portal;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.SusJump.BreakablePlatform;
import com.mygdx.game.Sprites.SusJump.Jumper;
import com.mygdx.game.Sprites.SusJump.Platform;
import com.mygdx.game.Sprites.SusJump.Tentacle;

public class SusContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            // check if the object is a extension of the Platform class
            if (object.getUserData() != null
                    && Platform.class.isAssignableFrom(object.getUserData().getClass())) {
                ((Platform) object.getUserData()).onHeadHit();
            }
        }

        switch (cDef) {
            // if the tentacle hits an object/wall the tentacle will turn around
            case MyGdxGame.ENEMY_BIT | MyGdxGame.FLOOR_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT) {
                    ((Tentacle) fixA.getUserData()).hitObject();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT) {
                    ((Tentacle) fixB.getUserData()).hitObject();
                }
                break;
            // if the player hits a breakable Platform the Platform will break
            case MyGdxGame.PLAYER_BIT | MyGdxGame.BRICK_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.BRICK_BIT) {
                    ((Platform) fixA.getUserData()).onHeadHit();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.BRICK_BIT) {
                    ((Platform) fixB.getUserData()).onHeadHit();
                }
                break;
            //if the enemy hit the player the player will die
            case  MyGdxGame.ENEMY_BIT | MyGdxGame.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Jumper) fixA.getUserData()).playerDeath();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Jumper) fixB.getUserData()).playerDeath();
                }
                break;
            //if the player hits the wall the player will die
            case  MyGdxGame.FLOOR_BIT | MyGdxGame.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Jumper) fixA.getUserData()).playerDeath();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Jumper) fixB.getUserData()).playerDeath();
                }
                break;
            //if the player is on top of the portal
            case MyGdxGame.PLAYER_BIT | MyGdxGame.PIPE_TOP_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Jumper) fixA.getUserData()).onTopOfPortal(true);
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Jumper) fixB.getUserData()).onTopOfPortal(true);
                }
                break; 
            case MyGdxGame.ENEMY_BIT | MyGdxGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT){
                    ((Tentacle) fixA.getUserData()).hitObject();
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT) {
                    ((Tentacle) fixB.getUserData()).hitObject();
                }
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
                    ((Jumper) fixA.getUserData()).onTopOfPortal(false);
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((Jumper) fixB.getUserData()).onTopOfPortal(false);
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

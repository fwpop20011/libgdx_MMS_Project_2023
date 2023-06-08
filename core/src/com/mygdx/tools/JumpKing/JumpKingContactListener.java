package com.mygdx.tools.JumpKing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.PlayerKing;
import com.mygdx.game.Sprites.JumpKing.Finish;
import com.mygdx.game.Sprites.JumpKing.Jumpable;

public class JumpKingContactListener implements ContactListener {

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
                    && Jumpable.class.isAssignableFrom(object.getUserData().getClass())) {
                ((Jumpable) object.getUserData()).onHeadHit();
            }
        }

        switch (cDef) {
            
            //if the player is on top of the pipe
            case MyGdxGame.PLAYER_BIT | MyGdxGame.PIPE_BIT:
                if (fixA.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((PlayerKing) fixA.getUserData()).onTopOfPipe(true);
                    
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((PlayerKing) fixB.getUserData()).onTopOfPipe(true);
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
                    ((PlayerKing) fixA.getUserData()).onTopOfPipe(false);
                } else if (fixB.getFilterData().categoryBits == MyGdxGame.PLAYER_BIT) {
                    ((PlayerKing) fixB.getUserData()).onTopOfPipe(false);
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

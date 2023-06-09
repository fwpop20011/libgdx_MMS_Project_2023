package com.mygdx.tools.JumpKing;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.JumpKing;
import com.mygdx.game.Sprites.JumpKing.Block;
import com.mygdx.game.Sprites.JumpKing.Finish;

public class JumpKingWorldCreator {

    public JumpKingWorldCreator(JumpKing screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        // create Ground
        for (MapObject o : map.getLayers().get(0).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(rect.getX() + (rect.getWidth()) / 2, rect.getY() + (rect.getHeight() / 2));

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fDef.shape = shape;
            body.createFixture(fDef);
        }

        // create finish
        for (MapObject o : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();
            new Finish(screen, rect);
        }

     

    }
}

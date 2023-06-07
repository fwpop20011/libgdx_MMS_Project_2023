package com.mygdx.tools.SusJump;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.Objects.SusJump.Portal;
import com.mygdx.game.Screens.SusJump;
import com.mygdx.game.Sprites.SusJump.BreakablePlatform;
import com.mygdx.game.Sprites.SusJump.DurablePlatform;
import com.mygdx.game.Sprites.SusJump.EnemyPlatform;
import com.mygdx.game.Sprites.SusJump.Task;
import com.mygdx.game.Sprites.SusJump.Tentacle;

public class SusWorldCreator {
    private Array<Tentacle> tentacles = new Array<>();

    public SusWorldCreator(SusJump susJump) {
        World world = susJump.getWorld();
        TiledMap map = susJump.getMap();

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

        // create durable platform
        for (MapObject o : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            new DurablePlatform(susJump, rect);
        }

        // create breakable platform
        for (MapObject o : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();
            Gdx.app.log(null, String.format("%f, %f", rect.getX(), rect.getY()));
            new BreakablePlatform(susJump, rect);
        }

        // create enemy platform
        for (MapObject o : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            new EnemyPlatform(susJump, rect);
        }

        // create task
        for (MapObject o : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            new Task(susJump, rect);
        }

        // create portal
        for (MapObject o : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();
            new Portal(susJump, rect);
        }

        //TODO not this
        // add in all tentacles into an array according to the tmx map
        /* 
        for (MapObject o : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            tentacles.add(new Tentacle(susJump, rect.getX(), rect.getY()));
        }*/
    }

    //returns all tentacles in an Array
    public Array<Tentacle> getTentacles(){
        return tentacles;
    }
}

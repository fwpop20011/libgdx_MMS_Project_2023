package com.mygdx.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Brick;
import com.mygdx.game.Sprites.Coin;

public class B2WorldCreator {
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        //create Ground
        for(MapObject o : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(rect.getX() + (rect.getWidth()) / 2, rect.getY() + (rect.getHeight() / 2));

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fDef.shape = shape;
            body.createFixture(fDef);
        }

        //create pipe
        for(MapObject o : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(rect.getX() + (rect.getWidth()) / 2, rect.getY() + (rect.getHeight() / 2));

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fDef.shape = shape;
            fDef.filter.categoryBits = MyGdxGame.PIPE_BIT;
            body.createFixture(fDef);
        }

        //create brickes
        for(MapObject o : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            new Brick(screen, rect);
        }

        //create coin
        for(MapObject o : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) o).getRectangle();

            new Coin(screen, rect);
        }
    }

    
}

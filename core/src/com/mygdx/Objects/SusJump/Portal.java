package com.mygdx.Objects.SusJump;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Filter;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;

public class Portal {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tileMap;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public Portal(SusJump screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(bounds.getX() + (bounds.getWidth()) / 2, bounds.getY() + (bounds.getHeight() / 2));

        body = world.createBody(bDef);

        shape.setAsBox(bounds.getWidth() / 2, bounds.getHeight() / 2);
        fDef.shape = shape;
        fixture = body.createFixture(fDef);

        setCategoryFilter(MyGdxGame.PIPE_BIT);
        fixture.setUserData(this);

        PolygonShape head = new PolygonShape();

        Vector2[] vertors = { new Vector2(-bounds.getWidth()/2, bounds.getHeight() / 2), new Vector2(bounds.getWidth()/2, bounds.getHeight() / 2),
                new Vector2(-3, bounds.getHeight() / 4), new Vector2(3, bounds.getHeight() / 4) };
        head.set(vertors);

        fDef.shape = head;
        fDef.filter.categoryBits = MyGdxGame.PIPE_TOP_BIT;
        body.createFixture(fDef).setUserData(this);
    }

    private void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}

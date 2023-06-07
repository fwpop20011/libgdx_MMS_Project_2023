package com.mygdx.game.Sprites.SusJump;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.SusJump;

public abstract class Platform {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tileMap;
    protected Rectangle bounds;
    protected Body body;
    protected Body bodyLow;

    protected Fixture fixture;

    private float restitution = 0.0f;

    public Platform(SusJump screen, Rectangle bounds) {
        this(screen, bounds, 0.0F);
    }

    public Platform(SusJump screen, Rectangle bounds, float restitution) {
        this.restitution = restitution;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(bounds.getX() + (bounds.getWidth()) / 2, bounds.getY() + (bounds.getHeight() / 2));

        body = world.createBody(bDef);
        bodyLow = world.createBody(bDef);

        fDef.filter.categoryBits = MyGdxGame.BRICK_BIT;

        fDef.filter.maskBits = MyGdxGame.PLAYER_BIT;

        shape.setAsBox(bounds.getWidth() / 2, bounds.getHeight() / 2);
        fDef.shape = shape;

        
        fDef.restitution = this.restitution;
        fixture = body.createFixture(fDef);

        /* 
        PolygonShape head = new PolygonShape(); 
        Vector2[] vertors = {new Vector2(-32, -12), new Vector2(32, -12),new Vector2(32, -7), new Vector2(-32, -7)};
        head.set(vertors);

        fDef.shape = head;
        fDef.filter.categoryBits = MyGdxGame.ENEMY_HEAD_BIT;
        bodyLow.createFixture(fDef).setUserData(this);
        fixture = bodyLow.createFixture(fDef);
        */
    }

    public abstract void onHeadHit();

    public abstract void onLandOn();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(10);
        return layer.getCell((int) (body.getPosition().x / 32),
                (int) (body.getPosition().y / 32));
    }
    public TiledMapTileLayer.Cell getCellLeft() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(10);
        return layer.getCell((int) ((body.getPosition().x / 32)-1),
                (int) (body.getPosition().y / 32));
    }
    public TiledMapTileLayer.Cell getCellRight() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(10);
        return layer.getCell((int) ((body.getPosition().x/ 32)+1),
                (int) (body.getPosition().y / 32));
    }

}

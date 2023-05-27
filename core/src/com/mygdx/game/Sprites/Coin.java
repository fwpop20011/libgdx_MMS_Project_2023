package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class Coin extends InteractiveTile {
    private static TiledMapTileSet tileSet;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet =map.getTileSets().getTileSet("tileset_gutter");
        setCategoryFilter(MyGdxGame.COIN_BIT);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("coin", "Collision");
        //id 28 is the blanc coin block
        if(getCell().getTile().getId() == 28){
            MyGdxGame.assetManager.get("assets/audio/sounds/bump.wav", Sound.class).play();
        } else{
            MyGdxGame.assetManager.get("assets/audio/sounds/coin.wav", Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(28));
        
    }
}

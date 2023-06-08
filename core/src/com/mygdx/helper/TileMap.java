package com.mygdx.helper;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMap {
    

    private TiledMap TiledMap;

    public TileMap(){

    }

    public OrthogonalTiledMapRenderer setUpMap(){
        TiledMap = new TmxMapLoader().load("assets/worlds/JumpKing/jumpking.tmx");
        return new OrthogonalTiledMapRenderer(TiledMap);
    }
}

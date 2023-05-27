package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.Screens.PlayScreen;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public Sprite sprite;
	public static final int V_Width = 400;
	public static final int V_Height = 208;

	public static final short DEFAULT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short PIPE_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short PIPE_TOP_BIT = 256;

	//Pixels per meter
	public static final float PPM = 10;

	public static AssetManager assetManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assetManager.load("assets/audio/music/mario_music.ogg", Music.class);
		assetManager.load("assets/audio/sounds/coin.wav", Sound.class);
		assetManager.load("assets/audio/sounds/breakblock.wav", Sound.class);
		assetManager.load("assets/audio/sounds/bump.wav", Sound.class);
		assetManager.finishLoading();
		
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render(){
		super.render();
	}
}

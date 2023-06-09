package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.BouncingBall;
import com.mygdx.game.Screens.MarioScreen;
import com.mygdx.game.Screens.SusJump;
import com.mygdx.game.Screens.JumpKing;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public Sprite sprite;
	public static final int V_Width = 400;
	public static final int V_Height = 208;

	//for mario game
	public static final short DEFAULT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short PIPE_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short PIPE_TOP_BIT = 256;

	//for dino runner
	public static final short FLOOR_BIT = 1;
	//public static final short PLAYER_BIT = 2;
	public static final short WALL_BIT = 4;

	//Pixels per meter
	public static final float PPM = 10;

	public static AssetManager assetManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();

		//Music for Jumpking and dinorunner
		assetManager.load("assets/audio/music/Jumper.mp3", Music.class);
		
		//Mario sounds
		assetManager.load("assets/audio/music/mario_music.ogg", Music.class);
		assetManager.load("assets/audio/sounds/coin.wav", Sound.class);
		assetManager.load("assets/audio/sounds/breakblock.wav", Sound.class);
		assetManager.load("assets/audio/sounds/bump.wav", Sound.class);
		assetManager.load("assets/audio/sounds/mariodie.wav", Sound.class);
		

		//Flappy Birds Sprites
		assetManager.load("assets/worlds/FlappyBird/bg.png", Texture.class);
		assetManager.load("assets/sprites/FlappyBird/bird.png", Texture.class);
		assetManager.load("assets/sprites/FlappyBird/pipes_up.png", Texture.class);
		assetManager.load("assets/sprites/FlappyBird/pipes_down.png", Texture.class);

		assetManager.update();

		//Flappy Birds Music
		assetManager.load("assets/audio/music/flappy_bird_music.mp3", Music.class);
		assetManager.load("assets/audio/sounds/flappybird/point.ogg", Sound.class);
		assetManager.load("assets/audio/sounds/flappybird/Jump.mp3", Sound.class);
		assetManager.load("assets/audio/sounds/flappybird/collide.mp3", Sound.class);

		//SusJump Sounds
		assetManager.load("assets/audio/music/susJump.mp3", Music.class);
		assetManager.load("assets/audio/sounds/SusJump/bounce.mp3", Sound.class);
		assetManager.load("assets/audio/sounds/SusJump/vent.mp3", Sound.class);
		assetManager.load("assets/audio/sounds/SusJump/death.mp3", Sound.class);


		assetManager.finishLoading();
		
		setScreen(new MarioScreen(this));
	} 

	@Override
	public void render(){
		super.render();
	}
}

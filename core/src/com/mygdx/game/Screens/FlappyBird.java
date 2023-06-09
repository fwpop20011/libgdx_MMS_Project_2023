package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.FlappyBird.Bird;
import com.mygdx.game.Sprites.FlappyBird.Pipe;

public class FlappyBird implements Screen {
    // The time interval between spawning pipes
    private static final int PIPE_SPAWN_TIME = 85;

    private MyGdxGame game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Hud hud;

    private Bird bird;
    private Array<Pipe> pipes;
    private int pipeSpawnTimer;
    private int score;
    private boolean isGameOver;
    private float gameOverWaitTime;

    Music backgroundMusic;
    Sound pointMusic, jumpMusic, collideMusic;

    public FlappyBird(MyGdxGame game) {
        this.game = game;
        batch = new SpriteBatch();
        backgroundTexture = MyGdxGame.assetManager.get("assets/worlds/FlappyBird/bg.png");
        hud = new Hud(batch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setupMusic();
    }


    private void setupMusic(){
        pointMusic = MyGdxGame.assetManager.get("assets/audio/sounds/flappybird/point.ogg", Sound.class);
        backgroundMusic = MyGdxGame.assetManager.get("assets/audio/music/flappy_bird_music.mp3", Music.class);
        jumpMusic = MyGdxGame.assetManager.get("assets/audio/sounds/flappybird/Jump.mp3", Sound.class);
        collideMusic = MyGdxGame.assetManager.get("assets/audio/sounds/flappybird/collide.mp3", Sound.class);

        backgroundMusic.setVolume(10);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {
        bird = new Bird(50, 300);
        pipes = new Array<>();

        pipeSpawnTimer = PIPE_SPAWN_TIME;
        isGameOver = false;
        gameOverWaitTime = 0;
        score = 0;

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        update(delta);
        //Begin drawing
        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw bird
        batch.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

        // Draw pipes
        for (Pipe pipe : pipes) {
            Rectangle pipeTop = pipe.getTopRectangle();
            Rectangle pipeBottom = pipe.getBottomRectangle();

            batch.draw(pipe.getTopTexture(), pipeTop.x, pipeTop.y, pipeTop.width, pipeTop.height);
            batch.draw(pipe.getBottomTexture(), pipeBottom.x, pipeBottom.y, pipeBottom.width, pipeBottom.height);
        }

        batch.end();

        // Display score
        hud.reName("FlappyBird", "Score: " +  score, "");
        hud.stage.draw();

        updateScene(delta);
    }

    public void update(float delta){
        //update the huds timer
        hud.updateTimeAdditive(delta);
    }

    public void updateScene(float delta){
        // Bird movement
        bird.move(delta);

        // Bird collides and game over
        if(isGameOver){
            gameOverWaitTime += delta;
            if(gameOverWaitTime > 2){
                game.setScreen(new GameOverScreen(game, 3));
            }
            return;
        }

        // Input from player
        if (Gdx.input.justTouched()) {
            bird.jump();
            jumpMusic.play();
        }

        // Update pipes
        for (Pipe pipe : pipes) {
            Rectangle pipeTop = pipe.getTopRectangle();

            pipe.move();

            // Player passed a pipe
            if (pipeTop.x + pipeTop.width <= 0) {
                pipes.removeValue(pipe, true);
                score++;
                pointMusic.play(); 
            }

            // Player collided with pipe
            if (pipe.collides(bird.getBounds())) {
                isGameOver = true;
                collideMusic.play();
                backgroundMusic.stop();
            }
        }

        // Spawn new pipe
        pipeSpawnTimer -= delta;
        if (pipeSpawnTimer <= 0) {
            pipes.add(new Pipe());
            pipeSpawnTimer = PIPE_SPAWN_TIME;
        }

        //Player collided with ground
        if (bird.getPosition().y <= 0) {
            isGameOver = true;
            collideMusic.play();
            backgroundMusic.stop();
        }
    }


    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bird.dispose();
        for (Pipe pipe : pipes) {
            pipe.dispose();
        }

        hud.dispose();

        batch.dispose();
        backgroundTexture.dispose();

        backgroundMusic.dispose();
        jumpMusic.dispose();
        collideMusic.dispose();
        pointMusic.dispose();
        game.dispose();
    }
}

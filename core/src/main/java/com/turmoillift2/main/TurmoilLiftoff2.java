package com.turmoillift2.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.turmoillift2.handlers.Content;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.handlers.MyInputProcessor;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class TurmoilLiftoff2 extends Game {

    // public game constants
    public static final String TITLE = "WIZARDS TURMOIL";
    public static final int WORLD_WIDTH = 640;
    public static final int WORLD_HEIGHT = 480;
    public static final float SCALE = 1.25f;

    // game engine (60fps) update time
    public static final float TICK = 1 / 144f;
    private float accum; // accumulator for steps (Only updates game if enough time has passed)

    // game fields for UI
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private FitViewport viewport;

    //TODO RefactorToPrivate
    public BitmapFont font;
    public TiledMap map;
    public OrthogonalTiledMapRenderer tmr;

    //private OrthographicCamera hudCamera;

    //handlers
    private GameStateManager gsm;
    public static Content resource;
    private InputMultiplexer inputMultiplexer;


    @Override
    public void create() {
        this.inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new MyInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        gsm = new GameStateManager(this);
        /* TODO Might want to try and make this work with gwt
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/ThaleahFat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.borderWidth = 2;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 3;
        font = generator.generateFont(parameter);
        generator.dispose();
         */

        font = new BitmapFont();
        map = new TmxMapLoader().load("map/tiles.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);
        resource = new Content();
        resource.loadTexture("animations/character.png", "character");
        resource.loadTexture("animations/characterFireAttack.png", "characterFireAttack");
        resource.loadTexture("animations/fireballBullet.png", "bullet");
        resource.loadTexture("animations/bettle.png", "bettle");
        resource.loadTexture("animations/bettleRedHit.png", "bettleRedHit");
        resource.loadTexture("animations/bluebettle.png", "bluebettle");
        resource.loadTexture("animations/characterHit.png", "characterHit");
        resource.loadTexture("animations/characterDead.png", "characterDead");
        resource.loadTexture("animations/vultureIdle.png", "vultureIdle");
        resource.loadTexture("animations/vultureAttacking.png", "vultureAttacking");
        resource.loadTexture("animations/dinoAttacking.png", "dinoAttacking");
        resource.loadTexture("animations/dinoHit.png", "dinoHit");
    }

    @Override
    public void render() {
        accum += Gdx.graphics.getDeltaTime();
        while (accum >= TICK) {
            accum -= TICK;
            gsm.update(TICK);
            gsm.render();
            MyInput.update();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        font.dispose();

    }

    // Getters

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }
}

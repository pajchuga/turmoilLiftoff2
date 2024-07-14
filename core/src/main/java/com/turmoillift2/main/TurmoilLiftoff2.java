package com.turmoillift2.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    public static final float SCALE = 1.5f;

    // game engine (60fps) update time
    public static final float TICK = 1 / 60f;
    private float accum; // accumulator for steps (Only updates game if enough time has passed)

    // game fields for UI
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private FitViewport viewport;

    //TODO RefactorToPrivate
    public BitmapFont font;
    public Texture background;
    public TiledMap map;
    public OrthogonalTiledMapRenderer tmr;
    //private OrthographicCamera hudCamera;

    //handlers
    private GameStateManager gsm;
    public static Content resource;


    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MyInputProcessor());

        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        gsm = new GameStateManager(this);
        font = new BitmapFont();
        background = new Texture(Gdx.files.internal("ui/background.png"));
        map = new TmxMapLoader().load("map/tiles.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);
        resource = new Content();
        resource.loadTexture("character.png", "character");
        resource.loadTexture("characterAttack.png", "characterAttack");
        resource.loadTexture("bullet.png", "bullet");

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
        background.dispose();
    }

    // Getters

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}

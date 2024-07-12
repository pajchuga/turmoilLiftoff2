package com.turmoillift2.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.main.TurmoilLiftoff2;

public abstract class GameState {
    protected GameStateManager gsm;
    protected TurmoilLiftoff2 game;

    protected SpriteBatch spriteBatch;
    protected OrthographicCamera camera;
//    protected OrthographicCamera hudCamera;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.getGame();
        this.spriteBatch = game.getSpriteBatch();
        this.camera = game.getCamera();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}

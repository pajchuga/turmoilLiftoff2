package com.turmoillift2.handlers;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.turmoillift2.main.TurmoilLiftoff2;
import com.turmoillift2.states.*;

import java.util.Stack;

public class GameStateManager {
    private TurmoilLiftoff2 game;
    private Stack<GameState> gameStateStack;

    public GameStateManager(TurmoilLiftoff2 game) {
        this.game = game;
        gameStateStack = new Stack<>();
        gameStateStack.push(null);
        pushState(GameStateType.INIT);
    }

    public void update(float dt) {
        gameStateStack.peek().update(dt);
    }

    public void render() {
        gameStateStack.peek().render();
    }

    public void setState(GameStateType gameStateType) {
        popState();
        pushState(gameStateType);
    }

    public void popState() {
        GameState gs = gameStateStack.pop();
        gs.dispose();
    }

    public void pushState(GameStateType gameStateType) {
        gameStateStack.push(getState(gameStateType));
    }

    //TODO Better handle of the states needed
    private GameState getState(GameStateType gameStateType) {
        switch (gameStateType) {
            case INIT:
                return new Init(this);
            case PLAY:
                return new Play(this);
        }
        return null;
    }

    //Getters
    public TurmoilLiftoff2 getGame() {
        return game;
    }

}

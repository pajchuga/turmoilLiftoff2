package com.turmoillift2.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.handlers.MyInputProcessor;
import com.turmoillift2.main.TurmoilLiftoff2;

public class Init extends GameState {

    public Init(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void handleInput() {
        if(MyInput.isPressed(MyInput.PLAY_BUTTON)) {
            gsm.setState(GameStateType.PLAY);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.47f, 0.25f, 0.55f, 1);
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();

        spriteBatch.draw(gsm.getGame().background,0,0,TurmoilLiftoff2.WORLD_WIDTH, TurmoilLiftoff2.WORLD_HEIGHT);
        gsm.getGame().font.draw(spriteBatch,"INIT STATE", TurmoilLiftoff2.WORLD_WIDTH/2,TurmoilLiftoff2.WORLD_HEIGHT/2);

        spriteBatch.end();


    }

    @Override
    public void dispose() {
    }
}

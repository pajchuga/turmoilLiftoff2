package com.turmoillift2.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {
    @Override
    public boolean keyDown(int k) {
        if (k == Input.Keys.P) {
            MyInput.setKey(MyInput.PAUSE_BUTTON, true);
        }
        if (k == Input.Keys.R) {
            MyInput.setKey(MyInput.PLAY_BUTTON, true);
        }
        if (k == Input.Keys.UP) {
            MyInput.setKey(MyInput.UP_BUTTON, true);
        }
        if (k == Input.Keys.DOWN) {
            MyInput.setKey(MyInput.DOWN_BUTTON, true);
        }
        if (k == Input.Keys.LEFT) {
            MyInput.setKey(MyInput.LEFT_BUTTON, true);
        }
        if (k == Input.Keys.RIGHT) {
            MyInput.setKey(MyInput.RIGHT_BUTTON, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int k) {
        if (k == Input.Keys.P) {
            MyInput.setKey(MyInput.PAUSE_BUTTON, false);
        }
        if (k == Input.Keys.R) {
            MyInput.setKey(MyInput.PLAY_BUTTON, false);
        }
        if (k == Input.Keys.UP) {
            MyInput.setKey(MyInput.UP_BUTTON, false);
        }
        if (k == Input.Keys.DOWN) {
            MyInput.setKey(MyInput.DOWN_BUTTON, false);
        }
        if (k == Input.Keys.LEFT) {
            MyInput.setKey(MyInput.LEFT_BUTTON, false);
        }
        if (k == Input.Keys.RIGHT) {
            MyInput.setKey(MyInput.RIGHT_BUTTON, false);
        }
        return true;
    }
}

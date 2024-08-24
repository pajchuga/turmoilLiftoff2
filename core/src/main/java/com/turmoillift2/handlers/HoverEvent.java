package com.turmoillift2.handlers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.tommyettinger.textra.TypingLabel;
import com.turmoillift2.main.TurmoilLiftoff2;

public class HoverEvent extends ClickListener {
    Sound sound;
    boolean restarted = false;

    public HoverEvent(Sound sound) {
        this.sound = sound;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if(fromActor == null && !restarted) {
            sound.play(0.7f);
            restarted = true;
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (toActor == null ) {
            restarted = false;
        }
    }
}

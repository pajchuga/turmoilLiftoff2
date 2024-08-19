package com.turmoillift2.handlers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.tommyettinger.textra.TypingLabel;

public class HoverEvent extends ClickListener {
    TypingLabel tl;
    boolean restarted = false;

    public HoverEvent(TypingLabel tl) {
        this.tl = tl;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if(fromActor == null) {
            tl.restart();
        }
    }

}

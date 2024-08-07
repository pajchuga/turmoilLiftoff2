package com.turmoillift2.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnimation {
    private TextureRegion[] frames;
    private float time;
    private float delay;
    private int currentFrame;
    private int timesPlayed;

    public MyAnimation() {

    }

    public MyAnimation(TextureRegion[] frames) {
        this(frames, 1 / 12f);
    }

    public MyAnimation(TextureRegion[] frames, float delay) {
        setFrames(frames, delay);
    }

    public void setFrames(TextureRegion[] frames, float delay) {
        this.frames = frames;
        this.delay = delay;
        time = 0;
        currentFrame = 0;
        timesPlayed = 0;
    }

    public void update(float dt) {
        if (delay <= 0) {
            return;
        }
        time += dt;
        while (time >= delay) {
            step();
        }

    }

    private void step() {
        time -= delay;
        currentFrame++;
        if (currentFrame == frames.length) {
            currentFrame = 0;
            timesPlayed++;
        }
    }

    public TextureRegion getCurrentFrame() {
        return frames[currentFrame];
    }


    public int getTimesPlayed() {
        return timesPlayed;
    }

    public int getTotalFramesInt() {
        return frames.length;
    }

    public int getCurrentFrameInt() {
        return currentFrame;
    }

    public boolean hasFinished() {
        return timesPlayed > 0;
    }

}

package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.handlers.MyAnimation;

import static com.turmoillift2.handlers.B2DVars.*;

public abstract class B2DSprite {
    protected Body body;
    protected MyAnimation animation;
    protected float width;
    protected float height;
    protected EntityOrientation orientation;

    public B2DSprite(Body body) {
        this.body = body;
        animation = new MyAnimation();
        orientation = EntityOrientation.RIGHT;
    }

    public void setAnimation(TextureRegion[] region, float delay) {
        animation.setFrames(region, delay);
        width = region[0].getRegionWidth();
        height = region[0].getRegionHeight();
    }

    public void update(float dt) {
        regulateTime(dt);
        animation.update(dt);
    }

    public void render(SpriteBatch sb) {
        boolean flip = (orientation == EntityOrientation.LEFT);
        int flipFactor = flip ? -1 : 1;
        sb.begin();

        sb.draw(animation.getCurrentFrame(), body.getPosition().x * PPM - width * flipFactor  / 2, body.getPosition().y * PPM - height / 2 + 1,flipFactor * width , height);

        sb.end();
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public abstract void regulateTime(float dt) ;
}

package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.*;

public class Enemy extends B2DSprite{
    // TODO finish up beetle enemy class extended from this one
    protected boolean isAlive = true;
    protected float moveForce;
    public Enemy(Body body) {
        super(body);
        body.setType(BodyDef.BodyType.DynamicBody);
        body.setGravityScale(0);
        Texture tex = TurmoilLiftoff2.resource.getTexture("bettle");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(textureRegions, 1 / 12f);
        moveForce = 1.6f;
    }

    @Override
    public void regulateTime(float dt) {
        boolean flip = orientation == EntityOrientation.RIGHT;
        int flipFactor = flip ? 1 : -1;
        if (this.getBody().getPosition().x * PPM  + 32 > TurmoilLiftoff2.WORLD_WIDTH && orientation == EntityOrientation.RIGHT) {
            flipOrientation();
        }
        if (this.getBody().getPosition().x * PPM  - 32 <=0 && orientation == EntityOrientation.LEFT) {
            flipOrientation();
        }

        this.getBody().setLinearVelocity(new Vector2(moveForce * flipFactor * 60 * dt, 0));
    }

    private void flipOrientation() {
        if (orientation == EntityOrientation.LEFT) {
            orientation = EntityOrientation.RIGHT;
        } else {
            orientation = EntityOrientation.LEFT;
        }
    }
}

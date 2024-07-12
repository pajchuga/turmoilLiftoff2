package com.turmoillift2.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class Player extends B2DSprite {
    float timer_t = 1/10f; // testing out different values
    float timer = timer_t;
    boolean canMove = true;


    public Player(Body body) {
        super(body);

        Texture tex = TurmoilLiftoff2.resource.getTexture("character");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(textureRegions, 1 / 2f);
    }

    @Override
    public void regulateTime(float dt) {
        if (timer > 0) {
            timer -= dt;
        } else {
            canMove = true;
            timer = timer_t;
        }
    }

    public void moveUp() {
        if(!canMove) return;

        float moveUnits = body.getPosition().y + 64f / PPM;
        if (moveUnits * PPM >= TurmoilLiftoff2.WORLD_HEIGHT) {
            return;
        }
        body.setTransform(body.getPosition().x, moveUnits, 0);
        canMove = false;
    }

    public void moveDown() {
        if(!canMove) return;

        float moveUnits = body.getPosition().y - 64f / PPM;
        if (moveUnits * PPM <= 0) {
            return;
        }
        body.setTransform(body.getPosition().x, body.getPosition().y - 64f / PPM, 0);
        canMove = false;
    }

    public void lookLeft() {
        orientation = EntityOrientation.LEFT;
    }

    public void lookRight() {
        orientation = EntityOrientation.RIGHT;
    }

}

package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class Player extends B2DSprite {
    private float timerMove_t = 1 / 10f; // testing out different values
    private float timerMove = timerMove_t;
    private float fireDelay = 1 / 12f; // fire rate is dependand on animation
    private boolean canMove = true;
    private PlayerState state = PlayerState.IDLE;

    public Player(Body body) {
        super(body);
        setStateAnimation();
    }

    @Override
    public void regulateTime(float dt) {
        if (state == PlayerState.ATTACKING && animation.getTimesPlayed() > 0) {
            state = PlayerState.IDLE;
            setStateAnimation();
        }
        if (timerMove > 0) {
            timerMove -= dt;
        } else {
            canMove = true;
            timerMove = timerMove_t;
        }
    }

    public void moveUp() {
        if (!canMove) return;

        float moveUnits = body.getPosition().y + 64f / PPM;
        if (moveUnits * PPM >= TurmoilLiftoff2.WORLD_HEIGHT) {
            return;
        }
        body.setTransform(body.getPosition().x, moveUnits, 0);
        canMove = false;
    }

    public void moveDown() {
        if (!canMove) return;

        float moveUnits = body.getPosition().y - 64f / PPM;
        if (moveUnits * PPM <= 0) {
            return;
        }
        body.setTransform(body.getPosition().x, body.getPosition().y - 64f / PPM, 0);
        canMove = false;
    }

    public void attack() {
        state = PlayerState.ATTACKING;
        setStateAnimation();
    }

    public void hit() {
        // TODO update hit system , add hit animation , add hit state, add lives
    }

    private void setStateAnimation() {
        Texture tex;
        TextureRegion[] textureRegions;
        switch (state) {
            case IDLE:
                tex = TurmoilLiftoff2.resource.getTexture("character");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 4f);
                return;
            case ATTACKING:
                tex = TurmoilLiftoff2.resource.getTexture("characterAttack");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, fireDelay);
        }

    }

    public void lookLeft() {
        orientation = EntityOrientation.LEFT;
    }

    public void lookRight() {
        orientation = EntityOrientation.RIGHT;
    }

    public PlayerState getState() {
        return state;
    }

}

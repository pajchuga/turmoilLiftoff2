package com.turmoillift2.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class EnemyVulture extends Enemy{
    protected float idleTimer = 2f;
    protected float attackingForce = 2.5f;

    public EnemyVulture(Body body) {
        super(body);
        state = EnemyState.IDLE;
        moveForce = 0f;
        lives = 1;
        setStateAnimation();
    }

    public EnemyVulture(Body body, int row) {
        super(body, row);
        state = EnemyState.IDLE;
        moveForce = 0f;
        lives = 1;
        setStateAnimation();
    }

    @Override
    public void regulateTime(float dt) {
        if (idleTimer >= 0) {
            idleTimer -= dt;
            return;
        }
        if (state != EnemyState.ATTACKING) {
            moveForce = attackingForce;
            state = EnemyState.ATTACKING;
            setMoveForce(orientation);
            setStateAnimation();
        }
        if (this.getBody().getPosition().x * PPM > TurmoilLiftoff2.WORLD_WIDTH + 32 || this.getBody().getPosition().x * PPM <= -32) {
            kill();
        }
    }

    protected void setStateAnimation() {
        Texture tex;
        TextureRegion[] textureRegions;
        switch (state) {
            case IDLE:
                tex = TurmoilLiftoff2.resource.getTexture("vultureIdle");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 8f);
                return;
            case ATTACKING:
                tex = TurmoilLiftoff2.resource.getTexture("vultureAttacking");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 16f);
                return;
            case HIT:
                return;
        }
    }


}

package com.turmoillift2.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.entities.EntityOrientation;
import com.turmoillift2.main.TurmoilLiftoff2;

public class EnemyDino extends Enemy{
    protected float pushBackImpulse = 1.5f;

    public EnemyDino(Body body) {
        super(body);
        pushBackImpulse = 1.5f;
        moveForce = 0.5f;
        lives = 5;
    }

    public EnemyDino(Body body, int row) {
        super(body, row);
        pushBackImpulse = 1.5f;
        moveForce = 0.8f;
        lives = 5;
    }

    @Override
    public void hit() {
        state = EnemyState.HIT;
        setStateAnimation();
        body.applyLinearImpulse(orientation == EntityOrientation.RIGHT ? -pushBackImpulse : pushBackImpulse, 0, body.getPosition().x, body.getPosition().y, true);
        if (--lives == 0) {
            kill();
        }
    }

    @Override
    protected void setStateAnimation() {
        Texture tex;
        TextureRegion[] textureRegions;
        switch (state) {
            case ATTACKING:
                tex = TurmoilLiftoff2.resource.getTexture("dinoAttacking");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 12f);
                return;
            case HIT:
                tex = TurmoilLiftoff2.resource.getTexture("dinoHit");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 24f);
        }
    }
}

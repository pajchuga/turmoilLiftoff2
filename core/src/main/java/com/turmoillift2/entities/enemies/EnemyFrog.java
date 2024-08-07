package com.turmoillift2.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.main.TurmoilLiftoff2;

public class EnemyFrog extends Enemy{

    public EnemyFrog(Body body) {
        super(body);
        lives = 1;
        moveForce = 0.75f;
        pointValue = 1;
    }

    public EnemyFrog(Body body, int row) {
        super(body, row);
        lives = 1;
        moveForce = 0.75f;
        pointValue = 1;
    }

    @Override
    protected void setStateAnimation() {
        Texture tex;
        TextureRegion[] textureRegions;
        switch (state) {
            case ATTACKING:
                tex = TurmoilLiftoff2.resource.getTexture("frogMove");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 16f);
                return;
            case HIT:
                //TODO add death animation of enemies and maybe frog then remove them.
                tex = TurmoilLiftoff2.resource.getTexture("frogMove");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 16f);
        }
    }

    @Override
    public EnemyTypes getType() {
        return EnemyTypes.FROG;
    }
}

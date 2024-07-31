package com.turmoillift2.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.entities.B2DSprite;
import com.turmoillift2.entities.EntityOrientation;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class Enemy extends B2DSprite {
    protected boolean isAlive = true;
    protected float moveForce;
    protected int row;
    protected int lives;
    protected EnemyState state = EnemyState.ATTACKING;
    protected int pointValue;

    public Enemy(Body body) {
        super(body);
        this.lives = 2;
        this.pointValue = 50;
        body.setGravityScale(0);
        setStateAnimation();
        moveForce = 1.8f;
    }

    public Enemy(Body body, int row) {
        this(body);
        this.row = row;
    }

    @Override
    public void regulateTime(float dt) {
        if (state == EnemyState.HIT && animation.getTimesPlayed() > 0) {
            setMoveForce(orientation);
            state = EnemyState.ATTACKING;
            setStateAnimation();
        }

        if (this.getBody().getPosition().x * PPM + 32 > TurmoilLiftoff2.WORLD_WIDTH && orientation == EntityOrientation.RIGHT) {
            flipOrientation();
            setMoveForce(orientation);
        }
        if (this.getBody().getPosition().x * PPM - 32 <= 0 && orientation == EntityOrientation.LEFT) {
            flipOrientation();
            setMoveForce(orientation);
        }

    }

    private void flipOrientation() {
        if (orientation == EntityOrientation.LEFT) {
            orientation = EntityOrientation.RIGHT;
        } else {
            orientation = EntityOrientation.LEFT;
        }
    }

    public int getRow() {
        return row;
    }

    public void kill() {
        pointValue = 0;
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void hit() {
        state = EnemyState.HIT;
        setStateAnimation();
        if (--lives == 0) {
           isAlive = false;
        }
    }

    public void setMoveForce(EntityOrientation orientation) {
        boolean flip = orientation == EntityOrientation.RIGHT;
        int flipFactor = flip ? 1 : -1;
        this.getBody().setLinearVelocity(new Vector2(moveForce * flipFactor, 0));
    }

    @Override
    protected void setStateAnimation() {
        Texture tex;
        TextureRegion[] textureRegions;
        switch (state) {
            case ATTACKING:
                tex = TurmoilLiftoff2.resource.getTexture("bettle");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 12f);
                return;
            case HIT:
                tex = TurmoilLiftoff2.resource.getTexture("bettleRedHit");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 24f);
        }
    }
}

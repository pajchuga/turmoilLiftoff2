package com.turmoillift2.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.turmoillift2.entities.projectiles.*;
import com.turmoillift2.entities.projectiles.Projectile;
import com.turmoillift2.entities.projectiles.ProjectileFactory;
import com.turmoillift2.entities.projectiles.ProjectileType;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class Player extends B2DSprite implements Killable {
    private float timerMove_t = 1 / 10f; // testing out different values
    private float timerMove = timerMove_t;
    private float fireDelay = 1 / 12f; // fire rate is dependent on animation
    private boolean canMove = true;
    private PlayerState state = PlayerState.IDLE;

    private HealthBar healthBar;
    private Sound playerHit;

    private int lives = 3;
    private Array<Projectile> activeProjectiles;
    private final ProjectileFactory projectileFactory;


    public Player(Body body) {
        super(body);
        setStateAnimation();
        healthBar = new HealthBar(this.getBody());
        healthBar.setKillableEntity(this);
        projectileFactory = new BasicProjectileFactory();
        playerHit = TurmoilLiftoff2.resource.getSound("playerHit");
    }

    @Override
    public void regulateTime(float dt) {
//        healthBar.update(dt);
        if (state == PlayerState.DEAD) return;
        if (state == PlayerState.ATTACKING && animation.getCurrentFrameInt() == animation.getTotalFramesInt() - 3) {
            if (projectileFactory.isCreateEnabled()) {
                activeProjectiles.add(projectileFactory.createProjectile(ProjectileType.BASIC, body, orientation));
                projectileFactory.setCreateEnable(false);
            }
        }
        if (state == PlayerState.ATTACKING && animation.getTimesPlayed() > 0) {
            state = PlayerState.IDLE;
            setStateAnimation();
            projectileFactory.setCreateEnable(true);
        }
        if (state == PlayerState.HIT && animation.getTimesPlayed() > 1) {
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

    public boolean attack() {
        if (state != PlayerState.IDLE) return false;
        state = PlayerState.ATTACKING;
        setStateAnimation();
        return true;
    }

    public void hit() {
        if (state == PlayerState.DEAD) return;
        playerHit.play(0.7f);
        if (--lives <= 0) {
            canMove = false;
            state = PlayerState.DEAD;
            setStateAnimation();
            return;
        }
        state = PlayerState.HIT;
        setStateAnimation();
    }

    @Override
    protected void setStateAnimation() {
        Texture tex;
        TextureRegion[] textureRegions;
        switch (state) {
            case IDLE:
                tex = TurmoilLiftoff2.resource.getTexture("character");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 4f);
                return;
            case ATTACKING:
                tex = TurmoilLiftoff2.resource.getTexture("characterFireAttack");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, fireDelay);
                return;

            case HIT:
                tex = TurmoilLiftoff2.resource.getTexture("characterHit");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 24f);
                return;

            case DEAD:
                tex = TurmoilLiftoff2.resource.getTexture("characterDead");
                textureRegions = TextureRegion.split(tex, 32, 32)[0];
                setAnimation(textureRegions, 1 / 8f);
                return;
        }

    }

    public void lookLeft() {
        if (!canMove) return;
        orientation = EntityOrientation.LEFT;
    }

    public void lookRight() {
        if (!canMove) return;
        orientation = EntityOrientation.RIGHT;
    }

    public PlayerState getState() {
        return state;
    }

    public boolean playerFinished() {
        return state == PlayerState.DEAD && animation.hasFinished();
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public int getLives() {
        return lives;
    }

    public void setActiveProjectiles(Array<Projectile> activeProjectiles) {
        this.activeProjectiles = activeProjectiles;
    }

    public void kill() {
        this.lives = 0;
        state = PlayerState.DEAD;
        setStateAnimation();
    }
}

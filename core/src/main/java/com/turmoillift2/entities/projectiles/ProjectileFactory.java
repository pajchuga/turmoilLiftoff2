package com.turmoillift2.entities.projectiles;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.turmoillift2.entities.EntityOrientation;

public interface ProjectileFactory {
    Projectile createProjectile(ProjectileType projectileType, Body body, EntityOrientation orientation);

    Array<ProjectileType> getProjectileTypes();

    void setCreateEnable(boolean bool);

    boolean isCreateEnabled();
}

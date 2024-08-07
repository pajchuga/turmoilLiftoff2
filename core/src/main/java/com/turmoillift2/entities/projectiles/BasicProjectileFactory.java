package com.turmoillift2.entities.projectiles;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.turmoillift2.entities.EntityOrientation;

public class BasicProjectileFactory implements ProjectileFactory {
    private final Array<ProjectileType> projectileTypes = new Array<>(1);
    private boolean createEnabled = true;
    {
        projectileTypes.add(ProjectileType.BASIC);
    }

    public BasicProjectileFactory() {

    }


    @Override
    public Projectile createProjectile(ProjectileType projectileType, Body body, EntityOrientation orientation) {
        Projectile projectile = new Projectile(body);
        projectile.setOrientation(orientation);
        projectile.setBody();
        return projectile;
    }

    @Override
    public Array<ProjectileType> getProjectileTypes() {
        return projectileTypes;
    }

    @Override
    public void setCreateEnable(boolean createEnabled) {
        this.createEnabled = createEnabled;
    }

    @Override
    public boolean isCreateEnabled() {
        return createEnabled;
    }
}

package com.turmoillift2.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.turmoillift2.entities.enemies.Enemy;
import com.turmoillift2.entities.Player;
import com.turmoillift2.entities.enemies.EnemyFrog;
import com.turmoillift2.entities.enemies.EnemyTypes;
import com.turmoillift2.entities.projectiles.Projectile;

public class MyContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa.getUserData() == null || fb.getUserData() == null) return ;
        handleEnemyProjectileContact(fa, fb);
        handleEnemyPlayerContact(fa, fb);

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void handleEnemyProjectileContact(Fixture fa, Fixture fb) {
//        if (fa.getUserData() instanceof Player || fb.getUserData() instanceof Player) return;
        if (fa.getUserData() instanceof Enemy && fb.getUserData() instanceof Projectile) {
            ((Enemy) fa.getUserData()).hit();
            ((Projectile) fb.getUserData()).setHit();
            return;
        }
        if (fb.getUserData() instanceof Enemy && fa.getUserData() instanceof Projectile) {
            ((Enemy) fb.getUserData()).hit();
            ((Projectile) fa.getUserData()).setHit();

        }
    }

    private void handleEnemyPlayerContact(Fixture fa, Fixture fb) {
        if (fa.getUserData() instanceof Enemy && fb.getUserData() instanceof Player) {
            if (((Enemy) fa.getUserData()).getType() == EnemyTypes.FROG) {
                ((Enemy) fa.getUserData()).kill();
                ((Player) fb.getUserData()).kill();
                return;
            }
            ((Enemy) fa.getUserData()).kill();
            ((Player) fb.getUserData()).hit();
            return;
        }
        if (fb.getUserData() instanceof Enemy && fa.getUserData() instanceof Player) {
            if (((Enemy) fb.getUserData()).getType() == EnemyTypes.FROG) {
                ((Enemy) fb.getUserData()).kill();
                ((Player) fa.getUserData()).kill();
                return;
            }
            ((Enemy) fb.getUserData()).kill();
            ((Player) fa.getUserData()).hit();
        }
    }
}

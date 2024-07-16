package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.*;

public class Projectile extends B2DSprite {
    private boolean hit = false;

    public Projectile(Body body) {
        super(body);
        Texture tex = TurmoilLiftoff2.resource.getTexture("bullet");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(textureRegions, 1/6f);
    }

    @Override
    public void regulateTime(float dt) {
        float moveUnits = body.getPosition().x;
        if (moveUnits * PPM >= TurmoilLiftoff2.WORLD_WIDTH || moveUnits * PPM <= 0) {
            setHit();
        }
    }

    public void setBody() {
        // if body is looking left flip asset
        boolean flip = (orientation == EntityOrientation.LEFT);
        int flipFactor = flip ? -1 : 1; // if projectile is looking left flip it to other side
        Vector2 force = new Vector2(2.8f * flipFactor, 0); // speed of projectile 2.8f
        // define body of projectile
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(this.body.getPosition().x + flipFactor * 5f / PPM , this.body.getPosition().y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = this.getBody().getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 14  / PPM, (float) 8 / PPM); // size of projectile physics body

        // define fixture definition (what type and with what colides, and it is sensor only (might change this not to be sensor for more interaction))
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = PROJECTILE_BIT;
        fdef.filter.maskBits = ENEMY_BIT;

        //set this projectile info to user data for easy handling
        body.createFixture(fdef).setUserData(this);
        body.setBullet(true);
        body.setLinearVelocity(force);
        body.setGravityScale(0); //ignores gravity
        this.body = body;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit() {
        hit = true;
    }

}

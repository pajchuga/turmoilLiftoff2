package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Pool;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.*;

public class Projectile extends B2DSprite implements Pool.Poolable {
    boolean hit = false;

    public Projectile(Body body) {
        super(body);
        Texture tex = TurmoilLiftoff2.resource.getTexture("bullet");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(textureRegions, 1/6f);
    }

    @Override
    public void regulateTime(float dt) {

    }

    @Override
    public void reset() {
        hit = false;
    }

    public void setBody() {
        boolean flip = (orientation == EntityOrientation.LEFT);
        int flipFactor = flip ? -1 : 1;
        Vector2 force = new Vector2(2.8f * flipFactor, 0);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(this.body.getPosition().x + flipFactor * 15f / PPM , this.body.getPosition().y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = this.getBody().getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 16  / PPM, (float) 8 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        //TODO mask bits for collisions (had trouble with GWT be wary)

        body.createFixture(fdef);
        body.setBullet(true);
        body.setLinearVelocity(force);
        body.setGravityScale(0);
        this.body = body;
    }

    public void setOrientation(EntityOrientation orientation) {
        this.orientation = orientation;
    }
}

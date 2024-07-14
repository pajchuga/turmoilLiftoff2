package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.*;

public class Enemy extends B2DSprite{
    public Enemy(Body body) {
        super(body);
        body.setType(BodyDef.BodyType.DynamicBody);
        body.setGravityScale(0);
        Texture tex = TurmoilLiftoff2.resource.getTexture("bettle");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(textureRegions, 1 / 2.8f);
    }

    @Override
    public void regulateTime(float dt) {

        this.getBody().setTransform(this.getBody().getPosition().x + 2 * dt , this.getBody().getPosition().y , 0);
    }
}

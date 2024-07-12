package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class Player extends B2DSprite {


    public Player(Body body) {
        super(body);

        Texture tex = TurmoilLiftoff2.resource.getTexture("character");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(textureRegions, 1 / 2f);
    }

    public void moveUp() {
        float moveUnits = body.getPosition().y + 64f / PPM;
        if (moveUnits * PPM >= TurmoilLiftoff2.WORLD_HEIGHT) {
            return;
        }
        body.setTransform(body.getPosition().x, moveUnits, 0);
    }

    public void moveDown() {
        float moveUnits = body.getPosition().y - 64f / PPM;
        if (moveUnits * PPM <= 0 || animation.hasFinished()) {
            return;
        }
        body.setTransform(body.getPosition().x, body.getPosition().y - 64f / PPM, 0);

    }

    public void lookLeft() {
        orientation = EntityOrientation.LEFT;
    }

    public void lookRight() {
        orientation = EntityOrientation.RIGHT;
    }
}

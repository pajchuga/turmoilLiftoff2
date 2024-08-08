package com.turmoillift2.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class HealthBar extends B2DSprite {
    private Killable killableEntity;

    public HealthBar(Body body) {
        super(body);
        Texture tex = TurmoilLiftoff2.resource.getTexture("healthbar");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 8, 8)[0];
        setAnimation(textureRegions, 1 / 6f);
    }

    @Override
    public void render(SpriteBatch sb) {
        int offset = 0;
        int lives = killableEntity.getLives();
        if (lives > 4) {
            offset = -MathUtils.ceil((lives - 4) / 2f) * 8;
        } else if (lives < 2) {
            offset = 8;
        } else if (lives == 2) {
            offset = 4;
        }
        sb.begin();
        for (int i = 0; i < lives; i ++) {
            sb.draw(animation.getCurrentFrame(), body.getPosition().x  * PPM - width - 4  +  i*8 + offset , body.getPosition().y * PPM - height / 2 + 20, width + 4, height + 4);
        }

        sb.end();
    }

    @Override
    public void regulateTime(float dt) {
    }

    public void setKillableEntity(Killable killableEntity) {
        this.killableEntity = killableEntity;
    }
}




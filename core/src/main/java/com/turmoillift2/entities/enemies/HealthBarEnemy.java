package com.turmoillift2.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.turmoillift2.entities.HealthBar;
import com.turmoillift2.main.TurmoilLiftoff2;

public class HealthBarEnemy extends HealthBar {
    public HealthBarEnemy(Body body) {
        super(body);
        Texture tex = TurmoilLiftoff2.resource.getTexture("healthbarEnemy");
        TextureRegion[] textureRegions = TextureRegion.split(tex, 8, 8)[0];
        setAnimation(textureRegions, 1 / 6f);
    }
}

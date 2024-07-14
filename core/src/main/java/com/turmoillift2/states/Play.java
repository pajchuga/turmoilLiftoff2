package com.turmoillift2.states;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ScreenUtils;
import com.turmoillift2.entities.Player;
import com.turmoillift2.entities.PlayerState;
import com.turmoillift2.entities.Projectile;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.main.TurmoilLiftoff2;

import java.util.LinkedList;

import static com.turmoillift2.handlers.B2DVars.*;

public class Play extends GameState {
    private World world;
    private Box2DDebugRenderer b2drDebug;
    private OrthographicCamera b2DCam;
    private Player player;
    private Pool<Projectile> projectilePool;
    private final LinkedList<Projectile> activeProjectiles = new LinkedList<>();

    public Play(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.81f), true);
        b2drDebug = new Box2DDebugRenderer();
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((float) TurmoilLiftoff2.WORLD_WIDTH / 2 / PPM, (float) TurmoilLiftoff2.WORLD_HEIGHT / 2 / PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 10 / PPM, (float) 14 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        b2DCam = new OrthographicCamera();
        b2DCam.setToOrtho(false, (float) TurmoilLiftoff2.WORLD_WIDTH / PPM, (float) TurmoilLiftoff2.WORLD_HEIGHT / PPM);

        player = new Player(body);

        projectilePool = new Pool<Projectile>() {
            @Override
            protected Projectile newObject() {
                return new Projectile(body);
            }
        };
        projectilePool.fill(10);
    }

    @Override
    public void handleInput() {
        if (MyInput.isPressed(MyInput.PAUSE_BUTTON)) {
            gsm.setState(GameStateType.INIT);
        }
        if (MyInput.isPressed(MyInput.UP_BUTTON)) {
            player.moveUp();
        }
        if (MyInput.isPressed(MyInput.DOWN_BUTTON)) {
            player.moveDown();
        }
        if (MyInput.isPressed(MyInput.LEFT_BUTTON)) {
            player.lookLeft();
        }
        if (MyInput.isPressed(MyInput.RIGHT_BUTTON)) {
            player.lookRight();
        }
        if (MyInput.isPressed(MyInput.ATTACK_BUTTON)) {
            if (player.getState() == PlayerState.ATTACKING) return;
            Projectile projectile = projectilePool.obtain();
            projectile.setOrientation(player.getOrientation());
            activeProjectiles.add(projectile);
            player.attack();
            projectile.setBody();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        world.step(dt, 6, 2);
        player.update(dt);
        for (Projectile projectile : activeProjectiles) {
            projectile.update(dt);
        }
        //TODO remove projectiles that hit

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.47f, 0.25f, 0.55f, 1);
        spriteBatch.setProjectionMatrix(camera.combined);


        spriteBatch.begin();

        game.tmr.setView(camera);
        game.tmr.render();


        game.font.draw(spriteBatch, "PLAY STATE", 5, 20);

        spriteBatch.end();


        player.render(spriteBatch);
        for (Projectile projectile : activeProjectiles) {
            projectile.render(spriteBatch);
        }
        b2drDebug.render(world, b2DCam.combined);

    }

    @Override
    public void dispose() {

    }

    public World getWorld() {
        return world;
    }
}

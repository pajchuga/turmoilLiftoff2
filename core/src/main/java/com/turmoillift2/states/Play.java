package com.turmoillift2.states;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.turmoillift2.entities.Enemy;
import com.turmoillift2.entities.Player;
import com.turmoillift2.entities.PlayerState;
import com.turmoillift2.entities.Projectile;
import com.turmoillift2.handlers.EnemySpawner;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.PPM;

public class Play extends GameState {
    private World world;
    private Box2DDebugRenderer b2drDebug;
    private OrthographicCamera b2DCam;
    private Player player;
    private final Array<Projectile> activeProjectiles = new Array<>();
    private final Array<Projectile> inactiveProjectiles = new Array<>();
    private final Array<Enemy> enemies = new Array<>();

    private EnemySpawner enemySpawner;

    private int[] firstLayers = {0,1};
    private int[] lastLayers = {2};


    public Play(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.81f), true);
        b2drDebug = new Box2DDebugRenderer();

        // create player
        createPlayer();
        enemySpawner = new EnemySpawner(game.map,enemies,world);

        b2DCam = new OrthographicCamera();
        b2DCam.setToOrtho(false, (float) TurmoilLiftoff2.WORLD_WIDTH / PPM, (float) TurmoilLiftoff2.WORLD_HEIGHT / PPM);

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
        if (MyInput.isDown(MyInput.ATTACK_BUTTON)) {
            if (player.getState() == PlayerState.ATTACKING) return; // comment for infinite fire rate
            player.attack();
            Projectile projectile = new Projectile(player.getBody());
            projectile.setOrientation(player.getOrientation());
            projectile.setBody();
            activeProjectiles.add(projectile);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 3);
        player.update(dt);
        enemySpawner.update(dt);
        for (Enemy enemy : enemies) {
            enemy.update(dt);
        }
        for (Projectile projectile : activeProjectiles) {
            if (projectile.isHit()) {
                inactiveProjectiles.add(projectile);
            }
            projectile.update(dt);
        }
        activeProjectiles.removeAll(inactiveProjectiles, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.37f, 0.75f, 0.85f, 1);
        spriteBatch.setProjectionMatrix(camera.combined);

        // start batch draw
        spriteBatch.begin();

        //render map/arena
        game.tmr.setView(camera);
        game.tmr.render(firstLayers);

        //render font
        game.font.draw(spriteBatch, "PLAY STATE", 5, 20);
        spriteBatch.end();
        // end batch draw

        //render player
        player.render(spriteBatch);

        for(Enemy enemy : enemies) {
            enemy.render(spriteBatch);
        }

        // render assets for parallax effect
        game.tmr.render(lastLayers);

        //render projectiles
        for (Projectile projectile : activeProjectiles) {
            projectile.render(spriteBatch);
        }

        //render debug boxes
        b2drDebug.render(world, b2DCam.combined);

    }

    @Override
    public void dispose() {
    }

    public World getWorld() {
        return world;
    }

    private void createPlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((float) TurmoilLiftoff2.WORLD_WIDTH / 2 / PPM, (float) TurmoilLiftoff2.WORLD_HEIGHT / 2 / PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 9 / PPM, (float) 14 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        player = new Player(body);
    }

}

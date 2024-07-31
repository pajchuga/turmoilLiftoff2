package com.turmoillift2.states;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.turmoillift2.entities.Player;
import com.turmoillift2.entities.Projectile;
import com.turmoillift2.entities.enemies.Enemy;
import com.turmoillift2.handlers.*;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.*;

public class Play extends GameState {
    private final World world;
    private MyContactListener contactListener;
    private final Box2DDebugRenderer b2drDebug;
    private final OrthographicCamera b2DCam;
    private Player player;
    private final Array<Projectile> activeProjectiles = new Array<>();
    private final Array<Projectile> inactiveProjectiles = new Array<>();
    private final Array<Enemy> enemies = new Array<>();
    private final Array<Enemy> killedEnemies = new Array<>();

    private final EnemySpawner enemySpawner;
    private final Score score;

    private final int[] firstLayers = {0, 1};
    private final int[] lastLayers = {2};


    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new MyContactListener();
        b2drDebug = new Box2DDebugRenderer();
        world.setContactListener(contactListener);

        // create player
        createPlayer();
        enemySpawner = new EnemySpawner(game.map, enemies, world);
        score = new Score();

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
//            if (player.getState() != PlayerState.IDLE) return; // comment for infinite fire rate
            if (player.attack()) {
                Projectile projectile = new Projectile(player.getBody());
                projectile.setOrientation(player.getOrientation());
                projectile.setBody();
                activeProjectiles.add(projectile);
            };
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 3);
        handlePlayer(dt);
        handleEnemies(dt);

        for (Projectile projectile : activeProjectiles) {
            if (projectile.isHit()) {
                inactiveProjectiles.add(projectile);
                world.destroyBody(projectile.getBody());
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
        game.font.draw(spriteBatch, "SCORE: " + score.getPoints(), TurmoilLiftoff2.WORLD_WIDTH - 150, TurmoilLiftoff2.WORLD_HEIGHT - 15);
        spriteBatch.end();
        // end batch draw

        //render player
        player.render(spriteBatch);

        //render enemies
        for (Enemy enemy : enemies) {
            enemy.render(spriteBatch);
        }

        //render projectiles
        for (Projectile projectile : activeProjectiles) {
            projectile.render(spriteBatch);
        }

        // render assets for parallax effect
        game.tmr.render(lastLayers);
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
        // define body of player and set position
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((float) TurmoilLiftoff2.WORLD_WIDTH / 2 / PPM, (float) TurmoilLiftoff2.WORLD_HEIGHT / 2 / PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 9 / PPM, (float) 14 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = PLAYER_BIT;
        fdef.filter.maskBits = ENEMY_BIT;
        player = new Player(body);

        body.createFixture(fdef).setUserData(player);
    }

    private void handleEnemies(float dt) {
        enemySpawner.update(dt);
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                killedEnemies.add(enemy);
                enemySpawner.freeRow(enemy.getRow());
                world.destroyBody(enemy.getBody());
                score.addPoints(enemy.getPointValue());
                continue;
            }
            enemy.update(dt);
        }
        enemies.removeAll(killedEnemies, true);
    }

    private void handlePlayer(float dt) {
        if (player.playerFinished()) {
            gsm.setState(GameStateType.INIT);
            return;
        }
        player.update(dt);
    }

}

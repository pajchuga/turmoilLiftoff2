package com.turmoillift2.states;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.tommyettinger.textra.KnownFonts;
import com.github.tommyettinger.textra.TypingLabel;
import com.turmoillift2.entities.Player;
import com.turmoillift2.entities.PlayerState;
import com.turmoillift2.entities.enemies.Enemy;
import com.turmoillift2.entities.enemies.EnemySpawner;
import com.turmoillift2.entities.projectiles.Projectile;
import com.turmoillift2.handlers.GameStateManager;
import com.turmoillift2.handlers.MyContactListener;
import com.turmoillift2.handlers.MyInput;
import com.turmoillift2.handlers.Score;
import com.turmoillift2.main.TurmoilLiftoff2;

import static com.turmoillift2.handlers.B2DVars.*;

public class Play extends GameState {
    private static final String POINTS_EFFECT = "[%125]{SLOWER}{FADE=738D68;add8e6;0.5}{SQUASH=1;true}";
    private static final String LOAD_POINTS_EFFECT = "[%125]{WAIT=0.75}{FADE=f1b209ff;add8e6;1.0}{SQUASH=5.0;true}{SLOWER}";
    private static final String LOAD_SCORE_EFFECT = "[%125]{FADE=f1b209ff;add8e6;1.0}{SQUASH=4.0;true}{SLOWER}";

    private final World world;
    private MyContactListener contactListener;
    private final Box2DDebugRenderer b2drDebug;
    private final OrthographicCamera b2DCam;
    private Player player;
    private final Array<Projectile> activeProjectiles = new Array<>();
    private final Array<Projectile> inactiveProjectiles = new Array<>();
    private final Array<Enemy> enemies = new Array<>();
    private final Array<Enemy> killedEnemies = new Array<>();
    private final Sound battleTheme;

    private final EnemySpawner enemySpawner;
    private final Score score;

    // STAGE FOR HUD SETUP - REFACTOR LATER
    private final Stage stage;
    private final Skin skin;
    private final TypingLabel scoreLabel;
    private final TypingLabel pointLabel;
    private  Stage stageAndroidOverlay;

    private final int[] firstLayers = {0, 1};
    private final int[] lastLayers = {2};

    private Application.ApplicationType appType;


    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new MyContactListener();
        b2drDebug = new Box2DDebugRenderer();
        world.setContactListener(contactListener);

        // create player
        createPlayer();
        enemySpawner = new EnemySpawner(game.getMap(), enemies, world);
        score = new Score();

        b2DCam = new OrthographicCamera();
        b2DCam.setToOrtho(false, (float) TurmoilLiftoff2.WORLD_WIDTH / PPM, (float) TurmoilLiftoff2.WORLD_HEIGHT / PPM);


        //TODO Refactor later, just testing right now
        skin = new Skin(Gdx.files.internal("ui/test2/uiskin.json"));
        stage = new Stage(game.getViewport());
        scoreLabel = new TypingLabel(LOAD_SCORE_EFFECT + "SCORE: ", KnownFonts.getIBM8x16());
        pointLabel = new TypingLabel(LOAD_POINTS_EFFECT + score.getPoints(), KnownFonts.getIBM8x16());
//        game.getInputMultiplexer().addProcessor(stage);
        stage.addActor(scoreLabel);
        Table root = new Table();
        root.setSkin(skin);
        root.setFillParent(true);
        skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
        stage.addActor(root);
        root.right().top().padTop(8);
        root.add(scoreLabel).align(Align.right).minWidth(40);
        root.add(pointLabel).align(Align.right).minWidth(120).padLeft(5);

        // Sounds
        battleTheme = TurmoilLiftoff2.resource.getSound("battleThemeSound");
        battleTheme.loop(0.15f);
        appType = Gdx.app.getType();
        if (appType == Application.ApplicationType.Android) {
            initOverlay();
        }
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
            player.attack();
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
        stage.getViewport().apply();

        spriteBatch.setProjectionMatrix(camera.combined);

        // start batch draw
        spriteBatch.begin();

        //render map/arena
        game.getTmr().setView(camera);
        game.getTmr().render(firstLayers);

        spriteBatch.end();
        // end batch draw

        //render player
        player.render(spriteBatch);
        player.getHealthBar().render(spriteBatch);

        //render enemies
        for (Enemy enemy : enemies) {
            enemy.render(spriteBatch);
            enemy.getHealthBar().render(spriteBatch);
        }

        //render projectiles
        for (Projectile projectile : activeProjectiles) {
            projectile.render(spriteBatch);
        }

        // render assets for parallax effect
        game.getTmr().render(lastLayers);
        //render debug boxes
//        b2drDebug.render(world, b2DCam.combined);
        stage.getViewport().apply();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 144f));
        stage.draw();

        if (stageAndroidOverlay != null) {
            stageAndroidOverlay.getViewport().apply();
            stageAndroidOverlay.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 144f));
            stageAndroidOverlay.draw();
        }

    }

    @Override
    public void dispose() {
        battleTheme.stop();
        //TODO Manage disposal for now everything loads at the start and stays there it should
        // most stuff should load on creating this state and stay only while this state is active
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
        player.setActiveProjectiles(this.activeProjectiles);
    }

    private void handleEnemies(float dt) {
        enemySpawner.update(dt);
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                killedEnemies.add(enemy);
                enemySpawner.freeRow(enemy.getRow());
                world.destroyBody(enemy.getBody());
                int points = enemy.getPointValue();
                if (points != 0) {
                    score.addPoints(enemy.getPointValue());
                    pointLabel.restart(POINTS_EFFECT + score.getPoints()); // shrink, spin, squash for now
                }
                continue;
            }
            enemy.update(dt);
            enemy.getHealthBar().update(dt);
        }
        enemies.removeAll(killedEnemies, true);
    }

    private void handlePlayer(float dt) {
        if (player.playerFinished()) {
            gsm.setState(GameStateType.INIT);
            return;
        }
        player.update(dt);
        player.getHealthBar().update(dt);
    }

    private void initOverlay() {
        stageAndroidOverlay = new Stage(new ScreenViewport());
        game.getInputMultiplexer().addProcessor(stageAndroidOverlay);
        Table overlayRoot = new Table();
        overlayRoot.setSkin(skin);
        overlayRoot.setFillParent(true);
        overlayRoot.top();
        stageAndroidOverlay.addActor(overlayRoot);

        TextButton testButton = new TextButton("Test", skin);
        testButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyInput.setKey(MyInput.ATTACK_BUTTON, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                MyInput.setKey(MyInput.ATTACK_BUTTON, false);
            }
        });
        overlayRoot.add(testButton).align(Align.center).align(Align.left)
            .minWidth(Gdx.graphics.getWidth()/6f)
            .minHeight(Gdx.graphics.getHeight()/3f)
            .padLeft(5)
            .expand();
//        overlayRoot.debug();
    }

}

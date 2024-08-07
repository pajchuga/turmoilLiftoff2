package com.turmoillift2.entities.enemies;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.turmoillift2.entities.EntityOrientation;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.turmoillift2.handlers.B2DVars.*;

public class EnemySpawner {
    private Set<Integer> availableRows = new HashSet<>();

    {
        availableRows.addAll(Set.of(1, 2, 3, 4, 5, 6, 7));
    }

    private TiledMap map;
    private World world;
    private Array<Enemy> enemies;

    private float spawnDelay = 0.3f;
    private float timer = 1f;
    private String location = "";

    public EnemySpawner(TiledMap map, Array<Enemy> enemies, World world) {
        this.map = map;
        this.enemies = enemies;
        this.world = world;
    }

    public void update(float dt) {
        if (availableRows.isEmpty()) return;
        Enemy spawnedEnemy = spawn(dt);
        if (spawnedEnemy == null) return;
        enemies.add(spawnedEnemy);
    }

    public void freeRow(int i) {
        availableRows.add(i);
    }

    private Enemy spawn(float dt) {
        if (timer >= 0) {
            timer -= dt;
            return null;
        }
        timer = spawnDelay;
        int randomIndex = new Random().nextInt(availableRows.size());
        int i = 0;
        int toRemove = 0;
        for (Integer row : availableRows) {
            if (i == randomIndex) {
                toRemove = row;
                break;
            }
            i++;
        }
        availableRows.remove(toRemove);
        return createEnemy(Math.random(), toRemove);
    }

    private Enemy createEnemy(double randSide, int toRemove) {
        location = "" + toRemove;
        EntityOrientation orientation;
        float xPosition;
        float yPosition;
        if (randSide >= 0.5) {// spawn left
            location += "left";
            orientation = EntityOrientation.RIGHT;
        } else { // spawn right
            location += "right";
            orientation = EntityOrientation.LEFT;
        }
        // get desiered spawn point location by name
        MapObject objects = map.getLayers().get("SpawnPoints").getObjects().get(location);

        // parse x and y information of object on Tiled map (My spawn points)
        xPosition = Float.parseFloat(objects.getProperties().get("x").toString());
        yPosition = Float.parseFloat(objects.getProperties().get("y").toString());

        //define body def and set position, we divide by Pixel per meter unit so box2d body gets scaled
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(xPosition / PPM, yPosition / PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 12 / PPM, (float) 12 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        //filter bits categoryBits sets what this body us type of (type of enemy)
        fdef.filter.categoryBits = ENEMY_BIT;
        //filters with what enemies detect collision (with both player and projectile bits
        fdef.filter.maskBits = PROJECTILE_BIT | PLAYER_BIT;
        Enemy enemy = getEnemy(toRemove, body, orientation);
        body.createFixture(fdef).setUserData(enemy);
        return enemy;
    }

    private Enemy getEnemy(int toRemove, Body body, EntityOrientation orientation) {
        Enemy enemy = null;
        switch (getRandType()) {
            case BEETLE:
                enemy = new Enemy(body, toRemove);
                break;
            case VULTURE:
                enemy = new EnemyVulture(body, toRemove);
                break;
            case DINO:
                enemy = new EnemyDino(body, toRemove);
                break;
            case FROG:
                enemy = new EnemyFrog(body, toRemove);
                break;
        }
        enemy.setOrientation(orientation);
        enemy.setMoveForce(orientation);
        return enemy;
    }

    private EnemyTypes getRandType() {
        Random r = new Random();
        int randomType = r.nextInt(100);

        if (randomType < 50) { // 50%
            return EnemyTypes.BEETLE;
        } else if (randomType < 85) { // 35%
            return EnemyTypes.VULTURE;
        } else if (randomType < 90) { // 5%
            return EnemyTypes.FROG;
        } else { // 10%
            return EnemyTypes.DINO;
        }
    }

}

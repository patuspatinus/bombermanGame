package com.example.demo.Collisions;

import com.example.demo.components.Enemy.Balloon;
import com.example.demo.components.Enemy.Tiger;
import com.example.demo.components.Enemy.Water;
import com.example.demo.components.FlameComponent;
import com.example.demo.BombermanType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;

import static com.example.demo.constants.GameConst.ENEMY_SPEED;
import static com.example.demo.DynamicEntityState.State.*;
import static com.example.demo.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class FlameEnemy3Handler extends CollisionHandler {

    @Override
    protected void onCollision(Entity flame, Entity enemy) {
        if (enemy.getComponent(Tiger.class).getState() != DIE
                && flame.getComponent(FlameComponent.class).isActivation()) {
            enemy.getComponent(Tiger.class).setStateDie();
            inc("score", 5);
            getGameTimer().runOnceAfter(() -> {
                onTransform(enemy);
                enemy.removeFromWorld();
                set("enemies", getGameWorld().getGroup(BALLOOM_E,
                        WATER_E, TIGER_E, LANTERN_E, CLOUD_E).getSize());
            }, Duration.seconds(2.3));
        }
    }

    private void onTransform(Entity parent) {
        Entity child1 = spawn("water", new SpawnData(parent.getX(), parent.getY()));
        Entity child2 = spawn("balloon", new SpawnData(parent.getX(), parent.getY()));
        inc("enemies", 2);
        if (parent.getComponent(Tiger.class).getDx() == 0) {
            child1.getComponent(Water.class).setState(UP);
            child1.getComponent(Water.class).setDxDy(0, -ENEMY_SPEED);
            child2.getComponent(Balloon.class).setState(DOWN);
            child2.getComponent(Balloon.class).setDxDy(0, ENEMY_SPEED);
        } else if (parent.getComponent(Tiger.class).getDy() == 0) {
            child1.getComponent(Water.class).setState(LEFT);
            child1.getComponent(Water.class).setDxDy(-ENEMY_SPEED, 0);
            child2.getComponent(Balloon.class).setState(RIGHT);
            child2.getComponent(Balloon.class).setDxDy(ENEMY_SPEED, 0);
        }
    }

    public FlameEnemy3Handler() {
        super(BombermanType.FLAME, TIGER_E);
    }
}
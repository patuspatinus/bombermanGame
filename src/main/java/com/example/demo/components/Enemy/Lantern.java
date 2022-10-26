package com.example.demo.components.Enemy;

import com.example.demo.components.FlameComponent;
import javafx.util.Duration;

import static com.example.demo.constants.GameConst.ENEMY_SPEED;
import static com.example.demo.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;

public class Lantern extends Enemy {
    public Lantern() {
        super(-ENEMY_SPEED, 0, 2.25, 4.5, "enemy4.png");
        onCollisionBegin(LANTERN_E, BRICK, (lantern, brick) -> {
            lantern.getComponent(Lantern.class).turn();
        });
        onCollisionBegin(LANTERN_E, WALL, (lantern, wall) -> {
            lantern.getComponent(Lantern.class).turn();
        });
        onCollisionBegin(LANTERN_E, DOOR, (lantern, door) -> {
            lantern.getComponent(Lantern.class).turn();
        });
        onCollisionBegin(LANTERN_E, BOMB, (lantern, bomb) -> {
            lantern.getComponent(Lantern.class).turn();
        });
        onCollision(LANTERN_E, FLAME, (lantern, flame) -> {
            if(flame.getComponent(FlameComponent.class).isActivation()) {
                lantern.getComponent(Lantern.class).setStateDie();
                inc("score", 30);
                getGameTimer().runOnceAfter(() -> {
                    lantern.removeFromWorld();
                    set("enemies", getGameWorld().getGroup(BALLOOM_E,
                            WATER_E, TIGER_E, LANTERN_E, CLOUD_E).getSize());
                }, Duration.seconds(2.4));
            }
        });

    }

    @Override
    public void turn() {
        speedFactor = Math.random() > 0.8 ? 1 : 2.5;
        super.turn();
    }
}

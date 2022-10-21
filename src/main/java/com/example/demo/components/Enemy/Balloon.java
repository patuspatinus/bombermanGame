package com.example.demo.components.Enemy;

import com.example.demo.components.FlameComponent;
import javafx.util.Duration;

import static com.example.demo.constants.GameConst.ENEMY_SPEED;
import static com.example.demo.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Balloon extends Enemy {
    public Balloon() {
        super(-ENEMY_SPEED, 0, 1, 3, "enemy1.png");

        onCollisionBegin(BALLOOM_E, BRICK, (balloom, brick) -> {
            balloom.getComponent(Balloon.class).turn();
        });
        onCollisionBegin(BALLOOM_E, WALL, (balloom, wall) -> {
            balloom.getComponent(Balloon.class).turn();
        });
        onCollisionBegin(BALLOOM_E, BOMB, (balloom, bomb) -> {
            balloom.getComponent(Balloon.class).turn();
        });
        onCollision(BALLOOM_E, FLAME, (balloom, flame) -> {
            if(flame.getComponent(FlameComponent.class).isActivation()) {
                balloom.getComponent(Balloon.class).setStateDie();
                getGameTimer().runOnceAfter(() -> {
                    balloom.removeFromWorld();
                    set("enemies", getGameWorld().getGroup(BALLOOM_E,
                            WATER_E,TIGER_E, LANTERN_E, CLOUD_E).getSize());
                }, Duration.seconds(2.4));
            }
        });
    }

}

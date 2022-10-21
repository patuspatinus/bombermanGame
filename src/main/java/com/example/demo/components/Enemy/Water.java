package com.example.demo.components.Enemy;

import com.example.demo.components.FlameComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;

import static com.example.demo.constants.GameConst.ENEMY_SPEED;
import static com.example.demo.constants.GameConst.SIZE_BLOCK;
import static com.example.demo.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Water extends Enemy {
    private boolean isCatching;

    public Water() {
        super(-ENEMY_SPEED, 0, 1, 3, "enemy2.png");
        isCatching = true;
        onCollisionBegin(WATER_E, BRICK, (water, brick) -> {
            water.getComponent(Water.class).turn();
        });
        onCollisionBegin(WATER_E, WALL, (water, wall) -> {
            water.getComponent(Water.class).turn();
        });
        onCollisionBegin(WATER_E, DOOR, (water, door) -> {
            water.getComponent(Water.class).turn();
        });
        onCollisionBegin(WATER_E, BOMB, (water, bomb) -> {
            water.getComponent(Water.class).turn();
        });
        onCollision(WATER_E, FLAME, (water, flame) -> {
            if(flame.getComponent(FlameComponent.class).isActivation()) {
                water.getComponent(Water.class).setStateDie();
                getGameTimer().runOnceAfter(() -> {
                    water.removeFromWorld();
                    set("enemies", getGameWorld().getGroup(BALLOOM_E,
                            WATER_E,TIGER_E, LANTERN_E, CLOUD_E).getSize());
                }, Duration.seconds(2.4));
            }
        });

    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);

        Entity player = getGameWorld().getSingleton(PLAYER);

        int playerCellX = (int) (player.getX() / SIZE_BLOCK);
        int playerCellY = (int) (player.getY() / SIZE_BLOCK);
        int enemyCellY = (int) (entity.getY() / SIZE_BLOCK);
        int enemyCellX = (int) (entity.getX() / SIZE_BLOCK);
        if (getEntity().distance(player) < SIZE_BLOCK * 3) {
            if (isCatching == true) {
                if (dx == 0) {
                    if ((entity.getY() - player.getY()) * dy < 0) {
                        speedFactor = 1.3;
                    } else {
                        speedFactor = 1;
                    }

                    if (enemyCellY == playerCellY) {
                        if (player.getX() > entity.getX()) {
                            turnRight();
                        } else {
                            turnLeft();
                        }
                    }
                } else if (dy == 0) {
                    if ((entity.getX() - player.getX()) * dx < 0) {
                        speedFactor = 1.3;
                    } else {
                        speedFactor = 1;
                    }

                    if (enemyCellX == playerCellX) {
                        if (player.getY() > entity.getY()) {
                            turnDown();
                        } else {
                            turnUp();
                        }
                    }
                }
            } else if (dx == 0 && ((int) entity.getY() % SIZE_BLOCK <= 5 && (int) entity.getY() % SIZE_BLOCK > 0)) {
                isCatching = true;
            } else if (dy == 0 && ((int) entity.getX() % SIZE_BLOCK <= 5 && (int) entity.getY() % SIZE_BLOCK > 0)) {
                isCatching = true;
            }
        } else {
            speedFactor = 1;
            isCatching = true;
        }

    }

    @Override
    public void turn() {
        isCatching = false;
        super.turn();
    }
}
package com.example.demo.components.Enemy;

import com.example.demo.components.FlameComponent;
import com.example.demo.components.PlayerComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;

import static com.example.demo.constants.GameConst.ENEMY_SPEED;
import static com.example.demo.constants.GameConst.SIZE_BLOCK;
import static com.example.demo.DynamicEntityState.State.DIE;
import static com.example.demo.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Cloud extends Enemy {
    private boolean isCatching;

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);

        Entity player = getGameWorld().getSingleton(PLAYER);

        if (state == DIE || player
                .getComponent(PlayerComponent.class)
                .getState() == DIE) {
            return;
        }

        int playerCellX = (int) (player.getX() / SIZE_BLOCK);
        int playerCellY = (int) (player.getY() / SIZE_BLOCK);
        int enemyCellY = (int) (entity.getY() / SIZE_BLOCK);
        int enemyCellX = (int) (entity.getX() / SIZE_BLOCK);
        if (getEntity().distance(player) < SIZE_BLOCK * 5) {
            if (isCatching == true) {
                if (dx == 0) {
                    if ((entity.getY() - player.getY()) * dy < 0) {
                        speedFactor = 1.5;
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
                        speedFactor = 1.5;
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

    public Cloud() {
        super(-ENEMY_SPEED, 0, 1, 4, "enemy5.png");
        isCatching = true;

        onCollisionBegin(CLOUD_E, BRICK, (cloud, brick) -> {
            if (speedFactor == 1) {
                cloud.getComponent(Cloud.class).turn();
            }
        });
        onCollisionBegin(CLOUD_E, WALL, (cloud, wall) -> {
            cloud.getComponent(Cloud.class).turn();
        });
        onCollisionBegin(CLOUD_E, DOOR, (cloud, door) -> {
            cloud.getComponent(Cloud.class).turn();
        });
        onCollisionBegin(CLOUD_E, BOMB, (cloud, bomb) -> {
            cloud.getComponent(Cloud.class).turn();
        });
        onCollision(CLOUD_E, FLAME, (cloud, flame) -> {
            if(flame.getComponent(FlameComponent.class).isActivation()) {
                cloud.getComponent(Cloud.class).setStateDie();
                inc("score", 5);
                getGameTimer().runOnceAfter(() -> {
                    cloud.removeFromWorld();
                    set("enemies", getGameWorld().getGroup(BALLOOM_E,
                            WATER_E, TIGER_E, LANTERN_E, CLOUD_E).getSize());
                }, Duration.seconds(2.4));//2.4
            }
        });

    }

    @Override
    public void turn() {
        isCatching = false;
        super.turn();
    }
}

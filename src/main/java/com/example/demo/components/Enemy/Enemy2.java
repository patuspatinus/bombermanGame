package com.example.demo.components.Enemy;

import com.example.demo.components.FlameComponent;
import com.example.demo.components.PlayerComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;

import static com.example.demo.constants.GameConst.ENEMY_SPEED;
import static com.example.demo.constants.GameConst.SIZE_BLOCK;
import static com.example.demo.DynamicEntityState.State.DIE;
import static com.example.demo.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Enemy2 extends Enemy {
    private boolean isCatching;

    public Enemy2() {
        super(-ENEMY_SPEED, 0, 1, 3, "enemy2.png");
        isCatching = true;
        onCollisionBegin(ENEMY2, BRICK, (enemy2, brick) -> {
            enemy2.getComponent(Enemy2.class).turn();
        });
        onCollisionBegin(ENEMY2, WALL, (enemy2, wall) -> {
            enemy2.getComponent(Enemy2.class).turn();
        });
        onCollisionBegin(ENEMY2, DOOR, (enemy2, door) -> {
            enemy2.getComponent(Enemy2.class).turn();
        });
        onCollisionBegin(ENEMY2, BOMB, (enemy2, bomb) -> {
            enemy2.getComponent(Enemy2.class).turn();
        });
        onCollision(ENEMY2, FLAME, (enemy2, flame) -> {
            if(flame.getComponent(FlameComponent.class).isActivation()) {
                enemy2.getComponent(Enemy2.class).setStateDie();
                getGameTimer().runOnceAfter(() -> {
                    enemy2.removeFromWorld();
                    set("enemies", getGameWorld().getGroup(ENEMY1,
                            ENEMY2).getSize());
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
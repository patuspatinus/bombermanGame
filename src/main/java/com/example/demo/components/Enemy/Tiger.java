package com.example.demo.components.Enemy;

import com.example.demo.Collisions.FlameEnemy3Handler;
import com.almasb.fxgl.physics.PhysicsWorld;

import static com.example.demo.constants.GameConst.ENEMY_SPEED;
import static com.example.demo.BombermanType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class Tiger extends Enemy {
    public Tiger() {
        super(-ENEMY_SPEED, 0, 1,3, "enemy3.png");
        PhysicsWorld physics = getPhysicsWorld();

        onCollisionBegin(TIGER_E, BRICK, (tiger, brick) -> {
            tiger.getComponent(Tiger.class).turn();
        });
        onCollisionBegin(TIGER_E, WALL, (tiger, wall) -> {
            tiger.getComponent(Tiger.class).turn();
        });
        onCollisionBegin(TIGER_E, DOOR, (tiger, door) -> {
            tiger.getComponent(Tiger.class).turn();
        });
        onCollisionBegin(TIGER_E, BOMB, (tiger, bomb) -> {
            tiger.getComponent(Tiger.class).turn();
        });

        physics.addCollisionHandler(new FlameEnemy3Handler());
    }

}
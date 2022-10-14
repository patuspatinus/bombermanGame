package com.example.demo;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.example.demo.components.BombComponent;
import com.example.demo.components.PlayerComponent;
import com.example.demo.components.FlameComponent;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.example.demo.BombermanConstant.TILED_SIZE;
import static com.example.demo.constants.GameConst.*;

public class BombermanFactory implements EntityFactory {

    private final int radius = SIZE_BLOCK / 2;

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef().friction(0).density(0.1f));
        BodyDef bd = new BodyDef();
        bd.setFixedRotation(true);
        bd.setType(BodyType.DYNAMIC);
        physics.setBodyDef(bd);


        return entityBuilder(data)
                .type(BombermanType.PLAYER)
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(20)))
                .with(physics)
                .with(new PlayerComponent())
                .with(new CollidableComponent(true))
                .zIndex(5)
                .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.WALL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(new Rectangle(1488, 624, Color.FORESTGREEN))
                .zIndex(-100)
                .with(new IrremovableComponent())
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        var width = (int) data.get("width");
        var height = (int) data.get("height");

        return FXGL.entityBuilder(data)
                .view("brick.png")
                .type(BombermanType.BRICK)
                .bbox(new HitBox(BoundingShape.box(width, height)))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("bomb")
    public Entity newBomb(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.BOMB)
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(22)))
                .with(new BombComponent())
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }


    @Spawns("brick_break")
    public Entity newBrickBreak(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.BRICK_BREAK)
                .with(new com.example.demo.components.BrickBreakComponent())
                .viewWithBBox(new Rectangle(TILED_SIZE, TILED_SIZE, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .zIndex(1)
                .build();
    }


    @Spawns("central_flame")
    public Entity newCFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.box(TILED_SIZE - 4, TILED_SIZE - 4)))
                .with(new FlameComponent("central_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("top_down_flame")
    public Entity newTDFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(2, TILED_SIZE - data.getZ() - 3), BoundingShape.box(TILED_SIZE - 4, data.getZ())))
                .with(new FlameComponent("top_down_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("top_up_flame")
    public Entity newTUFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(2, 3), BoundingShape.box(TILED_SIZE - 4, data.getZ())))
                .with(new FlameComponent("top_up_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("top_right_flame")
    public Entity newTRFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(TILED_SIZE - data.getZ() - 3, 2), BoundingShape.box(data.getZ(), TILED_SIZE - 4)))
                .with(new FlameComponent("top_right_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("top_left_flame")
    public Entity newTLFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(3, 2), BoundingShape.box(data.getZ(), TILED_SIZE - 4)))
                .with(new FlameComponent("top_left_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("up_flame")
    public Entity newUFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(2, 3), BoundingShape.box(TILED_SIZE - 4, data.getZ())))
                .with(new FlameComponent("up_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("down_flame")
    public Entity newDFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(2, TILED_SIZE - data.getZ() - 3), BoundingShape.box(TILED_SIZE - 4, data.getZ())))
                .with(new FlameComponent("down_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("left_flame")
    public Entity newLFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(3, 2), BoundingShape.box(data.getZ(), TILED_SIZE - 4)))
                .with(new FlameComponent("left_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }

    @Spawns("right_flame")
    public Entity newRFlame(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.FLAME)
                .bbox(new HitBox(new Point2D(TILED_SIZE - data.getZ() - 3, 2), BoundingShape.box(data.getZ(), TILED_SIZE - 4)))
                .with(new FlameComponent("right_flame.png"))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(-1)
                .build();
    }
}

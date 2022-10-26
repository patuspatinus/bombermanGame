package com.example.demo.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.demo.DynamicEntityState.State;

import static com.example.demo.BombermanConstant.TILED_SIZE;
import static com.example.demo.BombermanType.*;


import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;


public class PlayerComponent extends Component {
    private final int FRAME_SIZE = 45;

    private boolean bombInvalidation;
    private int bombCounter;

    public enum PlayerSkin {
        NORMAL, GOLD;
    }

    private PlayerSkin playerSkin;

    private State state = State.STOP;
    private PhysicsComponent physics;
    private AnimatedTexture texture;
    private AnimationChannel animDie;
    private AnimationChannel animIdleDown, animIdleRight, animIdleUp, animIdleLeft;
    private AnimationChannel animWalkDown, animWalkRight, animWalkUp, animWalkLeft;

    public PlayerComponent() {
        setSkin(PlayerSkin.NORMAL);
        texture = new AnimatedTexture(animIdleDown);
        PhysicsWorld physics = getPhysicsWorld();
        physics.setGravity(0, 0);

        bombCounter = 0;


        onCollisionBegin(PLAYER, FLAME_ITEM, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            inc("flame", 1);
        });
        onCollisionBegin(PLAYER, BOMB_ITEM, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            inc("bomb", 1);
        });
        onCollisionBegin(PLAYER, SPEED_ITEM, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            handlePowerUpSpeed();
        });
        onCollisionBegin(PLAYER, LIFE_ITEM, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            inc("life", 1);
        });
    }

    private void setSkin(PlayerSkin skin) {
        playerSkin = skin;
        if (playerSkin == PlayerSkin.NORMAL) {
            animDie = new AnimationChannel(image("player_die.png"), 5, FRAME_SIZE, FRAME_SIZE, Duration.seconds(3.5), 0, 4);

            animIdleDown = new AnimationChannel(image("player_down.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            animIdleRight = new AnimationChannel(image("player_right.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            animIdleUp = new AnimationChannel(image("player_up.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            animIdleLeft = new AnimationChannel(image("player_left.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);

            animWalkDown = new AnimationChannel(image("player_down.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            animWalkRight = new AnimationChannel(image("player_right.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            animWalkUp = new AnimationChannel(image("player_up.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            animWalkLeft = new AnimationChannel(image("player_left.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);

        }
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {

        if (physics.getVelocityX() != 0) {

            physics.setVelocityX((int) physics.getVelocityX() * 0.9);

            if (FXGLMath.abs(physics.getVelocityX()) < 1) {
                physics.setVelocityX(0);
            }
        }

        if (physics.getVelocityY() != 0) {

            physics.setVelocityY((int) physics.getVelocityY() * 0.9);

            if (FXGLMath.abs(physics.getVelocityY()) < 1) {
                physics.setVelocityY(0);
            }
        }

        switch (state) {
            case UP:
                texture.loopNoOverride(animWalkUp);
                break;
            case RIGHT:
                texture.loopNoOverride(animWalkRight);
                break;
            case DOWN:
                texture.loopNoOverride(animWalkDown);
                break;
            case LEFT:
                texture.loopNoOverride(animWalkLeft);
                break;
            case STOP:
                if (texture.getAnimationChannel() == animWalkDown) {
                    texture.loopNoOverride(animIdleDown);
                } else if (texture.getAnimationChannel() == animWalkUp) {
                    texture.loopNoOverride(animIdleUp);
                } else if (texture.getAnimationChannel() == animWalkLeft) {
                    texture.loopNoOverride(animIdleLeft);
                } else if (texture.getAnimationChannel() == animWalkRight) {
                    texture.loopNoOverride(animIdleRight);
                }
                break;
            case DIE:
                texture.loopNoOverride(animDie);
                break;
        }
    }

    public void up() {
        if (state != State.DIE) {
            state = State.UP;
            physics.setVelocityY(-geti("speed"));
            //play("updown_walk.wav");
        }
    }

    public void down() {
        if (state != State.DIE) {
            state = State.DOWN;
            physics.setVelocityY(geti("speed"));
            //play("updown_walk.wav");
        }
    }

    public void left() {
        if (state != State.DIE) {
            state = State.LEFT;
            physics.setVelocityX(-geti("speed"));
            //play("leftright_walk.wav");
        }
    }

    public void right() {
        if (state != State.DIE) {
            state = State.RIGHT;
            physics.setVelocityX(geti("speed"));
            //play("leftright_walk.wav");
        }
    }

    public void stop() {
        if (state != State.DIE) {
            state = State.STOP;
            physics.setVelocityY(0);
            physics.setVelocityX(0);
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void handlePowerUpSpeed() {
        //play("powerup.wav");
        inc("speed", 50);
        getGameTimer().runOnceAfter(() -> {
            inc("speed", -50);
        }, Duration.seconds(6));
    }

    public void placeBomb(int flames) {
        if (state != State.DIE) {
            play("bomb_placed.wav");
            if (bombCounter == geti("bomb")) {
                return;
            }
            bombCounter++;

            int bombLocationX = (int) (entity.getX() % TILED_SIZE > TILED_SIZE / 2
                    ? entity.getX() + TILED_SIZE - entity.getX() % TILED_SIZE + 1
                    : entity.getX() - entity.getX() % TILED_SIZE + 1);
            int bombLocationY = (int) (entity.getY() % TILED_SIZE > TILED_SIZE / 2
                    ? entity.getY() + TILED_SIZE - entity.getY() % TILED_SIZE + 1
                    : entity.getY() - entity.getY() % TILED_SIZE + 1);

            Entity bomb = spawn("bomb", new SpawnData(bombLocationX, bombLocationY));
            getGameTimer().runOnceAfter(() -> {
                if (!bombInvalidation) {
                    bomb.getComponent(BombComponent.class).explode(flames);
                    play("bomb_explode.wav");

                } else {
                    bomb.removeFromWorld();
                }
                bombCounter--;
            }, Duration.seconds(2.1));
        }
    }

    public void setBombInvalidation(boolean bombInvalidation) {
        this.bombInvalidation = bombInvalidation;
    }
}
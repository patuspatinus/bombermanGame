package com.example.demo.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {
    private final int FRAME_SIZE = 45;

    public enum State {
        UP, RIGHT, DOWN, LEFT, STOP
    }

    public enum PlayerSkin {
        NORMAL, GOLD;
    }

    private PlayerSkin playerSkin;

    private State state;
    private PhysicsComponent physics;
    private AnimatedTexture texture;
    private AnimationChannel animIdleDown, animIdleRight, animIdleUp, animIdleLeft;
    private AnimationChannel animWalkDown, animWalkRight, animWalkUp, animWalkLeft;

    public PlayerComponent() {
        state = State.STOP;
        setSkin(PlayerSkin.NORMAL);
        texture = new AnimatedTexture(animIdleDown);
    }

    private void setSkin(PlayerSkin skin) {
        playerSkin = skin;
        if (playerSkin == PlayerSkin.NORMAL) {
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
        }
    }

    public void up() {
        state = State.UP;
        physics.setVelocityY(-70);

    }

    public void down() {
        state = State.DOWN;
        physics.setVelocityY(70);
    }

    public void left() {
        state = State.LEFT;
        physics.setVelocityX(-70);
    }

    public void right() {
        state = State.RIGHT;
        physics.setVelocityX(70);
    }

    public void stop() {
        state = State.STOP;
        physics.setVelocityY(0);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
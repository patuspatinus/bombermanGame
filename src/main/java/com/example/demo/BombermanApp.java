package com.example.demo;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.example.demo.Menu.GameMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import com.example.demo.components.PlayerComponent;
import com.example.demo.BombermanType;
import com.example.demo.constants.GameConst;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;

public class BombermanApp extends GameApplication {
    private Map temp = new HashMap();
    private boolean isLoading = false;

    public Map getTemp() {
        return temp;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(624);
        settings.setWidth(1488);
        settings.setSceneFactory(new SceneFactory());

        settings.setIntroEnabled(false);
        settings.setGameMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setFontUI("assets/fonts/game_font.ttf");
        settings.setSceneFactory(new SceneFactory() {

            @Override
            public FXGLMenu newGameMenu() {
                return new GameMenu();
            }
        });
    }

    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BombermanFactory());
        FXGL.setLevelFromMap("map1.tmx");
        FXGL.spawn("background");
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("flame", 1);
        vars.put("bomb", 1);
    }

    private Entity getPlayer() {
        return getGameWorld().getSingleton(BombermanType.PLAYER);
    }

    private PlayerComponent getPlayerComponent() {
        return getPlayer().getComponent(PlayerComponent.class);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                getPlayerComponent().up();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.W);


        getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                getPlayerComponent().down();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getPlayerComponent().left();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getPlayerComponent().right();
            }

            @Override
            protected void onActionEnd() {
                getPlayerComponent().stop();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
                getPlayerComponent().placeBomb(geti("flame"));
            }
        }, KeyCode.SPACE);

    }

    protected void initPhysics() {
        PhysicsWorld physics = FXGL.getPhysicsWorld();
        physics.setGravity(0, 0);

        /*onCollision(PLAYER, FLAME, (player, flame) -> {
            if (flame.getComponent(FlameComponent.class).isActivation()
                    && getPlayerComponent().getPlayerSkin() == PlayerSkin.NORMAL
                    && getPlayerComponent().getState() != DIE) {
                onPlayerDied();
            }
        });*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example.demo;

import com.example.demo.Menu.MainMenu;
import com.example.demo.components.Enemy.*;
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
import com.example.demo.constants.GameConst.*;
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
import static com.example.demo.BombermanType.ENEMY1;

public class BombermanApp extends GameApplication {
    private Map temp = new HashMap();
    private boolean isLoading = false;

    public Map getTemp() {
        return temp;
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(GameConst.SCREEN_WIDTH);
        gameSettings.setHeight(GameConst.SCREEN_HEIGHT);
        gameSettings.setTitle(GameConst.GAME_TITLE);
        gameSettings.setVersion(GameConst.GAME_VERSION);

        //gameSettings.setFullScreenAllowed(true);
        //gameSettings.setFullScreenFromStart(true);

        gameSettings.setIntroEnabled(false);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setFontUI("game_font.ttf");
        gameSettings.setSceneFactory(new SceneFactory() {

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

//    private void setLevel() {
//        isLoading = false;
//        setLevelFromMap("level" + geti("level") + ".tmx");
//        Viewport viewport = getGameScene().getViewport();
//        viewport.setBounds(0, 0, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
//        viewport.bindToEntity(getPlayer(), getAppWidth() / 2, getAppHeight() / 2);
//        viewport.setLazy(true);
//
//        set("enemies", getGameWorld().getGroup(ENEMY1.Cla);
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
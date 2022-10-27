package com.example.demo;

import com.example.demo.UI.UIComponents;
import com.example.demo.components.Enemy.*;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.example.demo.Menu.GameMenu;
import javafx.scene.input.KeyCode;
import com.example.demo.components.PlayerComponent;
import com.example.demo.constants.GameConst;
import com.example.demo.DynamicEntityState.State;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.example.demo.constants.GameConst.*;
import static com.example.demo.Sounds.SoundEffect.*;

public class BombermanApp extends GameApplication {

    private AStarGrid grid;
    private Map temp = new HashMap();
    private boolean isLoading = false;

    public Map getTemp() {
        return temp;
    }

    public AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(GameConst.SCREEN_WIDTH);
        gameSettings.setHeight(GameConst.SCREEN_HEIGHT);
        gameSettings.setTitle(GameConst.GAME_TITLE);
        gameSettings.setVersion(GameConst.GAME_VERSION);

        gameSettings.setIntroEnabled(false);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setSceneFactory(new SceneFactory() {

            @Override
            public FXGLMenu newGameMenu() {
                return new GameMenu();
            }

        });
    }

    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BombermanFactory());
        setLevel();


    }

    protected void setLevel() {
        isLoading = false;
        FXGL.setLevelFromMap("map1.tmx");
        FXGL.spawn("background");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(0, 0, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        viewport.bindToEntity(getPlayer(), getAppWidth()/ 2, getAppHeight());
        viewport.setLazy(true);

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

    @Override
    protected void initPhysics() {


         onCollisionBegin(BombermanType.PLAYER, BombermanType.DOOR, (player, door) -> {
            if (isLoading == false && getGameWorld().getGroup(BombermanType.BALLOOM_E, BombermanType.WATER_E,
                    BombermanType.CLOUD_E, BombermanType.LANTERN_E, BombermanType.TIGER_E).getSize() == 0) {
                isLoading = true;
            getPlayerComponent().setBombInvalidation(true);
            turnOffMusic();
            getGameTimer().runOnceAfter(() -> {
                //turnOnMusic();
                showMessage("You win !!!", () -> getGameController().gotoMainMenu());
            }, Duration.seconds(0.1));
            }
         });

        onCollisionBegin(BombermanType.PLAYER, BombermanType.BALLOOM_E, (player, enemy) -> {
            if (enemy.getComponent(Balloon.class).getState() != State.DIE
                    && getPlayerComponent().getState() != State.DIE) {
                onPlayerDied();
            }
        });
        onCollisionBegin(BombermanType.PLAYER, BombermanType.WATER_E, (player, enemy) -> {
            if (enemy.getComponent(Water.class).getState() != State.DIE
                    && getPlayerComponent().getState() != State.DIE) {
                onPlayerDied();
            }
        });

         onCollisionBegin(BombermanType.PLAYER, BombermanType.CLOUD_E, (player, enemy) -> {
            if (enemy.getComponent(Cloud.class).getState() != State.DIE
                    && getPlayerComponent().getState() != State.DIE) {
                onPlayerDied();
            }
         });
         onCollisionBegin(BombermanType.PLAYER, BombermanType.LANTERN_E, (player, enemy) -> {
             if (enemy.getComponent(Lantern.class).getState() != State.DIE
                    && getPlayerComponent().getState() != State.DIE) {
                onPlayerDied();
         }
         });
         onCollisionBegin(BombermanType.PLAYER, BombermanType.TIGER_E, (player, enemy) -> {
            if (enemy.getComponent(Tiger.class).getState() != State.DIE
                    && getPlayerComponent().getState() != State.DIE) {
                onPlayerDied();
         }
         });

        onCollisionBegin(BombermanType.PLAYER, BombermanType.FLAME, (player, flame) -> {
            if (getPlayerComponent().getState() != State.DIE) {
                onPlayerDied();
            }
        });
    }

    @Override
    protected void onPreInit() {
        loopBGM("theme.mp3");
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("flame", 1);
        vars.put("bomb", 1);
        vars.put("life", 3);
        vars.put("speed", SPEED);
        vars.put("enemies", 8);
        vars.put("score", 0);
        vars.put("immortality", false);
    }

    public void onPlayerDied() {
        if (!getb("immortality")) {
            play("player_die.wav");
            isLoading = true;
            getPlayerComponent().setState(State.DIE);
            getPlayerComponent().setBombInvalidation(true);
            inc("life", -1);
            getGameTimer().runOnceAfter(() -> {
                getGameScene().getViewport().fade(() -> {
                    if (geti("life") > 0) {
                        getPlayerComponent().setBombInvalidation(false);
                        setLevel();
                    } else {
                        turnOffMusic();
                        showMessage("Game Over !!!", () -> getGameController().gotoMainMenu());
                    }
                });
            }, Duration.seconds(2.2));
        }
    }

    @Override
    protected void initUI() {
        UIComponents.addILabelUI("life", "💜 %d", 6,18 );
        UIComponents.addILabelUI("bomb", "💣 %d", 54, 18);
        UIComponents.addILabelUI("flame", "🔥 %d", 102, 18);
        UIComponents.addILabelUI("speed", "👟%d", 142, 18);
        UIComponents.addILabelUI("enemies", "👻 %d", 200, 18);
        UIComponents.addILabelUI("score", "💰 %d", 240, 18);
    }
    private void setGridForAi() {
        AStarGrid _grid = AStarGrid.fromWorld(getGameWorld(), 31, 15,
                SIZE_BLOCK, SIZE_BLOCK, (type) -> {
                    if (type == BombermanType.AROUND_WALL || type == BombermanType.WALL) {
                        return CellState.NOT_WALKABLE;
                    } else {
                        return CellState.WALKABLE;
                    }
                });

        grid = AStarGrid.fromWorld(getGameWorld(), 31, 15,
                SIZE_BLOCK, SIZE_BLOCK, (type) -> {
                    if (type == BombermanType.BRICK
                            || type == BombermanType.WALL) {
                        return CellState.NOT_WALKABLE;
                    } else {
                        return CellState.WALKABLE;
                    }
                });

        set("grid", grid);
        set("_grid", _grid);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
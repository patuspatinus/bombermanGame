package com.example.demo.Menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.input.view.KeyView;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;

public class MainMenu extends FXGLMenu {
    public MainMenu() {
        super(MenuType.MAIN_MENU);
        ImageView background = new ImageView();
        background.setImage(new Image("assets/textures/main_background.png"));


        var title_app = getUIFactoryService().newText(getSettings().getTitle(), Color.rgb(248, 185, 54), 130);
        centerTextBind(title_app, getAppWidth() / 2.0, 300);

        var version_app = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 25);
        version_app.setEffect(new DropShadow(3, 3, 3, Color.WHITE));
        centerTextBind(version_app, 860, 250);

        var menuBox_app = new VBox(
                new MenuButton("New Game", 27, () -> newGame()),
                new MenuButton("Control", 27, () -> instruct()),
                new MenuButton("Exit", 27, () -> fireExit())
        );

        menuBox_app.setAlignment(Pos.CENTER_LEFT);
        menuBox_app.setTranslateX(getAppWidth() * 0.35);
        menuBox_app.setTranslateY(getAppHeight() / 2.0 + 60);
        menuBox_app.setSpacing(20);

        getContentRoot().getChildren().addAll(background, menuBox_app);
    }

    private void instruct() {
        GridPane pane = new GridPane();
        pane.addRow(0, getUIFactoryService().newText(" Movement      "),
                new HBox(new KeyView(W), new KeyView(S), new KeyView(A), new KeyView(D)));
        pane.addRow(1, getUIFactoryService().newText(" Placed   Bomb      "),
                new KeyView(SPACE));
    }

    private Runnable action;
    public void newGame() {
        fireNewGame();
        getGameTimer().runOnceAfter(() -> {
            action.run();
        }, Duration.millis(10));
    }
}
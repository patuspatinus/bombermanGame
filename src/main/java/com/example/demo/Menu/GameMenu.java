package com.example.demo.Menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.geometry.Pos;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static com.example.demo.constants.GameConst.SCREEN_HEIGHT;
import static com.example.demo.constants.GameConst.SCREEN_WIDTH;
//import static Bomberman.Sounds.SoundEffect.setSoundSwitch;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.centerTextBind;

public class GameMenu extends FXGLMenu {
    public GameMenu() {
        super(MenuType.GAME_MENU);
        Shape shape = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT, Color.GREY);
        shape.setOpacity(0.5);

        ImageView background = new ImageView();
        background.setImage(new Image("assets/textures/esc_background.png"));


        var menuBox = new VBox(
                new MenuButton("Resume", 20, () -> fireResume()),
                //new MenuButton("Sound", 20, () -> setSoundSwitch()),
                new MenuButton("Menu", 20, () -> fireExitToMainMenu()),
                new MenuButton("Exit", 20, () -> fireExit())
        );

        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setTranslateX(getAppWidth() / 2.0 );
        menuBox.setTranslateY(getAppHeight() / 2.0 + 50);
        menuBox.setSpacing(20);

        getContentRoot().getChildren().addAll(background, menuBox);
    }

}
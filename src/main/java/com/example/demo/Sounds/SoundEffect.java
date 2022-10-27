package com.example.demo.Sounds;

import static com.example.demo.constants.GameConst.MAX_VOLUME;
import static com.almasb.fxgl.dsl.FXGL.getSettings;
import static com.almasb.fxgl.dsl.FXGL.showMessage;

public class SoundEffect {
    public static boolean isSoundEnabled = true;
    public static boolean isMusicEnabled = true;

    public static void turnOffMusic() {
        if (!isSoundEnabled) {
            return;
        }
        if (isMusicEnabled) {
            getSettings().setGlobalMusicVolume(0.0);
            isMusicEnabled = false;
        }
    }
}
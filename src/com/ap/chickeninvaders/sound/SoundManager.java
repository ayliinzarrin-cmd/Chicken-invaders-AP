package com.ap.chickeninvaders.sound;

import com.ap.chickeninvaders.model.User;

import java.awt.*;

public class SoundManager {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void playShot() {
        if (user != null && user.isShotSoundOn()) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void playExplosion() {
        if (user != null && user.isExplosionSoundOn()) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void playEnd() {
        if (user != null && user.isEndSoundOn()) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void playMenuBeep() {
        if (user != null && user.isMusicOn()) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}


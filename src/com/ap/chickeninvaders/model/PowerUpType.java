package com.ap.chickeninvaders.model;

import java.awt.*;

public enum PowerUpType {
    ADD_FIRE("Fire+", new Color(255, 110, 80)),
    RAPID_FIRE("Rapid", new Color(255, 215, 60)),
    EXTRA_LIFE("Life", new Color(80, 230, 120)),
    SHIELD("Shield", new Color(120, 160, 255)),
    FREEZE_BOMB("Freeze", new Color(80, 220, 255));

    private final String label;
    private final Color color;

    PowerUpType(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }
}


package com.example.game2048;

import java.awt.*;

public class Tile {
    private int value;
    private final Color color;
    private final Color fontColor;

    public Tile(int value) {
        this.value = value;
        this.color = getTileColor(value);
        this.fontColor = getFontColor(value);
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public Color getFontColor() {
        return fontColor;
    }

    private Color getTileColor(int value) {
        switch (value) {
            case 2: return new Color(0xeee4da);
            case 4: return new Color(0xede0c8);
            case 8: return new Color(0xf2b179);
            case 16: return new Color(0xf59563);
            case 32: return new Color(0xf67c5f);
            case 64: return new Color(0xf65e3b);
            case 128: return new Color(0xedcf72);
            case 256: return new Color(0xedcc61);
            case 512: return new Color(0xedc850);
            case 1024: return new Color(0xedc53f);
            case 2048: return new Color(0xedc22e);
        }
        return new Color(0xcdc1b4);
    }

    private Color getFontColor(int value) {
        return value < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);
    }
}

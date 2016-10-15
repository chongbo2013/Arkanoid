package com.arkanoid;

/**
 * Created by Sebas on 10/10/2016.
 */

public class Block {
    private String color;
    private int x;
    private int y;
    private int repeatX;
    private int repeatY;
    private int quantity;

    public Block(String color, int x, int y, int repeatX, int repeatY, int quantity) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.repeatX = repeatX;
        this.repeatY = repeatY;
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRepeatX() {
        return repeatX;
    }

    public void setRepeatX(int repeatX) {
        this.repeatX = repeatX;
    }

    public int getRepeatY() {
        return repeatY;
    }

    public void setRepeatY(int repeatY) {
        this.repeatY = repeatY;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

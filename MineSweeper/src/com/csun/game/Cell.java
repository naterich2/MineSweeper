package com.csun.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Cell {
    private int x;
    private int y;
    private int value = 0;
    private boolean covered;
    private boolean mined;
    private boolean marked;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
        this.covered = true;
        this.mined = false;
        this.marked = false;
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
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public boolean isCovered() {
        return covered;
    }
    public void setCovered(boolean state) {
        this.covered = state;
    }

    public boolean isMined() {
        return mined;
    }
    public void setMined(boolean state) {
        this.mined = state;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
    
    public void draw(Graphics g) {
        if (covered && !marked) {
            drawCell(Board.cache.get(Constants.Cell.BLANK),g,x,y);
        }else if (covered && marked) {
            drawCell(Board.cache.get(Constants.Cell.BOMB_FLAGGED),g,x,y);
        }else if (!covered && mined) {
            if (value != -1){
                drawCell(Board.cache.get(Constants.Cell.BOMB_REVEALED),g,x,y);
            }if (value == -1){
                drawCell(Board.cache.get(Constants.Cell.BOMB_DEATH),g,x,y);
            }
        }else if (!covered && !mined) {
            drawAdjacentCount(g);
        }
    }

    public static void drawCell(BufferedImage image,Graphics g, int x, int y) {
        g.drawImage(image, DrawInfo.OFFSET_X + x*DrawInfo.CELL_LENGTH, 
                DrawInfo.OFFSET_Y+ y*DrawInfo.CELL_LENGTH,
                DrawInfo.CELL_LENGTH,DrawInfo.CELL_LENGTH, null);

    }

    private void drawAdjacentCount(Graphics g) {
        switch (value) {
            case 0: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_ZERO),g,x,y);
                break;
            case 1: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_ONE),g,x,y);
                break;
            case 2: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_TWO),g,x,y);
                break;
            case 3: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_THREE),g,x,y);
                break;
            case 4: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_FOUR),g,x,y);
                break;
            case 5: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_FIVE),g,x,y);
                break;
            case 6: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_SIX),g,x,y);
                break;
            case 7: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_SEVEN),g,x,y);
                break;
            case 8: 
                drawCell(Board.cache.get(Constants.Cell.OPEN_EIGHT),g,x,y);
                break;
            case -1: 
                drawCell(Board.cache.get(Constants.Cell.BOMB_DEATH),g,x,y);
                break;
        }
    }
}

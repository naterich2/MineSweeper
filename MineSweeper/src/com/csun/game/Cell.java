package com.csun.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Cell implements CellAlgorithm{
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

    public void drawCell(Graphics g, DrawInfo info) {
        if (covered && !marked) {
            myDraw(Board.cache.get(Constants.Cell.BLANK),g,info,x,y);
        }else if (covered && marked) {
            myDraw(Board.cache.get(Constants.Cell.BOMB_FLAGGED),g,info,x,y);
        }else if (!covered && mined) {
            if (value != -1){
                myDraw(Board.cache.get(Constants.Cell.BOMB_REVEALED),g,info,x,y);
            }if (value == -1){
                myDraw(Board.cache.get(Constants.Cell.BOMB_DEATH),g,info,x,y);
            }
        }else if (!covered && !mined) {
            drawAdjacentCount(g,info);
        }
    }

    public static void myDraw(BufferedImage image,Graphics g, DrawInfo info, int x, int y) {
        g.drawImage(image, info.getOffsetX()+ x*info.getCellLength(), 
                info.getOffsetY()+ y*info.getCellLength(),
                info.getCellLength(), info.getCellLength(), null);

    }

    private void drawAdjacentCount(Graphics g, DrawInfo info) {
        switch (value) {
            case 0: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_ZERO),g,info,x,y);
                break;
            case 1: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_ONE),g,info,x,y);
                break;
            case 2: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_TWO),g,info,x,y);
                break;
            case 3: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_THREE),g,info,x,y);
                break;
            case 4: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_FOUR),g,info,x,y);
                break;
            case 5: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_FIVE),g,info,x,y);
                break;
            case 6: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_SIX),g,info,x,y);
                break;
            case 7: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_SEVEN),g,info,x,y);
                break;
            case 8: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_EIGHT),g,info,x,y);
                break;
            case -1: 
                myDraw(Board.cache.get(Constants.Cell.BOMB_DEATH),g,info,x,y);
                break;
        }
    }

    public void changeState() {
        if (!marked){
            marked = true;
        }else {
            marked = false;
        }
    }
}

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
            myDraw(Board.cache.get(Constants.Cell.BLANK),g,info);
        }else if (covered && marked) {
            myDraw(Board.cache.get(Constants.Cell.BOMB_FLAGGED),g,info);
        }else if (!covered && mined) {
            if (value != -1){
                myDraw(Board.cache.get(Constants.Cell.BOMB_REVEALED),g,info);
            }if (value == -1){
                myDraw(Board.cache.get(Constants.Cell.BOMB_DEATH),g,info);
            }
        }else if (!covered && !mined) {
            drawAdjacentCount(g,info);
        }
    }

    private void myDraw(BufferedImage image,Graphics g, DrawInfo info) {
        g.drawImage(image, info.getOffsetX()+ this.x*info.getCellLength(), 
                info.getOffsetY()+ this.y*info.getCellLength(),
                info.getCellLength(), info.getCellLength(), null);

    }

    private void drawAdjacentCount(Graphics g, DrawInfo info) {
        switch (value) {
            case 0: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_ZERO),g,info);
                break;
            case 1: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_ONE),g,info);
                break;
            case 2: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_TWO),g,info);
                break;
            case 3: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_THREE),g,info);
                break;
            case 4: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_FOUR),g,info);
                break;
            case 5: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_FIVE),g,info);
                break;
            case 6: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_SIX),g,info);
                break;
            case 7: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_SEVEN),g,info);
                break;
            case 8: 
                myDraw(Board.cache.get(Constants.Cell.OPEN_EIGHT),g,info);
                break;
            case -1: 
                myDraw(Board.cache.get(Constants.Cell.BOMB_DEATH),g,info);
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

    @Override
    public void setFlag() {
        // TODO Auto-generated method stub

    }
}

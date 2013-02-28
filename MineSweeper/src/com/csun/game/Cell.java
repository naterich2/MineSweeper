package com.csun.game;

import java.awt.Graphics;

public class Cell implements Drawable{
    private int x;
    private int y;
    private int value = 0;
    private boolean state;
    
    public Cell(int x, int y, int value, boolean state) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.state = state;
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
    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    
    public void drawCell(Graphics g, DrawInfo info) {
    	g.drawImage(Board.cache.get(Constants.Cell.BLANK), info.getOffsetX()+ this.x*info.getCellLength(), 
    				info.getOffsetY()+ this.y*info.getCellLength(),
    				info.getCellLength(), info.getCellLength(), null);
    }
}

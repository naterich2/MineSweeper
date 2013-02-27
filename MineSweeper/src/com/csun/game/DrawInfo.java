package com.csun.game;

public class DrawInfo {

    private int offsetX;
    private int offsetY;
    private int cellLength;
    
    public DrawInfo() {
        
    }
    
    public DrawInfo(int offsetX, int offsetY, int cellLength) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.cellLength = cellLength;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getCellLength() {
        return cellLength;
    }

    public void setCellLength(int cellLength) {
        this.cellLength = cellLength;
    }
}

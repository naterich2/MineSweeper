package com.csun.game;

import java.awt.Graphics;

public interface CellAlgorithm {
    public void drawCell(Graphics g, DrawInfo info);
    public void changeState();
    
}

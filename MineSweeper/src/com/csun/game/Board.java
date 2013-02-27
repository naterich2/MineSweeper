package com.csun.game;

import java.awt.Graphics;

import javax.swing.*;

public class Board extends JPanel{
    private static final int cellLength = 20;
    private static DrawInfo info;
    private int row;
    private int column;
    private java.util.ArrayList<Cell> listCell;
    
    public Board (int offsetX, int offsetY, int row, int column) {
        info = new DrawInfo(offsetX,offsetY,Board.cellLength);
        this.row = row;
        this.column = column;
        setupLayout();
    }
    @Override
    public void paintComponent(Graphics g) {
        for (Cell c : listCell) {
            c.drawCell(g, info);
        }
    }
    
    private void setupLayout() {
       initializeComponents(); 
    }
    
    private void initializeComponents() {
       listCell = new java.util.ArrayList<Cell> ();
       for (int i=0; i<row; i++){
           for (int j=0; j<column; j++){
               listCell.add(new Cell(j,i,0,false));
           }
       }
    }
    
    public static void play() {
        JFrame frame = new JFrame("MineSweeper");
        frame.setContentPane(new Board(40,40,10,10));
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

package com.csun.game;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends JPanel {
    static String imageDirname = "MineImages";

    static Map<String, BufferedImage> cache;

    private static final int cellLength = 20;
    private static DrawInfo info;
    private int row;
    private int column;
    private java.util.ArrayList<Cell> listCell;

    /**
     * Test only
     */
    private boolean isTest = false;
    private BufferedImage testImage;

    public Board(int offsetX, int offsetY, int row, int column) {
        super(true);
        info = new DrawInfo(offsetX, offsetY, Board.cellLength);
        this.row = row;
        this.column = column;
        setupLayout();
    }

    public Board(BufferedImage testImage) {
        this.testImage = testImage;
        this.isTest = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isTest) {
            g.drawImage(
                    testImage, 10, 10, 100, 100, null);
        }
        else {
            for (Cell c : listCell) {
                c.drawCell(g, info);
            }
        }
    }

    private void setupLayout() {
        init();
        this.addMouseListener(new BoardListener());
    }

    private void init() {
        listCell = new java.util.ArrayList<Cell>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                listCell.add(new Cell(j, i));
            }
        }
        cache = new HashMap<String, BufferedImage>();
        loadImages();
        setMine(40);
    }

    private void loadImages() {
        BufferedImage images;
        File imageDir;
        File[] imageFiles;
        imageDir = new File(imageDirname);
        imageFiles = imageDir.listFiles();

        for (int i = 0; i < imageFiles.length; i++) {
            try {
                images = ImageIO.read(new FileInputStream(imageFiles[i]));
                cache.put(imageFiles[i].getName(), images);
            } catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(null, "File not found!!!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "File not found!!!");
            }
        }
    }

    private void setMine(int num) {
        for (int i=0; i<num; i++) {
            Cell c;
            c = listCell.get(new Random().nextInt(listCell.size()));
            while (c.isMined()){
                c = listCell.get(new Random().nextInt(listCell.size()));
            }
            c.setMined(true);
        }
    }

    public class BoardListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e){
            if (SwingUtilities.isLeftMouseButton(e)){
                actionLeftClick(e);
            }
            repaint();
        }
    }

    private Cell getCell(int x,int y){
        if (x<0 || y<0 || x>=column || y>=row){
            return null;
        } else {
            return listCell.get(y*column+x);
        }
    }

    private int getAdjMineCount(Cell c){
        int count = 0;
        Cell tempCell;
        if (c.isMined()){
            return -1;
        }else {
            for (int i=0; i<8; i++) {
                tempCell = getCell(c.getX() + Constants.dx[i],c.getY() + Constants.dy[i]);
                if ( tempCell != null) {
                    if (tempCell.isMined()){
                        count++;
                    }
                }
            }
            return count;
        }
    }

    private void uncoverAdjCell(Cell c) {
        if (c == null) {
            return;
        }
        if (c.getValue() != 0) {
            return;
        }
        for (int i=0; i<8; i++) {
            Cell tempCell = getCell(c.getX()+Constants.dx[i], c.getY() + Constants.dy[i]);
            if (tempCell != null){
                tempCell.setValue(getAdjMineCount(tempCell));
                if (tempCell.isCovered()) {
                    if (tempCell.getValue() != -1) {
                        tempCell.setCovered(false);
                        if (tempCell.getValue()==0){
                            uncoverAdjCell(tempCell);
                        }
                    }
                }
            }
        }
    }

    private void actionLeftClick(MouseEvent e) {
        int x = (e.getX()-info.getOffsetX())/column;
        int y = (e.getY()-info.getOffsetY())/row;

        Cell selectedCell = getCell(x,y);
        selectedCell.setCovered(false);
        selectedCell.setValue(getAdjMineCount(selectedCell));
        if (selectedCell.getValue() == 0) {
            uncoverAdjCell(selectedCell); 
        }

    }
    public static void play() {
        JFrame frame = new JFrame("MineSweeper");
        frame.setContentPane(new Board(40, 40, 20, 20));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

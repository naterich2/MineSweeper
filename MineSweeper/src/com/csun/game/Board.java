package com.csun.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends JPanel {
    static File imageDir;
    static String imageDirname = "MineImages";

    static String[] imageNames;
    static File[] imageFiles;
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
    }

    private void init() {
        listCell = new java.util.ArrayList<Cell>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                listCell.add(new Cell(j, i, 0, false));
            }
        }
        cache = new HashMap<String, BufferedImage>();
        BufferedImage images;
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

    public static void play() {
        JFrame frame = new JFrame("MineSweeper");
        frame.setContentPane(new Board(40, 40, 20, 20));
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

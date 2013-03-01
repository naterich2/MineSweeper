package com.csun.game;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private boolean isGameOver;
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
        listCell = new java.util.ArrayList<Cell>();
        cache = new HashMap<String, BufferedImage>();
        this.isGameOver = false;
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
            drawBorder(g,info);
            for (Cell c : listCell) {
                c.drawCell(g, info);
            }
        }
    }

    private void setupLayout() {
        init(40);
        this.addMouseListener(new BoardListener());
    }

    private void init(int mines) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                listCell.add(new Cell(j, i));
            }
        }
        loadImages();
        setMine(mines);
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

    private void drawBorder(Graphics g, DrawInfo info) {
        drawCorners(g,info);
        drawTopEdge(g,info);
        drawRightEdge(g,info);
        drawBottomEdge(g,info);
        drawLeftEdge(g,info);
    }

    private void drawCorners(Graphics g,DrawInfo info) {
       Cell.myDraw(cache.get(Constants.BORDER.TOP_LEFT), g, info, -1, -1); 
       Cell.myDraw(cache.get(Constants.BORDER.TOP_RIGHT), g, info, column, -1); 
       Cell.myDraw(cache.get(Constants.BORDER.BOTTOM_RIGHT), g, info, column, row); 
       Cell.myDraw(cache.get(Constants.BORDER.BOTTOM_LEFT), g, info, -1, row); 
    }

    private void drawTopEdge(Graphics g,DrawInfo info) {
        for (int i=0; i<column; i++) {
           Cell.myDraw(cache.get(Constants.BORDER.TOP_BOTTOM), g, info, i, -1); 
        }
    }

    private void drawRightEdge(Graphics g,DrawInfo info) {
        for (int i=0; i<row; i++) {
           Cell.myDraw(cache.get(Constants.BORDER.LEFT_R), g, info, column, i); 
        }

    }

    private void drawBottomEdge(Graphics g,DrawInfo info) {
        for (int i=0; i<column; i++) {
           Cell.myDraw(cache.get(Constants.BORDER.TOP_BOTTOM), g, info, i, row); 
        }

    }

    private void drawLeftEdge(Graphics g,DrawInfo info) {
        for (int i=0; i<row; i++) {
           Cell.myDraw(cache.get(Constants.BORDER.LEFT_R), g, info, -1, i); 
        }

    }

    public class BoardListener extends MouseAdapter {
        @Override
        //		public void mouseClicked(MouseEvent e){
        public void mouseReleased(MouseEvent e){
            if (isGameOver) {
                return;
            }
            int x = (e.getX()-info.getOffsetX())/column;
            int y = (e.getY()-info.getOffsetY())/row;
            Cell c = getCell(x,y);
            if (c == null) {
                return;
            }
            if (c.isCovered()) {
                if (SwingUtilities.isLeftMouseButton(e)){
                    if (c.isMined()) {
                        c.setValue(-1);
                        gameOver();
                    }
                    actionLeftClick(c);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    actionRightClick(c);
                }
            }else {
                if (SwingUtilities.isLeftMouseButton(e)){
                    actionBothClick(c);
                }
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
            if (tempCell != null && tempCell.isCovered()){
                if (tempCell.isMarked() && !tempCell.isMined()){
                    tempCell.setValue(-1);
                    gameOver();
                    return;
                }
                if (!tempCell.isMined()){
                    tempCell.setValue(getAdjMineCount(tempCell));
                    tempCell.setCovered(false);
                    if (tempCell.getValue()==0){
                        uncoverAdjCell(tempCell);
                    }
                }
            }
        }
    }

    private void actionLeftClick(Cell c) {
        c.setCovered(false);
        c.setValue(getAdjMineCount(c));
        if (c.getValue() == 0) {
            uncoverAdjCell(c); 
        }
    }
    
    private void actionRightClick(Cell c) {
        c.changeState();
    }

    private void actionBothClick(Cell c) {
        int value = c.getValue();
        for (int i=0; i<8; i++) {
            Cell tempCell = getCell(c.getX()+Constants.dx[i], c.getY() + Constants.dy[i]);
            if (tempCell != null && tempCell.isMarked()) {
                value--;
            }
        }
        System.out.println(Integer.toString(value));
        if (value == 0) {
            for (int i=0; i<8; i++) {
                Cell tempCell = getCell(c.getX()+Constants.dx[i], c.getY() + Constants.dy[i]);
                if (tempCell != null) {
                    if (tempCell.isCovered() && !tempCell.isMarked()) {
                        if (tempCell.isMined()) {
                            tempCell.setValue(-1);
                            gameOver();
                            return;
                        }
                        tempCell.setValue(getAdjMineCount(tempCell));
                        tempCell.setCovered(false);
                        uncoverAdjCell(tempCell);
                    }
                }
            }
        }
    }
    
    private void gameOver() {
        isGameOver = true;
        uncoverBoard();
    }

    private void uncoverBoard() {
        for (Cell c :listCell) {
            if (c.isMined()) {
                c.setCovered(false);
            }
        }
    }
    
    private void resetBoard() {
        listCell.clear();
        init(40);
        this.isGameOver = false;
        repaint();
    }
    
    private JMenuBar buildMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        menuBar.add(menuGame);
        
        JMenuItem menuItemNew = new JMenuItem("New");
        menuItemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
        });
        menuGame.add(menuItemNew);
        return menuBar;
    }
    
    public static void play() {
        JFrame frame = new JFrame("MineSweeper");
        Board board = new Board(100,100,20,20);
        frame.setContentPane(board);
        frame.setJMenuBar(board.buildMenu());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

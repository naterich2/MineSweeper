
package com.csun.game;

import com.csun.game.utils.DrawingUtil;

import java.awt.Dimension;
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

    private static final int CELL_LENGTH = 20;

    private static final int BOMB = -1;

    private boolean isGameOver;
    private static DrawInfo info;
    private int row;
    private int column;
    private int numMines;
    private int cellCount;
    private int mineCount;
    private java.util.ArrayList<Cell> listCell;

    public Board() {
        super(true);
        this.addMouseListener(new BoardListener());
        listCell = new java.util.ArrayList<Cell>();
        cache = new HashMap<String, BufferedImage>();
        loadImages();
    }

    public void createBoard(int offsetX, int offsetY, int row, int column, double d) {
        info = new DrawInfo(offsetX, offsetY, Board.CELL_LENGTH);
        this.row = row;
        this.column = column;
        listCell.clear();
        this.numMines = (int) (row*column*d);
        this.isGameOver = false;
        setupLayout();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBorder(g, info);
        for (Cell c : listCell) {
            c.drawCell(g, info);
        }
    }

    private void setupLayout() {
        init();
        this.setPreferredSize(new Dimension(info.getOffsetX()*3 + column*Board.CELL_LENGTH,info.getOffsetY()*3 + row*Board.CELL_LENGTH));
    }

    private void init() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                listCell.add(new Cell(j, i));
            }
        }
        setMine();
        this.cellCount = row*column-this.numMines;
        mineCount = this.numMines;
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

    private void setMine() {
        for (int i = 0; i < numMines; i++) {
            Cell c;
            c = listCell.get(new Random().nextInt(listCell.size()));
            while (c.isMined()) {
                c = listCell.get(new Random().nextInt(listCell.size()));
            }
            c.setMined(true);
        }
    }

    private void drawBorder(Graphics g, DrawInfo info) {
        drawCorners(g, info);
        drawTopEdge(g, info);
        drawRightEdge(g, info);
        drawBottomEdge(g, info);
        drawLeftEdge(g, info);
    }

    private void drawCorners(Graphics g, DrawInfo info) {
        DrawingUtil.drawImage(cache.get(Constants.BORDER.TOP_LEFT), g, info, -1, -1);
        DrawingUtil.drawImage(cache.get(Constants.BORDER.TOP_RIGHT), g, info, column, -1);
        DrawingUtil.drawImage(cache.get(Constants.BORDER.BOTTOM_RIGHT), g, info, column, row);
        DrawingUtil.drawImage(cache.get(Constants.BORDER.BOTTOM_LEFT), g, info, -1, row);
    }

    private void drawTopEdge(Graphics g, DrawInfo info) {
        for (int i = 0; i < column; i++) {
            DrawingUtil.drawImage(cache.get(Constants.BORDER.TOP_BOTTOM), g, info, i, -1);
        }
    }

    private void drawRightEdge(Graphics g, DrawInfo info) {
        for (int i = 0; i < row; i++) {
            DrawingUtil.drawImage(cache.get(Constants.BORDER.LEFT_R), g, info, column, i);
        }
    }

    private void drawBottomEdge(Graphics g, DrawInfo info) {
        for (int i = 0; i < column; i++) {
            DrawingUtil.drawImage(cache.get(Constants.BORDER.TOP_BOTTOM), g, info, i, row);
        }
    }

    private void drawLeftEdge(Graphics g, DrawInfo info) {
        for (int i = 0; i < row; i++) {
            DrawingUtil.drawImage(cache.get(Constants.BORDER.LEFT_R), g, info, -1, i);
        }
    }

    public class BoardListener extends MouseAdapter {
        @Override
        // public void mouseClicked(MouseEvent e){
        public void mouseReleased(MouseEvent e) {
            if (isGameOver) {
                return;
            }
            int x = (e.getX() - info.getOffsetX()) / CELL_LENGTH;
            int y = (e.getY() - info.getOffsetY()) / CELL_LENGTH;
            Cell c = getCell(x, y);
            if (c == null) {
                return;
            }
            int btn = e.getButton();
            if (c.isCovered()) {
                if (btn == MouseEvent.BUTTON1) {
                    if (c.isMined()) {
                        c.setValue(-1);
                        gameOver();
                        JOptionPane.showMessageDialog(Board.this, "Game over. You lose!!");
                    }
                    onLeftClick(c);
                }
                if (btn == MouseEvent.BUTTON3) {
                    onRightClick(c);
                }
            } else {
                if (btn == MouseEvent.BUTTON1 || btn == MouseEvent.BUTTON3) {
                    onBothClick(c);
                }
            }
            repaint();
            System.out.println(Integer.toString(mineCount) + " " + Integer.toString(cellCount));
            if (cellCount==0 && mineCount==0) {
                JOptionPane.showMessageDialog(Board.this, "You won. Good job!!");
            }
        }
    }

    private Cell getCell(int x, int y) {
        if (x < 0 || y < 0 || x >= column || y >= row) {
            return null;
        } else {
            return listCell.get(y * column + x);
        }
    }

    private int getAdjMineCount(Cell c) {
        int count = 0;
        Cell tempCell;
        if (c.isMined()) {
            return BOMB; 
        } else {
            for (int i = 0; i < 8; i++) {
                tempCell = getCell(c.getX() + Constants.dx[i], c.getY() + Constants.dy[i]);
                if (tempCell != null) {
                    if (tempCell.isMined()) {
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
        for (int i = 0; i < 8; i++) {
            Cell tempCell = getCell(c.getX() + Constants.dx[i], c.getY() + Constants.dy[i]);
            if (tempCell != null && tempCell.isCovered()) {
                if (tempCell.isMarked() && !tempCell.isMined()) {
                    tempCell.setValue(-1);
                    gameOver();
                    JOptionPane.showMessageDialog(Board.this,"Game over, you lose!");
                    return;
                }
                if (!tempCell.isMined()) {
                    tempCell.setValue(getAdjMineCount(tempCell));
                    tempCell.setCovered(false);
                    cellCount--;
                    if (tempCell.getValue() == 0) {
                        uncoverAdjCell(tempCell);
                    }
                }
            }
        }
    }

    private void onLeftClick(Cell c) {
        c.setCovered(false);
        cellCount--;
        c.setValue(getAdjMineCount(c));
        if (c.getValue() == 0) {
            uncoverAdjCell(c);
        }
    }

    private void onRightClick(Cell c) {
        if (c.isMarked()){
            c.setMarked(false);
            if (c.isMined()) {
                mineCount++;
            }
        }else {
            c.setMarked(true);
            if (c.isMined()) {
                mineCount--;
            }
        }
    }

    private void onBothClick(Cell c) {
        int value = c.getValue();
        for (int i = 0; i < 8; i++) {
            Cell tempCell = getCell(c.getX() + Constants.dx[i], c.getY() + Constants.dy[i]);
            if (tempCell != null && tempCell.isMarked()) {
                value--;
            }
        }
        if (value == 0) {
            for (int i = 0; i < 8; i++) {
                Cell tempCell = getCell(c.getX() + Constants.dx[i], c.getY() + Constants.dy[i]);
                if (tempCell != null) {
                    if (tempCell.isCovered() && !tempCell.isMarked()) {
                        if (tempCell.isMined()) {
                            tempCell.setValue(-1);
                            gameOver();
                            return;
                        }
                        tempCell.setValue(getAdjMineCount(tempCell));
                        tempCell.setCovered(false);
                        cellCount--;
                        uncoverAdjCell(tempCell);
                    }
                }
            }
        }
    }

    private void gameOver() {
        isGameOver = true;
        uncoverBoard();
        repaint();
    }

    private void uncoverBoard() {
        for (Cell c : listCell) {
            if (c.isMined()) {
                c.setCovered(false);
            }
        }
    }

    private void resetBoard() {
        listCell.clear();
        init();
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

        JMenuItem menuItemEasy = new JMenuItem("Beginner");
        menuItemEasy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBoard(Board.info.getOffsetX(),Board.info.getOffsetY(),10,10,.1);
                repaint();
            }
        });
        menuGame.add(menuItemEasy);

        JMenuItem menuItemIntermediate = new JMenuItem("Intermediate");
        menuItemIntermediate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBoard(Board.info.getOffsetX(),Board.info.getOffsetY(),15,15,.15);
                repaint();
            }
        });
        menuGame.add(menuItemIntermediate);

        JMenuItem menuItemExpert = new JMenuItem("Expert");
        menuItemExpert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBoard(Board.info.getOffsetX(),Board.info.getOffsetY(),20,20,.2);
                repaint();
            }
        });
        menuGame.add(menuItemExpert);
        return menuBar;
    }

    public static void play() {
        JFrame frame = new JFrame("MineSweeper");
        int offsetX = 60;
        int offsetY = 60;
        int row = 20;
        int column = 20;
        double ratio = .1;
        Board board = new Board();
        board.createBoard(offsetX, offsetY, row, column, ratio);
        frame.setContentPane(board);
        frame.setJMenuBar(board.buildMenu());
        frame.setSize(board.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

package com.csun.game;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

enum Direction {
	N(0,-1),
	NE(1,-1),
	E(1,0),
	SE(1,1),
	S(0,1),
	SW(-1,1),
	W(-1,0),
	NW(-1,-1);

	private int dx;
	private int dy;
	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public int dx() {
		return dx;
	}

	public int dy() {
		return dy;
	}
}

enum GameState{
	LOSE(0),
	WIN(1);
	private int state;
	GameState(int state) {
		this.state = state;
	}
	public int state() {
		return state;
	}
}
public class Board extends JPanel {
	static final String IMAGE_DIRNAME = "MineImages";
	static final int BOMB_EXPLODED = -1;

	static Map<String, BufferedImage> cache;

	private boolean isGameOver;
	private int row;
	private int column;
	private int numMines;
	private int cellCount;
	private int mineCount;
	private java.util.ArrayList<Cell> listCell;
	private Border boardBorder;
	private JDialog dialog;

	public Board() {
		super(true);
		listCell = new java.util.ArrayList<Cell>();
		cache = new HashMap<String, BufferedImage>();
		boardBorder = new Border();
		loadImages();
		dialog = new JDialog();
		dialog.setTitle("Custom");
		dialog = buildDialog();
		this.addMouseListener(new BoardListener());
	}

	public void create(int offsetX, int offsetY, int row, int column, double d) {
		this.row = row;
		this.column = column;
		this.numMines = (int) (row*column*d);
		boardBorder.setColumn(column);
		boardBorder.setRow(row);
		init();
	}

	public void init() {
		this.isGameOver = false;
		listCell.clear();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				listCell.add(new Cell(j, i));
			}
		}
		setMine();
		this.cellCount = row*column-this.numMines;
		mineCount = this.numMines;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		boardBorder.draw(g);
		for (Cell c : listCell) {
			c.draw(g);
		}
	}

	private void loadImages() {
		BufferedImage images;
		File imageDir;
		File[] imageFiles;
		imageDir = new File(IMAGE_DIRNAME);
		imageFiles = imageDir.listFiles();

		for (int i = 0; i < imageFiles.length; i++) {
			try {
				images = ImageIO.read(new FileInputStream(imageFiles[i]));
				cache.put(imageFiles[i].getName(), images);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(Board.this, "File not found!!!");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(Board.this, "File not found!!!");
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

	public class BoardListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			if (isGameOver) {
				return;
			}
			int x = (e.getX() - DrawInfo.OFFSET_X) / DrawInfo.CELL_LENGTH;
			int y = (e.getY() - DrawInfo.OFFSET_Y) / DrawInfo.CELL_LENGTH;
			Cell c = getCell(x, y);
			if (c == null) {
				return;
			}
			int btn = e.getButton();
			if (c.isCovered()) {
				if (btn == MouseEvent.BUTTON1) {
					if (c.isMined()) {
						c.setValue(-1);
						gameOver(GameState.LOSE);
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
			if (cellCount==0 && mineCount==0) {
				gameOver(GameState.WIN);
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

	private int evalCell(Cell c) {
		int count = 0;
		Cell tempCell;
		if (c.isMined()) {
			return BOMB_EXPLODED; 
		} else {
			for (Direction d : Direction.values()) {
				tempCell = getCell(c.getX() + d.dx(), c.getY() + d.dy());
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
		for (Direction d : Direction.values()) {
			Cell tempCell = getCell(c.getX() + d.dx(), c.getY() + d.dy());
			if (tempCell != null && tempCell.isCovered()) {
				if (!tempCell.isMined() && !tempCell.isMarked()) {
					tempCell.setValue(evalCell(tempCell));
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
		c.setValue(evalCell(c));
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
		for (Direction d : Direction.values()) {
			Cell tempCell = getCell(c.getX() + d.dx(), c.getY() + d.dy());
			if (tempCell != null && tempCell.isMarked()) {
				value--;
			}
		}
		if (value == 0) {
			for (Direction d : Direction.values()) {
				Cell tempCell = getCell(c.getX() + d.dx(), c.getY() + d.dy());
				if (tempCell != null) {
					if (tempCell.isCovered() && !tempCell.isMarked()) {
						if (tempCell.isMined()) {
							tempCell.setValue(-1);
							gameOver(GameState.LOSE);
							return;
						}
						tempCell.setValue(evalCell(tempCell));
						tempCell.setCovered(false);
						cellCount--;
						uncoverAdjCell(tempCell);
					}
				}
			}
		}
	}

	private void gameOver(GameState s) {
		isGameOver = true;
		if (s.equals(GameState.LOSE)) {
			uncover();
			repaint();
			JOptionPane.showMessageDialog(Board.this,"Game over, you lose!");
		}else {
			JOptionPane.showMessageDialog(Board.this, "You won. Good job!!");
		}
	}

	public void uncover() {
		for (Cell c : listCell) {
			if (c.isMined()) {
				c.setCovered(false);
			}
		}
	}

	private JMenuBar buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuGame = new JMenu("Game");
		menuBar.add(menuGame);

		JMenuItem menuItemNew = new JMenuItem("New");
		menuItemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}
		});
		menuGame.add(menuItemNew);

		JMenuItem menuItemEasy = new JMenuItem("Beginner");
		menuItemEasy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				create(DrawInfo.OFFSET_X,DrawInfo.OFFSET_Y,10,10,.1);
				repaint();
			}
		});
		menuGame.add(menuItemEasy);

		JMenuItem menuItemIntermediate = new JMenuItem("Intermediate");
		menuItemIntermediate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				create(DrawInfo.OFFSET_X,DrawInfo.OFFSET_Y,15,15,.15);
				repaint();
			}
		});
		menuGame.add(menuItemIntermediate);

		JMenuItem menuItemExpert = new JMenuItem("Expert");
		menuItemExpert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				create(DrawInfo.OFFSET_X,DrawInfo.OFFSET_Y,20,20,.2);
				repaint();
			}
		});
		menuGame.add(menuItemExpert);
		
		JMenuItem menuItemCustom = new JMenuItem("Custom");
		menuItemCustom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(true);
			}
		});
		menuGame.add(menuItemCustom);
		return menuBar;
	}
	
	private JDialog buildDialog() {
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.ipadx = 20;
		c.gridwidth = 1;
		c.insets = new Insets(0,10,0,10);
		dialog.add(new JLabel("Number of row"),c);
		
		final JTextField rowField = new JTextField();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 2;
		c.weighty = 1;
		dialog.add(rowField,c);
		
		c.gridwidth = 1;
		dialog.add(new JLabel("Number of Column"),c);
		
		final JTextField colField = new JTextField();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 2;
		dialog.add(colField,c);
		
		c.gridwidth = 1;
		dialog.add(new JLabel("Mine percentage"),c);
		
		final JTextField mineField = new JTextField("10");
		c.gridwidth = 1;
		c.weightx = 1;
		dialog.add(mineField,c);
		JSlider slider = new JSlider(JSlider.HORIZONTAL,0,100,10);
		slider.addChangeListener(new ChangeListener(){
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				mineField.setText(Integer.toString(s.getValue()));
			}
			
		});
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0,0,0,10);
		dialog.add(slider,c);
		
		c.gridwidth = 1;
		JButton okButton = new JButton("OK");
		c.insets = new Insets(0,40,0,10);
		dialog.add(okButton,c);
		
		JButton cancelButton = new JButton("Cancel");
		c.insets = new Insets(0,10,0,10);
		c.gridwidth = GridBagConstraints.REMAINDER;
		dialog.add(cancelButton,c);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				try {
				create(DrawInfo.OFFSET_X,DrawInfo.OFFSET_Y,
						Integer.parseInt(colField.getText()),
						Integer.parseInt(rowField.getText()),
						Integer.parseInt(mineField.getText())/100.0);
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(Board.this, "Invalid input!!");
				}
				repaint();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.setSize(300,200);
		return dialog;
	}
	
	public static void play() {
		JFrame frame = new JFrame("MineSweeper");
		int offsetX = 60;
		int offsetY = 60;
		int row = 10;
		int column = 20;
		double ratio = .1;
		Board board = new Board();
		board.create(offsetX, offsetY, row, column, ratio);
		frame.setContentPane(board);
		frame.setJMenuBar(board.buildMenu());
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
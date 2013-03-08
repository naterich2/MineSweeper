package com.csun.game;

import java.awt.Graphics;

import com.csun.game.utils.DrawingUtil;

public class Border {
	private int column;
	private int row;
	
	public Border() {
	}
	
	public Border (int x, int y) {
		this.column = x;
		this.row = y;
	}
	
	public void draw(Graphics g) {
		drawCorners(g);
		drawTopEdge(g);
		drawRightEdge(g);
		drawBottomEdge(g);
		drawLeftEdge(g);
	}
	
	private void drawCorners(Graphics g) {
		DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.TOP_LEFT), g, -1, -1);
		DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.TOP_RIGHT), g, column, -1);
		DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.BOTTOM_RIGHT), g, column, row);
		DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.BOTTOM_LEFT), g, -1, row);
	}

	private void drawTopEdge(Graphics g) {
		for (int i = 0; i < column; i++) {
			DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.TOP_BOTTOM), g, i, -1);
		}
	}

	private void drawRightEdge(Graphics g) {
		for (int i = 0; i < row; i++) {
			DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.LEFT_R), g, column, i);
		}
	}

	private void drawBottomEdge(Graphics g) {
		for (int i = 0; i < column; i++) {
			DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.TOP_BOTTOM), g, i, row);
		}
	}

	private void drawLeftEdge(Graphics g) {
		for (int i = 0; i < row; i++) {
			DrawingUtil.drawImage(Board.cache.get(Constants.BORDER.LEFT_R), g, -1, i);
		}
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
}

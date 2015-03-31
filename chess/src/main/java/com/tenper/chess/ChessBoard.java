package com.tenper.chess;

import java.util.ArrayList;
import java.util.List;

import utils.Tuple;

public class ChessBoard {
	private int rowCount;
	private int columnCount;
	private List<Integer> board;

	public ChessBoard(int rowCount, int columnCount) {
		super();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.board = initChessBoard();
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public List<Integer> getBoard() {
		return board;
	}

	public List<Integer> initChessBoard() {
		List<Integer> board = new ArrayList<Integer>();
		for (int i = 0; i < rowCount * columnCount; i++) {
			board.add(i);
		}
		return board;
	}

	public Tuple<Integer, Integer> getCoordinates(int figurePosition) {
		if (columnCount <= 0) {
			throw new IllegalArgumentException(
					"Column count should be greater than 0");
		}
		return new Tuple<Integer, Integer>(figurePosition / columnCount,
				figurePosition % columnCount);
	}
}

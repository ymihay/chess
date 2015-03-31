package com.tenper.chess;

public enum ChessFigure {

	QUEEN("Q"), ROOK("R"), BISHOP("B"), KING("K"), KNIGHT("N");

	private final String name;

	ChessFigure(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

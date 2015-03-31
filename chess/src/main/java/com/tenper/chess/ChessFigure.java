package com.tenper.chess;

public enum ChessFigure {

	QUEEN("Q"), KING("K"), ROOK("R"), BISHOP("B"), KNIGHT("N");

	private final String name;

	ChessFigure(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

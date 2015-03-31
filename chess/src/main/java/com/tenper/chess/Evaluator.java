package com.tenper.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.Utils;

public class Evaluator {
	private ChessBoard chessBoard;
	private PositionPredicate positionPredicate;
	private Set<Map<Integer, ChessFigure>> solutions;
	private static List<ChessFigure> figuresToPut = new ArrayList<ChessFigure>();

	static {
		figuresToPut.add(ChessFigure.KING);
		figuresToPut.add(ChessFigure.KING);
		figuresToPut.add(ChessFigure.ROOK);
	}

	public Evaluator(int columnCount, int rowCount) {
		this.chessBoard = new ChessBoard(columnCount, rowCount);
		this.positionPredicate = new PositionPredicate(chessBoard);
		this.solutions = new HashSet<Map<Integer, ChessFigure>>();
		/**
		 * Need to perform sort of input chess figures for improving
		 * performance. Amount of possible figure positions free from attacks is
		 * decreasing after placing all Queens, then Rooks (or Bishops), then
		 * Kings(or Knights)
		 */
		Collections.sort(figuresToPut);
	}

	public Set<Map<Integer, ChessFigure>> getSolution() {
		return this.solutions;
	}

	public ChessBoard getBoard() {
		return this.chessBoard;
	}

	public PositionPredicate getPositionPredicate() {
		return this.positionPredicate;
	}

	public List<ChessFigure> getFiguresToPut() {
		return this.figuresToPut;
	}

	public void positionTry(List<Integer> board,
			Map<Integer, ChessFigure> chessPositions, int numOfFigureToPut,
			int prevPosition, ChessFigure prevFigure) {

		if (prevFigure != null) {
			chessPositions.put(prevPosition, prevFigure);
		}

		numOfFigureToPut++;

		if (numOfFigureToPut < figuresToPut.size()) {
			ChessFigure figureToPut = figuresToPut.get(numOfFigureToPut);

			for (Integer position : board) {
				if (!positionPredicate.isAnyAttackedByFigure(figureToPut,
						position, chessPositions.keySet())) {

					Stream<Integer> stream = board.stream().filter(
							positionPredicate.positionsFreeFromAttack(
									figureToPut, position));

					List<Integer> list = stream.collect(Collectors.toList());

					if (numOfFigureToPut == figuresToPut.size() - 1) {
						chessPositions.put(position, figureToPut);
						solutions.add(chessPositions);
					}
					positionTry(list, new HashMap<Integer, ChessFigure>(
							chessPositions), numOfFigureToPut, position,
							figureToPut);
				}
			}
		}
	}

	public static void main(String... args) {
		long prevTime = System.currentTimeMillis();

		Evaluator evaluator = new Evaluator(3, 3);

		evaluator.positionTry(evaluator.getBoard().getBoard(),
				new HashMap<Integer, ChessFigure>(), -1, 0, null);

		Utils.printResult(evaluator.solutions, evaluator.getBoard());

		long nextTime = System.currentTimeMillis();
		System.out.println("Time: " + (nextTime - prevTime));
	}
}

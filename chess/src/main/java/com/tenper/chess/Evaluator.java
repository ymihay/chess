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
	
	// Predicate that determines positions that are not under attack for chess
	// board
	private PositionPredicate positionPredicate;

	// Set of unique configurations of the board for which all of the pieces
	// can be placed on the board without threatening each other.
	private Set<Map<Integer, ChessFigure>> solutions;

	private static List<ChessFigure> figuresToPut = new ArrayList<ChessFigure>();

	// TOOD: fix it with getting input param from properties
	static {
		figuresToPut.add(ChessFigure.KING);
		figuresToPut.add(ChessFigure.KING);

		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);

		figuresToPut.add(ChessFigure.BISHOP);
		figuresToPut.add(ChessFigure.BISHOP);

		figuresToPut.add(ChessFigure.KNIGHT);
	}

	public Evaluator(int columnCount, int rowCount) {
		if (columnCount <= 0 || rowCount <= 0) {
			throw new IllegalArgumentException(
					"Dimensions of board should be greater then 0");
		}
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

	/**
	 * Recursive task for evaluating possible configurations of chess board for
	 * current @param chessPositions and with list of available chess positions @param
	 * board
	 * 
	 * @param board
	 *            is available (not under attack) chess positions on chess board
	 *            on current iteration
	 * @param chessPositions
	 *            is map of put chess figures that associated with occupied
	 *            position
	 * @param numOfFigureToPut
	 *            is the number of the chess figure to put on this iteration
	 * @param prevPosition
	 *            is the position that were occupied on chess board on the
	 *            previous step
	 * @param prevFigure
	 *            is the chess figure that were put on chess board on the first
	 *            step. This value equals null when any figure was put on the
	 *            chess board
	 */
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
				Map<Integer, ChessFigure> currentChessPositions = new HashMap<Integer, ChessFigure>(
						chessPositions);
				if (!positionPredicate.isAnyAttackedByFigure(figureToPut,
						position, chessPositions.keySet())) {

					Stream<Integer> stream = board.stream().filter(
							positionPredicate.positionsFreeFromAttack(
									figureToPut, position));

					List<Integer> list = stream.collect(Collectors.toList());

					/**
					 * If there is no available positions on the board to put
					 * and there are some input figures are left, then it's not
					 * a solution. Go to next iteration.
					 */
					if (numOfFigureToPut < figuresToPut.size() - 1
							&& board.isEmpty()) {
						continue;
					}

					/**
					 * If we put last input figure on the board, it's a
					 * solution.
					 */
					if (numOfFigureToPut == figuresToPut.size() - 1) {
						currentChessPositions.put(position, figureToPut);
						solutions.add(currentChessPositions);
						continue;
					}

					positionTry(list, currentChessPositions, numOfFigureToPut,
							position, figureToPut);
				}
			}
		}
	}

	
	public static void main(String... args) {
		long prevTime = System.currentTimeMillis();

		Evaluator evaluator = new Evaluator(7, 7);

		evaluator.positionTry(evaluator.getBoard().getBoard(),
				new HashMap<Integer, ChessFigure>(), -1, 0, null);

		Utils.printResult(evaluator.solutions, evaluator.getBoard());

		long nextTime = System.currentTimeMillis();
		System.out.println("Time: " + (nextTime - prevTime));
	}
}

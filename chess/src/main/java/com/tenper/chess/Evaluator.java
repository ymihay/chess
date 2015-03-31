package com.tenper.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Evaluator extends RecursiveTask<Void> {
	private static ChessBoard board = new ChessBoard(8, 8);
	private static PositionPredicate positionPredicate = new PositionPredicate(
			board);
	private static Set<Map<Integer, ChessFigure>> solutions = new HashSet<Map<Integer, ChessFigure>>();
	private static List<ChessFigure> figuresToPut = new ArrayList<ChessFigure>();
	static {
		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);
		figuresToPut.add(ChessFigure.QUEEN);
	}

	public static void positionTry(List<Integer> board,
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
		Map<Integer, ChessFigure> chessPositions = new HashMap<Integer, ChessFigure>();
		positionTry(board.getBoard(), chessPositions, 0, -1, null);
		// /new ForkJoinPool().invoke(new Evaluator());
		long nextTime = System.currentTimeMillis();
		System.out.println(nextTime - prevTime);
	}

	public static void printResult() {
		for (Map<Integer, ChessFigure> map : solutions) {
			System.out.println(" ");
			for (Map.Entry<Integer, ChessFigure> entry : map.entrySet()) {
				System.out.println(board.getCoordinates(entry.getKey()) + "/"
						+ entry.getValue());
			}
			System.out.println(" ");
		}

		System.out.println("Finish!" + solutions.size());
	}

	@Override
	protected Void compute() {

		Map<Integer, ChessFigure> chessPositions = new HashMap<Integer, ChessFigure>();
		positionTry(board.getBoard(), chessPositions, 0, -1, null);

		/*
		 * for (Integer position : board.getBoard()) { positionTry(
		 * board.getBoard() .stream()
		 * .filter(positionPredicate.positionsFreeFromAttack(
		 * figuresToPut.get(0), position)) .collect(Collectors.toList()), new
		 * HashMap<Integer, ChessFigure>(chessPositions), 0, position,
		 * figuresToPut.get(0)); }
		 */
		printResult();
		return null;
	}
}

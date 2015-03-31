package utils;

import java.util.Map;
import java.util.Set;

import com.tenper.chess.ChessBoard;
import com.tenper.chess.ChessFigure;

public class Utils {

	public static void printResult(Set<Map<Integer, ChessFigure>> solutions,
			ChessBoard board) {
		System.out.println("Results:");
		for (Map<Integer, ChessFigure> map : solutions) {
			System.out.println(" ");
			for (Map.Entry<Integer, ChessFigure> entry : map.entrySet()) {
				Tuple<Integer, Integer> pair = board.getCoordinates(entry
						.getKey());
				System.out.println(pair.getX() + ":" + pair.getY() + "/"
						+ entry.getValue());
			}
		}
		System.out.println("\nFinish! Amount of unique results is "
				+ solutions.size());
	}

}

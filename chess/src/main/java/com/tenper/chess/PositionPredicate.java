package com.tenper.chess;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PositionPredicate {

	private ChessBoard board;

	public PositionPredicate(ChessBoard board) {
		this.board = board;
	}

	public Predicate<Integer> positionsFreeFromRookAttack(Integer rookPosition) {
		int columnsCount = board.getColumnCount();
		// Under vertical/horizontal attack
		return p -> !(((1.0 * (p - rookPosition) % columnsCount == 0) || (rookPosition
				/ columnsCount == p / columnsCount)));
	}

	public Predicate<Integer> positionsFreeFromBishopAttack(
			Integer bishopPosition) {
		int columnsCount = board.getColumnCount();
		// Under diagonal attack
		return p -> !((Math.abs(p % columnsCount - bishopPosition
				% columnsCount)) == Math.abs((p / columnsCount - bishopPosition
				/ columnsCount)));
	}

	public Predicate<Integer> positionsFreeFromQueenAttack(Integer queenPosition) {
		int columnsCount = board.getColumnCount();
		return p -> !(((1.0 * (p - queenPosition) % columnsCount == 0)
				|| (queenPosition / columnsCount == p / columnsCount) || (Math
					.abs(p % columnsCount - queenPosition % columnsCount)) == Math
				.abs((p / columnsCount - queenPosition / columnsCount))));
	}

	public Predicate<Integer> positionsFreeFromKingAttack(Integer kingPosition) {
		int columnsCount = board.getColumnCount();
		return p -> !(((p / columnsCount == kingPosition / columnsCount) && (p == kingPosition - 1 || p == kingPosition + 1))
				|| (p == kingPosition + columnsCount)
				|| (p == kingPosition)
				|| (p == kingPosition - columnsCount)
				|| (p == kingPosition + (columnsCount + 1) && 1 + kingPosition
						/ columnsCount == p / columnsCount)
				|| (p == kingPosition - (columnsCount + 1) && kingPosition
						/ columnsCount == 1 + p / columnsCount)
				|| (p == kingPosition + (columnsCount - 1) && kingPosition
						/ columnsCount != p / columnsCount) || (p == kingPosition
				- (columnsCount - 1) && kingPosition / columnsCount != p
				/ columnsCount));
	}

	// FIXME
	public Predicate<Integer> positionsFreeFromKnightAttack(
			Integer knightPosition) {
		int columnsCount = board.getColumnCount();
		return p -> !((p == knightPosition)
				|| (p == knightPosition + (2 * columnsCount + 1) && 2
						+ knightPosition / columnsCount == p / columnsCount)
				|| (p == knightPosition - (2 * columnsCount + 1) && knightPosition
						/ columnsCount == 2 + p / columnsCount)
				|| (p == knightPosition + (2 * columnsCount - 1) && 2
						+ knightPosition / columnsCount == p / columnsCount)
				|| (p == knightPosition - (2 * columnsCount - 1) && knightPosition
						/ columnsCount == 2 + p / columnsCount)
				|| (p == knightPosition - (columnsCount - 2) && knightPosition
						/ columnsCount == 1 + p / columnsCount)
				|| (p == knightPosition - (columnsCount + 2)//
						&& knightPosition
						/ columnsCount == 1 + p / columnsCount)
				
				|| (p == knightPosition + (columnsCount - 2) && 1
						+ knightPosition / columnsCount == p / columnsCount) || (p == knightPosition
				+ (columnsCount + 2) && 1 + knightPosition / columnsCount == p
				/ columnsCount));
	}

	public Predicate<Integer> positionsFreeFromAttack(ChessFigure figure,
			Integer position) {

		switch (figure) {
		case QUEEN:
			return positionsFreeFromQueenAttack(position);

		case KING:
			return positionsFreeFromKingAttack(position);

		case BISHOP:
			return positionsFreeFromBishopAttack(position);

		case ROOK:
			return positionsFreeFromRookAttack(position);

		case KNIGHT:
			return positionsFreeFromKnightAttack(position);
		}

		return null;
	}

	public boolean isAnyAttackedByFigure(ChessFigure figure,
			int figurePosition, Set<Integer> positions) {

		return !(board.getBoard().stream()
				.filter(positionsFreeFromAttack(figure, figurePosition))
				.collect(Collectors.toSet()).containsAll(positions));

	}

}

package chess.domain.piece.bishop;

import java.util.Map;

import chess.domain.Team;
import chess.domain.piece.MovingStrategy;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

public class Bishop extends Piece {
	private static final MovingStrategy STRATEGY = new BishopStrategy();
	public static final String WHITE_BISHOP = "\u2657";
	public static final String BLACK_BISHOP = "\u265d";

	public Bishop(Team team, Position position) {
		super(team, position);
	}

	public static Piece of(Team team, Position position) {
		return new Bishop(team, position);
	}

	@Override
	public String toString() {
		if (team.equals(Team.WHITE)) {
			return WHITE_BISHOP;
		}
		return BLACK_BISHOP;
	}

	@Override
	public Piece move(Position from, Position to, Map<Position, Team> dto) {
		STRATEGY.validateMove(from, to, dto);
		this.position = to;
		return this;
	}
}

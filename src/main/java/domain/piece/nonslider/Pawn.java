package domain.piece.nonslider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import domain.board.Square;
import domain.piece.Camp;
import domain.piece.Direction;
import domain.piece.Piece;

public class Pawn extends Piece {
    private static final List<Direction> MOVABLE_DIRECTION_FORWARD = List.of(
        Direction.NORTH
    );
    private static final List<Direction> MOVABLE_DIRECTION_DIAGONAL = List.of(
        Direction.NORTH_WEST,
        Direction.NORTH_EAST
    );

    private static final Integer MIN_FILE_INDEX = 0;
    private static final Integer MAX_FILE_INDEX = 7;
    private static final Integer MIN_RANK_INDEX = 0;
    private static final Integer MAX_RANK_INDEX = 7;

    private boolean isGoingForward;
    private boolean isFirstMove = true;

    public Pawn(Camp camp) {
        super(camp);
    }

    @Override
    public List<Square> fetchMovableSquares(Square currentSquare, Square targetSquare) {
        isGoingForward = checkMoveDirection(currentSquare, targetSquare);

        List<Square> movableSquares = fetchSquares(currentSquare);
        addSquareIfMoveTwoSquareForward(currentSquare, movableSquares);
        if (movableSquares.contains(targetSquare)) {
            isFirstMove = false;
            return movableSquares;
        }
        throw new IllegalArgumentException("Pawn이 움직일 수 없는 경로입니다.");
    }

    private boolean checkMoveDirection(Square currentSquare, Square targetSquare) {
        int currentFileCoordinate = currentSquare.toCoordinate().get(FILE_INDEX);
        return currentFileCoordinate == targetSquare.toCoordinate().get(FILE_INDEX);
    }

    private List<Square> fetchSquares(Square currentSquare) {
        int directionUnit = fetchDirectionUnit();

        List<Direction> targetDirection = fetchMovableDirections();

        return findMovableSquares(directionUnit, currentSquare, targetDirection);
    }

    private int fetchDirectionUnit() {
        if (isWhite()) {
            return 1;
        }
        if (isBlack()) {
            return -1;
        }
        throw new IllegalStateException("폰의 진영이 없습니다.");
    }

    private List<Direction> fetchMovableDirections() {
        if (isGoingForward) {
            return MOVABLE_DIRECTION_FORWARD;
        }
        return MOVABLE_DIRECTION_DIAGONAL;
    }

    private List<Square> findMovableSquares(int directionUnit, Square currentSquare, List<Direction> targetDirection) {
        int currentFileCoordinate = currentSquare.toCoordinate().get(FILE_INDEX);
        int currentRankCoordinate = currentSquare.toCoordinate().get(RANK_INDEX);

        return targetDirection.stream()
            .filter(direction ->
                currentFileCoordinate + directionUnit * direction.getFile() >= MIN_FILE_INDEX
                    && currentFileCoordinate + directionUnit * direction.getFile() <= MAX_FILE_INDEX
                    && currentRankCoordinate + directionUnit * direction.getRank() >= MIN_RANK_INDEX
                    && currentRankCoordinate + directionUnit * direction.getRank() <= MAX_RANK_INDEX)
            .map(direction -> new Square(currentFileCoordinate + directionUnit * direction.getFile(),
                currentRankCoordinate + directionUnit * direction.getRank())).collect(Collectors.toList());
    }

    private void addSquareIfMoveTwoSquareForward(Square currentSquare, List<Square> movableCoordinate) {
        if (isGoingForward && isFirstMove) {
            movableCoordinate.add(fetchTwoStepForwardSquare(currentSquare));
        }
    }

    private Square fetchTwoStepForwardSquare(Square currentSquare) {
        int directionUnit = fetchDirectionUnit();
        int currentFileCoordinate = currentSquare.toCoordinate().get(FILE_INDEX);
        int currentRankCoordinate = currentSquare.toCoordinate().get(RANK_INDEX);
        return new Square(currentFileCoordinate, currentRankCoordinate + (directionUnit * 2));
    }

    @Override
    public boolean canMove(Map<Square, Piece> pathInfo, Square targetSquare) {
        Piece targetPiece = pathInfo.get(targetSquare);
        pathInfo.remove(targetSquare);
        if (isGoingForward) {
            return targetPiece.isEmpty() && existNoPieceOnPath(pathInfo);
        }
        return isOppositeCamp(targetPiece);
    }

    private boolean existNoPieceOnPath(Map<Square, Piece> pathInfo) {
        return pathInfo.values().stream()
            .allMatch(Piece::isEmpty);
    }
}

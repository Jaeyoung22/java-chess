package chess.domain.piece;

import chess.domain.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookTest {

    private Rook rook;

    private static Stream<Arguments> destinations() {
        return Stream.of(
                Arguments.of(Position.from("a5")),
                Arguments.of(Position.from("h5")),
                Arguments.of(Position.from("c1")),
                Arguments.of(Position.from("c7"))
        );
    }


    @BeforeEach
    void setUp() {
        rook = new Rook(Color.BLACK, Position.from("c5"));
    }


    @ParameterizedTest
    @MethodSource("destinations")
    void move(Position position) {
        rook.moveToEmpty(position, new Pieces());
        assertTrue(rook.hasPosition(position));
    }

    @ParameterizedTest
    @MethodSource("destinations")
    void interruptedMove(Position position) {
        List<Position> positions = Arrays.asList(Position.from("b5"), Position.from("e5"), Position.from("c2"), Position.from("c6"));

        assertThatThrownBy(() -> rook.moveToEmpty(position, blackPieces(positions)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Pieces blackPieces(List<Position> positions) {
        Piece[] pieces = new Piece[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            pieces[i] = new Pawn(Color.BLACK, positions.get(i));
        }
        return new Pieces(pieces);
    }


    @Test
    @DisplayName("룩 이동 가능한 위치 값 들 확인")
    void possiblePositions() {
        Position position = Position.from("c5");
        List<Position> positions = rook.movablePositions(position);
        assertThat(positions).contains(
                Position.from("a5"),
                Position.from("b5"),
                Position.from("d5"),
                Position.from("e5"),
                Position.from("f5"),
                Position.from("g5"),
                Position.from("h5"),
                Position.from("c1"),
                Position.from("c2"),
                Position.from("c3"),
                Position.from("c4"),
                Position.from("c6"),
                Position.from("c7"),
                Position.from("c8")
        );
    }
}
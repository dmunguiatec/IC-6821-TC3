package edu.tec.ic6821.tictactoe.board;

import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CountingBoardTest {

    @Test
    public void applyMoveWithValidPosition() {
        // given
        final CountingBoard board = new CountingBoard();

        // when
        final MoveStatus status = board.applyMove(new Pair<>(Row.TOP, Column.CENTER), Token.X);

        // then
        assertEquals(MoveStatus.TOKEN_SET, status);
    }

    @Test
    public void applyMoveOnOccupiedBox() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);

        // when
        final MoveStatus status = board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);

        // then
        assertEquals(MoveStatus.BOX_OCCUPIED, status);
    }

    @Test
    public void applyMoveTokenWinsHorizontal() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.TOP, Column.CENTER), Token.X);

        // when
        final MoveStatus status = board.applyMove(new Pair<>(Row.TOP, Column.RIGHT), Token.X);

        // then
        assertEquals(MoveStatus.X_WINS, status);
    }

    @Test
    public void applyMoveTokenWinsVertical() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.CENTER), Token.O);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.O);

        // when
        final MoveStatus status = board.applyMove(new Pair<>(Row.BOTTOM, Column.CENTER), Token.O);

        // then
        assertEquals(MoveStatus.O_WINS, status);
    }

    @Test
    public void applyMoveTokenWinsOnFirstDiagonal() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.X);

        // when
        final MoveStatus status = board.applyMove(new Pair<>(Row.BOTTOM, Column.RIGHT), Token.X);

        // then
        assertEquals(MoveStatus.X_WINS, status);
    }

    @Test
    public void applyMoveTokenWinsOnSecondDiagonal() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.RIGHT), Token.O);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.O);

        // when
        final MoveStatus status = board.applyMove(new Pair<>(Row.BOTTOM, Column.LEFT), Token.O);

        // then
        assertEquals(MoveStatus.O_WINS, status);
    }

    @Test
    public void applyMoveGameTied() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.CENTER), Token.X);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.TOP, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.TOP, Column.CENTER), Token.O);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.O);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.RIGHT), Token.O);

        // when
        final MoveStatus status = board.applyMove(new Pair<>(Row.BOTTOM, Column.LEFT), Token.O);

        // then
        assertEquals(MoveStatus.GAME_TIED, status);
    }

    @Test
    public void emptyBoardToString() {
        // given
        final CountingBoard board = new CountingBoard();
        final String emptyBoardText =
            "+---+---+---+\n"
                + "|   |   |   |\n"
                + "+---+---+---+\n"
                + "|   |   |   |\n"
                + "+---+---+---+\n"
                + "|   |   |   |\n"
                + "+---+---+---+\n";

        // when
        final String boardText = board.toString();

        // then
        assertEquals(emptyBoardText, boardText);
    }

    @Test
    public void randomBoardToString() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.X);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.RIGHT), Token.O);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.CENTER), Token.O);

        final String randomBoardText =
            "+---+---+---+\n"
                + "| X |   |   |\n"
                + "+---+---+---+\n"
                + "|   | X | O |\n"
                + "+---+---+---+\n"
                + "|   | O | X |\n"
                + "+---+---+---+\n";

        // when
        final String boardText = board.toString();

        // then
        assertEquals(randomBoardText, boardText);
    }

    @Test
    public void fullBoardToString() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.X);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.TOP, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.RIGHT), Token.O);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.CENTER), Token.O);
        board.applyMove(new Pair<>(Row.TOP, Column.CENTER), Token.O);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.LEFT), Token.O);

        final String fullBoardText =
            "+---+---+---+\n"
                + "| X | O | X |\n"
                + "+---+---+---+\n"
                + "| X | X | O |\n"
                + "+---+---+---+\n"
                + "| O | O | X |\n"
                + "+---+---+---+\n";

        // when
        final String boardText = board.toString();

        // then
        assertEquals(fullBoardText, boardText);
    }

    @Test
    public void randomBoardGetText() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.X);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.RIGHT), Token.O);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.CENTER), Token.O);

        final String randomBoardText =
                "+---+---+---+\n"
                        + "| X |   |   |\n"
                        + "+---+---+---+\n"
                        + "|   | X | O |\n"
                        + "+---+---+---+\n"
                        + "|   | O | X |\n"
                        + "+---+---+---+\n";

        // when
        final String boardText = board.getText();

        // then
        assertEquals(randomBoardText, boardText);
    }

    @Test
    public void isFullWithNonFullBoard() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.X);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.RIGHT), Token.O);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.CENTER), Token.O);

        // when
        final boolean isFull = board.isFull();

        // then
        assertFalse(isFull);
    }

    @Test
    public void isFullWithFullBoard() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.CENTER), Token.X);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.TOP, Column.RIGHT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.LEFT), Token.X);
        board.applyMove(new Pair<>(Row.MIDDLE, Column.RIGHT), Token.O);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.CENTER), Token.O);
        board.applyMove(new Pair<>(Row.TOP, Column.CENTER), Token.O);
        board.applyMove(new Pair<>(Row.BOTTOM, Column.LEFT), Token.O);

        // when
        final boolean isFull = board.isFull();

        // then
        assertTrue(isFull);
    }

    @Test
    public void getPositionOccupiedCell() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);

        // when
        final Optional<Token> position = board.getPosition(new Pair<>(Row.TOP, Column.LEFT));

        // then
        assertTrue(position.isPresent());
        assertEquals(position.get(), Token.X);
    }

    @Test
    public void getPositionEmptyCell() {
        // given
        final CountingBoard board = new CountingBoard();
        board.applyMove(new Pair<>(Row.TOP, Column.LEFT), Token.X);

        // when
        final Optional<Token> position = board.getPosition(new Pair<>(Row.BOTTOM, Column.CENTER));

        // then
        assertTrue(position.isEmpty());
    }
}

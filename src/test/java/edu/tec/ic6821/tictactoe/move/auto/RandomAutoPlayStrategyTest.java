package edu.tec.ic6821.tictactoe.move.auto;

import edu.tec.ic6821.tictactoe.board.BoardState;
import edu.tec.ic6821.tictactoe.board.Column;
import edu.tec.ic6821.tictactoe.board.Row;
import edu.tec.ic6821.tictactoe.board.Token;
import edu.tec.ic6821.tictactoe.player.Player;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RandomAutoPlayStrategyTest {

    @Mock
    private Player player;

    @Mock
    private BoardState boardState;

    @Test(expected = IllegalArgumentException.class)
    public void generateMoveFullBoard() {
        // given
        final RandomAutoPlayerStrategy strategy = new RandomAutoPlayerStrategy();
        when(boardState.isFull()).thenReturn(true);

        // when ... then Exception
        strategy.generateMove(player, boardState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateMoveManualPlayer() {
        // given
        final RandomAutoPlayerStrategy strategy = new RandomAutoPlayerStrategy();
        // player.isAuto() is false by default

        // when ... then Exception
        strategy.generateMove(player, boardState);
    }

    @Test
    public void generateMoveEmptyBoard() {
        // given
        final RandomAutoPlayerStrategy strategy = new RandomAutoPlayerStrategy();
        when(player.isAuto()).thenReturn(true);
        // boardState.isFull() is false by default
        when(boardState.getPosition(any())).thenReturn(Optional.empty());

        // when
        final Pair<Row, Column> move = strategy.generateMove(player, boardState);

        // then
        assertNotNull(move.getFirst());
        assertNotNull(move.getSecond());
    }

    @Test
    public void generateMoveNonEmptyBoard() {
        // given
        final RandomAutoPlayerStrategy strategy = new RandomAutoPlayerStrategy();
        final Pair<Row, Column> pos1 = new Pair<>(Row.MIDDLE, Column.RIGHT);
        final Pair<Row, Column> pos2 = new Pair<>(Row.TOP, Column.CENTER);
        when(player.isAuto()).thenReturn(true);
        // boardState.isFull() is false by default
        lenient().when(boardState.getPosition(pos1)).thenReturn(Optional.of(Token.X));
        lenient().when(boardState.getPosition(pos2)).thenReturn(Optional.of(Token.O));

        // when
        final Pair<Row, Column> move = strategy.generateMove(player, boardState);

        // then
        assertNotNull(move.getFirst());
        assertNotNull(move.getSecond());
        assertNotEquals(pos1, move);
        assertNotEquals(pos2, move);
    }

}

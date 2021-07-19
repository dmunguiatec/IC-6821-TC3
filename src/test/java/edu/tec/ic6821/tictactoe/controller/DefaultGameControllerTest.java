package edu.tec.ic6821.tictactoe.controller;

import edu.tec.ic6821.tictactoe.board.Board;
import edu.tec.ic6821.tictactoe.board.BoardState;
import edu.tec.ic6821.tictactoe.board.Column;
import edu.tec.ic6821.tictactoe.board.MoveStatus;
import edu.tec.ic6821.tictactoe.board.Row;
import edu.tec.ic6821.tictactoe.board.Token;
import edu.tec.ic6821.tictactoe.move.ManualMoveSpec;
import edu.tec.ic6821.tictactoe.move.auto.AutoPlayerStrategy;
import edu.tec.ic6821.tictactoe.player.Player;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGameControllerTest {
    @Mock
    private ManualMoveSpec manualMoveSpec;

    @Mock
    private Board board;

    @Mock
    private AutoPlayerStrategy strategy;

    @Mock
    private Player player1;

    @Mock
    private Player player2;

    @Mock
    private BoardState boardState;

    @Test
    public void doManualMoveInvalidSpec() {
        // given
        final DefaultGameController controller = new DefaultGameController(
                manualMoveSpec,
                board,
                strategy,
                player1,
                player2
        );
        given(manualMoveSpec.parse(anyString())).willReturn(Optional.empty());

        // when
        final MoveStatus status = controller.doManualMove(player1, "en la esquina arriba");

        // then
        assertEquals(MoveStatus.INVALID_POSITION, status);
    }

    @Test
    public void doManualMove() {
        // given
        final DefaultGameController controller = new DefaultGameController(
                manualMoveSpec,
                board,
                strategy,
                player1,
                player2
        );

        final String moveSpec = "medio derecha";
        final Pair<Row, Column> move = new Pair<>(Row.MIDDLE, Column.RIGHT);

        given(player1.getToken()).willReturn(Token.X);
        given(manualMoveSpec.parse(anyString())).willReturn(Optional.of(move));
        given(board.applyMove(any(), any())).willReturn(MoveStatus.TOKEN_SET);

        // when
        final MoveStatus status = controller.doManualMove(player1, moveSpec);

        // then
        verify(manualMoveSpec).parse(moveSpec);
        verify(board).applyMove(move, Token.X);
        assertEquals(MoveStatus.TOKEN_SET, status);
    }

    @Test
    public void doAutoMove() {
        // given
        final DefaultGameController controller = new DefaultGameController(
                manualMoveSpec,
                board,
                strategy,
                player1,
                player2
        );

        final Pair<Row, Column> move = new Pair<>(Row.TOP, Column.CENTER);

        given(player1.getToken()).willReturn(Token.X);
        given(board.getCurrentBoardState()).willReturn(boardState);
        given(strategy.generateMove(any(), any())).willReturn(move);
        given(board.applyMove(any(), any())).willReturn(MoveStatus.TOKEN_SET);

        // when
        final MoveStatus status = controller.doAutoMove(player1);

        // then
        verify(board).getCurrentBoardState();
        verify(strategy).generateMove(player1, boardState);
        verify(board).applyMove(move, Token.X);
        assertEquals(status, MoveStatus.TOKEN_SET);
    }

    @Test
    public void getBoardState() {
        // given
        final DefaultGameController controller = new DefaultGameController(
                manualMoveSpec,
                board,
                strategy,
                player1,
                player2
        );

        given(board.getCurrentBoardState()).willReturn(boardState);

        // when
        final BoardState state = controller.getBoardState();

        // then
        verify(board).getCurrentBoardState();
        assertEquals(boardState, state);
    }

    @Test
    public void nextPlayer() {
        // given
        final DefaultGameController controller = new DefaultGameController(
                manualMoveSpec,
                board,
                strategy,
                player1,
                player2
        );

        // when
        final Player first = controller.nextPlayer();
        final Player second = controller.nextPlayer();
        final Player third = controller.nextPlayer();

        // then
        assertEquals(player1, first);
        assertEquals(player2, second);
        assertEquals(player1, third);
    }
}

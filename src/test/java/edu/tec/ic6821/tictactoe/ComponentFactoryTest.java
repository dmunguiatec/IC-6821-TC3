package edu.tec.ic6821.tictactoe;

import edu.tec.ic6821.tictactoe.board.BoardState;
import edu.tec.ic6821.tictactoe.controller.DefaultGameController;
import edu.tec.ic6821.tictactoe.controller.GameController;
import edu.tec.ic6821.tictactoe.player.Player;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ComponentFactoryTest {

    @Test
    public void gameController() {
        // given
        final int level = 1;
        final boolean auto1 = true;
        final boolean auto2 = false;

        // when
        final GameController controller = ComponentFactory.gameController(level, auto1, auto2);

        // then
        assertNotNull(controller);
        assertTrue(controller instanceof DefaultGameController);

        final BoardState boardState = controller.getBoardState();
        assertNotNull(boardState);
        assertFalse(boardState.isFull());

        final Player player1 = controller.nextPlayer();
        final Player player2 = controller.nextPlayer();
        assertTrue(player1.isAuto());
        assertFalse(player2.isAuto());
    }

    @Test(expected = IllegalArgumentException.class)
    public void gameControllerWithLevelNotEqualTo1() {
        // given
        final int level = 2;
        final boolean auto1 = false;
        final boolean auto2 = true;

        // when ... then
        final GameController controller = ComponentFactory.gameController(level, auto1, auto2);
    }
}

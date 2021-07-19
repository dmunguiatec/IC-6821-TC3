package edu.tec.ic6821.tictactoe.player;

import edu.tec.ic6821.tictactoe.board.Token;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DefaultPlayerTest {

    @Test
    public void constructor() {
        // given
        final Token token = Token.X;
        final boolean auto = true;
        final String label = "Jugador 1";

        // when
        final Player player = new DefaultPlayer(token, auto, label);

        // then
        assertNotNull(player);
        assertEquals(token, player.getToken());
        assertEquals(auto, player.isAuto());
        assertEquals(label, player.getLabel());
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullToken() {
        // given
        final Token token = null;
        final boolean auto = false;
        final String label = "Jugador 1";

        // when ... then
        new DefaultPlayer(token, auto, label);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullLabel() {
        // given
        final Token token = Token.X;
        final boolean auto = false;
        final String label = null;

        // when ... then
        new DefaultPlayer(token, auto, label);
    }

}

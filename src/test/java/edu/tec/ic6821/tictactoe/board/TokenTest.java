package edu.tec.ic6821.tictactoe.board;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void tokenOToString() {
        // given
        final Token token = Token.O;

        // when
        final String textToken = token.toString();

        // then
        assertEquals("O", textToken);
    }

    @Test
    public void tokenXToString() {
        final Token token = Token.X;
        final String textToken = token.toString();
        assertEquals("X", textToken);
    }
}

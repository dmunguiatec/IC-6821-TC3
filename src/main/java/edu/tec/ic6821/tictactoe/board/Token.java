package edu.tec.ic6821.tictactoe.board;

public enum Token {
    X("X"),
    O("O");

    private final String textToken;

    Token(final String token) {
        this.textToken = token;
    }

    @Override
    public String toString() {
        return this.textToken;
    }
}

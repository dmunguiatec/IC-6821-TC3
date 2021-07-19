package edu.tec.ic6821.tictactoe.board;

import org.apache.commons.math3.util.Pair;

public interface Board {
    MoveStatus applyMove(Pair<Row, Column> move, Token token);
}

package edu.tec.ic6821.tictactoe.board;

import org.apache.commons.math3.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class CountingBoard implements Board {

    private static final int BOARD_ROW_COUNT = 3;
    private static final int BOARD_COLUMN_COUNT = 3;

    private static final int X_POINTS = 1;
    private static final int O_POINTS = -1;
    private static final int X_TARGET = 3;
    private static final int O_TARGET = -3;

    private final Map<Pair<Row, Column>, Token> board = new HashMap<>();
    private final Map<Row, Integer> rowCounts = new HashMap<>();
    private final Map<Column, Integer> colCounts = new HashMap<>();
    private int diagonal = 0;
    private int antiDiagonal = 0;
    private int freeCells = BOARD_COLUMN_COUNT * BOARD_ROW_COUNT;

    @Override
    public MoveStatus applyMove(Pair<Row, Column> move, Token token) {
        final Row row = move.getFirst();
        final Column col = move.getSecond();

        final int rowIndex = row.ordinal();
        final int colIndex = col.ordinal();

        if (this.board.containsKey(move)) {
            return MoveStatus.BOX_OCCUPIED;
        }

        final int point = token == Token.X ? X_POINTS : O_POINTS;

        this.rowCounts.compute(row, (k, v) -> (v == null) ? point : v + point);
        this.colCounts.compute(col, (k, v) -> (v == null) ? point : v + point);

        if (rowIndex == colIndex) {
            this.diagonal += point;
        }

        if (rowIndex + colIndex == 2) {
            this.antiDiagonal += point;
        }

        this.board.put(move, token);
        this.freeCells--;

        return verifyGameState(row, col, token);
    }

    private MoveStatus verifyGameState(Row row, Column col, Token token) {
        final MoveStatus winStatus = token == Token.X ? MoveStatus.X_WINS : MoveStatus.O_WINS;
        final int target = token == Token.X ? X_TARGET : O_TARGET;

        if (this.rowCounts.getOrDefault(row, 0) == target
                || this.colCounts.getOrDefault(col, 0) == target
                || this.diagonal == target
                || this.antiDiagonal == target) {
            return winStatus;
        }

        if (this.freeCells == 0) {
            return MoveStatus.GAME_TIED;
        }

        return MoveStatus.TOKEN_SET;
    }

    public String toString() {

        final String template =
                "+---+---+---+\n"
                        + "| %s | %s | %s |\n"
                        + "+---+---+---+\n"
                        + "| %s | %s | %s |\n"
                        + "+---+---+---+\n"
                        + "| %s | %s | %s |\n"
                        + "+---+---+---+\n";

        final Function<Token, String> ws = (Token token) -> (token != null) ? token.toString() : " ";

        return String.format(template,
                ws.apply(this.board.get(new Pair<>(Row.TOP, Column.LEFT))),
                ws.apply(this.board.get(new Pair<>(Row.TOP, Column.CENTER))),
                ws.apply(this.board.get(new Pair<>(Row.TOP, Column.RIGHT))),
                ws.apply(this.board.get(new Pair<>(Row.MIDDLE, Column.LEFT))),
                ws.apply(this.board.get(new Pair<>(Row.MIDDLE, Column.CENTER))),
                ws.apply(this.board.get(new Pair<>(Row.MIDDLE, Column.RIGHT))),
                ws.apply(this.board.get(new Pair<>(Row.BOTTOM, Column.LEFT))),
                ws.apply(this.board.get(new Pair<>(Row.BOTTOM, Column.CENTER))),
                ws.apply(this.board.get(new Pair<>(Row.BOTTOM, Column.RIGHT))));
    }
}

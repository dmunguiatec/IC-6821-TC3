package edu.tec.ic6821.tictactoe.move;

import edu.tec.ic6821.tictactoe.board.Column;
import edu.tec.ic6821.tictactoe.board.Row;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultManualMoveSpecTest {

    @Test
    public void parseNullMoveSpec() {
        // given
        final DefaultManualMoveSpec manualMoveSpec = new DefaultManualMoveSpec();

        // when
        final Optional<Pair<Row, Column>> move = manualMoveSpec.parse(null);

        // then
        assertTrue(move.isEmpty());
    }

    @Test
    public void parseEmptyMoveSpec() {
        // given
        final DefaultManualMoveSpec manualMoveSpec = new DefaultManualMoveSpec();

        // when
        final Optional<Pair<Row, Column>> move = manualMoveSpec.parse("");

        // then
        assertTrue(move.isEmpty());
    }

    @Test
    public void parseBlankMoveSpec() {
        // given
        final DefaultManualMoveSpec manualMoveSpec = new DefaultManualMoveSpec();

        // when
        final Optional<Pair<Row, Column>> move = manualMoveSpec.parse("     ");

        // then
        assertTrue(move.isEmpty());
    }

    @Test
    public void parseMoreThanTwoTokensMoveSpec() {
        // given
        final DefaultManualMoveSpec manualMoveSpec = new DefaultManualMoveSpec();

        // when
        final Optional<Pair<Row, Column>> move = manualMoveSpec.parse("arriba izquierda centro");

        // then
        assertTrue(move.isEmpty());
    }

    @Test
    public void parseInvalidFirstTokenMoveSpec() {
        // given
        final DefaultManualMoveSpec manualMoveSpec = new DefaultManualMoveSpec();

        // when
        final Optional<Pair<Row, Column>> move = manualMoveSpec.parse("cualquiera izquierda");

        // then
        assertTrue(move.isEmpty());
    }

    @Test
    public void parseInvalidSecondTokenMoveSpec() {
        // given
        final DefaultManualMoveSpec manualMoveSpec = new DefaultManualMoveSpec();

        // when
        final Optional<Pair<Row, Column>> move = manualMoveSpec.parse("arriba cualquiera");

        // then
        assertTrue(move.isEmpty());
    }

    @Test
    public void parseValidMoveSpecs() {
        // given
        final DefaultManualMoveSpec manualMoveSpec = new DefaultManualMoveSpec();

        // when
        final Optional<Pair<Row, Column>> move1 = manualMoveSpec.parse("superior izquierda");
        final Optional<Pair<Row, Column>> move2 = manualMoveSpec.parse("medio centro");
        final Optional<Pair<Row, Column>> move3 = manualMoveSpec.parse("inferior derecha");

        // then
        assertTrue(move1.isPresent());
        assertEquals(Row.TOP, move1.get().getFirst());
        assertEquals(Column.LEFT, move1.get().getSecond());
        assertTrue(move2.isPresent());
        assertEquals(Row.MIDDLE, move2.get().getFirst());
        assertEquals(Column.CENTER, move2.get().getSecond());
        assertTrue(move3.isPresent());
        assertEquals(Row.BOTTOM, move3.get().getFirst());
        assertEquals(Column.RIGHT, move3.get().getSecond());
    }


}

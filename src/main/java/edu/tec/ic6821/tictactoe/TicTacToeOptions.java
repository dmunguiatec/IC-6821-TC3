package edu.tec.ic6821.tictactoe;

import com.beust.jcommander.Parameter;

public final class TicTacToeOptions {

    @Parameter(
            names = {"--auto1", "-a1"},
            description = "El jugador 1 es automático, si no se indica se asume que el jugador será manual"
    )
    private boolean auto1;

    @Parameter(
            names = {"--auto2", "-a2"},
            description = "El jugador 2 es automático, si no se indica se asume que el jugador será manual"
    )
    private boolean auto2;

    @Parameter(
            names = {"--nivel", "-n"},
            required = true,
            description = "Nivel de los oponentes automáticos: 1 (fácil), 2 (intermedio), 3 (difícil)"
    )
    private int level;

    public boolean isAuto1() {
        return auto1;
    }

    public boolean isAuto2() {
        return auto2;
    }

    public int getLevel() {
        return level;
    }
}

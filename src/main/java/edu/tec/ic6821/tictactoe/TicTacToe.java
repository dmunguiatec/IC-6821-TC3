package edu.tec.ic6821.tictactoe;

import com.beust.jcommander.JCommander;
import edu.tec.ic6821.tictactoe.board.BoardState;
import edu.tec.ic6821.tictactoe.board.MoveStatus;
import edu.tec.ic6821.tictactoe.controller.GameController;
import edu.tec.ic6821.tictactoe.player.Player;

import java.util.Map;
import java.util.Scanner;

import static java.util.Map.entry;

public final class TicTacToe {

    private static final String PROMPT = "> ";
    private static final String MSG_INVALID_POSITION = "No entendí la movida, intente de nuevo";
    private static final String MSG_BOX_OCCUPIED = "Esa casilla ya está ocupada, indique otra posición";
    private static final String MSG_GAME_TIED = "Han empatado";
    private static final String MSG_O_WINS = "¡O ha ganado!";
    private static final String MSG_X_WINS = "¡X ha ganado!";

    private static final Map<MoveStatus, String> MESSAGES = Map.ofEntries(
            entry(MoveStatus.INVALID_POSITION, MSG_INVALID_POSITION),
            entry(MoveStatus.BOX_OCCUPIED, MSG_BOX_OCCUPIED),
            entry(MoveStatus.GAME_TIED, MSG_GAME_TIED),
            entry(MoveStatus.O_WINS, MSG_O_WINS),
            entry(MoveStatus.X_WINS, MSG_X_WINS)
    );

    private final GameController gameController;

    private TicTacToe(final boolean auto1, final boolean auto2, final int level) {
        this.gameController = ComponentFactory.gameController(level, auto1, auto2);
    }

    public static void main(String[] args) {
        try {
            final TicTacToeOptions ticTacToeOptions = new TicTacToeOptions();

            JCommander.newBuilder()
                    .addObject(ticTacToeOptions)
                    .build()
                    .parse(args);

            final TicTacToe ticTacToe = new TicTacToe(
                    ticTacToeOptions.isAuto1(),
                    ticTacToeOptions.isAuto2(),
                    ticTacToeOptions.getLevel()
            );

            ticTacToe.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void start() {
        final Scanner scanner = new Scanner(System.in);
        boolean gameFinished = false;
        Player currentPlayer = gameController.nextPlayer();

        BoardState boardState = gameController.getBoardState();
        System.out.println(boardState.getText());

        while (!gameFinished) {

            final MoveStatus status;
            if (currentPlayer.isAuto()) {
                status = gameController.doAutoMove(currentPlayer);
            } else {
                System.out.print(currentPlayer.getLabel() + PROMPT);
                final String move = scanner.nextLine();
                System.out.println();
                status = gameController.doManualMove(currentPlayer, move);
            }

            if (MESSAGES.containsKey(status)) {
                System.out.println(MESSAGES.get(status));
            }

            if (status == MoveStatus.TOKEN_SET) {
                currentPlayer = gameController.nextPlayer();
            }

            if (status == MoveStatus.GAME_TIED
                    || status == MoveStatus.O_WINS
                    || status == MoveStatus.X_WINS) {
                gameFinished = true;
            }

            boardState = gameController.getBoardState();
            System.out.println(boardState.getText());
        }
    }

}

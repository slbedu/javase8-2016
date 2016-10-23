package bg.uni.sofia.fmi.corejava.tictactoe;

import java.util.Scanner;

/**
 * Represents an implementation of Tic-Tac-Toe game.
 */

public class TicTacToe {

	private static final int BOARD_SIZE = 3;

	private static String[] players = { "X", "O" };

	private Board board;
	private int playerIndex = 0;

	/**
	 * Creates a new {@link TicTacToe}.
	 */

	public TicTacToe() {
		this.board = new Board(BOARD_SIZE);
	}

	/**
	 * Starts the game main loop.
	 */

	public void start() {
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String player = getNextPlayer();
				int position = readPosition(scanner, player);
				while (!board.put(position, player)) {
					System.out.println("Посоченото поле не съществува или вече е заето. Опитайте отново.");
					position = readPosition(scanner, player);
				}

				if (board.isWinner(player, position)) {
					System.out.println(board);
					System.out.println("Победител е играч " + player + "!");
					break;
				} else if (board.isDrawGame(players)) {
					System.out.println(board);
					System.out.println("Играта завърши наравно!");
					break;
				}
			}
		}
	}

	private String getNextPlayer() {
		if (playerIndex < 0 || playerIndex >= players.length) {
			playerIndex = 0;
		}

		return players[playerIndex++];
	}

	private int readPosition(Scanner scanner, String player) {
		promptInput(player);
		String line = scanner.nextLine();
		while (!line.matches("\\d+")) {
			System.out.println("Въведеният ход е невалиден. Опитайте отново.");
			promptInput(player);
			line = scanner.nextLine();
		}

		return Integer.parseInt(line);
	}

	private void promptInput(String player) {
		System.out.println(board);
		System.out.printf("Играч %s, моля въведете своя ход (1-%d)>", player, BOARD_SIZE * BOARD_SIZE);
	}
}

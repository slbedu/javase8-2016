package bg.uni.sofia.fmi.corejava.tictactoe;

import java.util.Arrays;

/**
 * Represents a Tic-Tac-Toe game board.
 */

public class Board {

	private int size;
	private String[][] board;

	/**
	 * Creates a new {@link Board} with given size.
	 * 
	 * @param size
	 */

	public Board(int size) {
		this.size = size;
		this.board = createBoard(size);
	}

	/**
	 * Puts a sign on the board. Returns whether the operation was successful.
	 * 
	 * @param position
	 *            the position on board.
	 * @param sign
	 *            the sign to mark with.
	 * 
	 * @return whether the operation was successful
	 */

	public boolean put(int position, String sign) {
		int row = getRowByPosition(position);
		int col = getColumnByPosition(position);

		if (position <= 0 || position > size * size || isOccupied(row, col)) {
			return false;
		}

		board[row][col] = sign;
		return true;
	}

	/**
	 * Returns whether given sign is a winner i.e. a column, row or diagonal is
	 * filled with the given sign.
	 * 
	 * @param sign
	 *            the sign to check.
	 * @param position
	 *            the last position
	 * @return whether given sign is a winner
	 */

	public boolean isWinner(String sign, int position) {
		int row = getRowByPosition(position);
		int col = getColumnByPosition(position);

		return hasWinningRow(sign, row) || hasWinningColumn(sign, col) || hasWinningDiagonal(sign);
	}

	/**
	 * Returns whether the game is draw i.e. the no one can win the game.
	 * 
	 * @param signs
	 *            the signs to check for.
	 * @return whether the game is draw
	 */

	public boolean isDrawGame(String[] signs) {
		for (int i = 0; i < signs.length; i++) {
			if (hasPotentialWinningRow(signs[i]) || hasPotentialWinningColumn(signs[i])
					|| hasPotentialWinningDiagonal(signs[i])) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				toString.append(formatCell(board[row][col]));
				if (col != size - 1) {
					toString.append('|');
				}
			}

			toString.append(System.lineSeparator());
			if (row != size - 1) {
				toString.append(getRowSeparator(size));
			}
		}

		return toString.toString();
	}

	private String[][] createBoard(int size) {
		String[][] board = new String[size][size];

		int current = 1;
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				board[row][col] = String.valueOf(current);
				current++;
			}
		}

		return board;
	}

	private boolean isOccupied(int row, int col) {
		return !isNumeric(board[row][col]);
	}

	public boolean hasWinningRow(String sign, int row) {
		for (int col = 0; col < size; col++) {
			if (!board[row][col].equals(sign)) {
				return false;
			}
		}

		return true;
	}

	private boolean hasPotentialWinningRow(String sign) {
		for (int row = 0; row < size; row++) {
			boolean isPotentialWinningRow = true;
			for (int col = 0; col < size; col++) {
				if (!board[row][col].equals(sign) && !isNumeric(board[row][col])) {
					isPotentialWinningRow = false;
				}
			}

			if (isPotentialWinningRow) {
				return true;
			}
		}

		return false;
	}

	private boolean hasWinningColumn(String sign, int col) {
		for (int row = 0; row < size; row++) {
			if (!board[row][col].equals(sign)) {
				return false;
			}
		}

		return true;
	}

	private boolean hasPotentialWinningColumn(String sign) {
		for (int col = 0; col < size; col++) {
			boolean isPotentialWinningColumn = true;
			for (int row = 0; row < size; row++) {
				if (!board[row][col].equals(sign) && !isNumeric(board[row][col])) {
					isPotentialWinningColumn = false;
				}
			}

			if (isPotentialWinningColumn) {
				return true;
			}
		}

		return false;
	}

	private boolean hasWinningDiagonal(String sign) {
		boolean isWinningMainDiagonal = true;
		for (int i = 0; i < size; i++) {
			if (!board[i][i].equals(sign)) {
				isWinningMainDiagonal = false;
				break;
			}
		}

		boolean isWinnigOppositeDiagonal = true;
		for (int i = 0; i < size; i++) {
			if (!board[i][size - i - 1].equals(sign)) {
				isWinnigOppositeDiagonal = false;
				break;
			}
		}
		return isWinnigOppositeDiagonal || isWinningMainDiagonal;
	}

	private boolean hasPotentialWinningDiagonal(String sign) {
		boolean isPotentialWinningMainDiagonal = true;
		for (int i = 0; i < size; i++) {
			if (!board[i][i].equals(sign) && !isNumeric(board[i][i])) {
				isPotentialWinningMainDiagonal = false;
				break;
			}
		}

		boolean isPotentialWinningOppositeDiagonal = true;
		for (int i = 0; i < board.length; i++) {
			if (!board[i][size - i - 1].equals(sign) && !isNumeric(board[i][board.length - i - 1])) {
				isPotentialWinningOppositeDiagonal = false;
				break;
			}
		}

		return isPotentialWinningMainDiagonal || isPotentialWinningOppositeDiagonal;
	}

	private String getRowSeparator(int size) {
		String[] dashes = new String[size];
		Arrays.fill(dashes, "---");
		return String.join("|", dashes) + System.lineSeparator();
	}

	private String formatCell(String cell) {
		if (cell.length() == 1) {
			return " " + cell + " ";
		} else if (cell.length() == 2) {
			return " " + cell;
		} else {
			return cell;
		}
	}

	private boolean isNumeric(String arg) {
		return arg.matches("\\d+");
	}

	private int getRowByPosition(int position) {
		return position % size == 0 ? (position / size) - 1 : (position / size);
	}

	private int getColumnByPosition(int position) {
		return position % size == 0 ? size - 1 : (position % size) - 1;
	}
}

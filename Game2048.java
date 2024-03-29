import java.util.Scanner;

class Cell {
	int value = 0;
	boolean hasMerged = false;
}

class Game {	
	final int BOARD_SIZE = 4;
	Cell[][] cells;

	void create() {
		cells = new Cell[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				cells[i][j] = new Cell();
				cells[i][j].value = 0;
			}
		}
	}

	int rowStart = 0;
	int columnStart = 0;
	int rowStep = 0;
	int columnStep = 0;
	int nextRow = 0;
	int nextColumn = 0;
	
	//to be used for move() and isLegal()
	void setDirection (char direction) {
		switch (direction) {
			case 'W': 
				rowStart = 1;
				columnStart = 0;
				rowStep = 1;
				columnStep = 1;
				nextRow = -1;
				nextColumn = 0;
				break;
			case 'A':
				rowStart = 0;
				columnStart = 1;
				rowStep = 1;
				columnStep = 1;
				nextRow = 0;
				nextColumn = -1;
				break;
			case 'S':
				rowStart = 2;
				columnStart = 0;
				rowStep = -1;
				columnStep = 1;
				nextRow = 1;
				nextColumn = 0;
				break;
			case 'D':
				rowStart = 0;
				columnStart = 2;
				rowStep = 1;
				columnStep = -1;
				nextRow = 0;
				nextColumn = 1;
				break;
		}
	}

	void move() {
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = rowStart; i >= 0 && i < BOARD_SIZE; i += rowStep) {
				for (int j = columnStart; j >= 0 && j < BOARD_SIZE; j += columnStep) {
					if (cells[i][j].value != 0 && cells[i + nextRow][j + nextColumn].value == 0) {
						changed = true;
						cells[i + nextRow][j + nextColumn].value = cells[i][j].value;
						cells[i + nextRow][j + nextColumn].hasMerged = cells[i][j].hasMerged;
						cells[i][j].value = 0;
						cells[i][j].hasMerged = false;
					}
					else if (cells[i][j].value != 0 && cells[i + nextRow][j + nextColumn].value == cells[i][j].value && !(cells[i][j].hasMerged || cells[i + nextRow][j + nextColumn].hasMerged)) {
						changed = true;
						cells[i + nextRow][j + nextColumn].value *= 2;
						cells[i + nextRow][j + nextColumn].hasMerged = true;
						cells[i][j].value = 0;
						cells[i][j].hasMerged = false;
					}			
				}
			}
		}

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				cells[i][j].hasMerged = false;
			}
		}
		
	}

	void generateRandomCell() {
		int count = 0;
		int[][] empty = new int[16][2];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (cells[i][j].value == 0) {
					empty[count][0] = i;
					empty[count][1] = j;
					count += 1;
				}
			}
		}
		
		int randomIndex = (int) (Math.random() * count);
		if (Math.random() < 0.9) {
			cells[empty[randomIndex][0]][empty[randomIndex][1]].value = 2;
		}
		else {
			cells[empty[randomIndex][0]][empty[randomIndex][1]].value = 4;
		}
	}
	
	boolean isLegal() {
		for (int i = rowStart; i >= 0 && i < BOARD_SIZE; i += rowStep) {
			for (int j = columnStart; j >= 0 && j < BOARD_SIZE; j += columnStep) {
					if (cells[i][j].value != 0 && (cells[i + nextRow][j + nextColumn].value == 0 || cells[i][j].value == cells[i + nextRow][j + nextColumn].value)) {
						return true;
					}
			}
		}
		return false;
	}

	boolean isOver() {
		char[] directions = {'W', 'A', 'S', 'D'};
		for (int i = 0; i < BOARD_SIZE; i++) {
			setDirection(directions[i]);
			if (isLegal()) {
				return false;
			}
		}
		return true;
	}

	void printBoard() {
		int width = 5;
		int space;
		System.out.print("\n");
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				space = width - String.valueOf(cells[i][j].value).length();
				if (cells[i][j].value != 0) {
					System.out.print(cells[i][j].value);
				}
				else 
				{
					System.out.print("_");
				}
				System.out.print(" ".repeat(space)); 
			}
			System.out.print("\n\n");
		}
		System.out.println("");
	}

}

class Game2048 {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Game game = new Game();
		
		game.create();
		game.generateRandomCell();

		System.out.print("\n\nHello!\nEnter W, A, S, D to move, and Q to quit.\n\n");

		while (!game.isOver()) {
			game.printBoard();

			System.out.println("Enter your move: ");
			String userInput = scanner.nextLine();
				
			if (userInput.length() != 1) {
				System.out.print("\nInvalid!\n");
				continue;
			}

			char userMove = userInput.charAt(0);

			if (userMove == 'Q') {
				System.exit(0);
			}
			else if (userMove != 'W' && userMove != 'A' && userMove != 'S' && userMove != 'D') {
				System.out.print("\nInvalid!\n");
				continue;
			}

			game.setDirection(userMove);
			if (game.isLegal()) {
				game.move();
				game.generateRandomCell();
			}
			else {
				System.out.print("\nIllegal!\n");
			}
		}

		System.out.print("\nGame over!\n");
	}
}
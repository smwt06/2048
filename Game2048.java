import java.util.Scanner;

class Game2048 {	
	final int BOARD_SIZE = 4;
	Cell[][] cells;
	
	Game2048() {
		cells = new Cell[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				cells[i][j] = new Cell();
				cells[i][j].value = 0;
			}
		}
	}
	
	class Cell {
		int value = 0;
		boolean hasMerged = false;
	}

	/*
 	parameters to ensure correct board traversal.
	traversal starts one cell away from the side corresponding to the direction of the user's move
 	and steps towards the other side.
  	nextRow and nextColumn are added to current indices to get the indices of the cell
   	that the current cell must be compared to for moving or merging.
	*/
	int rowStart = 0;
	int columnStart = 0;
	int rowStep = 0;
	int columnStep = 0;
	int nextRow = 0;
	int nextColumn = 0;
	
	//to be used in move() and isLegal()
	void setDirection(char direction) {
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

	void move(char direction) {
		setDirection(direction);
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
		int[][] empty = new int[BOARD_SIZE * BOARD_SIZE][2];
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
	
	boolean isLegal(char direction) {
		setDirection(direction);
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
		for (int i = 0; i < directions.length; i++) {
			if (isLegal(directions[i])) {
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
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Game2048 game = new Game2048();
		
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

			if (game.isLegal(userMove)) {
				game.move(userMove);
				game.generateRandomCell();
			}
			else {
				System.out.print("\nIllegal!\n");
			}
		}

		System.out.print("\nGame over!\n");
	}
}

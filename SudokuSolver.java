import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class SudokuSolver {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Please enter the name of the text file as an argument");
		}
		SudokuSolver ss = new SudokuSolver();
		int[][] board = ss.readBoard(args[0]);
		System.out.println("BOARD: ");
		ss.printBoard(board);
		long startTime = System.currentTimeMillis();
		ss.solve(board);
		long endTime = System.currentTimeMillis();
        ss.printBoard(board);
		System.out.println("Solved in " + (endTime - startTime) + " ms");
	}
	
	/**
	 * Reads a sudoku board from a text file
	 */
	protected int[][] readBoard(String textFile) {
		int[][] board = new int[9][9];
		try {
			File file = new File(textFile);
			Scanner scanner = new Scanner(file);		
			
			int r = 0;
			int c = 0;
			while(scanner.hasNextInt()) {
				if(r == 9) {
					r = 0;
					c++;
				}
				board[r][c] = scanner.nextInt();
				r++;
				
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return board;
	}
	
	protected void solve(int[][] board) {
		Cell[][] memo = setup(board);
		backtrack(board, memo);
	}
	
	protected boolean backtrack(int[][] board, Cell[][] memo) {
		// Metadata of the current move
		Cell current = getNextMove(board, memo);

		
		if(current == null) {
			return true;
		}
		
		for(int i=1; i < current.variablesAvailable.length; i++) {
			if(current.variablesAvailable[i]) {
				
				// assign
				current.assigned = true;
				board[current.row][current.col] = i;
				current.setQuadrant(i);
				//System.out.println("Doing - Row: " + (current.row+1) + " Col: " + (current.col+1));
				//printBoard(board);

				// backtract
				boolean valid = isValid(board, current, i);
				if( valid && backtrack(board, memo)) {
					return true;
				}
				
				// unassign
				current.assigned = false;
				current.unsetQuadrant(i);
				board[current.row][current.col] = 0;
				//System.out.println("Undoing - Row: " + (current.row+1) + " Col: " + (current.col+1));
				//printBoard(board);
			}
			

			
		}
		

		
		return false;
	}
	
	/**
	 * Checks if all the keys are valid
	 */
	boolean isValid(int[][] board, Cell cell, int key) {
		int rows = board.length;
		int cols = (board.length > 0) ? board[0].length : 0;
		
		for(int r = Cell.getRowByQuadrant(cell.quadrant); r < Cell.getRowByQuadrant(cell.quadrant) + 3; r++) {
			for(int c = Cell.getColByQuadrant(cell.quadrant); c < Cell.getColByQuadrant(cell.quadrant) + 3; c++) {
				if(r != cell.row && c != cell.col && board[r][c] == key) {
					return false;
				}
			}
		}
		
		for(int r=0; r < rows; r++) {
			if(r != cell.row && key == board[r][cell.col]) {
				return false;
			}
		}
		
		for(int c=0; c < cols; c++) {
			if(c != cell.col && key == board[cell.row][c]) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @return Cell table that holds metadata for each cell
	 */
	Cell[][] setup(int[][] board) {
		int rows = board.length;
		int cols = (board.length > 0) ? board[0].length : 0;
		Cell[][] memo = new Cell[rows][cols];
		
		for(int r=0; r < rows; r++) {
			for(int c=0; c < cols; c++) {
				memo[r][c] = new Cell(r,c,board);
				if(board[r][c] != 0) {
					memo[r][c].assigned = true;
				}
			}
		}
		return memo;
	}
	
	/**
	 * @return Cell of next best move
	 */
	protected Cell getNextMove(int[][] board, Cell[][] memo) {
		ArrayList<Cell> mostConstrainedVariables = new ArrayList<Cell>();
		
		int rows = board.length;
		int cols = (board.length > 0) ? board[0].length : 0;
		
		int minimumConstrains = 10;
		for(int r=0; r < rows; r++) {
			for(int c=0; c < cols; c++) {
				if(!memo[r][c].assigned) {					
					minimumConstrains = Math.min(minimumConstrains, memo[r][c].constrains);
				}
			}
		}
		
		for(int r=0; r < rows; r++) {
			for(int c=0; c < cols; c++) {
				if(memo[r][c].constrains == minimumConstrains && !memo[r][c].assigned) {
					mostConstrainedVariables.add(memo[r][c]);
				}
			}
		}
		
		return mostConstrainingVariable(board, memo, mostConstrainedVariables);
	}
	
	/** 
	 * Gets the most contraining variable 
	 */
	Cell mostConstrainingVariable(int[][] board, Cell[][] memo, ArrayList<Cell> constrainedVar) {
		int rows = board.length;
		int cols = (board.length > 0) ? board[0].length : 0;
		
		Cell mostConstraining = null;
		
		int mostConstrainingNumber = Integer.MAX_VALUE;
		for(Cell cell : constrainedVar) {
			
			int affectedCells = 0;
			for(int r = Cell.getRowByQuadrant(cell.quadrant); r < Cell.getRowByQuadrant(cell.quadrant) + 3; r++) {
				for(int c = Cell.getColByQuadrant(cell.quadrant); c < Cell.getColByQuadrant(cell.quadrant) + 3; c++) {
					if(board[r][c] == 0) {
						affectedCells++;
					}
				}
			}
			
			for(int r = 0; r < rows; r++) {
				if(board[r][cell.col] == 0 && r != cell.row) {
					affectedCells++;
				}
			}
			
			for(int c = 0; c < cols; c++) {
				if(board[cell.row][c] == 0 && c != cell.col) {
					affectedCells++;
				}
			}
			
			if(affectedCells > 0 && affectedCells < mostConstrainingNumber) {
				mostConstraining = cell;
				mostConstrainingNumber = affectedCells;
			}
			
			
		}
		
		return mostConstraining;
	}
	
	/**
	 * Prints the board
	 */
    public void printBoard(int[][] board){
        for(int r=0; r<9; r++){
            for(int c=0; c<9; c++){
                System.out.print(" "+ board[r][c] +" ");
                if((c+1)%3==0){
                    System.out.print("|");
                }
            }
            System.out.println();
            if((r+1)%3==0){
                
                System.out.println("------------------------------");
            }
        }
        System.out.println();
    }
	
	/** 
	 * Data-structure used to hold data for each cell
	 */
	static class Cell {
		int row;
		int col;
		int quadrant;
		int constrains = 10;
		boolean assigned = false;
		boolean[] variablesAvailable = new boolean[10];
		
		public Cell(int row, int col, int[][] board) {
			this.row = row;
			this.col = col;
			this.quadrant = quadrantByCoordinates(row, col);
			
			for(int i=0; i < 10; i++) {
				this.variablesAvailable[i] = true;
			}
			
			for(int r = getRowByQuadrant(this.quadrant); r < getRowByQuadrant(this.quadrant) + 3; r++) {
				for(int c = getColByQuadrant(this.quadrant); c < getColByQuadrant(this.quadrant) + 3; c++) {
					if(board[r][c] != 0 && this.variablesAvailable[board[r][c]]) {
						this.setQuadrant(board[r][c]);
					}
				}
			}
			
			int totalRows = board.length;
			int totalCols = board.length > 0 ? board[0].length : 0;
			
			for(int r = 0; r < totalRows; r++) {
				if(r != this.row && board[r][this.col] != 0) {
					int key = board[r][this.col];
					if(this.variablesAvailable[key]) {
						this.variablesAvailable[key] = false;
						this.constrains--;
					}
				}
				
			}
			
			for(int c = 0; c < totalCols; c++) {
				if(c != this.row && board[this.row][c] != 0) {
					int key = board[this.row][c];
					if(this.variablesAvailable[key]) {
						this.variablesAvailable[key] = false;
						this.constrains--;
					}
				}
				
			}
			
			
		}
		
		/**
		 * Adds a key to the quadrant
		 */
		public void setQuadrant(int key ) {
			if(this.variablesAvailable[key]) {				
				this.variablesAvailable[key] = false;
				this.constrains--;
			}
		}
		
		/**
		 * Removes a key from the quadrant
		 */
		public void unsetQuadrant(int key) {
			if(!this.variablesAvailable[key]) {				
				this.variablesAvailable[key] = true;
				this.constrains++;
			}
		}
		
		/**
		 *  Quadrants: divided into 3x3 Sub-boards
            | 1 | 2 | 3 |
            -------------
            | 4 | 5 | 6 |
            -------------
            | 7 | 8 | 9 |	
		 */
		static int quadrantByCoordinates(int c, int r){
			if(c >= 0 && c <= 2){
			    if(r >= 0 && r <= 2 ){
			        return 1;
			    }else if(r >= 3 && r<= 5){
			        return 2;
			    }else if(r >= 6 && r <= 8){
			        return 3;
			    }
			}else if(c >= 3 && c <= 5){
			    if(r >= 0 && r <= 2 ){
			        return 4;
			    }else if(r >= 3 && r<= 5){
			        return 5;
			    }else if(r >= 6 && r <= 8){
			        return 6;
			    }
			}else if(c >= 6 && c <= 8){
			    if(r >= 0 && r <= 2 ){
			        return 7;
			    }else if(r >= 3 && r<= 5){
			        return 8;
			    }else if(r >= 6 && r <= 8){
			        return 9;
			    }
			}
			
			return -1;
		}
		
		/**
		 * @return top-left starting column of quadrant
		 * 0 based coordinates
		 */
	    static int getColByQuadrant(int q){
            q = (q-1) % 3;
            return q*3;
	    }
	    
		/**
		 * @return top-left starting row of quadrant
		 * 0 based coordinates
		 */
	    static int getRowByQuadrant(int q){
	            q= (int) (q-1)/3;
	            return q*3;
	    }

	}
	
}

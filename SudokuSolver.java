import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;


public class SudokuSolver {
	

	/* r: 0   1   2    3   4   5    6   7   8    c:
	 * 
	 * 	| x | x | x || x | x | x || x | x | x |   0
	 *  | x | x | x || x | x | x || x | x | x |   1
	 *  | x | x | x || x | x | x || x | x | x |   2
	 *  ---------------------------------------
	 * 	| x | x | x || x | x | x || x | x | x |   3 
	 *  | x | x | x || x | x | x || x | x | x |   4
	 *  | x | x | x || x | x | x || x | x | x |   5
	 *  ---------------------------------------
	 * 	| x | x | x || x | x | x || x | x | x |   6
	 *  | x | x | x || x | x | x || x | x | x |   7
	 *  | x | x | x || x | x | x || x | x | x |   8
	 */
	
	public static void main(String[] args){
		SudokuSolver t = new SudokuSolver();
		System.out.println(t.columnsByQuadrant(1));
		System.out.println(t.columnsByQuadrant(2));
		System.out.println(t.columnsByQuadrant(3));
		System.out.println(t.columnsByQuadrant(4));
		System.out.println(t.columnsByQuadrant(5));
		System.out.println(t.columnsByQuadrant(6));
		System.out.println(t.columnsByQuadrant(7));
		System.out.println(t.columnsByQuadrant(8));
		System.out.println(t.columnsByQuadrant(9));
		//System.out.println(t.columnsByQuadrant(4));
	}
	
	public void sudokuSolver(int[][] board){
		
		
		HashMap<Integer, LinkedHashSet<Integer>> keySet = setBoard(board);
		
	}
	
	public boolean backtrackSudoku(int[][] board, HashMap<Integer, LinkedHashSet<Integer>> hm, int c, int r){
		
		if(board[c][r] != 0){
			
			
			
		}else{
			
		}
		
		
		
	}
	
	public HashMap<Integer, LinkedHashSet<Integer>> setBoard(int[][] board){

		HashMap<Integer, LinkedHashSet<Integer>> keySet  = new HashMap<Integer, LinkedHashSet<Integer>>();

		for(int quadrant=1; quadrant <= 9; quadrant++){
			LinkedHashSet<Integer> keys = new LinkedHashSet<Integer>();
			addNumbers(keys);
			
			int cStart = columnsByQuadrant(quadrant);
			int cEnd = cStart + 3;
			int rStart = rowsByQuadrant(quadrant);
			int rEnd = rStart + 3;
			for(int c = cStart; c < cEnd; c++){
				for(int r = rStart; r< rEnd; r++){
					int key = board[c][r];
					if(keys.contains(key)){
						keys.remove(key);
					}
				}
			}
			keySet.put(quadrant, keys);
		}
		
		return keySet;
		
	}
	
	public int rowsByQuadrant(int q){
		q = (q-1) % 3;
		return q*3;
	}
	
	public int columnsByQuadrant(int q){
		q= (int) (q-1)/3;
		return q*3;
	}
	
	public int quadrantByCoordinates(int r, int c){
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
	
	public void addNumbers(LinkedHashSet<Integer> numbers){
		for(int i=1; i<= 9; i++){
			numbers.add(i);
		}
	}
	 

	
	public boolean check(int[][] array, int row, int column, int number){
		// vertical check
		for(int i=0; i<9; i++){

			if( array[i][row] == number ){
					return false;
			}
		}
		// Horizontal check
		for(int i=0; i<9; i++){
			if(array[column][i] == number){
				return false;
			}
		}
		
		return true;
	}
}

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;


	/* r: 0   1   2    3   4   5    6   7   8    c:
	 * 
	 *  | x | x | x || x | x | x || x | x | x |   0
	 *  | x | x | x || x | x | x || x | x | x |   1
	 *  | x | x | x || x | x | x || x | x | x |   2
	 *  ---------------------------------------
	 *  | x | x | x || x | x | x || x | x | x |   3 
	 *  | x | x | x || x | x | x || x | x | x |   4
	 *  | x | x | x || x | x | x || x | x | x |   5
	 *  ---------------------------------------
	 *  | x | x | x || x | x | x || x | x | x |   6
	 *  | x | x | x || x | x | x || x | x | x |   7
	 *  | x | x | x || x | x | x || x | x | x |   8
	 
            Quadrants: divided into 3x3 Sub-boards
            | 1 | 2 | 3 |
            -------------
            | 4 | 5 | 6 |
            -------------
            | 7 | 8 | 9 |
            -------------
    */

public class SudokuSolver {
	

    
    
    // KeySet is a hashmap containing the key/value:  "Quadrant" -> {Set of available keys to put}
    // Ex: KeySet[ Quadrant 2 ] -> {1,2,6,8 keys left}
    public HashMap<Integer, LinkedHashSet<Integer>> keySet = null;
    
    public int[][] board = new int[9][9];
    
	
    SudokuSolver(int[][] board){
        if(board.length == 9 && board[0].length ==  9){
            keySet = new HashMap<>();
            // Reads from an 9x9 board and computers keys left to put 
            this.keySet = setBoard(board);
            this.board = board;
        }else{
            System.out.println("Error: Board dimensions should be 9x9");
        }

    }
    

    public static void main(String[] args){
        int[][] arr = new int[9][9];
        arr[0][0] = 1;
        arr[2][0] = 9;
        arr[1][1] = 5;
    
        SudokuSolver ss = new SudokuSolver(arr);
        System.out.println(ss.check(7,0,8));


    }

    public boolean backtrackSudoku(int[][] board, HashMap<Integer, LinkedHashSet<Integer>> hm, int c, int r){

        if(board[c][r] != 0){



        }else{

        }

        return false;

    }

    public HashMap<Integer, LinkedHashSet<Integer>> setBoard(int[][] board){

            HashMap<Integer, LinkedHashSet<Integer>> keySet  = new HashMap<Integer, LinkedHashSet<Integer>>();
            // Reading from each quadrant individually
            for(int quadrant=1; quadrant <= 9; quadrant++){
                    LinkedHashSet<Integer> keys = new LinkedHashSet<Integer>();
                    // Adding all keys to each quadrant
                    addNumbers(keys);
                    
                    int cStart = columnsByQuadrant(quadrant);
                    int rStart = rowsByQuadrant(quadrant);
                    for(int c = cStart; c < cStart+3; c++){
                            // Reading from each quadrant (3x3 sub-board)
                            for(int r = rStart; r< rStart+3; r++){
                                    int key = board[r][c];
                                    if(keys.contains(key)){
                                            keys.remove(key);
                                    }
                            }
                    }
                    keySet.put(quadrant, keys);
            }

            return keySet;

    }
    
    /**
     * 
     * @param q: 1 of the 9 possible quadrants
     * @return horizontal index j of the starting 3x3 sub-board
     */
    public int columnsByQuadrant(int q){
            q = (q-1) % 3;
            return q*3;
    }
    
    /**
     * 
     * @param q: 1 of the 9 possible quadrants
     * @return vertical index i of the starting 3x3 sub-board
     */
    public int rowsByQuadrant(int q){
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



    public boolean check(int row, int column, int number){
        // vertical check
        for(int i=0; i<9; i++){
            if( row != i &&  this.board[i][column] == number ){
                            return false;
            }
        }
        // Horizontal check
        for(int i=0; i<9; i++){
            if( row != i && this.board[row][i] == number){
                    return false;
            }
        }
        return true;
    }
}
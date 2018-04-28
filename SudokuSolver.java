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
        //arr[0][0] = 4;

        SudokuSolver ss = new SudokuSolver(arr);
        ss.solve();
        ss.printBoard();


    }
    
    public void solve(){
        for(int i=1; i<= 9; i++){
            backtrackSudoku(1,i);    
            
        }
    }
    
    public void printBoard(){
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
    }

    public void backtrackSudoku(int quadrant, int key){
        if(quadrant > 0 && quadrant <= 9){
            int row = getRowByQuadrant(quadrant);
            int col = getColumnByQuadrant(quadrant);
            
            if(this.keySet.get(quadrant).contains(key)){
                for(int r=row; r<row+3; r++){
                    for(int c=col;c<col+3; c++){
                        if(this.board[r][c] == 0){
                            if(check(r,c,key)){
                                this.board[r][c] = key;
                                this.keySet.get(quadrant).remove(key);
                                if(quadrant+1 <= 9){
                                    backtrackSudoku(quadrant+1, key);
                                }
                            }
                        }
                    }
                }
            }else{
                if(quadrant+1 <= 9){
                    backtrackSudoku(quadrant+1, key);
                }
            }
        }


    }

    public HashMap<Integer, LinkedHashSet<Integer>> setBoard(int[][] board){

            HashMap<Integer, LinkedHashSet<Integer>> keySet  = new HashMap<Integer, LinkedHashSet<Integer>>();
            // Reading from each quadrant individually
            for(int quadrant=1; quadrant <= 9; quadrant++){
                    LinkedHashSet<Integer> keys = new LinkedHashSet<Integer>();
                    // Adding all keys to each quadrant
                    addNumbers(keys);
                    
                    int cStart = getColumnByQuadrant(quadrant);
                    int rStart = getRowByQuadrant(quadrant);
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
    public int getColumnByQuadrant(int q){
            q = (q-1) % 3;
            return q*3;
    }
    
    /**
     * 
     * @param q: 1 of the 9 possible quadrants
     * @return vertical index i of the starting 3x3 sub-board
     */
    public int getRowByQuadrant(int q){
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

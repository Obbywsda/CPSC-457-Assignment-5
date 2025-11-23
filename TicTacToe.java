import java.util.Arrays;
import java.util.Random;

// just keeping the game board and current turn in one place
class Shared {
    volatile char[] board;
    volatile int turn;

    Shared() {
        board = new char[9];
        Arrays.fill(board, '-');
        turn = 0;   // main goes first
    }
}

// represents one of the players (either x or o)
class Player implements Runnable {

    private int id;      
    private char symbol;
    private Shared shared;
    private Random r = new Random();

    Player(int id, char symbol, Shared shared) {
        this.id = id;
        this.symbol = symbol;
        this.shared = shared;
    }

    @Override
    public void run() {

        //run until the main thread stops everything
        while (!Thread.currentThread().isInterrupted()){

            //thread waits for turn 
            while (shared.turn != id) {
                if (Thread.currentThread().isInterrupted()){
                    return;
                }
                Thread.yield();
            }

            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            // do one move
            putPiece();

            shared.turn = 0;   // let main do checks
        }
    }

    //player tries random spots until they find one open
    private void putPiece(){

        boolean done = false;

        while (!done){

            int pos = r.nextInt(9);

            if (shared.board[pos] == '-'){

                shared.board[pos] = symbol;
                done = true;
            }
        }
    }
}


public class TicTacToe {

    public static void main(String[] args){

        Shared shared = new Shared();

        //create the two player threads
        Thread px = new Thread(new Player(1, 'X', shared));
        Thread po = new Thread(new Player(2, 'O', shared));

        px.start();
        po.start();

        //print starting board just to see it
        System.out.println("Initial Board:");
        printBoard(shared.board);

        int cur = 1;    // player x starts
        shared.turn = cur;

        boolean over = false;

        //wait for move, check, switch turn, etc
        while (!over){

            while (shared.turn != 0){
                Thread.yield();
            }

            char s = (cur == 1 ? 'X' : 'O');

            //print current sitation in the game and whos winning
            System.out.println("Player " + s + "'s turn:");
            printBoard(shared.board);

            char w = checkWinner(shared.board);
            boolean full = isFull(shared.board);

            //check to see if win conditions have been met
            if (w == 'X' || w == 'O') {
                System.out.println("WINNER: Player " + w + " wins! :D");
                over = true;
            } else if (full) {
                System.out.println("TIE :(");
                over = true;
            } else {
                //next player
                cur = (cur == 1 ? 2 : 1);
                shared.turn = cur;
            }
        }

        //stop both player threads after the game
        px.interrupt();
        po.interrupt();

        try {
            px.join();
            po.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }

    //prints the board in a quick/dirty 3x3 layout
    private static void printBoard(char[] b){

        System.out.println("-------------");

        for (int i = 0; i < 3; i++){

            System.out.print("| ");
            for (int j = 0; j < 3; j++){
                System.out.print(b[i*3 + j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    //checks if any spot is still empty
    private static boolean isFull(char[] b){

        for (char c : b) {
            if (c == '-') return false;
        }
        return true;
    }

    //checks if any player has won
    private static char checkWinner(char[] b) {
        int[][] lines = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };
        for (int[] L : lines) {
            int a=L[0], c=L[2];
            if (b[a] != '-' && b[a] == b[L[1]] && b[L[1]] == b[c]) return b[a];
        }
        return '-';
    }
}

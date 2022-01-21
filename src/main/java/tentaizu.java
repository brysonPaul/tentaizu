import java.util.Scanner;
import java.util.StringTokenizer;

//Bryson Paul   1.20.2022   Professor Arup Guha COP3503 Tentaizu Solution
public class tentaizu
{
    static String[][] board;
    static String[][] complete;
    static boolean isComplete=false;
    public static void main( String[] args )
    {
        Scanner sc= new Scanner(System.in);
        int numOfBoards =sc.nextInt();
        for (int x=0;x<numOfBoards;x++){
            board = new String[7][7];
            readInput(sc);

            placeStar(0,0,10);

         //   printBoard(x+1);
            numOfBoards--;
        }
    }
    public static void procede(int r,int c, int usableStars){
        if(c>=6 && r>=6){
            System.out.println("complete");
            isComplete=true;
            printBoard(1);
        }
        else if(c>=6){
            placeStar(r+1,0,usableStars);
        }
        else{
            placeStar(r,c+1,usableStars);
        }
    }
    public static void placeStar(int r, int c,int usableStars){
       if(isComplete){
           return;
       }
       //if there is a number here
       else if(Character.isDigit(board[r][c].charAt(0))){
           System.out.println("Digit found");
           if (isNotEnoughOrMany(r,c,Integer.parseInt(board[r][c]),usableStars)){
               System.out.println("Issue found with stars, going back...");
                return;
           }
           else procede(r,c,usableStars);
       }
       else{
           board[r][c]="*";
           System.out.println("Star placed at "+r+" "+c);
           procede(r,c,usableStars-1);
           board[r][c]=".";
           System.out.println("Star removed at "+r+" "+c);
           procede(r,c,usableStars);
       }
    }
    //TRUE: too many or little stars
    //FALSE: not too many or little stars
    //note: to make this efficient this function needs to know what spots still can be filled and which cant.
    //EX: those at r-1 and r, c-1 CANNOT BE FILLED AGAIN. whatever is in them now will be for the rest of the run
    //those at r,c+1 and all of r+1 STILL NEED TO BE FILLED, but might have collisions with other numbers making it even smaller
    // if the number of open spots does not allow for the puzzle to work by restricting star usage, return true
    public static boolean isNotEnoughOrMany(int r, int c, int maxStars,int usableStars){
        System.out.println(maxStars+" "+usableStars);
        //we know r and c cannot be less than 0 or greater than
        for(int x=r-1;x<=r+1;x++){
            if(x<0||x>6){
                continue;
            }
            for(int y=c-1;y<=c+1;y++){
                if(y<0||y>6){
                    continue;
                }

                if(board[x][y].equals("*")){
                    maxStars--;
                }
            }
        }
        if(maxStars>usableStars || maxStars<0){
            return true;
        }
        else if(usableStars==0 && maxStars!=0){
            return true;
        }
        else return false;
    }
    public static void readInput(Scanner sc){

        for(int r=0;r<7;r++){
            String rowOfInput = sc.next();
            //initializes each row for the board
                for(int c=0;c<7;c++) {
                    board[r][c] = rowOfInput.substring(c,c+1);
                }
        }
    }
    public static void printBoard(int numberBoard){
        System.out.println("Tentaizu Board #"+numberBoard+":");
        for(int r=0;r<7;r++){
            for(int c=0;c<7;c++) {
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
    }
}

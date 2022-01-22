//import java.util.LinkedHashMap;
//import java.util.Scanner;
//import java.util.StringTokenizer;
//
////Bryson Paul   1.20.2022   Professor Arup Guha COP3503 Tentaizu Solution
//public class tentaizu
//{
//
//    static String[][] board;
//    static String[][] complete;
//    static boolean isComplete=false;
//    public static void main( String[] args )
//    {
//        Scanner sc= new Scanner(System.in);
//        int numOfBoards =sc.nextInt();
//        for (int x=0;x<numOfBoards;x++){
//            board = new String[7][7];
//            readInput(sc);
//            zeroClear();
//            go(0,0,10);
//
//            //   printBoard(x+1);
//            numOfBoards--;
//        }
//    }
//    public static void procede(int r,int c, int usableStars){
//        if(c>=6 && r>=6){
//            System.out.println("complete");
//            isComplete=true;
//            printBoard(1);
//        }
//        else if(c>=6){
//            go(r+1,0,usableStars);
//        }
//        else{
//            go(r,c+1,usableStars);
//        }
//    }
//    public static void zeroClear(){
//        for(int r=0;r<7;r++){
//            for(int c=0;c<7;c++) {
//                if(board[r][c].equals("0")){
//                    placeCharOnBoard(r,c,"-");//indicates NOTHING can go in these spots
//                }
//            }
//        }
//    }
//    public static void go(int r, int c, int usableStars) {
//        int[] starsUsed=new int[1];
//        printBoard(1);
//        starsUsed[0]=0;
//        if (isComplete) {
//            return;
//        }
//        else if (Character.isDigit(board[r][c].charAt(0))) {
//            System.out.println("Digit found");
//            if (!isPossible(r, c, Integer.parseInt(board[r][c]), usableStars, starsUsed)) {
//                System.out.println("Issue found with stars, going back...");
//                return;
//            }
//            else procede(r,c,usableStars);
//        }
//        else if (board[r][c].equals("-")) {
//            procede(r, c, usableStars);
//        }
//        else{
//            if(usableStars>=1 && board[r][c]!="-") {
//                board[r][c] = "*";
//                System.out.println("Star placed at " + r + " " + c);
//                procede(r, c, usableStars - 1);
//            }
//            board[r][c]=".";
//            System.out.println("Dot placed at "+r+" "+c);
//            procede(r,c,usableStars);
//        }
//
//    }
//
//    //TRUE: too many or little stars
//    //FALSE: not too many or little stars
//    //note: to make this efficient this function needs to know what spots still can be filled and which cant.
//    //EX: those at r-1 and r, c-1 CANNOT BE FILLED AGAIN. whatever is in them now will be for the rest of the run
//    //those at r,c+1 and all of r+1 STILL NEED TO BE FILLED, but might have collisions with other numbers making it even smaller
//    // if the number of open spots does not allow for the puzzle to work by restricting star usage, return true
//    public static boolean isPossible(int r, int c, int maxStars,int usableStars,int[] starsUsed){
//        int openSpotsForFuture = 0;//max 4
//        int starsLeft=maxStars;
//        System.out.println(maxStars+" "+usableStars);
//        //we know r and c cannot be less than 0 or greater than
//        for(int x=r-1;x<=r+1;x++){
//            if(x<0||x>6){
//                continue;
//            }
//            for(int y=c-1;y<=c+1;y++){
//                if(y<0||y>6){
//                    continue;
//                }
//                System.out.print(board[x][y]);
//                if((r+1==x )|| (r==x && c+1==y)){
//                    if(board[x][y].equals(".")){
//                        openSpotsForFuture++;
//                    }
//                }
//                else if(board[x][y].equals("*")){
//                    starsLeft--;
//                }
//            }
//            System.out.println();
//        }
//        if(starsLeft>usableStars || starsLeft<0){ //too many case
//            return false;
//        }
//        else if(usableStars==0 && starsLeft!=0){ //too little case
//            return false;
//        }
//        else if(starsLeft>openSpotsForFuture){ //not enough space for future case
//            return false;
//        }
//        else if(starsLeft==0){
//            placeCharOnBoard(r,c,"-");//meaning in all new spots, this - symbol will mean to add a . and keep going,never adding a star
//            return true;
//        }
//        else return true;
//    }
//    public static void readInput(Scanner sc){
//
//        for(int r=0;r<7;r++){
//            String rowOfInput = sc.next();
//            //initializes each row for the board
//            for(int c=0;c<7;c++) {
//                board[r][c] = rowOfInput.substring(c,c+1);
//            }
//        }
//    }
//    public static void printBoard(int numberBoard){
//       //resetPriority();
//        System.out.println("Tentaizu Board #"+numberBoard+":");
//        for(int r=0;r<7;r++){
//            for(int c=0;c<7;c++) {
//                System.out.print(board[r][c]);
//            }
//            System.out.println();
//        }
//    }
//    public static int placeCharOnBoard(int r, int c,String str){
//        int charsPlaced=0;
//        for(int x=r-1;x<=r+1;x++){
//            if(x<0||x>6){
//                continue;
//            }
//            for(int y=c-1;y<=c+1;y++){
//                if(y<0||y>6){
//                    continue;
//                }
//                    if(board[x][y].equals(".")){
//                        board[x][y]=str;
//                        charsPlaced++;
//                    }
//            }
//        }
//        return charsPlaced;
//    }
//
//    public static void resetPriority(){
//        for(int r=0;r<7;r++){
//            for(int c=0;c<7;c++) {
//                if(board[r][c].equals("-")){
//                    board[r][c]=".";
//                }
//            }
//        }
//    }
////    public static void placeStar(int r, int c,int usableStars){
////        int[] starsUsed = new int[1];
////        starsUsed[0]=0;
////        if(isComplete){
////            return;
////        }
////        //if there is a number here
////        else if(Character.isDigit(board[r][c].charAt(0))){
////            System.out.println("Digit found");
////            if (!isPossible(r,c,Integer.parseInt(board[r][c]),usableStars,starsUsed)){
////                System.out.println("Issue found with stars, going back...");
////                //resetPriority();
////                return;
////            }
////            else{
////                usableStars-=starsUsed[0];
////                procede(r,c,usableStars);
////            }
////        }
////        else{
////            if(usableStars>=1 && !board[r][c].equals("-")) {
////                board[r][c] = "*";
////                System.out.println("Star placed at " + r + " " + c);
////                procede(r, c, usableStars - 1);
////            }
////            board[r][c]=".";
////            System.out.println("Dot placed at "+r+" "+c);
////            procede(r,c,usableStars);
////        }
////    }
//}


import java.util.*;

//Bryson Paul   1.20.2022   Professor Arup Guha COP3503 Tentaizu Solution
public class tentaizu2 {
    //KEY:
    // "-" : NO STARS CAN GO HERE EVER
    // "?" : ON THIS RUN NO STARS CAN GO HERE, BUT IS SUBJECT TO CHANGE
    // "%" : A STAR IS FORCED TO BE HERE
    // "." : STAR CAN BE HERE BUT ALSO CANNOT, DEPENDS ON RUN
    // "(number)": DESIGNATES HOW MANY STARS ARE IN THE 3x3 AROUND IT
   static String[][] board;
   static boolean isComplete;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numOfBoards = sc.nextInt();
        for (int x = 0; x < numOfBoards; x++) {
            board = new String[7][7];
            readInput(sc);
            zeroClear();
            go(0, 0, 10);

            //   printBoard(x+1);
            numOfBoards--;
        }
    }
    public static void go(int r,int c,int numOfStars){
        if(isComplete){
            return;
        }
        //THIS IS A DIGIT WE CARE ABOUT
        if(Character.isDigit(board[r][c].charAt(0))){
            //if it is not possible that this number will work, we go back
            if(!isPossible(r,c,Integer.parseInt(board[r][c]),numOfStars,true)){
                System.out.println("not possible, leaving at"+ r+ " "+c);
                return;//we need to go back, something is not working
            }
            else if(numContainsExactStars(r,c,Integer.parseInt(board[r][c]))){
                System.out.println("num, contains exact stars at"+ r+ " "+c);
                placeCharOnBoard(r,c,".","?");
                printBoard(1337);

                procede(r,c,numOfStars);

                placeCharOnBoard(r,c,"?",".");
                printBoard(64);
                //procede(r,c,numOfStars);//idk if I should re procede here
                return;
            }
            else if(starsMustGoHere(r,c,Integer.parseInt(board[r][c]))){
                System.out.println("stars must go here at"+ r+ " "+c);
                placeCharOnFutureSpots(r,c,".","%");
                //printBoard(1337);
                procede(r,c,numOfStars);
                placeCharOnFutureSpots(r,c,"%",".");
                //printBoard(64);
                //procede(r,c,numOfStars);
                return;
            }
            else{
                //IF IT HITS THIS CASE IT IS POSSIBLE FOR THIS TO WORK, DOES NOT HAVE EXACT AMOUNT OF STARS,
                //AND THE STARS CANNOT GO EXACTLY IN ONE WAY.
                placeStarHolderAtFutureLocation(r,c,Integer.parseInt(board[r][c]),numOfStars);
            }
        }

        else if(board[r][c].equals("%")){
            //System.out.println("at percent @ "+r+ " "+c );
            if(!(numOfStars>=1)){
                //System.out.println("percent failed");
                return;
            }
            else {
              //  System.out.println("percent ran");
                board[r][c] = "*";
                procede(r, c, numOfStars - 1);
                board[r][c] = "%";
                return;
            }
        }
        else if (board[r][c].equals("-")||board[r][c].equals("?")) {
            System.out.println("at ? @ "+r+ " "+c );
            procede(r, c, numOfStars);
            return;
        }
        else{
            if(numOfStars>=1) {
                board[r][c] = "*";
                System.out.println("Star placed at " + r + " " + c);
                procede(r, c, numOfStars - 1);
            }
            board[r][c]=".";
            System.out.println("Dot placed at "+r+" "+c);
            procede(r,c,numOfStars);
        }
    }

    public static void placeStarHolderAtFutureLocation(int r, int c, int maxStars,int usableStars){
        //if working properly, open spots should always be greater in size than starsLeft;
        int starsLeft = maxStars;
        int numPoints=0;
        HashMap<Integer,Point> openSpots = new HashMap<>();
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(y>6 || y<0){
                    continue;
                }
                if((r+1==x )|| (r==x && c+1==y)){
                    if(board[x][y].equals(".")){
                        openSpots.put(numPoints, new Point(x,y));
                        numPoints++;
                    }
                }
                if(board[x][y].equals("*")){
                    starsLeft--;
                }
            }
        }
        System.out.println("stars left: "+ starsLeft+ " open spots: "+openSpots.size());
        checkEachPos(r,c,starsLeft,openSpots,0);

        for(int x=0;x<openSpots.size();x++){
            Point p=openSpots.get(x);
            board[p.x][p.y]=".";
        }

    }
    public static void checkEachPos(int r, int c,int starsLeft,HashMap<Integer,Point> openSpots,int k){
        if(k==openSpots.size()){
            procede(r,c,starsLeft);
            return;
        }
        Point p=openSpots.get(k);
        if(starsLeft!=0){
            board[p.x][p.y]="%";
            checkEachPos(r,c,starsLeft-1,openSpots,k+1);
            board[p.x][p.y]="?";
            checkEachPos(r,c,starsLeft-1,openSpots,k+1);
        }
        else{
            board[p.x][p.y]="?";
            checkEachPos(r,c,starsLeft-1,openSpots,k+1);
        }
    }
    public static boolean starsMustGoHere(int r, int c, int maxStars){
        int starsLeft = maxStars;
        int openSpotsForFuture=0;
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(y>6 || y<0){
                    continue;
                }
                if((r+1==x )|| (r==x && c+1==y)){
                    if(board[x][y].equals(".")){
                        openSpotsForFuture++;
                    }
                }
                if(board[x][y].equals("*")){
                    starsLeft--;
                }
            }
        }
        if(starsLeft==openSpotsForFuture){
            return true;
        }
        else return false;

    }
    //RETURNS TRUE IF THE NUMBER ALREADY CONTAINS ALL OF THE STARS IT NEEDS, ALLOWING "?" TO BE IN ALL ADJACENT SPOTS
    public static boolean numContainsExactStars(int r, int c, int maxStars){
        int starsLeft = maxStars;
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(y>6 || y<0){
                    continue;
                }
                else if(board[x][y].equals("*")){
                    starsLeft--;
                }
            }
        }
        if(starsLeft==0){
            return true;
        }
        else return false;
    }
    //ISPOSSIBLE IS GOING TO LET ME KNOW IF IT IS POSSIBLE FOR A NUMBER (OR ANY ADJACENT NUMBER) ARE POSSIBLE AT WORKING.
    public static boolean isPossible(int r, int c, int maxStars, int usableStars,boolean checkAdjacent){
        int starsLeft = maxStars;
        int openSpotsForFuture=0;
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(y>6 || y<0){
                    continue;
                }
                if(checkAdjacent && (r+1==x )|| (r==x && c+1==y)){
                    if(board[x][y].equals(".")){
                        openSpotsForFuture++;
                    }
                }
                if(checkAdjacent && Character.isDigit(board[x][y].charAt(0)) && x!=r && y!=c){
                    if(!isPossible(x,y,Integer.parseInt(board[x][y]),usableStars,false)){
                        return false;
                    }
                }
                else if(board[x][y].equals("*")){
                    starsLeft--;
                }
            }
        }
        if(starsLeft>usableStars || starsLeft<0){ //too many case
            return false;
        }
        else if(checkAdjacent && starsLeft>openSpotsForFuture){ //not enough space for future case. Only works on first num checked
            return false;
        }
        else if(usableStars==0 && starsLeft!=0){ //too little case
            return false;
        }
        else return true;

    }
    public static void zeroClear(){
        for(int r=0;r<7;r++){
            for(int c=0;c<7;c++) {
                if(board[r][c].equals("0")){
                    placeCharOnBoard(r,c,".","-");//indicates NOTHING can go in these spots
                }
            }
        }
    }
    //places a character around the row and column given
    public static void placeCharOnBoard(int r, int c,String lookFor, String replace){
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(y>6 || y<0){
                    continue;
                }
                if(board[x][y].equals(lookFor)){
                    board[x][y]=replace;
                }
            }
        }
    }
    public static void placeCharOnFutureSpots(int r,int c, String lookFor, String replace){
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if (y > 6 || y < 0) {
                    continue;
                }
                else if ((r + 1 == x) || (r == x && c + 1 == y)) {
                    if (board[x][y].equals(lookFor)) {
                        board[x][y]=replace;
                    }
                }
            }
        }
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
    public static void procede(int r,int c, int usableStars){
        if(c>=6 && r>=6){
            System.out.println("complete");
            isComplete=true;
            printBoard(1);
        }
        else if(c>=6){
            go(r+1,0,usableStars);
        }
        else{
            go(r,c+1,usableStars);
        }
    }
    public static void printBoard(int numberBoard){
       //resetPriority();
        System.out.println("Tentaizu Board #"+numberBoard+":");
        for(int r=0;r<7;r++){
            for(int c=0;c<7;c++) {
                if(board[r][c].equals("?")){
               //     board[r][c]=".";
                }
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
    }

}
class Point{
    int x;
    int y;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }
}

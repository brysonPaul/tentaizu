
import java.util.*;

//Bryson Paul   1.20.2022   Professor Arup Guha COP3503 Tentaizu Solution
public class tentaizu2 {
    //PROBLEMS: ? FOR STARS BEING BAD, IF THERE IS ONLY ONE CASE WHERE THE STARS CAN BE, PUT THOSE SPOTS IN A HASHMAP AND TURN THEM OFF. IF BAD, TURN THEM INTO .'s AGAIN
    //KEY:
    // "-" : NO STARS CAN GO HERE EVER
    // "?" : ON THIS RUN NO STARS CAN GO HERE, BUT IS SUBJECT TO CHANGE
    // "%" : A STAR IS FORCED TO BE HERE
    // "." : STAR CAN BE HERE BUT ALSO CANNOT, DEPENDS ON RUN
    // "(number)": DESIGNATES HOW MANY STARS ARE IN THE 3x3 AROUND IT
    static String[][] board;
    static boolean isComplete;
    static int boardNum;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numOfBoards = sc.nextInt();
        for (int x = 0; x < numOfBoards; x++) {
            board = new String[7][7];
            boardNum=x+1;
            readInput(sc);
            zeroClear();
            go(0, 0, 10);
            printBoard(x);
            isComplete=false;
           // sc.next();//runs that space line
        }
    }
    public static boolean inBounds(int x){
        if(x>6 || x<0){
            return false;
        }
        else return true;
    }
    public static boolean go(int r,int c,int numOfStars){

            System.out.println("row: "+r + " col: "+c);
           // printBoard(87989);

            int checkR= r;
            if(c==0 || c==1){
                checkR--;
            }
            //this is the case where if the character at r-1 and c-2 is a number (and thus is finalized with how my code is set up)
            if(inBounds(checkR-1) && Character.isDigit(board[checkR-1][(c+5)%7].charAt(0))){
                if(!numContainsExactStars(checkR-1,(c+5)%7,Integer.parseInt(board[checkR-1][(c+5)%7]))){
                    return false;
               }
           }
            if(Character.isDigit(board[r][c].charAt(0))){
                //we can check r-1 and c-1 here because we know the number here cant mess up the
                // board by adding a star or a dot
                if(inBounds(r-1) && inBounds(c-1) && Character.isDigit(board[r-1][c-1].charAt(0))){
                    if(!numContainsExactStars(r-1,c-1,Integer.parseInt(board[r-1][c-1]))){
                        return false;
                    }
                }
                if(!isPossible(r,c,Integer.parseInt(board[r][c]),numOfStars,false)){
                    return false;
                }
                else if(numContainsExactStars(r,c,Integer.parseInt(board[r][c]))){
                    System.out.println("num, contains exact stars at"+ r+ " "+c);
                    HashMap<Integer,Point> h =placeCharOnFutureSpots(r,c,".","?");

                   boolean hold = procede(r,c,numOfStars);
                    if(hold){
                        return true;
                    }
                    replaceFromHashMap(h,".");
                    return false;
                }
                if(starsMustGoHere(r,c,Integer.parseInt(board[r][c]))){
                    System.out.println("STARS MUST GO HERE AT "+r+" "+c);
                    HashMap<Integer, Point > hashMap = placeCharOnFutureSpots(r,c,".","%");
                    if(numOfStars-hashMap.size() < 0){
                        replaceFromHashMap(hashMap,".");
                        return false;
                    }
                    else{
                       boolean hold = procede(r,c,numOfStars- hashMap.size());
                       if(hold){
                           return true;
                       }
                        replaceFromHashMap(hashMap,".");
                        return false;
                    }

                }
                else{
                    boolean hold = procede(r,c,numOfStars);
                    if(hold){
                        return true;
                    }
                    return false;

                }

            }
            else if(board[r][c].equals("%")){
                System.out.println("at percent @ "+r+ " "+c );

                    System.out.println("percent ran");
                    board[r][c] = "*";
                    boolean hold =procede(r, c, numOfStars);
                    if(hold){
                        return true;
                    }
                    board[r][c] = "%";
                    return false;

            }
            //if the board is a "-" or "?", we know in this run that those places are reserved already
            else if (board[r][c].equals("-")||board[r][c].equals("?")) {
                System.out.println("at ? @ "+r+ " "+c );
                boolean hold = procede(r, c, numOfStars);
                if(hold){
                    return true;
                }
                return false;
            }
            else{
                if(numOfStars>=1) {
                    board[r][c] = "*";
                    System.out.println("Star placed at " + r + " " + c);

                    boolean hold = procede(r, c, numOfStars-1);
                    if(hold){
                        return true;
                    }
                }
                board[r][c]=".";
                 System.out.println("Dot placed at "+r+" "+c);
                boolean hold = procede(r, c, numOfStars);
                if(hold){
                    return true;
                }
                return false;
            }
        }


//    public static boolean placeStarHolderAtFutureLocation(int r, int c, int maxStars,int usableStars){
//        //if working properly, open spots should always be greater in size than starsLeft;
//        int starsLeft = maxStars;
//        int numPoints=0;
//        HashMap<Integer,Point> openSpots = new HashMap<>();
//        for(int x=r-1;x<=r+1;x++){
//            if(x>6 || x<0){
//                continue;
//            }
//            for(int y=c-1;y<=c+1;y++) {
//                if(y>6 || y<0){
//                    continue;
//                }
//                if((r+1==x )|| (r==x && c+1==y)){
//                    if(board[x][y].equals(".")){
//                        openSpots.put(numPoints, new Point(x,y));//places all places with a possible open spot in the hashmap
//                        numPoints++;
//                    }
//                }
//                if(board[x][y].equals("*")||board[x][y].equals("%")){
//                    starsLeft--;
//                }
//            }
//        }
//        // System.out.println("stars left: "+ starsLeft+ " open spots: "+openSpots.size());
//        boolean hold = checkEachPos(r,c,starsLeft,openSpots,0, openSpots.size()-starsLeft,usableStars);
//        if(hold){
//            return true;
//        }
//        //if all go through and an error occurs, we leave and clean up
//        for(int x=0;x<openSpots.size();x++){
//            Point p=openSpots.get(x);
//            board[p.x][p.y]=".";
//        }
//        return false;
//
//    }
//    //places board places with either a star place holder or a no go
//    public static boolean checkEachPos(int r, int c,int starsLeft,HashMap<Integer,Point> openSpots ,int k,int placesForQuestion,int usableStars){
//        if(k==openSpots.size()){
//            //printBoard(767);
//            boolean hold = procede(r,c,usableStars);
//            if(hold){
//                return true;
//            }
//            return false;
//        }
//        Point p = openSpots.get(k);
//        if(starsLeft!=0){
//            board[p.x][p.y]="%";
//            boolean hold = checkEachPos(r,c,starsLeft-1,openSpots,k+1,placesForQuestion,usableStars);
//            if(hold){
//                return true;
//            }
//
//        }
//        if(placesForQuestion!=0){
//            board[p.x][p.y]="?";
//            boolean hold = checkEachPos(r,c,starsLeft,openSpots,k+1,placesForQuestion-1,usableStars);
//            if(hold){
//                return true;
//            }
//        }
//        return false;
//    }
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
                    else if(board[x][y].equals("*")||board[x][y].equals("%")){
                        starsLeft--;
                    }
                }
                else if(board[x][y].equals("*")||board[x][y].equals("%")){
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
            if(!inBounds(x)){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(!inBounds(y)){
                    continue;
                }
                else if(board[x][y].equals("*")||board[x][y].equals("%")){
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
                else if(board[x][y].equals("*")||board[x][y].equals("%")){
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
                    placeTickOnBoard(r,c);//indicates NOTHING can go in these spots
                }
            }
        }
    }
    //places a character around the row and column given
    public static void placeTickOnBoard(int r, int c){
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(y>6 || y<0){
                    continue;
                }
                if(board[x][y].equals(".")){
                    board[x][y]="-";
                }
            }
        }
    }
    //places chars on the board, and if found out later that it does not work will revert itself
    public static HashMap<Integer,Point> placeCharOnBoard(int r, int c, String lookFor, String replace){
        HashMap<Integer,Point> replacedCoords = new HashMap<>();
        int count =0;
        for(int x=r-1;x<=r+1;x++){
            if(x>6 || x<0){
                continue;
            }
            for(int y=c-1;y<=c+1;y++) {
                if(y>6 || y<0){
                    continue;
                }
                if(board[x][y].equals(lookFor)){
                    replacedCoords.put(count, new Point(x,y));
                    board[x][y]=replace;
                    count++;
                }
            }
        }
        return replacedCoords;
    }
    public static void replaceFromHashMap(HashMap<Integer,Point> hashMap,String replace){
        for(int i=0;i<hashMap.size();i++){
            Point p = hashMap.get(i);
            board[p.x][p.y]=replace;
        }
    }
    public static HashMap<Integer,Point> placeCharOnFutureSpots(int r,int c, String lookFor, String replace){
        HashMap<Integer,Point> replacedCoords = new HashMap<>();
        int count =0;
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
                        replacedCoords.put(count, new Point(x,y));
                        board[x][y]=replace;
                        count++;
                    }
                }
            }
        }
        return replacedCoords;
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

    public static boolean isBoardComplete(int usableStars){
        if(usableStars !=0){
            return false;//need to use all of the stars
        }
        //there are three unchecked squares after 6 6 is reached, and they are at 6 5 , 5 6, and 5 5.
        // we need to check if there are numbers which depend on 6 6 being a star or not
        if(Character.isDigit(board[6][5].charAt(0)) && !numContainsExactStars(6,5,Integer.parseInt(board[6][5]))){
            return false;
        }
        if(Character.isDigit(board[5][6].charAt(0)) && !numContainsExactStars(5,6,Integer.parseInt(board[5][6]))){
            return false;
        }
        if(Character.isDigit(board[5][5].charAt(0)) && !numContainsExactStars(5,5,Integer.parseInt(board[5][5]))){
            return false;
        }
        return true;
    }
    public static boolean procede(int r,int c, int usableStars){
        if(c>=6 && r>=6){
            if(isBoardComplete(usableStars)){
                return true;
            }
            else return false;
        }
        else if(c>=6){
            return go(r+1,0,usableStars);
        }
        else{
            return go(r,c+1,usableStars);
        }
    }
    public static void printBoard(int numberBoard){
        //resetPriority();
        System.out.println("Tentaizu Board #"+numberBoard+":");
        for(int r=0;r<7;r++){
            for(int c=0;c<7;c++) {
               // if(board[r][c].equals("?")||board[r][c].equals("-")){
                 //   board[r][c]=".";
                //}
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
        System.out.println();
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


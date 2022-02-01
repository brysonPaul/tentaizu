import java.util.*;

//Bryson Paul   1.20.2022   Professor Arup Guha     COP3503 Tentaizu Solution
public class tentaizu {
    /*KEY:
     "-" : No stars can go here ever (only zero uses it)
     "?" : On this run no stars are able to go here, but is subject to change
     "%" : Star is forced to be here, and number of stars already account for it
     "." : Star could go here, depends on run. Will branch
     "(number)": Designates how many stars in the 3x3 can go around it */
    static char[][] board;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numOfBoards = sc.nextInt();
        for (int x = 0; x < numOfBoards; x++) {
            board = new char[7][7];
            readInput(sc);
            zeroClear();
            go(0, 0, 10);
            printBoard(x + 1);

        }
    }

    //checks to see if a number is in the bounds of the 7x7
    public static boolean inBounds(int number) {
        return number <= 6 && number >= 0;
    }

    //checks the past numbers which are not finalized to see if they have exact stars around a number.
    // If not, they are wrong and must go back
    public static boolean checkPast(int r, int c) {
        int checkR = r;
        //how this works is at 0 and 1, it would be out of bounds to do a c-2. to combat this, we use (c+5)%7
        // to wrap around, but because it would be on the wrong row of checking, we subtract one so it would be two rows up
        if (c == 0 || c == 1) {
            checkR--;
        }
        //this is the case where if the character at r-1 (or r-2) and (c+5)%7 is a number (and thus is finalized with how my code is set up)
        if (inBounds(checkR - 1) && Character.isDigit(board[checkR - 1][(c + 5) % 7])) {
            if (!numContainsExactStars(checkR - 1, (c + 5) % 7, Character.getNumericValue(board[checkR - 1][(c + 5) % 7]))) {
                return false;
            }
        }
        return true;
    }

    /*basic go method for backtracking. Holds the row, column, and number of stars and checks each time
     if it can work properly. if it does, goes to proceed method where we keep going until 6,6 and break
     if board is solved */
    public static boolean go(int r, int c, int numOfStars) {
        if (!checkPast(r, c)) {
            return false;
        }
        if (Character.isDigit(board[r][c])) {
            //we can check r-1 and c-1 here because we know the number here cant mess up the
            // board by adding a star or a dot. would technically check later in program, but if it is wrong we save a case
            if (inBounds(r - 1) && inBounds(c - 1) && Character.isDigit(board[r - 1][c - 1])) {
                if (!numContainsExactStars(r - 1, c - 1, Character.getNumericValue(board[r - 1][c - 1]))) {
                    return false;
                }
            }
            //if this is not possible, we can leave now. Will also check adjacent numbers in 3x3 if any
            if (!isPossible(r, c, Character.getNumericValue(board[r][c]), numOfStars, true)) {
                return false;
            } else if (numContainsExactStars(r, c, Character.getNumericValue(board[r][c]))) {
                //at this case we know the number is possible, so we can check for exact stars or if it is already solved
                HashMap<Integer, Point> h = placeCharOnFutureSpots(r, c, '.', '?');//only need to put on future cases because go runs row by row

                boolean hold = proceed(r, c, numOfStars);
                if (hold) {
                    return true;
                }
                //if it doesnt work, we need to undo what we did and go back
                replaceFromHashMap(h, '.');
                return false;
            }
            if (starsMustGoHere(r, c, Character.getNumericValue(board[r][c]))) {
                //if inside, the star has spots where it needs to go
                HashMap<Integer, Point> hashMap = placeCharOnFutureSpots(r, c, '.', '%');
                if (numOfStars - hashMap.size() < 0) { //not enough stars currently for this to work
                    replaceFromHashMap(hashMap, '.');
                    return false;
                } else {
                    boolean hold = proceed(r, c, numOfStars - hashMap.size());
                    if (hold) {
                        return true;
                    }
                    //if it doesnt work, we need to undo what we did and go back
                    replaceFromHashMap(hashMap, '.');
                    return false;
                }

            } else {
                return proceed(r, c, numOfStars);
            }

        } else if (board[r][c] == '%') { //if the board equals a "%", we just need to place a star and keep going
            board[r][c] = '*';
            boolean hold = proceed(r, c, numOfStars);
            if (hold) {
                return true;
            }
            //if it doesnt work in the future, we need to make sure we place a % sign instead because it is possible
            // that it fits there, just we need to place a dot or something in another spot behind it
            board[r][c] = '%';
            return false;

        }
        //if the board is a "-" or "?", we know in this run that those places are reserved already
        else if (board[r][c] == '-' || board[r][c] == '?') {
            return proceed(r, c, numOfStars);
        } else {
            if (numOfStars >= 1) { //checks if we are able to place a star here. If so, we place and continue on.
                board[r][c] = '*';

                boolean hold = proceed(r, c, numOfStars - 1);//keeps recursing. If it returns true, code works
                if (hold) {
                    return true;
                }
            }
            //if not, we need to change the star to a dot and keep going
            board[r][c] = '.';
            return proceed(r, c, numOfStars);
        }
    }

    /*
    starsMustGoHere checks to see if the stars in the future are going to need to be there.
    this occurs when all future spots (enything with just a .) equals the amount of stars needed to fill a square.
    if this does occur, we can fill those spots with a % sign to denote in the future that the stars are forced to be here
     */
    public static boolean starsMustGoHere(int r, int c, int maxStars) {
        int starsLeft = maxStars;
        int openSpotsForFuture = 0;
        for (int x = r - 1; x <= r + 1; x++) {
            if (!inBounds(x)) {
                continue;
            }
            for (int y = c - 1; y <= c + 1; y++) {
                if (!inBounds(y)) {
                    continue;
                }
                //the if statement below checks just for all future spots in the code. If its not in the future cases,
                // we check around it still to see if there are open star spots around it
                if ((r + 1 == x) || (r == x && c + 1 == y)) {
                    if (board[x][y] == '.') {
                        openSpotsForFuture++;
                    } else if (board[x][y] == '*' || board[x][y] == '%') {
                        starsLeft--;
                    }
                } else if (board[x][y] == '*' || board[x][y] == '%') {
                    starsLeft--;
                }
            }
        }
        //will return true if the stars left are exactly equal to the open spots for the future
        return starsLeft == openSpotsForFuture;

    }

    //Will return true if the number at this point contains all the exact stars it needs. If true, we are able to
    // place a "?" around it, denoting we can skip over this spot
    public static boolean numContainsExactStars(int r, int c, int maxStars) {
        int starsLeft = maxStars;
        for (int x = r - 1; x <= r + 1; x++) {
            if (!inBounds(x)) {
                continue;
            }
            for (int y = c - 1; y <= c + 1; y++) {
                if (!inBounds(y)) {
                    continue;
                } else if (board[x][y] == '*' || board[x][y] == '%') {
                    starsLeft--;
                }
            }
        }
        return starsLeft == 0;
    }

    //IsPossible checks to see if the number at the row and column is able to actually work in the future. This method gives the star the
    // benefit of doubt that it will work in the future. but is also able to check adjacent stars to make sure they could work too.
    public static boolean isPossible(int r, int c, int maxStars, int usableStars, boolean checkAdjacent) {
        int starsLeft = maxStars;
        int openSpotsForFuture = 0;
        for (int x = r - 1; x <= r + 1; x++) {
            if (x > 6 || x < 0) {
                continue;
            }
            for (int y = c - 1; y <= c + 1; y++) {
                if (y > 6 || y < 0) {
                    continue;
                }
                if (checkAdjacent && (r + 1 == x) || (r == x && c + 1 == y)) {
                    if (board[x][y] == '.') {
                        openSpotsForFuture++;
                    }
                }
                if (checkAdjacent && Character.isDigit(board[x][y]) && x != r && y != c) {
                    if (!isPossible(x, y, Character.getNumericValue(board[x][y]), usableStars, false)) {
                        return false;
                    }
                } else if (board[x][y] == '*' || board[x][y] == '%') {
                    starsLeft--;
                }
            }
        }
        //too many stars case
        if (starsLeft > usableStars || starsLeft < 0) {
            return false;
        }
        //not enough space for future case. Only works on first num checked due to checkAdjacent being false to prevent infinite calls between two numbers
        else if (checkAdjacent && starsLeft > openSpotsForFuture) {
            return false;
        } else return true;

    }

    //Because we know early on zero cannot have any numbers associated with it, we can simply say that no stars can go in its vicinity early on
    public static void zeroClear() {
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (board[r][c] == '0') {
                    placeTickOnBoard(r, c);//indicates NOTHING can go in these spots
                }
            }
        }
    }

    //places a "-" around the row and column given, only used for zero
    public static void placeTickOnBoard(int r, int c) {
        for (int x = r - 1; x <= r + 1; x++) {
            if (x > 6 || x < 0) {
                continue;
            }
            for (int y = c - 1; y <= c + 1; y++) {
                if (y > 6 || y < 0) {
                    continue;
                }
                if (board[x][y] == '.') {
                    board[x][y] = '-';
                }
            }
        }
    }

    /* takes an r and c value and checks all future spots for a value where it might be able to go. If we find a value
     which contains the lookFor value, we replace it and send the coordinates changed to a hashMap for later if we realize
     this case cannot work */
    public static HashMap<Integer, Point> placeCharOnFutureSpots(int r, int c, char lookFor, char replace) {
        HashMap<Integer, Point> replacedCoords = new HashMap<>();
        int count = 0;
        for (int x = r - 1; x <= r + 1; x++) {
            if (!inBounds(x)) {
                continue;
            }
            for (int y = c - 1; y <= c + 1; y++) {
                if (inBounds(y) && ((r + 1 == x) || (r == x && c + 1 == y))) {
                    if (board[x][y] == lookFor) {
                        replacedCoords.put(count, new Point(x, y));
                        board[x][y] = replace;
                        count++;
                    }
                }
            }
        }
        return replacedCoords;
    }

    //helper method for the case where we need to replace points from placeCharOnFutureSpots.
    public static void replaceFromHashMap(HashMap<Integer, Point> hashMap, char replace) {
        for (int i = 0; i < hashMap.size(); i++) {
            Point p = hashMap.get(i);
            board[p.x][p.y] = replace;
        }
    }

    //reads in the input for tentaizu board
    public static void readInput(Scanner sc) {
        for (int r = 0; r < 7; r++) {
            char[] rowOfInput = sc.next().toCharArray();
            //initializes each row for the board
            for (int c = 0; c < 7; c++) {
                board[r][c] = rowOfInput[c];
            }
        }
    }

    //checks to see if exactly everything in the last corner is valid before calling board complete
    public static boolean isBoardComplete(int usableStars) {
        if (usableStars != 0) {
            return false;//need to use all of the stars
        }
        //there are three unchecked squares after 6 6 is reached, and they are at 6 5 , 5 6, and 5 5.
        // we need to check if there are numbers which depend on 6 6 being a star or not
        if (Character.isDigit(board[6][5]) && !numContainsExactStars(6, 5, Character.getNumericValue(board[6][5]))) {
            return false;
        }
        if (Character.isDigit(board[5][6]) && !numContainsExactStars(5, 6, Character.getNumericValue(board[5][6]))) {
            return false;
        }
        if (Character.isDigit(board[5][5]) && !numContainsExactStars(5, 5, Character.getNumericValue(board[5][5]))) {
            return false;
        }
        //This is for a single case where the one at 6,6 is not able to become a star, and needs to be
        if (board[6][6] == '%') {
            board[6][6] = '*';
        }
        return true;
    }

    //helper method for the go function. Checks to see if everything seen is valid
    public static boolean proceed(int r, int c, int usableStars) {
        if (c >= 6 && r >= 6) {
            return isBoardComplete(usableStars);
        } else if (c >= 6) {
            return go(r + 1, 0, usableStars);
        } else {
            return go(r, c + 1, usableStars);
        }
    }

    //prints out the board, and takes in the number board for formatting purposes
    public static void printBoard(int numberBoard) {
        System.out.println("Tentaizu Board #" + numberBoard + ":");
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (board[r][c] == '?' || board[r][c] == '-') {
                    board[r][c] = '.';
                }

                System.out.print(board[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }

}

//point class to make HashMap input much cleaner
class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
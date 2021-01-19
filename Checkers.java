// Arpon Purkayastha
// P2
// Checkers
// 12/28

import java.util.*;

public class Checkers {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        pieces[][] board = new pieces[8][8];
        List<pieces> blacks = new ArrayList<pieces>();
        List<pieces> whites = new ArrayList<pieces>();

        makeBoard(board, blacks, whites);

        String move = "black";
        boolean isWinner = false;
        boolean killAgain;

        System.out.println(
                "RULES:\nKING PIECES ARE CAPITALIZED, REGULAR PIECES ARE LOWERCASE\nMULTIPLE KILLS ARE FORCED IF POSSIBLE");

        while (!isWinner) {

            if (move.equals("black")) {
                boolean canMove = false;
                for (pieces p : blacks) {
                    if (canMove(board, p)) {
                        canMove = true;
                    }
                }
                if (!canMove) {
                    System.out.println("No moves for black. White wins!");
                    break;
                }
            } else {
                boolean canMove = false;
                for (pieces p : whites) {
                    if (canMove(board, p)) {
                        canMove = true;
                    }
                }
                if (!canMove) {
                    System.out.println("No moves for black. White wins!");
                    break;
                }
            }

            System.out.println(move + "'s move \nPick your piece");

            // moving

            boolean kill;
            int row, col;
            pieces killer = null;

            while (true) {

                printBoard(board);

                if (killer != null) {
                    System.out.println(move + "'s next kill");
                    System.out.println("Row: " + (killer.getY() + 1) + ", Col: " + (killer.getX() + 1));
                    System.out.print("What row will you move to: ");
                    int moveRow = input.nextInt() - 1;
                    input.nextLine();
                    System.out.print("What col will you move to: ");
                    int moveCol = input.nextInt() - 1;
                    input.nextLine();
                    if (!canMoveOrKill(killer, board, moveRow, moveCol, true)) {
                        System.out.println("Move not possible. Redo");
                        continue;
                    }

                    row = killer.getY();
                    col = killer.getX();

                    // moving to new position
                    board[row][col].setX(moveCol);
                    board[row][col].setY(moveRow);
                    board[moveRow][moveCol] = board[row][col];
                    board[row][col] = null;

                    // setting king
                    if (board[moveRow][moveCol].getY() == 0 && board[moveRow][moveCol].getColor().equals("black"))
                        board[moveRow][moveCol].makeKing();
                    else if (board[moveRow][moveCol].getY() == 7 && board[moveRow][moveCol].getColor().equals("white"))
                        board[moveRow][moveCol].makeKing();

                    // removing piece from List
                    if (move.equals("black"))
                        whites.remove(board[(row + moveRow) / 2][(col + moveCol) / 2]);
                    else
                        blacks.remove(board[(row + moveRow) / 2][(col + moveCol) / 2]);
                    // remove from board
                    board[(row + moveRow) / 2][(col + moveCol) / 2] = null;
                    // check if another kill can be done
                    if (canKillAgain(board, board[moveRow][moveCol])) {
                        printBoard(board);
                        System.out.println(
                                "Another kill is possible\nWould you like to kill again:\nYes (true)\nNo (false)");
                        killAgain = input.nextBoolean();
                        input.nextLine();

                        if (killAgain) {
                            killer = board[moveRow][moveCol];
                            continue;
                        } else {
                            killer = null;
                        }
                    }
                    break;
                } else {
                    System.out.print("Pick a row ");
                    row = input.nextInt() - 1;
                    input.nextLine();
                    System.out.print("Pick a column ");
                    col = input.nextInt() - 1;
                    input.nextLine();

                    // checking cell in bounds or if exists

                    if (row < 0 || row > 7 || col < 0 || col > 7 || board[row][col] == null
                            || !board[row][col].getColor().equals(move)) {
                        System.out.println("Invalid cell. Pick again");
                        continue;
                    }

                    // checking move destination
                    System.out.print("What row would you like to move to: ");
                    int moveRow = input.nextInt() - 1;
                    input.nextLine();
                    System.out.print("What col would you like to move to: ");
                    int moveCol = input.nextInt() - 1;
                    input.nextLine();

                    if (Math.abs(moveRow - row) == 2 && Math.abs(moveCol - col) == 2)
                        kill = true;
                    else
                        kill = false;

                    // checking move

                    if (!canMoveOrKill(board[row][col], board, moveRow, moveCol, kill)) {
                        System.out.println("Move not possible. Redo");
                        System.out.println(move + "'s move \nPick your piece");
                        continue;
                    }

                    // moving
                    board[row][col].setX(moveCol);
                    board[row][col].setY(moveRow);
                    board[moveRow][moveCol] = board[row][col];
                    board[row][col] = null;

                    // setting king
                    if (board[moveRow][moveCol].getY() == 0 && board[moveRow][moveCol].getColor().equals("black"))
                        board[moveRow][moveCol].makeKing();
                    else if (board[moveRow][moveCol].getY() == 7 && board[moveRow][moveCol].getColor().equals("white"))
                        board[moveRow][moveCol].makeKing();

                    if (kill) {
                        // removing piece from List
                        if (move.equals("black"))
                            whites.remove(board[(row + moveRow) / 2][(col + moveCol) / 2]);
                        else
                            blacks.remove(board[(row + moveRow) / 2][(col + moveCol) / 2]);
                        // remove from board
                        board[(row + moveRow) / 2][(col + moveCol) / 2] = null;

                        // check if another kill can be done

                        if (canKillAgain(board, board[moveRow][moveCol])) {
                            printBoard(board);
                            System.out.println(
                                    "Another kill is possible\nWould you like to kill again:\nYes (true)\nNo (false)");
                            killAgain = input.nextBoolean();
                            input.nextLine();

                            if (killAgain) {
                                killer = board[moveRow][moveCol];
                                continue;
                            } else {
                                killer = null;
                            }
                        }
                        break;
                    } else {
                        break;
                    }
                }

            }

            // checking winner

            if (blacks.size() == 0) {
                System.out.println("White wins!");
                printBoard(board);
                break;
            } else if (whites.size() == 0) {
                System.out.println("Black wins!");
                printBoard(board);
                break;
            }

            if (move.equals("black")) {
                move = "white";
            } else {
                move = "black";
            }

        }

    }

    public static void makeBoard(pieces[][] board, List<pieces> blacks, List<pieces> whites) {

        for (int i = 0; i < board.length; i++) {
            for (int n = 0; n < board[i].length; n++) {

                int row = i + 1;
                int col = n + 1;

                if (row >= 1 && row <= 3) {
                    // white
                    if (row == 1 || row == 3) {
                        if (col % 2 == 0) {
                            pieces checker = new pieces(n, i, false, "white");
                            board[i][n] = checker;
                            whites.add(checker);
                        }
                    } else {
                        if (col % 2 == 1) {
                            pieces checker = new pieces(n, i, false, "white");
                            board[i][n] = checker;
                            whites.add(checker);
                        }
                    }

                    // moving
                } else if (row >= 6 && row <= 8) {
                    // black
                    if (row == 7) {
                        if (col % 2 == 0) {
                            pieces checker = new pieces(n, i, false, "black");
                            board[i][n] = checker;
                            blacks.add(checker);
                        }
                    } else {
                        if (col % 2 == 1) {
                            pieces checker = new pieces(n, i, false, "black");
                            board[i][n] = checker;
                            blacks.add(checker);
                        }
                    }

                }

            }
        }
    }

    public static void printBoard(pieces[][] board) {
        for (int i = 0; i < board.length; i++) {
            if (i == 0) {
                System.out.println("  " + 1 + " " + 2 + " " + 3 + " " + 4 + " " + 5 + " " + 6 + " " + 7 + " " + 8);
            }
            System.out.print(i + 1 + " ");
            for (int n = 0; n < board[i].length; n++) {
                if (board[i][n] != null) {
                    System.out.print(board[i][n] + " ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
    }

    public static boolean canMoveOrKill(pieces checker, pieces[][] board, int moveRow, int moveCol, boolean kill) {
        // check if target location is in bounds
        if (moveRow < 0 || moveRow > 7 || moveCol < 0 || moveCol > 7) {
            System.out.println("location isnt in bounds");
            return false;
        }
        // check if destination isnt null
        if (board[moveRow][moveCol] != null) {
            System.out.println("location isnt null");
            return false;
        }
        // check if move distance is invalid
        System.out.println("col distance: " + (checker.getX() - moveCol));
        System.out.println("row distance: " + (checker.getY() - moveRow));

        if (checker.kingStatus()) {
            System.out.println("king conditions");
            int colD = Math.abs(checker.getX() - moveCol);
            int rowD = Math.abs(checker.getY() - moveRow);
            if (kill) {
                if (colD != 2 || rowD != 2) {
                    System.out.println("Invalid distance");
                    return false;
                }
                int midRow = (checker.getY() + moveRow) / 2;
                int midCol = (checker.getX() + moveCol) / 2;
                if (board[midRow][midCol] == null || board[midRow][midCol].getColor().equals(checker.getColor())) {
                    System.out.println("Invalid kill");
                    return false;
                }
            } else {
                if (colD != 1 || rowD != 1)
                    return false;
            }
        } else {
            System.out.println("regular conditions");
            int colD = moveCol - checker.getX();
            int rowD = moveRow - checker.getY();
            if (checker.getColor().equals("white") && rowD < 0) {
                System.out.println("Invalid direction");
                return false;
            }
            if (checker.getColor().equals("black") && rowD > 0) {
                System.out.println("Invalid direction");
                return false;
            }
            colD = Math.abs(colD);
            rowD = Math.abs(rowD);
            if (kill) {
                if (colD != 2 || rowD != 2) {
                    System.out.println("Invalid distance");
                    return false;
                }
                int midRow = (checker.getY() + moveRow) / 2;
                int midCol = (checker.getX() + moveCol) / 2;
                if (board[midRow][midCol] == null || board[midRow][midCol].getColor().equals(checker.getColor())) {
                    System.out.println("Invalid kill");
                    return false;
                }
            } else {
                if (colD != 1 || rowD != 1)
                    return false;
            }
        }

        return true;
    }

    public static boolean canKillAgain(pieces[][] game, pieces checker) {
        boolean canKill = false;
        System.out.println("Checker in question: row " + (checker.getY() + 1) + ", col " + (checker.getX() + 1));
        if (checker.kingStatus()) {
            // check down
            if (checker.getY() + 2 <= 7 && checker.getX() + 2 <= 7) { // bottom right in bounds
                if (game[checker.getY() + 1][checker.getX() + 1] != null
                        && game[checker.getY() + 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom right");
                        canKill = true;
                    }
                }
            }
            if (checker.getY() + 2 <= 7 && checker.getX() - 2 >= 0) { // bottom left in bounds
                if (game[checker.getY() + 1][checker.getX() - 1] != null
                        && game[checker.getY() + 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom left");
                        canKill = true;
                    }
                }
            }
            // check up
            if (checker.getY() - 2 >= 0 && checker.getX() + 2 <= 7) { // top right in bounds
                if (game[checker.getY() - 1][checker.getX() + 1] != null
                        && game[checker.getY() - 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top right");
                        canKill = true;
                    }
                }
            }
            if (checker.getY() - 2 >= 0 && checker.getX() - 2 >= 0) { // top left in bounds
                if (game[checker.getY() - 1][checker.getX() - 1] != null
                        && game[checker.getY() - 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top left");
                        canKill = true;
                    }
                }
            }
        } else if (checker.getColor().equals("white")) {
            // check down
            if (checker.getY() + 2 <= 7 && checker.getX() + 2 <= 7) { // bottom right in bounds
                if (game[checker.getY() + 1][checker.getX() + 1] != null
                        && game[checker.getY() + 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom right");
                        canKill = true;
                    }
                }
            }
            if (checker.getY() + 2 <= 7 && checker.getX() - 2 >= 0) { // bottom left in bounds
                if (game[checker.getY() + 1][checker.getX() - 1] != null
                        && game[checker.getY() + 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom left");
                        canKill = true;
                    }
                }
            }
        } else if (checker.getColor().equals("black")) {
            // check up
            if (checker.getY() - 2 >= 0 && checker.getX() + 2 <= 7) { // top right in bounds
                if (game[checker.getY() - 1][checker.getX() + 1] != null
                        && game[checker.getY() - 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top right");
                        canKill = true;
                    }
                }
            }
            if (checker.getY() - 2 >= 0 && checker.getX() - 2 >= 0) { // top left in bounds
                if (game[checker.getY() - 1][checker.getX() - 1] != null
                        && game[checker.getY() - 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top left");
                        canKill = true;
                    }
                }
            }
        }

        return canKill;
    }

    public static boolean canMove(pieces[][] game, pieces checker) {
        boolean canMove = false;

        if (checker.kingStatus()) {
            // check kill possibilities
            // check down
            if (checker.getY() + 2 <= 7 && checker.getX() + 2 <= 7) { // bottom right in bounds
                if (game[checker.getY() + 1][checker.getX() + 1] != null
                        && game[checker.getY() + 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom right");
                        canMove = true;
                    }
                }
            }
            if (checker.getY() + 2 <= 7 && checker.getX() - 2 >= 0) { // bottom left in bounds
                if (game[checker.getY() + 1][checker.getX() - 1] != null
                        && game[checker.getY() + 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom left");
                        canMove = true;
                    }
                }
            }
            // check up
            if (checker.getY() - 2 >= 0 && checker.getX() + 2 <= 7) { // top right in bounds
                if (game[checker.getY() - 1][checker.getX() + 1] != null
                        && game[checker.getY() - 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top right");
                        canMove = true;
                    }
                }
            }
            if (checker.getY() - 2 >= 0 && checker.getX() - 2 >= 0) { // top left in bounds
                if (game[checker.getY() - 1][checker.getX() - 1] != null
                        && game[checker.getY() - 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top left");
                        canMove = true;
                    }
                }
            }
            // check move possibilities
            // check down
            if (checker.getY() + 1 <= 7 && checker.getX() + 1 <= 7) { // bottom right in bounds
                if (game[checker.getY() + 1][checker.getX() + 1] == null) { // check if there is space
                    canMove = true;
                }
            }
            if (checker.getY() + 1 <= 7 && checker.getX() - 1 >= 0) { // bottom left in bounds
                if (game[checker.getY() + 1][checker.getX() - 1] == null) { // check if there is space
                    canMove = true;
                }
            }
            // check up
            if (checker.getY() - 1 >= 0 && checker.getX() + 1 <= 7) { // top right in bounds
                if (game[checker.getY() - 1][checker.getX() + 1] == null) { // check if there is space
                    canMove = true;
                }
            }
            if (checker.getY() - 1 >= 0 && checker.getX() - 1 >= 0) { // top left in bounds
                if (game[checker.getY() - 1][checker.getX() - 1] == null) { // check if there is space
                    canMove = true;
                }
            }
        } else if (checker.getColor().equals("white")) {
            // check kill possibilities
            // check down
            if (checker.getY() + 2 <= 7 && checker.getX() + 2 <= 7) { // bottom right in bounds
                if (game[checker.getY() + 1][checker.getX() + 1] != null
                        && game[checker.getY() + 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom right");
                        canMove = true;
                    }
                }
            }
            if (checker.getY() + 2 <= 7 && checker.getX() - 2 >= 0) { // bottom left in bounds
                if (game[checker.getY() + 1][checker.getX() - 1] != null
                        && game[checker.getY() + 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() + 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill bottom left");
                        canMove = true;
                    }
                }
            }
            // move
            if (checker.getY() + 1 <= 7 && checker.getX() + 1 <= 7) { // bottom right in bounds
                if (game[checker.getY() + 1][checker.getX() + 1] == null) { // check if there is space
                    canMove = true;
                }
            }
            if (checker.getY() + 1 <= 7 && checker.getX() - 1 >= 0) { // bottom left in bounds
                if (game[checker.getY() + 1][checker.getX() - 1] == null) { // check if there is space
                    canMove = true;
                }
            }
        } else if (checker.getColor().equals("black")) {
            // check kill possibilities
            // check up
            if (checker.getY() - 2 >= 0 && checker.getX() + 2 <= 7) { // top right in bounds
                if (game[checker.getY() - 1][checker.getX() + 1] != null
                        && game[checker.getY() - 2][checker.getX() + 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() + 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top right");
                        canMove = true;
                    }
                }
            }
            if (checker.getY() - 2 >= 0 && checker.getX() - 2 >= 0) { // top left in bounds
                if (game[checker.getY() - 1][checker.getX() - 1] != null
                        && game[checker.getY() - 2][checker.getX() - 2] == null) { // check if there is space
                    if (!game[checker.getY() - 1][checker.getX() - 1].getColor().equals(checker.getColor())) {
                        System.out.println("Can kill top left");
                        canMove = true;
                    }
                }
            }
            // move
            if (checker.getY() - 1 >= 0 && checker.getX() + 1 <= 7) { // top right in bounds
                if (game[checker.getY() - 1][checker.getX() + 1] == null) { // check if there is space
                    canMove = true;
                }
            }
            if (checker.getY() - 1 >= 0 && checker.getX() - 1 >= 0) { // top left in bounds
                if (game[checker.getY() - 1][checker.getX() - 1] == null) { // check if there is space
                    canMove = true;
                }
            }
        }

        return canMove;
    }
}
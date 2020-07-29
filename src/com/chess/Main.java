package com.chess;

import com.chess.engine.board.Board;
import com.chess.engine.bughouse.BugHouseGame;
import com.chess.gui.Table;

import java.util.Scanner;


public class Main {
    public static void main(String args[]) {

        Board first = Board.createStandardBoard();
        Board second = Board.secondBoard();

        System.out.println("Choose time control for game in seconds");
        Scanner sc = new Scanner(System.in);
        int timer = sc.nextInt();

        BugHouseGame newGame = new BugHouseGame(first, second, 10, timer);

    }
}

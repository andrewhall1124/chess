package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class KnightMoves {
    private final ChessPosition myPosition;
    private final ChessBoard board;

    public KnightMoves(ChessPosition myPosition, ChessBoard board) {
        this.myPosition = myPosition;
        this.board = board;
    }

    public HashSet<ChessMove> getMoves() {
        HashSet<ChessMove> moveList = new HashSet<>();

        int[][] offsets = {{-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}};

        for (int[] offset : offsets) {
            int newRow = myPosition.getRow() + offset[0];
            int newCol = myPosition.getColumn() + offset[1];

            ChessUtils.addPossibleMoveIfValid(moveList,board,myPosition,newRow,newCol);
        }

        return moveList;
    }
}

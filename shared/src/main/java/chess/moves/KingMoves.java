package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class KingMoves {
    private final ChessPosition myPosition;
    private final ChessBoard board;

    public KingMoves(ChessPosition myPosition, ChessBoard board) {
        this.myPosition = myPosition;
        this.board = board;
    }

    public HashSet<ChessMove> getMoves() {
        HashSet<ChessMove> moveList = new HashSet<>();

        int[] rowOffsets = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] colOffsets = {1, 0, -1, 1, -1, 1, 0, -1};

        for (int i = 0; i < 8; i++) {
            int newRow = myPosition.getRow() + rowOffsets[i];
            int newCol = myPosition.getColumn() + colOffsets[i];

            ChessUtils.addPossibleMoveIfValid(moveList,board,myPosition,newRow,newCol);
        }
        return moveList;
    }
}
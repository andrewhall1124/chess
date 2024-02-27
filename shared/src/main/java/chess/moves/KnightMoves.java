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

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition currentPosition = new ChessPosition(newRow, newCol);
                if (board.getPiece(currentPosition) == null ||
                        board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
        }

        return moveList;
    }
}

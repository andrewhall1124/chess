package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class ChessUtils {

    public static void addPossibleMoveIfValid(Collection<ChessMove> moveList, ChessBoard board, ChessPosition myPosition, int newRow, int newCol) {
        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition currentPosition = new ChessPosition(newRow, newCol);
            if (board.getPiece(currentPosition) == null ||
                    board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
            }
        }
    }
}


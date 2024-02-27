package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class BishopMoves {
    private final ChessPosition myPosition;
    private final ChessBoard board;

    public BishopMoves(ChessPosition myPosition, ChessBoard board) {
        this.myPosition = myPosition;
        this.board = board;
    }

    public HashSet<ChessMove> getMoves() {
        HashSet<ChessMove> moveList = new HashSet<>();
        moveList.addAll(diagonalMoves(1, 1));
        moveList.addAll(diagonalMoves(1, -1));
        moveList.addAll(diagonalMoves(-1, 1));
        moveList.addAll(diagonalMoves(-1, -1));
        return moveList;
    }

    private HashSet<ChessMove> diagonalMoves(int rowDirection, int colDirection) {
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = myPosition;
        int curRow = currentPosition.getRow() + rowDirection;
        int curCol = currentPosition.getColumn() + colDirection;

        while (isValidPosition(curRow, curCol)) {
            currentPosition = new ChessPosition(curRow, curCol);
            if (isValidMove(currentPosition)) {
                moveList.add(new ChessMove(myPosition, currentPosition, null));
            }
            if (!isEmpty(currentPosition)) {
                break;
            }
            curRow += rowDirection;
            curCol += colDirection;
        }
        return moveList;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private boolean isEmpty(ChessPosition position) {
        return board.getPiece(position) == null;
    }

    private boolean isValidMove(ChessPosition position) {
        return isEmpty(position) || board.getPiece(position).getTeamColor() != board.getPiece(myPosition).getTeamColor();
    }
}

package chess.moves;

import chess.*;

import java.util.HashSet;

public class PawnMoves {
    private final ChessPosition myPosition;
    private final ChessBoard board;
    private static final int BOARD_SIZE = 8;

    public PawnMoves(ChessPosition myPosition, ChessBoard board) {
        this.myPosition = myPosition;
        this.board = board;
    }

    public HashSet<ChessMove> getMoves() {
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPiece currentPiece = board.getPiece(myPosition);
        if (currentPiece == null || currentPiece.getPieceType() != ChessPiece.PieceType.PAWN)
            return moveList;

        int direction = (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (direction == 1) ? 2 : BOARD_SIZE - 1;
        int forwardOne = myPosition.getRow() + direction;
        int forwardTwo = myPosition.getRow() + 2 * direction;

        addForwardMoves(moveList, forwardOne);
        if (myPosition.getRow() == startRow)
            addForwardMoves(moveList, forwardTwo);

        addCaptureMoves(moveList, direction);
        addPromotionMoves(moveList, startRow, forwardOne);

        return moveList;
    }

    private void addForwardMoves(HashSet<ChessMove> moveList, int forwardRow) {
        ChessPosition forwardPosition = new ChessPosition(forwardRow, myPosition.getColumn());
        if (isValidPosition(forwardPosition) && board.getPiece(forwardPosition) == null)
            moveList.add(new ChessMove(myPosition, forwardPosition, null));
    }

    private void addCaptureMoves(HashSet<ChessMove> moveList, int direction) {
        for (int colOffset : new int[]{-1, 1}) {
            int targetRow = myPosition.getRow() + direction;
            int targetCol = myPosition.getColumn() + colOffset;
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            if (isValidPosition(targetPosition)) {
                ChessPiece targetPiece = board.getPiece(targetPosition);
                if (targetPiece != null && targetPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, targetPosition, null));
                }
            }
        }
    }

    private void addPromotionMoves(HashSet<ChessMove> moveList, int startRow, int forwardOne) {
        if (myPosition.getRow() == startRow) {
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));

        }
    }

    private boolean isValidPosition(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row >= 1 && row <= BOARD_SIZE && col >= 1 && col <= BOARD_SIZE;
    }
}

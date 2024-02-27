package chess.moves;

import chess.*;

import java.util.HashSet;

public class PawnMoves {
    private final ChessPosition myPosition;
    private final ChessBoard board;
    private final HashSet<ChessMove> moveList = new HashSet<>();

    public PawnMoves(ChessPosition myPosition, ChessBoard board) {
        this.myPosition = myPosition;
        this.board = board;
    }

    public HashSet<ChessMove> getMoves() {
        ChessPiece currentPiece = board.getPiece(myPosition);
        if (currentPiece == null || currentPiece.getPieceType() != ChessPiece.PieceType.PAWN)
            return moveList;

        int direction = (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (direction == 1) ? 2 : 7;
        int promotionRow = (direction == 1) ? 7 : 2;
        int forwardOne = myPosition.getRow() + direction;
        int forwardTwo = myPosition.getRow() + 2 * direction;

        addForwardMoves(forwardOne);
        if (myPosition.getRow() == startRow){
            addForwardMoves(forwardTwo);
        }

        addCaptureMoves(direction);
        addPromotionMoves(promotionRow, forwardOne);

        return moveList;
    }

    private void addForwardMoves(int forwardRow) {
        ChessPosition forwardPosition = new ChessPosition(forwardRow, myPosition.getColumn());
        if (isValidPosition(forwardPosition) && board.getPiece(forwardPosition) == null)
            moveList.add(new ChessMove(myPosition, forwardPosition, null));
    }

    private void addCaptureMoves(int direction) {
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

    private void addPromotionMoves(int promotionRow, int forwardOne) {
        if (myPosition.getRow() == promotionRow) {
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            moveList.add(new ChessMove(myPosition, new ChessPosition(forwardOne, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
        }
    }

    private boolean isValidPosition(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}

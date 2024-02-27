package chess;

import chess.moves.*;

import java.util.*;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }
    public void setPieceType(PieceType type){
        this.type = type;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return HashSet of valid moves
     */
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition).getPieceType().equals(PieceType.BISHOP)) {
            BishopMoves bishopMoves = new BishopMoves(myPosition,board);
            return bishopMoves.getMoves();
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.KING)) {
            KingMoves kingMoves = new KingMoves(myPosition,board);
            return kingMoves.getMoves();
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.KNIGHT)) {
            KnightMoves knightMoves = new KnightMoves(myPosition, board);
            return knightMoves.getMoves();
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.ROOK)) {
            RookMoves rookMoves = new RookMoves(myPosition, board);
            return rookMoves.getMoves();
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.QUEEN)) {
            QueenMoves queenMoves = new QueenMoves(myPosition, board);
            return queenMoves.getMoves();
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.PAWN)) {
            PawnMoves pawnMoves = new PawnMoves(myPosition, board);
            return pawnMoves.getMoves();
        }
        return new HashSet<>();
    }

    @Override
    public String toString(){
        return this.type.name();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}

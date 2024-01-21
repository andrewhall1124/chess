package chess;

import java.sql.Array;
import java.util.*;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveList = new ArrayList<ChessMove>();
        PieceType currentType = board.getPiece(myPosition).getPieceType();

        //Piece is a BISHOP
        if(currentType.equals(PieceType.BISHOP)){
            //Loop through all squares and find valid moves regardless of other pieces
            for(int curRow = 1; curRow <= 8; curRow++){
                for(int curCol = 1; curCol <=8; curCol++){
                   if(curCol == myPosition.getColumn() && curRow == myPosition.getRow()){
                       continue;
                   }
                   if(((myPosition.getRow() - curRow) == (myPosition.getColumn() - curCol)) || ((myPosition.getRow() -curRow) == -(myPosition.getColumn() - curCol))){
                       ChessPosition currentPosition = new ChessPosition(curRow,curCol);
                       ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                       moveList.add(possibleMove);
                   }
                }
            }
        }
        return moveList;
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

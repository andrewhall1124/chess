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

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

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
            //Up + Right
            ChessPosition currentPosition = new ChessPosition(myPosition.getRow() + 1,myPosition.getColumn() + 1);
            while(board.getPiece(currentPosition) == null){
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition,currentPosition,null);
                moveList.add(possibleMove);
                if(curRow < 8 && curCol < 8){
                    currentPosition = new ChessPosition(curRow + 1, curCol + 1);
                } else{
                    break; //Hit the edge of the board
                }
            }
            //Down + Right
            currentPosition = new ChessPosition(myPosition.getRow() - 1,myPosition.getColumn() + 1);
            while(board.getPiece(currentPosition) == null){
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition,currentPosition,null);
                moveList.add(possibleMove);
                if(curRow > 1 && curCol < 8){
                    currentPosition = new ChessPosition(curRow - 1, curCol + 1);
                } else{
                    break; //Hit the edge of the board
                }
            }
            //Down + Left
            currentPosition = new ChessPosition(myPosition.getRow() - 1,myPosition.getColumn() - 1);
            while(board.getPiece(currentPosition) == null){
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition,currentPosition,null);
                moveList.add(possibleMove);
                if(curRow > 1 && curCol > 1){
                    currentPosition = new ChessPosition(curRow - 1, curCol - 1);
                } else{
                    break; //Hit the edge of the board
                }
            }
            //Up + Left
            currentPosition = new ChessPosition(myPosition.getRow() + 1,myPosition.getColumn() - 1);
            while(board.getPiece(currentPosition) == null){
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition,currentPosition,null);
                moveList.add(possibleMove);
                if(curRow < 8 && curCol > 1){
                    currentPosition = new ChessPosition(curRow + 1, curCol - 1);
                } else{
                    break; //Hit the edge of the board
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

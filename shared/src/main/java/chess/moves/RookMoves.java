package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class RookMoves {
    private final ChessPosition myPosition;
    private final ChessBoard board;

    public RookMoves(ChessPosition myPosition, ChessBoard board) {
        this.myPosition = myPosition;
        this.board = board;
    }
    public HashSet<ChessMove> getMoves(){
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = myPosition;
        //Up
        if(myPosition.getRow() < 8){
            currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            while(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if(currentPosition.getRow() < 8){
                    currentPosition = new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn());
                } else{
                    break;
                }
            }
        }
        //Right
        if(myPosition.getColumn() < 8){
            currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            while(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if(currentPosition.getColumn() < 8){
                    currentPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn() + 1);
                } else{
                    break;
                }
            }
        }
        //Down
        if(myPosition.getRow() > 1){
            currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            while(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if(currentPosition.getRow() > 1){
                    currentPosition = new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn());
                } else{
                    break;
                }
            }
        }
        //Left
        if(myPosition.getColumn() > 1){
            currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() -1);
            while(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if(currentPosition.getColumn() > 1){
                    currentPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn() - 1);
                } else{
                    break;
                }
            }
        }
        return moveList;
    }
}

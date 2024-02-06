package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    private ChessPosition getKingPosition(TeamColor teamColor){
        ChessPosition kingPosition = new ChessPosition(0,0);
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                if(board.getPiece(currentPosition) != null
                        &&board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING
                        && board.getPiece(currentPosition).getTeamColor() == teamColor){
                    return currentPosition;
                }
            }
        }
        return kingPosition;
    }

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public HashSet<ChessMove> validMoves(ChessPosition startPosition) {
        if(board.getPiece(startPosition) == null){
            return null;
        } else{
            ChessPiece currentPiece = board.getPiece(startPosition);
            HashSet<ChessMove> possibleMoves = currentPiece.pieceMoves(board,startPosition);
            HashSet<ChessMove> validatedMoves = new HashSet<>();
            ChessBoard boardCopy = board;
            for(ChessMove move : possibleMoves){
                //Make move
                board.removePiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(),currentPiece);
                if(isInCheck(currentPiece.getTeamColor())){
                    board = boardCopy;
                }
                else{
                    validatedMoves.add(move);
                }
            }
            return validatedMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(teamTurn == board.getPiece(move.getStartPosition()).getTeamColor()){
            ChessPiece currentPiece = board.getPiece(move.getStartPosition());
            HashSet<ChessMove> possibleMoves = currentPiece.pieceMoves(board,move.getStartPosition());
            //Check that move is possible
            if(possibleMoves.contains(move)){
                //Make move
                board.removePiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(),currentPiece);
                //Check if King is in check
                if(isInCheck(this.teamTurn)){
                    board.removePiece(move.getEndPosition());
                    board.addPiece(move.getStartPosition(),currentPiece);
                    throw new InvalidMoveException("Invalid move: puts king in check");
                }
                //Alternate team turn
                if(this.teamTurn == TeamColor.WHITE){
                    this.teamTurn = TeamColor.BLACK;
                }
                else{
                    this.teamTurn = TeamColor.WHITE;
                }
            }
            else{
                throw new InvalidMoveException("Invalid move: end position not allowed");
            }
        }
        else{
            throw new InvalidMoveException("Invalid move: not your turn");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        //Check all possible moves
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                if(board.getPiece(currentPosition) != null
                        && board.getPiece(currentPosition).getTeamColor() != teamColor){
                    HashSet<ChessMove> possibleMoves = board.getPiece(currentPosition).pieceMoves(board,currentPosition);
                    for(ChessMove move : possibleMoves){
                        if(move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //Iterate through whole board
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                if(board.getPiece(currentPosition) != null){
                    ChessPiece currentPiece = board.getPiece(currentPosition);
                    if(currentPiece.getTeamColor() == teamColor){
                        HashSet<ChessMove> validMoves = validMoves(currentPosition);
                        if(!validMoves.isEmpty()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}

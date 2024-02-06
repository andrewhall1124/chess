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
    private TeamColor teamTurn = TeamColor.WHITE;

    private ChessPosition getKingPosition(TeamColor teamColor){
        ChessPosition kingPosition = new ChessPosition(1,1);
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

    private boolean tryMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(start);
        ChessPiece capturedPiece = board.getPiece(end); // Remember the piece at the destination
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();

        // Try move
        if (promotionPiece == null) {
            board.removePiece(start);
            board.addPiece(end, currentPiece);
        } else {
            ChessPiece promotion = new ChessPiece(this.teamTurn, promotionPiece);
            board.removePiece(start);
            board.addPiece(end, promotion);
        }

        // Check if in check after move
        boolean valid = !isInCheck(this.teamTurn);

        // Revert move
        board.removePiece(end);
        if (capturedPiece != null) {
            board.addPiece(end, capturedPiece);
        } else {
            board.removePiece(end);
        }
        board.addPiece(start, currentPiece);

        return valid;
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
        System.out.println("Ran validMoves");
        HashSet<ChessMove> validatedMoves = new HashSet<>();
        //Check for valid start position and team turn
        System.out.println("Cur color: " + this.teamTurn);
        System.out.println("Piece color: " + board.getPiece(startPosition).getTeamColor());
        if(board.getPiece(startPosition) != null
                && board.getPiece(startPosition).getTeamColor() == this.teamTurn){
            ChessPiece currentPiece = board.getPiece(startPosition);
            HashSet<ChessMove> possibleMoves = currentPiece.pieceMoves(board,startPosition);
            for(ChessMove move : possibleMoves){
                System.out.println("Trying move: " + move);
                System.out.println("Result: " + tryMove(move));
                if(tryMove(move)){
                    validatedMoves.add(move);
                }
            }
        }
        return validatedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece currentPiece = board.getPiece(move.getStartPosition());
        HashSet<ChessMove> validatedMoves = currentPiece.pieceMoves(board,move.getStartPosition());
        if(validatedMoves.contains(move)){
            if(move.getPromotionPiece() == null){
                board.removePiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), currentPiece);
            }
            else{
                ChessPiece promotionPiece = new ChessPiece(this.teamTurn, move.getPromotionPiece());
                board.removePiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(),promotionPiece);
            }
            if(this.teamTurn == TeamColor.WHITE){
                this.teamTurn = TeamColor.BLACK;
            } else{
                this.teamTurn = TeamColor.WHITE;
            }
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
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i,j);
                if(board.getPiece(currentPosition) != null
                        && board.getPiece(currentPosition).getTeamColor() != teamColor){
                    HashSet<ChessMove> oponentMoves = board.getPiece(currentPosition).pieceMoves(board,currentPosition);
                    for(ChessMove move : oponentMoves){
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

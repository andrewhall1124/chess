package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
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

    private void revertMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(end);
        ChessPiece capturedPiece = board.getPiece(start);

        board.removePiece(end);
        if (capturedPiece != null) {
            board.addPiece(end, capturedPiece);
        }
        board.addPiece(start, currentPiece);
    }

    private boolean tryMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(start);
        ChessPiece capturedPiece = board.getPiece(end);
        TeamColor pieceColor = board.getPiece(start).getTeamColor();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();

        // Try move
        if (promotionPiece == null) {
            board.removePiece(start);
            board.addPiece(end, currentPiece);
        } else {
            ChessPiece promotion = new ChessPiece(pieceColor, promotionPiece);
            board.removePiece(start);
            board.addPiece(end, promotion);
        }

        // Check if in check after move
        boolean valid = !isInCheck(pieceColor);

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
        board.resetBoard();
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
        BLACK,
        OBSERVER,
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public HashSet<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> validatedMoves = new HashSet<>();
        //Check for valid start position
        if(board.getPiece(startPosition) != null){
            ChessPiece currentPiece = board.getPiece(startPosition);
            HashSet<ChessMove> possibleMoves = currentPiece.pieceMoves(board,startPosition);
            for(ChessMove move : possibleMoves){
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
        //Check for team turn
        TeamColor curColor = currentPiece.getTeamColor();
        if(currentPiece.getTeamColor() == this.teamTurn){
            HashSet<ChessMove> validatedMoves = validMoves(move.getStartPosition());
            //Check that move is valid
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
            else{
//                throw new InvalidMoveException("End position not allowed");
            }
        }
        else{
            throw new InvalidMoveException("Not your turn");
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
                    HashSet<ChessMove> opponentMoves = board.getPiece(currentPosition).pieceMoves(board,currentPosition);
                    for(ChessMove move : opponentMoves){
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
        if (!isInCheck(teamColor)) {
            return false;
        }

        ChessPosition kingPosition = getKingPosition(teamColor);
        ChessPiece king = board.getPiece(kingPosition);
        HashSet<ChessMove> kingMoves = king.pieceMoves(board, kingPosition);
        for (ChessMove move : kingMoves) {
            if (tryMove(move)) {
                revertMove(move);
                return false;
            }
        }

        // Check if any other piece can block or capture the attacking piece
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    HashSet<ChessMove> possibleMoves = validMoves(currentPosition);
                    for (ChessMove move : possibleMoves) {
                        if (tryMove(move)) {
                            boolean stillInCheck = isInCheck(teamColor);
                            revertMove(move);
                            if (!stillInCheck) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true; // If unable to escape check, it's checkmate
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    HashSet<ChessMove> possibleMoves = validMoves(currentPosition);
                    if (!possibleMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
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

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame chessGame)) return false;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}

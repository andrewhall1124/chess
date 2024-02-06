package chess;

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

    private HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = myPosition;
        //Up + Right
        if (myPosition.getRow() < 8 && myPosition.getColumn() < 8) {
            currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            while (board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if (curRow < 8 && curCol < 8) {
                    currentPosition = new ChessPosition(curRow + 1, curCol + 1);
                } else {
                    break; //Hit the edge of the board
                }
            }
        }

        //Down + Right
        if (myPosition.getRow() > 1 && myPosition.getColumn() < 8) {
            currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            while (board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if (curRow > 1 && curCol < 8) {
                    currentPosition = new ChessPosition(curRow - 1, curCol + 1);
                } else {
                    break; //Hit the edge of the board
                }
            }
        }
        //Down + Left
        if (myPosition.getRow() > 1 && myPosition.getColumn() > 1) {
            currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            while (board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if (curRow > 1 && curCol > 1) {
                    currentPosition = new ChessPosition(curRow - 1, curCol - 1);
                } else {
                    break; //Hit the edge of the board
                }
            }
        }
        //Up + Left
        if (myPosition.getRow() < 8 && myPosition.getColumn() > 1) {
            currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            while (board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                int curRow = currentPosition.getRow();
                int curCol = currentPosition.getColumn();
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
                if (board.getPiece(currentPosition) != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        break;
                    }
                }
                if (curRow < 8 && curCol > 1) {
                    currentPosition = new ChessPosition(curRow + 1, curCol - 1);
                } else {
                    break; //Hit the edge of the board
                }
            }
        }
        return moveList;
    }

    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moveList = new HashSet<>();
        //Up
        if(myPosition.getRow() < 8){
            ChessPosition currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
            }
            //Right
            if(myPosition.getColumn() < 8){
                currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
            //Left
            if(myPosition.getColumn() > 1){
                currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
        }
        //Down
        if(myPosition.getRow() > 1){
            ChessPosition currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
            }
            //Right
            if(myPosition.getColumn() < 8){
                currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
            //Left
            if(myPosition.getColumn() > 1){
                currentPosition = new ChessPosition(myPosition.getRow() -1, myPosition.getColumn() - 1);
                if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
        }
        //Right
        if(myPosition.getColumn() < 8){
            ChessPosition currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
            }
        }
        //Left
        if(myPosition.getColumn() > 1){
            ChessPosition currentPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
            if(board.getPiece(currentPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                moveList.add(possibleMove);
            }
        }
        return moveList;
    }
    private HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = myPosition;

        //Column 1
        if(myPosition.getColumn() > 2){
            //Bottom
            if(myPosition.getRow() > 1){
                currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
            //Top
            if(myPosition.getRow() < 8){
                currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
        }
        //Column 2
        if(myPosition.getColumn() > 1){
            //Bottom
            if(myPosition.getRow() > 2){
                currentPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
            //Top
            if(myPosition.getRow() < 7){
                currentPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
        }
        //Column 3
        if(myPosition.getColumn() < 8){
            //Bottom
            if(myPosition.getRow() > 2){
                currentPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
            //Top
            if(myPosition.getRow() < 7){
                currentPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
        }
        //Column 4
        if(myPosition.getColumn() < 7){
            //Bottom
            if(myPosition.getRow() > 1){
                currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
            //Top
            if(myPosition.getRow() < 8){
                currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
                if(board.getPiece(currentPosition) == null || (board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
            }
        }

        return moveList;
    }
    private HashSet<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moveList = new HashSet<>();
        ChessPosition currentPosition = myPosition;
        //White
        if(board.getPiece(myPosition).getTeamColor() == (ChessGame.TeamColor.WHITE)){
            if(myPosition.getRow() == 2){
                //Move Up 1 or 2
                currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if(board.getPiece(currentPosition) == null){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                    currentPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    if(board.getPiece(currentPosition) == null){
                        possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
                //Capture
                //Left
                if(myPosition.getColumn() < 8){
                    currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
                //Right
                if(myPosition.getColumn() > 1){
                    currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
            }
            else if(myPosition.getRow() < 7){
                //Up
                currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if(board.getPiece(currentPosition) == null){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
                //Promotion
                //Left
                if(myPosition.getColumn() < 8){
                    currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
                //Right
                if(myPosition.getColumn() > 1){
                    currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
            }
            if(myPosition.getRow() == 7){
                //Up
                currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if(board.getPiece(currentPosition) == null){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, PieceType.BISHOP);
                    moveList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition, currentPosition, PieceType.KNIGHT);
                    moveList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition, currentPosition, PieceType.QUEEN);
                    moveList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition, currentPosition, PieceType.ROOK);
                    moveList.add(possibleMove);
                }
                //Capture
                //Left
                if(myPosition.getColumn() < 8){
                    currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, PieceType.BISHOP);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.KNIGHT);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.QUEEN);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.ROOK);
                        moveList.add(possibleMove);
                    }
                }
                //Right
                if(myPosition.getColumn() > 1){
                    currentPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, PieceType.BISHOP);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.KNIGHT);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.QUEEN);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.ROOK);
                        moveList.add(possibleMove);
                    }
                }
            }
        }
        //Black
        if(board.getPiece(myPosition).getTeamColor() == (ChessGame.TeamColor.BLACK)){
            if(myPosition.getRow() == 7){
                //Move Down 1 or 2
                currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if(board.getPiece(currentPosition) == null){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                    currentPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if(board.getPiece(currentPosition) == null){
                        possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
                //Capture
                //Left
                if(myPosition.getColumn() < 8){
                    currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
                //Right
                if(myPosition.getColumn() > 1){
                    currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
            }
            else if(myPosition.getRow() > 2){
                //Down
                currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if(board.getPiece(currentPosition) == null){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                    moveList.add(possibleMove);
                }
                //Promotion
                //Left
                if(myPosition.getColumn() < 8){
                    currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
                //Right
                if(myPosition.getColumn() > 1){
                    currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, null);
                        moveList.add(possibleMove);
                    }
                }
            }
            if(myPosition.getRow() == 2){
                //Up
                currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if(board.getPiece(currentPosition) == null){
                    ChessMove possibleMove = new ChessMove(myPosition, currentPosition, PieceType.BISHOP);
                    moveList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition, currentPosition, PieceType.KNIGHT);
                    moveList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition, currentPosition, PieceType.QUEEN);
                    moveList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition, currentPosition, PieceType.ROOK);
                    moveList.add(possibleMove);
                }
                //Capture
                //Left
                if(myPosition.getColumn() < 8){
                    currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, PieceType.BISHOP);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.KNIGHT);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.QUEEN);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.ROOK);
                        moveList.add(possibleMove);
                    }
                }
                //Right
                if(myPosition.getColumn() > 1){
                    currentPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove possibleMove = new ChessMove(myPosition, currentPosition, PieceType.BISHOP);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.KNIGHT);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.QUEEN);
                        moveList.add(possibleMove);
                        possibleMove = new ChessMove(myPosition, currentPosition, PieceType.ROOK);
                        moveList.add(possibleMove);
                    }
                }
            }
        }
        return moveList;
    }
    private HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moveList = new HashSet<>();
        HashSet<ChessMove> bishopMoves = bishopMoves(board,myPosition);
        HashSet<ChessMove> rookMoves = rookMoves(board,myPosition);
        moveList.addAll(bishopMoves);
        moveList.addAll(rookMoves);
        return moveList;
    }
    private HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return HashSet of valid moves
     */
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition).getPieceType().equals(PieceType.BISHOP)) {
            return bishopMoves(board, myPosition);
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.KING)) {
            return kingMoves(board, myPosition);
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.KNIGHT)) {
            return knightMoves(board, myPosition);
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.ROOK)) {
            return rookMoves(board, myPosition);
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.QUEEN)) {
            return queenMoves(board, myPosition);
        }
        else if(board.getPiece(myPosition).getPieceType().equals(PieceType.PAWN)) {
            return pawnMoves(board, myPosition);
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

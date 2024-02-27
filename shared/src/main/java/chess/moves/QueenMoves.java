package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class QueenMoves {
    private final ChessPosition myPosition;
    private final ChessBoard board;

    public QueenMoves(ChessPosition myPosition, ChessBoard board) {
        this.myPosition = myPosition;
        this.board = board;
    }

    public HashSet<ChessMove> getMoves(){
        HashSet<ChessMove> moveList = new HashSet<>();
        BishopMoves bishopMoves = new BishopMoves(myPosition, board);
        RookMoves rookMoves = new RookMoves(myPosition, board);
        moveList.addAll(bishopMoves.getMoves());
        moveList.addAll(rookMoves.getMoves());
        return moveList;
    }
}

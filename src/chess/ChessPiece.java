package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
	
	private Color color;
	private int moveCount;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}
	
	public ChessPosition getChessPosition() {/*check*/
		return ChessPosition.formPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = /*down cast ---->*/(ChessPiece)/*<---*/getBoard().piece(position);
		return p != null && p.getColor() != color; /*Cor Adversaria*/
	}
 
	
}

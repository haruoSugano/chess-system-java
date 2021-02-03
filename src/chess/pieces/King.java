package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
	
	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override 
	public String toString() {
		return "K";
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}
	
	private boolean testRookCastling (Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		//above
		p.setValues(position.getRow()-1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//below
		p.setValues(position.getRow()+1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//left
		p.setValues(position.getRow(), position.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//right
		p.setValues(position.getRow(), position.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//nw (noroest)
		p.setValues(position.getRow()-1, position.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//ne(nordest)
		p.setValues(position.getRow()-1, position.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//sw(suldoest)
		p.setValues(position.getRow()+1, position.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//se(sudest)
		p.setValues(position.getRow()+1, position.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// #specialMove castling
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			//#specialMove castling kingSide rook
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			/*Verificar as duas casas para o movimento*/
		if(testRookCastling(posT1)) {
			Position p1 = new Position(position.getRow(), position.getColumn() + 1);
			Position p2 = new Position(position.getRow(), position.getColumn() + 2);
		/*----------------------------------------*/
		if(getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
			mat[position.getRow()][position.getColumn() + 2] = true;
			}
		}
		// #specialMove castling queenSide rook
		Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
		/*Verificar as tres casa para o movimento*/
		if(testRookCastling(posT1)) {
			Position p1 = new Position(position.getRow(), position.getColumn() - 1);
			Position p2 = new Position(position.getRow(), position.getColumn() - 2);
			Position p3 = new Position(position.getRow(), position.getColumn() - 3);
			/*------------------------------------*/
			/*Teste para que o rei possa se mover */
		if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null ) {
			mat[position.getRow()][position.getColumn() - 2] = true;
				}
		/*---------------------------------------*/
			}
		
			
		}
		return mat;
	}
}

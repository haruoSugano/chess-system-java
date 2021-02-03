package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "Q";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		// above
		
		p.setValues(position.getRow() - 1, position.getColumn()); /*position.getRow() - 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		/*Verificando acima da pecas */
		
		// below
		
		p.setValues(position.getRow() + 1, position.getColumn()); /*position.getRow() + 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		/*Verificando em baixo da pecas */
		
		// left
		
		p.setValues(position.getRow() , position.getColumn()- 1); /*position.getColumn() - 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
				/*Verificando a esqueda da pecas */
		
		// right
		
		p.setValues(position.getRow() , position.getColumn() + 1); /*position.getColumn() + 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
				/*Verificando a direita da pecas */
		
		// NW
		
		p.setValues(position.getRow() - 1, position.getColumn() -1); /*position.getRow() - 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		/*Verificando diagonal noroeste da pecas */
		
		// NE
		
		p.setValues(position.getRow() - 1, position.getColumn() + 1); /*position.getRow() + 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		/*Verificando diagonal nordeste da pecas */
		
		// SE
		
		p.setValues(position.getRow() + 1 , position.getColumn() + 1); /*position.getColumn() - 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
				/*Verificando diagonal sudeste pecas */
		
		// SO
		
		p.setValues(position.getRow() + 1, position.getColumn() - 1); /*position.getColumn() + 1 e o verificador*/
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
				/*Verificando diagonal sudoeste pecas */
				
		return mat;
	} 

}

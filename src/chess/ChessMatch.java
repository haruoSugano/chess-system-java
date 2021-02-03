package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check; /*property check*/
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVunerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public ChessPiece[][] getPiece(){/*Comando com que o programa so enchergue a camada chess*/
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i=0; i<board.getRows(); i++) {
			for(int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j);
			}
		}
		return mat;
	}
	/*Imprimir aplicacoes de origem*/
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves(); 
	}
	/*------------------------------*/
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);/*Validando a posicao do destino*/
		Piece capturedPiece = makeMove(source, target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		/*Promotion move precisa ser montada antes do teste de comando "check" */
		//Promotion move
		promoted = null;
		if(movedPiece instanceof Pawn) {
			if(movedPiece.getColor() == Color.WHITE && target.getRow() == 0/*peca branca chegou ao final*/ || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7 /*peca preta chegou ao final*/)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		// #spiecialmove en passant
		if(movedPiece /*se a peca movida for*/instanceof Pawn && (target.getRow() == source.getRow() - 2 ) || (target.getRow() == source.getRow() + 2 ) ) {//Testando se ela andou duas casas
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	public ChessPiece replacePromotedPiece (String type) {
		if(promoted == null) {
			throw new IllegalStateException ("There is no piece to be promoted");
		}
		if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
			throw new InvalidParameterException("Invalid type for promotion");
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {//metodo auxiliar testar para instanciar uma peca especifica
		if(type.equals("B")) return new Bishop(board, color);
		if(type.equals("N")) return new Knight(board, color);
		if(type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);/*retiro a pecas no tabuleiro*/
			capturedPieces.add(capturedPiece);/*add na lista de pecas capturadas*/
		}
		
		// #specialMove castling kingSide rook
		if(p instanceof King && target.getColumn() == source.getColumn() + 2 ) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);//Posicao onde esta a torre "source"
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);//Posicao onde esta a torre "target"
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);/*retirar onde esta a torre*/
			board.placePiece(rook, targetT);/*Colocar a torre para o destino dela*/
			rook.increaseMoveCount();/*incrementar a quantidade de movimento da torre*/
		}
		
		// #specialMove castling queenSide rook
		if(p instanceof King && target.getColumn() == source.getColumn() - 2 ) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);//Posicao onde esta a torre "source"
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);//Posicao onde esta a torre "target"
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);/*retirar onde esta a torre*/
			board.placePiece(rook, targetT);/*Colocar a torre para o destino dela*/
			rook.increaseMoveCount();/*incrementar a quantidade de movimento da torre*/
		}
		
		// #specialmove en passant
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {//o peao andou na diagonal e nao capturou a peca e um en passant
				Position pawnPosition;
				if(p.getColor() == Color.WHITE) {//peca "white"  ira capturar a peca de baixo 
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());//Linha de baixo
				}
				else {//peca "black" ira capturar peca de cima
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());//Linha acima
				}
				//Comando de captura 
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {/*no check nao pode ser mover method UndoMove*/
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		// #specialMove castling kingSide rook
		if(p instanceof King && target.getColumn() == source.getColumn() + 2 ) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);//Posicao onde esta a torre "source"
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);//Posicao onde esta a torre "target"
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);/*retirar onde esta a torre*/
			board.placePiece(rook, sourceT);/*Colocar a torre para o destino dela*/
			rook.decreaseMoveCount();;/*incrementar a quantidade de movimento da torre*/
		}
		
		// #specialMove castling queenSide rook
		if(p instanceof King && target.getColumn() == source.getColumn() - 2 ) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);//Posicao onde esta a torre "source"
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);//Posicao onde esta a torre "target"
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);/*retirar onde esta a torre*/
			board.placePiece(rook, sourceT);/*Colocar a torre para o destino dela*/
			rook.decreaseMoveCount();/*incrementar a quantidade de movimento da torre*/
		}
		
		// #specialmove en passant
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {//o peao andou na diagonal e nao capturou a peca e um en passant
				ChessPiece pawn = (ChessPiece)board.removePiece(target);/*tirou o pawn do lugar errado(manualmente)*/
				Position pawnPosition;
				if(p.getColor() == Color.WHITE) {//peca "white"  ira capturar a peca de baixo 
					pawnPosition = new Position(3, target.getColumn());//Linha de baixo
				}
				else {//peca "black" ira capturar peca de cima
					pawnPosition = new Position(4, target.getColumn());//Linha acima
				}
				board.placePiece(pawn, pawnPosition);/*devolveu a pecas*/
			}
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;/*Troca de turno*/
	}
	
	private Color opponent(Color color) {/*metod opponent*/
		return (color == Color.WHITE) /*entao*/?/*<--*/ Color.BLACK /*caso contrario*/:/*<--*/Color.WHITE;
	}
	
	private ChessPiece King(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x-> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p: list) {
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + "king on the board");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = King(color).getChessPosition().toPosition();
		List<Piece>opponentPieces = piecesOnTheBoard.stream().filter(x-> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece>list = piecesOnTheBoard.stream().filter(x-> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for(int i=0; i<board.getRows();i++) {
				for(int j=0; j<board.getColumns(); j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());/*Coloca pecas no tabuleiro*/
		piecesOnTheBoard.add(piece);/*Coloca as pecas na lista*/
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE,this));
		
		
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK,this));
		
	
	}
}

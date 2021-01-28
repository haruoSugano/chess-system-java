package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		
		while (true) {
			try {
				UI.clearScreen();/*Tem a funcao de limpar a tela, so mostra a tela do xadrez atualizada*/
				UI.printBoard(chessMatch.getPiece());
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPiece(), possibleMoves);/*Esse printBoard e o que vai imprimir os movimentos possiveis*/
				
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			}
			catch(ChessException e){
				System.out.println(e.getMessage());
				sc.nextLine();/*Excessao para que o usuario, retorne a inserir o dados*/
			}
			catch(InputMismatchException e){
				System.out.println(e.getMessage());
				sc.nextLine();/*Excessao para que o usuario, retorne a inserir o dados*/
			}

		}
	}

}

package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();/*Lista de pecas capturada*/
		
		while (!chessMatch.getCheckMate()) {
			try {
				UI.clearScreen();/*Tem a funcao de limpar a tela, so mostra a tela do xadrez atualizada*/
				UI.printMatch(chessMatch, captured);
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
				if(capturedPiece != null) {
					captured.add(capturedPiece);
				}
				
				if(chessMatch.getPromoted() != null) {
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String type = sc.nextLine().toUpperCase();
					while((!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q") )) {
						System.out.print("Invalid value!! Enter piece for promotion (B/N/R/Q): ");
						type = sc.nextLine().toUpperCase();
					}
					chessMatch.replacePromotedPiece(type);
				}
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
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}

}

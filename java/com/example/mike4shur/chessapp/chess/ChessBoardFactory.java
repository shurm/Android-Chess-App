package com.example.mike4shur.chessapp.chess;

import com.example.mike4shur.chessapp.chess_piece_types.Bishop;
import com.example.mike4shur.chessapp.chess_piece_types.ChessPieceTypes;
import com.example.mike4shur.chessapp.chess_piece_types.EmptyChessPiece;
import com.example.mike4shur.chessapp.chess_piece_types.King;
import com.example.mike4shur.chessapp.chess_piece_types.Knight;
import com.example.mike4shur.chessapp.chess_piece_types.Pawn;
import com.example.mike4shur.chessapp.chess_piece_types.Queen;
import com.example.mike4shur.chessapp.chess_piece_types.Rook;
import com.example.mike4shur.chessapp.geometry.Point;


/**
 * This is the ChessBoardFactory class it is used to create ChessBoard Objects
 *
 * This class's main purpose was to modularize/ divide up the code,
 * also prevents the ChessBoard.java file from being super long
 *
 * @author Michael Shur
 *
 */
public class ChessBoardFactory
{
    /**
     * @return a new ChessBoard containing chess pieces in the starting positions
     */
    public static ChessBoard createNewChessBoard()
    {
        ChessBoard newBoard = new ChessBoard();


        putPiecesOnBoard(newBoard);

        newBoard.whiteKingLocation=new Point(7,4);

        newBoard.blackKingLocation=new Point(0,4);


        return newBoard;
    }

    /**
     *
     * @param board     the board that will be copied
     * @return a new ChessBoard which is an exact duplicate of the param
     */
    public static ChessBoard createNewChessBoard(ChessBoard board)
    {
        return createCopyBoard(board, null, null);
    }


    public static ChessBoard createNewChessBoard(ChessBoard chessBoard2, Point copiedFrom, Point copiedInto)
    {
        ChessBoard newBoard=createCopyBoard( chessBoard2,copiedFrom, copiedInto);

        SpecialMoves.makeSpecialChanges(newBoard, newBoard.getPiece(copiedInto), copiedFrom, copiedInto, ' ');
        return newBoard;
    }

    /**
     * Updates the locations of the Kings on the chessBoard if the piece
     * that was just moved was a King
     *
     *
     * @param r				row of the chesspiece
     * @param c 			column of the chesspiece
     */
    private static void getNewKingLocations(ChessBoard chessBoard,int r , int c)
    {
        Point p=new Point(r,c);
        EmptyChessPiece piece=chessBoard.getPiece(p);
        if(piece!=null && piece instanceof King)
        {
            if(piece.isBlack())
                chessBoard.blackKingLocation=p;
            else
                chessBoard.whiteKingLocation=p;
        }
    }

    /**
     *
     * @param chessBoard2
     * @param copiedFrom
     * @param copiedInto
     */
    private static ChessBoard createCopyBoard(ChessBoard chessBoard2, Point copiedFrom, Point copiedInto)
    {
        ChessBoard newBoard= new ChessBoard();

        for(int r=0;r<ChessBoard.classicChessBoardDimension;r++)
        {
            for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
            {
                newBoard.chessBoard[r][c]=null;
                if(chessBoard2.chessBoard[r][c]!=null)
                    newBoard.chessBoard[r][c]=chessBoard2.chessBoard[r][c].createDuplicate(newBoard);

                getNewKingLocations(newBoard,r,c);
            }
        }
        if(copiedInto!=null) {
            newBoard.chessBoard[copiedInto.getRow()][copiedInto.getColumn()] = newBoard.chessBoard[copiedFrom.getRow()][copiedFrom.getColumn()];
            newBoard.chessBoard[copiedFrom.getRow()][copiedFrom.getColumn()] = null;

            getNewKingLocations(newBoard, copiedInto.getRow(), copiedInto.getColumn());
        }
        return newBoard;
    }

    private static void putPiecesOnBoard(ChessBoard newBoard)
    {
        generatePiecesForAPlayer(newBoard, ChessPieceTypes.black);

        generatePiecesForAPlayer(newBoard, ChessPieceTypes.white);

    }

    private static void generatePiecesForAPlayer(ChessBoard newBoard, String type)
    {
        generate8Pawns(newBoard,type);
        generateRooks(newBoard,type);
        generateKnights(newBoard,type);
        generateBishops(newBoard,type);
        generateQueen(newBoard,type);
        generateKing(newBoard,type);
    }

    private static void generateKing(ChessBoard newBoard, String type)
    {
        int rowNumber=getStartingRow(type);

        newBoard.chessBoard[rowNumber][4]=new King(newBoard,type);
    }

    private static void generateQueen(ChessBoard newBoard, String type)
    {
        int rowNumber=getStartingRow(type);

        newBoard.chessBoard[rowNumber][3]=new Queen(newBoard,type);
    }

    private static void generateBishops(ChessBoard newBoard, String type)
    {
        int rowNumber=getStartingRow(type);

        newBoard.chessBoard[rowNumber][2]=new Bishop(newBoard,type);

        newBoard.chessBoard[rowNumber][5]=new Bishop(newBoard,type);
    }

    private static void generateKnights(ChessBoard newBoard, String type)
    {
        int rowNumber=getStartingRow(type);

        newBoard.chessBoard[rowNumber][1]=new Knight(newBoard,type);

        newBoard.chessBoard[rowNumber][6]=new Knight(newBoard,type);
    }

    private static void generateRooks(ChessBoard newBoard, String type)
    {
        int rowNumber=getStartingRow(type);

        newBoard.chessBoard[rowNumber][0]=new Rook(newBoard,type);

        newBoard.chessBoard[rowNumber][7]=new Rook(newBoard,type);
    }

    private static void generate8Pawns(ChessBoard newBoard, String type)
    {
        int rowNumber;

        if(type.equals(ChessPieceTypes.black))
            rowNumber=1;
        else
            rowNumber=ChessBoard.classicChessBoardDimension-2;

        for(int column=0;column<ChessBoard.classicChessBoardDimension;column++)
        {
            newBoard.chessBoard[rowNumber][column]=new Pawn(newBoard,type);
        }

    }
    private static int getStartingRow(String type)
    {
        int rowNumber;

        if(type.equals(ChessPieceTypes.black))
            rowNumber=0;
        else
            rowNumber=ChessBoard.classicChessBoardDimension-1;

        return rowNumber;
    }


}

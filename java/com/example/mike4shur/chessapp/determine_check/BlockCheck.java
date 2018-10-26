package com.example.mike4shur.chessapp.determine_check;

import android.util.Log;

import com.example.mike4shur.chessapp.chess.ChessBoardFactory;
import com.example.mike4shur.chessapp.chess.SpecialMoves;
import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.chess_piece_types.*;

import java.util.ArrayList;

/**
 * This is the BlockCheck class for determining if a player is able to block a check
 *
 * @author Michael Shur
 *
 */
public class BlockCheck
{
    /**
     * @param board				the particular chessBoard we are dealing with
     * @param king				the Point on the ChessBoard where the King is
     * @param enemyType			specifies what the enemy piece type is
     * @return true 			if a player is able to block a check from an enemy piece given the pieces
     *         					that are currently on the chessboard
     * @return false 			otherwise
     */
    public static boolean areAbleToBlockCheck(ChessBoard board,Point king, String enemyType)
    {
        String typeWeCanMove;

        if(enemyType.equals(ChessPieceTypes.black))
            typeWeCanMove=ChessPieceTypes.white;
        else
            typeWeCanMove=ChessPieceTypes.black;


        for(int r=0;r<ChessBoard.classicChessBoardDimension;r++)
        {
            for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
            {
                Point p=new Point (r,c);

                if(	board.getPiece(p)!=null && 	board.getPiece(p).getType().equals(typeWeCanMove))
                {
                    //tries to move the piece every way it can be moved
                    if(tryAllPossibleMoves(board,board.getPiece(p),new Point(r,c),enemyType,typeWeCanMove))
                        return true;
                }
            }
        }

        return false;
    }

    /**
     * This method attempts to try all possible ways the chessPiece can move
     *
     * @param chessBoard			the particular chessBoard we are dealing with
     * @param chessPiece			the opponent piece on the chessBoard that was found
     * @param p						the location of the ChessBoard where the chessPiece is located

     * @param enemyType				specifies what the enemy piece type is
     * @return true 				if there are possible moves that can block the check
     * @return false 				otherwise
     */
    private static boolean tryAllPossibleMoves(ChessBoard chessBoard,EmptyChessPiece chessPiece, Point p, String enemyType,String type)
    {

        ArrayList<Point> points=chessPiece.generatePoints(p);

        for(int a=0;a<points.size();a++)
        {
            ChessBoard newBoard= ChessBoardFactory.createNewChessBoard(chessBoard, p, points.get(a));

            if(!newBoard.checkHasOccurred(newBoard.getKingLocation(type), enemyType))
            {
                return true;
            }
        }

        return false;
    }

}

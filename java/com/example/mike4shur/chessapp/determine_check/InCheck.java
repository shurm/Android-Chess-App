package com.example.mike4shur.chessapp.determine_check;

import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.chess_piece_types.*;

/**
 * This is the InCheck class for determining if a player is in check
 *
 * @author Michael Shur
 *
 */
public class InCheck
{
    /**
     *
     * @param chessBoard the particular chessBoard we are dealing with
     * @param enemyType specifies what the enemy piece type is
     * @param p, the Point on the ChessBoard (presumbly where the King is)
     * @return true if opponent chesspieces are checking/attacking the Point p
     * @return false otherwise
     */
    public static boolean areEnemiesAttackingPoint(ChessBoard chessBoard, String enemyType, Point p)
    {
        if(enemyPawnCheck(chessBoard,enemyType,p))
            return true;
        if(enemyKnightsCheck(chessBoard,enemyType,p))
            return true;
        if(enemyBishopOrQueenCheck(chessBoard,enemyType,p))
            return true;
        if(enemyRookOrQueenCheck(chessBoard,enemyType,p))
            return true;
        if(enemyKingCheck(chessBoard,enemyType,p))
            return true;
        return false;
    }

    /**
     *
     * @param chessBoard the particular chessBoard we are dealing with
     * @param type specifies what the enemy piece type is
     * @param p, the Point on the ChessBoard (presumably where the King is)
     * @return true if an opponent King piece is checking/attacking the Point p
     * @return false otherwise
     */
    private static boolean enemyKingCheck(ChessBoard chessBoard,String type, Point p)
    {
        for(int r=p.getRow()-1;r<=p.getRow()+1;r++)
        {
            for(int c=p.getColumn()-1;c<=p.getColumn()+1;c++)
            {
                Point p1=new Point(r,c);

                if(chessBoard.pointOutOfBounds(p1)==false && chessBoard.getPiece(p1)!=null)
                {
                    if(chessBoard.getPiece(p1).getType().equals(type) &&
                            chessBoard.getPiece(p1) instanceof King)
                        return true;
                    break;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param chessBoard		 the particular chessBoard we are dealing with
     * @param enemyType 		specifies what the enemy piece type is
     * @param p,				 the Point on the ChessBoard (presumably where the King is)
     * @return true 			if an opponent Rook or Queen piece is checking/attacking the Point p
     * @return false otherwise
     */
    private static boolean enemyRookOrQueenCheck(ChessBoard chessBoard, String enemyType, Point p)
    {
        //go right
        if(goStraight(chessBoard,enemyType,p,0,1))
            return true;

        //go left
        if(goStraight(chessBoard,enemyType,p,0,-1))
            return true;

        //go up
        if(goStraight(chessBoard,enemyType,p,1,0))
            return true;

        //go down
        if(goStraight(chessBoard,enemyType,p,-1,0))
            return true;
        return false;
    }

    /**
     * Goes straight (up, down, left or right) looking for an enemy Queen or Rook
     *
     * @param chessBoard		the particular chessBoard we are dealing with
     * @param type 		specifies what the enemy piece type is
     * @param p,				a Point on the ChessBoard (presumably where the King is)
     * @param rInc				the amount that will be added to row
     * @param cInc				the amount that will be added to column
     * @return true				if an opponent Rook or Queen piece is checking/attacking the Point p
     */
    private static boolean goStraight(ChessBoard chessBoard, String type, Point p, int rInc, int cInc)
    {
        for(Point p1=new Point(p.getRow()+rInc,p.getColumn()+cInc);chessBoard.pointOutOfBounds(p1)==false;
            p1.setRow(p1.getRow()+rInc),p1.setColumn(p1.getColumn()+cInc))
        {
            if(chessBoard.getPiece(p1)!=null)
            {
                if(chessBoard.getPiece(p1).getType().equals(type) &&
                        (chessBoard.getPiece(p1) instanceof Queen ||
                                chessBoard.getPiece(p1) instanceof Rook))
                    return true;
                break;
            }
        }
        return false;
    }

    /**
     *
     * @param chessBoard		the particular chessBoard we are dealing with
     * @param enemyType 		specifies what the enemy piece type is
     * @param p,				a Point on the ChessBoard (presumably where the King is)
     * @return true 			if an opponent Bishop or Queen piece is checking/attacking the Point p
     * @return false otherwise
     */
    private static boolean enemyBishopOrQueenCheck(ChessBoard chessBoard, String enemyType, Point p)
    {
        if(goDiagonally( chessBoard,enemyType, p, 1, 1))
            return true;
        if(goDiagonally(chessBoard, enemyType, p, 1, -1))
            return true;
        if(goDiagonally( chessBoard,enemyType, p, -1, 1))
            return true;
        if(goDiagonally( chessBoard,enemyType, p, -1, -1))
            return true;

        return false;
    }

    /**
     * Goes diagonally  (left-up, left-down, right-up, or right-down) looking for an enemy Queen or Bishop
     *
     * @param chessBoard		the particular chessBoard we are dealing with
     * @param type 		specifies what the enemy piece type is
     * @param p,				a Point on the ChessBoard (presumably where the King is)
     * @param rInc				the amount that will be added to row
     * @param cInc				the amount that will be added to column
     * @return true				if an opponent Queen or Bishop piece is checking/attacking the Point p
     */
    private static boolean goDiagonally(ChessBoard chessBoard, String type, Point p, int rInc, int cInc)
    {
        for(Point p1=new Point(p.getRow()+rInc,p.getColumn()+cInc);chessBoard.pointOutOfBounds(p1)==false;
            p1.setRow(p1.getRow()+rInc),p1.setColumn(p1.getColumn()+cInc))
        {
            if(chessBoard.getPiece(p1)!=null)
            {
                if(chessBoard.getPiece(p1).getType().equals(type) &&
                        (chessBoard.getPiece(p1) instanceof Queen ||
                                chessBoard.getPiece(p1) instanceof Bishop))
                    return true;
                break;
            }
        }
        return false;
    }

    /**
     *
     * @param chessBoard		the particular chessBoard we are dealing with
     * @param enemyType 		specifies what the enemy piece type is
     * @param p,				a Point on the ChessBoard (presumably where the King is)
     * @return true 			if an opponent Pawn piece is checking/attacking the Point p
     * @return false otherwise
     */
    private static boolean enemyPawnCheck(ChessBoard chessBoard, String enemyType, Point p)
    {
        Point p1;
        Point p2;

        if(enemyType.equals(ChessPieceTypes.black))
        {
            p1=new Point(p.getRow()-1, p.getColumn()-1);
            p2=new Point(p.getRow()-1, p.getColumn()+1);
        }
        else
        {
            p1=new Point(p.getRow()+1, p.getColumn()-1);
            p2=new Point(p.getRow()+1, p.getColumn()+1);
        }

        if(chessBoard.pointOutOfBounds(p1)==false && chessBoard.getPiece(p1)!=null &&
                chessBoard.getPiece(p1).getType().equals(enemyType) &&
                chessBoard.getPiece(p1) instanceof Pawn)
            return true;


        if(chessBoard.pointOutOfBounds(p2)==false && chessBoard.getPiece(p2)!=null &&
                chessBoard.getPiece(p2).getType().equals(enemyType) &&
                chessBoard.getPiece(p2) instanceof Pawn)
            return true;


        return false;
    }

    /**
     *
     * @param chessBoard		the particular chessBoard we are dealing with
     * @param enemyType 		specifies what the enemy piece type is
     * @param p,				a Point on the ChessBoard (presumably where the King is)
     * @return true 			if an opponent Knight piece is checking/attacking the Point p
     * @return false otherwise
     */
    private static boolean enemyKnightsCheck(ChessBoard chessBoard, String enemyType, Point p)
    {
        int [] m1={1,-1};

        int [] m2={2,-2};

        if(knightHelper(m1,m2, chessBoard, enemyType, p))
            return true;
        if(knightHelper(m2,m1, chessBoard, enemyType, p))
            return true;

        return false;
    }

    /**
     *
     * A Knight can only move 1 square vertically and 2 squares horizontally or vice-versa
     *
     * knight Helper computes the possible squares an enemy Knight could be,
     * which would also result in a "check"
     *
     * @param rAdd 				the amount that will be added to row
     * @param cAdd				the amount that will be added to column
     * @param chessBoard		the particular chessBoard we are dealing with
     * @param enemyType 		specifies what the enemy piece type is
     * @param p,				a Point on the ChessBoard (presumably where the King is)
     * @return true 			if an opponent Knight piece is at any of the Points computed
     * @return false otherwise
     */
    private static boolean knightHelper(int [] rAdd,int []  cAdd, ChessBoard chessBoard, String enemyType, Point p)
    {
        for(int a=0;a<rAdd.length;a++)
        {
            for(int b=0;b<cAdd.length;b++)
            {
                Point p1= new Point(p.getRow()+rAdd[a],p.getColumn()+cAdd[b]);

                if(chessBoard.pointOutOfBounds(p1)==false && chessBoard.getPiece(p1)!=null &&
                        chessBoard.getPiece(p1).getType().equals(enemyType) &&
                        chessBoard.getPiece(p1) instanceof Knight)
                {
                    return true;
                }
            }
        }
        return false;
    }
}

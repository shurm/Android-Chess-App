package com.example.mike4shur.chessapp.chess_piece_types;

import android.util.Log;

import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.determine_check.InCheck;

import java.util.ArrayList;

/**
 * Class which represents the King chess piece
 * it is a subclass of EmptyChessPiece.
 *
 * @author Michael Shur
 *
 */
public class King extends EmptyChessPiece
{
    //Variable which tells if King has been moved before, because if it has then it cannot perform castling
    private boolean moved=false;

    /*
         getter and setter
     */
    public boolean hasBeenMoved()
    {
        return moved;
    }
    public void setMoved(boolean moved)
    {
        this.moved=moved;
    }

    public King(ChessBoard board,String type)
    {
        super(board,type);
    }

    private void tryToCastle(ArrayList<Point> newPoints,Point p1)
    {
        //if King has been moved before we cant castle
        if(hasBeenMoved())
            return;

        if(this.isBlack())
        {
            //Log.v("tag","right rook");
            //are we castling with Rook on the right side
            if(areCastlingConditionsGood(new Point(0,7)))
                castlingHelper(newPoints,p1,new Point(0,6), 1, ChessPieceTypes.white);

           // Log.v("tag","left rook");
            //are we castling with Rook on the left side
            if(areCastlingConditionsGood(new Point(0,0)))
                castlingHelper(newPoints,p1,new Point(0,2), -1, ChessPieceTypes.white);
        }
        else
        {
           // Log.v("tag","right rook");
            //are we castling with Rook on the right side
            if(areCastlingConditionsGood(new Point(7,7)))
                castlingHelper(newPoints,p1,new Point(7,6), 1, ChessPieceTypes.black);

           // Log.v("tag","left rook");
            //are we castling with Rook on the left side
            if(areCastlingConditionsGood(new Point(7,0)))
                castlingHelper(newPoints,p1,new Point(7,2), -1, ChessPieceTypes.black);
        }

    }

    private void castlingHelper(ArrayList<Point> newPoints,Point p1,Point p2, int inc, String enemyType)
    {
        if(!kingCrossingCheck(p1, p2,  inc, enemyType))
            return;

        if(emptySquaresCheck(p1,  inc))
            newPoints.add(p2);
    }

    private boolean emptySquaresCheck(Point p1, int inc) {
        int squaresInBetween;
        if(inc==1)
        {
            squaresInBetween=2;
        }
        else
        {
            squaresInBetween=3;
        }

        //checks if the squares Between the King and the Rook are unoccupied
        Point p=new Point(p1.getRow(),p1.getColumn()+inc);
        for(int a=0;a<squaresInBetween;a++,p.setColumn(p.getColumn()+inc) )
        {
            if(board.getPiece(p)!=null)
            {
                //.v("tag","There is a piece in between the rook and king");
                return false;
            }
        }
        return true;
    }

    private boolean kingCrossingCheck(Point p1,Point p2, int inc, String enemyType)
    {
        //checks if the King every crosses a Point that is "in check"
        for(Point p=new Point(p1.getRow(),p1.getColumn());true;p.setColumn(p.getColumn()+inc))
        {
            if(InCheck.areEnemiesAttackingPoint(board, enemyType, p))
            {
                //Log.v("tag","King crosses in check square.");
                return false;
            }
            if(p.equals(p2))
                break;
        }
        return true;
    }

    // if the Rook was moved before.
    private boolean areCastlingConditionsGood(Point rookPosition)
    {
        Point rook=rookPosition;
        if(board.getPiece(rook)==null || !(board.getPiece(rook) instanceof Rook)
                || ((Rook)board.getPiece(rook)).hasBeenMoved()) {
            //Log.v("tag","Rook test failed");
            return false;
        }

        return true;
    }

    public String toString()
    {
        return type+"K";
    }

    @Override
    public ArrayList<Point> generatePoints(Point p)
    {
        ArrayList<Point> newPoints = new ArrayList<Point>();
        for(int r=p.getRow()-1;r<=p.getRow()+1;r++)
        {
            for(int c=p.getColumn()-1;c<=p.getColumn()+1;c++)
            {
                Point p1=new Point(r,c);
                if(!board.pointOutOfBounds(p1) && !p1.equals(p) &&(board.getPiece(p1)==null || board.hasEnemyPiece(p1, this)))
                {
                    newPoints.add(p1);
                }
            }
        }

        tryToCastle(newPoints, p);

        return newPoints;
    }

    @Override
    public EmptyChessPiece createDuplicate(ChessBoard newBoard)
    {
        King king =new King(newBoard, getType());
        king.moved=moved;
        return king;
    }
}

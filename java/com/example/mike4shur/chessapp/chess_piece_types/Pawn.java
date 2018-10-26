package com.example.mike4shur.chessapp.chess_piece_types;

import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;

import java.util.ArrayList;

/**
 * Class which represents the Pawn chess piece
 * it is a subclass of EmptyChessPiece.
 *
 * @author Michael Shur
 *
 */
public class Pawn extends EmptyChessPiece
{
    //variable which states whether this Pawn may be taken via en passant
    private boolean en_passant=false;

    /*
         getter and setter
     */
    public boolean isEn_passant() {
        return en_passant;
    }
    public void setEn_passant(boolean en_passant) {
        this.en_passant= en_passant;
    }


    public Pawn(ChessBoard board,String type)
    {
        super(board,type);
    }


    public String toString()
    {
        return type+"p";
    }

    @Override
    public ArrayList<Point> generatePoints(Point p)
    {
        ArrayList<Point> newPoints=new ArrayList<Point>();
        if(isBlack())
        {
            pawnMoves(newPoints, p, 1, 1);
        }
        else
        {
            pawnMoves(newPoints, p, 6, -1);
        }
        return newPoints;
    }


    private void pawnMoves(ArrayList<Point> newPoints, Point p, int startingRow,int rInc)
    {
        moveStraight(newPoints,  p,  startingRow, rInc);

        Point newPoint;
        int [] array={-1,1};

        //diagonally takes enemy piece
        for(int a=0;a<array.length;a++)
        {
            newPoint=new Point(p.getRow()+rInc,p.getColumn()+array[a]);
            if(!board.pointOutOfBounds(newPoint) && board.hasEnemyPiece(newPoint,this))
                newPoints.add(newPoint);
        }

        //try en_passant
        for(int a=0;a<array.length;a++)
        {
            Point p1=new Point(p.getRow(),p.getColumn()+array[a]);
            Point p2=new Point(p.getRow()+rInc,p.getColumn()+array[a]);
            if(!board.pointOutOfBounds(p1) && board.hasEnemyPiece(p1, this) && board.getPiece(p1) instanceof Pawn && ((Pawn)board.getPiece(p1)).isEn_passant())
                newPoints.add(p2);
        }

    }

    private void moveStraight(ArrayList<Point> newPoints,Point p,int startingRow, int rInc )
    {
        Point newPoint;

        //try moving forward 1 square
        newPoint=new Point(p.getRow()+rInc,p.getColumn());
        if(board.pointOutOfBounds(newPoint) || board.objectIsOnChessBoard(newPoint))
            return;

        newPoints.add(newPoint);

        //try moving forward 2 squares
        if(p.getRow()==startingRow)
        {
            newPoint=new Point(startingRow+2*rInc,p.getColumn());

            if(!board.objectIsOnChessBoard(newPoint))
                newPoints.add(newPoint);
        }
    }
    @Override
    public EmptyChessPiece createDuplicate(ChessBoard newBoard) {
        Pawn pawn = new Pawn(newBoard, getType());
        pawn.en_passant=this.en_passant;
        return pawn;
    }
}

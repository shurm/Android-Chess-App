package com.example.mike4shur.chessapp.chess_piece_types;

import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;

import java.util.ArrayList;

/**
 * Class which represents the Knight chess piece
 * it is a subclass of EmptyChessPiece.
 *
 * @author Michael Shur
 *
 */
public class Knight extends EmptyChessPiece
{

    public Knight(ChessBoard board,String type)
    {
        super(board,type);
    }

    public String toString()
    {
        return type+"N";
    }


    //A knight can travel 1 square vertically and 2 squares horizontally or visa-versa
    @Override
    public ArrayList<Point> generatePoints(Point p)
    {
        ArrayList<Point> newPoints= new ArrayList<Point>();

        int [] m1={1,-1};

        int [] m2={2,-2};

        knightHelper(newPoints,m1,m2, p);

        knightHelper(newPoints,m2,m1, p);

        return newPoints;
    }

    private void knightHelper(ArrayList<Point> newPoints,int [] rAdd,int []  cAdd, Point p)
    {
        for(int a=0;a<rAdd.length;a++)
        {
            for(int b=0;b<cAdd.length;b++)
            {
                Point p1= new Point(p.getRow()+rAdd[a],p.getColumn()+cAdd[b]);

                if(board.pointOutOfBounds(p1)==false && (board.getPiece(p1)==null ||
                        board.hasEnemyPiece(p1, this)))
                {
                    newPoints.add(p1);
                }
            }
        }
    }

    @Override
    public EmptyChessPiece createDuplicate(ChessBoard newBoard) {

        return new Knight(newBoard,getType());
    }

}

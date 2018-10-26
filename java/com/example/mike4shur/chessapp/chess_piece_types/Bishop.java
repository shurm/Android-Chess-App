package com.example.mike4shur.chessapp.chess_piece_types;

import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;

import java.util.ArrayList;

/**
 *
 * Class which represents the Bishop chess piece
 * it is a subclass of EmptyChessPiece.
 *
 *
 * @author Michael Shur
 *
 */
public class Bishop extends EmptyChessPiece
{

    public Bishop(ChessBoard board,String type)
    {
        super(board,type);
    }


    public String toString()
    {
        return type+"B";
    }

    @Override
    public ArrayList<Point> generatePoints(Point p)
    {
        ArrayList<Point> newPoints= new ArrayList<Point>();
        int[] array={-1,1};

        for(int a=0;a<array.length;a++)
            for(int b=0;b<array.length;b++)
                CommonGenerateMethods.generateLinearPoints(newPoints, this, board, p, array[a],array[b]);


        return newPoints;
    }


    @Override
    public EmptyChessPiece createDuplicate(ChessBoard newBoard)
    {
        return new Bishop(newBoard,getType());
    }


}

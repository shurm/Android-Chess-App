package com.example.mike4shur.chessapp.chess_piece_types;

import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;

import java.util.ArrayList;

/**
 * This is the Rook class for a chess piece
 *
 * @author Michael Shur
 *
 */
public class Rook extends EmptyChessPiece
{
    //checks if rook piece has been moved, because if it has it cannot castle with the King
    private boolean moved =false;

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

    public Rook(ChessBoard board,String type)
    {
        super(board,type);
    }

    public String toString()
    {
        return type+"R";
    }



    @Override
    public ArrayList<Point> generatePoints(Point p)
    {
        ArrayList<Point> newPoints= new ArrayList<Point>();

        int[] array1={1,-1}, array2={0};

        helperGeneratePoints(newPoints, p, array1,array2);

        helperGeneratePoints(newPoints, p, array2,array1);

        return newPoints;
    }
    private void helperGeneratePoints(ArrayList<Point> newPoints, Point p , int [] array1, int [] array2)
    {
        for(int a=0;a<array1.length;a++)
            for(int b=0;b<array2.length;b++)
                CommonGenerateMethods.generateLinearPoints(newPoints, this, board, p, array1[a],array2[b]);

    }

    @Override
    public EmptyChessPiece createDuplicate(ChessBoard newBoard)
    {
        Rook rook = new Rook(newBoard, getType());
        rook.moved=moved;
        return rook;
    }
}

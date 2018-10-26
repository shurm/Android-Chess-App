package com.example.mike4shur.chessapp.chess_piece_types;

import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.geometry.Point;

import java.util.ArrayList;


public class CommonGenerateMethods
{
    public static void generateLinearPoints(ArrayList<Point> newPoints, EmptyChessPiece piece, ChessBoard board, Point p, int rInc, int cInc)
    {
        Point newPoint=new Point(p.getRow()+rInc,p.getColumn()+cInc);
        for(;!board.pointOutOfBounds(newPoint);newPoint=new Point(newPoint.getRow()+rInc,newPoint.getColumn()+cInc))
        {

            if(board.getPiece(newPoint)==null)
                newPoints.add(newPoint);
            else
            {
                if(board.hasEnemyPiece(newPoint, piece))
                    newPoints.add(newPoint);
                break;
            }
        }
    }
}

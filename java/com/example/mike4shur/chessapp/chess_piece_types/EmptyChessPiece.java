package com.example.mike4shur.chessapp.chess_piece_types;

import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.chess.ChessBoard;

import java.util.ArrayList;

/**
 * This is the EmptyChessPiece class
 *
 * @author Michael Shur
 */
public abstract class EmptyChessPiece
{
    protected String type;

    protected ChessBoard board;


    /**
     * Protected constructor for EmptyChessPiece, if type of piece are not black or white
     * we throw an exception
     * @param board
     * @param type
     */
    protected EmptyChessPiece(ChessBoard board,String type)
    {
        if(!type.equals(ChessPieceTypes.white) && !type.equals(ChessPieceTypes.black))
            throw new IllegalArgumentException("Error, the type provided must be a valid type defined in the ChessPieceTypes class");
        this.type=type;
        this.board=board;
    }

    /**
     * Checks if this piece is white
     *
     * @return true if this piece is white
     * @return false otherwise
     */
    public boolean isWhite()
    {
        if(type.equals(ChessPieceTypes.white))
            return true;
        return false;
    }

    /**
     * Checks if this piece is black
     *
     * @return true if this piece is black
     * @return false otherwise
     */
    public boolean isBlack()
    {
        if(type.equals(ChessPieceTypes.black))
            return true;
        return false;
    }

    /**
     * abstract method that should be overridden by every class that extends EmptyChessPiece
     * @return a list of Points, the chess piece should be capable of going to each one of these Points
     */
    public abstract ArrayList<Point> generatePoints(Point p);


    /**
     * template method
     *
     * determines if this chess piece is able to move validly from point p1 to point p2
     *
     * @param p1
     * @param p2
     * @return true if this chess piece is able to move validly from point p1 to point p2
     * @return false otherwise
     */
    public boolean isValidMove(Point p1,Point p2)
    {
        ArrayList<Point> pointsPieceIsAbleToGoTo=this.generatePoints(p1);

        for(int a=0;a<pointsPieceIsAbleToGoTo.size();a++)
        {
            if(p2.equals(pointsPieceIsAbleToGoTo.get(a)))
                return true;
        }
        return false;
    }

    /**
     * abstract method that should be overridden by every class that extends EmptyChessPiece
     *
     * Creates a new Chess Piece
     *
     * the values of this piece's global variables should be copied into the new piece
     *
     * @return a list of Points, the chess piece should be capable of going to each one of these Points
     */
    public abstract EmptyChessPiece createDuplicate(ChessBoard newBoard);


    public String getType()
    {
        return type;
    }


    /**
     * abstract method that should be overridden by every class that extends EmptyChessPiece
     *
     * @return a String representation of the chess piece
     */
    public abstract String toString();

}
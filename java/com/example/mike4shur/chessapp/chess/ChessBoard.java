package com.example.mike4shur.chessapp.chess;

import android.content.Context;
import android.widget.Toast;

import com.example.mike4shur.chessapp.chess_piece_types.*;
import com.example.mike4shur.chessapp.geometry.Point;
import com.example.mike4shur.chessapp.determine_check.BlockCheck;
import com.example.mike4shur.chessapp.determine_check.InCheck;
/**
 * public class which simulates a real life ChessBoard
 *
 * @author Michael Shur
 *
 */
public class ChessBoard
{
    public static final int classicChessBoardDimension=8;

    EmptyChessPiece [][] chessBoard=new EmptyChessPiece [classicChessBoardDimension][classicChessBoardDimension];

    Point whiteKingLocation;

    Point blackKingLocation;


    ChessBoard()
    {

    }

    public boolean objectIsOnChessBoard(Point p)
    {
        if(chessBoard[p.getRow()][p.getColumn()]!=null)
            return true;

        return false;
    }
    public boolean isValidMove(String turn, Point p1,Point p2)
    {
        //bounds check
        if(pointOutOfBounds(p1) || pointOutOfBounds(p2))
            return false;

        //checks if a piece is at your original point
        if(chessBoard[p1.getRow()][p1.getColumn()]==null)
            return false;

        //if white player tries to move a black piece or vice-versa
        if(!turn.substring(0, 1).equalsIgnoreCase(chessBoard[p1.getRow()][p1.getColumn()].getType()))
            return false;

        if(chessBoard[p2.getRow()][p2.getColumn()]!=null &&
                (chessBoard[p1.getRow()][p1.getColumn()].getType().equals(chessBoard[p2.getRow()][p2.getColumn()].getType())))
            return false;

        return chessBoard[p1.getRow()][p1.getColumn()].isValidMove(p1, p2);
    }

    //returns the desired String representation of the chessboard
    public String toString()
    {
        String returnString="";
        for(int r=0;r<classicChessBoardDimension;r++)
        {
            for(int c=0;c<classicChessBoardDimension;c++)
            {
                if(chessBoard[r][c]!=null)
                {
                    returnString+=chessBoard[r][c].toString();
                }
                else
                {
                    if(r%2==0)
                    {
                        if(c%2==0)
                            returnString+="  ";
                        else
                            returnString+="##";
                    }
                    else
                    {
                        if(c%2==0)
                            returnString+="##";
                        else
                            returnString+="  ";
                    }
                }
                returnString+=" ";

            }
            returnString+=(classicChessBoardDimension-r)+"\n";
        }
        for(int c=0;c<classicChessBoardDimension;c++)
        {
            returnString+=" "+(char)('a'+c);

            returnString+=" ";
        }
        return returnString;
    }

    /**
     * Method for checking whether or not a point is in the bounds
     * of the ChessBoard.
     *
     * @param p
     * @return true if point is out of bounds
     * @return false otherwise
     */
    public boolean pointOutOfBounds(Point p)
    {
        if(p.getRow()<0 || p.getRow()>=classicChessBoardDimension || p.getColumn()<0 ||  p.getColumn()>=classicChessBoardDimension)
            return true;

        return false;
    }

    /**
     * Method for retrieving a chess piece that is on the ChessBoard, at Point P
     * as a parameter.
     *
     * @param p
     * @return piece at Point p
     */
    public EmptyChessPiece getPiece(Point p)
    {
        return chessBoard[p.getRow()][p.getColumn()];
    }

    public void setPiece(Point p,EmptyChessPiece piece)
    {
        if(!pointOutOfBounds(p))
            chessBoard[p.getRow()][p.getColumn()]=piece;
    }

    public void movePiece(Point p1, Point p2)
    {
        EmptyChessPiece pieceThatIsGoingToMove=chessBoard[p1.getRow()][p1.getColumn()];

        clearEnPassant();

        chessBoard[p2.getRow()][p2.getColumn()]=chessBoard[p1.getRow()][p1.getColumn()];

        chessBoard[p1.getRow()][p1.getColumn()]=null;

        char c=' ';
        SpecialMoves.makeSpecialChanges(this,pieceThatIsGoingToMove, p1, p2, c);
    }

    /**
     * Method for clearing en passant of all Pawns on the board, by
     * setting en passant to false.
     *
     */
    private void clearEnPassant()
    {
        for(int r=0;r<classicChessBoardDimension;r++)
        {
            for(int c=0;c<classicChessBoardDimension;c++)
            {
                if(chessBoard[r][c]==null && chessBoard[r][c] instanceof Pawn)
                {
                    ((Pawn)(chessBoard[r][c])).setEn_passant(false);
                }
            }
        }
    }

    /**
     * Method for checking whether or not a stalemate has occurred
     *
     *
     * @param king Position of the king that is in possible jeopardy
     * @param enemyType the type the enemy pieces
     * @return true if statemate has occurred
     * @return false otherwise
     */
    public boolean stalemateHasOccurred(Point king, String enemyType)
    {
        if(checkHasOccurred(king, enemyType))
            return false;

        if(BlockCheck.areAbleToBlockCheck(this,king, enemyType))
            return false;

        return true;
    }

    /**
     * Method for checking whether or not checkmate has occurred
     *
     *
     * @param king Position of the king that is in possible jeopardy
     * @param enemyType the type the enemy pieces
     * @return true if checkmate has occurred
     * @return false otherwise
     */
    public boolean checkmateHasOccurred(Point king, String enemyType)
    {
        if(!checkHasOccurred(king, enemyType))
            return false;

        if(BlockCheck.areAbleToBlockCheck(this,king, enemyType))
            return false;

        return true;
    }


    /**
     * Method for checking whether or not a check has occurred
     *
     *
     * @param king Position of the king that is in possible jeopardy
     * @param enemyType the type the enemy pieces
     * @return true if check has occurred
     * @return false otherwise
     */
    public boolean checkHasOccurred(Point king, String enemyType)
    {
        return InCheck.areEnemiesAttackingPoint(this, enemyType,king);
    }


    public boolean hasEnemyPiece(Point p, EmptyChessPiece piece)
    {
        if(getPiece(p)==null)
            return false;
        if(getPiece(p).getType().equals(piece.getType()))
            return false;
        return true;
    }

    /**
     * Checks if the move you just made puts your own king in check
     *
     * @param turn
     * @param p1
     * @param p2
     * @return true if your king is now in check
     * @return false otherwise
     */
    public boolean kingIsInCheck( String turn, Point p1, Point p2)
    {
        ChessBoard newBoard=ChessBoardFactory.createNewChessBoard(this,p1,p2);

        Point kingLocation;
        String enemyType;
        if(turn.equals("White"))
        {
            kingLocation=newBoard.whiteKingLocation;
            enemyType=ChessPieceTypes.black;
        }
        else
        {
            kingLocation=newBoard.blackKingLocation;
            enemyType=ChessPieceTypes.white;
        }

        if(newBoard.checkHasOccurred(kingLocation, enemyType))
            return true;

        return false;
    }

    public Point getKingLocation(String type)
    {
        if(type.equals(ChessPieceTypes.white))
            return new Point(whiteKingLocation.getRow(),whiteKingLocation.getColumn());
        return new Point(blackKingLocation.getRow(),blackKingLocation.getColumn());
    }
}

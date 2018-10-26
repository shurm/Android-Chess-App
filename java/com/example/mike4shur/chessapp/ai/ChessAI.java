package com.example.mike4shur.chessapp.ai;

import android.util.Log;

import com.example.mike4shur.chessapp.PlayingChessActivity;
import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.chess.ChessBoardFactory;
import com.example.mike4shur.chessapp.chess.PairOfPoints;
import com.example.mike4shur.chessapp.chess_piece_types.Bishop;
import com.example.mike4shur.chessapp.chess_piece_types.ChessPieceTypes;
import com.example.mike4shur.chessapp.chess_piece_types.EmptyChessPiece;
import com.example.mike4shur.chessapp.chess_piece_types.Knight;
import com.example.mike4shur.chessapp.chess_piece_types.Pawn;
import com.example.mike4shur.chessapp.chess_piece_types.Queen;
import com.example.mike4shur.chessapp.chess_piece_types.Rook;
import com.example.mike4shur.chessapp.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the ChessAI class
 *
 * it used for single player mode, this is the computer player
 *
 * @author Michael Shur
 *
 */
public class ChessAI
{
    //the type of pieces it can move
    private String type;

    private final int looksAheadMoves=1;
    private PlayingChessActivity activity;

    //the type of pieces it CANNOT move
    private String enemyType;

    //used for recognizing repeat moves
    private int moveCounter=2;
    private PairOfPoints moveFrom2TurnsAgo=null;

    private static final int MAX_VALUE=10000;

    //constructor
    public ChessAI(PlayingChessActivity activity,String type)
    {
        if(!type.equals(ChessPieceTypes.white) && !type.equals(ChessPieceTypes.black))
            throw new IllegalArgumentException("Error, the type provided must be a valid type defined in the ChessPieceTypes class");
        this.type=type;
        this.activity=activity;
        if(type.equals(ChessPieceTypes.white))
            enemyType=ChessPieceTypes.black;
        else
            enemyType=ChessPieceTypes.white;
    }

    //creates an ArrayList of all possible future boards that can arise from making 1 move
    private ArrayList<AIBoardPackage> createPossibleFutureBoards(ChessBoard chessBoard, String type, String enemyType,int turnsAhead)
    {
        ArrayList<AIBoardPackage> newBoards=new ArrayList<AIBoardPackage>();

        createAllPossibleNextMoveChessBoards(newBoards,chessBoard,type, enemyType,turnsAhead);

        Collections.sort(newBoards);

        return newBoards;
    }

    //called when it is time for the computer to make a move
    public PairOfPoints performMove()
    {
      // Log.v("t","\n\n----------------------\n\n");

        ChessBoard board= activity.getBoard();

        ArrayList<AIBoardPackage> newBoards=createPossibleFutureBoards(board,type,enemyType,looksAheadMoves);

        Collections.sort(newBoards);

        for(int a=0;a<newBoards.size();a++)
        {
           // Log.v("tag",newBoards.get(a).heuristicValue+" , "+newBoards.get(a).p1+" , "+newBoards.get(a).p2);
        }

        if(newBoards.size()>1 && newBoards.get(0).heuristicValue==newBoards.get(1).heuristicValue)
        {
            int hValue=newBoards.get(0).heuristicValue;
            for(int a=0;a<newBoards.size();a++)
            {
                if(hValue==newBoards.get(a).heuristicValue)
                {
                    AIBoardPackage boardPackage=newBoards.get(a);

                    ChessBoard board2=newNextMoveBoard(board,board.getPiece(boardPackage.p1),boardPackage.p1,boardPackage.p2);

                    newBoards.get(a).heuristicValue=calculateMobilityHeuristic(board2,type,enemyType) + squarePower(boardPackage.p2)+100;
                }
                else
                    break;
            }

            Collections.sort(newBoards);

        }

        PairOfPoints nextMove=new PairOfPoints(newBoards.get(0).p1,newBoards.get(0).p2);

        // prevents ai from doing same pair of moves over and over again
        moveCounter--;
        if(moveCounter==0)
        {
            if(nextMove.equals(moveFrom2TurnsAgo))
            {
                if(newBoards.size()==1)
                    return null;
                nextMove=new PairOfPoints(newBoards.get(1).p1,newBoards.get(1).p2);
            }
            moveCounter=2;
        }
        return nextMove;
    }

    private int squarePower(Point p2)
    {
        if(p2.getColumn()>=2 && p2.getColumn()<=5 && p2.getRow()>=3 && p2.getRow()<=4)
            return 100;
        return 0;
    }

    private int calculateMobilityHeuristic(ChessBoard board, String type, String enemyType)
    {
        int mobilityScoreForYou=0,mobilityScoreForOpponent=0;
        for(int r=0;r<ChessBoard.classicChessBoardDimension;r++)
        {
            for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
            {
                Point p=new Point (r,c);
                EmptyChessPiece piece=board.getPiece(p);
                if(piece!=null)
                {
                    if(piece.getType().equals(type))
                        mobilityScoreForYou+=piece.generatePoints(p).size();
                    else
                        mobilityScoreForOpponent+=piece.generatePoints(p).size();
                }
            }
        }

        int mob=(mobilityScoreForYou-mobilityScoreForOpponent);

        return mob;

    }

    private void createAllPossibleNextMoveChessBoards(ArrayList<AIBoardPackage> newBoards, ChessBoard chessBoard,
                                                      String type,String enemyType,int turnsAhead)
    {
        for(int r=0;r<ChessBoard.classicChessBoardDimension;r++)
        {
            for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
            {
                Point p=new Point (r,c);

                EmptyChessPiece piece=chessBoard.getPiece(p);
                if(	piece!=null && 	piece.getType().equals(type))
                {
                    //tries to move the piece every way it can be moved
                    tryAllPossibleMoves(newBoards,chessBoard,piece,p,type,enemyType,turnsAhead);
                }
            }
        }
    }
    /**
     * This method attempts to try all possible ways the chessPiece can move
     *
     *
     * @param chessBoard
     * @param chessPiece            the opponent piece on the chessBoard that was found
     * @param p                        the location of the ChessBoard where the chessPiece is located
     *
     *
     * @return true 				if there are possible moves that can block the check
     * @return false 				otherwise
     */
    private  void tryAllPossibleMoves(ArrayList<AIBoardPackage> newBoards, ChessBoard chessBoard,
                                       EmptyChessPiece chessPiece, Point p, String type, String enemyType, int turnsAhead)
    {

        ArrayList<Point> points=chessPiece.generatePoints(p);

        for(int a=0;a<points.size();a++)
        {

            ChessBoard newBoard= newNextMoveBoard(chessBoard,chessPiece, p, points.get(a));

            boolean check=newBoard.checkHasOccurred(newBoard.getKingLocation(type), enemyType);

            if(!check)
                newBoards.add(new AIBoardPackage(p, points.get(a),calculateMaterialHeuristic(newBoard,type, enemyType,turnsAhead)));

        }
    }

    private ChessBoard newNextMoveBoard(ChessBoard chessBoard, EmptyChessPiece chessPiece, Point p1 ,Point p2)
    {
        ChessBoard newBoard= ChessBoardFactory.createNewChessBoard(chessBoard, p1, p2);
        return newBoard;
    }

    private int calculateMaterialHeuristic(ChessBoard board, String type, String enemyType, int turnsAhead)
    {
        //if opponent is now checkmated this is best possible outcome
        if(board.checkmateHasOccurred(board.getKingLocation(enemyType), type))
            return MAX_VALUE;

        int materialScoreForYou=0,materialScoreForOpponent=0;
        for(int r=0;r<ChessBoard.classicChessBoardDimension;r++)
        {
            for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
            {
                Point p=new Point (r,c);
                EmptyChessPiece piece=board.getPiece(p);
                if(piece!=null)
                {
                    if(piece.getType().equals(type))
                        materialScoreForYou+=getPieceScore(piece);
                    else
                        materialScoreForOpponent+=getPieceScore(piece);
                }
            }
        }
		/*
		System.out.println("materialScoreForYou is "+materialScoreForYou);
		System.out.println("materialScoreForOpponent is "+materialScoreForOpponent);
		System.out.println("mobilityScoreForYou is "+mobilityScoreForYou);
		System.out.println("mobilityScoreForOpponent is "+mobilityScoreForOpponent);
		*/

        int material=(materialScoreForYou-materialScoreForOpponent);

        if(turnsAhead==0)
        {
            return material;
        }

       // int mob=(int)(mobWeight*(mobilityScoreForYou-mobilityScoreForOpponent));

        ArrayList<AIBoardPackage> newBoards=createPossibleFutureBoards(board,enemyType,type,turnsAhead-1);
        int worstNext=newBoards.get(0).heuristicValue;


        if(turnsAhead%2!=0)
            worstNext*=(-1);

     //   Log.v("t",board.toString());
       // Log.v("t","worst next is "+worstNext);
        //Log.v("t","from "+newBoards.get(0).p1+" to "+newBoards.get(0).p2+" \n");

        return worstNext;
    }

    private static int getPieceScore(EmptyChessPiece piece)
    {
        if(piece instanceof Queen)
            return 9;
        if(piece instanceof Rook)
            return 5;
        if(piece instanceof Bishop)
            return 3;
        if(piece instanceof Knight)
            return 3;
        if(piece instanceof Pawn)
            return 1;

        return 0;
    }

    private class AIBoardPackage implements Comparable<AIBoardPackage>
    {
        Point p1,p2;
        int heuristicValue;

        public AIBoardPackage(Point p1, Point p2, int i)
        {
            this.p1=p1;
            this.p2=p2;
            heuristicValue=i;
        }

        public String toString()
        {	return heuristicValue+"";}

        @Override
        public int compareTo(AIBoardPackage o)
        {
            return o.heuristicValue-heuristicValue;
        }
    }

}

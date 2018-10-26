package com.example.mike4shur.chessapp.testing;


import com.example.mike4shur.chessapp.ai.ChessAI;
import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.chess.ChessBoardFactory;
import com.example.mike4shur.chessapp.chess_piece_types.EmptyChessPiece;
import com.example.mike4shur.chessapp.geometry.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mike 4 Shur on 9/2/2017.
 */

public class EvalutatePosition
{
    private static final int globalDepth=4;

    public static String alphaBeta(ChessBoard currentBoard, int depth, int beta, int alpha, AIBoardPackage move, int player)
    {
        //String move
        //return in the form of 1234b##########

        List<AIBoardPackage> list=possibleMoves();

        if (depth==0 || list.size()==0)
        {
            return move+(Rating.rating(list.size(), depth)*(player*2-1));
        }

        list=sortMoves(list);
        player=1-player;//either 1 or 0

        for(Iterator<AIBoardPackage> iterator = list.iterator(); iterator.hasNext(); )
        {
            ChessBoardFactory.createNewChessBoard(currentBoard,from,to);

            AIBoardPackage newMove = iterator.next();

            makeMove(newMove);
            flipBoard();
            AIBoardPackage returnString=alphaBeta(depth-1, beta, alpha, newMove, player);
            int value=Integer.valueOf(returnString.substring(5));
            flipBoard();
            undoMove(newMove);
            if (player==0)
            {
                if (value<=beta)
                {
                    beta=value;
                    if (depth==globalDepth)
                    {
                        move=returnString;
                    }
                }
            }
            else
            {
                if (value>alpha)
                {
                    alpha=value;
                    if (depth==globalDepth)
                    {
                        move=returnString;
                    }
                }
            }
            if (alpha>=beta)
            {
                if (player==0)
                {
                    return move+beta;
                }
                else
                {
                    return move+alpha;
                }
            }
        }

        if (player==0)
        {
            return move+beta;
        }
        else
        {
            return move+alpha;
        }
    }

    private static List<AIBoardPackage> sortMoves(List<AIBoardPackage> list)
    {
        int[] score=new int [list.size()];

        int i=0;
        for(Iterator<AIBoardPackage> iterator = list.iterator(); iterator.hasNext();i++)
        {
                AIBoardPackage move=iterator.next();
                makeMove(move);
                score[i] = -Rating.rating(-1, 0);
                undoMove(move);
        }

        List<AIBoardPackage> newListA= new ArrayList<>(), newListB=list;
        for ( i=0;i<Math.min(6, list.size());i++) {//first few moves only
            int max=-1000000, maxLocation=0;
            for (int j=0;j<score.length;j++) {
                if (score[j]>max)
                {
                    max=score[j];
                    maxLocation=j;
                }
            }
            score[maxLocation]=-1000000;
            newListA.add(list.get(maxLocation));
            newListB.remove(maxLocation);
        }
        newListA.addAll(newListB);
        return newListA;
    }

    private static class AIBoardPackage implements Comparable<AIBoardPackage>
    {
        Point p1,p2;
        int heuristicValue;

        public AIBoardPackage(Point p1, Point p2)
        {
            this.p1=p1;
            this.p2=p2;
        }

        public String toString()
        {	return heuristicValue+"";}

        @Override
        public int compareTo(AIBoardPackage o)
        {
            return o.heuristicValue-heuristicValue;
        }
    }

    private static List<AIBoardPackage> possibleMoves(ChessBoard chessBoard, String type)
    {
        List<AIBoardPackage> moves = new ArrayList<>();

        for(int r=0;r<ChessBoard.classicChessBoardDimension;r++)
        {
            for(int c=0;c<ChessBoard.classicChessBoardDimension;c++)
            {
                Point p=new Point (r,c);

                EmptyChessPiece piece=chessBoard.getPiece(p);
                if(	piece!=null && 	piece.getType().equals(type))
                {
                    //tries to move the piece every way it can be moved
                    List<Point> points = piece.generatePoints(p);

                    for(Iterator<Point> iterator = points.iterator(); iterator.hasNext(); )
                    {
                        moves.add(new AIBoardPackage(p,iterator.next()));
                    }
                }
            }
        }
        return moves;
    }
}

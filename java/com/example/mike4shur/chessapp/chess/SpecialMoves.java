package com.example.mike4shur.chessapp.chess;

import com.example.mike4shur.chessapp.chess_piece_types.Bishop;
import com.example.mike4shur.chessapp.chess_piece_types.EmptyChessPiece;
import com.example.mike4shur.chessapp.chess_piece_types.King;
import com.example.mike4shur.chessapp.chess_piece_types.Knight;
import com.example.mike4shur.chessapp.chess_piece_types.Pawn;
import com.example.mike4shur.chessapp.chess_piece_types.Queen;
import com.example.mike4shur.chessapp.chess_piece_types.Rook;
import com.example.mike4shur.chessapp.geometry.Point;


public class SpecialMoves
{
    static void makeSpecialChanges(ChessBoard board, EmptyChessPiece pieceThatIsGoingToMove, Point p1, Point p2, char type)
    {
        //the player is moving a King
        if(pieceThatIsGoingToMove instanceof King)
        {
            if(pieceThatIsGoingToMove.isWhite())
            {
                board.whiteKingLocation.setRow(p2.getRow());
                board.whiteKingLocation.setColumn(p2.getColumn());
            }
            else
            {
                board.blackKingLocation.setRow(p2.getRow());
                board.blackKingLocation.setColumn(p2.getColumn());
            }

            //this King can no longer be castled
            ((King)(pieceThatIsGoingToMove)).setMoved(true);

            //castling
            if(Math.abs(p2.getColumn()-p1.getColumn())==2)
            {
                Rook r;
                if(p2.getColumn()>p1.getColumn())
                {
                    r=(Rook) board.getPiece(new Point(p2.getRow(),ChessBoard.classicChessBoardDimension-1));

                    board.chessBoard[p2.getRow()][ChessBoard.classicChessBoardDimension-1]=null;
                    board.chessBoard[p2.getRow()][5]=r;
                }
                else
                {
                    r=(Rook) board.getPiece(new Point(p2.getRow(),0));
                    board.chessBoard[p2.getRow()][0]=null;
                    board.chessBoard[p2.getRow()][3]=r;
                }
                r.setMoved(true);
            }
        }
        else if (pieceThatIsGoingToMove instanceof Rook)
        {
            ((Rook)(pieceThatIsGoingToMove)).setMoved(true);
        }
        else if (pieceThatIsGoingToMove instanceof Pawn)
        {
            if(Math.abs(p1.getRow()-p2.getRow())==2)
            {
                ((Pawn)(pieceThatIsGoingToMove)).setEn_passant(true);
            }
            // pawn promotion
            else if(p2.getRow()==0 || p2.getRow()==ChessBoard.classicChessBoardDimension-1)
            {
                switch(type)
                {
                    case 'B':
                    {
                        board.chessBoard[p1.getRow()][p1.getColumn()]=new Bishop(board,pieceThatIsGoingToMove.getType());
                        break;
                    }
                    case 'N':
                    {
                        board.chessBoard[p1.getRow()][p1.getColumn()]=new Knight(board,pieceThatIsGoingToMove.getType());
                        break;
                    }
                    case 'R':
                    {
                        board.chessBoard[p1.getRow()][p1.getColumn()]=new Rook(board,pieceThatIsGoingToMove.getType());
                        break;
                    }
                    default:
                    {
                        board.chessBoard[p1.getRow()][p1.getColumn()]=new Queen(board,pieceThatIsGoingToMove.getType());
                    }
                }
            }
            //en passant
            else if(Math.abs(p1.getRow()-p2.getRow())==1 &&
                    Math.abs(p1.getColumn()-p2.getColumn())==1 && board.getPiece(p2)==null)
            {
                board.chessBoard[p1.getRow()][p2.getColumn()]=null;
            }

        }
    }
}

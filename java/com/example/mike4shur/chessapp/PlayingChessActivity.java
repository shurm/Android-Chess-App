package com.example.mike4shur.chessapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike4shur.chessapp.ai.ChessAI;
import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.chess.ChessBoardFactory;
import com.example.mike4shur.chessapp.chess.PairOfPoints;
import com.example.mike4shur.chessapp.chess_piece_types.Bishop;
import com.example.mike4shur.chessapp.chess_piece_types.ChessPieceTypes;
import com.example.mike4shur.chessapp.chess_piece_types.EmptyChessPiece;
import com.example.mike4shur.chessapp.chess_piece_types.King;
import com.example.mike4shur.chessapp.chess_piece_types.Knight;
import com.example.mike4shur.chessapp.chess_piece_types.Pawn;
import com.example.mike4shur.chessapp.chess_piece_types.Queen;
import com.example.mike4shur.chessapp.chess_piece_types.Rook;
import com.example.mike4shur.chessapp.geometry.Point;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.mike4shur.chessapp.chess.ChessBoard.classicChessBoardDimension;


public class PlayingChessActivity extends AppCompatActivity {
    private AtomicBoolean whiteHasLost = new AtomicBoolean(false);
    private AtomicBoolean blackHasLost = new AtomicBoolean(false);
    private AtomicBoolean animationIsHappening=new AtomicBoolean(false);
    private AtomicBoolean undoWasClicked = new AtomicBoolean(false);


    private AtomicBoolean hasAI = new AtomicBoolean(false);
    private AtomicBoolean aisTurn = new AtomicBoolean(false);

    private ChessBoard currentBoard;
    private ChessBoard previousBoard;


    private TextView info;
    private TextView turnText;
    private Point startingPoint = new Point(-1, -1);

    private String turn = "White";

    private ChessAI opponent=null;


    private GraphicsChessBoard chessSquares;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.playing_chess);

        turnText = (TextView) findViewById(R.id.turn);

        info = (TextView) findViewById(R.id.info);

        chessSquares=new GraphicsChessBoard(this,(GridLayout) findViewById(R.id.chessboard),(ImageView)findViewById(R.id.imageView));

        Boolean value=(Boolean)getIntent().getExtras().get("ai");

        hasAI.set(value);

        reset(null);
    }

    public void reset(View v) {
        currentBoard = ChessBoardFactory.createNewChessBoard();

        whiteHasLost.set(false);
        blackHasLost.set(false);
        undoWasClicked.set(false);
        animationIsHappening.set(false);
        aisTurn.set(false);

        startingPoint = new Point(-1, -1);

        turn = "White";
        turnText.setText(turn + "'s turn");
        info.setText("");
        previousBoard = null;

        chessSquares.initialize(currentBoard);

        if(hasAI.get())
            opponent=new ChessAI(this,ChessPieceTypes.black);

    }

    void animationToTrue()
    {
        animationIsHappening.set(true);
    }



    public ChessBoard getBoard()
    {
        return currentBoard;
    }


    public void resign(View v) {

        if (!aisTurn.get() && !animationIsHappening.get()&& !whiteHasLost.get() && !blackHasLost.get()) {
            if (turn.equals("White")) {
                info.setText("White has resigned, Black wins");
                whiteHasLost.set(true);
            } else {
                info.setText("Black has resigned, White wins");
                blackHasLost.set(true);
            }
            displayWinner();
        }
    }

    private void selectNewSquare(Point p) {
        startingPoint.setColumn(p.getColumn());
        startingPoint.setRow(p.getRow());
        chessSquares.selectSquare(p.getRow(), p.getColumn());
        info.setText("");
    }

    private boolean prepareForNextMove()
    {
        boolean b;
        if (!checkIfGameIsOver(currentBoard)) {
            changeTurn();
            b=false;
          //  Log.v("tag","game is NOT over");
        } else {
            displayWinner();
            b=true;

        }
        resetStartingPoint();
        undoWasClicked.set(false);

        return b;
    }

    private void resetStartingPoint() {
        startingPoint.setRow(-1);
        startingPoint.setColumn(-1);
    }

    private void tryToMovePiece(final Point p1, final Point p2)
    {
        /*
        if(!aisTurn.get()) {

            Point wK = currentBoard.getKingLocation(ChessPieceTypes.white);
            King king = (King) currentBoard.getPiece(wK);
            Toast.makeText(getApplicationContext(), "before check " + wK + " (,) " + king.hasBeenMoved(), Toast.LENGTH_SHORT).show();
        }
        */
        if (p1.equals(p2))
        {
            info.setText("Illegal move, try again");
            return;
        }
        if(currentBoard.isValidMove(turn, p1, p2) == false)
        {
            info.setText("Illegal move, try again");
            return;
        }

        if(currentBoard.kingIsInCheck(turn, p1, p2)) {

            info.setText("Illegal move, try again");
            return;
        }


        chessSquares.startAnimation(p1,p2,(ImageView) findViewById(R.id.imageView));
    }

    public void stuffWhenAnimationIsOver(Point p1, final Point p2)
    {
        if(!aisTurn.get()) {

            previousBoard = ChessBoardFactory.createNewChessBoard(currentBoard);
        }

        EmptyChessPiece pieceThatIsGoingToMove = currentBoard.getPiece(p1);
        //the player is moving a King
        if (pieceThatIsGoingToMove instanceof King) {

            //castling
            if (Math.abs(p2.getColumn() - p1.getColumn()) == 2) {
                Point p3,p4;
                if (p2.getColumn() > p1.getColumn()) {
                    p3=new Point(p2.getRow(),classicChessBoardDimension - 1);
                    p4=new Point(p2.getRow(),5);

                } else {
                    p3=new Point(p2.getRow(),0);
                    p4=new Point(p2.getRow(),3);
                }

                chessSquares.movePiece(p3,p4 );
            }
        } else if (pieceThatIsGoingToMove instanceof Pawn) {
            // pawn promotion

            if (p2.getRow() == 0 || p2.getRow() == classicChessBoardDimension - 1) {
                final String[] kk = {"Queen", "Bishop", "Knight", "Rook"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EmptyChessPiece pawnPiece = currentBoard.getPiece(p1);
                builder.setTitle("Pawn Promotion")
                        .setItems(kk, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                promotePawn(kk[which], p2, pawnPiece.getType());
                            }
                        });


                final AlertDialog dialog = builder.create();

                dialog.setCanceledOnTouchOutside(false);

                dialog.show();

            }
            //en passant
            else if (Math.abs(p1.getRow() - p2.getRow()) == 1 &&
                    Math.abs(p1.getColumn() - p2.getColumn()) == 1 && currentBoard.getPiece(p2) == null) {
                chessSquares.makeNull(p1.getRow(),p2.getColumn());

            }
        }
        currentBoard.movePiece(p1, p2);
        chessSquares.movePiece(p1,p2);

        if(!aisTurn.get()) {
            Log.v("tag", p1 + "  to  " + p2);
        }

        animationIsHappening.set(false);

        boolean b=prepareForNextMove();
        if(!b && opponent!=null && !aisTurn.get()) {
            aisTurn.set(true);
            PairOfPoints pts=opponent.performMove();

            //ai cant make any move (stalemate)
            if(pts==null)
            {
                whiteHasLost.set(true);
                blackHasLost.set(true);
                aisTurn.set(false);
                displayWinner();
                return;
            }
            //Log.v("tag","p1 is "+pts.p1+" , p2 is "+pts.p2);

            tryToMovePiece(pts.p1,pts.p2);
        }
        else
            aisTurn.set(false);

       // Log.v("tag",currentBoard.toString());
    }

    private boolean checkIfGameIsOver(ChessBoard chessBoard) {
        Point kingPosition;
        String enemyType;

        //if Black just made a move then the White King is in possible jeopardy
        if (turn.equals("Black")) {
            kingPosition = chessBoard.getKingLocation(ChessPieceTypes.white);
            enemyType = ChessPieceTypes.black;
        } else {
            kingPosition = chessBoard.getKingLocation(ChessPieceTypes.black);
            enemyType = ChessPieceTypes.white;
        }

        if (chessBoard.stalemateHasOccurred(kingPosition, enemyType)) {
            whiteHasLost.set(true);
            blackHasLost.set(true);
            info.setText("Stalemate");
            return true;

        } else if (chessBoard.checkmateHasOccurred(kingPosition, enemyType)) {
            if (turn.equals("Black"))
                whiteHasLost.set(true);
            else
                blackHasLost.set(true);
            info.setText("Checkmate");
            return true;
        } else if (chessBoard.checkHasOccurred(kingPosition, enemyType)) {
            info.setText("Check");
            displayTextDialog("Check");
        } else
            info.setText("");

        return false;
    }



    private void changeTurn() {
        if (turn.equals("White"))
            turn = "Black";
        else
            turn = "White";

        turnText.setText(turn + "'s turn");

        //if single player dont bother announcing whose turn it is
        if(!hasAI.get()) {
            Toast.makeText(getApplicationContext(), "it is now " + turn + "'s turn", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayWinner() {
        String endGameText;
        if (whiteHasLost.get() && blackHasLost.get())
            endGameText="Draw";
        else if (whiteHasLost.get())
            endGameText="Black wins";
        else
            endGameText="White wins";

        turnText.setText(endGameText);

        displayTextDialog(endGameText);
    }
    private void displayTextDialog(String string)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(string);
        final AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void undoLastMove(View v) {
        if(aisTurn.get() || animationIsHappening.get())
            return;

        info.setText("");
        if (previousBoard == null) {
            displayTextDialog("Error, there were no previous moves");
            return;
        }
        if (undoWasClicked.get()) {
            displayTextDialog("Error, cannot undo more than 1 move");
        return;
        }
        currentBoard = ChessBoardFactory.createNewChessBoard(previousBoard);


        chessSquares.refreshImages(currentBoard);

        if(!hasAI.get())
            changeTurn();

        undoWasClicked.set(true);

    }



    public void promotePawn(String pawnPromotionType, Point p1, String pawnType) {
        if (pawnPromotionType != null) {
            if (pawnPromotionType.equals("Queen")) {
                currentBoard.setPiece(p1, new Queen(currentBoard, pawnType));
            } else if (pawnPromotionType.equals("Bishop")) {
                currentBoard.setPiece(p1, new Bishop(currentBoard, pawnType));
            } else if (pawnPromotionType.equals("Knight")) {
                currentBoard.setPiece(p1, new Knight(currentBoard, pawnType));
            } else if (pawnPromotionType.equals("Rook")) {
                currentBoard.setPiece(p1, new Rook(currentBoard, pawnType));
            }
            chessSquares.placeImageOnBoard(p1.getRow(),p1.getColumn(), currentBoard);

        }
    }
    public MyListener newButtonListener(Point p)
    {
        return new MyListener(p);
    }

    class MyListener implements View.OnClickListener
    {
        private Point p;

        MyListener(Point p)
        {
            this.p = p;
        }

        @Override
        public void onClick(View v) {
            //info.setText("you clicked on point "+p.toString());
            if (!whiteHasLost.get() && !blackHasLost.get() && !animationIsHappening.get() && !aisTurn.get()) {

                if (currentBoard.pointOutOfBounds(startingPoint)) {
                    if (currentBoard.getPiece(p) != null) {
                        if (currentBoard.getPiece(p).getType().equalsIgnoreCase(turn.substring(0, 1))) {
                            selectNewSquare(p);
                        } else
                            info.setText("Illegal move, try again");
                    }
                } else {
                    if (p.equals(startingPoint)) {
                        resetStartingPoint();
                        chessSquares.deselectSquare(p.getRow(), p.getColumn());
                    } else if (currentBoard.getPiece(p) != null && currentBoard.getPiece(p).getType().equals(currentBoard.getPiece(startingPoint).getType())) {
                        chessSquares.deselectSquare(startingPoint.getRow(), startingPoint.getColumn());
                        selectNewSquare(p);
                    } else {
                        tryToMovePiece(startingPoint, p);
                    }
                }
            }
        }
    }
}

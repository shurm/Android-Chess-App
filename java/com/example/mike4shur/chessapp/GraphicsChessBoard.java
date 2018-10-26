package com.example.mike4shur.chessapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mike4shur.chessapp.chess.ChessBoard;
import com.example.mike4shur.chessapp.chess_piece_types.Bishop;
import com.example.mike4shur.chessapp.chess_piece_types.EmptyChessPiece;
import com.example.mike4shur.chessapp.chess_piece_types.King;
import com.example.mike4shur.chessapp.chess_piece_types.Knight;
import com.example.mike4shur.chessapp.chess_piece_types.Pawn;
import com.example.mike4shur.chessapp.chess_piece_types.Queen;
import com.example.mike4shur.chessapp.chess_piece_types.Rook;
import com.example.mike4shur.chessapp.geometry.Point;

/**
 * This is the GraphicsChessBoard class it is used to display/manage the images displayed on screen
 *
 * This class's main purpose was to modularize/ divide up the code,
 * also prevents the PlayingChessActivity.java file from being super long
 *
 * @author Michael Shur
 *
 */
public class GraphicsChessBoard {

    //how long the animation will be
    private static final int animationDuration=1000;

    //each square which makes up the chess board
    private LinearLayout[][] squares2 = new LinearLayout[8][8];

    //the chess piece images which go on the chess board squares
    private ImageView[][] squares = new ImageView[8][8];

    //a link to the PlayingChessActivity have created this GraphicsChessBoard
    private PlayingChessActivity main;

    //the size of the individual squares that make up the chessBoard, also the of most of the chess pieces
    private int imageSize;


    private GridLayout chessBoardView;


    public GraphicsChessBoard(PlayingChessActivity main, GridLayout layout,ImageView view) {
        this.main = main;
        this.chessBoardView = layout;
        imageSize = getWidthOfScreen() / 8;
    }

    //unhighlights the specified square
    public void deselectSquare(int r, int c) {
        if ((c + r) % 2 == 0)
            squares2[r][c].setBackgroundDrawable(new BitmapDrawable(main.getResources(), resizeImage(R.drawable.tan_square, imageSize)));
        else
            squares2[r][c].setBackgroundDrawable(new BitmapDrawable(main.getResources(), resizeImage(R.drawable.brown_square, imageSize)));
    }

    //highlights the specified square
    public void selectSquare(int r, int c) {
        if ((c + r) % 2 == 0)
            squares2[r][c].setBackgroundDrawable(new BitmapDrawable(main.getResources(), resizeImage(R.drawable.selected_tan_square, imageSize)));
        else
            squares2[r][c].setBackgroundDrawable(new BitmapDrawable(main.getResources(), resizeImage(R.drawable.selected_brown_square, imageSize)));
    }

    private int getWidthOfScreen() {
        Display display = main.getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        return size.x;
    }

    private LinearLayout createCompositeSquare(int r, int c, ChessBoard chessboard, int imageSize) {
        LinearLayout square = new LinearLayout(main);

        squares2[r][c] = square;

        square.setGravity(Gravity.CENTER);

        if ((c + r) % 2 == 0)
            square.setBackgroundDrawable(new BitmapDrawable(main.getResources(), resizeImage(R.drawable.tan_square, imageSize)));
        else
            square.setBackgroundDrawable(new BitmapDrawable(main.getResources(), resizeImage(R.drawable.brown_square, imageSize)));

        ImageView child = new ImageView(main);

        squares[r][c] = child;

        placeImageOnBoard(r, c, chessboard);

        square.setOnClickListener(main.newButtonListener(new Point(r, c)));

        square.addView(child);

        return square;
    }

    /**
     * method which moves a chess piece for Point p1 on the chess board to Point p2 on the chess board
     *
     * @param p1    The Point(row and column) where the chess piece currently is.
     * @param p2    The Point(row and column) where the chess piece will move to.
     * @param imageView     the image that moves creating the illusion of animation
     */
    public void startAnimation(final Point p1, final Point p2, final ImageView imageView)
    {
        int squareWidth=getWidthOfScreen()/ChessBoard.classicChessBoardDimension;

        int [] coordinates1=getImageCoordinates(squares[p1.getRow()][p1.getColumn()]);

        int[] currentCoordinates=getImageCoordinates(imageView);

        imageView.setImageBitmap(((BitmapDrawable)squares[p1.getRow()][p1.getColumn()].getDrawable()).getBitmap());

        int startingX=coordinates1[0]-currentCoordinates[0];
        int startingY= coordinates1[1]-currentCoordinates[1];

        int endingX=(squareWidth*(p2.getColumn()-p1.getColumn()))+startingX;
        int endingY= (squareWidth*(p2.getRow()-p1.getRow()))+startingY;

        TranslateAnimation movement = new TranslateAnimation(
                startingX, endingX,
                startingY,endingY);

        movement.setDuration(animationDuration);

        movement.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {
                deselectSquare(p1.getRow(),p1.getColumn());

                imageView.setVisibility(View.VISIBLE);

                makeInvisible(p1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                makeVisible(p1);

                imageView.setVisibility(View.INVISIBLE);

                main.stuffWhenAnimationIsOver(p1,p2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        main.animationToTrue();

        imageView.startAnimation(movement);
    }

    private int[] getImageCoordinates(ImageView imageView)
    {
        int[] currentCoordinates=new int[2];

        imageView.getLocationOnScreen(currentCoordinates);

        return currentCoordinates;
    }

    void placeImageOnBoard( int r, int c, ChessBoard chessboard) {
        EmptyChessPiece piece = chessboard.getPiece(new Point(r, c));
        if (piece instanceof King) {
            if (piece.isWhite()) {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.white_king, imageSize));
            } else {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.black_king, imageSize));
            }
        } else if (piece instanceof Queen) {
            if (piece.isWhite()) {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.white_queen, imageSize));
            } else {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.black_queen, imageSize));
            }
        } else if (piece instanceof Pawn) {
            if (piece.isWhite()) {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.white_pawn, (2 * imageSize) / 3));
            } else {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.black_pawn, (2 * imageSize) / 3));
            }
        } else if (piece instanceof Rook) {
            if (piece.isWhite()) {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.white_rook, imageSize));
            } else {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.black_rook, imageSize));
            }
        } else if (piece instanceof Knight) {
            if (piece.isWhite()) {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.white_knight, imageSize));
            } else {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.black_knight, imageSize));
            }
        } else if (piece instanceof Bishop) {
            if (piece.isWhite()) {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.white_bishop, imageSize));
            } else {
                squares[r][c].setImageBitmap(resizeImage(R.drawable.black_bishop, imageSize));
            }
        } else
            squares[r][c].setImageBitmap(null);
    }

    private Bitmap resizeImage(int id, int imageSize) {
        Bitmap bMap = BitmapFactory.decodeResource(main.getResources(), id);
        int width = bMap.getWidth();
        int height = bMap.getHeight();

        double s = ((double) height) / imageSize;
        return Bitmap.createScaledBitmap(bMap, (int) (width / s), imageSize, true);
    }

    public void movePiece(Point p1, Point p2)
    {
        squares[p2.getRow()][p2.getColumn()].setImageBitmap(((BitmapDrawable) squares[p1.getRow()][p1.getColumn()].getDrawable()).getBitmap());

        squares[p1.getRow()][p1.getColumn()].setImageBitmap(null);
    }


    public void initialize(ChessBoard currentBoard)
    {
        chessBoardView.removeAllViews();


        for (int r = 0; r < ChessBoard.classicChessBoardDimension; r++) {
            for (int c = 0; c < ChessBoard.classicChessBoardDimension; c++) {
                chessBoardView.addView(createCompositeSquare(r, c, currentBoard, imageSize));
            }
        }
    }

    public void refreshImages(ChessBoard currentBoard)
    {
        for (int r = 0; r < ChessBoard.classicChessBoardDimension; r++) {
            for (int c = 0; c < ChessBoard.classicChessBoardDimension; c++) {
                placeImageOnBoard(r,c, currentBoard);
            }
        }
    }

    public void makeInvisible(Point p1)
    {
        squares[p1.getRow()][p1.getColumn()].setVisibility(View.INVISIBLE);
    }

    public void makeVisible(Point p1) {
        squares[p1.getRow()][p1.getColumn()].setVisibility(View.VISIBLE);
    }

    public void makeNull(int row, int column) {
        squares[row][column].setImageBitmap(null);
    }
}

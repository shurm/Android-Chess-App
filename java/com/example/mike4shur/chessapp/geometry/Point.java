package com.example.mike4shur.chessapp.geometry;


/**
 * This is the class called Point which is used to determine
 * specific locations on the ChessBoard
 *
 * @author Michael Shur
 *
 */
public class Point
{
    private int r;
    private int c;

    public Point(int r, int c)
    {
        this.setRow(r);
        this.setColumn(c);
    }
    public boolean equals(Point point)
    {
        if(r==point.r && c==point.c)
            return true;
        return false;
    }

    public int getRow() {
        return r;
    }

    public void setRow(int r) {
        this.r = r;
    }

    public int getColumn() {
        return c;
    }

    public void setColumn(int c) {
        this.c = c;
    }

    public String toString()
    {
        return "R:"+r+" , C:"+c;
    }

}

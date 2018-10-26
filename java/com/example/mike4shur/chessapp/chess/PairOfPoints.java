package com.example.mike4shur.chessapp.chess;

import com.example.mike4shur.chessapp.geometry.Point;

/**
 * Created by Mike 4 Shur on 12/25/2016.
 */
public class PairOfPoints
{

    public Point p1;
    public Point p2;

    public PairOfPoints(int r1, int c1, int r2, int c2) {
        p1=new Point(r1,c1);
        p2=new Point(r2,c2);
    }
    public PairOfPoints(Point p12, Point p22) {
        p1=p12;
        p2=p22;
    }

    public boolean equals(PairOfPoints pairOfPoints)
    {
        if(pairOfPoints==null)
            return false;
        if(pairOfPoints.p1.equals(p1) && pairOfPoints.p2.equals(p2))
            return true;

        return false;
    }

}

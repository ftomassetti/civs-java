package me.tomassetti.civs.ui;

import kotlin.Pair;

public class CellCoordinatesCalculator {

    public Pair<Integer, Integer> cellCoords(int normalizedX, int normalizedY, int restX, int restY) {
        int row, col;
        boolean xeven = (normalizedX - normalizedY )% 2 == 0;
        //boolean yeven = normalizedY % 2 == 0;
        //System.out.println("X "+normalizedX+", Y "+normalizedY + " -> xeven " + xeven);
        //System.out.println("   RestX "+restX+", RestY "+restY);
        if (xeven) {
            // diagonal goes from bottom left to top right
            if (restX > restY) {
                //System.out.println("A");
                row = (normalizedX + normalizedY)/2;
                col = (normalizedX - normalizedY)/2;
            } else {
                //System.out.println("B");
                row = (normalizedX + normalizedY)/2;
                col = (normalizedX - normalizedY - 1)/2;
            }
        } else /*if ((normalizedX - normalizedY )% 2 == 1 && normalizedY % 2 == 0)*/ {
            // diagonal goes from top left to bottom right
            if (restX < (64-restY)) {
                //System.out.println("C");
                row = (normalizedX + normalizedY - 1)/2;
                col = (normalizedX - (normalizedY+1))/2;
            } else {
                //System.out.println("D");
                row = (normalizedX + normalizedY+1)/2;
                col = (normalizedX - (normalizedY+1))/2;
            }
        }
        return new Pair<>(row, col);
    }

}

package me.tomassetti.civs.ui;

import kotlin.Pair;

public class CellCoordinatesCalculator {

    // Result is ROW COL
    public Pair<Integer, Integer> findCellCoords(float viewportWidth, float viewportHeight,
                                                  int screenWidth, int screenHeight,
                                                  float cameraX, float cameraY, float zoom,
                                                  int inputX, int inputY) {
        float fx = viewportWidth/screenWidth;
        float fy = viewportHeight/screenHeight;
        float x = cameraX + (inputX * fx - viewportWidth/2) * zoom;
        float y = cameraY + (viewportHeight/2 - inputY * fy) * zoom;
        return findCellCoords(x, y);
    }


    private Pair<Integer, Integer> findCellCoords(float x, float y) {
        int normalizedX = (int)(x/128);
        int normalizedY = (int)(y/64) - 1;
        int restX = (int)(x/2 - normalizedX * 64);
        int restY = (int)(y - normalizedY * 64) - 64;
        if (restY < 0) {
            restY += 64;
        }
        Pair<Integer, Integer> coords = new CellCoordinatesCalculator().cellCoords(normalizedX, normalizedY, restX, restY);
        return coords;
    }

    public Pair<Integer, Integer> cellCoords(int normalizedX, int normalizedY, int restX, int restY) {
        int row, col;
        boolean xeven = (normalizedX - normalizedY )% 2 == 0;
        if (xeven) {
            // diagonal goes from bottom left to top right
            if (restX > restY) {
                row = (normalizedX + normalizedY)/2;
                col = (normalizedX - normalizedY)/2;
            } else {
                row = (normalizedX + normalizedY)/2;
                col = (normalizedX - normalizedY - 1)/2;
            }
        } else {
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

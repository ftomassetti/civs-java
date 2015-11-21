package me.tomassetti.civs.ui;

import kotlin.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CellCoordinatesCalculatorTest {

    @Test
    public void testCellCoords() {
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().cellCoords(0, 0, 12, 1));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().cellCoords(0, -1, 60, 15));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().cellCoords(1, -1, 55, 62));
        assertEquals(new Pair<>(0, 1), new CellCoordinatesCalculator().cellCoords(1, -1, 40, 26));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().cellCoords(2, -1, 7, 63));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().cellCoords(2, 0, 7, 5));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().cellCoords(2, 0, 3, 16));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().cellCoords(2, 0, 30, 37));
        assertEquals(new Pair<>(0, 1), new CellCoordinatesCalculator().cellCoords(2, -1, 9, 45));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().cellCoords(1, -1, 37, 44));
        assertEquals(new Pair<>(2, 0), new CellCoordinatesCalculator().cellCoords(2, 1, 24, 49));
        assertEquals(new Pair<>(2, 1), new CellCoordinatesCalculator().cellCoords(3, 1, 21, 3));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().cellCoords(1, 0, 21, 33));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().cellCoords(1, 0, 43, 13));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().cellCoords(3, 0, 12, 34));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().cellCoords(1, 0, 43, 33));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().cellCoords(1, -1, 44, 54));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().cellCoords(2, -1, 27, 60));
    }
}

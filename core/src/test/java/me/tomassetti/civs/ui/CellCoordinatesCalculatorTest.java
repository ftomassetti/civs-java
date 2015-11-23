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


    @Test
    public void testCellCoordsWithAllParams() {
        //float viewportWidth, float viewportHeight,
        //float screenWidth, float screenHeight,
        //float cameraX, float cameraY, float zoom,
        //float inputX, float inputY
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                14, 417
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                75, 426
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                138, 422
        ));
        assertEquals(new Pair<>(0, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                217, 442
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                246, 417
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                14, 417
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                186, 386
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                130, 357
        ));

        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                162, 361
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                237, 394
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                280, 394
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                329, 354
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                302, 315
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                258, 296
        ));

        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                275, 416
        ));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                373, 380
        ));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                332, 390
        ));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 640, 480,
                320.0f, 240.0f, 1.0f,
                455, 420
        ));

        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                390, 437
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                419, 457
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                452, 436
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                422, 419
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                443, 411
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                469, 433
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                505, 414
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                473, 396
        ));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                503, 437
        ));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                320.0f, 240.0f, 4.0f,
                537, 445
        ));

        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                704.0f, 240.0f, 4.0f,
                236, 437
        ));
        assertEquals(new Pair<>(0, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                704.0f, 240.0f, 4.0f,
                267, 452
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                704.0f, 240.0f, 4.0f,
                323, 429
        ));
        assertEquals(new Pair<>(1, 0), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                704.0f, 240.0f, 4.0f,
                328, 397
        ));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                704.0f, 240.0f, 4.0f,
                406, 438
        ));
        assertEquals(new Pair<>(1, 1), new CellCoordinatesCalculator().findCellCoords(
                640.0f, 480.f, 988, 740,
                704.0f, 240.0f, 4.0f,
                370, 457
        ));
    }
}

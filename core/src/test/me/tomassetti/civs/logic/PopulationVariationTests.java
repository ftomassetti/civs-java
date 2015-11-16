package me.tomassetti.civs.logic;

import me.tomassetti.civs.model.Population;
import org.junit.Test;

import java.util.Random;

import static me.tomassetti.civs.logic.LogicKt.*;

/**
 * We want to ensure that the population vary in a reasonable way.
 */
public class PopulationVariationTests {

    @Test
    public void verifyBirthsAreReasonable() {
        Population pop = new Population(0, 0, 10, 10, 0, 0);
        Random r = new Random(1);
        newBirths(1.0f, pop, r);
    }

}

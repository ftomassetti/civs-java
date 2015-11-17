package me.tomassetti.civs.logic;

import me.tomassetti.civs.model.Population;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.tomassetti.civs.logic.PopulationKt.*;
import static org.junit.Assert.*;

/**
 * We want to ensure that the population vary in a reasonable way.
 */
public class PopulationVariationTests {

    private int min(List<GenderData> data) {
        return data.stream().map(d -> d.total()).min( (a, b) -> Integer.compare(a, b)).get();
    }

    private int max(List<GenderData> data) {
        return data.stream().map(d -> d.total()).max( (a, b) -> Integer.compare(a, b)).get();
    }

    private float average(List<GenderData> data) {
        int total = data.stream().map(d -> d.total()).reduce((a, b) -> a + b).get();
        return ((float)total)/(float)data.size();
    }

    private float ratio(List<GenderData> data) {
        int totalMale = data.stream().map(d -> d.getMale()).reduce((a, b) -> a + b).get();
        int totalFemale = data.stream().map(d -> d.getFemale()).reduce((a, b) -> a + b).get();
        if (totalMale == 0.0f) return 0.0f;
        return ((float)totalMale)/(float)(totalMale+totalFemale);
    }

    private static final float LOW_PROSPERITY = 0.3f;
    private static final float MED_PROSPERITY = 0.6f;
    private static final float HIGH_PROSPERITY = 0.9f;
    private static final int N_SAMPLES = 30;

    private void assertInRange(int value, int min, int max) {
        assertTrue("Value: " + value + ", range: "+min+".."+max, value >= min);
        assertTrue("Value: " + value + ", range: "+min+".."+max, value <= max);
    }

    private void assertInRange(float value, float min, float max) {
        assertTrue("Value: " + value + ", range: "+min+".."+max, value >= min);
        assertTrue("Value: " + value + ", range: "+min+".."+max, value <= max);
    }

    @Test
    public void verifyBirthsAreReasonable() {
        Population pop = new Population(0, 0, 10, 10, 0, 0);

        List<GenderData> dataLow = new ArrayList<>();
        List<GenderData> dataMed = new ArrayList<>();
        List<GenderData> dataHigh = new ArrayList<>();
        for (int i=0;i<N_SAMPLES;i++) {
            dataLow.add(newBirths(LOW_PROSPERITY, pop, new Random(i)));
            dataMed.add(newBirths(MED_PROSPERITY, pop, new Random(i)));
            dataHigh.add(newBirths(HIGH_PROSPERITY, pop, new Random(i)));
        }

        // Check average
        assertInRange(average(dataLow),  25.00f, 40.00f);
        assertInRange(average(dataMed),  30.00f, 45.00f);
        assertInRange(average(dataHigh), 35.00f, 50.00f);

        // Check ratio male/female
        assertInRange(ratio(dataLow), 0.35f, 0.65f);
        assertInRange(ratio(dataMed), 0.35f, 0.65f);
        assertInRange(ratio(dataHigh), 0.35f, 0.65f);
    }

    @Test
    public void verifyChildrenDeathsAreReasonable() {
        Population pop = new Population(10, 10, 0, 0, 0, 0);

        List<GenderData> dataLow = new ArrayList<>();
        List<GenderData> dataMed = new ArrayList<>();
        List<GenderData> dataHigh = new ArrayList<>();
        for (int i=0;i<N_SAMPLES;i++) {
            dataLow.add(childrenDeathsAndGrowth(LOW_PROSPERITY, pop, new Random(i)).getDeaths());
            dataMed.add(childrenDeathsAndGrowth(MED_PROSPERITY, pop, new Random(i)).getDeaths());
            dataHigh.add(childrenDeathsAndGrowth(HIGH_PROSPERITY, pop, new Random(i)).getDeaths());
        }

        // Check average
        assertInRange(average(dataLow),  8.00f, 11.50f);
        assertInRange(average(dataMed),  6.50f, 10.00f);
        assertInRange(average(dataHigh), 5.00f,  8.50f);
    }

    @Test
    public void verifyChildrenGrowthAreReasonable() {
        Population pop = new Population(10, 10, 0, 0, 0, 0);

        List<GenderData> dataLow = new ArrayList<>();
        List<GenderData> dataMed = new ArrayList<>();
        List<GenderData> dataHigh = new ArrayList<>();
        for (int i=0;i<N_SAMPLES;i++) {
            dataLow.add(childrenDeathsAndGrowth(LOW_PROSPERITY, pop, new Random(i)).getGrowth());
            dataMed.add(childrenDeathsAndGrowth(MED_PROSPERITY, pop, new Random(i)).getGrowth());
            dataHigh.add(childrenDeathsAndGrowth(HIGH_PROSPERITY, pop, new Random(i)).getGrowth());
        }

        // Check average
        assertInRange(average(dataLow),  4.00f,  7.00f);
        assertInRange(average(dataMed),  4.75f,  7.75f);
        assertInRange(average(dataHigh), 5.50f,  8.50f);
    }

    @Test
    public void verifyAdultDeathsAreReasonable() {
        Population pop = new Population(0, 0, 10, 10, 0, 0);

        List<GenderData> dataLow = new ArrayList<>();
        List<GenderData> dataMed = new ArrayList<>();
        List<GenderData> dataHigh = new ArrayList<>();
        for (int i=0;i<N_SAMPLES;i++) {
            dataLow.add(adultsDeathsAndGrowth(LOW_PROSPERITY, pop, new Random(i)).getDeaths());
            dataMed.add(adultsDeathsAndGrowth(MED_PROSPERITY, pop, new Random(i)).getDeaths());
            dataHigh.add(adultsDeathsAndGrowth(HIGH_PROSPERITY, pop, new Random(i)).getDeaths());
        }

        // Check average
        assertInRange(average(dataLow),  4.50f, 6.50f);
        assertInRange(average(dataMed),  3.50f, 5.50f);
        assertInRange(average(dataHigh), 2.50f, 4.50f);
    }

    @Test
    public void verifyAdultGrowthAreReasonable() {
        Population pop = new Population(0, 0, 10, 10, 0, 0);

        List<GenderData> dataLow = new ArrayList<>();
        List<GenderData> dataMed = new ArrayList<>();
        List<GenderData> dataHigh = new ArrayList<>();
        for (int i=0;i<N_SAMPLES;i++) {
            dataLow.add(adultsDeathsAndGrowth(LOW_PROSPERITY, pop, new Random(i)).getGrowth());
            dataMed.add(adultsDeathsAndGrowth(MED_PROSPERITY, pop, new Random(i)).getGrowth());
            dataHigh.add(adultsDeathsAndGrowth(HIGH_PROSPERITY, pop, new Random(i)).getGrowth());
        }

        // Check average
        assertInRange(average(dataLow), 3.50f, 5.50f);
        assertInRange(average(dataMed), 4.00f, 6.00f);
        assertInRange(average(dataHigh), 4.50f, 6.50f);
    }

    @Test
    public void verifyOldDeathsAreReasonable() {
        Population pop = new Population(0, 0, 0, 0, 10, 10);

        List<GenderData> dataLow = new ArrayList<>();
        List<GenderData> dataMed = new ArrayList<>();
        List<GenderData> dataHigh = new ArrayList<>();
        for (int i=0;i<N_SAMPLES;i++) {
            dataLow.add(oldDeaths(LOW_PROSPERITY, pop, new Random(i)));
            dataMed.add(oldDeaths(MED_PROSPERITY, pop, new Random(i)));
            dataHigh.add(oldDeaths(HIGH_PROSPERITY, pop, new Random(i)));
        }
        // Check average
        assertInRange(average(dataLow),  8.50f, 10.00f);
        assertInRange(average(dataMed),  7.50f,  9.00f);
        assertInRange(average(dataHigh), 6.50f,  8.00f);
    }

    private final static int N_TURNS_SIMULATION_SHORT = 25;
    private final static int N_TURNS_SIMULATION_MED = 50;

    private Population simulate(Population initialPop, Random random, float prosperity, int nTurns) {
        Population p = initialPop;
        for (int t=0;t<nTurns;t++){
            p = calcPopulationGivenProsperity(prosperity, p, random);
            System.out.println("PROSP " + prosperity + " T "+t+ " : " + p.total()+" -> "+p);
        }
        return p;
    }

    private int nExtinguished(List<Population> populations) {
        return (int)populations.stream().filter(p -> p.extinguished()).count();
    }

    @Test
    public void verifyPopulationEvolutionIsReasonableUnderShortTime() {
        Population pop = new Population(15, 15, 10, 10, 10, 10);

        List<Population> dataLow = new ArrayList<>();
        List<Population> dataMed = new ArrayList<>();
        List<Population> dataHigh = new ArrayList<>();
        for (int i=0;i<N_SAMPLES;i++) {
            dataLow.add(simulate(pop, new Random(i), LOW_PROSPERITY, N_TURNS_SIMULATION_SHORT));
            dataMed.add(simulate(pop, new Random(i), MED_PROSPERITY, N_TURNS_SIMULATION_SHORT));
            dataHigh.add(simulate(pop, new Random(i), HIGH_PROSPERITY, N_TURNS_SIMULATION_SHORT));
        }

        // Check extinguished
        System.out.println("LOW " + nExtinguished(dataLow)/(float)N_SAMPLES);
        System.out.println("MED " + nExtinguished(dataMed)/(float)N_SAMPLES);
        System.out.println("HIG " + nExtinguished(dataHigh)/(float)N_SAMPLES);
        assertInRange(nExtinguished(dataLow)/(float)N_SAMPLES,  0.15f, 0.25f);
        assertInRange(nExtinguished(dataMed)/(float)N_SAMPLES,  0.03f, 0.10f);
        assertInRange(nExtinguished(dataHigh)/(float)N_SAMPLES, 0.00f, 0.05f);
    }

    //Test per vedere modifica media per gruppo (adulti, bambini, vecchi)
    //con 0.55 deve rimanere stabile con meno deve diminuire con piu' deve crescere

}

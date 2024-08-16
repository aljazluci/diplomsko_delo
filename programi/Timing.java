import RangeQuery.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Timing <T>{
    public static void main(String[] args) {
        Random random = new Random();
        timing1D(random);
        timing2D(random);
        timing3D(random);
    }

    public static void timing1D(Random random) {
        for (int kk = 0; kk < 3; kk++) {
            System.out.println(kk);
            OperacijaFenwick<Integer> f = new Utils.Sum();
            int[] dims = {5000};
            Integer[] seznam = new Integer[dims[0]];
            long numberOfOperations = 20000;
            for (int i = 0; i < seznam.length; i++) {
                seznam[i] = random.nextInt(1000) - 500;
            }
            PairTime<Integer>[] pairTimes = case1D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);

            dims[0] = 15000;
            seznam = new Integer[dims[0]];
            for (int i = 0; i < seznam.length; i++) {
                seznam[i] = random.nextInt(1000) - 500;
            }
            pairTimes = case1D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);

            dims[0] = 30000;
            seznam = new Integer[dims[0]];
            for (int i = 0; i < seznam.length; i++) {
                seznam[i] = random.nextInt(1000) - 500;
            }
            pairTimes = case1D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);
        }
    }

    public static void timing2D(Random random) {
        for (int kk = 0; kk < 3; kk++) {
            System.out.println(kk);
            OperacijaFenwick<Integer> f = new Utils.Sum();
            int[] dims = {100, 100};
            Integer[][] seznam = new Integer[dims[0]][dims[1]];
            long numberOfOperations = 10000;
            for (int i = 0; i < seznam.length; i++) {
                for (int j = 0; j < seznam[0].length; j++) {
                    seznam[i][j] = random.nextInt(1000) - 500;
                }
            }
            PairTime<Integer>[] pairTimes = case2D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);

            dims = new int[]{200, 200};
            seznam = new Integer[dims[0]][dims[1]];
            for (int i = 0; i < seznam.length; i++) {
                for (int j = 0; j < seznam[0].length; j++) {
                    seznam[i][j] = random.nextInt(1000) - 500;
                }
            }
            pairTimes = case2D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);

            dims = new int[]{400, 400};
            seznam = new Integer[dims[0]][dims[1]];
            for (int i = 0; i < seznam.length; i++) {
                for (int j = 0; j < seznam[0].length; j++) {
                    seznam[i][j] = random.nextInt(1000) - 500;
                }
            }
            pairTimes = case2D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);
        }
    }

    public static void timing3D(Random random) {
        for (int kk = 0; kk < 3; kk++) {
            System.out.println(kk);
            OperacijaFenwick<Integer> f = new Utils.Sum();
            int[] dims = {20, 20, 20};
            Integer[][][] seznam = new Integer[dims[0]][dims[1]][dims[2]];
            long numberOfOperations = 20000;
            for (int i = 0; i < seznam.length; i++) {
                for (int j = 0; j < seznam[0].length; j++) {
                    for (int k = 0; k < seznam[0][0].length; k++ ){
                        seznam[i][j][k] = random.nextInt(1000) - 500;
                    }
                }
            }
            PairTime<Integer>[] pairTimes = case3D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);

            dims = new int[]{40, 40, 40};
            seznam = new Integer[dims[0]][dims[1]][dims[2]];
            for (int i = 0; i < seznam.length; i++) {
                for (int j = 0; j < seznam[0].length; j++) {
                    for (int k = 0; k < seznam[0][0].length; k++ ){
                        seznam[i][j][k] = random.nextInt(1000) - 500;
                    }
                }
            }
            pairTimes = case3D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);

            dims = new int[]{60, 60, 60};
            seznam = new Integer[dims[0]][dims[1]][dims[2]];
            for (int i = 0; i < seznam.length; i++) {
                for (int j = 0; j < seznam[0].length; j++) {
                    for (int k = 0; k < seznam[0][0].length; k++ ){
                        seznam[i][j][k] = random.nextInt(1000) - 500;
                    }
                }
            }
            pairTimes = case3D(seznam, f, random, dims, numberOfOperations);
            if (kk == 2)
                printIntResults(pairTimes, numberOfOperations, dims);


        }
    }

    static void printIntResults(PairTime<Integer>[] pairTimes, long numberOfOperations, int[] size) {
        System.out.print("Array size:\t");
        System.out.println(Arrays.toString(size));
        for (PairTime<Integer> pairTime : pairTimes) {
            System.out.print(pairTime.pair.name);
            System.out.print(":\tupdate:\t");
            System.out.printf("%08d\tquery:\t%d\n", (pairTime.endTime - pairTime.startTime) / numberOfOperations,
                    (pairTime.endQuery - pairTime.endTime) / numberOfOperations);
        }
    }

    static<T> PairTime<T>[] case2D(T[][] seznam, OperacijaFenwick<T> f, Random random, int[] dims, long noOperations) {
        PairTime<T>[] pairTimes = queriablesTimesArray2D(seznam, f);
        for (PairTime<T> pairTime : pairTimes) {
            // JIT
            for (long i = 0; i < 20000; i++) {
                pairTime.pair.queryable.posodobi(randomUpdateIndex(dims, random), (T)randomT(seznam[0][0], random));
                pairTime.pair.queryable.poizvedi(randomQuery(dims, random));
            }
            pairTime.startTime = System.nanoTime();
            for(long i = 0; i < noOperations; i++) {
                pairTime.pair.queryable.posodobi(randomUpdateIndex(dims, random), (T)randomT(seznam[0][0], random));
            }
            pairTime.endTime = System.nanoTime();
            for (long i = 0; i < noOperations; i++) {
                pairTime.pair.queryable.poizvedi(randomQuery(dims, random));
            }
            pairTime.endQuery = System.nanoTime();
        }
        return pairTimes;
    }

    static<T> PairTime<T>[] case3D(T[][][] seznam, OperacijaFenwick<T> f, Random random, int[] dims, long noOperations) {
        PairTime<T>[] pairTimes = queriablesTimesArray3D(seznam, f);
        for (PairTime<T> pairTime : pairTimes) {
            // JIT
            for (long i = 0; i < 20000; i++) {
                pairTime.pair.queryable.posodobi(randomUpdateIndex(dims, random), (T)randomT(seznam[0][0][0], random));
                pairTime.pair.queryable.poizvedi(randomQuery(dims, random));
            }
            pairTime.startTime = System.nanoTime();
            for(long i = 0; i < noOperations; i++) {
                pairTime.pair.queryable.posodobi(randomUpdateIndex(dims, random), (T)randomT(seznam[0][0][0], random));
            }
            pairTime.endTime = System.nanoTime();
            for (long i = 0; i < noOperations; i++) {
                pairTime.pair.queryable.poizvedi(randomQuery(dims, random));
            }
            pairTime.endQuery = System.nanoTime();
        }
        return pairTimes;
    }

    static<T> PairTime<T>[] case1D(T[] seznam, OperacijaFenwick<T> f, Random random, int[] dims, long noOperations) {
        PairTime<T>[] pairTimes = queriablesTimesArray(seznam, f);
        for (PairTime<T> pairTime : pairTimes) {
            // JIT
            for (long i = 0; i < 20000; i++) {
                pairTime.pair.queryable.posodobi(randomUpdateIndex(dims, random), (T)randomT(seznam[0], random));
                pairTime.pair.queryable.poizvedi(randomQuery(dims, random));
            }
            pairTime.startTime = System.nanoTime();
            for(long i = 0; i < noOperations; i++) {
                pairTime.pair.queryable.posodobi(randomUpdateIndex(dims, random), (T)randomT(seznam[0], random));
            }
            pairTime.endTime = System.nanoTime();
            for (long i = 0; i < noOperations; i++) {
                pairTime.pair.queryable.poizvedi(randomQuery(dims, random));
            }
            pairTime.endQuery = System.nanoTime();
        }
        return pairTimes;
    }

    static Object randomT(Object o, Random random) {
        if (o instanceof Integer) return random.nextInt(1000) - 500;
        if (o instanceof Double) {
            double dbl;
            do {
                dbl = -10.0 + 20.0 * random.nextDouble();
            } while (dbl == 0);
            return dbl;

        }
        return null;
    }

    static <T> PairTime<T>[] queriablesTimesArray(T[] seznam, OperacijaFenwick<T> f) {
        Pair<T>[] pairs = queriablesArray(seznam, f);
        PairTime<T>[] res = (PairTime<T>[]) Array.newInstance(PairTime.class, pairs.length);
        for (int i = 0; i < res.length; i++) {
            res[i] = new PairTime<>(pairs[i], 0,0 ,0);
        }
        return res;
    }

    static <T> PairTime<T>[] queriablesTimesArray2D(T[][] seznam, OperacijaFenwick<T> f) {
        Pair<T>[] pairs = queriablesArray2D(seznam, f);
        PairTime<T>[] res = (PairTime<T>[]) Array.newInstance(PairTime.class, pairs.length);
        for (int i = 0; i < res.length; i++) {
            res[i] = new PairTime<>(pairs[i], 0,0 ,0);
        }
        return res;
    }

    static <T> PairTime<T>[] queriablesTimesArray3D(T[][][] seznam, OperacijaFenwick<T> f) {
        Pair<T>[] pairs = queriablesArray3D(seznam, f);
        PairTime<T>[] res = (PairTime<T>[]) Array.newInstance(PairTime.class, pairs.length);
        for (int i = 0; i < res.length; i++) {
            res[i] = new PairTime<>(pairs[i], 0,0 ,0);
        }
        return res;
    }



    private static int[] randomQuery(int[] dimSizes, Random random) {
        int[] res = new int[dimSizes.length * 2];
        for (int i = 0; i < dimSizes.length; i++) {
            int start = random.nextInt(dimSizes[i]);
            int end = start + random.nextInt(dimSizes[i] - start);
            res[2 * i] = start;
            res[2 * i + 1] = end;
        }
        return res;
    }

    private static int[] randomUpdateIndex(int[] dimSizes, Random random) {
        int[] res = new int[dimSizes.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = random.nextInt(dimSizes[i]);
        }
        return res;
    }

    private static <T> Pair<T>[] queriablesArray3D(T[][][] seznam, OperacijaFenwick<T> f) {
        Pair<T> p1 = new Pair<T>(new FenwickovoDrevo3D<T>(seznam, f), "Segmentno drevo");
        Pair<T> p2 = new Pair<T>(new FenwickovoDrevo3D<T>(seznam, f), "Fenwickovo drevo");
        Pair<T> p3 = new Pair<T>(new Baseline.NaivnoPoizvedovanje3D<T>(seznam, f), "Naivno iterativno");
        Pair<T> p4 = new Pair<T>(new Baseline.NaivnoKumulativnoPoizvedovanje3D<T>(seznam, f), "Naivno Kumulativno");
        Pair<T>[] res = (Pair<T>[]) Array.newInstance(Pair.class, 4);
        res[0] = p2; res[1] = p3; res[2] = p4; res[3] = p1;
        return res;
    }

    //preveri, da so vsi rezultati pozvedovanja enaki
    private static <T> boolean checkEqual(Pair<T>[] pairs, int[] range) {
        T first = pairs[0].queryable.poizvedi(range);
        for(Pair<T> pair: pairs) {
            // System.out.printf("%s %d\t", pair.name, pair.queryable.poizvedi(range));
            // System.out.printf("%d %d:\t%s:\t %d\n", a, b, pair.name, pair.queryable.poizvedi(new int[] {a, b}));
            if (!pair.queryable.poizvedi(range).equals(first)) {
                System.out.print("prva: \t");
                System.out.print(first);
                System.out.printf("\t%s: \t", pair.name);
                System.out.println(pair.queryable.poizvedi(range));
                return false;
            }
        }
        return true;
    }

    private static <T> Pair<T>[] queriablesArray(T[] seznam, OperacijaFenwick<T> f) {
        Pair<T> p1 = new Pair<T>(new SegmentnoDrevo<T>(seznam, f), "Segmentno drevo");
        Pair<T> p2 = new Pair<T>(new FenwickovoDrevo<T>(seznam, f), "Fenwickovo drevo");
        Pair<T> p3 = new Pair<T>(new Baseline.NaivnoPoizvedovanje<T>(seznam, f), "Naivno iterativno");
        Pair<T> p4 = new Pair<T>(new Baseline.NaivnoKumulativnoPoizvedovanje<T>(seznam, f), "Naivno kumulativno");
        Pair<T>[] res = (Pair<T>[]) Array.newInstance(Pair.class, 4);
        res[0] = p1; res[1] = p2; res[2] = p3; res[3] = p4;
        return res;
    }

    private static <T> Pair<T>[] queriablesArray2D(T[][] seznam, OperacijaFenwick<T> f) {
        Pair<T> p1 = new Pair<T>(new SegmentnoDrevo2D<T>(seznam, f), "Segmentno drevo");
        Pair<T> p2 = new Pair<T>(new FenwickovoDrevo2D<T>(seznam, f), "Fenwickovo drevo");
        Pair<T> p3 = new Pair<T>(new Baseline.NaivnoPoizvedovanje2D<T>(seznam, f), "Naivno iterativno:");
        Pair<T> p4 = new Pair<T>(new Baseline.NaivnoKumulativnoPoizvedovanje2D<T>(seznam, f), "Naivno kumulativno");
        Pair<T>[] res = (Pair<T>[]) Array.newInstance(Pair.class, 4);
        res[0] = p2; res[1] = p3; res[2] = p4; res[3] = p1;
        return res;
    }

    private static<T> Pair<T>[] nonFenwickQueriablesArray(T[] seznam, Operacija f) {
        Pair<T> p1 = new Pair<T>(new SegmentnoDrevo<T>(seznam, f), "Segmentno drevo");
        Pair<T> p2 = new Pair<T>(new Baseline.NaivnoPoizvedovanje<T>(seznam, f), "Naivno");

        return (Pair<T>[]) new Object[]{p1, p2};
    }
}

class Pair<T> {
    Queryable<T> queryable;
    String name;
    Pair(Queryable<T> queryable, String name) {
        this.name = name;
        this.queryable = queryable;
    }
}

class PairTime<T> {
    Pair<T> pair;
    long startTime;
    long endTime;
    long endQuery;
    PairTime(Pair<T> pair, long startTime, long endTime, long endQuery) {
        this.pair = pair;
        this.startTime = startTime;
        this.endTime = endTime;
        this.endQuery = endQuery;
    }
}
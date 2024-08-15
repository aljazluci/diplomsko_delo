import RangeQuery.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Timing <T>{
    public static void main(String[] args) {
        // testing1D();
        // testing2D();
        testing3D();
    }

    public static void testing1D() {
        Integer[] seznam = {4, 6, 2, 2, 8, -1, 3, 4, -3, 4, 7, 6, 1};
        OperacijaFenwick<Integer> f = new Utils.Sum();
        Pair<Integer>[] pairs = queriablesArray(seznam, f);
        List<int[]> queryTests = new ArrayList<>();

        for(int i = 0; i < seznam.length; i++) {
            for (int j = i; j < seznam.length; j++) {
                queryTests.add(new int[] {i, j});
            }
        }
        for(int[] range: queryTests) {
            if (!checkEqual(pairs, range)) {
                System.out.printf("WRONG: %d %d", range[0], range[1]);
            }
        }
        Random ran = new Random();
        /*
        for(int i = 0; i < 100; i++) {
            int ind = ran.nextInt(seznam.length);
            int val = ran.nextInt(200) - 100;
            val = val == 0 ? 1 : val;
            System.out.println(val);
            for(Pair p: pairs) {
                p.queryable.posodobi(new int[] {ind}, val);
            }
        }*/
        for(int[] range: queryTests) {
            if (!checkEqual(pairs, range)) {
                System.out.printf("WRONG: %d %d\n", range[0], range[1]);
            }
        }

    }

    public static void testing2D() {
        Integer[][] seznam = {{1, 3, 0, -1, 2, 2}, {7, 9, 3, 2, 1, 0}, {4, 4, 3, -3, -3, 0},
                {2, 0, 9, 3, 1, 2}, {4, 3, -2, 4, 4, 4}, {1, 2, 3, 4, 5, 6}, {1, -1, 2, 2, 2, 3},
                {4, 5, 1, -5, 2, 1}, {1, 2, 0, 0, -2, 4}
        };
        // Integer[][] seznam = {{1,2,3,4},{3,4,5,6},{5,6,7,8},{7,8,9,10}};
        OperacijaFenwick<Integer> f = new Utils.Sum();
        Pair<Integer>[] pairs = queriablesArray2D(seznam, f);
        List<int[]> queryTests = new ArrayList<>();

        for(int i = 0; i < seznam.length; i++) {
            for (int j = i; j < seznam.length; j++) {
                for (int k = 0; k < seznam[0].length; k++) {
                    for (int l = k; l < seznam[0].length; l++) {
                        queryTests.add(new int[] {i, j, k, l});
                    }
                }
            }
        }
        Random ran = new Random();

        for(int i = 0; i < 100; i++) {
            int ind = ran.nextInt(seznam.length); int ind2 = ran.nextInt(seznam[0].length);
            int val = ran.nextInt(200) - 100;
            val = val == 0 ? 1 : val;
            for(Pair<Integer> p: pairs) {
                p.queryable.posodobi(new int[] {ind, ind2}, val);
            }
        }
        for(int[] range: queryTests) {
            if (!checkEqual(pairs, range)) {
                System.out.printf("WRONG: %d %d %d %d\n", range[0], range[1], range[2], range[3]);
            }
        }
        //System.out.println(seznam[8][5]);

    }

    public static void testing3D() {
        Random rand = new Random();
        Integer[][][] seznam = new Integer[11][18][7];
        for (int i = 0; i < seznam.length; i++) {
            for (int j = 0; j < seznam[0].length; j++) {
                for (int k = 0; k < seznam[0][0].length; k++) {
                    seznam[i][j][k] = rand.nextInt(20) - 10;
                }
            }
        }

        OperacijaFenwick<Integer> f = new Utils.Sum();
        Pair<Integer>[] pairs = queriablesArray3D(seznam, f);
        List<int[]> queryTests = new ArrayList<>();
        for(int i = 0; i < seznam.length; i++) {
            for (int j = i; j < seznam.length; j++) {
                for (int k = 0; k < seznam[0].length; k++) {
                    for (int l = k; l < seznam[0].length; l++) {
                        for (int m = 0; m < seznam[0][0].length; m++) {
                            for (int n = m; n < seznam[0][0].length; n++) {
                                queryTests.add(new int[] {i, j, k, l, m, n});
                            }
                        }
                    }
                }
            }
        }
        Random ran = new Random();

        for(int i = 0; i < 100; i++) {
            int ind = ran.nextInt(seznam.length); int ind2 = ran.nextInt(seznam[0].length); int ind3 = ran.nextInt(seznam[0][0].length);
            int val = ran.nextInt(200) - 100;
            val = val == 0 ? 1 : val;
            for(Pair<Integer> p: pairs) {
                p.queryable.posodobi(new int[] {ind, ind2, ind3}, val);
            }
        }
        for(int[] range: queryTests) {
            if (!checkEqual(pairs, range)) {
                System.out.printf("WRONG: %d %d %d %d %d %d\n", range[0], range[1], range[2], range[3], range[4], range[5]);
            }
        }

    }

    private static <T> Pair<T>[] queriablesArray3D(T[][][] seznam, OperacijaFenwick<T> f) {
        Pair<T> p1 = new Pair<T>(new FenwickovoDrevo3D<T>(seznam, f), "Segmentno");
        Pair<T> p2 = new Pair<T>(new FenwickovoDrevo3D<T>(seznam, f), "Fenwickovo drevo");
        Pair<T> p3 = new Pair<T>(new Baseline.NaivnoPoizvedovanje3D<T>(seznam, f), "Naivno");
        Pair<T> p4 = new Pair<T>(new Baseline.NaivnoKumulativnoPoizvedovanje3D<T>(seznam, f), "Kumulativno");
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
        Pair<T> p3 = new Pair<T>(new Baseline.NaivnoPoizvedovanje<T>(seznam, f), "Naivno");
        Pair<T> p4 = new Pair<T>(new Baseline.NaivnoKumulativnoPoizvedovanje<T>(seznam, f), "Naivno kumulativno");
        Pair<T>[] res = (Pair<T>[]) Array.newInstance(Pair.class, 4);
        res[0] = p1; res[1] = p2; res[2] = p3; res[3] = p4;
        return res;
    }

    private static <T> Pair<T>[] queriablesArray2D(T[][] seznam, OperacijaFenwick<T> f) {
        Pair<T> p1 = new Pair<T>(new SegmentnoDrevo2D<T>(seznam, f), "Segmentno drevo");
        Pair<T> p2 = new Pair<T>(new FenwickovoDrevo2D<T>(seznam, f), "Fenwickovo drevo");
        Pair<T> p3 = new Pair<T>(new Baseline.NaivnoPoizvedovanje2D<T>(seznam, f), "Naivno");
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

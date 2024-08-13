package RangeQuery;

import java.lang.reflect.Array;

public class SegmentnoDrevo3D<T> implements Queryable<T>{
    Operacija<T> operacija;
    SegmentnoDrevo<SegmentnoDrevo2D<T>> drevo;
    public SegmentnoDrevo3D(T[][][] seznam, Operacija<T> operacija) {
        // nova zacasna tabela segmentnih dreves
        this.operacija = operacija;
        SegmentnoDrevo2D<T>[] sDs = makeSegmentTreeTable(seznam, operacija);
        this.drevo = new SegmentnoDrevo<>(sDs, new Operacija<SegmentnoDrevo2D<T>> () {
            @Override
            public SegmentnoDrevo2D<T> elVal() {
                T[][] temp = (T[][]) new Object[seznam[0].length];
                for (int i = 0; i < temp.length; i++) {
                    for (int j = 0; j < temp[0].length; j++) {
                        temp[i][j] = operacija.elVal();
                    }
                }
                return new SegmentnoDrevo2D<T>(temp, operacija);
            }

            @Override
            public SegmentnoDrevo2D<T> funkcija(SegmentnoDrevo2D<T> a, SegmentnoDrevo2D<T> b) {
                SegmentnoDrevo2D<T> temp = this.elVal();
                zdruzi(a, b, temp);
                return temp;
            }
        });
    }
    private static<T> SegmentnoDrevo2D<T>[] makeSegmentTreeTable(T[][][] seznam, Operacija<T> operacija) {
        SegmentnoDrevo2D<T>[] sDs = (SegmentnoDrevo2D<T>[]) Array.newInstance(SegmentnoDrevo2D.class, seznam.length);
        for (int i = 0; i < seznam.length; i++) {
            sDs[i] = new SegmentnoDrevo2D<>(seznam[i], operacija);
        }
        return sDs;
    }

    private static<T> void zdruzi(SegmentnoDrevo2D<T> a, SegmentnoDrevo2D<T> b, SegmentnoDrevo2D<T> target) {
        for (int i = 0; i < a.drevo.drevo.length; i++) {
            SegmentnoDrevo2D.zdruzi(a.drevo.drevo[i], b.drevo.drevo[i], target.drevo.drevo[i]);
        }

    }

    @Override
    public T poizvedi(int[] range) {
        int a1 = range[0]; int b1 = range[1]; int a2 = range[2]; int b2 = range[3]; int a3 = range[4]; int b3 = range[4];
        SegmentnoDrevo2D<T> sT = drevo.poizvedi(new int[] {a1, b1});
        return sT.poizvedi(new int[] {a2, b2, a3, b3});
    }

    @Override
    public void posodobi(int[] indices, T val) {
        int i = indices[0]; int j = indices[1]; int k = indices[2];
        drevo.getElement(i).posodobi(new int[]{j, k}, val);
        drevo.posodobi(new int[]{i}, drevo.getElement(i));
    }
}



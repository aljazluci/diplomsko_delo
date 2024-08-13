package RangeQuery;

import java.lang.reflect.Array;

public class SegmentnoDrevo2D<T> implements Queryable<T>{
    Operacija<T> operacija;
    SegmentnoDrevo<SegmentnoDrevo<T>> drevo;
    public SegmentnoDrevo2D(T[][] seznam, Operacija<T> operacija) {
        // nova zacasna tabela segmentnih dreves
        this.operacija = operacija;
        SegmentnoDrevo<T>[] sDs = makeSegmentTreeTable(seznam, operacija);
        this.drevo = new SegmentnoDrevo<>(sDs, new Operacija<SegmentnoDrevo<T>> () {
            @Override
            public SegmentnoDrevo<T> elVal() {
                T[] temp = (T[]) new Object[seznam[0].length];
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = operacija.elVal();
                }
                return new SegmentnoDrevo<T>(temp, operacija);
            }

            @Override
            public SegmentnoDrevo<T> funkcija(SegmentnoDrevo<T> a, SegmentnoDrevo<T> b) {
                SegmentnoDrevo<T> temp = this.elVal();
                zdruzi(a, b, temp);
                return temp;
            }
        });
    }
    private static<T> SegmentnoDrevo<T>[] makeSegmentTreeTable(T[][] seznam, Operacija<T> operacija) {
        SegmentnoDrevo<T>[] sDs = (SegmentnoDrevo<T>[]) Array.newInstance(SegmentnoDrevo.class, seznam.length);
        for (int i = 0; i < seznam.length; i++) {
            sDs[i] = new SegmentnoDrevo<>(seznam[i], operacija);
        }
        return sDs;
    }

    protected static<T> void zdruzi(SegmentnoDrevo<T> a, SegmentnoDrevo<T> b, SegmentnoDrevo<T> target) {
        for(int i = 0; i < a.drevo.length; i++) {
            target.drevo[i] = a.operacija.funkcija(a.drevo[i], b.drevo[i]);
        }
    }

    @Override
    public T poizvedi(int[] range) {
        int a1 = range[0]; int b1 = range[1]; int a2 = range[2]; int b2 = range[3];
        SegmentnoDrevo<T> sT = drevo.poizvedi(new int[] {a1, b1});
        return sT.poizvedi(new int[] {a2, b2});
    }

    @Override
    public void posodobi(int[] indices, T val) {
        int i = indices[0]; int j = indices[1];
        drevo.getElement(i).posodobi(new int[]{j}, val);
        drevo.posodobi(new int[]{i}, drevo.getElement(i));
    }

}



package RangeQuery;

import java.lang.reflect.Array;

import java.util.Arrays;

public class   SegmentnoDrevo2D<T> implements Queryable<T>{
    final Operacija<T> operacija;
    final int[] velOrig; // velikost orig tabele vsake dimenzije, zaokrožena navzgor do najbližje potence 2
    T[][] drevo;


    public SegmentnoDrevo2D(T[][] seznam, Operacija<T> operacija) {
        this.operacija  = operacija;
        this.velOrig = new int[2];
        this.velOrig[0] = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(seznam.length - 1));
        this.velOrig[1] = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(seznam[0].length - 1));
        this.drevo = (T[][]) new Object[2 * velOrig[0]][2*velOrig[1]];
        for (int i = 0; i < this.drevo.length; i++) {
            Arrays.fill(this.drevo[i], operacija.elVal());
        }
        zgradi(seznam, 1, 1);
    }

    public T getElement(int i, int j) {
        return drevo[i + velOrig[0]][j + velOrig[1]];
    }

    // najde minimum na intervalu [lm, dm]
    @Override
    public T poizvedi(int[] range) {
        return _poizvedi(1, 1, range[0], range[1], range[2], range[3], 0, velOrig[0] - 1);
    }

    private T _poizvedi2(int i, int j, int a2, int b2, int lv, int dv) {
        if (a2 <= lv && b2 >= dv) {
            return this.drevo[i][j];
        }
        int sv = (lv + dv) / 2;
        T rezultat = this.operacija.elVal();
        if (a2 <= sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi2(i, L(j), a2, b2, lv, sv));
        }
        if (b2 > sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi2(i, D(j), a2, b2, sv + 1, dv));
        }
        return rezultat;
    }

    private T _poizvedi(int i, int j, int a1, int b1, int a2, int b2, int lv, int dv) {
        if (a1 <= lv && b1 >= dv) {
            return _poizvedi2(i, 1, a2, b2, 0, velOrig[1]-1);
        }
        int sv = (lv + dv) / 2;
        T rezultat = this.operacija.elVal();
        if (a1 <= sv) {
            rezultat = operacija.funkcija(rezultat, _poizvedi(L(i), j, a1, b1, a2, b2, lv, sv));
        }
        if (b1 > sv) {
            rezultat = operacija.funkcija(rezultat, _poizvedi(D(i), j, a1, b1, a2, b2, sv + 1, dv));
        }
        return rezultat;
    }


    @Override
    public void posodobi(int[] indices, T value) {
        int i = indices[0]; int j = indices[1];
        _posodobi(i+velOrig[0], j+velOrig[1], value);
    }

    private void _posodobi(int i, int j, T value) {
        _posodobi2(i, j, value);
        for(i /= 2; i >= 1; i /=2) {
            _posodobi2(i, j, this.operacija.funkcija(this.drevo[L(i)][j], this.drevo[D(i)][j]));
        }
    }
    private void _posodobi2(int i, int j, T value) {
        this.drevo[i][j] = value;
        for(j /= 2; j >= 1; j /=2) {
            this.drevo[i][j] = operacija.funkcija(this.drevo[i][L(j)], this.drevo[i][D(j)]);
        }
    }

    private void zgradi(T[][] seznam, int i, int j) {
        if (i >= velOrig[0]) {
            if (i < seznam.length + velOrig[0]) {
                if (j >= velOrig[1]) {
                    if (j < seznam[0].length + velOrig[1]) {
                        // originalni element
                        this.drevo[i][j] = seznam[i-velOrig[0]][j-velOrig[1]];
                    }else {
                        // pad na koncu v prvi dimenziji
                        this.drevo[i][j] = operacija.elVal();
                    }
                }
                else {
                    zgradi(seznam, i, L(j));
                    zgradi(seznam, i, D(j));
                    this.drevo[i][j] = this.operacija.funkcija(this.drevo[i][L(j)], this.drevo[i][D(j)]);
                }
            }
            else {
                // pad na koncu v drugi dimenziji
                this.drevo[i][j] = this.operacija.elVal();
            }
            return;
        }

        zgradi(seznam, L(i), j);
        zgradi(seznam, D(i), j);
        for (int y = 1; y < this.drevo[0].length; y++) {
            this.drevo[i][y] = operacija.funkcija(this.drevo[L(i)][y], this.drevo[D(i)][y]);
        }
    }

    private static int L(int index) {
        return 2 * index;
    }
    private static int D(int index) {
        return 2 * index + 1;
    }

}
/*
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
*/


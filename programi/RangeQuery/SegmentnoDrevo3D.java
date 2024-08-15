package RangeQuery;

import java.lang.reflect.Array;
import java.util.Arrays;

public class   SegmentnoDrevo3D<T> implements Queryable<T>{
    final Operacija<T> operacija;
    final int[] velOrig; // velikost orig tabele vsake dimenzije, zaokrožena navzgor do najbližje potence 2
    T[][][] drevo;


    public SegmentnoDrevo3D(T[][][] seznam, Operacija<T> operacija) {
        this.operacija  = operacija;
        this.velOrig = new int[2];
        this.velOrig[0] = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(seznam.length - 1));
        this.velOrig[1] = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(seznam[0].length - 1));
        this.velOrig[2] = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(seznam[0][0].length - 1));
        this.drevo = (T[][][]) new Object[2 * velOrig[0]][2*velOrig[1]];
        for (int i = 0; i < this.drevo.length; i++) {
            Arrays.fill(this.drevo[i], operacija.elVal());
        }
        zgradi(seznam, 1, 1, 1);
    }

    public T getElement(int i, int j, int k) {
        return drevo[i + velOrig[0]][j + velOrig[1]][k + velOrig[2]];
    }

    // najde minimum na intervalu [lm, dm]
    @Override
    public T poizvedi(int[] range) {
        return _poizvedi(1, 1, 1, range[0], range[1], range[2], range[3],
                range[4], range[5], 0, velOrig[0] - 1);
    }

    private T _poizvedi3(int i, int j, int k, int a3, int b3, int lv, int dv) {
        if (a3 <= lv && b3 >= dv) {
            return this.drevo[i][j][k];
        }
        int sv = (lv + dv) / 2;
        T rezultat = this.operacija.elVal();
        if (a3 <= sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi3(i, j, L(k), a3, b3, lv, sv));
        }
        if (b3 > sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi3(i, j, D(k), a3, b3, sv + 1, dv));
        }
        return rezultat;
    }

    private T _poizvedi2(int i, int j, int k, int a2, int b2, int a3, int b3, int lv, int dv) {
        if (a2 <= lv && b2 >= dv) {
            return _poizvedi3(i, j, 1, a3, b3, lv, dv);
        }
        int sv = (lv + dv) / 2;
        T rezultat = this.operacija.elVal();
        if (a2 <= sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi2(i, L(j), k, a2, b2, a3, b3, lv, sv));
        }
        if (b2 > sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi2(i, D(j), k, a2, b2, a3, b3, sv + 1, dv));
        }
        return rezultat;
    }

    private T _poizvedi(int i, int j, int k, int a1, int b1, int a2, int b2, int a3, int b3, int lv, int dv) {
        if (a1 <= lv && b1 >= dv) {
            return _poizvedi2(i, 1, 1, a2, b2, a3, b3, 0, velOrig[1]-1);
        }
        int sv = (lv + dv) / 2;
        T rezultat = this.operacija.elVal();
        if (a1 <= sv) {
            rezultat = operacija.funkcija(rezultat, _poizvedi(L(i), j, k, a1, b1, a2, b2, a3, b3, lv, sv));
        }
        if (b1 > sv) {
            rezultat = operacija.funkcija(rezultat, _poizvedi(D(i), j, k, a1, b1, a2, b2, a3, b3, sv + 1, dv));
        }
        return rezultat;
    }


    @Override
    public void posodobi(int[] indices, T value) {
        int i = indices[0]; int j = indices[1]; int k = indices[2];
        _posodobi(i+velOrig[0], j+velOrig[1], k + velOrig[2], value);
    }

    private void _posodobi(int i, int j, int k, T value) {
        _posodobi2(i, j, k, value);
        for(i /= 2; i >= 1; i /=2) {
            _posodobi2(i, j, k, this.operacija.funkcija(this.drevo[L(i)][j][k], this.drevo[D(i)][j][k]));
        }
    }
    private void _posodobi2(int i, int j, int k, T value) {
        _posodobi3(i, j, k, value);
        for(j /= 2; j >= 1; j /=2) {
            _posodobi3(i, j, k, this.operacija.funkcija(this.drevo[i][L(j)][k], this.drevo[i][D(j)][k]));
        }
    }

    private void _posodobi3(int i, int j, int k, T value) {
        this.drevo[i][j][k] = value;
        for(k /= 2; k >= 1; k /=2) {
            this.drevo[i][j][k] = operacija.funkcija(this.drevo[i][j][L(k)], this.drevo[i][j][D(k)]);
        }
    }

    private void zgradi(T[][][] seznam, int i, int j, int k) {
        if (i >= velOrig[0]) {
            if (i < seznam.length + velOrig[0]) {
                if (j >= velOrig[1]) {
                    if (j < seznam[0].length + velOrig[1]) {
                        if (k >= velOrig[2]) {
                            if (k < seznam[0][0].length + velOrig[2]) {
                                this.drevo[i][j][k] = seznam[i-velOrig[0]][j-velOrig[1]][k-velOrig[2]];
                            }
                            else {
                                this.drevo[i][j][k] = operacija.elVal();
                            }
                        } else {
                            zgradi(seznam, i, j, L(k));
                            zgradi(seznam, i, j, D(k));
                            this.drevo[i][j][k] = this.operacija.funkcija(this.drevo[i][j][L(k)], this.drevo[i][j][D(k)]);
                        }
                    } else {
                        this.drevo[i][j][k] = operacija.elVal();
                    }
                }
                else {
                    zgradi(seznam, i, L(j), k);
                    zgradi(seznam, i, D(j), k);
                    this.drevo[i][j][k] = this.operacija.funkcija(this.drevo[i][L(j)][k], this.drevo[i][D(j)][k]);
                }
            }
            else {
                // pad na koncu v drugi dimenziji
                this.drevo[i][j][k] = this.operacija.elVal();
            }
            return;
        }

        zgradi(seznam, L(i), j, k);
        zgradi(seznam, D(i), j, k);
        for (int y = 1; y < this.drevo[0].length; y++) {
            for (int z = 1; z < this.drevo[0][0].length; z++) {
                this.drevo[i][y][z] = operacija.funkcija(this.drevo[L(i)][y][z], this.drevo[D(i)][y][z]);
            }
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


*/
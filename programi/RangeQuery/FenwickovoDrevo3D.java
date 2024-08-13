package RangeQuery;

public class FenwickovoDrevo3D<T> implements Queryable<T> {

    OperacijaFenwick<T> operacija;
    T[][][] drevo;
    public FenwickovoDrevo3D(T[][][] seznam, OperacijaFenwick<T> operacija) {
        this.operacija = operacija;
        this.drevo = (T[][][]) new Object[seznam.length + 1][seznam[0].length+1][seznam[0][0].length + 1];
        for(int i = 0; i < this.drevo.length; i++) {
            for(int j = 0; j < this.drevo[0].length; j++) {
                for (int k = 0; k < this.drevo[0][0].length; k++) {
                    this.drevo[i][j][k] = this.operacija.elVal();
                }
            }
        }
        for (int i = 0; i < seznam.length; i++) {
            for (int j = 0; j < seznam[0].length; j++) {
                for(int k = 0; k < seznam[0][0].length; k++) {
                    this.posodobi(new int[]{i, j, k}, seznam[i][j][k]);
                }
            }
        }
    }

    public T getElement(int i, int j, int k) {
        return poizvedi(new int[]{i,i,j,j,k,k});
    }

    private T sestevek(int i, int j, int k) {
        i += 1; j += 1; k += 1;
        T sum = this.operacija.elVal();
        while (i > 0) {
            int jj = j;
            while (jj > 0) {
                int kk = k;
                while (kk > 0) {
                    sum = this.operacija.funkcija(sum, this.drevo[i][jj][kk]);
                    kk -= lsb(kk);
                }
                jj -= lsb(jj);
            }
            i -= lsb(i);
        }
        return sum;
    }

    public void _posodobi(int i, int j, int k, T delta) {
        i += 1; j += 1; k += 1;
        while (i < this.drevo.length) {
            int jj = j;
            while (jj < this.drevo[0].length) {
                int kk = k;
                while (kk < this.drevo[0][0].length) {
                    this.drevo[i][jj][kk] = this.operacija.funkcija(this.drevo[i][jj][kk], delta);
                    kk += lsb(kk);
                }
                jj += lsb(jj);
            }
            i += lsb(i);
        }
    }

    private static int lsb(int i) {
        return i & - i;
    }

    @Override
    public T poizvedi(int[] range) {
        int i1 = range[0]; int j1 = range[1]; int i2 = range[2]; int j2 = range[3]; int i3 = range[4]; int j3 = range[5];
        T res = this.sestevek(j1, j2, j3);
        if (i1 > 0) {
            res = this.operacija.inverzFunkcija(res,  this.sestevek(i1 - 1, j2, j3));
        }
        if (i2 > 0) {
            res = this.operacija.inverzFunkcija(res, this.sestevek(j1, i2 - 1, j3));
        }
        if (i3 > 0) {
            res = this.operacija.inverzFunkcija(res, this.sestevek(j1, j2, i3 - 1));
        }
        if (i1 > 0 && i2 > 0) {
            res = this.operacija.funkcija(res, this.sestevek(i1 - 1, i2 - 1, j3));
        }
        if (i1 > 0 && i3 > 0) {
            res = this.operacija.funkcija(res, this.sestevek(i1 - 1, j2, i3 - 1));
        }
        if (i2 > 0 && i3 > 0) {
            res = this.operacija.funkcija(res, this.sestevek(j1, i2 - 1, i3 - 1));
        }
        if (i1 > 0 && i2 > 0 && i3 > 0) {
            res = this.operacija.inverzFunkcija(res, this.sestevek(i1 - 1, i2 - 1, i3 - 1));
        }
        return res;
    }

    @Override
    public void posodobi(int[] indices, T val) {
        this._posodobi(indices[0], indices[1], indices[2],
                this.operacija.inverzFunkcija(val, this.getElement(indices[0], indices[1], indices[2])));
    }
}

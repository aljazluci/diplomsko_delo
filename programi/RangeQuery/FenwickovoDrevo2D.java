package RangeQuery;


public class FenwickovoDrevo2D<T> implements Queryable<T> {
    OperacijaFenwick<T> operacija;
    T[][] drevo;
    public FenwickovoDrevo2D(T[][] seznam, OperacijaFenwick<T> operacija) {
        this.operacija = operacija;
        this.drevo = (T[][]) new Object[seznam.length + 1][seznam[0].length+1];
        for(int i = 0; i < this.drevo.length; i++) {
            for(int j = 0; j < this.drevo[0].length; j++) {
                this.drevo[i][j] = this.operacija.elVal();
            }
        }
        for (int i = 0; i < seznam.length; i++) {
            for (int j = 0; j < seznam[0].length; j++) {
                this.posodobi(new int[]{i, j}, seznam[i][j]);
            }
        }
    }

    public T getElement(int i, int j) {
        return poizvedi(new int[]{i,i,j,j});
    }

    private T sestevek(int i, int j) {
        i += 1; j += 1;
        T sum = this.operacija.elVal();
        while (i > 0) {
            int jj = j;
            while (jj > 0) {
                sum = this.operacija.funkcija(sum, this.drevo[i][jj]);
                jj -= lsb(jj);
            }
            i -= lsb(i);
        }
        return sum;
    }

    public void _posodobi(int i, int j, T delta) {
        i += 1; j += 1;
        while (i < this.drevo.length) {
            int jj = j;
            while (jj < this.drevo[0].length) {
                this.drevo[i][jj] = this.operacija.funkcija(this.drevo[i][jj], delta);
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
        int i1 = range[0]; int j1 = range[1]; int i2 = range[2]; int j2 = range[3];
        T res = this.sestevek(j1, j2);
        if (i1 > 0) {
            res = this.operacija.inverzFunkcija(res,  this.sestevek(i1 - 1, j2));
        }
        if (i2 > 0) {
            res = this.operacija.inverzFunkcija(res, this.sestevek(j1, i2 - 1));
        }
        if (i1 > 0 && i2 > 0) {
            res = this.operacija.funkcija(res, this.sestevek(i1 - 1, i2 - 1));
        }
        return res;
    }

    @Override
    public void posodobi(int[] indices, T val) {
        this._posodobi(indices[0], indices[1], this.operacija.inverzFunkcija(val, this.getElement(indices[0], indices[1])));
    }
}

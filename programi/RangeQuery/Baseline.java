package RangeQuery;
public class Baseline {
    public static class NaivnoPoizvedovanje<T> implements Queryable<T> {
        T[] tabela;
        final Operacija<T> operacija;

        public NaivnoPoizvedovanje(T[] tabela, Operacija<T> operacija) {
            this.tabela = (T[]) new Object[tabela.length];
            for(int i = 0; i < tabela.length; i++) {
                this.tabela[i] = tabela[i];
            }
            this.operacija = operacija;
        }

        public T poizvedi(int[] range) {
            int a = range[0]; int b = range[1];
            T res = this.operacija.elVal();
            for (int i = a; i <= b; i++) {
                res = this.operacija.funkcija(res, tabela[i]);
            }
            return res;
        }

        public void posodobi(int[] index, T val) {
            int i = index[0];
            this.tabela[i] = val;
        }
    }

    public static class NaivnoKumulativnoPoizvedovanje<T> implements Queryable<T> {
        T[] tabela;
        final OperacijaFenwick<T> operacija;

        public NaivnoKumulativnoPoizvedovanje(T[] tabela, OperacijaFenwick<T> operacija) {
            this.tabela = (T[])new Object[tabela.length];
            this.tabela[0] = tabela[0];
            for (int i = 1; i < tabela.length; i++) {
                this.tabela[i] = operacija.funkcija(this.tabela[i - 1], tabela[i]);
            }
            this.operacija = operacija;
        }

        public T poizvedi(int[] range) {
            int a = range[0]; int b = range[1];
            if (a == 0) {
                return this.tabela[b];
            }
            return this.operacija.inverzFunkcija(tabela[b], tabela[a - 1]);
        }
        public void posodobi(int[] indices, T val) {
            int i = indices[0];
            if (i == 0) {
                _posodobi(0, this.operacija.inverzFunkcija(val, this.tabela[0]));
                return;
            }
            _posodobi(i, this.operacija.inverzFunkcija(val, this.operacija.inverzFunkcija(tabela[i], tabela[i-1])));
        }
        private void _posodobi(int i, T delta) {
            for (int ii = i; ii < tabela.length; ii++) {
                tabela[ii] = operacija.funkcija(tabela[ii], delta);
            }
        }
    }

    public static class NaivnoPoizvedovanje2D<T> implements Queryable<T>{
        T[][] tabela;
        Operacija<T> operacija;

        public NaivnoPoizvedovanje2D(T[][] tabela, Operacija<T> operacija) {
            this.tabela = tabela;
            this.operacija = operacija;
        }

        @Override
        public T poizvedi(int[] range) {
            int i1 = range[0]; int j1 = range[1]; int i2 = range[2]; int j2 = range[3];
            T res = this.operacija.elVal();
            for (int i = i1; i <= j1; i++) {
                for (int j = i2; j <= j2; j++) {
                    res = this.operacija.funkcija(res, this.tabela[i][j]);
                }
            }
            return res;
        }

        @Override
        public void posodobi(int[] indices, T val) {
            this.tabela[indices[0]][indices[1]] = val;
        }
    }


    public static class NaivnoKumulativnoPoizvedovanje2D<T> implements Queryable<T>{
        T[][] tabela;
        OperacijaFenwick<T> operacija;

        public NaivnoKumulativnoPoizvedovanje2D(T[][] tabela, OperacijaFenwick<T> operacija) {
            this.operacija = operacija;
            narediTabelo(tabela);
        }

        private void narediTabelo(T[][] tabela) {
            this.tabela = (T[][]) new Object[tabela.length][tabela[0].length];
            this.tabela[0][0] = tabela[0][0];
            for (int i = 1; i < tabela[0].length; i++) {
                this.tabela[0][i] = this.operacija.funkcija(this.tabela[0][i - 1], tabela[0][i]);
            }
            for (int i = 1; i < tabela.length; i++) {
                this.tabela[i][0] = this.operacija.funkcija(this.tabela[i - 1][0], tabela[i][0]);
            }
            for (int i = 1; i < tabela.length; i++) {
                for (int j = 1; j < tabela[0].length; j++) {
                    this.tabela[i][j] = this.operacija.funkcija(tabela[i][j], this.tabela[i][j - 1]);
                    T temp = this.operacija.inverzFunkcija(this.tabela[i - 1][j], this.tabela[i - 1][j - 1]);
                    this.tabela[i][j] = this.operacija.funkcija(this.tabela[i][j], temp);
                }
            }
        }

        public T getValue(int i, int j) {
            T res = this.tabela[i][j];
            if (i > 0) {
                res = operacija.inverzFunkcija(res, this.tabela[i-1][j]);
            }
            if (j > 0) {
                res = operacija.inverzFunkcija(res, this.tabela[i][j-1]);
            }
            if (i > 0 && j > 0) {
                res = operacija.funkcija(res, this.tabela[i-1][j-1]);
            }
            return res;
        }

        @Override
        public T poizvedi(int[] range) {
                return this.poizvedi(range[0], range[1], range[2], range[3]);
        }

        @Override
        public void posodobi(int[] indices, T val) {
            this.posodobi(indices[0], indices[1],
                    this.operacija.inverzFunkcija(val, this.getValue(indices[0], indices[1])));
        }


        public T poizvedi(int i1, int j1, int i2, int j2) {
            // i1-j1;i2-j2
            T res = tabela[j1][j2];
            if (i1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i1 - 1][j2]);
            }
            if (i2 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[j1][i2 - 1]);
            }
            if (i1 > 0 && i2 > 0) {
                res = this.operacija.funkcija(res, this.tabela[i1-1][i2-1]);
            }
            return res;
        }

        private void posodobi(int i, int j, T delta) {
            for (int ii = i; ii < this.tabela.length; ii++) {
                for (int jj = j; jj < this.tabela[0].length; jj++) {
                    this.tabela[ii][jj] = this.operacija.funkcija(this.tabela[ii][jj], delta);
                }
            }
        }
    }

    public static class NaivnoPoizvedovanje3D<T> implements Queryable<T> {
        T[][][] tabela;
        Operacija<T> operacija;

        public NaivnoPoizvedovanje3D(T[][][] tabela, Operacija<T> operacija) {
            this.tabela = (T[][][]) new Object[tabela.length][tabela[0].length][tabela[0][0].length];
            for (int i = 0; i < tabela.length; i++) {
                for (int j = 0; j < tabela[0].length; j++) {
                    for (int k = 0; k < tabela[0][0].length; k++) {
                        this.tabela[i][j][k] = tabela[i][j][k];
                    }
                }
            }
            this.operacija = operacija;
        }

        private T _poizvedi(int i1, int j1, int i2, int j2, int i3, int j3) {
            T res = this.operacija.elVal();
            for (int i = i1; i <= j1; i++) {
                for (int j = i2; j <= j2; j++) {
                    for (int k = i3; k <= j3; k++) {
                        res = this.operacija.funkcija(res, this.tabela[i][j][k]);
                    }
                }
            }
            return res;
        }

        @Override
        public T poizvedi(int[] range) {
            return _poizvedi(range[0], range[1], range[2], range[3], range[4], range[5]);
        }

        @Override
        public void posodobi(int[] indices, T val) {
            this.tabela[indices[0]][indices[1]][indices[2]] = val;
        }
    }

    public static class NaivnoKumulativnoPoizvedovanje3D<T> implements Queryable<T> {
        T[][][] tabela;
        OperacijaFenwick<T> operacija;

        public NaivnoKumulativnoPoizvedovanje3D(T[][][] tabela, OperacijaFenwick<T> operacija) {
            this.operacija = operacija;
            zgradiTabelo(tabela);
        }

        private void zgradiTabelo(T[][][] tabela) {
            this.tabela = (T[][][]) new Object[tabela.length][tabela[0].length][tabela[0][0].length];

            for (int i = 0; i < tabela.length; i++) {
                for (int j = 0; j < tabela[0].length; j++) {
                    for (int k = 0; k < tabela[0][0].length; k++) {
                        T res = tabela[i][j][k];

                        if (i > 0) res = this.operacija.funkcija(res, this.tabela[i-1][j][k]);
                        if (j > 0) res = this.operacija.funkcija(res, this.tabela[i][j-1][k]);
                        if (k > 0) res = this.operacija.funkcija(res, this.tabela[i][j][k-1]);

                        if (i > 0 && j > 0) res = this.operacija.inverzFunkcija(res, this.tabela[i-1][j-1][k]);
                        if (i > 0 && k > 0) res = this.operacija.inverzFunkcija(res, this.tabela[i-1][j][k-1]);
                        if (j > 0 && k > 0) res = this.operacija.inverzFunkcija(res, this.tabela[i][j-1][k-1]);

                        if (i > 0 && j > 0 && k > 0) res = this.operacija.funkcija(res, this.tabela[i-1][j-1][k-1]);

                        this.tabela[i][j][k] = res;
                    }
                }
            }
        }

        private void _posodobi(int i, int j, int k, T delta) {
            for (int ii = i; ii < tabela.length; ii++) {
                for (int jj = j; jj < tabela[0].length; jj++) {
                    for (int kk = k; kk < tabela[0][0].length; kk++) {
                        this.tabela[ii][jj][kk] = this.operacija.funkcija(this.tabela[ii][jj][kk], delta);
                    }
                }
            }
        }

        private T _poizvedi(int i1, int j1, int i2, int j2, int i3, int j3) {

            T res = this.tabela[j1][j2][j3];
            if (i1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i1 - 1][j2][j3]);
            }
            if (i2 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[j1][i2 - 1][j3]);
            }
            if (i3 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[j1][j2][i3 - 1]);
            }
            if (i1 > 0 && i2 > 0) {
                res = this.operacija.funkcija(res, this.tabela[i1 - 1][i2 - 1][j3]);
            }
            if (i1 > 0 && i3 > 0) {
                res = this.operacija.funkcija(res, this.tabela[i1 - 1][j2][i3 - 1]);
            }
            if (i2 > 0 && i3 > 0) {
                res = this.operacija.funkcija(res, this.tabela[j1][i2 - 1][i3 - 1]);
            }
            if (i1 > 0 && i2 > 0 && i3 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i1 - 1][i2 - 1][i3 - 1]);
            }
            return res;
        }

        @Override
        public T poizvedi(int[] range) {
            return this._poizvedi(range[0], range[1], range[2], range[3], range[4], range[5]);
        }

        @Override
        public void posodobi(int[] indices, T val) {
            this._posodobi(indices[0], indices[1], indices[2], this.operacija.inverzFunkcija(val,
                    this.getValue(indices[0], indices[1], indices[2])));
        }

        public T getValue(int i, int j, int k) {
            return this._poizvedi(i, i, j, j, k, k);
        }
    }
}

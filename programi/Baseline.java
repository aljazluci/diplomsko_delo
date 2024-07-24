public class Baseline {
    class NaivnoPoizvedovanje {
        int[] tabela;
        final Operacija operacija;

        public NaivnoPoizvedovanje(int[] tabela, Operacija operacija) {
            this.tabela = tabela;
            this.operacija = operacija;
        }

        public int poizvedi(int a, int b) {
            int res = this.operacija.elVal;
            for (int i = a; i <= b; i++) {
                res = this.operacija.funkcija(res, tabela[i]);
            }
            return res;
        }

        public void posodobi(int i, int val) {
            this.tabela[i] = val;
        }
    }

    class NaivnoKumulativnoPoizvedovanje {
        int[] tabela;
        final OperacijaFenwick operacija;

        public NaivnoKumulativnoPoizvedovanje(int[] tabela, OperacijaFenwick operacija) {
            this.tabela = new int[tabela.length];
            this.tabela[0] = tabela[0];
            for (int i = 1; i < tabela.length; i++) {
                this.tabela[i] = operacija.funkcija(this.tabela[i - 1], tabela[i]);
            }
            this.operacija = operacija;
        }

        public int poizvedi(int a, int b) {
            return this.operacija.inverzFunkcija(tabela[b], tabela[a]);
        }

        public void posodobi(int i, int delta) {
            for (int ii = i; ii < tabela.length; ii++) {
                tabela[ii] = operacija.funkcija(tabela[ii], delta);
            }
        }
    }

    class NaivnoPoizvedovanje2D {
        int[][] tabela;
        Operacija operacija;
        public NaivnoPoizvedovanje2D(int[][] tabela, Operacija operacija) {
            this.tabela = tabela;
            this.operacija = operacija;
        }
        public void posodobi(int i, int j, int val) {
            this.tabela[i][j] = val;
        }
        public int poizvedi(int i1, int j1, int i2, int j2) {
            int res = 0;
            for(int i = i1; i <= i2; i++) {
                for(int j = j1; j <= j2; j++) {
                    res = this.operacija.funkcija(res, this.tabela[i][j]);
                }
            }
            return res;
        }
    }

    class NaivnoKumulativnoPoizvedovanje2D {
        int[][] tabela;
        OperacijaFenwick operacija;
        public NaivnoKumulativnoPoizvedovanje2D(int[][] tabela, OperacijaFenwick operacija) {
            this.operacija = operacija;
            narediTabelo(tabela);
        }
        private void narediTabelo(int[][] tabela) {
            this.tabela = new int[tabela.length][tabela[0].length];
            this.tabela[0][0] = tabela[0][0];
            for (int i = 1; i < tabela.length; i++) {
                this.tabela[0][i] = this.operacija.funkcija(this.tabela[0][i - 1], tabela[0][i]);
            }
            for (int i = 1; i < tabela[0].length; i++) {
                this.tabela[i][0] = this.operacija.funkcija(this.tabela[i - 1][0], tabela[i][0]);
            }
            for(int i = 1; i < tabela.length; i++) {
                for(int j = 1; j < tabela[0].length; j++) {
                    this.tabela[i][j] = this.operacija.funkcija(tabela[i][j], this.tabela[i][j-1]);
                    int temp = this.operacija.inverzFunkcija(this.tabela[i-1][j], this.tabela[i-1][j-1]);
                    this.tabela[i][j] = this.operacija.funkcija(this.tabela[i][j], temp);
                }
            }
        }
        public int poizvedi(int i1, int j1, int i2, int j2) {
            int res = tabela[i2][j2];
            if (i1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i1 - 1][j2]);
            }
            if (j1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i2][j1 - 1]);
            }
            if (j1 > 0 && i1 > 0) {
                res = this.operacija.funkcija(res, this.tabela[i1][j1]);
            }
            return res;
        }
        public void posodobi(int i, int j, int delta) {
            for(int ii = i; i < this.tabela.length; i++) {
                for(int jj = j; j < this.tabela[0].length; j++) {
                    this.tabela[ii][jj] = this.operacija.funkcija(this.tabela[ii][jj], delta);
                }
            }
        }
    }

    class NaivnoPoizvedovanje3D {
        int[][][] tabela;
        Operacija operacija;

        public NaivnoPoizvedovanje3D(int[][][] tabela, Operacija operacija) {
            this.tabela = tabela;
            this.operacija = operacija;
        }
        public void posodobi(int i, int j, int k, int val) {
            this.tabela[i][j][k] = val;
        }
        public int poizvedi(int i1, int j1, int k1, int i2, int j2, int k2) {
            int res = this.operacija.elVal;
            for(int i = i1; i <= i2; i++) {
                for(int j = j1; j <= j2; j++) {
                    for(int k = k1; k <= k2; k++) {
                        res = this.operacija.funkcija(res, this.tabela[i][j][k]);
                    }
                }
            }
            return res;
        }
    }

    class NaivnoKumulativnoPoizvedovanje3D {
        int[][][] tabela;
        OperacijaFenwick operacija;
        public NaivnoKumulativnoPoizvedovanje3D(int[][][] tabela, OperacijaFenwick operacija) {
            zgradiTabelo(tabela);
            this.operacija = operacija;
        }
        private void zgradiTabelo(int[][][] tabela) {
            this.tabela = new int[tabela.length][tabela[0].length][tabela[0][0].length];
            this.tabela[0][0][0] = tabela[0][0][0];
            for(int i = 1; i < tabela.length; i++) {
                this.tabela[i][0][0] = this.operacija.funkcija(this.tabela[i-1][0][0], tabela[i][0][0]);
            }
            for(int j = 1; j < tabela[0].length; j++) {
                this.tabela[0][j][0] = this.operacija.funkcija(this.tabela[0][j-1][0], tabela[0][j][0]);
            }
            for(int k = 1; k < tabela[0][0].length; k++) {
                this.tabela[0][0][k] = this.operacija.funkcija(this.tabela[0][0][k-1], tabela[0][0][k]);
            }
            for(int i = 1; i < tabela.length; i++) {
                for(int j = 1; j < tabela[0].length; j++) {
                    for(int k = 1; k < tabela[0][0].length; k++) {
                        int res = this.operacija.funkcija(tabela[i][j][k], this.tabela[i-1][j][k]);
                        int temp1 = this.operacija.funkcija(this.tabela[i][j-1][k], this.tabela[i][j][k-1]);
                        int temp2 = this.operacija.inverzFunkcija(this.tabela[i-1][j-1][k-1], this.tabela[i-1][j-1][k]);
                        res = this.operacija.inverzFunkcija(res, this.tabela[i-1][j][k-1]);
                        res = this.operacija.inverzFunkcija(res, this.tabela[i][j-1][k-1]);
                        res = this.operacija.funkcija(res, temp1);
                        res = this.operacija.funkcija(res, temp2);
                        this.tabela[i][j][k] = res;
                    }
                }
            }
        }
        public void posodobi(int i, int j, int k, int delta) {
            for(int ii = i; ii < tabela.length; ii++) {
                for(int jj = j; jj < tabela[0].length; jj++) {
                    for(int kk = k; kk < tabela[0][0].length; kk++) {
                        this.tabela[ii][jj][kk] = this.operacija.funkcija(this.tabela[ii][jj][kk], delta);
                    }
                }
            }
        }

        public int poizvedi(int i1, int j1, int k1, int i2, int j2, int k2) {
            int res = this.tabela[i2][j2][k2];
            if(i1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i1-1][j2][k2]);
            }
            if(j1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i2][j1 - 1][k2]);
            }
            if(k1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i2][j2][k1 - 1]);
            }
            if (i1 > 0 && j1 > 0) {
                res = this.operacija.funkcija(res, this.tabela[i1 - 1][j1 - 1][k2]);
            }
            if (i1 > 0 && k1 > 0) {
                res = this.operacija.funkcija(res, this.tabela[i1 - 1][j2][k1 - 1]);
            }
            if (j1 > 0 && k1 > 0) {
                res = this.operacija.funkcija(res, this.tabela[i2][j1 - 1][k1 - 1]);
            }
            if(i1 > 0 && j1 > 0 && k1 > 0) {
                res = this.operacija.inverzFunkcija(res, this.tabela[i1 - 1][j1 - 1][k1 - 1]);
            }
            return res;
        }
    }

}

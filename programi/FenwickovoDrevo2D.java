public class FenwickovoDrevo2D {
    OperacijaFenwick operacija;
    int[][] drevo;
    public FenwickovoDrevo2D(int[][] seznam, OperacijaFenwick operacija) {
        this.operacija = operacija;
        this.drevo = new int[seznam.length + 1][seznam[0].length];
        for(int i = 0; i < seznam.length; i++) {
            for(int j = 0; j < seznam[0].length; j++) {
                this.posodobi(i+1, j+1, seznam[i][j]);
            }
        }
    }

    private int sestevek(int i, int j) {
        int sum = 0;
        int jj = j;
        while (jj > 0) {
            int ii = i;
        }
        return -1;
    }

    public int poizvedi(int i1, int j1, int i2, int j2) {
        return -1;
    }

    public void posodobi(int i, int j, int vrednost) {
        while (i < this.drevo.length) {
            int jj = j;
            while (jj < this.drevo[0].length) {
                this.drevo[i][jj] = this.operacija.funkcija(this.drevo[i][jj], vrednost);
                jj += lsb(jj);
            }
            i += lsb(i);
        }
    }

    private static int lsb(int i) {
        return i & - i;
    }

}

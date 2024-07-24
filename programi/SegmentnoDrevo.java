import java.util.Arrays;

public class SegmentnoDrevo {
    final Operacija operacija;
    final int velOrig; // velikost orig tabele, zaokrožena navzgor do najbližje potence 2
    int[] drevo;

    public SegmentnoDrevo(int[] seznam, Operacija operacija) {
        this.operacija  = operacija;
        this.velOrig = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(seznam.length - 1));
        this.drevo = new int[2 * velOrig];
        Arrays.fill(drevo, operacija.elVal);
        zgradi(seznam, 1);
    }

    // najde minimum na intervalu [lm, dm]
    public int poizvedi(int a, int b) {
        return _poizvedi(1, a, b, 0, velOrig - 1);
    }

    // poizvedba [a,b]
    // trenutno pokritje [lv, dv]
    private int _poizvedi(int index, int a, int b, int lv, int dv) {
        if (a <= lv && b >= dv) {
            return this.drevo[index];
        }
        int sv = (lv + dv) / 2;
        int rezultat = this.operacija.elVal;
        // se prekriva z levim
        if (a <= sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi(L(index), a, b, lv, sv));
        }
        if (b > sv) {
            rezultat = this.operacija.funkcija(rezultat, _poizvedi(D(index), a, b, sv + 1, dv));
        }
        return rezultat;
    }

    public void posodobi(int index, int value) {
        _posodobi(index + velOrig, value);
    }

    private void _posodobi(int index, int value) {
        this.drevo[index] = operacija.funkcija(this.drevo[index], value);
        if (index == 1) return;
        _posodobi(index / 2, value);
    }

    private void zgradi(int[] seznam, int index) {
        if (index >= velOrig) {
            if (index < seznam.length + velOrig) {
                this.drevo[index] = seznam[index - velOrig];
            }
            else this.drevo[index] = operacija.elVal;
            return;
        }
        zgradi(seznam, L(index));
        zgradi(seznam, D(index));
        this.drevo[index] = operacija.funkcija(this.drevo[L(index)], this.drevo[D(index)]);
    }

    private static int L(int index) {
        return 2 * index;
    }
    private static int D(int index) {
        return 2 * index + 1;
    }

    public static void main(String[] args) {
        Operacija minimum = new Minimum();
        SegmentnoDrevo sD = new SegmentnoDrevo(new int[] {20, 15, 27, 13, 7, 14, 11, 18, 25, 16, 19}, minimum);
        System.out.println(Arrays.toString(sD.drevo));
        System.out.println(sD.poizvedi(2, 8));
    }
}



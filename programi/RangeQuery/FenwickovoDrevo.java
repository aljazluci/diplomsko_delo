package RangeQuery;

public class FenwickovoDrevo<T> implements Queryable<T>{
    /*
    *   Fenwickovo drevo v 1D za operacijo seštevanje
    */
    T[] drevo;
    OperacijaFenwick<T> operacija;
    public FenwickovoDrevo(T[] seznam, OperacijaFenwick<T> operacija) {
        drevo = (T[])new Object[seznam.length + 1];
        for(int i = 0; i < drevo.length; i++) {
            drevo[i] = operacija.elVal();
        }
        this.operacija = operacija;
        zgradi(seznam);
    }

    // najvecja potenca st 2
    private static int lsb(int i) {
        return i & - i;
    }

    private void zgradi(T[] seznam) {
        for (int i = 0; i < seznam.length; i++) {
            posodobi(new int[] {i}, seznam[i]);
        }
    }

    public void posodobi(int[] ind, T val) {
        int i = ind[0];
        if(i == 0) {
            _posodobi(i, this.operacija.inverzFunkcija(val, this.poizvedi(i)));
            return;
        }
        T k = this.operacija.inverzFunkcija(this.poizvedi(i), this.poizvedi(i-1));
        _posodobi(i, this.operacija.inverzFunkcija(val, k));
    }

    private void _posodobi(int index, T delta) {
        index += 1;
        while(index < this.drevo.length) {
            // this.drevo[index] += vrednost;
            this.drevo[index] = this.operacija.funkcija(this.drevo[index], delta);
            index += lsb(index);
        }
    }

    public T poizvedi(int[] range) {
        int a = range[0]; int b = range[1];
        if (a == 0) {
            return this.poizvedi(b);
        }
        return this.operacija.inverzFunkcija(this.poizvedi(b), this.poizvedi(a - 1));
    }

    private T poizvedi(int index) {
        //int sum = 0;
        T sum = this.operacija.elVal();
        index += 1;
        while (index > 0) {
            // sum += this.drevo[index];
            sum = this.operacija.funkcija(this.drevo[index], sum);
            index -= lsb(index);
        }
        return sum;
    }

    public static void main(String[] args) {
        // RangeQuery.FenwickovoDrevo fD = new RangeQuery.FenwickovoDrevo(new int[]{2,1,1,3,2,3,4,5,6,7,8,9});
        // System.out.println(Arrays.toString(fD.drevo));
    }
}

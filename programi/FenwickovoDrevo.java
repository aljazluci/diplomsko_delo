import java.util.Arrays;

public class FenwickovoDrevo {
    /*
    *   Fenwickovo drevo v 1D za operacijo seštevanje
    */
    int[] drevo;
    public FenwickovoDrevo(int[] seznam) {
        drevo = new int[seznam.length + 1];
        zgradi(seznam);
    }

    // najvecja potenca st 2
    private static int lsb(int i) {
        return i & - i;
    }

    private void zgradi(int[] seznam) {
        for (int i = 0; i < seznam.length; i++) {
            posodobi(i, seznam[i]);
        }
    }

    public void posodobi(int index, int vrednost) {
        index += 1;
        while(index < this.drevo.length) {
            this.drevo[index] += vrednost;
            index += lsb(index);
        }
    }

    public int poizvedi(int index) {
        int sum = 0;
        index += 1;
        while (index > 0) {
            sum += this.drevo[index];
            index -= lsb(index);
        }
        return sum;
    }

    public static void main(String[] args) {
        FenwickovoDrevo fD = new FenwickovoDrevo(new int[]{2,1,1,3,2,3,4,5,6,7,8,9});
        System.out.println(Arrays.toString(fD.drevo));
    }
}

import java.util.Arrays;

public class SegmentnoDrevoKD {
    Operacija operacija;
    int[] drevo;
    // TODO: kumulativno
    int[] velOrig;
    // seznam 1D array
    // dimenzije so velikosti dimenzij
    public SegmentnoDrevoKD(int[] seznam, int[] dimenzije, Operacija operacija) {
        this.operacija = operacija;
        velOrig = new int[dimenzije.length];
        int velikost = 1;
        for (int i = 0; i < dimenzije.length; i++) {
            velOrig[i] = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(dimenzije[i] - 1));
            velikost *= 2 * velOrig[i];
        }
        this.drevo = new int[velikost];
        Arrays.fill(drevo, operacija.elVal);
        populirajDrevo(seznam,  dimenzije);
        zgradi(seznam, dimenzije, produktZadnjihDimenzij(velOrig.length - 1), 0);
    }

    // nivo se nanasa na dimenzije
    // deluje samo na indeksih zacetka drevesa
    private int L(int index, int nivo) {
        // O(k)
        return index % (produktZadnjihDimenzij(nivo)) + index;
    }
    private int D(int index, int nivo) {
        // O(k)
        return L(index, nivo) + produktZadnjihDimenzij(nivo + 1);
    }

    private void zgradi(int[] seznam, int[] dimenzije, int index, int nivo) {
        if ((nivo >= velOrig.length) || (index >= drevo.length)) return;

        if (index % produktZadnjihDimenzij(nivo) >= produktZadnjihDimenzij(nivo) / 2) {
            // System.out.printf("index: %d\t produkt: %d\n", index, produktZadnjihDimenzij(nivo + 2));
            zgradi(seznam, dimenzije, index + produktZadnjihDimenzij(nivo + 2), nivo + 1);
            return;
        }
        zgradi(seznam, dimenzije, L(index, nivo), nivo);
        zgradi(seznam, dimenzije, D(index, nivo), nivo);
        arrayOperacija(L(index, nivo), D(index, nivo), produktZadnjihDimenzij(nivo + 1), index);
    }

    private void populirajDrevo(int[] seznam,  int[] dimenzije) {
        // secondary index
        int[] offsets = getOffsets(seznam, dimenzije);
        int index = getFirstIndex();
        for (int i = 0; i < seznam.length; i++) {
            drevo[index] = seznam[i];
            index = index + offsets[i];
        }
    }

    private int getFirstIndex() {
        int index = drevo.length / 2;
        for (int i = 1; i < velOrig.length; i++) {
            index += produktZadnjihDimenzij(i) / 2;
        }
        return index;
    }

    private int[] getOffsets(int[] seznam, int[] dimenzije) {
        int[] offsets = new int[seznam.length];
        Arrays.fill(offsets, 1);
        int prod = 1;
        int f = 1;
        for (int i = velOrig.length - 1; i >= 0; i--) {
            for (int j = f - 1; j < seznam.length; j += f) {
                offsets[j] = (offsets[j] * prod);
                if (j == 0) break;
                offsets[j] -= dimenzije[i] - 1;
            }
            prod = velOrig[i] * 2;
            f *= dimenzije[i];
        }
        return offsets;
    }

    private void arrayOperacija(int ia, int ib, int velikost, int index) {
        System.out.printf("ia: %d\tib: %d\tvelikost: %d\tindex: %d\n", ia, ib, velikost, index);
        for (int i = 0; i < velikost; i++) {
            drevo[index + i] = operacija.funkcija(drevo[ia + i], drevo[ib + i]);
        }
    }

    private int produktZadnjihDimenzij(int nivo) {
        // a odstranis * 2 ?
        // O(k)
        int prod = 1;
        for (int i = nivo; i < velOrig.length; i++) {
            prod *= velOrig[velOrig.length - 1 - i] * 2;
        }
        return prod;
    }

    private int stars(int index, int nivo) {
        return -1;
    }

    public static void main(String[] args) {
        Operacija minimum = new Minimum();
        Operacija sum = new Sum();
        // SegmentnoDrevoKD sD = new SegmentnoDrevoKD(new int[] {1,2,3,4,5,6,7,8,1,7,5,9,3,0,6,2}, new int[] {4,4}, minimum);
         SegmentnoDrevoKD sD = new SegmentnoDrevoKD(new int[] {3,7,-1,4,2,0,-3,1,1,17,-2}, new int[] {11},minimum);
        // SegmentnoDrevoKD sD = new SegmentnoDrevoKD(new int[] {2,7,8,3,1,4,4,2}, new int[] {2,2,2}, sum);
        System.out.println(Arrays.toString(sD.drevo));

    }
}

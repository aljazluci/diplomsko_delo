import java.util.Arrays;

public class QuadDrevo {
    final Operacija operacija;
    int[] tabela;
    int[] origDims;

    public QuadDrevo (Operacija operacija, int[] tabela, int[] dims) {
        // podatki velikost dims[0], dims[1]
        this.operacija = operacija;
        this.origDims = dims;
        padTabela(tabela);

    }
    private void padTabela(int[] tabela) {
        int dim0 = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(this.origDims[0] - 1));
        int dim1 = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(this.origDims[1] - 1));
        this.tabela = new int[dim0 * dim1];
        Arrays.fill(this.tabela, operacija.elVal);
        for(int i = 0; i < this.origDims[0]; i++) {
            for(int j = 0; j < this.origDims[1]; j++) {
                this.tabela[i * dim1 + j] = tabela[i * this.origDims[1] + j];
            }
        }
    }

}

class QuadDrevoNode {
    QuadDrevoNode nw;
    QuadDrevoNode sw;
    QuadDrevoNode ne;
    QuadDrevoNode se;

}

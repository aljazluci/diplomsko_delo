/*
*
*   Abstraktni razred Operacija in nekaj predlogov funkcij, ki jih lahko uporabljamo v segmentnem drevesu
*   abstraktni razred OperacijaFenwick bo delovala tako na Fenwickovem kot segmentnem drevesu
*
*/

public abstract class Operacija {
    // default value
    final int elVal;
    Operacija(int val) {
        elVal = val;
    }
    abstract int funkcija(int a, int b);
}

abstract class OperacijaFenwick extends Operacija {
    abstract int funkcija(int a, int b);
    abstract int inverzFunkcija(int a, int b);
    OperacijaFenwick(int val) {
        super(val);
    }
}

class Minimum extends Operacija {
    Minimum() {
        super(Integer.MAX_VALUE);
    }
    int funkcija(int a, int b) {
        return a < b ? a : b;
    }
}

class Sum extends OperacijaFenwick {
    Sum() {super(0);}
    int funkcija(int a, int b) {
        return a + b;
    }
    int inverzFunkcija(int a, int b){
        return a - b;
    }
}
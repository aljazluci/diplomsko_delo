package RangeQuery;

public interface OperacijaFenwick<T> extends Operacija<T> {
    T funkcija(T a, T b);
    T inverzFunkcija(T a, T b);
    T elVal();
}
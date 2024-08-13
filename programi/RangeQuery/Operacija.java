package RangeQuery;/*
*
*   Abstraktni razred RangeQuery.Operacija in nekaj predlogov funkcij, ki jih lahko uporabljamo v segmentnem drevesu
*   abstraktni razred RangeQuery.OperacijaFenwick bo delovala tako na Fenwickovem kot segmentnem drevesu
*
*/

public interface Operacija <T>{
    // default value
    T elVal();
    T funkcija(T a, T b);
}


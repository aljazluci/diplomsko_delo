package RangeQuery;

public interface Queryable <T>{
    // po dimenzijah po 1. dimenziji: range[0] - range[1], po 2. dimenziji: range[2] - range[3]...
    T poizvedi(int[] range);
    void posodobi(int[] indices, T val);
}

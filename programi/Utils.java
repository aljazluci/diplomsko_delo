import RangeQuery.Operacija;
import RangeQuery.OperacijaFenwick;

public class Utils {
    public static class Minimum implements Operacija<Integer> {
        @Override
        public Integer elVal() {
            return Integer.MAX_VALUE;
        }

        public Integer funkcija(Integer a, Integer b) {
            return a < b ? a : b;
        }
    }

    public static class Sum implements OperacijaFenwick<Integer> {

        @Override
        public Integer elVal() {
            return 0;
        }

        public Integer funkcija(Integer a, Integer b) {
            return a + b;
        }
        public Integer inverzFunkcija(Integer a, Integer b){
            return a - b;
        }
    }

    public static class Multiply implements OperacijaFenwick<Double> {
        // ne uporabljaj z 0
        public Double funkcija(Double a, Double b) {
            return a * b;
        }
        public Double inverzFunkcija(Double a, Double b){
            return a / b;
        }
        @Override
        public Double elVal() {
            return 1.0;
        }
    }


}

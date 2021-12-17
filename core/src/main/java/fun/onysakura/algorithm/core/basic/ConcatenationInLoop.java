package fun.onysakura.algorithm.core.basic;

import fun.onysakura.algorithm.utils.core.basic.Benchmark;
import fun.onysakura.algorithm.utils.core.basic.str.RandomUtils;

import java.util.ArrayList;

public class ConcatenationInLoop {

    public static void main(String[] args) {
        Benchmark.init();
        {
            ArrayList<Double> times = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Benchmark.begin();
                String s = "";
                for (int j = 0; j < 100; j++) {
                    s += RandomUtils.randomStr(10);
                }
                double v = Benchmark.endWithoutPrint();
                times.add(v);
                String s1 = s;
            }
            System.out.println(times);
        }
        {
            ArrayList<Double> times = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Benchmark.begin();
                StringBuffer s = new StringBuffer();
                for (int j = 0; j < 100; j++) {
                    s.append(RandomUtils.randomStr(10));
                }
                double v = Benchmark.endWithoutPrint();
                times.add(v);
            }
            System.out.println(times);
        }
        {
            ArrayList<Double> times = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Benchmark.begin();
                StringBuilder s = new StringBuilder();
                for (int j = 0; j < 100; j++) {
                    s.append(RandomUtils.randomStr(10));
                }
                double v = Benchmark.endWithoutPrint();
                times.add(v);

            }
            System.out.println(times);
        }
    }
}

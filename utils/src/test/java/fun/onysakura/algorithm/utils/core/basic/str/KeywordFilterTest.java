package fun.onysakura.algorithm.utils.core.basic.str;

import fun.onysakura.algorithm.utils.core.basic.Benchmark;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KeywordFilterTest {

    @Test
    public void simple() throws Exception {
        KeywordFilter.init(Arrays.asList("一二三", "一二三四五", "三四五"));
        String s = "aaaa一二三四五aaa五四三二一aaaa一二四五aaa";
        List<String> keywords = KeywordFilter.match(s);
        System.out.println(s);
        System.out.println(keywords);
    }

    @Test
    public void file() throws Exception {
        Benchmark.init();
        {
            // init
            Benchmark.begin();
            File file = new File("C:/Files/Temp/BlockWords.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> list = reader.lines().collect(Collectors.toList());
            KeywordFilter.init(list);
            Benchmark.end();
        }
        List<List<String>> lists = new ArrayList<>();
        AtomicInteger lines = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        AtomicInteger total = new AtomicInteger();
        File file = new File("C:/Files/Temp/Article.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Benchmark.begin();
        reader.lines().forEach(line -> {
            lines.getAndIncrement();
            List<String> match = KeywordFilter.match(line);
            if (!match.isEmpty()) {
                count.getAndIncrement();
                total.addAndGet(match.size());
                lists.add(match);
            }
        });
        Benchmark.end();
        System.out.println(lines + " -> " + count + ", total: " + total);
        for (int i = 0; i < lists.size(); i++) {
            System.out.print(lists.get(i) + "  ");
            if (i % 20 == 0) {
                System.out.println();
            }
        }
    }
}

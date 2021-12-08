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
    public static void init() throws Exception {
        File file = new File("C:\\Files\\Workspace\\Mine\\Java\\Algorithm\\utils\\src\\test\\java\\fun\\onysakura\\algorithm\\utils\\core\\basic\\str\\block.txt");
        File file2 = new File("C:\\Files\\Workspace\\Mine\\Java\\Algorithm\\utils\\src\\test\\java\\fun\\onysakura\\algorithm\\utils\\core\\basic\\str\\block1.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file2));
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> collect = reader.lines().distinct().sorted().collect(Collectors.toList());
        collect.forEach(s -> {
            try {
                writer.write(s);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.flush();
        writer.close();
    }

    @Test
    public void simple() throws Exception {
        KeywordFilter.init(Arrays.asList("一二三", "一二三四五", "三四五"));
        String s = "aaaa一二三四五aaa";
        List<String> keywords = KeywordFilter.match(s);
        System.out.println(KeywordFilter.BLOCK_WORD_MAP);
        System.out.println(s);
        System.out.println(keywords);
    }

    @Test
    public void file() throws Exception {
        Benchmark.init();
        {
            // init
            Benchmark.begin();
            File file = new File("C:\\Files\\Workspace\\Mine\\Java\\Algorithm\\utils\\src\\test\\java\\fun\\onysakura\\algorithm\\utils\\core\\basic\\str\\block1.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> list = reader.lines().collect(Collectors.toList());
            KeywordFilter.init(list);
            Benchmark.end();
        }
        KeywordFilter.init(Arrays.asList("的", "是"));

        List<List<String>> lists = new ArrayList<>();
        AtomicInteger lines = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        AtomicInteger total = new AtomicInteger();
        File file = new File("C:\\Users\\gjp06\\Documents\\Tencent Files\\425165357\\FileRecv\\《希灵帝国》作者：远瞳.txt");
//        File file = new File("C:\\Users\\gjp06\\OneDrive\\Docs\\Books\\Novel\\龙族2悼亡者之瞳.txt");
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
        for (List<String> list : lists) {
            System.out.println(list);
        }
    }
}

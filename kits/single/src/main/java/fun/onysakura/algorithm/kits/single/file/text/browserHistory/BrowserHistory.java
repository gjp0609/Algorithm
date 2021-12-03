package fun.onysakura.algorithm.kits.single.file.text.browserHistory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import fun.onysakura.algorithm.utils.core.basic.Benchmark;
import fun.onysakura.algorithm.utils.db.sqlite.BaseRepository;
import fun.onysakura.algorithm.utils.db.sqlite.SQLite;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class BrowserHistory {

    private static final BaseRepository<History> historyBaseRepository;

    static {
        SQLite.open("C:/Files/Workspace/Mine/JavaScript/pages/docs/single/bookmarks/history.db");
        historyBaseRepository = new BaseRepository<>(History.class);
    }

    public static void main1(String[] args) throws Exception {
//        historyBaseRepository.createTable();

        System.out.println(new BigDecimal("1.0").compareTo(new BigDecimal("0")));
//        read();
    }

    public static void read() throws Exception {
        Benchmark.init();
//        File tsv = new File("R:/Downloads/exported_archived_history_demo.tsv");
        File tsv = new File("R:/Downloads/exported_archived_history_20211115.tsv");
        {
            BufferedReader reader = new BufferedReader(new FileReader(tsv));
            Supplier<Stream<String>> stream = reader::lines;
            Benchmark.begin();
            long count = stream.get().count();
            Benchmark.end();
            System.out.println(count);
        }
        {
            BufferedReader reader = new BufferedReader(new FileReader(tsv));
            Benchmark.begin();
            AtomicLong count = new AtomicLong(0L);
            reader.lines()
                    .parallel()
                    .filter(line -> line.length() < 500)
                    .map(line -> {
                        String[] split = line.split("\t", -1);
                        String strTime = split[1];
                        long time = (long) (Double.parseDouble(strTime.substring(1)) * 1000);
                        return new History(String.valueOf(time), split[3], split[0]);
                    })
                    .filter(distinctByKey(History::getTime))
                    .filter(distinctByKey(History::getUrl))
                    .sorted(Comparator.comparing(History::getTime))
                    .collect(Collectors.toList())
                    .forEach(history -> {
                        if (count.incrementAndGet() % 100 == 0) {
                            log.info(String.valueOf(count.get()));
                        }
                        save(history);
                    });
            Benchmark.end();
            System.out.println(count.get());
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private static void save(History history) {
        History queryHistory = new History();
        queryHistory.setTime(history.getTime());
        List<History> list = historyBaseRepository.select(queryHistory);
        if (list.isEmpty()) {
            historyBaseRepository.insert(history);
        }
    }


    public static void main(String[] args) {

        JSONArray array = new JSONArray();
        array.add(new HashMap<String,String>());
        array.add(new HashMap<String,String>());
        array.add(new HashMap<String,String>());
        for (Object o : JSON.parseArray(JSON.toJSONString(array))) {
            System.out.println(o.getClass());
        }

    }
}

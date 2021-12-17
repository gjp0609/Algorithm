package fun.onysakura.algorithm.utils.core.basic.str;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpiteWordTest {

    @Test
    public void spiteWord() throws Exception {
        LinkedHashMap<String, Long> counts = new LinkedHashMap<>();
        File file = new File("C:/Files/Temp/Article.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.lines().forEach(line -> {
            Result result = NlpAnalysis.parse(line);
            result.getTerms().forEach(term -> {
                Long count = counts.getOrDefault(term.getName(), 0L);
                counts.put(term.getName(), count + 1);
            });
        });
        List<Map.Entry<String, Long>> collect = counts.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        for (int i = collect.size() - 1; i >= 0; i--) {
            if (i < collect.size() - 10) break;
            System.out.printf("%8d: %s\n", collect.get(i).getValue(), collect.get(i).getKey());
        }
    }

    @Test
    public void keywords() {
        KeyWordComputer kwc = new KeyWordComputer(3);
        List<Keyword> keywords = kwc.computeArticleTfidf("据市场消息，大众汽车正考虑让旗下豪华品牌保时捷独立上市，获取的资金将用于大众智能化和电动化转型。据消息人士透露，目前尚不清楚保时捷最终是否会上市，这家公司的市值预计为 80 到 105 亿欧元。");
        System.out.println(keywords);
    }
}

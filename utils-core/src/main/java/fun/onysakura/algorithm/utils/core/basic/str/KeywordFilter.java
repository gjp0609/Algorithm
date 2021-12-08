package fun.onysakura.algorithm.utils.core.basic.str;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class KeywordFilter {
    /**
     * 最小匹配，开启则只获取第一个关键词
     * 开启、关闭性能差距约 10%
     */
    public static boolean FAIL_FAST = false;
    /**
     * 屏蔽词拆分树
     */
    private static ConcurrentHashMap<Character, Object> BLOCK_WORD_MAP;

    /**
     * 初始化屏蔽词
     *
     * @param blockWords 屏蔽词列表
     */
    public static void init(Collection<String> blockWords) throws Exception {
        if (blockWords == null || blockWords.isEmpty()) {
            throw new RuntimeException("black word list can't be empty!");
        }
        BLOCK_WORD_MAP = new ConcurrentHashMap<>(blockWords.size() / 2);
        blockWords.forEach(blockWord -> init(blockWord, 0, BLOCK_WORD_MAP));
    }

    private static void init(String blockWord, int index, ConcurrentHashMap<Character, Object> map) {
        Object o = map.get(blockWord.charAt(index));
        ConcurrentHashMap<Character, Object> hashMap;
        if (o == null) {
            hashMap = new ConcurrentHashMap<>();
            map.put(blockWord.charAt(index), hashMap);
        } else {
            hashMap = (ConcurrentHashMap<Character, Object>) o;
        }
        if (index + 1 < blockWord.length()) {
            init(blockWord, index + 1, hashMap);
        } else {
            hashMap.put('\t', true);
        }
    }

    public static List<String> match(String text) {
        List<String> blockWords = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (FAIL_FAST && blockWords.size() > 0) {
                break;
            }
            // 先根据首位字符获取子树
            // 直接传输完整的屏蔽词列表会导致性能下降
            // getMatch(this.text, i, DFA_MAP, "");
            char c = text.charAt(i);
            Object o = BLOCK_WORD_MAP.get(c);
            if (o != null) {
                ConcurrentHashMap<Character, Object> map = (ConcurrentHashMap<Character, Object>) o;
                if (map.containsKey('\t')) {
                    blockWords.add(String.valueOf(c));
                }
                getMatch(blockWords, text, i + 1, map, String.valueOf(c));
            }
        }
        return blockWords;
    }

    private static void getMatch(List<String> blockWords, String text, int index, ConcurrentHashMap<Character, Object> dfaMap, String key) {
        for (int i = index; i < text.length(); i++) {
            char c = text.charAt(i);
            Object o = dfaMap.get(c);
            if (o == null) {
                break;
            } else {
                ConcurrentHashMap<Character, Object> map = (ConcurrentHashMap<Character, Object>) o;
                if (map.containsKey('\t')) {
                    blockWords.add(key + c);
                }
                if (index + 1 < text.length()) {
                    getMatch(blockWords, text, index + 1, map, key + c);
                }
            }
        }
    }

}

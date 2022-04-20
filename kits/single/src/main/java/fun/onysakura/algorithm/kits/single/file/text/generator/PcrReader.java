package fun.onysakura.algorithm.kits.single.file.text.generator;

import fun.onysakura.algorithm.kits.single.Constants;
import fun.onysakura.algorithm.kits.single.file.text.generator.base.FtlGeneratorUtils;
import fun.onysakura.algorithm.kits.single.file.text.generator.base.FtlTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PcrReader {

    private static final Map<String, Map<String, String>> WIKI_DATA = new TreeMap<>();

    public static void main(String[] args) throws Exception {

        initData();
//        System.out.println(JSON.toJSONString(WIKI_DATA));
//        /*for (String s : WIKI_DATA.keySet()) {
//            System.out.println(WIKI_DATA.get(s));
//        }*/
        generate();
    }

    private static void generate() throws Exception {
        File dir = new File("C:\\Users\\gjp06\\OneDrive\\Pictures\\pcr\\excel");
        Map<String, Map<String, Object>> map = new TreeMap<>();
        File[] files = dir.listFiles();
        int length = files.length;
        int index = 0;
        String[] ranks = new String[length];
        for (File file : files) {
            String rank = file.getName().replace(".xlsx", "");
            ranks[index] = rank;
            System.out.println(rank);
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            for (int j = 0; j < 1000; j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    Cell numCell = row.getCell(2);
                    Cell nameCell = row.getCell(3);
                    Cell ghzCell = row.getCell(4);
                    Cell jjcCell = row.getCell(5);
                    if (numCell != null && CellType.NUMERIC == numCell.getCellType()) {
                        String name = getCellString(nameCell).trim();
                        name = name.replace("\\n", " ");
                        name = name.replaceAll(" {2}", " ");
                        String key = getCellString(numCell);
                        key = NAME_DICT_TW.getOrDefault(name, key);
                        Map<String, String> infos = WIKI_DATA.get(key);
                        if (!map.containsKey(key)) {
                            TreeMap<String, Object> treeMap = new TreeMap<>();
                            treeMap.put("name", infos.get("name"));
                            treeMap.put("nickname", infos.get("nickname"));
                            treeMap.put("img", infos.get("img"));
                            treeMap.put("imgSrc", infos.get("imgSrc"));
                            treeMap.put("position", infos.get("position"));
                            treeMap.put("ghz", new String[length]);
                            treeMap.put("jjc", new String[length]);
                            map.put(key, treeMap);
                        }
                        Map<String, Object> treeMap = map.get(key);
                        String[] ghz = (String[]) treeMap.get("ghz");
                        String[] jjc = (String[]) treeMap.get("jjc");
                        ghz[index] = getCellString(ghzCell);
                        jjc[index] = getCellString(jjcCell);
                    }
                }
            }
            index++;
        }
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("datas", map);
        rootMap.put("ranks", ranks);
        FtlGeneratorUtils.generate(FtlTemplate.pcr, "pcr", "/pcr_rank.md", rootMap);
        FtlGeneratorUtils.generate(FtlTemplate.pcr_html, "pcr", "/pcr_rank.html", rootMap);
    }

    public static String getCellString(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().replace("\n", "\\n");
            case NUMERIC:
                double doubleValue = cell.getNumericCellValue();
                String doubleNum = String.valueOf(doubleValue);
                if (doubleValue - Math.round(doubleValue) == 0) {
                    return String.valueOf(Math.round(doubleValue));
                }
                return doubleNum;
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private static void initData() throws Exception {
        File file = new File(Constants.OUTPUT_PATH + "/pcr/wiki-2022-01-06.html");
        Document document;
        if (file.exists()) {
            document = Jsoup.parse(file, StandardCharsets.UTF_8.name());
        } else {
            document = Jsoup.parse(URI.create("https://wiki.biligame.com/pcr/%E8%A7%92%E8%89%B2%E5%9B%BE%E9%89%B4").toURL(), 1000 * 10);
        }
        @SuppressWarnings("ConstantConditions")
        Elements elements = document.body()
                .getElementById("CardSelectTr")
                .getElementsByTag("tbody").first()
                .children();
        HttpClient client = HttpClient.newBuilder().build();
        for (int i = 1; i < elements.size(); i++) {
            Elements infos = elements.get(i).children();
            String position = infos.get(4).text().trim();
            String name = infos.get(1).text().trim();
            String nickname = infos.get(2).text().trim();
            String img = infos.get(0).getElementsByTag("img").attr("src").trim();
            String key = NAME_DICT_CN.getOrDefault(name, position);
            if (WIKI_DATA.containsKey(key)) {
                throw new RuntimeException("new!!" + key);
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("position", position);
            hashMap.put("name", name);
            hashMap.put("nickname", nickname);
            hashMap.put("img", img);
            File image = new File(Constants.OUTPUT_PATH + "/pcr/images/" + key + ".png");
            if (!image.exists()) {
                Thread.sleep(50);
                HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(img))
                        .header("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Mobile Safari/537.36 Edg/96.0.1054.62")
                        .build();
                HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                InputStream body = response.body();
                FileUtils.copyToFile(body, image);
            }
            image = new File(Constants.OUTPUT_PATH + "/pcr/images/" + key + ".png");
            String base64 = new String(Base64.getEncoder().encode(new FileInputStream(image).readAllBytes()), StandardCharsets.UTF_8);
            hashMap.put("imgSrc", base64);
            WIKI_DATA.put(key, hashMap);
        }
    }

    private static final HashMap<String, String> NAME_DICT_TW = new HashMap<>();
    private static final HashMap<String, String> NAME_DICT_CN = new HashMap<>();

    static {
        NAME_DICT_TW.put("凜(偶像大師)", "153+");
        NAME_DICT_CN.put("凛（偶像大师）", "153+");
        NAME_DICT_TW.put("公主佩可 公佩 鋼彈 高達", "155+");
        NAME_DICT_CN.put("佩可莉姆（公主）", "155+");
        NAME_DICT_TW.put("泳裝真琴 泳月 水狼 浪", "180+");
        NAME_DICT_CN.put("真琴（夏日）", "180+");
        NAME_DICT_TW.put("茉莉 跳跳虎 加分仔", "185+");
        NAME_DICT_CN.put("茉莉", "185+");
        NAME_DICT_TW.put("萬聖美美 萬兔", "365+");
        NAME_DICT_CN.put("美美（万圣节）", "365+");
        NAME_DICT_TW.put("泳裝香織 泳狗", "425+");
        NAME_DICT_CN.put("香织（夏日）", "425+");
        NAME_DICT_TW.put("萬聖忍 萬忍", "440+");
        NAME_DICT_CN.put("忍（万圣节）", "440+");
        NAME_DICT_CN.put("环奈（振袖）", "527+");
        NAME_DICT_CN.put("步美（怪盗）", "590+");
        NAME_DICT_TW.put("泳裝鈴奈 泳爆 泳裝爆弓 水爆", "705+");
        NAME_DICT_CN.put("铃奈（夏日）", "705+");
        NAME_DICT_TW.put("泳裝伊緒 泳老師 水魅魔", "715+");
        NAME_DICT_CN.put("伊绪（夏日）", "715+");
        NAME_DICT_TW.put("魔驢 魔霞", "730+");
        NAME_DICT_CN.put("香澄（魔法少女）", "730+");
        NAME_DICT_TW.put("愛麗絲妹弓", "730++");
        NAME_DICT_CN.put("璃乃（仙境）", "730++");
    }

}

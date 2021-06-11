package com.onysakura.algorithm.file.office;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class PcrReader {

    private static final String TEMPLATE_PATH = "tools/src/main/resources/template";
    private static final String OUTPUT_PATH = "tools/src/main/resources/output";

    private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

    static {
        try {
            configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Map<String, Object>> map = new TreeMap<>();
        File dir = new File("C:\\Users\\gjp06\\OneDrive\\Pictures\\pcr\\excel");
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
            HashSet<Object> set = new HashSet<>();
            for (int j = 0; j < 1000; j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    Cell numCell = row.getCell(2);
                    Cell nameCell = row.getCell(3);
                    Cell ghzCell = row.getCell(4);
                    Cell jjcCell = row.getCell(5);
                    if (numCell != null && CellType.NUMERIC == numCell.getCellType()) {
                        String key = getCellString(numCell);
                        while (set.contains(key)) {
                            key += "+";
                        }
                        if (!map.containsKey(key)) {
                            TreeMap<String, Object> treeMap = new TreeMap<>();
                            treeMap.put("name", getCellString(nameCell).split("\\\\n")[0]);
                            String[] ghz = new String[length];
                            treeMap.put("ghz", ghz);
                            map.put(key, treeMap);
                        }
                        Map<String, Object> treeMap = map.get(key);
                        String[] ghz = (String[]) treeMap.get("ghz");
                        ghz[index] = getCellString(ghzCell);
                        set.add(key);
                    }
                }
            }
            index++;
        }
//        System.out.println(JSON.toJSONString(map, SerializerFeature.PrettyFormat));
        Template template = configuration.getTemplate("pcr.ftl");
        File docFile = new File(OUTPUT_PATH + "/pcr.md");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("datas", map);
        rootMap.put("ranks", ranks);
        template.process(rootMap, writer);
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
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

}

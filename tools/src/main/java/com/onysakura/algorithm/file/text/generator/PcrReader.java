package com.onysakura.algorithm.file.text.generator;

import com.onysakura.algorithm.file.text.generator.base.FtlGeneratorUtils;
import com.onysakura.algorithm.file.text.generator.base.FtlTemplate;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class PcrReader {

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
                            String[] jjc = new String[length];
                            treeMap.put("ghz", ghz);
                            treeMap.put("jjc", jjc);
                            map.put(key, treeMap);
                        }
                        Map<String, Object> treeMap = map.get(key);
                        String[] ghz = (String[]) treeMap.get("ghz");
                        String[] jjc = (String[]) treeMap.get("jjc");
                        ghz[index] = getCellString(ghzCell);
                        jjc[index] = getCellString(jjcCell);
                        set.add(key);
                    }
                }
            }
            index++;
        }
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("datas", map);
        rootMap.put("ranks", ranks);
        FtlGeneratorUtils.generate(FtlTemplate.pcr, "pcr", "/pcr_rank.md", rootMap);
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

}

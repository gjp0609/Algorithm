package com.onysakura.algorithm.file.office;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;

public class PcrReader {
    public static void main(String[] args) throws Exception {
        InputStream inp = new FileInputStream("C:\\Users\\gjp06\\OneDrive\\Pictures\\pcr\\RANK (14-3)11.15.xlsx");
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);
        for (int j = 0; j < 10; j++) {
            Row row = sheet.getRow(j);
            for (int i = 0; i < 10; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    CellType cellType = cell.getCellType();
                    if (cellType == CellType.STRING) {
                        System.out.print(cell.getStringCellValue().replace("\n", ""));
                    } else if (cellType == CellType.NUMERIC) {
                        System.out.print(cell.getNumericCellValue());
                    }
                }
            }
            System.out.println();
        }
    }
}

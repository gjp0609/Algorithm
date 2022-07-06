package fun.onysakura.algorithm.kits.single.file.office;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import fun.onysakura.algorithm.utils.core.basic.Benchmark;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class ExcelCopy {
    private static final String SRC_PATH = "C:\\Files\\Temp\\Types\\office\\src.xlsx";
    private static final String TARGET_PATH = "C:\\Files\\Temp\\Types\\office\\src_copy.xlsx";

    public static void main(String[] args) throws Exception {
        Benchmark.init();
        Benchmark.begin();
        cloneSheet(SRC_PATH, TARGET_PATH, 0, 1);
        Benchmark.end();
        Benchmark.begin();
        cloneSheetValue(SRC_PATH, TARGET_PATH.replace("copy", "copy_value"), 0, 1);
        Benchmark.end();
    }

    /**
     * 复制 Excel 表格，连同格式复制
     */
    public static void cloneSheet(String srcPath, String targetPath, int... sheets) throws Exception {
        if (sheets.length == 0) {
            sheets = new int[]{0};
        }
        XSSFWorkbook workbook = new XSSFWorkbook(srcPath);
        XSSFWorkbook targetWorkbook = new XSSFWorkbook();
        for (int sheet : sheets) {
            cloneSheet(workbook.getSheetAt(sheet), targetWorkbook.createSheet());
        }
        targetWorkbook.write(Files.newOutputStream(Path.of(targetPath)));
        targetWorkbook.close();
    }

    /**
     * 复制 Excel 表格，读取+重新写入
     *
     * @param srcPath    原始
     * @param targetPath 目标
     */
    public static void cloneSheetValue(String srcPath, String targetPath, int... sheets) {
        if (sheets.length == 0) {
            sheets = new int[]{0};
        }
        try (ExcelWriter writer = EasyExcel.write(targetPath).build()) {
            for (int sheet : sheets) {
                EasyExcel.read(srcPath, new NoModelDataListener(maps -> writer.write(maps, EasyExcel.writerSheet(sheet).build())))
                        .sheet(sheet)
                        .doRead();
            }
        }
    }

    @Slf4j
    static class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {
        private static final int BATCH_COUNT = 20;
        private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        private final Consumer<List<Map<Integer, String>>> consumer;

        public NoModelDataListener(Consumer<List<Map<Integer, String>>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void invoke(Map<Integer, String> data, AnalysisContext context) {
            cachedDataList.add(data);
            if (cachedDataList.size() >= BATCH_COUNT) {
                consumer.accept(cachedDataList);
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            consumer.accept(cachedDataList);
        }
    }

    public static void cloneSheet(XSSFSheet srcSheet, XSSFSheet targetSheet) {
        for (int i = 0; i <= srcSheet.getLastRowNum(); i++) {
            Row srcRow = srcSheet.getRow(i);
            Row targetRow = targetSheet.createRow(i);
            short lastCellNum = srcRow.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell srcCell = srcRow.getCell(j);
                Cell targetCell = targetRow.createCell(j);
                CellStyle cellStyle = targetCell.getCellStyle();
                cellStyle.cloneStyleFrom(srcCell.getCellStyle());
                switch (srcCell.getCellType()) {
                    case STRING:
                        targetCell.setCellValue(srcCell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(targetCell)) {
                            targetCell.setCellValue(srcCell.getDateCellValue());
                        } else {
                            targetCell.setCellValue(srcCell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        targetCell.setCellValue(srcCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        targetCell.setCellFormula(srcCell.getCellFormula());
                        break;
                    default:
                        targetCell.setCellValue("");
                        break;
                }
            }
        }
    }

    /**
     * 计算单元格的值
     * <p style="color:red">特别注意：当动态改变excel的值的时候，必须用本方法重新用公式计算单元格的值</p>
     *
     * @param cell 单元格
     * @return {@link org.apache.poi.ss.usermodel.CellValue}
     */
    private static CellValue calcCellValue(Cell cell) {
        Workbook workbook = cell.getSheet().getWorkbook();
        FormulaEvaluator formulaEvaluator = null;
        if (cell instanceof HSSFCell) {
            // Excel 2003
            formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
        } else if (cell instanceof XSSFCell) {
            // Excel 2007+
            formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
        }
        if (formulaEvaluator != null) {
            // 进行计算并拿到值
            return formulaEvaluator.evaluate(cell);
        }
        return new CellValue("");
    }

}

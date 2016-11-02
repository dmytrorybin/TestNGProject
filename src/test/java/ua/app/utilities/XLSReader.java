package ua.app.utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dmytro_Rybin on 11/1/2016.
 */
public class XLSReader {
    private InputStream input;
    private HSSFWorkbook workBook;

    public XLSReader(String filePath) {
        try {
            input = new FileInputStream(filePath);
            workBook = new HSSFWorkbook(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object[][] parseAmount(String nameSheet) {
        Sheet sheet = workBook.getSheet(nameSheet);
        int rows = sheet.getPhysicalNumberOfRows();
        int column = sheet.getRow(0).getPhysicalNumberOfCells();
        Object[][] result = new Object[rows][column];

        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < column; j++ ) {
                Cell cell = row.getCell(j);

                switch (cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:
                        result[i][j] = (long)cell.getNumericCellValue();
                        break;
                    case Cell.CELL_TYPE_STRING:
                        result[i][j] = cell.getStringCellValue();
                        break;
                }
               // result[i][j] = (long)cell.getNumericCellValue();
            }
        }
        return result;
    }

    public Object[][] parseCurrency(String nameSheet) {
        Sheet sheet = workBook.getSheet(nameSheet);
        int rows = sheet.getPhysicalNumberOfRows();
        int column = sheet.getRow(0).getPhysicalNumberOfCells();
        Object[][] result = new Object[rows][column];

        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < column; j++ ) {
                Cell cell = row.getCell(j);

                switch (cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:
                        result[i][j] = (int)cell.getNumericCellValue();
                        break;
                    case Cell.CELL_TYPE_STRING:
                        result[i][j] = cell.getStringCellValue();
                        break;
                }
                // result[i][j] = (long)cell.getNumericCellValue();
            }
        }
        return result;
    }
}
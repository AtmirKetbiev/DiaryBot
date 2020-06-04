package ru.bot.extension;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.bot.objects.Progress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ReportExcel {

    public File getReportFile(List<String> studentNameList, Map<String ,String> taskMap, Map<String, List<Progress>> studentProgress) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("group");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("name");
        int j=0;
        for (String s: taskMap.keySet()) {
            j++;
            Cell cell1 = row.createCell(j);
            cell1.setCellValue(taskMap.get(s));
        }

        int i = 1;
        for (String s: studentNameList) {
            j=0;
            Row rowNext = sheet.createRow(i);
            Cell cellNext = rowNext.createCell(j);
            cellNext.setCellValue(s);
            List<Progress> p = studentProgress.get(s);
            for (Progress pp: p) {
                j++;
                Cell cellNext2 = rowNext.createCell(j);
                cellNext2.setCellValue(pp.getGrade());
            }
            i++;
        }

        FileOutputStream fileOutputStream = new FileOutputStream("my.xls");

        book.write(fileOutputStream);
        fileOutputStream.close();

        return new File("my.xls");
    }

}

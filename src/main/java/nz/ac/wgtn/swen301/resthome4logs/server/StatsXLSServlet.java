package nz.ac.wgtn.swen301.resthome4logs.server;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StatsXLSServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/vnd.ms-excel");
        OutputStream out = resp.getOutputStream();
        Persistency persistency = new Persistency();
        ArrayList<String> levels = persistency.getLevels();
        HashMap<String, int[]> levelsFrequency = persistency.getLogLevels();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("XLS Log Stats");

        int rowCount = 0;
        Row row = sheet.createRow(rowCount);
        int columnCount = 1;
        Cell cell = row.createCell(0);
        cell.setCellValue("Logger");
        for(String header : levels){
            cell = row.createCell(columnCount++);
            cell.setCellValue(header);
        }


        for (String logger : levelsFrequency.keySet()) {
            columnCount = 1;
            row = sheet.createRow(++rowCount);
            cell = row.createCell(0);
            cell.setCellValue(logger);
            for (Integer field : levelsFrequency.get(logger)) {
                cell = row.createCell(columnCount++);
                cell.setCellValue(field);
            }
        }
        workbook.write(out);
        workbook.close();
        out.close();
    }
}
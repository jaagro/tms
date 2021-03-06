package com.jaagro.tms.biz.utils;

import com.alibaba.fastjson.JSON;
import com.jaagro.tms.biz.service.OssSignUrlClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * apache POI工具类,用于对excel操作
 *
 * @author yj
 * @date 2019/1/8 15:05
 */
@Component
@Slf4j
public class PoiUtil {
    /**
     * EXCEL2003版本文件扩展名
     */
    private static final String xls = "xls";
    /**
     * EXCEL2007版本文件扩展名
     */
    private static final String xlsx = "xlsx";

    private static OssSignUrlClientService ossSignUrlClientService;

    @Autowired
    public void initSetOssSignUrlClientService(OssSignUrlClientService ossSignUrlClientService) {
        PoiUtil.ossSignUrlClientService = ossSignUrlClientService;
    }

    /**
     * 读取excel文件
     *
     * @param relativeUrl
     * @return
     * @throws IOException
     */
    public static List<List<String[]>> readExcel(String relativeUrl) throws IOException {
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(relativeUrl);
        if (relativeUrl.endsWith(xls)) {
            return getContent(workbook, true);
        } else {
            return getContent(workbook, false);
        }

    }

    public static Workbook getWorkBook(String relativeUrl) throws IOException {
        //检查文件
        if (!relativeUrl.endsWith(xls) && !relativeUrl.endsWith(xlsx)) {
            throw new IOException("路径" + relativeUrl + "对应的文件不是excel文件");
        }
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        InputStream is = null;
        try {
            //获取绝对路径
            String absoluteUrl = getAbsoluteUrl(relativeUrl);
            if (!StringUtils.hasText(absoluteUrl)) {
                throw new IOException("路径" + relativeUrl + "不是有效的oss相对路径");
            }
            //获取excel文件的io流
            URL url = new URL(absoluteUrl);
            URLConnection urlConnection = url.openConnection();
            is = urlConnection.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (relativeUrl.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            log.error("getWorkBook error", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.error("close InputStream error", e);
            }
        }
        return workbook;
    }

    public static String getCellValue(Cell cell, FormulaEvaluator formulaEvaluator) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        try {
            //判断数据的类型
            switch (cell.getCellType()) {
                //数字
                case Cell.CELL_TYPE_NUMERIC:
                    short format = cell.getCellStyle().getDataFormat();
                    SimpleDateFormat sdf = null;
                    if (format == 14 || format == 31 || format == 57 || format == 58
                            || (182 <= format && format <= 196)
                            || (210 <= format && format <= 213) || (208 == format)) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        cellValue = sdf.format(date);
                    } else if (format == 20 || format == 32 || format == 183 || (200 <= format && format <= 209)) {
                        sdf = new SimpleDateFormat("HH:mm");
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        cellValue = sdf.format(date);
                    } else if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                        cellValue = sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                    } else {
                        cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                    }
                    break;
                //字符串
                case Cell.CELL_TYPE_STRING:
                    cellValue = String.valueOf(cell.getStringCellValue());
                    break;
                //Boolean
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                //公式
                case Cell.CELL_TYPE_FORMULA:
                    CellValue formulaValue = formulaEvaluator.evaluate(cell);
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        format = cell.getCellStyle().getDataFormat();
                        if (format == 14 || format == 31 || format == 57 || format == 58
                                || (176 <= format && format <= 178) || (182 <= format && format <= 196)
                                || (210 <= format && format <= 213) || (208 == format)) {
                            sdf = new SimpleDateFormat("yyyy/MM/dd");
                            double value = cell.getNumericCellValue();
                            Date date = DateUtil.getJavaDate(value);
                            cellValue = sdf.format(date);
                        } else if (format == 20 || format == 32 || format == 183 || (200 <= format && format <= 209)) {
                            sdf = new SimpleDateFormat("HH:mm");
                            double value = cell.getNumericCellValue();
                            Date date = DateUtil.getJavaDate(value);
                            cellValue = sdf.format(date);
                        }
                    } else {
                        formulaEvaluator.clearAllCachedResultValues();
                        cellValue = getByCellValue(formulaValue);
                    }
                    break;
                //空值
                case Cell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                //故障
                case Cell.CELL_TYPE_ERROR:
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            cellValue = String.valueOf(cell.getRichStringCellValue());
        }
        return cellValue;
    }

    /**
     * 获取oss绝对路径
     *
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        String[] relativeUrlArray = {relativeUrl};
        List<URL> urlList = ossSignUrlClientService.listSignedUrl(relativeUrlArray);
        if (!CollectionUtils.isEmpty(urlList)) {
            return urlList.get(0).toString();
        }
        return null;
    }

    private static String getByCellValue(CellValue cell) {
        String cellValue = null;
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = String.valueOf(cell.getNumberValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = String.valueOf(cell.getStringValue());
                break;
            default:
                break;
        }
        return cellValue;
    }

    public static Workbook getWorkBook(File file) {
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        InputStream is = null;
        try {
            //2007
            is = new FileInputStream(file);
            if (file.getName().endsWith(xls)) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            log.error("getWorkBook error", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.error("close InputStream error", e);
            }
        }
        return workbook;
    }


    public static List<List<String[]>> readExcel(File file) throws IOException {
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        if (file.getName().endsWith(xls)) {
            return getContent(workbook, true);
        } else {
            return getContent(workbook, false);
        }

    }

    private static List<List<String[]>> getContent(Workbook workbook, boolean isXls) {
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<List<String[]>> list = new ArrayList<>();
        FormulaEvaluator formulaEvaluator;
        if (workbook != null) {
            if (isXls) {
                formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            } else {
                formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            }
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                // 过滤隐藏的sheet
                if (workbook.isSheetHidden(sheetNum) || workbook.isSheetVeryHidden(sheetNum)) {
                    continue;
                }
                List<String[]> sheetList = new ArrayList<>();
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环所有行
                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getLastCellNum();
                    String[] cells = new String[row.getLastCellNum()];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell, formulaEvaluator);
                    }
                    sheetList.add(cells);
                }
                list.add(sheetList);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        File file = new File("E:\\1月28日毛鸡出栏时间排程.xlsx");
        try {
            List<List<String[]>> lists = readExcel(file);
            System.out.println(JSON.toJSONString(lists.get(0)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


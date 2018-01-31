package com.zly.elasticsearch.common.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取excel中的数据
 *
 * @author zhangmeng0725 on 2018/1/31
 */
public class ReadExcelDataUtil {

    private static InputStream inputStream;

    private static HSSFWorkbook hssfWorkbook;

    /**
     * 读取excel中的数据
     *
     * @param excelPath     文件路径(写绝对路径)
     * @param excelStartCol 表格中有用数据开始的列数
     * @return 数据对象列表
     * @throws IOException IO异常
     */
    public static List<UserDO> readExcelData(String excelPath, int excelStartCol) throws IOException {

        inputStream = new FileInputStream(excelPath);
        hssfWorkbook = new HSSFWorkbook(inputStream);

        List<UserDO> listUserPO = new ArrayList<>();
        for (int sheetNum = 0; sheetNum < hssfWorkbook.getNumberOfSheets(); sheetNum++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(sheetNum);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                UserDO userDO = transfSheetRow2UserDO(hssfRow, excelStartCol);
                listUserPO.add(userDO);
            }
        }
        return listUserPO;
    }

    /**
     * 将每一行的数据封装为DO
     *
     * @param hssfRow       excel单张表对象每行的记录
     * @param excelStartCol 有用数据开始的列数
     * @return 数据对象
     */
    private static UserDO transfSheetRow2UserDO(HSSFRow hssfRow, int excelStartCol) {
        //TODO:组装数据对象
        UserDO userDO = new UserDO();
        //1、姓名
        userDO.setPhName(transfCell2String(hssfRow, excelStartCol++));
        //2、性别
        userDO.setPhSex(transfCell2Integer(hssfRow, excelStartCol++));
        return userDO;
    }


    /**
     * 将excel单元格中的内容转换为Integer
     *
     * @param hssfRow  excel单张表对象每行的记录
     * @param excelCol 当前的列数
     * @return Integer    返回类型
     */
    private static Integer transfCell2Integer(HSSFRow hssfRow, int excelCol) {
        HSSFCell hssfCell = hssfRow.getCell(excelCol);
        if (hssfCell != null) {
            hssfCell.setCellType(Cell.CELL_TYPE_NUMERIC);
            return new Double(hssfCell.getNumericCellValue()).intValue();
        }
        return null;

    }

    /**
     * 将excel单元格中的内容转换为String
     *
     * @param hssfRow  excel单张表对象每行的记录
     * @param excelCol 当前的列数
     * @return String    返回类型
     */
    private static String transfCell2String(HSSFRow hssfRow, int excelCol) {
        HSSFCell hssfCell = hssfRow.getCell(excelCol);
        if (hssfCell != null) {
            hssfCell.setCellType(Cell.CELL_TYPE_STRING);
            return hssfCell.getStringCellValue();
        }
        return null;

    }
}

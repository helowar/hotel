package com.mangocity.hotel.base.web.webwork;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mangocity.util.DateUtil;

/**
 */
public class ExcelUploadAction extends GenericAction {

    private static final String TYPE_DATE = "type_date";

    /**
     * 上传的文件
     */
    private File upload;

    private String fileName;

    private String caption;

    private String mapperID;

    @Override
    public String execute() throws Exception {

        FileInputStream is = new FileInputStream(upload);
        try {
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);

            int fromIndex = 0;
            int rowcount = 10;
            short colNum = 10;
            for (int i = fromIndex; i < rowcount; i++) {

                HSSFRow row = sheet.getRow(i);

                for (short m = 0; m < colNum; m++) {
                    row.getCell(m);

                }
            }
        } catch (Exception e) {
        	log.error(e);
        } finally {
            is.close();
        }

        return SUCCESS;

    }

    public String getStringValue(HSSFCell cell, String type) throws Exception {

        int cellType = cell.getCellType();
        if (cellType == HSSFCell.CELL_TYPE_STRING)
            return cell.getStringCellValue().trim();
        else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
            if (TYPE_DATE.equals(type))
                return "" + DateUtil.dateToString(cell.getDateCellValue());
            return "" + cell.getNumericCellValue();
        } else if (cellType == HSSFCell.CELL_TYPE_BLANK)
            return null;
        else
            throw new Exception("非法的单元格式");
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getMapperID() {
        return mapperID;
    }

    public void setMapperID(String mapperID) {
        this.mapperID = mapperID;
    }

}

package com.mangocity.hotel.base.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.mangocity.hotel.base.dao.IGeograpPositionDAO;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.service.GeographicalPositionService;

public class GeographicalPositionServiceImpl implements GeographicalPositionService{
	private static Log log = LogFactory.getLog(GeographicalPositionServiceImpl.class);
	private IGeograpPositionDAO geograpPositionDAO;
	private static final String filePath="f:\\elong.xls";
	public void generatorPosition() {
		ExcelReader excelReader=new ExcelReader();
		try {
			InputStream is2 = new FileInputStream(filePath);
			Map<Integer, HtlGeographicalposition> map = excelReader.readExcelContent(is2);
			log.info("获得Excel表格的内容:size="+map.size());
			for (int i = 1; i <= map.size(); i++) {
				HtlGeographicalposition geo=map.get(i);
				log.info(geo.getID()+":"+geo.getName()+":"+geo.getLatitude()+":"+geo.getLongitude());
				if(null!=geo.getLatitude()&&null!=geo.getLongitude()){
					geo.setOperationDate(new Date());
					geo.setOperationer("IT");
					geo.setOperationerId("0");
					if( geograpPositionDAO.queryGeograpPosition(geo.getCityName(),geo.getGptypeId(),geo.getName()).isEmpty()){
						log.info(geo.getCityName()+"   "+geo.getName()+"  "+geo.getLatitude()+" "+geo.getLongitude());
						geograpPositionDAO.createGeograpPosition(geo);
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error("load excel file not found!", e);
		}
		
	}
	public List<HtlGeographicalposition> queryPositinList(String cityName,
			Long type) {
		return geograpPositionDAO.queryPositionByType(GEOGRAPHICAL_POSITION_TYPE, cityName);
		
	}
	public IGeograpPositionDAO getGeograpPositionDAO() {
		return geograpPositionDAO;
	}
	public void setGeograpPositionDAO(IGeograpPositionDAO geograpPositionDAO) {
		this.geograpPositionDAO = geograpPositionDAO;
	}
	public Map queryBusinessForCityName(String cityName) {
		log.info("GeographicalPositionServiceImpl queryBusinessForCityName cityName:"+cityName);
		List list=geograpPositionDAO.queryBusinessForCityName(cityName);
		Map map=new HashMap();
		List districtList=new ArrayList();
		List businessList=new ArrayList();
		for(Iterator i = list.iterator();i.hasNext();){
			Object[] objects=(Object[]) i.next();
			if(null!=objects&&objects.length>3){
				String treePath=(String) objects[2];
				if(treePath.contains("business")){
					businessList.add(objects);
				}
				if(treePath.contains("district")){
					districtList.add(objects);
				}
			}
		}
		map.put("business", businessList);
		map.put("district", districtList);
		return map;
	}
	
	public Map queryBusinessForCityCode(String cityCode) {
		log.info("GeographicalPositionServiceImpl queryBusinessForCityCode cityCode:"+cityCode);
		List list=geograpPositionDAO.queryBusinessForCityCode(cityCode);
		Map map=new HashMap();
		List districtList=new ArrayList();
		List businessList=new ArrayList();
		for(Iterator i = list.iterator();i.hasNext();){
			Object[] objects=(Object[]) i.next();
			if(null!=objects&&objects.length>3){
				String treePath=(String) objects[2];
				if(treePath.contains("business")){
					businessList.add(objects);
				}
				if(treePath.contains("district")){
					districtList.add(objects);
				}
			}
		}
		map.put("business", businessList);
		map.put("district", districtList);
		return map;
	}
	public String getGeoPostitionName(Long id) {
		return geograpPositionDAO.queryGeopositionById(id).getName();
	}
	public HtlGeographicalposition getGeographicalposition(Long id) {
		return geograpPositionDAO.queryGeopositionById(id);
	}
	public Map findAllCity() {
		 List list=geograpPositionDAO.queryAllCity();
		 String flag="";
		 Map map=new HashMap();
		 List newList=new ArrayList();
		 for(Iterator i = list.iterator();i.hasNext();){
			 Object[] oo=(Object[]) i.next();
			 if(!flag.equals(oo[2])){
				 if(!newList.isEmpty()){
					 map.put(flag,newList);
					 newList=new ArrayList();
				 }else{
					 flag=(String) oo[2];
					 newList.add(oo);
				 }
			 }else{
				 newList.add(oo);
			 }
		 }
		 return map;
	}
	public List queryPositinListByCityCode(String cityCode, Integer type) {
		return geograpPositionDAO.queryPostionListByCityCode(cityCode,type);
	}
	public List findCityByCode(String cityCode) {
		return geograpPositionDAO.findCityByCode(cityCode);
	}
	

}
class ExcelReader {
	private static Log log = LogFactory.getLog(ExcelReader.class);
	private POIFSFileSystem fs;
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private HSSFRow row;
	/**
	 * 读取Excel数据内容
	 * 
	 * @param InputStream
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public Map<Integer, HtlGeographicalposition> readExcelContent(InputStream is) {
		Map<Integer, HtlGeographicalposition> content = new HashMap<Integer, HtlGeographicalposition>();
		
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			HtlGeographicalposition geo=new HtlGeographicalposition();
			row = sheet.getRow(i);
			geo.setCityName( getStringCellValue(row.getCell((short) 1)).trim());
			String memo=getStringCellValue(row.getCell((short) 3)).trim();
			if(null!=memo&&"地铁".equals(memo)){
				geo.setGptypeId(24l);
			}else{
				String typeName=getStringCellValue(row.getCell((short) 2)).trim();
				geo.setGptypeId(adapterType().get(typeName));
			}
			String name=getStringCellValue(row.getCell((short) 4)).trim();
			geo.setName(name);
			geo.setAddress(name);
			try{
				geo.setLatitude(Double.valueOf(getStringCellValue(row.getCell((short) 5)).trim()));
				geo.setLongitude(Double.valueOf(getStringCellValue(row.getCell((short) 6)).trim()));
			}catch(NumberFormatException e){
				log.error(geo.getName(),e);
			}
			//int j = 0;
			//while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				//str += getStringCellValue(row.getCell((short) j)).trim() + "-";
			//	j++;
			//}
			content.put(i, geo);
		}
		return content;
	}
	
	private Map<String,Long> adapterType(){
		Map<String,Long> map =new HashMap<String,Long>();
		map.put("景点", 23l);
		map.put("大学", 26l);
		map.put("地铁", 24l);
		map.put("交通枢纽", 21l);
		map.put("医院", 27l);
		map.put("主题", 28l);
		map.put("交通", 29l);
		map.put("商企", 30l);
		map.put("生活服务", 31l);
		map.put("购物", 32l);
		map.put("餐馆", 33l);
		return map;
	}
	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}
}
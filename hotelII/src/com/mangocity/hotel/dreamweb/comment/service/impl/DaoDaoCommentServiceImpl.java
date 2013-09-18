package com.mangocity.hotel.dreamweb.comment.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import com.mangocity.hotel.base.persistence.OrParam;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.dreamweb.comment.dao.IDaoDaoCommentDao;
import com.mangocity.hotel.dreamweb.comment.handler.SaxXmlHandler;
import com.mangocity.hotel.dreamweb.comment.model.DaoDaoBasicComment;
import com.mangocity.hotel.dreamweb.comment.service.IDaoDaoCommentService;
import com.mangocity.util.log.MyLog;

public class DaoDaoCommentServiceImpl implements IDaoDaoCommentService,Runnable {
	/**
	 * 所有含有状态的成员变量，都没有设置setter和getter方法，
	 * 而是在构造函数中从properties配置文件中读取值赋给成员变量
	 * （Service可供多个Action调用，防止数据被污染）
	 */
	private static final MyLog log = MyLog.getLogger(DaoDaoCommentServiceImpl.class);
	private String fileName;
	private String uploadPath;
	private String savePath;
	private String urlConn;
	private String parseXmlPathError;
	private String parseXmlFailed;
	private String readPropertiesSuccess;
	private String[] updateDBStatus;
	private String[] downloadStatus;
	
	private SystemDataService systemDataService;
    private IDaoDaoCommentDao daoDaoCommentDao;
    
    /**
     * 从properties配置文件中读取值赋给成员变量
     */
    public DaoDaoCommentServiceImpl() {
    	Properties prop = new Properties();
    	InputStream in =DaoDaoCommentServiceImpl.class.getResourceAsStream("/daodao.properties");
    	try {
			prop.load(in);
			fileName = prop.getProperty("daodao.fileName");
			uploadPath = prop.getProperty("daodao.uploadPath");
			savePath = prop.getProperty("daodao.savePath");
			urlConn = prop.getProperty("daodao.urlConnection");
			parseXmlPathError = prop.getProperty("daodao.parseXml.pathError");
			parseXmlFailed = prop.getProperty("daodao.parseXmlStatus.failed");
			readPropertiesSuccess = prop.getProperty("daodao.readProperties.success");
			String timeout = prop.getProperty("daodao.downloadStatus.timeout");
			String downloading = prop.getProperty("daodao.downloadStatus.downloading");
			String complete = prop.getProperty("daodao.downloadStatus.complete");
			String error = prop.getProperty("daodao.downloadStatus.error");
			String startUpdateDB = prop.getProperty("daodao.updatedb.start");
			String endUpdateDB = prop.getProperty("daodao.updatedb.end");
			downloadStatus = new String[]{timeout,downloading,complete,error};
			updateDBStatus = new String[]{startUpdateDB,endUpdateDB};
			log.info(readPropertiesSuccess);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
	public DaoDaoBasicComment getBasicCommentByHotelId(Long hotelId){  
    	    return daoDaoCommentDao.getDaoDaoCommentByHotelId(hotelId); 		    	
	}
	
	public void downloadXmlFromDaoDao() {
		URL url;
		OutputStream outputStream=null;
		OutputStreamWriter writer=null;
		InputStream inputStream=null;
		InputStreamReader reader=null;
		URLConnection urlConnection=null;
	    log.info(downloadStatus[1]);
		try {
			url = new URL(urlConn);
			urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(100000);//如果100秒内无法建立连接，则放弃连接
			inputStream=urlConnection.getInputStream();
		    reader = new InputStreamReader(inputStream,"utf-8");
		    File uploadFolder = new File(InitServlet.SAVE_ROOTPATH,uploadPath);
	        File daodaoFolder = new File(uploadFolder,savePath);
	        if(!uploadFolder.exists()){	        	
	        		uploadFolder.mkdir();				
	        }
	        if(!daodaoFolder.exists()){	        	
	        		daodaoFolder.mkdir();				
	        }
			File file = new File(daodaoFolder,fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);
		    writer = new OutputStreamWriter(outputStream,"utf-8");
			int contentLength = urlConnection.getContentLength();//待下载文件的大小
			if(contentLength<1){
				log.error(downloadStatus[3]);
			}
			int read=0;
            char buffer[] = new char[64];
            while(-1!=(read=reader.read(buffer))){ 
            	writer.write(buffer, 0, read);
            }    
                log.info(downloadStatus[2]);
			
		} catch (SocketTimeoutException e) {
			log.error(downloadStatus[0],e); //连接超时
		} catch (Exception e) {
			log.error(downloadStatus[3],e);
		} finally{                        //关掉io流
			try {
				if(null!=writer){
					writer.close();
				}
				if(null!=outputStream){
					outputStream.close();
				}
				if(null!=reader){
					 reader.close();
				}
				if(null!=inputStream){
					inputStream.close();
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	/**
	 * 因为下载、解析、同步一系列过程比较耗时，
	 * 这是开启一个线程执行这一过程
	 */
	public void updateHtlCommentDaoDaoTable(){
		Thread updateHtlCommentDaoDaoTableThread = new Thread(this);
		updateHtlCommentDaoDaoTableThread.start();
	}
	
	public void run() {
	    downloadXmlFromDaoDao();
		List<DaoDaoBasicComment> daoDaoBasicComments = this.parseXml();
		if(null!=daoDaoBasicComments&&0!=daoDaoBasicComments.size()){
			log.info(updateDBStatus[0]);
            saveOrUpdateDaoDaoCommentsInDB(daoDaoBasicComments);
            OrParam orParam =systemDataService.getSysParamByName("IS_DAODAO_TASK_EXECUTIG");
            orParam.setValue("0");	
			systemDataService.updateSysParamByName(orParam);
            log.info(updateDBStatus[1]);
		}

             
    }
	public boolean hasDaoDaoCommentTBBeenInitted() {
	     if(0<daoDaoCommentDao.size()){
	    	 return true;
	     }else{
	    	 return false;
	     }
	        
	}
	public List<DaoDaoBasicComment> parseXml(){
		SAXParser parser = null;
        //构建SAXParser
        try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
        //实例化  DefaultHandler对象
        SaxXmlHandler handler = new SaxXmlHandler(); 
        //加载资源文件 转化为一个输入流
        InputStream stream =null;
        String filePath = InitServlet.SAVE_ROOTPATH+"//"+uploadPath+"//"+savePath+"//"+fileName;
        File file = new File(filePath);
        if(null!=file&&file.exists()){
       	 try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				log.error(parseXmlPathError+":"+filePath,e);
				e.printStackTrace();
			} 
        }              
        //调用parse()方法
        if(null!=stream){
       	  try {
				parser.parse(stream, handler);
			} catch (SAXException e) {
               if(SaxXmlHandler.NOTFOUND.equals(e.getMessage())){
                	log.info(SaxXmlHandler.NOTFOUND,e);                    	
               }else{
               	e.printStackTrace();
               }
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(null!=stream){
						stream.close();
					}							
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
 			  return handler.getBasicComments(); 
        }else{
       	 log.error(parseXmlFailed);
       	 return null;
        }
	}
	
	
	
	public boolean contains(Object entitiy) {
		return daoDaoCommentDao.contains(entitiy);
	}
	public void saveOrUpdateDaoDaoCommentsInDB(List<DaoDaoBasicComment> daoDaoBasicComments){
		
		daoDaoCommentDao.saveOrUpdateDaoDaoComments(daoDaoBasicComments);
	}


	public IDaoDaoCommentDao getDaoDaoCommentDao() {
		return daoDaoCommentDao;
	}

	public void setDaoDaoCommentDao(IDaoDaoCommentDao daoDaoCommentDao) {
		this.daoDaoCommentDao = daoDaoCommentDao;
	}
	public SystemDataService getSystemDataService() {
		return systemDataService;
	}
	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}
    
	
		
	
	
	
}

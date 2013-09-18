package com.mangocity.webnew.service.impl;

import java.io.File;
import java.util.Date;
import java.util.TimerTask;

import com.mangocity.framework.util.DateTimeUtils;
import com.mangocity.util.log.MyLog;


public class DeleteTempFileTask extends TimerTask{
	 private static final MyLog log = MyLog.getLogger(DeleteTempFileTask.class);        
	 private static boolean isRunning = false;
	 private String realPath;
	 public DeleteTempFileTask(String realPath) {
		 this.realPath=realPath;
	}
	public void run() {
	  if (!isRunning) {
	   isRunning = true;
	   log.info("DeleteTempFileTask 开始执行任务..."+System.currentTimeMillis());
	   autoWorkOff(realPath);
	   log.info("DeleteTempFileTask 执行任务完成..."); //任务完成
	   isRunning = false;
	  } else {
	   log.info("上一次任务执行还未结束..."); //上一次任务执行还未结束
	  }
	 }
	public void autoWorkOff(String dir) {
//			如果dir不以文件分隔符结尾，自动添加文件分隔符      
        if(!dir.endsWith(File.separator)){      
            dir = dir+File.separator;      
        }      
        File dirFile = new File(dir);      
        //如果dir对应的文件不存在，或者不是一个目录，则退出      
        if(!dirFile.exists() || !dirFile.isDirectory()){      
        	log.info("删除目录失败"+dir+"目录不存在！");      
        }      
        boolean flag = true;      
        //删除文件夹下的所有文件(包括子目录)      
        File[] files = dirFile.listFiles();  
        if(null == files || 1 > files.length) {
        	return ;
        }
        for(int i=0;i<files.length;i++){      
            //删除子文件      
            if(files[i].isFile()){      
            	String fileName=files[i].getName();
            	String beginTime=fileName.substring(fileName.lastIndexOf("_")+1, fileName.indexOf(".kml"));
            	String nowTime=DateTimeUtils.formatDate(new Date(), "yyyyMMddHHmmssms");
            	//只删除一个小时之前生成的文件
            	if(Long.parseLong(nowTime)-Long.parseLong(beginTime)>1000000){
            		flag = deleteFile(files[i].getAbsolutePath());      
            	}
            	if(!flag){      
                    break;      
                }
            }      
        }      
		
	}
	 public  boolean deleteFile(String fileName){      
       File file = new File(fileName);      
       if(file.isFile() && file.exists()){      
           file.delete();      
           log.info("删除单个文件"+fileName+"成功！");      
           return true;      
       }else{      
    	   log.info("删除单个文件"+fileName+"失败！");      
           return false;      
       }      
    }
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

}

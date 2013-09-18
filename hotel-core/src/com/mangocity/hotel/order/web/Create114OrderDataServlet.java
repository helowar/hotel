package com.mangocity.hotel.order.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.hotel.order.service.IOrderService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;
import com.mangocity.util.web.WebUtil;

/**
 * 系统初始化器，需要将其配置为自动执行的Servlet
 */
public class Create114OrderDataServlet extends HttpServlet {
	private static final MyLog log = MyLog.getLogger(Create114OrderDataServlet.class);
    public static File configFile = null;

    private OrOrderDao orOrderDao;

    private IOrderService orderService;

    // OrderDao orderDao = (OrderDao) Framework.getDaoFactory().getBean("OrderDao");

    /**
     * 根据配置初始化系统
     * 
     * @param config
     *            配置
     * @throws ServletException
     *             如果启动失败
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = this.getServletContext();
        orderService = (IOrderService) WebUtil.getBean(servletContext, "orderService");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.CHINA);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.HOUR_OF_DAY);
        calendar.get(Calendar.MINUTE);

        Date date = null;
        try {
            date = sf.parse("" + year + "/" + month + "/" + day + " " + "01:10:00");
        } catch (ParseException e) {
        	log.error(e.getMessage(),e);
        }

        // 测试
        // Date curDate=new Date();

        Date curDate = new Date(date.getTime() + 86400000);

        // Date curDate=new Date(date.getTime());
        Reate114OrderDataTimerTask tempMapTimerTask = new Reate114OrderDataTimerTask();
        tempMapTimerTask.setServletContext(servletContext);

        Timer timer = new Timer();

        timer.schedule(tempMapTimerTask, curDate, 3000);
        // timer.schedule(tempMapTimerTask, curDate, 1000*60*10);
        // timer.schedule(tempMapTimerTask, new Date(new Date().getTime()+1000*40), 1000*50);
    }

    ///**
//     * 应用结束时进行必要的销毁操作
//     */
//    public void destroy() {
//        super.destroy();
//        // Scheduler.getInstance().shutdown();
//    }


    private class Reate114OrderDataTimerTask extends TimerTask {

        private ServletContext servletContext = null;

        public ServletContext getServletContext() {
            return servletContext;
        }

        public void setServletContext(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        public void run() {
           
            if (null == servletContext) {
            	log.info("servletContext==null");
            } else {
            	log.info("---servletContext!=null");
                try {

                    long m = Double.valueOf((Math.random() * 30000)).longValue();
                    log.info("random-sleep:" + m);
                    try {
                        Thread.sleep(m);
                    } catch (InterruptedException e1) {
                    	log.error(e1.getMessage(),e1);
                    }

                    String dir = servletContext.getRealPath("/");
                    log.info("servletContext.getRealPath(/):" + dir);
                    log.error("hwlhwlhwl  servletContext.getRealPath(/):" + dir);
                    // dir=dir+"/";
                    // 时间
                    Date now = new Date();
                    Date subDate = DateUtil.minusDate(now, 1);
                    Date subDate1 = DateUtil.minusDate(now, 3);

                    String dateString = DateUtil.toStringByFormat(subDate, "yyyy-MM-dd");
                    String dateString1 = DateUtil.toStringByFormat(subDate1, "yyyy-MM-dd");

                    Date subDateShort = null;
                    Date subDateShort1 = null;

                    try {
                        subDateShort = DateUtil.toDateByFormat(dateString, "yyyy-MM-dd");
                        subDateShort1 = DateUtil.toDateByFormat(dateString1, "yyyy-MM-dd");
                    } catch (Exception e) {
                    	log.error(e.getMessage(),e);
                    }

                    String docmentNameOrder1 = DateUtil.toStringByFormat(subDate1, "yyyyMMdd")
                        + "mangoorder";
                    String docmentNameDailyAuth1 = DateUtil.toStringByFormat(subDate1, "yyyyMMdd")
                        + "check";
                    // 在本地临时目录下先生产文件

                    String txtnameOrder1 = dir + "download114" + File.separator + docmentNameOrder1
                        + ".txt";
                    String txtnameDailyAuth1 = dir + "download114" + File.separator
                        + docmentNameDailyAuth1 + ".txt";
                    log.info(txtnameOrder1);
                    log.info(txtnameDailyAuth1);
                    File tmpPathOrder1 = new File(txtnameOrder1);
                    File tmpPathDailyAuth1 = new File(txtnameDailyAuth1);
                    // EmailFax/FAXXML
                    log.info(tmpPathOrder1.exists());
                    if (!tmpPathOrder1.exists()) {
                    	log.info("!tmpPathOrder1.exists():" + txtnameOrder1);
                        createOrderDate(subDateShort1, dir);
                        log.info("create ok:" + txtnameOrder1);
                    }

                    if (!tmpPathDailyAuth1.exists()) {
                    	log.info("!tmpPathDailyAuth1.exists():" + txtnameDailyAuth1);
                        createDailyAultDate(subDateShort1, dir);
                        log.info("create ok:" + txtnameDailyAuth1);
                    }

                    String docmentNameOrder = DateUtil.toStringByFormat(subDate, "yyyyMMdd")
                        + "mangoorder";
                    String docmentNameDailyAuth = DateUtil.toStringByFormat(subDate, "yyyyMMdd")
                        + "check";

                    String txtnameOrder = dir + "download114" + File.separator + docmentNameOrder
                        + ".txt";
                    String txtnameDailyAuth = dir + "download114" + File.separator
                        + docmentNameDailyAuth + ".txt";
                    log.info(txtnameOrder);
                    log.info(txtnameDailyAuth);
                    File tmpPathOrder = new File(txtnameOrder);
                    File tmpPathDailyAuth = new File(txtnameDailyAuth);
                    // EmailFax/FAXXML
                    if (!tmpPathOrder.exists()) {
                    	log.info("!tmpPathOrder.exists():" + txtnameOrder);
                        createOrderDate(subDateShort, dir);
                        log.info("create ok:" + txtnameOrder);
                    }

                    if (!tmpPathDailyAuth.exists()) {
                    	log.info("!tmpPathDailyAuth.exists():" + txtnameDailyAuth);
                        createDailyAultDate(subDateShort, dir);
                        log.info("create ok:" + txtnameDailyAuth);

                    }

                    // if newDate=2007-06-07 01:00
                    // 则subDateShort=2007-06-06

                    // 创建订单数据文件
                    // createOrderDate(subDateShort,dir); //前一天生成的所有广东114订单。

                    // createDailyAultDate(subDateShort,dir); //点完成后，记录日审的日期

                } catch (Exception e) {
                	log.error(e.getMessage(),e);
                }

            }
        }

        private void createOrderDate(Date subdate, String add) throws IOException {

            // List order114List= orderDao.queryHotelOrder114(subdate);
            Date subdate1 = DateUtil.getDate(subdate, 1);
            List order114List = orderService.queryHotelOrder114(subdate, subdate1);
          

            String dateString = DateUtil.toStringByFormat(subdate, "yyyyMMdd");
            String docmentName1 = dateString + "mangoorder";
            // 在本地临时目录下先生产文件

            String dir = add + "download114";
            // File tmpPath = new File(dir);
            // EmailFax/FAXXML
            /*
             * if (!tmpPath.exists()) { tmpPath.mkdir(); }
             */
            String xmlFilePath = dir + File.separator + docmentName1 + ".txt";

            // /CTIIWeb/WebContent/download114/20070606check.txt
            File xmlFile = new File(xmlFilePath);
            OutputStreamWriter writer = null;
            // File fNew = new File(application.getRealPath("/")+"/TMC_REPORT/", saveFileName);

            try {
                xmlFile.createNewFile();

                 writer = new OutputStreamWriter(new FileOutputStream(xmlFile),
                    "GBK");

                String pXmlContent = return114OrderTxt(order114List);// "文件内容";

                writer.write(pXmlContent);
                writer.close();

            } catch (IOException e) {
            	log.error(e.getMessage(),e);
                writer.close();
            }finally{
                writer.close();
            }

        }

        private String return114OrderTxt(List order114List) {
            String txtList = "";
            for (int i = 0; i < order114List.size(); i++) {
                OrOrder oBean = (OrOrder) order114List.get(i);
                // BeanUtil.SetNullDefault(oBean);

                String order = "";
                if (null != oBean) {
                    order = "" + oBean.getMemberName() + "|" + oBean.getHotelName() + "|"
                        + oBean.getRoomTypeName() + "|" + oBean.getRoomQuantity() + "|"
                        + oBean.getSum() + "|" + oBean.getCheckinDate() + "|"
                        + oBean.getCheckoutDate() + "|" + oBean.getCreateDate() + "|"
                        + oBean.getModifiedTime() + "|" + oBean.getOrderState() + "|"
                        + oBean.getLinkMan() + "|" + oBean.getMobile() + "|"
                        + oBean.isHotelConfirm() + "|" + oBean.getFellowNames() + "|"
                        + oBean.getOrderCD() + "\n";
                }
                txtList += order;
            }

            return txtList;

        }

        private void createDailyAultDate(Date subdate, String add) throws IOException {
            // List order114List= orderDao.queryHotelDailyAult114(subdate);
            Date subdate1 = DateUtil.getDate(subdate, 1);
            List order114List = orderService.queryHotelDailyAult114(subdate, subdate1);
          
            String dateString = DateUtil.toStringByFormat(subdate, "yyyyMMdd");
            String docmentName1 = dateString + "check";
            // 在本地临时目录下先生产文件
            String dir = add + "download114";
            /*
             * File tmpPath = new File(dir); //EmailFax/FAXXML if (!tmpPath.exists()) {
             * tmpPath.mkdir(); }
             */
            String xmlFilePath = dir + File.separator + docmentName1 + ".txt";

            OutputStreamWriter writer=null;
            File xmlFile = new File(xmlFilePath);
            try {
                xmlFile.createNewFile();

                writer = new OutputStreamWriter(new FileOutputStream(xmlFile),
                    "GBK");

                String pXmlContent = return114DailyAultTxt(order114List);// "文件内容";

                writer.write(pXmlContent);
                writer.close();

            } catch (IOException e) {
            	log.error(e.getMessage(),e);
                writer.close();
            }finally{
                writer.close();
            }

        }

        private String return114DailyAultTxt(List order114List) {
            String txtList = "";
            for (int i = 0; i < order114List.size(); i++) {
                OrOrderItem oBean = (OrOrderItem) order114List.get(i);
                // BeanUtil.SetNullDefault(oBean);

                String order = "";
                if (null != oBean) {
                    order = "" + oBean.getOrder().getMemberName() + "|"
                        + oBean.getOrder().getHotelName() + "|"
                        + oBean.getOrder().getRoomTypeName() + "|" + oBean.getOrder().getSum()
                        + "|" + oBean.getSalePrice() + "|" + oBean.getOrder().getRoomQuantity()
                        + "|" + oBean.getNight() + "|" + oBean.getOrder().getFellowNames() + "|"
                        + oBean.getNoteTime() + "|" + oBean.getOrder().getOrderCD() + "\n";
                }
                txtList += order;
            }

            return txtList;
        }

    }

    public OrOrderDao getOrOrderDao() {
        return orOrderDao;
    }

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }
}

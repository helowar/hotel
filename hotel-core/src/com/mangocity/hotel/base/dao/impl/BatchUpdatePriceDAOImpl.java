package com.mangocity.hotel.base.dao.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mangocity.hotel.base.dao.BatchUpdatePriceDAO;
import com.mangocity.hotel.base.persistence.BatchUpdateParam;
import com.mangocity.hotel.base.persistence.HtlBatchSalePrice;
import com.mangocity.hotel.base.persistence.HtlContract;
import com.mangocity.hotel.base.persistence.HtlSupplierInfo;
import com.mangocity.hotel.user.UserWrapper;
import com.mangocity.util.log.MyLog;


/**
 * 批量更新价格 
 * @author donglei
 *
 */
public class BatchUpdatePriceDAOImpl extends HibernateDaoSupport implements BatchUpdatePriceDAO{
    
	private static final MyLog log = MyLog.getLogger(BatchUpdatePriceDAOImpl.class);

	public List<HtlSupplierInfo> getSupplyId(){
		
		List<HtlSupplierInfo> list = new ArrayList<HtlSupplierInfo>();
		Session session = null;
		
		try {
			 session = super.getSessionFactory().openSession();
			
			 String hql = "from HtlSupplierInfo";
			
			 Query query = session.createQuery(hql);
			
			 list = query.list();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		
		
		return list;
	}

	public List<String[]> getExportData(BatchUpdateParam bachUpdateParam) {
		
		List<String []> list = new ArrayList<String []>();
		Session session = null;
		DecimalFormat myformat = new  DecimalFormat("#####0.00");   

		try {
			 session = super.getSessionFactory().openSession();
			
			 StringBuffer sql= new StringBuffer();
			 
			 sql.append("select ");
			 sql.append("case ");
			 sql.append("when supplier_id > 0 then supplier_id ");
			 sql.append("else 0 end supplier_id,");
			 sql.append("case ");
			 sql.append("when supply_name is null then '酒店' ");
			 sql.append("else supply_name end supply_name,");
			 sql.append("ht.hotel_id,");
			 sql.append("ht.chn_name,");
			 sql.append("ht.room_type_id,");
			 sql.append("ht.room_name,");
			 sql.append("ht.price_type_id,");
			 sql.append("ht.price_type,");
			 sql.append("p.commission ");
			 sql.append("from ");
			 sql.append("(select supplier_id, ");
			 sql.append("(select hs.name from HTL_SUPPLIERINFO hs where hs.id = hpt.supplier_id) supply_name,");
			 sql.append("hh.hotel_id,");
			 sql.append("hh.chn_name,");
			 sql.append("hr.room_type_id,");
			 sql.append("hr.room_name,");
			 sql.append("hpt.price_type_id,");
			 sql.append("hpt.price_type ");
			 sql.append("from ");
			 sql.append("htl_price_type hpt, htl_roomtype hr, htl_hotel hh ");
			 sql.append("where ");
			 sql.append("hr.hotel_id = hh.hotel_id ");
			 sql.append("and hpt.room_type_id = hr.room_type_id ");
			 sql.append("and hh.active = 1 ");
			 sql.append("and hh.hotel_system_sign = '01' ");
			 if(bachUpdateParam.getProductManager() != null && bachUpdateParam.getProductManager().length()!=0){
				   sql.append("and hh.product_manager like '%"+(bachUpdateParam.getProductManager()).trim()+"%' ");
			 }
			 sql.append("and hh.state = '"+bachUpdateParam.getProvince()+"' ");
			 sql.append("and hh.city = '"+bachUpdateParam.getCity()+"' ");
			 sql.append(") ht,");
			 sql.append("hwtemp_htl_price p ");
			 sql.append("where ");
			 sql.append("p.child_room_type_id = ht.price_type_id ");
			 sql.append("and p.room_type_id = ht.room_type_id ");
			 sql.append("and p.able_sale_date = trunc(sysdate) ");
			 sql.append("and p.hotel_id =ht.hotel_id ");
			 if(bachUpdateParam.getSupplyId() != null && bachUpdateParam.getSupplyId().length()!=0){
			 sql.append("and ht.supplier_id="+bachUpdateParam.getSupplyId()+" ");
			 }
			 						 
			 if(bachUpdateParam.getSupplyId()!=null  && bachUpdateParam.getSupplyId().trim().length()!=0){
				 sql.append("order by ht.supplier_id,ht.hotel_id ");
			 }else{
				 sql.append("order by ht.hotel_id ");
			 }
			 
			 log.debug(sql.toString());
			 
			 Query query = session.createSQLQuery(sql.toString());
			
			 List<Object []>  listObj = query.list();
			 
			 for(Object [] resultObj : listObj){
				 String [] obj = new String[9];
				 for(int n=0;n<=8;n++){
					 if(n==8){
					  obj[n]=(resultObj[n]!=null ? myformat.format(resultObj[n]).toString() : null);
					  continue;
					 }
					  obj[n]=(resultObj[n]!=null ? resultObj[n].toString() : null);
				 }
				 list.add(obj);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.flush();
			session.clear();
			session.close();
		}
		
		
		return list;
	}

	public void batchUpdatePrice(List<String[]> importData,UserWrapper roleUser,BatchUpdateParam bachUpdateParam) {
		
		Session session = null;
		
		Integer index=0;
		for(String[] data : importData){
			String val_data="";
			if(index++==0){
				continue;
			}
			
			for(int m=0 ; m<=29 ; m++){
				try {
					
					//必须得更新修改人和时间
					session = super.getSessionFactory().openSession();
																													
					if(data[9+m]!=null && data[9+m].trim().length()>0 ){
					
					if(val_data.equals("")){
						val_data=data[8];
					}
						
					if(data[9+m].equals("X") || data[9+m].equals("x")){
						data[9+m]="0";
						data[8]="0";
					}else{
						data[8]=val_data;
					}
					
					Integer result_flag=0;				

					
					
					StringBuffer sql = new StringBuffer();		
					sql.append("update ");
					sql.append("htl_price h set ");
					sql.append("h.salesroom_price=0,");
					sql.append("h.net_price=0,");
					sql.append("h.formulaid=0,");
					sql.append("h.commission="+Float.parseFloat(data[8])+",");
					sql.append("h.base_price="+data[9+m]+",");
					if(!data[8].equals("0")){
					sql.append("h.sale_price="+(Float.parseFloat(data[9+m])+Float.parseFloat(data[8])+0.01)+",");//芒果售价=底价+佣金
					sql.append("h.commission_rate="+(Float.parseFloat(data[8])/(Float.parseFloat(data[9+m])+Float.parseFloat(data[8])))+",");//佣金率=佣金/售价
					}else{
						sql.append("h.sale_price="+0+",");
						sql.append("h.commission_rate="+0+",");//佣金率=佣金/售价

					}
					sql.append("h.inc_breakfast_price=0,");
					sql.append("h.inc_breakfast_number=0, ");
					sql.append("h.modify_by_id='"+roleUser.getLoginName()+"',");
					sql.append("h.modify_by='"+roleUser.getName()+"',");
					sql.append("h.modify_time=sysdate ");
					sql.append("where ");
					sql.append("h.able_sale_date=to_date('"+importData.get(0)[9+m]+"','yyyy/MM/dd') ");
					sql.append("and h.child_room_type_id="+data[6]+" ");
					sql.append("and h.room_type_id="+data[4]+" ");
					sql.append("and h.hotel_id="+data[2]+" ");		
					sql.append("and h.pay_method='"+bachUpdateParam.getPayMethod()+"'");
					sql.append("and exists (select 'x' from htl_hotel hh where hh.state='"+bachUpdateParam.getProvince()+"' and hh.city='"+bachUpdateParam.getCity()+"' and hh.hotel_id="+data[2]+") ");
					
					
					if(bachUpdateParam.getSupplyId() != null && !bachUpdateParam.getSupplyId().equals("0")){
					 sql.append("and exists (select 'x' from htl_price_type hpt where hpt.price_type_id="+data[6]+" and hpt.room_type_id="+data[4]+" and hpt.supplier_id ="+bachUpdateParam.getSupplyId()+"  ) ");
					}
					
					if(bachUpdateParam.getHotelName() != null && bachUpdateParam.getHotelName().trim().length()>0){
						sql.append("and exists (select 'x' from htl_hotel hh where hh.chn_name like '%"+(bachUpdateParam.getHotelName()).trim()+"%' and hh.hotel_id="+data[2]+" ) ");
					}
					
					if(bachUpdateParam.getProductManager() != null && bachUpdateParam.getProductManager().trim().length()>0){
						sql.append("and exists (select 'x' from htl_hotel hh where hh.product_manager like '%"+(bachUpdateParam.getProductManager()).trim()+"%' and hh.hotel_id="+data[2]+" ) ");
					}
					
					log.debug(sql.toString());
					
					result_flag=session.createSQLQuery(sql.toString()).executeUpdate();
					
					//如果更新了就写日志
					if(result_flag>=0){												
						
						//查询htl_price 表信息
						StringBuffer sql1= new StringBuffer();
						sql1.append("select inc_breakfast_type,return_comm,can_add_scope,advice_price,service_charge,commission,can_add_scope,commission_rate from htl_price h ");
						sql1.append(sql.toString().toString().substring(sql.toString().indexOf("where")));
						
						log.debug(sql1.toString());
						Object obj=null;
						if((obj= session.createSQLQuery(sql1.toString()).list())!=null){
						 List<Object []> priceData = (List<Object []>)obj;
						 if(priceData.size()>0){
							//查询合同信息
							 String hql = "from HtlContract where hotelId="+data[2];
							 HtlContract htlContract = (HtlContract) session.createQuery(hql).uniqueResult();
							 
							 HtlBatchSalePrice htlBatchSalePrice = new HtlBatchSalePrice();
							 htlBatchSalePrice.setContractId(htlContract.getID());
							 htlBatchSalePrice.setBeginDate(htlContract.getBeginDate());
							 htlBatchSalePrice.setEndDate(htlContract.getEndDate());
							 htlBatchSalePrice.setAdjustWeek(",");	
							 htlBatchSalePrice.setRoomTypeId(Long.parseLong(data[4].toString()));
							 htlBatchSalePrice.setChildRoomTypeId(Long.parseLong(data[6]));
							 htlBatchSalePrice.setPayMethod("pre_pay");
							 htlBatchSalePrice.setAdvicePrice(priceData.get(0)[3]!=null ? Double.parseDouble(priceData.get(0)[3].toString()) : 0.00);
							 htlBatchSalePrice.setBasePrice(Double.parseDouble(data[9+m]));
							 htlBatchSalePrice.setSalesroomPrice(0.0);
							 htlBatchSalePrice.setServiceCharge(priceData.get(0)[4]!=null ? Double.parseDouble(priceData.get(0)[4].toString()) : 0.0);
							 if(!data[8].equals("0")){
							 htlBatchSalePrice.setSalePrice((Float.parseFloat(data[9+m])+Float.parseFloat(data[8])+0.01));
							 }else{
								 htlBatchSalePrice.setSalePrice(0);
							 }
							 htlBatchSalePrice.setCommission(priceData.get(0)[5]!=null ? Double.parseDouble(priceData.get(0)[5].toString()) : 0.0);
							 htlBatchSalePrice.setIncBreakfastNumber("0");							 							 
							 htlBatchSalePrice.setIncBreakfastType(priceData.get(0)[0]!=null ? priceData.get(0)[0].toString() : null);
							 htlBatchSalePrice.setIncBreakfastPrice(0);
							 htlBatchSalePrice.setReturnComm(false);
							 htlBatchSalePrice.setCanAddScope(priceData.get(0)[6]!=null && Integer.parseInt(priceData.get(0)[6].toString())==0 ? false : true);
							 htlBatchSalePrice.setPreCalcFormula("0");
							 htlBatchSalePrice.setCreateById(roleUser.getLoginName());
							 htlBatchSalePrice.setCreateBy(roleUser.getName());
							 htlBatchSalePrice.setModifyById(roleUser.getLoginName());
							 htlBatchSalePrice.setModifyBy(roleUser.getName());
							 htlBatchSalePrice.setCreateTime(new Date());
							 htlBatchSalePrice.setModifyTime(new Date());
							 htlBatchSalePrice.setCommissionRate(priceData.get(0)[7]!=null ? Double.parseDouble(priceData.get(0)[7].toString()) : 0.0);
							 htlBatchSalePrice.setUpdateFlag("S");
							 
							 session.save(htlBatchSalePrice);
						 }
						}

						
					}
					
					}
				} catch (Exception e) {
					log.error("------------------------第"+index+"出错了!--------------------------");
					log.error("----------hotel_id="+data[2]+"---child_room_type_id="+data[4]+"---room_type_id="+data[6]+"---able_sale_date="+importData.get(0)[9+m]+"-----------");
					e.printStackTrace();
				}finally{
					session.flush();
					session.close();
				}
				
				
			}
		}				
	}
	
	
	
	

}

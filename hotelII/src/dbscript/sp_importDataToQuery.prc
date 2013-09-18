create or replace procedure sp_importDataToQuery(startdate in varchar2,  endDate in varchar2,citycode in varchar2, opcount out number) is
 -- sql
 i_roomtypesql varchar2(4000);
 i_pricesql varchar2(4000);
 i_quotasql varchar2(4000);
  --insertsql;
 insertsql varchar2(4000);
 --查询记录集定义
 type query_record_type is record(
       hotelid numeric(12),
       roomtypeid numeric(12),
       pricetypeid numeric(12),
       isctshotel char(1),
       priceid numeric(12),
       bedtype char(1),
       abledate date,
       sale_channel varchar2(128),
       closeflag varchar2(2),
       paymethod varchar2(16),
       pricetype varchar2(256),
       roomname varchar2(256),
       hdlchannel varchar2(20),
       saleprice numeric(10,2),
       salesroomprice numeric(10,2),
       inc_breakfast_number numeric(3),
       inc_breakfast_type numeric(1),
       inc_breakfast_price numeric(10,2),
       currency varchar2(32),
       pay_to_prepay char(1),
       first_bookable_date date,
       latest_bookable_date date,
       first_bookable_time  varchar2(10),
       latest_bokable_time varchar2(10),
       continueday numeric(2),
       must_last_date date,
       must_first_date date,
       must_in_date  varchar2(128),
       max_restrict_nights numeric(2),
       continue_dates_relation varchar2(10),
       need_assure char(1),
       room_state varchar2(64)
 );
 
 type curPrice_cursor is REF CURSOR; --定义Price游标
 curPriceCursor curPrice_cursor;
 
 query_price query_record_type;
 
 --定义中旅渠道记录
  type CTLDSPLYRECORD is record(
       ctlid HTL_CTL_DSPLY.CTLID%type,
       cc HTL_CTL_DSPLY.Cc%type,
       tp HTL_CTL_DSPLY.Tp%type,
       tmc HTL_CTL_DSPLY.Tmc%type,
       web HTL_CTL_DSPLY.Web%type,
       agents HTL_CTL_DSPLY.Agent%type
 );
 ctldsply CTLDSPLYRECORD;
 
 distchannel varchar2(128) default ''; --销售渠道
 --直联合作方
 hdltype varchar2(20);
 --表名,城市
 tablename varchar2(30);
 curcity varchar2(10);
--变量
 l_cnt   int default 0;
 u_cnt  int default 0;

 --quota游标处理
 type queryQuota is record(
   quotaid Htl_quota_new.Htl_Quota_New_Id%type,
   hotelid Htl_Quota_New.Hotel_Id%type,
   roomtypeid Htl_Quota_New.Roomtype_Id%type,
   abledate Htl_Quota_New.Able_Sale_Date%type,
   bedtype Htl_Quota_New.Bed_Id%type,
   sharetype Htl_Quota_New.Quota_Share_Type%type,
   buy_able_num Htl_Quota_New.Buy_Quota_Able_Num%type,
   common_able_num Htl_Quota_New.Common_Quota_Able_Num%type,
   casual_able_num Htl_Quota_New.Casual_Quota_Able_Num%type,
   buy_outof_num Htl_Quota_New.Buy_Quota_Outofdate_Num%type,
   common_outof_num Htl_Quota_New.Common_Quota_Outofdate_Num%type,
   casual_outof_num Htl_Quota_New.Casual_Quota_Outofdate_Num%type
 );
 type curQuota_cursor is REF CURSOR; --定义quota游标
 
 curQuotaCursor curQuota_cursor;
 
 curquotarecord queryQuota;
 
 curquotanum int  default 0;--当前配额数
 curquotasql varchar2(1024) default ''; --当前配额sql
 l_errmsg    varchar2(512);--错误信息
 
begin
  

       
      if upper(citycode)='PEK'or upper(citycode)='SHA' or upper(citycode)='CAN'or upper(citycode)='SZX'then
           tablename :='HTLQUERY_'||citycode;
           curcity := upper(citycode);
      else
          tablename :='HTLQUERY_OTHER';
      end if;
                
                
       --将房型记录转化成以床型为单位的记录,一种床型
       i_roomtypesql :=i_roomtypesql||' select rp.hotel_id,rp.room_type_id,rp.bed_type,rp.room_name from  htl_roomtype rp where length(rp.bed_type)=1 union';


       --将房型记录转化成以床型为单位的记录,两种床型

       i_roomtypesql :=i_roomtypesql||'(select rp.hotel_id, rp.room_type_id, substr(rp.bed_type,1,1) bed_type,rp.room_name from htl_roomtype rp where length(bed_type)=3) union ';
       
       i_roomtypesql :=i_roomtypesql||' (select rp.hotel_id, rp.room_type_id, substr(rp.bed_type,3,1) bed_type,rp.room_name from htl_roomtype rp where length(bed_type)=3) union ';

       --将房型记录转化成以床型为单位的记录,三种床型

       i_roomtypesql :=i_roomtypesql||' (select  rp.hotel_id,rp.room_type_id, substr(rp.bed_type,1,1) bed_type,rp.room_name from htl_roomtype rp where length(bed_type)=5) union ';
       i_roomtypesql :=i_roomtypesql||' (select  rp.hotel_id,rp.room_type_id, substr(rp.bed_type,3,1) bed_type,rp.room_name from htl_roomtype rp where length(bed_type)=5) union ';
       i_roomtypesql :=i_roomtypesql||' (select  rp.hotel_id,rp.room_type_id, substr(rp.bed_type,5,1) bed_type,rp.room_name from htl_roomtype rp where length(bed_type)=5) ';


       --价格表sql语句,每天每种房型每种床型每种价格类型的不同支付方式的价格情况
       i_pricesql:= 'select hotel.hotel_id, ot.room_type_id, pt.price_type_id, ext.isctshotel,price.price_id,ot.bed_type,price.able_sale_date,
       hotel.sale_channel,price.close_flag,price.pay_method,pt.price_type,ot.room_name,ext.cooperate_channel,
       price.sale_price,price.salesroom_price,price.inc_breakfast_number,price.inc_breakfast_type,price.inc_breakfast_price,price.currency,
       price.pay_to_prepay,price.first_bookable_date,price.latest_bookable_date,price.first_bookable_time,price.latest_bokable_time,price.continue_day,
       price.must_last_date,price.must_first_date,price.must_in_date,price.max_restrict_nights,price.continue_dates_relation,price.need_assure,
       (case when ot.bed_type=''1''then
              substr(room.room_state,instr(room.room_state,''1:'')+2,1)
         when ot.bed_type=''2'' then 
              substr(room.room_state,instr(room.room_state,''2:'')+2,1) 
        when ot.bed_type=''3'' then 
             substr(room.room_state,instr(room.room_state,''3:'')+2,1)
        end) room_state
       from htl_hotel      hotel,
       htl_hotel_ext  ext,
       ('||i_roomtypesql||')   ot,
       htl_price_type pt,
       htl_price price,
       htl_room room 
       where hotel.hotel_id = ext.hotel_ext_id(+)
         and hotel.hotel_id = ot.hotel_id
         and ot.room_type_id = pt.room_type_id
         and pt.price_type_id=price.child_room_type_id 
         and price.able_sale_date>=to_date('''||startdate||''',''YYYY-MM-DD'')
         and price.able_sale_date <= to_date('''||enddate||''',''YYYY-MM-DD'')
         and hotel.active=''1''
         and room.room_id=price.room_id';
         if(curcity is not null) then
         i_pricesql:=i_pricesql||' and Upper(hotel.city)='''||curcity||'''';
         end if;

          open curPriceCursor for i_pricesql; --打开价格游标
          fetch curPriceCursor into query_price;
          while curPriceCursor%found loop
                --处理数据
                
                --获取销售渠道,默认为"",中旅酒店取HTL_CTL_DSPLY中设置的值
                if(query_price.isctshotel='1') then--是中旅酒店
                      select hcd.ctlid,
                             hcd.cc,
                             hcd.tp,
                             hcd.tmc,
                             hcd.web,
                             hcd.agent
                        into ctldsply.ctlid,
                             ctldsply.cc,
                             ctldsply.tp,
                             ctldsply.tmc,
                             ctldsply.web,
                             ctldsply.agents
                        from HTL_CTL_DSPLY hcd
                       where hcd.price_type_id = query_price.pricetypeid;
                       
                        case 
                          when ctldsply.cc=1 then
                            distchannel:=distchannel||'CC,';
                          when ctldsply.web=1 then
                            distchannel:=distchannel||'WEB,';
                        when ctldsply.tp=1 then
                            distchannel:=distchannel||'TP,';
                        when ctldsply.tmc=1 then
                           distchannel:=distchannel||'TMC_CC,TMC_WEB,';
                        when ctldsply.agents=1 then
                            distchannel:=distchannel||'B2B,';
                        end case;
                end if;
                if (substr(distchannel,length(distchannel))=',') then
                   distchannel := substr(distchannel,1,length(distchannel)-1);
                end if;

                --直联方式
                hdltype:=query_price.hdlchannel;
                if(query_price.isctshotel='1') then
                    hdltype:='8';
                end if;
                --更新到查询表中去

                --动态sql语句,execute immediate 
                insertsql:='insert into '||tablename ||'(
                                QUERYID,
                                ABLEDATE,
                                DISTCHANNEL,
                                USERTYPE,
                                CLOSEFLAG,
                                PAYMETHOD,
                                COMMODITYID,
                                COMMODITYNAME,
                                BEDTYPE,
                                ROOMTYPEID,
                                ROOMTYPENAME,
                                HOTELID,
                                HDLTYPE,
                                PRICEID,
                                SALEPRICE,
                                SALESROOMPRICE,
                                BREAKFASTTYPE,
                                BREAKFASTNUMBER,
                                BREAKFASTPRICE,
                                CURRENCY,
                                PAYTOPREPAY,
                                CONTINUEDAY,
                                BOOKSTARTDATE,
                                BOOKENDDATE,
                                MORNINGTIME,
                                EVENINGTIME,
                                CONTINUUM_IN_END,
                                CONTINUUM_IN_START,
                                MUST_IN,
                                RESTRICT_IN,
                                CONTINUE_DATES_RELATION,
                                NEED_ASSURE,
                                HASBOOK,
                                HASOVERDRAFT
                
                )values(
                         SEQ_HTLQUERY_OTHER.NEXTVAL,

                         to_date('''||to_char(query_price.abledate,'yyyy-mm-dd')||''',''yyyy-mm-dd''),'''||
                         distchannel||''','''||
                         query_price.sale_channel||''','''||
                         query_price.closeflag||''','''||
                         query_price.paymethod||''','||
                         query_price.pricetypeid||','''||
                         query_price.pricetype||''','''||
                         query_price.bedtype||''','||
                         query_price.roomtypeid||','''||
                         query_price.roomname||''','||
                         query_price.hotelid||','''||
                         hdltype||''','||
                         query_price.priceid||','||
                         nvl(query_price.saleprice,0)||','||
                         nvl(query_price.salesroomprice,0)||','||
                         nvl(query_price.inc_breakfast_type,0)||','||
                         nvl(query_price.inc_breakfast_number,0)||','||
                         nvl(query_price.inc_breakfast_price,0)||','''||
                         query_price.currency||''','''||
                         query_price.pay_to_prepay||''','||
                         nvl(query_price.continueday,0)||',to_date('''||
                         nvl(to_char(query_price.first_bookable_date,'yyyy-mm-dd'),'1900-01-01')||''',''yyyy-mm-dd''),to_date('''||
                         nvl(to_char(query_price.latest_bookable_date,'yyyy-mm-dd'),'2099-12-31')||''',''yyyy-mm-dd''),'''||
                         query_price.first_bookable_time||''','''||
                         query_price.latest_bokable_time||''',to_date('''||
                         nvl(to_char(query_price.must_last_date,'yyyy-mm-dd'),'2099-12-31')||''',''yyyy-mm-dd''),to_date('''||
                         nvl(to_char(query_price.must_first_date,'yyyy-mm-dd'),'1900-01-01')||''',''yyyy-mm-dd''),'''||
                         query_price.must_in_date||''','||
                         nvl(query_price.max_restrict_nights,0)||','''||
                         query_price.continue_dates_relation||''','''||
                         query_price.need_assure||''','||
                         '(case when '''||query_price.room_state||'''=''0'' or '''||query_price.room_state||'''=''1'' or ''' || query_price.room_state||'''=''2'' or '''||query_price.room_state||'''=''3''
                                   then ''1''
                               when '''||query_price.room_state||'''=''4''
                                   then ''0''
                          end),'||
                          '(case when '''||query_price.room_state||'''=''0'' or '''||query_price.room_state||'''=''1'' or ''' || query_price.room_state||'''=''2''
                                   then ''1''
                               when  '''||query_price.room_state||'''=''3'' or '''||query_price.room_state||'''=''4''
                                   then ''0''
                          end)
                          )';
                
                
                execute immediate insertsql;
             if mod (l_cnt, 1000) = 0 and l_cnt >= 1000 then--批量提交
                commit;
              end if;
              l_cnt := l_cnt + 1;
              fetch curPriceCursor into query_price;
           end loop;
           commit;
          close curPriceCursor; --关闭价格游标
          opcount:=l_cnt;--增加的行数
      
      --配额的sql,每天每房型每床型不同支付方式的配额情况,用游标循环更新查询表
      i_quotasql:='select hqn.htl_quota_new_id,
             hqn.hotel_id,
             hqn.roomtype_id,
             hqn.able_sale_date,
             hqn.bed_id,
             hqn.quota_share_type,
             hqn.buy_quota_able_num,
             hqn.common_quota_able_num,
             hqn.casual_quota_able_num,
             hqn.buy_quota_outofdate_num,
             hqn.common_quota_outofdate_num,
             hqn.casual_quota_outofdate_num
        from htl_quota_new hqn
       where hqn.able_sale_date >= to_date('''||startdate||''',''YYYY-MM-DD'')
         and hqn.able_sale_date <= to_date('''||enddate||''',''YYYY-MM-DD'')';
         
         --处理游标
          open curQuotaCursor for i_quotasql; --打开价格游标
          fetch curQuotaCursor into curquotarecord;
          while curQuotaCursor%found loop
          curquotanum:=(nvl(curquotarecord.buy_able_num,0)+nvl(curquotarecord.common_able_num,0)+nvl(curquotarecord.casual_able_num,0))-
                        (nvl(curquotarecord.buy_outof_num,0)+nvl(curquotarecord.common_outof_num,0)+nvl(curquotarecord.casual_outof_num,0));
                        
           case when curquotarecord.sharetype=1--面付
                  then
                    curquotasql:= 'update '||tablename||' hq
                        set hq.QUOTANUMBER = nvl(hq.QUOTANUMBER,0) +'||
                                             curquotanum||'
                      where hq.abledate = to_date('''||to_char(curquotarecord.abledate,'yyyy-mm-dd')||''',''yyyy-mm-dd'')
                      and hq.roomtypeid='||curquotarecord.roomtypeid||'
                      and hq.bedtype='''||curquotarecord.bedtype||''' 
                      and upper(hq.paymethod)=''PAY''';
                when curquotarecord.sharetype=2--预付
                  then
                    curquotasql:= 'update '||tablename||' hq
                        set hq.QUOTANUMBER = nvl(hq.QUOTANUMBER,0) +'||
                                             curquotanum||'
                      where hq.abledate = to_date('''||to_char(curquotarecord.abledate,'yyyy-mm-dd')||''',''yyyy-mm-dd'')
                      and hq.roomtypeid='||curquotarecord.roomtypeid||'
                      and hq.bedtype='''||curquotarecord.bedtype||''' 
                      and upper(hq.paymethod)=''PRE_PAY''';
                when curquotarecord.sharetype=3--面预付
                  then
                     curquotasql:= 'update '||tablename||' hq
                        set hq.QUOTANUMBER = nvl(hq.QUOTANUMBER,0) +'||
                                             curquotanum||'
                      where hq.abledate = to_date('''||to_char(curquotarecord.abledate,'yyyy-mm-dd')||''',''yyyy-mm-dd'')
                      and hq.roomtypeid='||curquotarecord.roomtypeid||'
                      and hq.bedtype='''||curquotarecord.bedtype||''' 
                      and (upper(hq.paymethod)=''PAY'' or upper(hq.paymethod)=''PRE_PAY'')';
            end case;
            
            --执行sql
              execute immediate curquotasql;
             if mod (u_cnt, 1000) = 0 and u_cnt >= 1000 then--批量提交
                commit;
              end if;
              u_cnt:=u_cnt+1;
             fetch curQuotaCursor into curquotarecord;
          end loop;
          commit;
          close curQuotaCursor;
exception
  when others then
    l_errmsg := substr(sqlerrm, 1, 300);
    insert into err_log
    values
      (sysdate, 'htl_ii.importDataToQuery:' || insertsql);

end sp_importDataToQuery;

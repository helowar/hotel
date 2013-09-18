create or replace procedure sp_importDataToQuery(startdate in varchar2,  endDate in varchar2,citycode in varchar2, opcount out number) is
 -- sql
 --citycode,北上广深港分别对应PEK,SHA,CAN,SZX;杭州,成都,南京对应HGH,CTU,NKG;华东区对应除去上海,杭州,南京以外的华东城市,华北区对应除北京以外的华北城市
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
       room_state varchar2(64),
       commission  numeric(10,2),
       commissionrate  numeric(11,3),
       formulaid  varchar2(64)
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
 tempsql varchar2(1000);--临时sql
 --直联合作方
 hdltype varchar2(20);
 --表名
 tablename varchar2(30);
 --城市
 curcity varchar2(600);
 citytype varchar2(10);
 bigcity varchar2(500);
 hdqcitysql varchar2(500);
 hbqcitysql varchar2(500);
 
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
 curquotasql varchar2(30000) default ''; --当前配额sql
 l_errmsg    varchar2(512);--错误信息

begin

      bigcity :='''PEK'',''SHA'',''CAN'',''SZX'',''HKG'',''HGH'',''CTU'',''NKG''';--大城市
      --华东区
      hdqcitysql :='select distinct citycode from htl_area ha where ha.areacode=''HDQ'' and (ha.citycode<>''SHA'' or  ha.citycode<>''NKG''  or ha.citycode<>''HGH'')';
      --华北区
      hbqcitysql :='select distinct citycode from htl_area ha where ha.areacode=''HBQ'' and ha.citycode<>''PEK''';



      if (citycode is null or citycode ='') then
          tablename :='HTLQUERY_OTHER';
          citytype :='3';--小城市
      else
          tablename :='HTLQUERY_'||citycode;
           
           if(citycode='HDQ') then--华东
                 curcity := hdqcitysql;
                 citytype :='2';--区域
           ELSIF (citycode='HBQ') then--华北
                 curcity := hbqcitysql;
                 citytype :='2';--区域
            else
                 curcity :=upper(citycode);--具体城市
                 citytype :='1';--大城市
           end if;
      end if;
      
      --先删除过期的数据
      execute immediate 'delete '||tablename||' ht where ht.abledate<trunc(sysdate)';
      --先删除要导入的该城市的指定时间的数据
      execute  immediate 'delete '||tablename||' ht where ht.abledate>=to_date('''||startdate||''',''YYYY-MM-DD'') and ht.abledate<=to_date('''||enddate||''',''YYYY-MM-DD'')';

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
       (case when ot.bed_type=''1'' and instr(room.room_state,''1:'')>0 then
              substr(room.room_state,instr(room.room_state,''1:'')+2,1)
         when ot.bed_type=''2''  and instr(room.room_state,''2:'')>0 then
              substr(room.room_state,instr(room.room_state,''2:'')+2,1)
        when ot.bed_type=''3''  and instr(room.room_state,''3:'')>0 then
             substr(room.room_state,instr(room.room_state,''3:'')+2,1)
        end) room_state,price.commission,price.commission_rate,price.formulaid
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
         if (citytype='1') then--大城市
                    i_pricesql:=i_pricesql||' and Upper(hotel.city)='''||curcity||'''';
         elsif(citytype='2') then --区域
                     i_pricesql:=i_pricesql||' and Upper(hotel.city) in ('||curcity||')';        
         else--其他城市
                    i_pricesql:=i_pricesql||' and (Upper(hotel.city) not in ('||bigcity||') and Upper(hotel.city) not in ('||hdqcitysql||') and Upper(hotel.city) not in ('||hbqcitysql||'))';                    
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

           --动态sql语句,execute immediat
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
                                HASOVERDRAFT,
                                quotanumber,
                                COMMISSIONRATE,
                                COMMISSION,
                                FORMULA,
                                freeNet

                )values(
                         SEQ_'|| tablename ||'.NEXTVAL,
                         :1,:2,:3,:5,:6,:7,:8,:9,:10,:11,:12, :13,
                         :14,:15,:16,:17,:18,:19,:20,:21,:22,:23,:24,
                         :25,:26,:27,:28,:29,:30,:31,:32,:33,:34,
                         :35,:36,:37,:38,:39
                         )';
             
               
                execute immediate insertsql using                                                                                    
                  query_price.abledate,
                  distchannel,                                                                                                              
                  query_price.sale_channel ,                                                
                  query_price.closeflag  ,                                                  
                  query_price.paymethod   ,                                                 
                  query_price.pricetypeid  ,                                                
                  query_price.pricetype   ,                                                 
                  query_price.bedtype    ,                                                  
                  query_price.roomtypeid  ,                                                 
                  query_price.roomname   ,                                                  
                  query_price.hotelid   , 
                  hdltype,                                                                                                               
                  query_price.priceid    ,                                                  
                  nvl(query_price.saleprice,0) ,                                            
                  nvl(query_price.salesroomprice,0)  ,                                      
                  nvl(query_price.inc_breakfast_type,0)  ,                                  
                  nvl(query_price.inc_breakfast_number,0) ,                                 
                  nvl(query_price.inc_breakfast_price,0)  ,                                 
                  query_price.currency  ,                                                   
                  query_price.pay_to_prepay    ,                                            
                  nvl(query_price.continueday,0),                                           
                  nvl(query_price.first_bookable_date,to_date('1900-01-01','YYYY-MM-DD')),  
                  nvl(query_price.latest_bookable_date,to_date('2099-12-31','YYYY-MM-DD')), 
                  query_price.first_bookable_time,                                          
                  query_price.latest_bokable_time,                                          
                  nvl(query_price.must_last_date,to_date('2099-12-31','YYYY-MM-DD')),       
                  nvl(query_price.must_first_date,to_date('1900-01-01','YYYY-MM-DD')),      
                  query_price.must_in_date,                                                 
                  nvl(query_price.max_restrict_nights,0),                                   
                  query_price.continue_dates_relation,                                      
                  query_price.need_assure,                                                  
                   (case when query_price.room_state='0' or query_price.room_state='1' or query_price.room_state='2' or  query_price.room_state='3'
                                   then '1'
                         when query_price.room_state='4'
                                   then '0'
                    end),
                    (case when query_price.room_state='0' or query_price.room_state='1' or  query_price.room_state='2'
                                   then '1'
                          when  query_price.room_state='3' or query_price.room_state='4'
                                   then '0'
                    end),
                    0,
                    query_price.commissionrate,
                    query_price.commission,
                    query_price.formulaid,
                    0--暂时宽带信息未实现
                    ;
              
                          
              if mod (l_cnt, 10000) = 0 and l_cnt >= 10000 then--批量提交
                commit;
              end if;
              l_cnt := l_cnt + 1;
               fetch curPriceCursor into query_price;
           end loop;
           commit;
           close curPriceCursor;
          --关闭价格游标
           opcount:=l_cnt;
           --增加的行数


         --配额的sql,每天每房型每床型不同支付方式的配额情况,用游标循环更新查询表
         --打开价格游标
         tempsql:=' from htl_quota_new hqn where hqn.able_sale_date=hq.abledate   and   hqn.roomtype_id=hq.roomtypeid 
 and   hqn.bed_id=hq.bedtype
  and   upper(hq.paymethod)  like 
                         case when hqn.quota_share_type=1 then  ''PAY''
                             when  hqn.quota_share_type=2 then  ''PRE_PAY''
                             when  hqn.quota_share_type=3 then  ''%''
                        end   
 and  hq.abledate 
 between to_date('''||startdate||''',''YYYY-MM-DD'')
 and    to_date('''||enddate||''',''YYYY-MM-DD'')                   
';
curquotasql:='update '||tablename||' hq
set  hq.quotanumber=nvl(hq.quotanumber,0)+
nvl
(
 (
 select (nvl(hqn.buy_quota_able_num,0)+nvl(hqn.common_quota_able_num,0)+nvl(hqn.casual_quota_able_num,0))-
                        (nvl(hqn.buy_quota_outofdate_num,0)+nvl(hqn.common_quota_outofdate_num,0)
                        +nvl(hqn.casual_quota_outofdate_num,0))' || tempsql||' ),0
)where exists 
(

 select 1 '||tempsql||'
)';   
      --执行sql
    execute immediate curquotasql;
    commit;
exception
  when others then
    l_errmsg := substr(sqlerrm, 1, 300);
    insert into err_log
    values
      (sysdate, 'htl_ii.importDataToQuery:' || l_errmsg);

end sp_importDataToQuery;

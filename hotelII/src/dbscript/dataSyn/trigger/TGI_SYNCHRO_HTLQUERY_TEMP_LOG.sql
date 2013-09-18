create or replace trigger TGI_SYNCHRO_HTLQUERY_TEMP_LOG
  after insert or update or delete ON HTL_PRICE
  for each row
-- Synchronize data to table HTLQUERY_TEMP_LOG
declare
  l_type      integer default 0;
  l_strerrmsg varchar2(500);
begin

  -- 如果记录在60天以内
  if (:new.able_sale_date >= trunc(sysdate) and
     :new.able_sale_date <= trunc(sysdate + 60)) then
  
    -- log类型
    if inserting then
      l_type := 1;
    elsif updating then
      l_type := 2;
    elsif deleting then
      l_type := 3;
    end if;
  
    if l_type = 3 then
      insert into HTLQUERY_TEMP_LOG
        (tempId, LOGTYPE, PRICEID, insertTime)
      values
        (SEQ_HTLQUERY_TEMP_LOG.Nextval,
         l_type,
         :new.price_id,
         systimestamp);
    else
      insert into HTLQUERY_TEMP_LOG
        (tempId,
         LOGTYPE,
         ABLEDATE,
         CLOSEFLAG,
         PAYMETHOD,
         COMMODITYID,
         ROOMTYPEID,
         HOTELID,
         PRICEID,
         SALEPRICE,
         SALESROOMPRICE,
         BREAKFASTTYPE,
         BREAKFASTNUMBER,
         BREAKFASTPRICE,
         CURRENCY,
         PAYTOPREPAY,
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
         CLOSEREASON,
         CONTINUEDAY,
         insertTime,
         formula,
         commission,
         commissionrate)
      values
        (SEQ_HTLQUERY_TEMP_LOG.Nextval,
         l_type,
         :new.able_sale_date,
         :new.close_flag,
         :new.pay_method,
         :new.child_room_type_id,
         :new.room_type_id,
         :new.hotel_id,
         :new.price_id,
         :new.sale_price,
         :new.salesroom_price,
         :new.inc_breakfast_type,
         :new.inc_breakfast_number,
         :new.inc_breakfast_price,
         :new.currency,
         :new.pay_to_prepay,
         :new.first_bookable_date,
         :new.latest_bookable_date,
         :new.first_bookable_time,
         :new.latest_bokable_time,
         :new.must_last_date,
         :new.must_first_date,
         :new.must_in_date,
         :new.max_restrict_nights,
         :new.continue_dates_relation,
         :new.need_assure,
         :new.all_close_reason,
         :new.continue_day,
         systimestamp,
         :new.FORMULAID,
         :new.COMMISSION,
         :new.COMMISSION_RATE);
    end if;
  
  end if;

exception
  when others then
    l_strerrmsg := substr(sqlerrm, 1, 150);
    insert into err_log
    values
      (sysdate, 'htl_ii.TGI_SYNCHRO_HTLQUERY_TEMP_LOG:' || l_strerrmsg);
  
end TGI_SYNCHRO_HTLQUERY_TEMP_LOG;

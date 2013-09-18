create or replace trigger TGI_QTY_SYN_HTQ_TEMP_LOG
  after INSERT OR DELETE OR UPDATE ON htl_quota_new
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
      l_type := 7;
    elsif updating then
      l_type := 8;
    elsif deleting then
      l_type := 9;
    end if;
  
    if l_type = 9 then
      -- delete
      insert into HTLQUERY_TEMP_LOG
        (tempId,
         LOGTYPE,
         HOTELID,
         ROOMTYPEID,
         bedType,
         ABLEDATE,
         quotaShareType,
         insertTime)
      values
        (SEQ_HTLQUERY_TEMP_LOG.Nextval,
         l_type,
         :new.Hotel_Id,
         :new.roomtype_id,
         :new.bed_id,
         :new.Able_Sale_Date,
         :new.quota_share_type,
         systimestamp);
    else
      -- insert or update
      insert into HTLQUERY_TEMP_LOG
        (tempId,
         LOGTYPE,
         HOTELID,
         ROOMTYPEID,
         bedType,
         ABLEDATE,
         quotaNumber,
         quotaShareType,
         insertTime)
      values
        (SEQ_HTLQUERY_TEMP_LOG.Nextval,
         l_type,
         :new.Hotel_Id,
         :new.roomtype_id,
         :new.bed_id,
         :new.Able_Sale_Date,
         (nvl(:new.BUY_QUOTA_ABLE_NUM, 0) +
         nvl(:new.COMMON_QUOTA_ABLE_NUM, 0) +
         nvl(:new.CASUAL_QUOTA_ABLE_NUM, 0)) -
         (nvl(:new.BUY_QUOTA_OUTOFDATE_NUM, 0) +
         nvl(:new.COMMON_QUOTA_OUTOFDATE_NUM, 0) +
         nvl(:new.CASUAL_QUOTA_OUTOFDATE_NUM, 0)),
         :new.quota_share_type,
         systimestamp);
    end if;
  
  end if;

exception
  when others then
    l_strerrmsg := substr(sqlerrm, 1, 150);
    insert into err_log
    values
      (sysdate, 'htl_ii.TGI_QTY_SYN_HTQ_TEMP_LOG:' || l_strerrmsg);
  
end TGI_QTY_SYN_HTQ_TEMP_LOG;

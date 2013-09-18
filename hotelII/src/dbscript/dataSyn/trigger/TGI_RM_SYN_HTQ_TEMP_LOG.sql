create or replace trigger TGI_RM_SYN_HTQ_TEMP_LOG
  after DELETE OR UPDATE OF Room_State ON HTL_ROOM
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
    if updating then
      l_type := 5;
    elsif deleting then
      l_type := 6;
    end if;

    if l_type = 6 then
      -- delete
      insert into HTLQUERY_TEMP_LOG
        (tempId, LOGTYPE, HOTELID, ROOMTYPEID, ABLEDATE, insertTime)
      values
        (SEQ_HTLQUERY_TEMP_LOG.Nextval,
         l_type,
         :new.Hotel_Id,
         :new.Room_Type_Id,
         :new.Able_Sale_Date,
         systimestamp);
    else
      -- update room_state
      insert into HTLQUERY_TEMP_LOG
        (tempId,
         LOGTYPE,
         HOTELID,
         ROOMTYPEID,
         ABLEDATE,
         roomState,
         insertTime)
      values
        (SEQ_HTLQUERY_TEMP_LOG.Nextval,
         l_type,
         :new.Hotel_Id,
         :new.Room_Type_Id,
         :new.Able_Sale_Date,
         :new.Room_State,
         systimestamp);
    end if;

  end if;

exception
  when others then
    l_strerrmsg := substr(sqlerrm, 1, 150);
    insert into err_log
    values
      (sysdate, 'htl_ii.TGI_RM_SYN_HTQ_TEMP_LOG:' || l_strerrmsg);

end TGI_RM_SYN_HTQ_TEMP_LOG;

create or replace trigger TGI_HTLINT_SYN_HTQ_TEMP_LOG1
  after UPDATE or insert or delete ON htl_internet
  for each row
-- Synchronize data to table HTLQUERY_TEMP_LOG1
declare
  l_strerrmsg varchar2(500);
begin

  if (:new.end_date >= trunc(sysdate) and
     :new.begin_date <= trunc(sysdate + 60)) then
  
    insert into HTLQUERY_TEMP_LOG1
      (tempId, LOGTYPE, HOTEL_ID, PRICE_TYPE_ID, insertTime)
    values
      (SEQ_HTLQUERY_TEMP_LOG1.Nextval,
       6,
       :new.HOTEL_ID,
       :new.room_type_id,
       systimestamp);
  
  end if;

exception
  when others then
    l_strerrmsg := substr(sqlerrm, 1, 150);
    insert into err_log
    values
      (sysdate, 'htl_ii.TGI_HTLINT_SYN_HTQ_TEMP_LOG1:' || l_strerrmsg);
  
end TGI_HTLINT_SYN_HTQ_TEMP_LOG1;

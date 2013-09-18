create or replace trigger TGI_HTLEXT_SYN_HTQ_TEMP_LOG1
  after UPDATE OF COOPERATE_CHANNEL ON HTL_HOTEL_EXT
  for each row
-- Synchronize data to table HTLQUERY_TEMP_LOG1
declare
  l_strerrmsg varchar2(500);
begin

  insert into HTLQUERY_TEMP_LOG1
    (tempId, LOGTYPE, HOTEL_ID, hdlType, insertTime)
  values
    (SEQ_HTLQUERY_TEMP_LOG1.Nextval,
     5,
     :new.Hotel_Id,
     :new.COOPERATE_CHANNEL,
     systimestamp);

exception
  when others then
    l_strerrmsg := substr(sqlerrm, 1, 150);
    insert into err_log
    values
      (sysdate, 'htl_ii.TGI_HTLEXT_SYN_HTQ_TEMP_LOG1:' || l_strerrmsg);

end TGI_HTLEXT_SYN_HTQ_TEMP_LOG1;

create or replace trigger TGI_CTL_SYN_HTQ_TEMP_LOG1
  after INSERT OR UPDATE ON HTL_CTL_DSPLY
  for each row
-- Synchronize data to table HTLQUERY_TEMP_LOG1
declare
  l_strerrmsg varchar2(500);
begin

  insert into HTLQUERY_TEMP_LOG1
    (tempId, LOGTYPE, HOTEL_ID, price_type_id, distChannel, INSERTTIME)
  values
    (SEQ_HTLQUERY_TEMP_LOG1.Nextval,
     2,
     :new.Hotel_Id,
     :new.PRICE_TYPE_ID,
     decode(:new.cc, 1, 'CC,', '') || decode(:new.web, 1, 'WEB,', '') ||
     decode(:new.tp, 1, 'TP,', '') || decode(:new.tmc, 1, 'TMC,', '') ||
     decode(:new.agent, 1, 'AGENT,', ''),
     systimestamp);

exception
  when others then
    l_strerrmsg := substr(sqlerrm, 1, 150);
    insert into err_log
    values
      (sysdate, 'htl_ii.TGI_CTL_SYN_HTQ_TEMP_LOG1:' || l_strerrmsg);

end TGI_CTL_SYN_HTQ_TEMP_LOG1;

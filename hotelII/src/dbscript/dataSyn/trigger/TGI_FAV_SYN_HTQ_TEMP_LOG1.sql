create or replace trigger TGI_FAV_SYN_HTQ_TEMP_LOG1
  after INSERT OR UPDATE OR DELETE ON Htl_Favoura_Parameter
  for each row
-- Synchronize data to table HTLQUERY_TEMP_LOG1
declare
  l_strerrmsg varchar2(500);
begin

  if :new.FAVOURABLE_TYPE = '1' or :new.FAVOURABLE_TYPE = '3' then

    if inserting or updating then

      insert into HTLQUERY_TEMP_LOG1
        (tempId,
         LOGTYPE,
         favourable_clause_id,
         FAVOURABLE_TYPE,
         CONTINUE_NIGHT,
         DONATE_NIGHT,
         PACKAGERATE_NIGHT,
         INSERTTIME)
      values
        (SEQ_HTLQUERY_TEMP_LOG1.Nextval,
         3,
         :new.favourable_clause_id,
         :new.FAVOURABLE_TYPE,
         :new.CONTINUE_NIGHT,
         :new.DONATE_NIGHT,
         :new.PACKAGERATE_NIGHT,
         systimestamp);
    else
      -- deleting
      insert into HTLQUERY_TEMP_LOG1
        (tempId,
         LOGTYPE,
         favourable_clause_id,
         FAVOURABLE_TYPE,
         CONTINUE_NIGHT,
         DONATE_NIGHT,
         PACKAGERATE_NIGHT,
         INSERTTIME)
      values
        (SEQ_HTLQUERY_TEMP_LOG1.Nextval,
         4,
         :new.favourable_clause_id,
         :new.FAVOURABLE_TYPE,
         :new.CONTINUE_NIGHT,
         :new.DONATE_NIGHT,
         :new.PACKAGERATE_NIGHT,
         systimestamp);
    end if;

  end if;

exception
  when others then
    l_strerrmsg := substr(sqlerrm, 1, 150);
    insert into err_log
    values
      (sysdate, 'htl_ii.TGI_FAV_SYN_HTQ_TEMP_LOG1:' || l_strerrmsg);

end TGI_FAV_SYN_HTQ_TEMP_LOG1;

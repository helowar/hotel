
-- ����ͬ�����ݵ�job, ע:Ҫ��sp_htlSynToHtlQuery������ִ��
begin
  sys.dbms_job.submit(job => :job,
                      what => 'sp_htlSynToHtlQuery;',
                      next_date => to_date('28-02-2011 11:01:35', 'dd-mm-yyyy hh24:mi:ss'),
                      interval => 'SYSDATE + 3/(60*24)');
  commit;
end;

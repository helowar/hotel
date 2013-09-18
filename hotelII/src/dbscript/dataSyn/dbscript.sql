create table HTLQUERY_TEMP_LOG
(
  tempId                   number(12),          
  LOGTYPE                  number(2),     
  ABLEDATE                 DATE,
  CLOSEFLAG                VARCHAR2(2),
  PAYMETHOD                VARCHAR2(16),
  COMMODITYID              NUMBER(12),
  BEDTYPE                  CHAR(1),
  ROOMTYPEID               NUMBER(12),
  bedTypeId								 NUMBER(4),
  HOTELID                  NUMBER(12),
  PRICEID                  NUMBER(12),
  SALEPRICE                NUMBER(10),
  SALESROOMPRICE           NUMBER(10),
  BREAKFASTTYPE            NUMBER(1),
  BREAKFASTNUMBER          NUMBER(3),
  BREAKFASTPRICE           NUMBER(10,2),
  CURRENCY                 VARCHAR2(32),
  PAYTOPREPAY              CHAR(1),
  BOOKSTARTDATE            DATE,
  BOOKENDDATE              DATE,
  MORNINGTIME              VARCHAR2(10),
  EVENINGTIME              VARCHAR2(10),
  CONTINUUM_IN_END         DATE,
  CONTINUUM_IN_START       DATE,
  MUST_IN                  VARCHAR2(128),
  RESTRICT_IN              NUMBER(2),
  CONTINUE_DATES_RELATION  VARCHAR2(10),
  NEED_ASSURE              CHAR(1),
  QUOTANUMBER              NUMBER(5),  
  HASOVERDRAFT             VARCHAR2(64),
  CLOSEREASON              VARCHAR2(512),
  CONTINUEDAY              number(2),
  roomState                VARCHAR2(64),
  quotaShareType           number(2),
  insertTime               timestamp
);

alter table HTLQUERY_TEMP_LOG
  add constraint PK_HTLQUERY_TEMP_LOG primary key (tempId)
  using index;
create index IDX_HQ_TMP_LOG_ITime on HTLQUERY_TEMP_LOG (insertTime);

-- Add comments to the table 
comment on table HTLQUERY_TEMP_LOG
  is 'HTLQUERY用到的临时日志表';
-- Add comments to the columns 
comment on column HTLQUERY_TEMP_LOG.LOGTYPE
  is 'log类型. 1:price表insert,2:price表update,3:price表delete,4:room表insert,5:room表update,6:room表delete,7:quota表insert,8:quota表update,9:quota表delete';
comment on column HTLQUERY_TEMP_LOG.insertTime
  is 'log插入时间';
comment on column HTLQUERY_TEMP_LOG.roomState
  is '房态';  

-- Create sequence 
create sequence SEQ_HTLQUERY_TEMP_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


-- Create table
create table HTLQUERY_TEMP_LOG1
(
  TEMPID                  NUMBER(12) not null,
  LOGTYPE                 NUMBER(2), -- 1:酒店基本表的sale_channel, 2:HTL_CTL_DSPLY表, 3,4:Htl_Favoura_Parameter表, 5:htl_hotel_ext表
  saleChannel             varchar2(64),
  distChannel             varchar2(64),
  hdlType                 varchar2(4),
  favourable_clause_id    number(10),
  FAVOURABLE_TYPE         varchar2(4),
  hotel_id                number(10),
  price_type_id           number(10),
  begin_date              date,  
  end_date                date,
  CONTINUE_NIGHT         NUMBER(3),
  DONATE_NIGHT           NUMBER(3),
  PACKAGERATE_NIGHT      NUMBER(3),
  INSERTTIME              TIMESTAMP
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table HTLQUERY_TEMP_LOG1
  add constraint PK_HTLQUERY_TEMP_LOG1 primary key (TEMPID)
  using index ;
-- Create/Recreate indexes 
create index IDX_HQ_TMP_LOG1_ITIME on HTLQUERY_TEMP_LOG1 (INSERTTIME);

create sequence SEQ_HTLQUERY_TEMP_LOG1
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


-- 创建同步数据的job, 注:要等sp_htlSynToHtlQuery创建后执行
begin
  sys.dbms_job.submit(job => :job,
                      what => 'sp_htlSynToHtlQuery;',
                      next_date => to_date('28-02-2011 11:01:35', 'dd-mm-yyyy hh24:mi:ss'),
                      interval => 'SYSDATE + 3/(60*24)');
  commit;
end;


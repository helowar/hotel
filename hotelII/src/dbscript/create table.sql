-- Create sequence 
create sequence SEQ_HTLQUERY_OTHER
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_HTLQUERY_CAN
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_HTLQUERY_PEK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;
-- Create sequence 
create sequence SEQ_HTLQUERY_SHA
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

-- Create sequence 
create sequence SEQ_HTLQUERY_SZX
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


-- Create table
create table HTLQUERY_PEK
(
  QUERYID                  NUMBER(12) not null,
  DISTID                   NUMBER(12),
  ABLEDATE                 DATE,
  DISTCHANNEL              VARCHAR2(128),
  MEMBERTYPE               VARCHAR2(64),
  USERTYPE                 VARCHAR2(64),
  CLOSEFLAG                VARCHAR2(2),
  PAYMETHOD                VARCHAR2(16),
  COMMODITYID              NUMBER(12),
  COMMODITYNAME            VARCHAR2(256),
  COMMODITYNO              VARCHAR2(64),
  COMMODITYCOUNT           NUMBER(3),
  BEDTYPE                  CHAR(1),
  ROOMTYPEID               NUMBER(12),
  ROOMTYPENAME             VARCHAR2(256),
  HOTELID                  NUMBER(12),
  HDLTYPE                  VARCHAR2(32),
  PRICEID                  NUMBER(12),
  SALEPRICE                NUMBER(10),
  SALESROOMPRICE           NUMBER(10),
  BREAKFASTTYPE            NUMBER(1),
  BREAKFASTNUMBER          NUMBER(3),
  BREAKFASTPRICE           NUMBER(10,2),
  CURRENCY                 VARCHAR2(32),
  DEALER_FAVOURABLEID      NUMBER(12),
  DEALER_PROMOTIONSALEID   NUMBER(12),
  PROVIDER_FAVOURABLEID    NUMBER(12),
  PROVIDER_PROMOTIONSALEID NUMBER(12),
  BOOKCLAUSEID             NUMBER(12),
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
  HASBOOK                  VARCHAR2(64),
  HASOVERDRAFT             VARCHAR2(64),
  CLOSEREASON              VARCHAR2(512),
  CONTINUEDAY              NUMBER(2),
  COMMISSIONRATE           NUMBER(11,3),
  COMMISSION               NUMBER(10,2),
  FORMULA                  VARCHAR2(20)
)
tablespace DEV_TP
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column HTLQUERY_PEK.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_PEK.COMMISSION
  is '佣金';
comment on column HTLQUERY_PEK.FORMULA
  is '佣金计算公式';
-- Create/Recreate primary, unique and foreign key constraints 
alter table HTLQUERY_PEK
  add constraint HTLQUERY primary key (QUERYID)
  using index 
  tablespace DEV_TP
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate indexes 
create index HTLQUERY_BREAKFAST on HTLQUERY_PEK (BREAKFASTNUMBER)
  tablespace DEV_TP
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_H_A_C on HTLQUERY_PEK (ABLEDATE, HOTELID, DISTCHANNEL)
  tablespace DEV_TP
  pctfree 10
  initrans 8
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PEK_ABLEDATE on HTLQUERY_PEK (ABLEDATE)
  tablespace DEV_TP
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PEK_CHANNEL on HTLQUERY_PEK (DISTCHANNEL)
  tablespace DEV_TP
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PEK_HOTELID on HTLQUERY_PEK (HOTELID)
  tablespace DEV_TP
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PEK_PRICE on HTLQUERY_PEK (SALEPRICE)
  tablespace DEV_TP
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
  
  
  -- Create table
create table HTLQUERY_SHA
(
  QUERYID                  NUMBER(12) not null,
  DISTID                   NUMBER(12),
  ABLEDATE                 DATE,
  DISTCHANNEL              VARCHAR2(128),
  MEMBERTYPE               VARCHAR2(64),
  USERTYPE                 VARCHAR2(64),
  CLOSEFLAG                VARCHAR2(2),
  PAYMETHOD                VARCHAR2(16),
  COMMODITYID              NUMBER(12),
  COMMODITYNAME            VARCHAR2(256),
  COMMODITYNO              VARCHAR2(64),
  COMMODITYCOUNT           NUMBER(3),
  BEDTYPE                  CHAR(1),
  ROOMTYPEID               NUMBER(12),
  ROOMTYPENAME             VARCHAR2(256),
  HOTELID                  NUMBER(12),
  HDLTYPE                  VARCHAR2(32),
  PRICEID                  NUMBER(12),
  SALEPRICE                NUMBER(10),
  SALESROOMPRICE           NUMBER(10),
  BREAKFASTTYPE            NUMBER(1),
  BREAKFASTNUMBER          NUMBER(3),
  BREAKFASTPRICE           NUMBER(10,2),
  CURRENCY                 VARCHAR2(32),
  DEALER_FAVOURABLEID      NUMBER(12),
  DEALER_PROMOTIONSALEID   NUMBER(12),
  PROVIDER_FAVOURABLEID    NUMBER(12),
  PROVIDER_PROMOTIONSALEID NUMBER(12),
  BOOKCLAUSEID             NUMBER(12),
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
  HASBOOK                  VARCHAR2(64),
  HASOVERDRAFT             VARCHAR2(64),
  CLOSEREASON              VARCHAR2(512),
  CONTINUEDAY              NUMBER(2),
  COMMISSIONRATE           NUMBER(11,3),
  COMMISSION               NUMBER(10,2),
  FORMULA                  VARCHAR2(20)
)
tablespace DEV_TP
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column HTLQUERY_SHA.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_SHA.COMMISSION
  is '佣金';
comment on column HTLQUERY_SHA.FORMULA
  is '佣金计算公式';
-- Create table
create table HTLQUERY_CAN
(
  QUERYID                  NUMBER(12) not null,
  DISTID                   NUMBER(12),
  ABLEDATE                 DATE,
  DISTCHANNEL              VARCHAR2(128),
  MEMBERTYPE               VARCHAR2(64),
  USERTYPE                 VARCHAR2(64),
  CLOSEFLAG                VARCHAR2(2),
  PAYMETHOD                VARCHAR2(16),
  COMMODITYID              NUMBER(12),
  COMMODITYNAME            VARCHAR2(256),
  COMMODITYNO              VARCHAR2(64),
  COMMODITYCOUNT           NUMBER(3),
  BEDTYPE                  CHAR(1),
  ROOMTYPEID               NUMBER(12),
  ROOMTYPENAME             VARCHAR2(256),
  HOTELID                  NUMBER(12),
  HDLTYPE                  VARCHAR2(32),
  PRICEID                  NUMBER(12),
  SALEPRICE                NUMBER(10),
  SALESROOMPRICE           NUMBER(10),
  BREAKFASTTYPE            NUMBER(1),
  BREAKFASTNUMBER          NUMBER(3),
  BREAKFASTPRICE           NUMBER(10,2),
  CURRENCY                 VARCHAR2(32),
  DEALER_FAVOURABLEID      NUMBER(12),
  DEALER_PROMOTIONSALEID   NUMBER(12),
  PROVIDER_FAVOURABLEID    NUMBER(12),
  PROVIDER_PROMOTIONSALEID NUMBER(12),
  BOOKCLAUSEID             NUMBER(12),
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
  HASBOOK                  VARCHAR2(64),
  HASOVERDRAFT             VARCHAR2(64),
  CLOSEREASON              VARCHAR2(512),
  CONTINUEDAY              NUMBER(2),
  COMMISSIONRATE           NUMBER(11,3),
  COMMISSION               NUMBER(10,2),
  FORMULA                  VARCHAR2(20)
)
tablespace DEV_TP
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column HTLQUERY_CAN.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_CAN.COMMISSION
  is '佣金';
comment on column HTLQUERY_CAN.FORMULA
  is '佣金计算公式';
-- Create table
create table HTLQUERY_SZX
(
  QUERYID                  NUMBER(12) not null,
  DISTID                   NUMBER(12),
  ABLEDATE                 DATE,
  DISTCHANNEL              VARCHAR2(128),
  MEMBERTYPE               VARCHAR2(64),
  USERTYPE                 VARCHAR2(64),
  CLOSEFLAG                VARCHAR2(2),
  PAYMETHOD                VARCHAR2(16),
  COMMODITYID              NUMBER(12),
  COMMODITYNAME            VARCHAR2(256),
  COMMODITYNO              VARCHAR2(64),
  COMMODITYCOUNT           NUMBER(3),
  BEDTYPE                  CHAR(1),
  ROOMTYPEID               NUMBER(12),
  ROOMTYPENAME             VARCHAR2(256),
  HOTELID                  NUMBER(12),
  HDLTYPE                  VARCHAR2(32),
  PRICEID                  NUMBER(12),
  SALEPRICE                NUMBER(10),
  SALESROOMPRICE           NUMBER(10),
  BREAKFASTTYPE            NUMBER(1),
  BREAKFASTNUMBER          NUMBER(3),
  BREAKFASTPRICE           NUMBER(10,2),
  CURRENCY                 VARCHAR2(32),
  DEALER_FAVOURABLEID      NUMBER(12),
  DEALER_PROMOTIONSALEID   NUMBER(12),
  PROVIDER_FAVOURABLEID    NUMBER(12),
  PROVIDER_PROMOTIONSALEID NUMBER(12),
  BOOKCLAUSEID             NUMBER(12),
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
  HASBOOK                  VARCHAR2(64),
  HASOVERDRAFT             VARCHAR2(64),
  CLOSEREASON              VARCHAR2(512),
  CONTINUEDAY              NUMBER(2),
  COMMISSIONRATE           NUMBER(11,3),
  COMMISSION               NUMBER(10,2),
  FORMULA                  VARCHAR2(20)
)
tablespace DEV_TP
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column HTLQUERY_SZX.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_SZX.COMMISSION
  is '佣金';
comment on column HTLQUERY_SZX.FORMULA
  is '佣金计算公式';
-- Create table
create table HTLQUERY_OTHER
(
  QUERYID                  NUMBER(12) not null,
  DISTID                   NUMBER(12),
  ABLEDATE                 DATE,
  DISTCHANNEL              VARCHAR2(128),
  MEMBERTYPE               VARCHAR2(64),
  USERTYPE                 VARCHAR2(64),
  CLOSEFLAG                VARCHAR2(2),
  PAYMETHOD                VARCHAR2(16),
  COMMODITYID              NUMBER(12),
  COMMODITYNAME            VARCHAR2(256),
  COMMODITYNO              VARCHAR2(64),
  COMMODITYCOUNT           NUMBER(3),
  BEDTYPE                  CHAR(1),
  ROOMTYPEID               NUMBER(12),
  ROOMTYPENAME             VARCHAR2(256),
  HOTELID                  NUMBER(12),
  HDLTYPE                  VARCHAR2(32),
  PRICEID                  NUMBER(12),
  SALEPRICE                NUMBER(10),
  SALESROOMPRICE           NUMBER(10),
  BREAKFASTTYPE            NUMBER(1),
  BREAKFASTNUMBER          NUMBER(3),
  BREAKFASTPRICE           NUMBER(10,2),
  CURRENCY                 VARCHAR2(32),
  DEALER_FAVOURABLEID      NUMBER(12),
  DEALER_PROMOTIONSALEID   NUMBER(12),
  PROVIDER_FAVOURABLEID    NUMBER(12),
  PROVIDER_PROMOTIONSALEID NUMBER(12),
  BOOKCLAUSEID             NUMBER(12),
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
  HASBOOK                  VARCHAR2(64),
  HASOVERDRAFT             VARCHAR2(64),
  CLOSEREASON              VARCHAR2(512),
  CONTINUEDAY              NUMBER(2),
  COMMISSIONRATE           NUMBER(11,3),
  COMMISSION               NUMBER(10,2),
  FORMULA                  VARCHAR2(20)
)
tablespace DEV_TP
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table HTLQUERY_OTHER
  is '酒店查询表';
-- Add comments to the columns 
comment on column HTLQUERY_OTHER.DISTID
  is '上架销售ID';
comment on column HTLQUERY_OTHER.ABLEDATE
  is '日期';
comment on column HTLQUERY_OTHER.DISTCHANNEL
  is '销售渠道';
comment on column HTLQUERY_OTHER.MEMBERTYPE
  is '会员类型';
comment on column HTLQUERY_OTHER.USERTYPE
  is '用户类型';
comment on column HTLQUERY_OTHER.CLOSEFLAG
  is '开关状态';
comment on column HTLQUERY_OTHER.PAYMETHOD
  is '支付方式';
comment on column HTLQUERY_OTHER.COMMODITYID
  is '商品ID';
comment on column HTLQUERY_OTHER.COMMODITYNAME
  is '商品名称';
comment on column HTLQUERY_OTHER.COMMODITYNO
  is '商品编码';
comment on column HTLQUERY_OTHER.COMMODITYCOUNT
  is '商品数量';
comment on column HTLQUERY_OTHER.BEDTYPE
  is '床型';
comment on column HTLQUERY_OTHER.ROOMTYPEID
  is '房型ID';
comment on column HTLQUERY_OTHER.ROOMTYPENAME
  is '房型名称';
comment on column HTLQUERY_OTHER.HOTELID
  is '酒店ID';
comment on column HTLQUERY_OTHER.HDLTYPE
  is '直联方式';
comment on column HTLQUERY_OTHER.PRICEID
  is '价格ID';
comment on column HTLQUERY_OTHER.SALEPRICE
  is '售价';
comment on column HTLQUERY_OTHER.SALESROOMPRICE
  is '门市价';
comment on column HTLQUERY_OTHER.BREAKFASTTYPE
  is '含早类型';
comment on column HTLQUERY_OTHER.BREAKFASTNUMBER
  is '含早数量';
comment on column HTLQUERY_OTHER.BREAKFASTPRICE
  is '含早价';
comment on column HTLQUERY_OTHER.CURRENCY
  is '币种';
comment on column HTLQUERY_OTHER.DEALER_FAVOURABLEID
  is '商家优惠ID';
comment on column HTLQUERY_OTHER.DEALER_PROMOTIONSALEID
  is '商家促销ID';
comment on column HTLQUERY_OTHER.PROVIDER_FAVOURABLEID
  is '供应商优惠ID';
comment on column HTLQUERY_OTHER.PROVIDER_PROMOTIONSALEID
  is '供应商促销ID';
comment on column HTLQUERY_OTHER.BOOKCLAUSEID
  is '预订条款ID';
comment on column HTLQUERY_OTHER.PAYTOPREPAY
  is '面转预,不许,允许,必须';
comment on column HTLQUERY_OTHER.BOOKSTARTDATE
  is '最早预订日期';
comment on column HTLQUERY_OTHER.BOOKENDDATE
  is '最晚预订日期';
comment on column HTLQUERY_OTHER.MORNINGTIME
  is '最早预定时间,如:0300';
comment on column HTLQUERY_OTHER.EVENINGTIME
  is '最晚预定时间,如:2200';
comment on column HTLQUERY_OTHER.CONTINUUM_IN_END
  is '入住最晚日期(冗余)';
comment on column HTLQUERY_OTHER.CONTINUUM_IN_START
  is '入住最早日期(冗余)';
comment on column HTLQUERY_OTHER.MUST_IN
  is '必住日期';
comment on column HTLQUERY_OTHER.RESTRICT_IN
  is '限住';
comment on column HTLQUERY_OTHER.CONTINUE_DATES_RELATION
  is '必住关系,"与"/"或"';
comment on column HTLQUERY_OTHER.NEED_ASSURE
  is '是否需要担保';
comment on column HTLQUERY_OTHER.QUOTANUMBER
  is '配额数';
comment on column HTLQUERY_OTHER.HASBOOK
  is '是否可订';
comment on column HTLQUERY_OTHER.HASOVERDRAFT
  is '能否透支';
comment on column HTLQUERY_OTHER.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_OTHER.COMMISSION
  is '佣金';
comment on column HTLQUERY_OTHER.FORMULA
  is '佣金计算公式';















-- Create table
create table HTL_QUERYSPEED_LOG
(
  ID                   NUMBER(10),
  CITY                 VARCHAR2(10),
  DAYS                 NUMBER(3),
  QUERYCONDITION       VARCHAR2(2000),
  LUCENE_TIMES         NUMBER,
  PRICEBREAKFAST_TIMES NUMBER,
  SORT_TIMES           NUMBER,
  COMMODITYQUERY_TIMES NUMBER,
  HANDLER_TIMES        NUMBER,
  ALLTIMES             NUMBER,
  WEBIP                VARCHAR2(20),
  CREATETIME           DATE default sysdate
)
tablespace HTLII_TAB
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column HTL_QUERYSPEED_LOG.CITY
  is '城市代码';
comment on column HTL_QUERYSPEED_LOG.DAYS
  is '查询天数';
comment on column HTL_QUERYSPEED_LOG.QUERYCONDITION
  is '查询条件';
comment on column HTL_QUERYSPEED_LOG.LUCENE_TIMES
  is 'Lucene花费时长';
comment on column HTL_QUERYSPEED_LOG.PRICEBREAKFAST_TIMES
  is '早餐和价格时长';
comment on column HTL_QUERYSPEED_LOG.SORT_TIMES
  is '排序时长';
comment on column HTL_QUERYSPEED_LOG.COMMODITYQUERY_TIMES
  is '商品查询时长';
comment on column HTL_QUERYSPEED_LOG.HANDLER_TIMES
  is '组装时长';
comment on column HTL_QUERYSPEED_LOG.ALLTIMES
  is '总查询时长';
comment on column HTL_QUERYSPEED_LOG.WEBIP
  is '访问的ip地址';
comment on column HTL_QUERYSPEED_LOG.CREATETIME
  is '创建时间';

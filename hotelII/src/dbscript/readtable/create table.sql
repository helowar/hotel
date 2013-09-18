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
  FORMULA                  VARCHAR2(20),
  FREENET                  NUMBER(1) default 0
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
comment on column HTLQUERY_PEK.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_PEK.COMMISSION
  is '佣金';
comment on column HTLQUERY_PEK.FORMULA
  is '佣金计算公式';
-- Create/Recreate primary, unique and foreign key constraints 
alter table HTLQUERY_PEK
  add constraint HTLQUERY_PEK primary key (QUERYID)
  using index 
  tablespace HTLII_TAB
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
create index HTLQUERY_PEK_ABLEDATE on HTLQUERY_PEK (ABLEDATE)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PEK_BREAKFAST on HTLQUERY_PEK (BREAKFASTNUMBER)
  tablespace HTLII_TAB
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
  tablespace HTLII_TAB
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
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PEK_H_A_C on HTLQUERY_PEK (ABLEDATE, HOTELID, DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 8
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PEK_PRICE on HTLQUERY_PEK (SALEPRICE)
  tablespace HTLII_TAB
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
  FORMULA                  VARCHAR2(20),
  FREENET                  NUMBER(1) default 0
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
comment on column HTLQUERY_SHA.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_SHA.COMMISSION
  is '佣金';
comment on column HTLQUERY_SHA.FORMULA
  is '佣金计算公式';
-- Create/Recreate primary, unique and foreign key constraints 
alter table HTLQUERY_SHA
  add constraint HTLQUERY_SHA primary key (QUERYID)
  using index 
  tablespace HTLII_TAB
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
create index HTLQUERY_SHA_ABLEDATE on HTLQUERY_SHA (ABLEDATE)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SHA_BREAKFAST on HTLQUERY_SHA (BREAKFASTNUMBER)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SHA_CHANNEL on HTLQUERY_SHA (DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SHA_HOTELID on HTLQUERY_SHA (HOTELID)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SHA_H_A_C on HTLQUERY_SHA (ABLEDATE, HOTELID, DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 8
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SHA_PRICE on HTLQUERY_SHA (SALEPRICE)
  tablespace HTLII_TAB
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
  FORMULA                  VARCHAR2(20),
  FREENET                  NUMBER(1) default 0
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
comment on column HTLQUERY_CAN.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_CAN.COMMISSION
  is '佣金';
comment on column HTLQUERY_CAN.FORMULA
  is '佣金计算公式';
-- Create/Recreate indexes 
create index HTLQUERY_ABLEDATE_CAN on HTLQUERY_CAN (ABLEDATE, ROOMTYPEID)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_BREAKFAST_CAN on HTLQUERY_CAN (BREAKFASTNUMBER)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_CAN_QUERYID on HTLQUERY_CAN (QUERYID)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_CHANNEL_CAN on HTLQUERY_CAN (DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_HOTELID_CAN on HTLQUERY_CAN (HOTELID)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_H_A_C_CAN on HTLQUERY_CAN (ABLEDATE, HOTELID, DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 8
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_PRICE_CAN on HTLQUERY_CAN (SALEPRICE)
  tablespace HTLII_TAB
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
  FORMULA                  VARCHAR2(20),
  FREENET                  NUMBER(1) default 0
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
comment on column HTLQUERY_SZX.COMMISSIONRATE
  is '佣金率';
comment on column HTLQUERY_SZX.COMMISSION
  is '佣金';
comment on column HTLQUERY_SZX.FORMULA
  is '佣金计算公式';
-- Create/Recreate primary, unique and foreign key constraints 
alter table HTLQUERY_SZX
  add constraint HTLQUERY_SZX primary key (QUERYID)
  using index 
  tablespace HTLII_TAB
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
create index HTLQUERY_SZX_ABLEDATE on HTLQUERY_SZX (ABLEDATE)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SZX_BREAKFAST on HTLQUERY_SZX (BREAKFASTNUMBER)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SZX_CHANNEL on HTLQUERY_SZX (DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SZX_HOTELID on HTLQUERY_SZX (HOTELID)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SZX_H_A_C on HTLQUERY_SZX (ABLEDATE, HOTELID, DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 8
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_SZX_PRICE on HTLQUERY_SZX (SALEPRICE)
  tablespace HTLII_TAB
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
  FORMULA                  VARCHAR2(20),
  FREENET                  NUMBER(1) default 0
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
comment on column HTLQUERY_OTHER.FREENET
  is '免费宽带';
-- Create/Recreate primary, unique and foreign key constraints 
alter table HTLQUERY_OTHER
  add constraint HTLQUERY_OTHER primary key (QUERYID)
  using index 
  tablespace HTLII_TAB
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
create index HTLQUERY_OTHER_ABLEDATE on HTLQUERY_OTHER (ABLEDATE)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_OTHER_BREAKFAST on HTLQUERY_OTHER (BREAKFASTNUMBER)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_OTHER_CHANNEL on HTLQUERY_OTHER (DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_OTHER_HOTELID on HTLQUERY_OTHER (HOTELID)
  tablespace HTLII_TAB
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_OTHER_H_A_C on HTLQUERY_OTHER (ABLEDATE, HOTELID, DISTCHANNEL)
  tablespace HTLII_TAB
  pctfree 10
  initrans 8
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
create index HTLQUERY_OTHER_PRICE on HTLQUERY_OTHER (SALEPRICE)
  tablespace HTLII_TAB
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
create table HTLQUERY_HKG                                                       
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
  FORMULA                  VARCHAR2(20),                                        
  FREENET                  NUMBER(1) default 0                                  
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
comment on column HTLQUERY_HKG.COMMISSIONRATE                                   
  is '佣金率';                                                                  
comment on column HTLQUERY_HKG.COMMISSION                                       
  is '佣金';                                                                    
comment on column HTLQUERY_HKG.FORMULA                                          
  is '佣金计算公式';                                                            
-- Create/Recreate primary, unique and foreign key constraints                  
alter table HTLQUERY_HKG                                                        
  add constraint HTLQUERY_HKG primary key (QUERYID)                             
  using index                                                                   
  tablespace HTLII_TAB                                                          
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
create index HTLQUERY_HKG_ABLEDATE on HTLQUERY_HKG (ABLEDATE)                   
  tablespace HTLII_TAB                                                          
  pctfree 10                                                                    
  initrans 2                                                                    
  maxtrans 255                                                                  
  storage                                                                       
  (                                                                             
    initial 64K                                                                 
    minextents 1                                                                
    maxextents unlimited                                                        
  );                                                                            
create index HTLQUERY_HKG_BREAKFAST on HTLQUERY_HKG (BREAKFASTNUMBER)           
  tablespace HTLII_TAB                                                          
  pctfree 10                                                                    
  initrans 2                                                                    
  maxtrans 255                                                                  
  storage                                                                       
  (                                                                             
    initial 64K                                                                 
    minextents 1                                                                
    maxextents unlimited                                                        
  );                                                                            
create index HTLQUERY_HKG_CHANNEL on HTLQUERY_HKG (DISTCHANNEL)                 
  tablespace HTLII_TAB                                                          
  pctfree 10                                                                    
  initrans 2                                                                    
  maxtrans 255                                                                  
  storage                                                                       
  (                                                                             
    initial 64K                                                                 
    minextents 1                                                                
    maxextents unlimited                                                        
  );                                                                            
create index HTLQUERY_HKG_HOTELID on HTLQUERY_HKG (HOTELID)                     
  tablespace HTLII_TAB                                                          
  pctfree 10                                                                    
  initrans 2                                                                    
  maxtrans 255                                                                  
  storage                                                                       
  (                                                                             
    initial 64K                                                                 
    minextents 1                                                                
    maxextents unlimited                                                        
  );                                                                            
create index HTLQUERY_HKG_H_A_C on HTLQUERY_HKG (ABLEDATE, HOTELID, DISTCHANNEL)
  tablespace HTLII_TAB                                                          
  pctfree 10                                                                    
  initrans 8                                                                    
  maxtrans 255                                                                  
  storage                                                                       
  (                                                                             
    initial 64K                                                                 
    minextents 1                                                                
    maxextents unlimited                                                        
  );                                                                            
create index HTLQUERY_HKG_PRICE on HTLQUERY_HKG (SALEPRICE)                     
  tablespace HTLII_TAB                                                          
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
  create table HTLQUERY_HGH                                                       
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
    FORMULA                  VARCHAR2(20),                                        
    FREENET                  NUMBER(1) default 0                                  
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
  comment on column HTLQUERY_HGH.COMMISSIONRATE                                   
    is '佣金率';                                                                  
  comment on column HTLQUERY_HGH.COMMISSION                                       
    is '佣金';                                                                    
  comment on column HTLQUERY_HGH.FORMULA                                          
    is '佣金计算公式';                                                            
  -- Create/Recreate primary, unique and foreign key constraints                  
  alter table HTLQUERY_HGH                                                        
    add constraint HTLQUERY_HGH primary key (QUERYID)                             
    using index                                                                   
    tablespace HTLII_TAB                                                          
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
  create index HTLQUERY_HGH_ABLEDATE on HTLQUERY_HGH (ABLEDATE)                   
    tablespace HTLII_TAB                                                          
    pctfree 10                                                                    
    initrans 2                                                                    
    maxtrans 255                                                                  
    storage                                                                       
    (                                                                             
      initial 64K                                                                 
      minextents 1                                                                
      maxextents unlimited                                                        
    );                                                                            
  create index HTLQUERY_HGH_BREAKFAST on HTLQUERY_HGH (BREAKFASTNUMBER)           
    tablespace HTLII_TAB                                                          
    pctfree 10                                                                    
    initrans 2                                                                    
    maxtrans 255                                                                  
    storage                                                                       
    (                                                                             
      initial 64K                                                                 
      minextents 1                                                                
      maxextents unlimited                                                        
    );                                                                            
  create index HTLQUERY_HGH_CHANNEL on HTLQUERY_HGH (DISTCHANNEL)                 
    tablespace HTLII_TAB                                                          
    pctfree 10                                                                    
    initrans 2                                                                    
    maxtrans 255                                                                  
    storage                                                                       
    (                                                                             
      initial 64K                                                                 
      minextents 1                                                                
      maxextents unlimited                                                        
    );                                                                            
  create index HTLQUERY_HGH_HOTELID on HTLQUERY_HGH (HOTELID)                     
    tablespace HTLII_TAB                                                          
    pctfree 10                                                                    
    initrans 2                                                                    
    maxtrans 255                                                                  
    storage                                                                       
    (                                                                             
      initial 64K                                                                 
      minextents 1                                                                
      maxextents unlimited                                                        
    );                                                                            
  create index HTLQUERY_HGH_H_A_C on HTLQUERY_HGH (ABLEDATE, HOTELID, DISTCHANNEL)
    tablespace HTLII_TAB                                                          
    pctfree 10                                                                    
    initrans 8                                                                    
    maxtrans 255                                                                  
    storage                                                                       
    (                                                                             
      initial 64K                                                                 
      minextents 1                                                                
      maxextents unlimited                                                        
    );                                                                            
  create index HTLQUERY_HGH_PRICE on HTLQUERY_HGH (SALEPRICE)                     
    tablespace HTLII_TAB                                                          
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
    create table HTLQUERY_CTU                                                       
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
      FORMULA                  VARCHAR2(20),                                        
      FREENET                  NUMBER(1) default 0                                  
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
    comment on column HTLQUERY_CTU.COMMISSIONRATE                                   
      is '佣金率';                                                                  
    comment on column HTLQUERY_CTU.COMMISSION                                       
      is '佣金';                                                                    
    comment on column HTLQUERY_CTU.FORMULA                                          
      is '佣金计算公式';                                                            
    -- Create/Recreate primary, unique and foreign key constraints                  
    alter table HTLQUERY_CTU                                                        
      add constraint HTLQUERY_CTU primary key (QUERYID)                             
      using index                                                                   
      tablespace HTLII_TAB                                                          
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
    create index HTLQUERY_CTU_ABLEDATE on HTLQUERY_CTU (ABLEDATE)                   
      tablespace HTLII_TAB                                                          
      pctfree 10                                                                    
      initrans 2                                                                    
      maxtrans 255                                                                  
      storage                                                                       
      (                                                                             
        initial 64K                                                                 
        minextents 1                                                                
        maxextents unlimited                                                        
      );                                                                            
    create index HTLQUERY_CTU_BREAKFAST on HTLQUERY_CTU (BREAKFASTNUMBER)           
      tablespace HTLII_TAB                                                          
      pctfree 10                                                                    
      initrans 2                                                                    
      maxtrans 255                                                                  
      storage                                                                       
      (                                                                             
        initial 64K                                                                 
        minextents 1                                                                
        maxextents unlimited                                                        
      );                                                                            
    create index HTLQUERY_CTU_CHANNEL on HTLQUERY_CTU (DISTCHANNEL)                 
      tablespace HTLII_TAB                                                          
      pctfree 10                                                                    
      initrans 2                                                                    
      maxtrans 255                                                                  
      storage                                                                       
      (                                                                             
        initial 64K                                                                 
        minextents 1                                                                
        maxextents unlimited                                                        
      );                                                                            
    create index HTLQUERY_CTU_HOTELID on HTLQUERY_CTU (HOTELID)                     
      tablespace HTLII_TAB                                                          
      pctfree 10                                                                    
      initrans 2                                                                    
      maxtrans 255                                                                  
      storage                                                                       
      (                                                                             
        initial 64K                                                                 
        minextents 1                                                                
        maxextents unlimited                                                        
      );                                                                            
    create index HTLQUERY_CTU_H_A_C on HTLQUERY_CTU (ABLEDATE, HOTELID, DISTCHANNEL)
      tablespace HTLII_TAB                                                          
      pctfree 10                                                                    
      initrans 8                                                                    
      maxtrans 255                                                                  
      storage                                                                       
      (                                                                             
        initial 64K                                                                 
        minextents 1                                                                
        maxextents unlimited                                                        
      );                                                                            
    create index HTLQUERY_CTU_PRICE on HTLQUERY_CTU (SALEPRICE)                     
      tablespace HTLII_TAB                                                          
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
      create table HTLQUERY_NKG                                                       
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
        FORMULA                  VARCHAR2(20),                                        
        FREENET                  NUMBER(1) default 0                                  
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
      comment on column HTLQUERY_NKG.COMMISSIONRATE                                   
        is '佣金率';                                                                  
      comment on column HTLQUERY_NKG.COMMISSION                                       
        is '佣金';                                                                    
      comment on column HTLQUERY_NKG.FORMULA                                          
        is '佣金计算公式';                                                            
      -- Create/Recreate primary, unique and foreign key constraints                  
      alter table HTLQUERY_NKG                                                        
        add constraint HTLQUERY_NKG primary key (QUERYID)                             
        using index                                                                   
        tablespace HTLII_TAB                                                          
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
      create index HTLQUERY_NKG_ABLEDATE on HTLQUERY_NKG (ABLEDATE)                   
        tablespace HTLII_TAB                                                          
        pctfree 10                                                                    
        initrans 2                                                                    
        maxtrans 255                                                                  
        storage                                                                       
        (                                                                             
          initial 64K                                                                 
          minextents 1                                                                
          maxextents unlimited                                                        
        );                                                                            
      create index HTLQUERY_NKG_BREAKFAST on HTLQUERY_NKG (BREAKFASTNUMBER)           
        tablespace HTLII_TAB                                                          
        pctfree 10                                                                    
        initrans 2                                                                    
        maxtrans 255                                                                  
        storage                                                                       
        (                                                                             
          initial 64K                                                                 
          minextents 1                                                                
          maxextents unlimited                                                        
        );                                                                            
      create index HTLQUERY_NKG_CHANNEL on HTLQUERY_NKG (DISTCHANNEL)                 
        tablespace HTLII_TAB                                                          
        pctfree 10                                                                    
        initrans 2                                                                    
        maxtrans 255                                                                  
        storage                                                                       
        (                                                                             
          initial 64K                                                                 
          minextents 1                                                                
          maxextents unlimited                                                        
        );                                                                            
      create index HTLQUERY_NKG_HOTELID on HTLQUERY_NKG (HOTELID)                     
        tablespace HTLII_TAB                                                          
        pctfree 10                                                                    
        initrans 2                                                                    
        maxtrans 255                                                                  
        storage                                                                       
        (                                                                             
          initial 64K                                                                 
          minextents 1                                                                
          maxextents unlimited                                                        
        );                                                                            
      create index HTLQUERY_NKG_H_A_C on HTLQUERY_NKG (ABLEDATE, HOTELID, DISTCHANNEL)
        tablespace HTLII_TAB                                                          
        pctfree 10                                                                    
        initrans 8                                                                    
        maxtrans 255                                                                  
        storage                                                                       
        (                                                                             
          initial 64K                                                                 
          minextents 1                                                                
          maxextents unlimited                                                        
        );                                                                            
      create index HTLQUERY_NKG_PRICE on HTLQUERY_NKG (SALEPRICE)                     
        tablespace HTLII_TAB                                                          
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
        create table HTLQUERY_HBQ                                                       
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
          FORMULA                  VARCHAR2(20),                                        
          FREENET                  NUMBER(1) default 0                                  
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
        comment on column HTLQUERY_HBQ.COMMISSIONRATE                                   
          is '佣金率';                                                                  
        comment on column HTLQUERY_HBQ.COMMISSION                                       
          is '佣金';                                                                    
        comment on column HTLQUERY_HBQ.FORMULA                                          
          is '佣金计算公式';                                                            
        -- Create/Recreate primary, unique and foreign key constraints                  
        alter table HTLQUERY_HBQ                                                        
          add constraint HTLQUERY_HBQ primary key (QUERYID)                             
          using index                                                                   
          tablespace HTLII_TAB                                                          
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
        create index HTLQUERY_HBQ_ABLEDATE on HTLQUERY_HBQ (ABLEDATE)                   
          tablespace HTLII_TAB                                                          
          pctfree 10                                                                    
          initrans 2                                                                    
          maxtrans 255                                                                  
          storage                                                                       
          (                                                                             
            initial 64K                                                                 
            minextents 1                                                                
            maxextents unlimited                                                        
          );                                                                            
        create index HTLQUERY_HBQ_BREAKFAST on HTLQUERY_HBQ (BREAKFASTNUMBER)           
          tablespace HTLII_TAB                                                          
          pctfree 10                                                                    
          initrans 2                                                                    
          maxtrans 255                                                                  
          storage                                                                       
          (                                                                             
            initial 64K                                                                 
            minextents 1                                                                
            maxextents unlimited                                                        
          );                                                                            
        create index HTLQUERY_HBQ_CHANNEL on HTLQUERY_HBQ (DISTCHANNEL)                 
          tablespace HTLII_TAB                                                          
          pctfree 10                                                                    
          initrans 2                                                                    
          maxtrans 255                                                                  
          storage                                                                       
          (                                                                             
            initial 64K                                                                 
            minextents 1                                                                
            maxextents unlimited                                                        
          );                                                                            
        create index HTLQUERY_HBQ_HOTELID on HTLQUERY_HBQ (HOTELID)                     
          tablespace HTLII_TAB                                                          
          pctfree 10                                                                    
          initrans 2                                                                    
          maxtrans 255                                                                  
          storage                                                                       
          (                                                                             
            initial 64K                                                                 
            minextents 1                                                                
            maxextents unlimited                                                        
          );                                                                            
        create index HTLQUERY_HBQ_H_A_C on HTLQUERY_HBQ (ABLEDATE, HOTELID, DISTCHANNEL)
          tablespace HTLII_TAB                                                          
          pctfree 10                                                                    
          initrans 8                                                                    
          maxtrans 255                                                                  
          storage                                                                       
          (                                                                             
            initial 64K                                                                 
            minextents 1                                                                
            maxextents unlimited                                                        
          );                                                                            
        create index HTLQUERY_HBQ_PRICE on HTLQUERY_HBQ (SALEPRICE)                     
          tablespace HTLII_TAB                                                          
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
          create table HTLQUERY_HDQ                                                       
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
            FORMULA                  VARCHAR2(20),                                        
            FREENET                  NUMBER(1) default 0                                  
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
          comment on column HTLQUERY_HDQ.COMMISSIONRATE                                   
            is '佣金率';                                                                  
          comment on column HTLQUERY_HDQ.COMMISSION                                       
            is '佣金';                                                                    
          comment on column HTLQUERY_HDQ.FORMULA                                          
            is '佣金计算公式';                                                            
          -- Create/Recreate primary, unique and foreign key constraints                  
          alter table HTLQUERY_HDQ                                                        
            add constraint HTLQUERY_HDQ primary key (QUERYID)                             
            using index                                                                   
            tablespace HTLII_TAB                                                          
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
          create index HTLQUERY_HDQ_ABLEDATE on HTLQUERY_HDQ (ABLEDATE)                   
            tablespace HTLII_TAB                                                          
            pctfree 10                                                                    
            initrans 2                                                                    
            maxtrans 255                                                                  
            storage                                                                       
            (                                                                             
              initial 64K                                                                 
              minextents 1                                                                
              maxextents unlimited                                                        
            );                                                                            
          create index HTLQUERY_HDQ_BREAKFAST on HTLQUERY_HDQ (BREAKFASTNUMBER)           
            tablespace HTLII_TAB                                                          
            pctfree 10                                                                    
            initrans 2                                                                    
            maxtrans 255                                                                  
            storage                                                                       
            (                                                                             
              initial 64K                                                                 
              minextents 1                                                                
              maxextents unlimited                                                        
            );                                                                            
          create index HTLQUERY_HDQ_CHANNEL on HTLQUERY_HDQ (DISTCHANNEL)                 
            tablespace HTLII_TAB                                                          
            pctfree 10                                                                    
            initrans 2                                                                    
            maxtrans 255                                                                  
            storage                                                                       
            (                                                                             
              initial 64K                                                                 
              minextents 1                                                                
              maxextents unlimited                                                        
            );                                                                            
          create index HTLQUERY_HDQ_HOTELID on HTLQUERY_HDQ (HOTELID)                     
            tablespace HTLII_TAB                                                          
            pctfree 10                                                                    
            initrans 2                                                                    
            maxtrans 255                                                                  
            storage                                                                       
            (                                                                             
              initial 64K                                                                 
              minextents 1                                                                
              maxextents unlimited                                                        
            );                                                                            
          create index HTLQUERY_HDQ_H_A_C on HTLQUERY_HDQ (ABLEDATE, HOTELID, DISTCHANNEL)
            tablespace HTLII_TAB                                                          
            pctfree 10                                                                    
            initrans 8                                                                    
            maxtrans 255                                                                  
            storage                                                                       
            (                                                                             
              initial 64K                                                                 
              minextents 1                                                                
              maxextents unlimited                                                        
            );                                                                            
          create index HTLQUERY_HDQ_PRICE on HTLQUERY_HDQ (SALEPRICE)                     
            tablespace HTLII_TAB                                                          
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
                                                                  
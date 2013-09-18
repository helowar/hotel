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
  is 'Ӷ����';
comment on column HTLQUERY_PEK.COMMISSION
  is 'Ӷ��';
comment on column HTLQUERY_PEK.FORMULA
  is 'Ӷ����㹫ʽ';
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
  is 'Ӷ����';
comment on column HTLQUERY_SHA.COMMISSION
  is 'Ӷ��';
comment on column HTLQUERY_SHA.FORMULA
  is 'Ӷ����㹫ʽ';
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
  is 'Ӷ����';
comment on column HTLQUERY_CAN.COMMISSION
  is 'Ӷ��';
comment on column HTLQUERY_CAN.FORMULA
  is 'Ӷ����㹫ʽ';
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
  is 'Ӷ����';
comment on column HTLQUERY_SZX.COMMISSION
  is 'Ӷ��';
comment on column HTLQUERY_SZX.FORMULA
  is 'Ӷ����㹫ʽ';
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
  is '�Ƶ��ѯ��';
-- Add comments to the columns 
comment on column HTLQUERY_OTHER.DISTID
  is '�ϼ�����ID';
comment on column HTLQUERY_OTHER.ABLEDATE
  is '����';
comment on column HTLQUERY_OTHER.DISTCHANNEL
  is '��������';
comment on column HTLQUERY_OTHER.MEMBERTYPE
  is '��Ա����';
comment on column HTLQUERY_OTHER.USERTYPE
  is '�û�����';
comment on column HTLQUERY_OTHER.CLOSEFLAG
  is '����״̬';
comment on column HTLQUERY_OTHER.PAYMETHOD
  is '֧����ʽ';
comment on column HTLQUERY_OTHER.COMMODITYID
  is '��ƷID';
comment on column HTLQUERY_OTHER.COMMODITYNAME
  is '��Ʒ����';
comment on column HTLQUERY_OTHER.COMMODITYNO
  is '��Ʒ����';
comment on column HTLQUERY_OTHER.COMMODITYCOUNT
  is '��Ʒ����';
comment on column HTLQUERY_OTHER.BEDTYPE
  is '����';
comment on column HTLQUERY_OTHER.ROOMTYPEID
  is '����ID';
comment on column HTLQUERY_OTHER.ROOMTYPENAME
  is '��������';
comment on column HTLQUERY_OTHER.HOTELID
  is '�Ƶ�ID';
comment on column HTLQUERY_OTHER.HDLTYPE
  is 'ֱ����ʽ';
comment on column HTLQUERY_OTHER.PRICEID
  is '�۸�ID';
comment on column HTLQUERY_OTHER.SALEPRICE
  is '�ۼ�';
comment on column HTLQUERY_OTHER.SALESROOMPRICE
  is '���м�';
comment on column HTLQUERY_OTHER.BREAKFASTTYPE
  is '��������';
comment on column HTLQUERY_OTHER.BREAKFASTNUMBER
  is '��������';
comment on column HTLQUERY_OTHER.BREAKFASTPRICE
  is '�����';
comment on column HTLQUERY_OTHER.CURRENCY
  is '����';
comment on column HTLQUERY_OTHER.DEALER_FAVOURABLEID
  is '�̼��Ż�ID';
comment on column HTLQUERY_OTHER.DEALER_PROMOTIONSALEID
  is '�̼Ҵ���ID';
comment on column HTLQUERY_OTHER.PROVIDER_FAVOURABLEID
  is '��Ӧ���Ż�ID';
comment on column HTLQUERY_OTHER.PROVIDER_PROMOTIONSALEID
  is '��Ӧ�̴���ID';
comment on column HTLQUERY_OTHER.BOOKCLAUSEID
  is 'Ԥ������ID';
comment on column HTLQUERY_OTHER.PAYTOPREPAY
  is '��תԤ,����,����,����';
comment on column HTLQUERY_OTHER.BOOKSTARTDATE
  is '����Ԥ������';
comment on column HTLQUERY_OTHER.BOOKENDDATE
  is '����Ԥ������';
comment on column HTLQUERY_OTHER.MORNINGTIME
  is '����Ԥ��ʱ��,��:0300';
comment on column HTLQUERY_OTHER.EVENINGTIME
  is '����Ԥ��ʱ��,��:2200';
comment on column HTLQUERY_OTHER.CONTINUUM_IN_END
  is '��ס��������(����)';
comment on column HTLQUERY_OTHER.CONTINUUM_IN_START
  is '��ס��������(����)';
comment on column HTLQUERY_OTHER.MUST_IN
  is '��ס����';
comment on column HTLQUERY_OTHER.RESTRICT_IN
  is '��ס';
comment on column HTLQUERY_OTHER.CONTINUE_DATES_RELATION
  is '��ס��ϵ,"��"/"��"';
comment on column HTLQUERY_OTHER.NEED_ASSURE
  is '�Ƿ���Ҫ����';
comment on column HTLQUERY_OTHER.QUOTANUMBER
  is '�����';
comment on column HTLQUERY_OTHER.HASBOOK
  is '�Ƿ�ɶ�';
comment on column HTLQUERY_OTHER.HASOVERDRAFT
  is '�ܷ�͸֧';
comment on column HTLQUERY_OTHER.COMMISSIONRATE
  is 'Ӷ����';
comment on column HTLQUERY_OTHER.COMMISSION
  is 'Ӷ��';
comment on column HTLQUERY_OTHER.FORMULA
  is 'Ӷ����㹫ʽ';















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
  is '���д���';
comment on column HTL_QUERYSPEED_LOG.DAYS
  is '��ѯ����';
comment on column HTL_QUERYSPEED_LOG.QUERYCONDITION
  is '��ѯ����';
comment on column HTL_QUERYSPEED_LOG.LUCENE_TIMES
  is 'Lucene����ʱ��';
comment on column HTL_QUERYSPEED_LOG.PRICEBREAKFAST_TIMES
  is '��ͺͼ۸�ʱ��';
comment on column HTL_QUERYSPEED_LOG.SORT_TIMES
  is '����ʱ��';
comment on column HTL_QUERYSPEED_LOG.COMMODITYQUERY_TIMES
  is '��Ʒ��ѯʱ��';
comment on column HTL_QUERYSPEED_LOG.HANDLER_TIMES
  is '��װʱ��';
comment on column HTL_QUERYSPEED_LOG.ALLTIMES
  is '�ܲ�ѯʱ��';
comment on column HTL_QUERYSPEED_LOG.WEBIP
  is '���ʵ�ip��ַ';
comment on column HTL_QUERYSPEED_LOG.CREATETIME
  is '����ʱ��';

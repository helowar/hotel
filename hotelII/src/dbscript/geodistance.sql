create table T_HTL_GEODISTANCE
(
  GEODISTANCE_ID    NUMBER(15) not null,
  HOTEL_ID     NUMBER(15),
  GEO_ID       NUMBER(15),
  GEO_TYPE     NUMBER(3),
  CITY_CODE   VARCHAR2(4),
  GEODISTANCE_NAME VARCHAR2(100),
  DISTANCE    NUMBER(5),
  CREATE_TIME TIMESTAMP(6)
)

-- Create sequence 
create sequence SEQ_SEODISTANCE
minvalue 1
maxvalue 999999999999999999999999999
start with 1

increment by 1
cache 20;

alter table T_HTL_GEODISTANCE
  add constraint PK_HTLGEODISTANCE_id primary key (GEODISTANCE_ID)
  using index ;

 alter table htl_geographicalposition add city_code varchar2(4);
 
 
 
  
 update htl_geographicalposition  
set city_code=(
select  c.name from(
select  b.name name ,g.id gid from 
cmd.t_cdm_basedata b join htl_ii.htl_geographicalposition g 
on g.city_name=b.title where b.levels=5 and b.treepath like '/root/comment/area/CN%'
)c where c.gid=htl_geographicalposition.id);
 
create sequence SEQ_T_HTL_GEODISTANCE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20; 



alter table HTL_GEOGRAPHICALPOSITION add seq_no number(10);


update htl_geographicalposition set seq_no=id


同步表htl_geographicalposition数据到生产
同步t_htl_geodistance表数据到生产上


update htl_geographicalposition  set province_name=(select province_name from (select tt.id, decode(tt.province_name,
    '安徽','A安徽',
    '北京','B北京',
    '重庆','C重庆',
    '福建','F福建',
    '广东','G广东',
    '甘肃','G甘肃',
    '广西','G广西',
    '贵州','G贵州',
    '海南','H海南',
    '湖北','H湖北',
    '河北','H河北',
    '河南','H河南',
    '-中国香港','Z-中国香港',
    '黑龙江','H黑龙江',
    '湖南','H湖南',
    '吉林','J吉林',
    '江苏','J江苏',
    '江西','J江西',
    '辽宁','L辽宁',
    '-中国澳门','Z-中国澳门',
    '内蒙古','N内蒙古',
    '宁夏','N宁夏',
    '青海','Q青海',
    '四川','S四川',
    '山东','S山东',
    '上海','S上海',
    '山西','S山西',
    '陕西','S陕西',
    '天津','T天津',
    '新疆','X新疆',
    '西藏','X西藏',
    '云南','Y云南',
    '浙江','Z浙江') as province_name
from htl_geographicalposition  tt) b where htl_geographicalposition.id= b.id)


-- Create table
create table HTL_COMMENT_SUMMARY
(
  HOTEL_ID     INTEGER,
  COMMENTNUM   INTEGER,
  AVERAGEPOINT VARCHAR2(50),
  COMMENDUP    INTEGER,
  COMMENDDOWN  INTEGER
)

-- Create/Recreate indexes 
create unique index PK_HTL_COMMSUM on HTL_COMMENT_SUMMARY (HOTEL_ID)
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
  
  
  ALTER TABLE HTL_CHANNEL_CLICK_LOG ADD COLUMN channel VARCHAR2(50);
ALTER TABLE HTL_CHANNEL_CLICK_LOG ADD COLUMN roomType VARCHAR2(20);
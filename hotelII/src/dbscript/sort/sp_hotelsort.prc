create or replace procedure sp_hotelsort(p_startdate    in varchar2,
                                         p_endDate      in varchar2,
                                         p_citycode     in varchar2,
                                         p_hotelid_list in varchar2,
                                         p_sorttype     in int, --排序方式,1推荐,2,价格,3,星级，默认是推荐
                                         p_upordown     in int, --1代表序,2代表降序,默认是推荐从高到底,价格从低到高,星级从高到底
                                         p_pagesize     in int,
                                         p_currpage     in int,
                                         p_sortedlist   out varchar2,
                                         p_hotelcount   out int,
                                         return_message out varchar2) is



  --参数
  tablename varchar2(30); --查询表名
  --curcity      varchar2(10); --查询城市名
  temp_hotelid varchar2(32767);
  str_hotelid  varchar2(32767);
  --bookdate     date; --预订日期
  booktime varchar2(10); --预订时间'14:45'
  --days         numeric(3); --入住天数
  hotelcount numeric(4); --酒店总数
  pagesize   numeric(3); --酒店页数
  currpage   numeric(3); --当前页
  --pagecount    numeric(3); --总页数
  number_temp numeric(4);
  ---游标
  type tempcursor is REF CURSOR; --定义hotelId游标
  hotelIdcursor tempcursor;
  ---sql
  incontractdate_sql varchar2(1024); --在合同日期内
  stopsell_sql       varchar2(1024); --酒店停止售卖
  item_sql           varchar2(1024); --条款
  notitem_sql        varchar2(1024); --非条款（包括开关房，价格，可订，床型，支付方式）一种商品原子
  temp_sql           varchar2(32767);

   -- 拆分p_hotelid_list
  l_tmp_str varchar2(32);
  l_index1  int;
  l_index2  int;
  -----error
  l_errmsg varchar2(512); --错误信息

begin
  dbms_profiler.start_profiler('htltest');

  insert into err_log
    (errdate, errmsg)
  values
    (sysdate, 'enter sp_hotelsort');
  commit;
  if p_hotelid_list is null then
    return_message := '酒店列表为空';
    return;
  end if;
  if p_pagesize is null then
    return_message := '每页显示记录数不能为空';
    return;
  end if;
  if p_currpage is null then
    currpage := 1;
  else
    currpage := p_currpage;
  end if;

begin
    ----清除临时数据
    delete from TMP_HOTEL_SORT;
  end;


  --初始化参数
  pagesize := p_pagesize;
  --bookdate := trunc(sysdate);
  booktime := to_char(sysdate, 'hh24:mi');
  /*days     := to_date(p_enddate, 'yyyy-mm-dd') -
  to_date(p_startdate, 'yyyy-mm-dd');*/

  --插入到临时表里
   l_index1 := 0;
  for i in 1 .. 3000 loop
    l_index1 := l_index1 + 1;
    l_index2 := instr(p_hotelid_list, ',', l_index1);
    if l_index2 < 1 then
      l_tmp_str := substr(p_hotelid_list,
                          l_index1,
                          length(p_hotelid_list) - l_index1 + 1);
      insert into TMP_HOTEL_SORT (HOTELID) values (to_number(l_tmp_str));
      exit;
    end if;
    l_tmp_str := substr(p_hotelid_list, l_index1, l_index2 - l_index1);
    insert into TMP_HOTEL_SORT (HOTELID) values (to_number(l_tmp_str));
    l_index1 := l_index2;
  end loop;

  --查询合同,看该酒店是否为停止售卖酒店或不在合同范围内
  incontractdate_sql := 'select hc.hotel_id
                      from htl_contract hc
                      where hc.hotel_id = hq.hotelid and hc.begin_date <= to_date(''' ||
                        p_startDate ||
                        ''',''yyyy-mm-dd'')
                      and hc.end_date >= to_date(''' ||
                        p_endDate || ''',''yyyy-mm-dd'')-1 ';
  stopsell_sql       := 'select hhe.hotel_id
                  from htl_hotel_ext hhe
         where ((to_date(''' || p_startdate ||
                        ''',''yyyy-mm-dd'') between hhe.stopsell_begin_date and hhe.stopsell_end_date)
                 or(to_date(''' || p_endDate ||
                        ''',''yyyy-mm-dd'')-1 between hhe.stopsell_begin_date and hhe.stopsell_end_date))
            and hhe.hotel_id = hq.hotelid';

  --查询只读表,得到条款,价格,开关房,房态信息,并计算出不可订的酒店
  if upper(p_citycode) = 'PEK' or upper(p_citycode) = 'SHA' or
     upper(p_citycode) = 'CAN' or upper(p_citycode) = 'SZX' then
    tablename := 'HTLQUERY_' || p_citycode;
  else
    tablename := 'HTLQUERY_OTHER';
  end if;

  -- 查出可显示酒店数
  temp_sql := ' select count(*) from ( select hq.hotelid
                    from ' || tablename || ' hq
                where hq.hotelid in ( select hotelid from tmp_hotel_sort )
                and abledate >= to_date(''' || p_startDate ||
              ''',''yyyy-mm-dd'')
                and abledate < to_date(''' || p_endDate ||
              ''',''yyyy-mm-dd'')
                and exists (' || incontractdate_sql || ')
                and not exists (' || stopsell_sql || ')
                         group by hq.hotelid ) d';
  execute immediate temp_sql
    into hotelcount;

  p_hotelcount := hotelcount;
  -- 酒店记录为0
  if hotelcount = 0 then
    return_message := '没有满足条件的酒店';
    return;
  end if;

  --条款
  item_sql := ' min((case when (hq.abledate = to_date(''' || p_startdate ||
              ''',''yyyy-mm-dd'')
                           and trunc(sysdate) between
                               nvl(hq.bookstartdate, trunc(sysdate) - 3000) and
                               nvl(hq.bookenddate, trunc(sysdate) + 3000)
                           and ''' || booktime ||
              ''' between
                               nvl(hq.morningtime, ''00:00'') and
                               nvl(hq.eveningtime, ''23:59'')
                           and 3 > nvl(hq.continueday, 0)
                           and 3 < decode(hq.restrict_in, 0, 1000, hq.restrict_in))
                           or hq.abledate <> to_date(''' ||
              p_startdate || ''',''yyyy-mm-dd'')
                           then 1
                           else 0
                           end)*  ';

  --非条款（开关房，价格，可订，床型，支付方式）
  notitem_sql := ' (case
                            when (nvl(hq.closeflag, ''K'') = ''K'' and
                             nvl(hq.saleprice, 0) > 0 and
                                 hq.hasbook = 1) then
                             1
                             else
                             0
                             end)) as canbook, ';

  temp_sql := ' select hq.hotelid,hq.commodityid,hq.bedtype,hq.paymethod,' ||
              item_sql || notitem_sql || ' min(decode(hq.saleprice,0,999999,hq.saleprice)) as minprice
                    from ' || tablename ||
              ' hq
                where hq.hotelid in ( select hotelid from tmp_hotel_sort )
                and abledate >= to_date(''' || p_startDate ||
              ''',''yyyy-mm-dd'')
                and abledate < to_date(''' || p_endDate ||
              ''',''yyyy-mm-dd'')
                and exists (' || incontractdate_sql || ')
                and not exists (' || stopsell_sql || ')
                         group by hq.hotelid,
                                  hq.commodityid,
                                  hq.bedtype,
                                  hq.paymethod';

  -- 查每个酒店是否可订,最低价,主推级别,星级
  temp_sql := 'select c1.hotelid,c1.canbook,c1.minprice,
                            (SELECT b.commendtype
                                      FROM htl_comm_list b
                                     WHERE b.hotel_id = c1.hotelid
                                       AND SYSDATE BETWEEN b.begin_date AND b.end_date
                                       AND ROWNUM < 2) as commendtype,
                            (select h.hotel_star from htl_hotel h where h.hotel_id = c1.hotelid) as hotelstar
                            from (select c.hotelid,
                            max(c.canbook) as canbook,
                            min(minprice) as minprice
                          from (' || temp_sql || ') c
                       group by c.hotelid) c1';

  --总页数
  --pagecount := ceil(hotelcount / p_pagesize);

  temp_sql := 'select d.hotelid,d.canbook from (' || temp_sql ||
              ') d order by d.canbook desc, ';
  -- 排序
  if p_sorttype = 3 then
    -- 星级
    if p_upordown = 2 then
      -- 降序
      temp_sql := temp_sql ||
                  ' decode(d.COMMENDTYPE,4,1,0), d.HOTELSTAR desc, d.COMMENDTYPE, d.minprice ';
    else
      -- 默认为升序
      temp_sql := temp_sql ||
                  ' decode(d.COMMENDTYPE,4,1,0), d.HOTELSTAR, d.COMMENDTYPE, d.minprice ';
    end if;
  elsif p_sorttype = 2 then
    -- 价格
    if p_upordown = 2 then
      -- 降序
      temp_sql := temp_sql ||
                  ' decode(d.COMMENDTYPE,4,1,0), d.minprice desc, d.COMMENDTYPE, d.HOTELSTAR ';
    else
      -- 默认为升序
      temp_sql := temp_sql ||
                  ' decode(d.COMMENDTYPE,4,1,0), d.minprice, d.COMMENDTYPE, d.HOTELSTAR ';
    end if;
  else
    -- 默认是推荐
    if p_upordown = 2 then
      -- 降序
      temp_sql := temp_sql ||
                  ' decode(d.COMMENDTYPE,4,6,nvl(d.COMMENDTYPE,5)) desc, d.HOTELSTAR, d.minprice ';
    else
      -- 默认为升序
      temp_sql := temp_sql ||
                  ' decode(d.COMMENDTYPE,4,6,nvl(d.COMMENDTYPE,5)), d.HOTELSTAR, d.minprice ';
    end if;
  end if;
  temp_sql := 'select indexNo,hotelid from ( select rownum indexNo,e.hotelid,e.canbook from
              (' || temp_sql ||') e) where indexNo > ' || (currpage - 1) * pagesize ||
              ' and indexNo <= ' || currpage * pagesize;

  --insert into err_log(errdate,errmsg) values(sysdate,temp_sql);
  --insert into tmp_sql values(seq_tmp_sql.nextval,temp_sql,p_citycode,sysdate);

  open hotelIdcursor for temp_sql;
  temp_hotelid := null;
  fetch hotelIdcursor
    into number_temp, str_hotelid;
  while hotelIdcursor%found loop
    temp_hotelid := temp_hotelid || str_hotelid || ',';
    fetch hotelIdcursor
      into number_temp, str_hotelid;
  end loop;
  close hotelIdcursor;
  --去掉最后一个逗号
  if temp_hotelid is not null then
    temp_hotelid := substr(temp_hotelid, 1, length(temp_hotelid) - 1);
  end if;

  p_sortedlist := temp_hotelid;

  insert into err_log
    (errdate, errmsg)
  values
    (sysdate, 'exit sp_hotelsort');
commit;
  dbms_profiler.stop_profiler;
exception
  when others then
    l_errmsg := substr(sqlerrm, 1, 300);
    insert into err_log
    values
      (sysdate, 'htl_ii.sp_hotelsort:' || l_errmsg);
 commit;
  -- dbms_profiler.stop_profiler;
end sp_hotelsort;

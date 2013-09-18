create or replace package pkg_zhx is

  -- Author  : CHENKEMING
  -- Purpose : 中航信酒店相关

  -- 更新价格
  function updatePrice(pChildRoomType in integer,
                       pPayMethod     in varchar2,
                       pStartDate     in varchar2,
                       pEndDate       in varchar2,
                       pSalePrice     in number) return number;

  -- 更新价格，供java程序调用
  procedure prcUpdatePrice(pChildRoomType in integer,
                           pPayMethod     in varchar2,
                           pStartDate     in varchar2,
                           pEndDate       in varchar2,
                           pSalePrice     in number,
                           pRes           out NUMERIC);

end pkg_zhx;

/

create or replace package body pkg_zhx is

  -- 更新价格，供java程序调用
  procedure prcUpdatePrice(pChildRoomType in integer,
                           pPayMethod     in varchar2,
                           pStartDate     in varchar2,
                           pEndDate       in varchar2,
                           pSalePrice     in number,
                           pRes           out NUMERIC) is
  
  begin
    pRes := updatePrice(pChildRoomType,
                        pPayMethod,
                        pStartDate,
                        pEndDate,
                        pSalePrice);
  end prcUpdatePrice;

  -- 更新价格
  function updatePrice(pChildRoomType in integer,
                       pPayMethod     in varchar2,
                       pStartDate     in varchar2,
                       pEndDate       in varchar2,
                       pSalePrice     in number) return number is
    l_strerrmsg varchar2(160);
  begin
  
    for curPrice in (select p.price_id
                       from htl_price p
                      where p.child_room_type_id = pChildRoomType
                        and p.pay_method = pPayMethod
                        and p.able_sale_date >=
                            to_date(pStartDate, 'yyyy-mm-dd')
                        and p.able_sale_date <=
                            to_date(pEndDate, 'yyyy-mm-dd')) loop
      update htl_price p
         set p.sale_price = pSalePrice
       where p.price_id = curPrice.price_id;
    end loop;
  
    return(1);
  exception
    when others then
      l_strerrmsg := substr(sqlerrm, 1, 150);
      insert into err_log
      values
        (sysdate, 'pkg_shp.updatePrice' || l_strerrmsg);
      return(0);
  end updatePrice;

end pkg_zhx;

CREATE OR REPLACE PACKAGE PKG_HTL_SEARCH is

  -- 根据酒店id获取交通信息字符串
  function getHotelTrafficInfo(pHotelId in number) return varchar2;

end PKG_HTL_SEARCH;

CREATE OR REPLACE PACKAGE BODY PKG_HTL_SEARCH is

  function getHotelTrafficInfo(pHotelId in number) return varchar2 is
    trafficInfo varchar2(300) default null;
  begin
  
    for traf in (select hti.arrive_address, hti.distance
                   from htl_traffic_info hti
                  where hti.hotel_id = pHotelId) loop
    
      if (traf.arrive_address is not null) then
      
        trafficInfo := trafficInfo || traf.arrive_address || '!' ||
                       traf.distance || '#';
      
      end if;
    
    end loop;
  
    return(trafficInfo);
  end getHotelTrafficInfo;

end PKG_HTL_SEARCH;
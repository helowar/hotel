create or replace function FUN_GET_DISTANCE_TO_POSITION(geoPositionId   number,
                                                        hotelId number)
  return number is
  distance                number;
  v_hotel_longitude       number;
  v_hotel_latitude        number;
  v_geoposition_longitude number;
  v_geoposition_latidue   number;
begin
  if geoPositionId is null or hotelId is null then
    return distance;
  end if;
  
  for cur_hotel_coordinate in (select h.longitude, h.latitude
                                 from htl_hotel h
                                where h.hotel_id = hotelId) loop
    v_hotel_longitude := cur_hotel_coordinate.longitude;
    v_hotel_latitude  := cur_hotel_coordinate.latitude;
  end loop;

  if v_hotel_longitude is null or v_hotel_latitude is null then
    return distance;
  end if;

  for cur_geoposition_coordinate in (select g.longitude, g.latitude
                                       from htl_geographicalposition g
                                      where g.id = geoPositionId) loop
    v_geoposition_longitude := cur_geoposition_coordinate.longitude;
    v_geoposition_latidue   := cur_geoposition_coordinate.latitude;
  end loop;

  if v_geoposition_longitude is null or v_geoposition_latidue is null then
    return distance;
  end if;

  --根据两点的经纬度坐标计算两点的距离
  distance := sin(v_hotel_latitude * 3.141592653589793 / 180.0) *
              sin(v_geoposition_latidue * 3.141592653589793 / 180.0) +
              cos(v_hotel_latitude * 3.141592653589793 / 180.0) *
              cos(v_geoposition_latidue * 3.141592653589793 / 180.0) *
              cos((v_hotel_longitude - v_geoposition_longitude) * 3.141592653589793 / 180.0);
  distance := acos(distance);
  distance := distance * 180.0 / 3.141592653589793;
  distance := distance * 60 * 1.1528455;
  distance := distance * 1609.344;
  distance := round(distance / 10) / 100.0;

  return distance;

end FUN_GET_DISTANCE_TO_POSITION;

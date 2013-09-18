package com.mangocity.hotel.base.service.assistant;

import com.mangocity.mgis.domain.valueobject.LatLng;

/**
 * 计算地图上两点的距离
 * 
 * @author yangshaojun
 */
public class CalculateDistance {
    /**
     * Automatically generated variable: EARTH_DISTANCE
     */
    private static final double EARTH_DISTANCE = 1000.0;

    /**
     * Automatically generated variable: LIMIT
     */
    private static final double LIMIT = 180.0;

    /**
     * 地球的半径
     */
    private final static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / LIMIT;
    }

    /**
     * 地球(在这里把地球当是一个圆来计算）赤道上环绕地球一周走一圈共40075.04公里,而@一圈分成360°,而每1°(度)有60,每一度一秒在赤道上的长度计算如下：
     * 40075.04km/360°=111.31955km 111.31955km/60=1.8553258km=1855.3m
     * 　而每一分又有60秒,每一秒就代表1855.3m/60=30.92m 　任意两点距离计算公式为
     * 　d＝111.12cos{1/[sinΦAsinΦB十cosΦAcosΦBcos(λB—λA)]} 　其中A点经度，纬度分别为λA和ΦA，B点的经度、纬度分别为λB和ΦB，d为距离。
     * 
     * @param latLng1
     *            地球上某点的经纬度
     * @param latLng2
     *            地球上某点的经纬度
     * @return distance:两点的距离 单位为：公里
     */
    public static double getDistance(LatLng latLng1, LatLng latLng2) {
        double distance = 0;
        if ((null != latLng1) && (null != latLng2)) {
            double radLat1 = rad(latLng1.getLatitude());
            double radLat2 = rad(latLng2.getLatitude());
            double a = radLat1 - radLat2;
            double b = rad(latLng1.getLongitude()) - rad(latLng2.getLongitude());
            distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(latLng1.getLatitude()) * Math.cos(latLng2.getLatitude())
                * Math.pow(Math.sin(b / 2), 2)));
            distance *= EARTH_RADIUS;
            distance = Math.round(distance * EARTH_DISTANCE) / EARTH_DISTANCE;
            return distance;
        }
        return distance;
    }

    public static boolean contains(LatLng latLng1, LatLng latLng2, double distance) {
        double range = getDistance(latLng1, latLng2);
        if (range < distance) {
            return false;
        }
        return true;
    }
    
    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two ZIP Codes or Postal Codes using our           :*/
	/*::  ZIPCodeWorld(TM) and PostalCodeWorld(TM) products.                     :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::    unit = the unit you desire for results                               :*/
	/*::           where: 'M' is statute miles                                   :*/
	/*::                  'K' is kilometers (default)                            :*/
	/*::                  'N' is nautical miles                                  :*/
	/*::  United States ZIP Code/ Canadian Postal Code databases with latitude & :*/
	/*::  longitude are available at http://www.zipcodeworld.com                 :*/
	/*::                                                                         :*/
	/*::  For enquiries, please contact sales@zipcodeworld.com                   :*/
	/*::                                                                         :*/
	/*::  Official Web site: http://www.zipcodeworld.com                         :*/
	/*::                                                                         :*/
	/*::  Hexa Software Development Center © All Rights Reserved 2004            :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

	public static double getDistance(double lat1, double lon1, double lat2, double lon2, char unit) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1528455;
		  if (unit == 'K') {
			  dist = dist * 1609.344;
		  } else if (unit == 'N') {
		  	dist = dist * 0.8684;
		    }
		  return (dist);
	}
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

	private static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

	private static double rad2deg(double rad) {
	  return (rad * 180.0 / Math.PI);
	}
}

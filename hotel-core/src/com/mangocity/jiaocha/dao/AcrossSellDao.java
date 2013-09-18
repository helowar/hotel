package com.mangocity.jiaocha.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcrossSellDao
{
	
	// singleton add by chenkeming
	private static final AcrossSellDao asDao = new AcrossSellDao();
	
	private AcrossSellDao() {}
	
	public static AcrossSellDao getInstance() {
		return asDao;
	}

    public List<HotelPKG> getSellPKG(String city)
    {
        List<HotelPKG> SellPKGList = new ArrayList<HotelPKG>();
        Connection c = ConnFactoryEBTI.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            st = c.createStatement();
            String sql = "select * from (select t.id,t.title,t.price from t_eb_vacation t where t.arr like '%" + city
                    + "' order by to_number(substr(t.price,2)) )where rownum < 7";

            System.out.println("sql==" + sql);

            rs = st.executeQuery(sql);

            while (rs.next())
            {
                HotelPKG hotelPKG = new HotelPKG();
                hotelPKG.setId(rs.getInt("id"));
                hotelPKG.setPrice(rs.getString("price"));
                hotelPKG.setTitle(rs.getString("title"));
                hotelPKG.setCity(city);
                SellPKGList.add(hotelPKG);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (st != null)
                    st.close();
                ConnFactoryEBTI.closeConnection(c);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return SellPKGList;
    }

    public List<HotelCommon> getHotelCommon(List<String> list)
    {
        List<HotelCommon> hotelCommonList = new ArrayList<HotelCommon>();
        Connection c = ConnFactoryEBTI.getConnectionSqlServer();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            st = c.createStatement();
            String idStrAll = "";
            for (String idStr : list)
            {
                idStrAll += idStr + ",";
            }//end for
            idStrAll = idStrAll.substring(0, idStrAll.length() - 1);

            String sql = "select  t.hotelid,h.hotelnamechn,t.commentid,t.usrid,t.context,t.createtime,"
                    + "t.usrmobilephone,userInfo.usrname,userInfo.usrnick from t_hotel_comment t "
                    + "Left Join T_User userInfo On userInfo.usrId = t.usrId left join t_hotel_info h on t.hotelid=h.hotelid "
                    + "where t.commentid in(select max(t.commentid) from t_hotel_comment t " + "where t.hotelid in("
                    + idStrAll + ") and t.commentstatus=1 group by t.hotelid) order by t.createtime desc";

            System.out.println("sql==" + sql);

            rs = st.executeQuery(sql);

            while (rs.next())
            {
                HotelCommon hotelCommon = new HotelCommon();
                hotelCommon.setCommentid(rs.getString("commentid"));
                hotelCommon.setUsrid(rs.getString("usrid"));
                hotelCommon.setContext(rs.getString("context"));
                hotelCommon.setCreatetime(rs.getString("createtime"));
                hotelCommon.setHotelid(rs.getString("hotelid"));

                String author = "";
                if (rs.getString("usrNick") != null && !"".equals(rs.getString("usrNick")))
                {
                    author = rs.getString("usrNick");
                }
                else if (rs.getString("usrname") != null && !"".equals(rs.getString("usrname")))
                {
                    author = rs.getString("usrName");
                }
                else
                {
                    String authorTemp = rs.getString("usrmobilephone");
                    if (authorTemp != null && !authorTemp.trim().equals(""))
                        author = authorTemp.substring(0, 3) + "xxxx" + authorTemp.substring(7);
                }
                hotelCommon.setAuthor(author);
                hotelCommon.setHotelName(rs.getString("hotelnamechn"));
                hotelCommonList.add(hotelCommon);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (st != null)
                    st.close();
                ConnFactoryEBTI.closeConnection(c);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return hotelCommonList;
    }

    public HotelDestguide getHotelDestguide(String cityName)
    {
        Connection c = ConnFactoryEBTI.getConnection();
        Statement st = null;
        ResultSet rs = null;
        HotelDestguide hotelDestguide = new HotelDestguide();
        try
        {
            st = c.createStatement();
            String sql = "select t.code,t.name,t.brief from t_dg_area t where t.typelevel=2 and t.validflag=1 and t.name='"
                    + cityName + "'";

            System.out.println("sql==" + sql);

            rs = st.executeQuery(sql);

            while (rs.next())
            {
                hotelDestguide.setCityBrief(rs.getString("brief"));
                hotelDestguide.setCityCode(rs.getString("code"));
                hotelDestguide.setCityName(rs.getString("name"));
            }

            String sql2 = "select * from (select t.name,t.code from t_dg_area t where t.typelevel=3 and t.validflag=1 and t.parentcode='"
                    + hotelDestguide.getCityCode() + "' order by t.recomflag desc )where rownum < 16";

            System.out.println("sql2==" + sql2);

            rs = st.executeQuery(sql2);

            Map<String, String> signMap = new HashMap<String, String>();
            while (rs.next())
            {
                signMap.put(rs.getString("code"), rs.getString("name"));
            }
            hotelDestguide.setCitySigns(signMap);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (st != null)
                    st.close();
                ConnFactoryEBTI.closeConnection(c);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return hotelDestguide;
    }

    public Map<String, String> getCommonCountByHotelId(List<String> list)
    {

        Map<String, String> countMap = new HashMap<String, String>();
        Connection c = ConnFactoryEBTI.getConnectionSqlServer();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            st = c.createStatement();
            String idStrAll = "";
            for (String idStr : list)
            {
                idStrAll += idStr + ",";
            }//end for
            idStrAll = idStrAll.substring(0, idStrAll.length() - 1);

            String sql = "select count(*) as qty,t.hotelid from t_hotel_comment t  where t.hotelid in(" + idStrAll
                    + ") and t.commentstatus=1 group by t.hotelid order by t.hotelid";

            System.out.println("sql==" + sql);
            rs = st.executeQuery(sql);

            while (rs.next())
            {
                countMap.put(rs.getString("hotelid"), rs.getString("qty"));
                //System.out.println(rs.getString("hotelid") + "  ;  " + rs.getString("qty"));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                if (st != null)
                    st.close();
                ConnFactoryEBTI.closeConnection(c);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

        }
        return countMap;

    }

}

/*
 * @(#)ConnFactory.java 1.0 06/07/17
 */
package com.mangocity.jiaocha.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * ��ȡ��ݿ�l�Ӻ͹ر�Connection. <br>
 * 
 * <pre>
 *   Connection conn=ConnFactory.getConnection();
 *   try{
 *    ...
 *   }catch(Exception ex){
 *    ...
 *   }finally{
 *     ConnFactory.closeConnection(conn);
 *   }
 * </pre>
 * 
 * @version 1.0
 * @author xxb
 * 
 */
public final class ConnFactoryEBTI
{

    private static final boolean useJdbc = false;//�Ƿ���jdbc,������jndi  

    private static final boolean isTomcat = false;//����tomcat��������

    private static final String dsName = "jdbc/ebti";//MBOS jndi��

    private static DataSource ds;

    private static Driver driver;

    static
    {
        try
        {
            if (!useJdbc)
            {
                Context txt = new InitialContext();
                ds = (DataSource) txt.lookup("jdbc/ebti");
            }
            else
            {
                String driverClassName = "oracle.jdbc.driver.OracleDriver";
                driver = (Driver) Class.forName(driverClassName).newInstance();
                DriverManager.registerDriver(driver);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Connection getConnection()
    {

        Connection dbCon = null;

        if (useJdbc)
        {

            try
            {

                dbCon = DriverManager.getConnection("jdbc:oracle:thin:@10.10.4.62:1901:hkcts01", "ebcm",
                        "ebcm_pass_081229");

                dbCon.setAutoCommit(false);

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            try
            {
                dbCon = ds.getConnection();
                if (dbCon.getAutoCommit())
                {
                    dbCon.setAutoCommit(false);
                }

            }
            catch (Exception ne)
            {
                System.out.println("Error" + ne.toString());
            }
        }

        return dbCon;
    }

    /**
     * ȡ����ݿ�l��
     * 
     * @return Connection
     */
    public static Connection getConnection12()
    {

        Connection dbCon = null;

        if (useJdbc)
        {

            try
            {

                dbCon = DriverManager.getConnection("jdbc:oracle:thin:@10.10.5.18:1901:hccdb", "ebcm", "ebcm");
                dbCon.setAutoCommit(false);

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            try
            {
                dbCon = ds.getConnection();
                if (dbCon.getAutoCommit())
                {
                    dbCon.setAutoCommit(false);
                }

            }
            catch (Exception ne)
            {
                System.out.println("Error" + ne.toString());
            }
        }

        return dbCon;
    }

    /**
     * @return
     */
    public static Connection getConnectionSqlServer() {

        Connection dbCon = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            //dbCon=DriverManager.getConnection("jdbc:sqlserver://10.10.63.71:1433;DatabaseName=mangobbs","sa","mango");
            dbCon=DriverManager.getConnection("jdbc:sqlserver://10.10.222.71:1433;DatabaseName=mangobbs","sa","bbs_psw_!@#");
            
            dbCon.setAutoCommit(false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dbCon;
    }

    /**
     * Connection
     */
    public static void free(Connection conn, Statement stmt, ResultSet rs)
    {

        if (rs != null)
        {
            try
            {
                rs.close();
                rs = null;
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally
            {
                if (stmt != null)
                {
                    try
                    {
                        stmt.close();
                        stmt = null;
                    }
                    catch (SQLException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (conn != null)
                        {
                            try
                            {
                                conn.close();
                                conn = null;
                            }
                            catch (SQLException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }

                }
            }

        }
    }

    /**
     * �ر�Connection
     * 
     * @param conn
     *            Connection Ҫ�رյ�Connection
     */
    public static void closeConnection(Connection conn)
    {
        try
        {
            if (conn != null)
            {
                //logger.info("close connection.....");
                if (!isTomcat)
                {
                    try
                    {
                        conn.commit();
                    }
                    catch (Exception e)
                    {

                    }
                }
                conn.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        finally
        {
            conn = null;
        }
    }
}

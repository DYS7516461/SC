package com.CRMEB.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @author 张双虎
 *
 * @TODO
 */
public class BaseDAO {

	// 申明部分常量
	private static String USERNAME;// 用户名
	private static String PASSWORD;// 密码
	// 驱动类全限定类名
	private static String DRIVER_CLASS;
	// 链接的数据库的地址
	private static String URL;
	// 通过静态代码块读取配置文件
	static {
		Properties properties = new Properties();
		try {
			properties.load(BaseDAO.class.getClassLoader().getResourceAsStream("db.properties"));
			USERNAME = properties.getProperty("username");
			PASSWORD = properties.getProperty("password");
			DRIVER_CLASS = properties.getProperty("driverclass");
			URL = properties.getProperty("url");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		Connection con = null;
		try {
			Class.forName(DRIVER_CLASS);
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 查询操作
	 * 
	 * @param sql
	 *            要执行的sql
	 * @param params
	 *            sql中的参数
	 * @return
	 */
	public ArrayList<String[]> query(String sql, Object... params) {
		ArrayList<String[]> result = new ArrayList<>();
		Connection con = getCon();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			// 设置参数
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pst.setString(i + 1, params[i].toString());
				}
			}
			rs = pst.executeQuery();
			// 遍历结果集
			for (; rs.next();) {
				// 准备一个数组
				int columnCount = rs.getMetaData().getColumnCount();
				String[] values = new String[columnCount];
				// 获取数据放入数组
				for (int i = 0; i < columnCount; i++) {
					values[i] = rs.getString(i + 1);
				}
				// 将数组加入集合
				result.add(values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 增删改查操作统一的操作方法
	 * 
	 * @param sql
	 *            要执行的sql语句
	 * @param params
	 *            sql语句中的参数
	 * @return
	 */
	public int update(String sql, Object... params) {
		int result = 0;
		Connection con = getCon();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			// 设置参数
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					if(params[i]==null){
						pst.setString(i + 1, null);
					}else{
						pst.setString(i + 1, params[i].toString());
					}
					
				}
			}
			result = pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 保存数据
	 * 
	 * @param infos
	 *            要保存的数据数组，数组的数据顺序要和数据库的列的顺序一致
	 * @param tableName
	 * @param keyName 自增列，请传递列名，不是自增列请传递null
	 * @return
	 * @throws SQLException 
	 */
	public int save(Object[] infos, String tableName,String keyName) throws SQLException {
		Connection con = getCon();
		DatabaseMetaData metaData = con.getMetaData();
		//获取所有的列名
		ResultSet tables = metaData.getColumns(null, null, tableName, null);
		String sql = "insert into "+tableName+"(";
		int i = 0;
		for(;tables.next();){
			String columnName = tables.getString("COLUMN_NAME");
			if(!columnName.equalsIgnoreCase(keyName)){
				sql += columnName+",";
				i++;
			}
		}
		sql = sql.substring(0,sql.length()-1)+") values(";
		//添加？
		for(int x = 0;x < i;x++){
			sql += "?,";
		}
		sql = sql.substring(0,sql.length()-1)+")";
		Object [] params = infos;
		if(keyName!=null){
			params = new Object[infos.length-1];
			for(int j = 1;j<infos.length;j++){
				params[j-1]=infos[j];
			}
		}
		return update(sql,params);
	}
	

	/**
	 * 修改数据
	 * 
	 * @param infos
	 *            要保存的数据数组，数组的数据顺序要和数据库的列的顺序一致
	 *            默认第一位是主键
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public int update(Object[] infos, String tableName,String keyName) throws SQLException {
		Connection con = getCon();
		DatabaseMetaData metaData = con.getMetaData();
		//获取所有的列名
		ResultSet columns = metaData.getColumns(null, null, tableName, null);
		String sql = "update "+tableName+" set ";
		for(;columns.next();){
			String columnName = columns.getString("COLUMN_NAME");
			if(!columnName.equalsIgnoreCase(keyName))
				sql += columnName +"=?,";
		}
		sql = sql.substring(0,sql.length()-1)+" where "+keyName+"=?";
		Object [] params = new Object[infos.length];
		for(int x = 1;x<infos.length;x++){
			params[x-1] = infos[x];
		}
		params[params.length-1] = infos[0];
		return update(sql,params);
	}

	/**
	 * 删除数据
	 * 
	 * @param key
	 *            主键的值
	 * @param keyName
	 *            主键列名
	 * @param tableName
	 *            表名
	 * @return
	 */
	public int delete(Object key, String keyName, String tableName) {
		String sql = "delete from "+tableName +" where "+keyName+" = "+key;
		return update(sql);
	}

	/**
	 * 根据主键查询一行数据
	 * 
	 * @param key
	 * @param keyName
	 * @return
	 */
	public String[] queryByPrimayKey(Object key, String keyName,String tableName) {
		String sql = "select * from "+tableName +" where "+keyName+" = "+key;
		ArrayList<String[]> data = query(sql);
		if(data !=null && data.size()>0){
			return data.get(0);//如果查询到数据了就返回第一个。
		}
		return null;
	}

	/**
	 * 根据条件查询
	 * 
	 * @param trem
	 *            自己拼接where后面的查询条件
	 * @param tableName
	 * @return
	 */
	public List<String[]> queryByTrem(String trem, String tableName) {
		//拼接sql
		String sql = "select * from "+tableName;
		if(trem!=null && !"".equals(trem)){
			sql += " where "+trem;
		}
		return query(sql);
	}

	/**
	 * 根据sql查询
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<String[]> queryBySql(String sql, Object... params) {
		return query(sql, params);
	}
	//查询总条数得方法
	public int queryTotal(String tableName){
		ArrayList<String[]> result = query("select count(*) from "+tableName);
		if(result!=null && result.size()>0){
			String[] info = result.get(0);
			if(info!=null && info.length>0){
				return Integer.parseInt(info[0]);
			}
		}
		return 0;
	}
}

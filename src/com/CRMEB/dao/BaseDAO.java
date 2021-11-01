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
 * @author ��˫��
 *
 * @TODO
 */
public class BaseDAO {

	// �������ֳ���
	private static String USERNAME;// �û���
	private static String PASSWORD;// ����
	// ������ȫ�޶�����
	private static String DRIVER_CLASS;
	// ���ӵ����ݿ�ĵ�ַ
	private static String URL;
	// ͨ����̬������ȡ�����ļ�
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
	 * ��ѯ����
	 * 
	 * @param sql
	 *            Ҫִ�е�sql
	 * @param params
	 *            sql�еĲ���
	 * @return
	 */
	public ArrayList<String[]> query(String sql, Object... params) {
		ArrayList<String[]> result = new ArrayList<>();
		Connection con = getCon();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			// ���ò���
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pst.setString(i + 1, params[i].toString());
				}
			}
			rs = pst.executeQuery();
			// ���������
			for (; rs.next();) {
				// ׼��һ������
				int columnCount = rs.getMetaData().getColumnCount();
				String[] values = new String[columnCount];
				// ��ȡ���ݷ�������
				for (int i = 0; i < columnCount; i++) {
					values[i] = rs.getString(i + 1);
				}
				// ��������뼯��
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
	 * ��ɾ�Ĳ����ͳһ�Ĳ�������
	 * 
	 * @param sql
	 *            Ҫִ�е�sql���
	 * @param params
	 *            sql����еĲ���
	 * @return
	 */
	public int update(String sql, Object... params) {
		int result = 0;
		Connection con = getCon();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			// ���ò���
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
	 * ��������
	 * 
	 * @param infos
	 *            Ҫ������������飬���������˳��Ҫ�����ݿ���е�˳��һ��
	 * @param tableName
	 * @param keyName �����У��봫�������������������봫��null
	 * @return
	 * @throws SQLException 
	 */
	public int save(Object[] infos, String tableName,String keyName) throws SQLException {
		Connection con = getCon();
		DatabaseMetaData metaData = con.getMetaData();
		//��ȡ���е�����
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
		//��ӣ�
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
	 * �޸�����
	 * 
	 * @param infos
	 *            Ҫ������������飬���������˳��Ҫ�����ݿ���е�˳��һ��
	 *            Ĭ�ϵ�һλ������
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public int update(Object[] infos, String tableName,String keyName) throws SQLException {
		Connection con = getCon();
		DatabaseMetaData metaData = con.getMetaData();
		//��ȡ���е�����
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
	 * ɾ������
	 * 
	 * @param key
	 *            ������ֵ
	 * @param keyName
	 *            ��������
	 * @param tableName
	 *            ����
	 * @return
	 */
	public int delete(Object key, String keyName, String tableName) {
		String sql = "delete from "+tableName +" where "+keyName+" = "+key;
		return update(sql);
	}

	/**
	 * ����������ѯһ������
	 * 
	 * @param key
	 * @param keyName
	 * @return
	 */
	public String[] queryByPrimayKey(Object key, String keyName,String tableName) {
		String sql = "select * from "+tableName +" where "+keyName+" = "+key;
		ArrayList<String[]> data = query(sql);
		if(data !=null && data.size()>0){
			return data.get(0);//�����ѯ�������˾ͷ��ص�һ����
		}
		return null;
	}

	/**
	 * ����������ѯ
	 * 
	 * @param trem
	 *            �Լ�ƴ��where����Ĳ�ѯ����
	 * @param tableName
	 * @return
	 */
	public List<String[]> queryByTrem(String trem, String tableName) {
		//ƴ��sql
		String sql = "select * from "+tableName;
		if(trem!=null && !"".equals(trem)){
			sql += " where "+trem;
		}
		return query(sql);
	}

	/**
	 * ����sql��ѯ
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<String[]> queryBySql(String sql, Object... params) {
		return query(sql, params);
	}
	//��ѯ�������÷���
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

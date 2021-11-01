package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.UserDAO;
import com.CRMEB.util.EasyUIJsonUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {
	private UserDAO userDAO = new UserDAO();

	// ��ҳ��ѯ
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int page = 1;// ҳ�룬Ĭ��1
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
		}
		int rows = 20;// ÿҳ��С��Ĭ��20
		try {
			rows = Integer.parseInt(request.getParameter("rows"));
		} catch (NumberFormatException e) {
		}
		// ��ѯ������
		int total = userDAO.queryTotal("tbl_user");
		// ��ҳ��ѯ����
		String sql = "SELECT uid,tbl_user.`Name`,tbl_UserLevel.`name` AS Level,Balance,Integral,tbl_user.`status`,phone,tbl_UserType.`name` AS UserType,tbl_UserGroup.groupName AS UserGroup,tbl_UserLabel.labelName AS UserLabel "
				+ "FROM tbl_user,tbl_UserLevel,tbl_UserType,tbl_UserLabel,tbl_UserGroup "
				+ "WHERE tbl_user.`Level`=tbl_UserLevel.id AND tbl_user.UserType=tbl_UserType.id AND tbl_user.UserGroup=tbl_UserGroup.id AND tbl_user.UserLabel=tbl_UserLabel.id ORDER BY uid ASC limit "
				+ ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = userDAO.query(sql);
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "uid", "Name", "Level", "Balance", "Integral", "status", "phone", "UserType", "UserGroup",
				"UserLabel" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �û��ȼ��б�
	public void userLevelList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String sql = "SELECT id,name FROM tbl_UserLevel";
		ArrayList<String[]> list = userDAO.query(sql);

		int total = userDAO.queryTotal("tbl_UserLevel");
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "id", "labelName" };
		String json = EasyUIJsonUtil.comboBoxJson(list, 0, 1);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �û������б�
	public void userGroupList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String sql = "SELECT id,groupName FROM tbl_UserGroup";
		ArrayList<String[]> list = userDAO.query(sql);

		int total = userDAO.queryTotal("tbl_UserGroup");
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "id", "groupName" };
		String json = EasyUIJsonUtil.comboBoxJson(list, 0, 1);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �û���ǩ�б�
	public void userLabelList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String sql = "SELECT id,labelName FROM tbl_UserLabel";
		ArrayList<String[]> list = userDAO.query(sql);

		int total = userDAO.queryTotal("tbl_UserLabel");
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "id", "labelName" };
		String json = EasyUIJsonUtil.comboBoxJson(list, 0, 1);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// ����û�
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// ����ɹ���״̬��
		// ��ȡ�ύ����������
		String Name = request.getParameter("Name");
		String Balance = String.valueOf((Double.valueOf(request.getParameter("Balance"))) * 100);
		String Integral = String.valueOf((Double.valueOf(request.getParameter("Integral"))) * 100);
		String phone = request.getParameter("phone");
		String Level = request.getParameter("Level");
		String UserType = request.getParameter("UserType");
		String status = request.getParameter("status");
		String UserGroup = request.getParameter("UserGroup");
		String UserLabel = request.getParameter("status");
		// ׼������
		String[] Users = { "1", Name, Level, Balance, Integral, status, phone, UserType, UserGroup, UserLabel };
		try {
			userDAO.save(Users, "tbl_user", "uid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		// ������
		response.getWriter().write(result);
	}

	// ����ID��ѯ�û���Ϣ
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String uid = request.getParameter("uid");
		// ��ѯ�û�
		String[] user = userDAO.queryByPrimayKey(uid, "uid", "tbl_user");
		// ������ת��json
		String[] attrNames = { "uid", "Name", "Level", "Balance", "Integral", "status", "phone", "UserType",
				"UserGroup", "UserLabel" };
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �༭�û�
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// �༭�ɹ�
		// ��ȡ�ύ����������
		String uid = request.getParameter("uid");
		String Name = request.getParameter("Name");
		String Balance = String.valueOf((Double.valueOf(request.getParameter("Balance"))) * 100);
		String Integral = request.getParameter("Integral");
		String phone = request.getParameter("phone");
		String Level = request.getParameter("Level");
		String UserType = request.getParameter("UserType");
		String UserGroup = request.getParameter("UserGroup");
		String UserLabel = request.getParameter("UserLabel");
		String status = request.getParameter("status");
		// ׼������
		String[] Users = { uid, Name, Level, Balance, Integral, status, phone, UserType, UserGroup, UserLabel };
		try {
			userDAO.update(Users, "tbl_user", "uid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}

	// ɾ���û�
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// ��ȡ�ύ����������
		String uid = request.getParameter("uid");
		// �ȸ���id��ѯ����û���������Ϣ��
		String[] users = userDAO.queryByPrimayKey(uid, "uid", "tbl_user");
		// �޸�״̬λ3
		users[5] = "3";
		// �ٽ��޸ĺ�����ݱ��浽���ݿ���
		try {
			userDAO.update(users, "tbl_user", "uid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}

	// �������÷���
	public void setGroup(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String result = "1";
		String UserGroup = request.getParameter("UserGroup");
		String uids = request.getParameter("uids");
		// ��ѧ����Ž��зָ�
		String[] ids = uids.split(",");
		// ����޸ģ���ȻҲ���������޸ġ�
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			// �ȸ���id��ѯ����û���������Ϣ��
			String[] user = userDAO.queryByPrimayKey(id, "uid", "tbl_user");
			// ���ð༶���
			user[8] = UserGroup;
			// ����
			try {
				userDAO.update(user, "tbl_user", "uid");
			} catch (SQLException e) {
				e.printStackTrace();
				result = "0";
			}
		}
		response.getWriter().write(result);
	}

	// �������÷���
	public void setLabels(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String result = "1";
		String UserLabel = request.getParameter("UserLabel");
		String uids = request.getParameter("uidss");
		System.out.println(uids);
		//System.out.println(UserLabel);
		// ��ѧ����Ž��зָ�
		String[] ids = uids.split(",");
		// ����޸ģ���ȻҲ���������޸ġ�
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			System.out.println(ids[i]);
			// �ȸ���id��ѯ����û���������Ϣ��
			String[] user = userDAO.queryByPrimayKey(id, "uid", "tbl_user");
			// ���ð༶���
			user[9] = UserLabel;
			// ����
			try {
				userDAO.update(user, "tbl_user", "uid");
			} catch (SQLException e) {
				e.printStackTrace();
				result = "0";
			}
		}
		response.getWriter().write(result);
	}

}

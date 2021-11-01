package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.UserGroupDAO;
import com.CRMEB.util.EasyUIJsonUtil;

/**
 * Servlet implementation class UserGroup
 */
@WebServlet("/usergroup")
public class UserGroupServlet extends BaseServlet {
	private UserGroupDAO userGroupDAO = new UserGroupDAO();

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
		int total = userGroupDAO.queryTotal("tbl_UserGroup");
		// ��ҳ��ѯ����
		String sql = "SELECT * FROM tbl_UserGroup limit " + ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = userGroupDAO.query(sql);
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "id", "groupName","status" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// ��ӱ�ǩ
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// ����ɹ���״̬��
		// ��ȡ�ύ����������
		String groupName = request.getParameter("groupName");
		String status = request.getParameter("status");
		// ׼������
		String[] Users = { "1", groupName, status };
		try {
			userGroupDAO.save(Users, "tbl_UserGroup", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		// ������
		response.getWriter().write(result);
	}

	// ����ID��ѯ��ǩ��Ϣ
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String id = request.getParameter("id");
		// ��ѯ��ǩ
		String[] user = userGroupDAO.queryByPrimayKey(id, "id", "tbl_UserGroup");
		// ������ת��json
		String[] attrNames = { "id", "groupName", "status" };
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �༭��ǩ
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// �༭�ɹ�
		// ��ȡ�ύ����������
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		String groupName = request.getParameter("groupName");
		// ׼������
		String[] Users = { id, groupName, status };
		try {
			userGroupDAO.update(Users, "tbl_UserGroup", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}

	// ɾ����ǩ
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// ��ȡ�ύ����������
		String id = request.getParameter("id");
		// �ȸ���id��ѯ�����ǩ��������Ϣ��
		String[] users = userGroupDAO.queryByPrimayKey(id, "id", "tbl_UserGroup");
		// �޸�״̬λ3
		users[2] = "3";
		// �ٽ��޸ĺ�����ݱ��浽���ݿ���
		try {
			userGroupDAO.update(users, "tbl_UserGroup", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}

}

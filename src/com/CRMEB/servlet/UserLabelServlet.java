package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.UserLabelDAO;
import com.CRMEB.util.EasyUIJsonUtil;

/**
 * Servlet implementation class UserLabelServlet
 */
@WebServlet("/userlabel")
public class UserLabelServlet extends BaseServlet {
	private UserLabelDAO userLabelDAO = new UserLabelDAO();

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
		int total = userLabelDAO.queryTotal("tbl_UserLabel");
		// ��ҳ��ѯ����
		String sql = "SELECT * FROM tbl_UserLabel limit " + ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = userLabelDAO.query(sql);
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "id", "labelName","status" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// ��ӱ�ǩ
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// ����ɹ���״̬��
		// ��ȡ�ύ����������
		String labelName = request.getParameter("labelName");
		// ׼������
		String[] Users = { "1", labelName, "1" };
		try {
			userLabelDAO.save(Users, "tbl_UserLabel", "id");
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
		String[] user = userLabelDAO.queryByPrimayKey(id, "id", "tbl_UserLabel");
		// ������ת��json
		String[] attrNames = { "id", "labelName", "status" };
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
		String labelName = request.getParameter("labelName");
		// ׼������
		String[] Users = { id, labelName, status };
		try {
			userLabelDAO.update(Users, "tbl_UserLabel", "id");
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
		String[] users = userLabelDAO.queryByPrimayKey(id, "id", "tbl_UserLabel");
		// �޸�״̬λ3
		users[2] = "3";
		// �ٽ��޸ĺ�����ݱ��浽���ݿ���
		try {
			userLabelDAO.update(users, "tbl_UserLabel", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}

}

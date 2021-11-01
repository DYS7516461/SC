package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.UserLevelDAO;
import com.CRMEB.util.EasyUIJsonUtil;

/**
 * Servlet implementation class UserLevelServelt
 */
@WebServlet("/userlevel")
public class UserLevelServelt extends BaseServlet {
	private UserLevelDAO userlevelDAO = new UserLevelDAO();

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
		int total = userlevelDAO.queryTotal("tbl_UserLevel");
		// ��ҳ��ѯ����
		String sql = "SELECT id,name,discount,grade,icon,isShow,status FROM tbl_UserLevel limit "
				+ ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = userlevelDAO.query(sql);
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "id", "name", "discount", "grade", "icon", "isShow","status" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// ��ӵȼ�
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// ����ɹ���״̬��
		// ��ȡ�ύ����������
		String name = request.getParameter("name");
		String discount = request.getParameter("discount");
		String grade = request.getParameter("grade");
		String icon = request.getParameter("icon");
		String isShow = request.getParameter("isShow");
		String status = request.getParameter("status");
		System.out.println("isShow:"+isShow);
		// ׼������
		String[] Users = { "1", name, discount, grade, icon, isShow,status };
		try {
			userlevelDAO.save(Users, "tbl_UserLevel", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		// ������
		response.getWriter().write(result);
	}

	// ����ID��ѯ�ȼ���Ϣ
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String id = request.getParameter("id");
		// ��ѯ�ȼ�
		String[] user = userlevelDAO.queryByPrimayKey(id, "id", "tbl_UserLevel");
		// ������ת��json
		String[] attrNames = { "id", "name", "discount", "grade", "icon", "isShow","status"};
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �༭�û�
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// �༭�ɹ�
		// ��ȡ�ύ����������
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String discount = request.getParameter("discount");
		String grade = request.getParameter("grade");
		String icon = request.getParameter("icon");
		String isShow = request.getParameter("isShow");
		String status = request.getParameter("status");
		System.out.println("name"+name+"discount"+ discount+ "grade"+grade+ "icon"+icon+ "isShow"+isShow+"status"+status);
		// ׼������
		String[] Users = { id, name, discount, grade, icon, isShow,status };
		try {
			userlevelDAO.update(Users, "tbl_UserLevel", "id");
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
		String id = request.getParameter("id");
		// �ȸ���id��ѯ����û���������Ϣ��
		String[] users = userlevelDAO.queryByPrimayKey(id, "id", "tbl_UserLevel");
		// �޸�״̬λ3
		users[6] = "3";
		// �ٽ��޸ĺ�����ݱ��浽���ݿ���
		try {
			userlevelDAO.update(users, "tbl_UserLevel", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}
	
	// �޸���ʾ״̬
		public void IsShow(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			// ��ȡ�ύ����������
			String id = request.getParameter("id");
			String isShow = request.getParameter("isShow");
			// �ȸ���id��ѯ����û���������Ϣ��
			String[] users = userlevelDAO.queryByPrimayKey(id, "id", "tbl_UserLevel");
			// �޸�״̬λisShow
			users[5] = isShow;
			// �ٽ��޸ĺ�����ݱ��浽���ݿ���
			try {
				userlevelDAO.update(users, "tbl_UserLevel", "id");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().write("1");
		}

}

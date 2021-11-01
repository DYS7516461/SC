package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.ProductReviewDAO;
import com.CRMEB.util.EasyUIJsonUtil;

/**
 * Servlet implementation class ProductReviewServlet
 */
@WebServlet("/productreview")
public class ProductReviewServlet extends BaseServlet {
	private ProductReviewDAO productreviewDAO = new ProductReviewDAO();

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
		int total = productreviewDAO.queryTotal("tbl_productreviews");
		// ��ҳ��ѯ����
		String sql = "SELECT * from tbl_productreviews ORDER BY id ASC limit "
				+ ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = productreviewDAO.query(sql);
		// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = { "id", "information", "userName", "productScore", "serviceScore", "content", "reply", "datetime", "status" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}


	// ����û�
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// ����ɹ���״̬��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// ��ȡ�ύ����������
		String id = request.getParameter("id");
		String information = request.getParameter("information");
		String userName = request.getParameter("userName");
		String productScore = request.getParameter("productScore");
		String serviceScore = request.getParameter("serviceScore");
		String content = request.getParameter("content");
		String reply = request.getParameter("reply");
		String datetime = request.getParameter("datetime");
		String status = request.getParameter("status");
		datetime = sdf.format(new java.util.Date());

		// ׼������
		String[] Users = { "1", information, userName, productScore, serviceScore, content, reply, datetime, status };
		try {
			productreviewDAO.save(Users, "tbl_productreviews", "id");
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
		String id = request.getParameter("id");
		// ��ѯ�û�
		String[] user = productreviewDAO.queryByPrimayKey(id, "id", "tbl_productreviews");
		// ������ת��json
		String[] attrNames = {  "id", "information", "userName", "productScore", "serviceScore", "content", "reply", "datetime", "status" };
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �༭�û�
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// �༭�ɹ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// ��ȡ�ύ����������
		String id = request.getParameter("id");
		String information = request.getParameter("information");
		String userName = request.getParameter("userName");
		String productScore = request.getParameter("productScore");
		String serviceScore = request.getParameter("serviceScore");
		String content = request.getParameter("content");
		String reply = request.getParameter("reply");
		String datetime = request.getParameter("datetime");
		String status = request.getParameter("status");
		datetime = sdf.format(new java.util.Date());
		
		// ׼������
		String[] Users = { id, information, userName, productScore, serviceScore, content, reply, datetime, status };
		try {
			productreviewDAO.update(Users, "tbl_productreviews", "id");
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
		String[] users = productreviewDAO.queryByPrimayKey(id, "id", "tbl_productreviews");
		// �޸�״̬λ3
		users[8] = "3";
		// �ٽ��޸ĺ�����ݱ��浽���ݿ���
		try {
			productreviewDAO.update(users, "tbl_productreviews", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}


}

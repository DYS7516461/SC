package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.EssayDAO;
import com.CRMEB.util.EasyUIJsonUtil;
@WebServlet("/essay")
public class EssayServlet extends BaseServlet{

	private EssayDAO essayDAO = new EssayDAO();
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		String essayid = request.getParameter("essayid");
		String [] essay = essayDAO.queryByPrimayKey(essayid, "essayid", "tbl_Essay");
		
		
		essay[7] = "3";
		try {
			essayDAO.update(essay, "tbl_Essay", "essayid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
		
	}
	//�༭�û�
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";//�༭�ɹ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//��ȡ��������
		String essayid = request.getParameter("essayid");
		String essaytitle = request.getParameter("essaytitle");
		String essayclassify = request.getParameter("essayclassify");
		String essayview = request.getParameter("essayview");
		String essayauthor = request.getParameter("essayauthor");
		String essayintro = request.getParameter("essayintro");
		String turnovertime = request.getParameter("turnovertime");
		turnovertime = sdf.format(new java.util.Date());
		String status = request.getParameter("status");
		String[] essay = {essayid,essaytitle,essayclassify,essayview,essayauthor,essayintro,turnovertime,status};
		try {
			essayDAO.update(essay, "tbl_Essay","essayid");
		}catch(SQLException e) {
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}
	//����ID��ѯ������Ϣ
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String essayid = request.getParameter("essayid");
		//��ѯ����
		String[] essay = essayDAO.queryByPrimayKey(essayid, "essayid", "tbl_Essay");
		//������ת����json
		String[] attrNames = {"essayid","essaytitle","essayclassify","essayview","essayauthor","essayintro","turnovertime","status"};
		String json = com.CRMEB.util.EasyUIJsonUtil.array2json(essay, attrNames);
		//�������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
	//��������
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result ="1";
		
		
		//��ȡ�ύ����������
		String essayid = request.getParameter("essayid");
		String essaytitle = request.getParameter("essaytitle");
		String essayclassify = request.getParameter("essayclassify");
		String essayview = request.getParameter("essayview");
		String essayauthor = request.getParameter("essayauthor");
		String essayintro = request.getParameter("essayintro");
		String turnovertime = request.getParameter("turnovertime");
		String status = request.getParameter("status");
		//׼������
		String[] essay = {essayid,essaytitle,essayclassify,essayview,essayauthor,essayintro,turnovertime,"1"};
		try {
			essayDAO.save(essay, "tbl_Essay", null);
		}catch(SQLException e) {
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}
	//��ҳ��ѯ
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		}catch(NumberFormatException e) {
			
		}
		int rows = 23;
		//try {
		//	rows = Integer.parseInt(request.getParameter("rows"));
		//}catch(NumberFormatException e) {
			
		//}
		//��ѯ������
		int total = essayDAO.queryTotal("tbl_Essay");
		//��ҳ��ѯ����
		String sql = "select * from tbl_Essay limit "+((page - 1)*rows) + ","+rows;
		ArrayList<String[]> list = essayDAO.query(sql);
		//������������ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = {"essayid", "essaytitle", "essayclassify", "essayview", "essayauthor", "essayintro","turnovertime","status"};
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		//�������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
}

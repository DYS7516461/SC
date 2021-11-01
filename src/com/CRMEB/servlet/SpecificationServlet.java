package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.SpecificationDAO;
import com.CRMEB.util.EasyUIJsonUtil;
@WebServlet("/specification")
public class SpecificationServlet extends BaseServlet{
	private SpecificationDAO specificationDAO = new SpecificationDAO();
	//ɾ���û�
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		String specificationid = request.getParameter("specificationid");
		String [] specification = specificationDAO.queryByPrimayKey(specificationid, "specificationid", "tbl_Specification");
		specification[4] = "3";//����״̬Ϊ3
		try {
			specificationDAO.update(specification, "tbl_Specification", "specificationid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}
	//�༭����
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";//�༭�ɹ�
		//��ȡ����
		String specificationid = request.getParameter("specificationid");
		String specificationname = request.getParameter("specificationname");
		String specifications =request.getParameter("specifications");
		String goodsproperty = request.getParameter("goodsproperty");
		String status = request.getParameter("status");
		String [] specification = {specificationid,specificationname,specifications,goodsproperty,status};
		try {
			specificationDAO.update(specification, "tbl_Specification","specificationid");
		}catch(SQLException e) {
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}
	//����ID��ѯ�����Ϣ
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String specificationid = request.getParameter("specificationid");
		//��ѯ���
		String [] specification = specificationDAO.queryByPrimayKey(specificationid, "specificationid", "tbl_Specification");
		//������ת����josn
		String[] attrNames = {"specificationid","specificationname","specifications","goodsproperty","status"};
		//�������
		String json = EasyUIJsonUtil.array2json(specification, attrNames);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
	//����
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";
		//��ȡ�����ύ������
		String specificationid = request.getParameter("specificationid");
		String specificationname = request.getParameter("specificationname");
		String specifications =request.getParameter("specifications");
		String goodsproperty = request.getParameter("goodsproperty");
		String status = request.getParameter("status");
		//׼������
		String [] specification = {specificationid,specificationname,specifications,goodsproperty,"1"};
		try {
			specificationDAO.save(specification, "tbl_Specification", "specificationid");
			
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
		try {
			rows = Integer.parseInt(request.getParameter("rows"));
		}catch(NumberFormatException e) {
			
		}
		//��ҳ��ѯ������
		int total = specificationDAO.queryTotal("tbl_Specification");
		//��ҳ��ѯ����
		String sql = "select * from tbl_Specification limit "+((page - 1)*rows) + ","+rows;
		ArrayList<String[]> list = specificationDAO.query(sql);
		//������������ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
		String[] columns = {"specificationid","specificationname","specifications","goodsproperty","status"};
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		//�������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
	
}

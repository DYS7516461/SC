package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.ClassifyDAO;
import com.CRMEB.util.EasyUIJsonUtil;

@WebServlet("/classify")
public class ClassifyServlet extends BaseServlet{
	private ClassifyDAO classifyDAO = new ClassifyDAO();
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		String classifyid = request.getParameter("classifyid");
		String [] classify = classifyDAO.queryByPrimayKey(classifyid, "classifyid", "tbl_Classify");
		classify[2] = "3";
		try {
			classifyDAO.update(classify, "tbl_Classify", "classifyid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";
		String classifyid = request.getParameter("classifyid");
		String classifyname = request.getParameter("classifyname");
		String status = request.getParameter("status");
		String[] classify = {classifyid,classifyname,status};
		try {
			classifyDAO.update(classify, "tbl_Classify","classifyid");
		}catch(SQLException e) {
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String classifyid = request.getParameter("classifyid");
		String [] classify = classifyDAO.queryByPrimayKey(classifyid, "classifyid", "tbl_Classify");
		String [] attrNames = {"classifyid","classifyname","status"};
		String json = com.CRMEB.util.EasyUIJsonUtil.array2json(classify, attrNames);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result ="1";
		String classifyid = request.getParameter("classifyid");
		String classifyname = request.getParameter("classifyname");
		String status = request.getParameter("status");
		String []classify = {classifyid,classifyname,"1"};
		try {
			classifyDAO.save(classify, "tbl_Classify",null);
		}catch(SQLException e) {
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		}catch(NumberFormatException e) {
			
		}
		int rows = 20;
		try {
			rows = Integer.parseInt(request.getParameter("rows"));
		}catch(NumberFormatException e) {
			
		}
		int total = classifyDAO.queryTotal("tbl_Classify");
		String sql = "select * from tbl_Classify limit "+((page - 1)*rows) + ","+rows;
		ArrayList<String[]> list =classifyDAO.query(sql);
		String[] columns = {"classifyid","classifyname","status"};
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

}

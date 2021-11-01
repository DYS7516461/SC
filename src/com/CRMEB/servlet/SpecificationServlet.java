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
	//删除用户
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		String specificationid = request.getParameter("specificationid");
		String [] specification = specificationDAO.queryByPrimayKey(specificationid, "specificationid", "tbl_Specification");
		specification[4] = "3";//定义状态为3
		try {
			specificationDAO.update(specification, "tbl_Specification", "specificationid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}
	//编辑方法
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";//编辑成功
		//获取数据
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
	//根据ID查询规格信息
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String specificationid = request.getParameter("specificationid");
		//查询规格
		String [] specification = specificationDAO.queryByPrimayKey(specificationid, "specificationid", "tbl_Specification");
		//将数据转换成josn
		String[] attrNames = {"specificationid","specificationname","specifications","goodsproperty","status"};
		//输出数据
		String json = EasyUIJsonUtil.array2json(specification, attrNames);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
	//保存
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";
		//获取所有提交的数据
		String specificationid = request.getParameter("specificationid");
		String specificationname = request.getParameter("specificationname");
		String specifications =request.getParameter("specifications");
		String goodsproperty = request.getParameter("goodsproperty");
		String status = request.getParameter("status");
		//准备数组
		String [] specification = {specificationid,specificationname,specifications,goodsproperty,"1"};
		try {
			specificationDAO.save(specification, "tbl_Specification", "specificationid");
			
		}catch(SQLException e) {
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}
	//分页查询
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
		//分页查询总条数
		int total = specificationDAO.queryTotal("tbl_Specification");
		//分页查询数据
		String sql = "select * from tbl_Specification limit "+((page - 1)*rows) + ","+rows;
		ArrayList<String[]> list = specificationDAO.query(sql);
		//根据总条数查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = {"specificationid","specificationname","specifications","goodsproperty","status"};
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		//输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
	
}

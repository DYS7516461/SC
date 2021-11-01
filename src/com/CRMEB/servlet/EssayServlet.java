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
	//编辑用户
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";//编辑成功
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取所有数据
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
	//根据ID查询文章信息
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String essayid = request.getParameter("essayid");
		//查询文章
		String[] essay = essayDAO.queryByPrimayKey(essayid, "essayid", "tbl_Essay");
		//将数据转换成json
		String[] attrNames = {"essayid","essaytitle","essayclassify","essayview","essayauthor","essayintro","turnovertime","status"};
		String json = com.CRMEB.util.EasyUIJsonUtil.array2json(essay, attrNames);
		//输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
	//保存文章
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result ="1";
		
		
		//获取提交的所有数据
		String essayid = request.getParameter("essayid");
		String essaytitle = request.getParameter("essaytitle");
		String essayclassify = request.getParameter("essayclassify");
		String essayview = request.getParameter("essayview");
		String essayauthor = request.getParameter("essayauthor");
		String essayintro = request.getParameter("essayintro");
		String turnovertime = request.getParameter("turnovertime");
		String status = request.getParameter("status");
		//准备数组
		String[] essay = {essayid,essaytitle,essayclassify,essayview,essayauthor,essayintro,turnovertime,"1"};
		try {
			essayDAO.save(essay, "tbl_Essay", null);
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
		//try {
		//	rows = Integer.parseInt(request.getParameter("rows"));
		//}catch(NumberFormatException e) {
			
		//}
		//查询总条数
		int total = essayDAO.queryTotal("tbl_Essay");
		//分页查询数据
		String sql = "select * from tbl_Essay limit "+((page - 1)*rows) + ","+rows;
		ArrayList<String[]> list = essayDAO.query(sql);
		//根据总条数查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = {"essayid", "essaytitle", "essayclassify", "essayview", "essayauthor", "essayintro","turnovertime","status"};
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		//输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
}

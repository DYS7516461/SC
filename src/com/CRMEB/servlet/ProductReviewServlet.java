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

	// 分页查询
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int page = 1;// 页码，默认1
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
		}
		int rows = 20;// 每页大小，默认20
		try {
			rows = Integer.parseInt(request.getParameter("rows"));
		} catch (NumberFormatException e) {
		}
		// 查询总条数
		int total = productreviewDAO.queryTotal("tbl_productreviews");
		// 分页查询数据
		String sql = "SELECT * from tbl_productreviews ORDER BY id ASC limit "
				+ ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = productreviewDAO.query(sql);
		// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = { "id", "information", "userName", "productScore", "serviceScore", "content", "reply", "datetime", "status" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}


	// 添加用户
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 保存成功的状态码
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取提交的所有数据
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

		// 准备数组
		String[] Users = { "1", information, userName, productScore, serviceScore, content, reply, datetime, status };
		try {
			productreviewDAO.save(Users, "tbl_productreviews", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		// 输出结果
		response.getWriter().write(result);
	}

	// 根据ID查询用户信息
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String id = request.getParameter("id");
		// 查询用户
		String[] user = productreviewDAO.queryByPrimayKey(id, "id", "tbl_productreviews");
		// 将数组转换json
		String[] attrNames = {  "id", "information", "userName", "productScore", "serviceScore", "content", "reply", "datetime", "status" };
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 编辑用户
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 编辑成功
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取提交的所有数据
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
		
		// 准备数组
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

	// 删除用户
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 获取提交的所有数据
		String id = request.getParameter("id");
		// 先根据id查询这个用户的所有信息，
		String[] users = productreviewDAO.queryByPrimayKey(id, "id", "tbl_productreviews");
		// 修改状态位3
		users[8] = "3";
		// 再将修改后的数据保存到数据库中
		try {
			productreviewDAO.update(users, "tbl_productreviews", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}


}

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
		int total = userlevelDAO.queryTotal("tbl_UserLevel");
		// 分页查询数据
		String sql = "SELECT id,name,discount,grade,icon,isShow,status FROM tbl_UserLevel limit "
				+ ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = userlevelDAO.query(sql);
		// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = { "id", "name", "discount", "grade", "icon", "isShow","status" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 添加等级
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 保存成功的状态码
		// 获取提交的所有数据
		String name = request.getParameter("name");
		String discount = request.getParameter("discount");
		String grade = request.getParameter("grade");
		String icon = request.getParameter("icon");
		String isShow = request.getParameter("isShow");
		String status = request.getParameter("status");
		System.out.println("isShow:"+isShow);
		// 准备数组
		String[] Users = { "1", name, discount, grade, icon, isShow,status };
		try {
			userlevelDAO.save(Users, "tbl_UserLevel", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		// 输出结果
		response.getWriter().write(result);
	}

	// 根据ID查询等级信息
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String id = request.getParameter("id");
		// 查询等级
		String[] user = userlevelDAO.queryByPrimayKey(id, "id", "tbl_UserLevel");
		// 将数组转换json
		String[] attrNames = { "id", "name", "discount", "grade", "icon", "isShow","status"};
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 编辑用户
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 编辑成功
		// 获取提交的所有数据
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String discount = request.getParameter("discount");
		String grade = request.getParameter("grade");
		String icon = request.getParameter("icon");
		String isShow = request.getParameter("isShow");
		String status = request.getParameter("status");
		System.out.println("name"+name+"discount"+ discount+ "grade"+grade+ "icon"+icon+ "isShow"+isShow+"status"+status);
		// 准备数组
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

	// 删除用户
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 获取提交的所有数据
		String id = request.getParameter("id");
		// 先根据id查询这个用户的所有信息，
		String[] users = userlevelDAO.queryByPrimayKey(id, "id", "tbl_UserLevel");
		// 修改状态位3
		users[6] = "3";
		// 再将修改后的数据保存到数据库中
		try {
			userlevelDAO.update(users, "tbl_UserLevel", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}
	
	// 修改显示状态
		public void IsShow(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			// 获取提交的所有数据
			String id = request.getParameter("id");
			String isShow = request.getParameter("isShow");
			// 先根据id查询这个用户的所有信息，
			String[] users = userlevelDAO.queryByPrimayKey(id, "id", "tbl_UserLevel");
			// 修改状态位isShow
			users[5] = isShow;
			// 再将修改后的数据保存到数据库中
			try {
				userlevelDAO.update(users, "tbl_UserLevel", "id");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.getWriter().write("1");
		}

}

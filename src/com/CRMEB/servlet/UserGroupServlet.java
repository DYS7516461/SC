package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.UserGroupDAO;
import com.CRMEB.util.EasyUIJsonUtil;

/**
 * Servlet implementation class UserGroup
 */
@WebServlet("/usergroup")
public class UserGroupServlet extends BaseServlet {
	private UserGroupDAO userGroupDAO = new UserGroupDAO();

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
		int total = userGroupDAO.queryTotal("tbl_UserGroup");
		// 分页查询数据
		String sql = "SELECT * FROM tbl_UserGroup limit " + ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = userGroupDAO.query(sql);
		// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = { "id", "groupName","status" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 添加标签
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 保存成功的状态码
		// 获取提交的所有数据
		String groupName = request.getParameter("groupName");
		String status = request.getParameter("status");
		// 准备数组
		String[] Users = { "1", groupName, status };
		try {
			userGroupDAO.save(Users, "tbl_UserGroup", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		// 输出结果
		response.getWriter().write(result);
	}

	// 根据ID查询标签信息
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String id = request.getParameter("id");
		// 查询标签
		String[] user = userGroupDAO.queryByPrimayKey(id, "id", "tbl_UserGroup");
		// 将数组转换json
		String[] attrNames = { "id", "groupName", "status" };
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 编辑标签
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 编辑成功
		// 获取提交的所有数据
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		String groupName = request.getParameter("groupName");
		// 准备数组
		String[] Users = { id, groupName, status };
		try {
			userGroupDAO.update(Users, "tbl_UserGroup", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
	}

	// 删除标签
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 获取提交的所有数据
		String id = request.getParameter("id");
		// 先根据id查询这个标签的所有信息，
		String[] users = userGroupDAO.queryByPrimayKey(id, "id", "tbl_UserGroup");
		// 修改状态位3
		users[2] = "3";
		// 再将修改后的数据保存到数据库中
		try {
			userGroupDAO.update(users, "tbl_UserGroup", "id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}

}

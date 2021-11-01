package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.UserDAO;
import com.CRMEB.util.EasyUIJsonUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {
	private UserDAO userDAO = new UserDAO();

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
		int total = userDAO.queryTotal("tbl_user");
		// 分页查询数据
		String sql = "SELECT uid,tbl_user.`Name`,tbl_UserLevel.`name` AS Level,Balance,Integral,tbl_user.`status`,phone,tbl_UserType.`name` AS UserType,tbl_UserGroup.groupName AS UserGroup,tbl_UserLabel.labelName AS UserLabel "
				+ "FROM tbl_user,tbl_UserLevel,tbl_UserType,tbl_UserLabel,tbl_UserGroup "
				+ "WHERE tbl_user.`Level`=tbl_UserLevel.id AND tbl_user.UserType=tbl_UserType.id AND tbl_user.UserGroup=tbl_UserGroup.id AND tbl_user.UserLabel=tbl_UserLabel.id ORDER BY uid ASC limit "
				+ ((page - 1) * rows) + "," + rows;
		ArrayList<String[]> list = userDAO.query(sql);
		// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = { "uid", "Name", "Level", "Balance", "Integral", "status", "phone", "UserType", "UserGroup",
				"UserLabel" };
		String json = EasyUIJsonUtil.datagridJson(total, list, columns);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 用户等级列表
	public void userLevelList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String sql = "SELECT id,name FROM tbl_UserLevel";
		ArrayList<String[]> list = userDAO.query(sql);

		int total = userDAO.queryTotal("tbl_UserLevel");
		// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = { "id", "labelName" };
		String json = EasyUIJsonUtil.comboBoxJson(list, 0, 1);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 用户分组列表
	public void userGroupList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String sql = "SELECT id,groupName FROM tbl_UserGroup";
		ArrayList<String[]> list = userDAO.query(sql);

		int total = userDAO.queryTotal("tbl_UserGroup");
		// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = { "id", "groupName" };
		String json = EasyUIJsonUtil.comboBoxJson(list, 0, 1);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 用户标签列表
	public void userLabelList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String sql = "SELECT id,labelName FROM tbl_UserLabel";
		ArrayList<String[]> list = userDAO.query(sql);

		int total = userDAO.queryTotal("tbl_UserLabel");
		// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
		String[] columns = { "id", "labelName" };
		String json = EasyUIJsonUtil.comboBoxJson(list, 0, 1);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 添加用户
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 保存成功的状态码
		// 获取提交的所有数据
		String Name = request.getParameter("Name");
		String Balance = String.valueOf((Double.valueOf(request.getParameter("Balance"))) * 100);
		String Integral = String.valueOf((Double.valueOf(request.getParameter("Integral"))) * 100);
		String phone = request.getParameter("phone");
		String Level = request.getParameter("Level");
		String UserType = request.getParameter("UserType");
		String status = request.getParameter("status");
		String UserGroup = request.getParameter("UserGroup");
		String UserLabel = request.getParameter("status");
		// 准备数组
		String[] Users = { "1", Name, Level, Balance, Integral, status, phone, UserType, UserGroup, UserLabel };
		try {
			userDAO.save(Users, "tbl_user", "uid");
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
		String uid = request.getParameter("uid");
		// 查询用户
		String[] user = userDAO.queryByPrimayKey(uid, "uid", "tbl_user");
		// 将数组转换json
		String[] attrNames = { "uid", "Name", "Level", "Balance", "Integral", "status", "phone", "UserType",
				"UserGroup", "UserLabel" };
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 编辑用户
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 编辑成功
		// 获取提交的所有数据
		String uid = request.getParameter("uid");
		String Name = request.getParameter("Name");
		String Balance = String.valueOf((Double.valueOf(request.getParameter("Balance"))) * 100);
		String Integral = request.getParameter("Integral");
		String phone = request.getParameter("phone");
		String Level = request.getParameter("Level");
		String UserType = request.getParameter("UserType");
		String UserGroup = request.getParameter("UserGroup");
		String UserLabel = request.getParameter("UserLabel");
		String status = request.getParameter("status");
		// 准备数组
		String[] Users = { uid, Name, Level, Balance, Integral, status, phone, UserType, UserGroup, UserLabel };
		try {
			userDAO.update(Users, "tbl_user", "uid");
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
		String uid = request.getParameter("uid");
		// 先根据id查询这个用户的所有信息，
		String[] users = userDAO.queryByPrimayKey(uid, "uid", "tbl_user");
		// 修改状态位3
		users[5] = "3";
		// 再将修改后的数据保存到数据库中
		try {
			userDAO.update(users, "tbl_user", "uid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}

	// 批量设置分组
	public void setGroup(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String result = "1";
		String UserGroup = request.getParameter("UserGroup");
		String uids = request.getParameter("uids");
		// 将学生编号进行分割
		String[] ids = uids.split(",");
		// 逐个修改，当然也可以批量修改。
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			// 先根据id查询这个用户的所有信息，
			String[] user = userDAO.queryByPrimayKey(id, "uid", "tbl_user");
			// 设置班级编号
			user[8] = UserGroup;
			// 保存
			try {
				userDAO.update(user, "tbl_user", "uid");
			} catch (SQLException e) {
				e.printStackTrace();
				result = "0";
			}
		}
		response.getWriter().write(result);
	}

	// 批量设置分组
	public void setLabels(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String result = "1";
		String UserLabel = request.getParameter("UserLabel");
		String uids = request.getParameter("uidss");
		System.out.println(uids);
		//System.out.println(UserLabel);
		// 将学生编号进行分割
		String[] ids = uids.split(",");
		// 逐个修改，当然也可以批量修改。
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			System.out.println(ids[i]);
			// 先根据id查询这个用户的所有信息，
			String[] user = userDAO.queryByPrimayKey(id, "uid", "tbl_user");
			// 设置班级编号
			user[9] = UserLabel;
			// 保存
			try {
				userDAO.update(user, "tbl_user", "uid");
			} catch (SQLException e) {
				e.printStackTrace();
				result = "0";
			}
		}
		response.getWriter().write(result);
	}

}

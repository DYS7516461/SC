package com.CRMEB.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.CRMEB.dao.GoodsDAO;
import com.CRMEB.util.EasyUIJsonUtil;
@WebServlet("/goods")
public class GoodsServlet extends BaseServlet {
	private GoodsDAO goodsDAO = new GoodsDAO();
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 获取提交的所有数据
		String goodsid = request.getParameter("goodsid");
		//先根据id查询这个用户的所有信息，
		String[] goods = goodsDAO.queryByPrimayKey(goodsid, "goodsid", "tbl_goods");
		//修改状态位3
		goods[4] = "3";
		//再将修改后的数据保存到数据库中
		try {
			goodsDAO.update(goods, "tbl_goods", "goodsid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 编辑成功
		// 获取提交的所有数据
		String goodsid = request.getParameter("goodsid");
		String goodsname = request.getParameter("goodsname");
		String goodsprice = request.getParameter("goodsprice");
		String goodsnumber = request.getParameter("goodsnumber");
		String goodsstate = request.getParameter("goodsstate");
		String goodstime = request.getParameter("goodstime");
		String[] user = { goodsid, goodsname, goodsprice, goodsnumber, goodsstate,goodstime };
		try {
			goodsDAO.update(user, "tbl_goods", "goodsid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		response.getWriter().write(result);
		
	}
	public void queryById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String goodsid = request.getParameter("goodsid");
		// 查询用户
		String[] user = goodsDAO.queryByPrimayKey(goodsid, "goodsid", "tbl_goods");
		// 将数组转换json
		String[] attrNames = { "goodsid", "goodsname", "goodsprice", "goodsnumber", "goodsstate" ,"goodstime"};
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// 输出数据
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// 保存教员
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// 保存成功的状态码
		// 获取提交的所有数据
				String goodsid = request.getParameter("goodsid");
				String goodsname = request.getParameter("goodsname");
				String goodsprice = request.getParameter("goodsprice");
				String goodsnumber = request.getParameter("goodsnumber");
				String goodsstate = request.getParameter("goodsstate");
				String goodstime = request.getParameter("goodstime");
				
				// 准备数组
				String[] goods = { "1", goodsname, goodsprice, goodsnumber, goodsstate,goodstime};
				try {
					goodsDAO.save(goods, "tbl_goods", "goodsid");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = "0";
				}
				// 输出结果
				response.getWriter().write(result);
			}

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
				int total = goodsDAO.queryTotal("tbl_goods");
				// 分页查询数据
				String sql = "select * from tbl_goods limit " + ((page - 1) * rows) + "," + rows;
				ArrayList<String[]> list = goodsDAO.query(sql);
				// 根据总条数和查询的数据包装一个EasyUI的Datagrid的数据结构
				String[] columns = { "goodsid", "goodsname", "goodsprice", "goodsnumber", "goodsstate" ,"goodstime" };
				String json = EasyUIJsonUtil.datagridJson(total, list, columns);
				// 输出数据
				response.setContentType("application/json;charset=utf-8");
				response.getWriter().write(json);
			}


}


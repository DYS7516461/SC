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
		// ��ȡ�ύ����������
		String goodsid = request.getParameter("goodsid");
		//�ȸ���id��ѯ����û���������Ϣ��
		String[] goods = goodsDAO.queryByPrimayKey(goodsid, "goodsid", "tbl_goods");
		//�޸�״̬λ3
		goods[4] = "3";
		//�ٽ��޸ĺ�����ݱ��浽���ݿ���
		try {
			goodsDAO.update(goods, "tbl_goods", "goodsid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("1");
	}
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// �༭�ɹ�
		// ��ȡ�ύ����������
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
		// ��ѯ�û�
		String[] user = goodsDAO.queryByPrimayKey(goodsid, "goodsid", "tbl_goods");
		// ������ת��json
		String[] attrNames = { "goodsid", "goodsname", "goodsprice", "goodsnumber", "goodsstate" ,"goodstime"};
		String json = EasyUIJsonUtil.array2json(user, attrNames);
		// �������
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	// �����Ա
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String result = "1";// ����ɹ���״̬��
		// ��ȡ�ύ����������
				String goodsid = request.getParameter("goodsid");
				String goodsname = request.getParameter("goodsname");
				String goodsprice = request.getParameter("goodsprice");
				String goodsnumber = request.getParameter("goodsnumber");
				String goodsstate = request.getParameter("goodsstate");
				String goodstime = request.getParameter("goodstime");
				
				// ׼������
				String[] goods = { "1", goodsname, goodsprice, goodsnumber, goodsstate,goodstime};
				try {
					goodsDAO.save(goods, "tbl_goods", "goodsid");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = "0";
				}
				// ������
				response.getWriter().write(result);
			}

			// ��ҳ��ѯ
			public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				int page = 1;// ҳ�룬Ĭ��1
				try {
					page = Integer.parseInt(request.getParameter("page"));
				} catch (NumberFormatException e) {
				}
				int rows = 20;// ÿҳ��С��Ĭ��20
				try {
					rows = Integer.parseInt(request.getParameter("rows"));
				} catch (NumberFormatException e) {
				}
				// ��ѯ������
				int total = goodsDAO.queryTotal("tbl_goods");
				// ��ҳ��ѯ����
				String sql = "select * from tbl_goods limit " + ((page - 1) * rows) + "," + rows;
				ArrayList<String[]> list = goodsDAO.query(sql);
				// �����������Ͳ�ѯ�����ݰ�װһ��EasyUI��Datagrid�����ݽṹ
				String[] columns = { "goodsid", "goodsname", "goodsprice", "goodsnumber", "goodsstate" ,"goodstime" };
				String json = EasyUIJsonUtil.datagridJson(total, list, columns);
				// �������
				response.setContentType("application/json;charset=utf-8");
				response.getWriter().write(json);
			}


}


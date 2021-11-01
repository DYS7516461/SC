package com.CRMEB.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张双虎
 *
 * @TODO
 */
public class BaseServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取要执行的方法名称
		String methodName = req.getParameter("method");
		if(methodName==null || methodName.equals("")){
			throw new RuntimeException("你的请求中没有method,或者method编写错误");
		}
		//通过反射获取方法对象
		Class<? extends BaseServlet> clazz = this.getClass();
		try {
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			if(method==null){
				throw new RuntimeException("你的请求中没有method,或者method编写错误");
			}
			//执行这个方法
			method.invoke(this,req, resp);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		this.doGet(req,resp);
	}
}

package com.CRMEB.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ��˫��
 *
 * @TODO
 */
public class EasyUIJsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * �����ǲ�ѯ���б�ת��Ϊ EasyUITree��Ҫ�Ľṹ��json
	 * @param data
	 * @param idIndex
	 * @param textIndex
	 * @return
	 */
	public static String list2EasyUITreeNode(List<String[]> data,int idIndex,int textIndex){
		List<Map> result = new ArrayList<Map>();
		for(String [] info : data){
			Map map = new HashMap<>();
			map.put("id", info[idIndex]);
			map.put("text", info[textIndex]);
			result.add(map);
		}
		try {
			return mapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ������ת��Ϊjson
	 * @param data
	 * @param attrNames
	 * @return
	 */
	public static String array2json(Object [] data,String [] attrNames){
		Map map = new HashMap();
		for(int i = 0;i<attrNames.length;i++){
			map.put(attrNames[i], data[i]);
		}
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * ����ѯ���б�ת��combobox��Ҫ�����ݽṹ  [{id:11,text:xxx},{id:xx,text:xxx}]
	 * @param data ��ѯ�������б�
	 * @param idIndex  �����еĵڼ���Ԫ����Ϊѡ���id
	 * @param textIndex   ����ĵڼ���Ԫ����Ϊѡ����ı�
	 * @return
	 */
	public static String comboBoxJson(List<String[]> data,int idIndex,int textIndex){
		List<Map> all = new ArrayList<Map>();
		//����ת��
		for(int i = 0;i < data.size();i++){
			String[] infos = data.get(i);
			Map node = new HashMap();
			node.put("id", infos[idIndex]);
			node.put("text", infos[textIndex]);
			all.add(node);
		}
		try {
			return mapper.writeValueAsString(all);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ���datagrid�����ݽṹ
	 * @param total ������
	 * @param data ���� List<String> 
	 * @param columns ���� ["isbn","title","description","authorName","publisherName"]
	 * @return
	 */
	public static String datagridJson(int total,List<String[]> data,String [] columns){
		Map all = new HashMap();
		all.put("total",total);
		List<Map> dataList = new ArrayList<Map>();
		for(int i = 0;i<data.size();i++){
			String[] info = data.get(i);
			Map element = new HashMap<>();
			for(int j = 0;j<columns.length;j++){
				element.put(columns[j], info[j]);
			}
			dataList.add(element);
		}
		all.put("rows", dataList);
		try {
			return mapper.writeValueAsString(all);
		} catch (JsonProcessingException e) {
			e.printStackTrace();//ת���쳣
		}
		return null;
	}
}

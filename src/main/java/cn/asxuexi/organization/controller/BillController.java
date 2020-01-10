package cn.asxuexi.organization.controller;
/**
 * @author fanjunguo
 * @version 2019年4月1日 下午3:23:28
 * @description 账单管理相关类
 */

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.organization.service.BillService;
@Controller
public class BillController {
	
	@Resource
	private BillService service;
	
	/**
	 * @description 存储机构的账单信息
	 * @param items 新增的费用项信息
	 * @param courseId 
	 * @param classId
	 * @param period 账期
	 * @return 存储结果,json
	 */
	@RequestMapping("bill/saveBill.action")
	@ResponseBody
	public Map<String, Object> saveBill(String items,String courseId,String classId,String period){
		Map<String, Object> json = service.saveBill(items, courseId, classId, period);
		return json;
	}
	
	/**
	 * @description 查询所有的班级列表(包含了上次结算日期)
	 */
	@RequestMapping(value="bill/getClassList.action")
	@ResponseBody
	public Map<String, Object> getClassList(int page,int rows){
		Map<String, Object> resultJson = service.getClassList(page, rows);
		return resultJson;
	}
	
	/**
	 * @description 查询班级下所有的账单. 
	 * @param classId
	 * @return
	 */
	@RequestMapping(value="bill/getAllBills.action")
	@ResponseBody
	public Map<String, Object> getAllBills(String classId){
		Map<String, Object> allBills = service.getAllBills(classId);
		return allBills;
	}
	
	/**
	 * @description 查询班级下,单个账期内的账单
	 * @param billId 账期id
	 */
	@RequestMapping(value="bill/getBill.action")
	@ResponseBody
	public Map<String, Object> getBill(String billId,String classId,String startDate,String endDate){
		Map<String, Object> json = service.getBill(billId, classId, startDate, endDate);
		return json;
	}
	
}

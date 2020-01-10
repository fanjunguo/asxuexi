package cn.asxuexi.organization.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.asxuexi.message.tool.MessageTool;
import cn.asxuexi.organization.dao.AttendanceDao;
import cn.asxuexi.organization.dao.BillDao;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.RandomTool;

/**
 * @author fanjunguo
 * @version 2019年4月1日 下午4:00:33
 * @description service:机构账单
 */
@Service
public class BillServiceImpl implements BillService {
	@Resource
	private BillDao dao;
	@Resource
	private AttendanceDao attendanceDao;
	@Autowired
	private MessageTool messageTool;
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	//保存账单信息,如费用项,账期
	@Override
	public Map<String, Object> saveBill(String items,String courseId,String classId,String period){
		Map<String, Object> json=new HashMap<String, Object>();
		int code=600;
		String message="success";
		//获取当前时间和随机数,形成id
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String timeOfString = format.format(now);
		int randomInt = RandomTool.getRandomInt(8);
		String id="bill"+timeOfString+randomInt;
		//将账单周期period转成localdata格式
		int index = period.indexOf("——");
		LocalDate startDate=LocalDate.parse(period.substring(0, index));
		LocalDate endDate=LocalDate.parse(period.substring(index+2));
		
		//计算总价
		List<Map<String, Object>> itemList = JSONObject.parseObject(items, new TypeReference<List<Map<String,Object>>>() {});
		double totalOfUnitPrice = 0,totalOfOncePrice=0;
		for (Map<String, Object> map : itemList) {
			int type=(int) map.get("type");
			if (type==1) {
				totalOfUnitPrice+=Double.valueOf((String)map.get("price"));
			} else {
				totalOfOncePrice+=Double.parseDouble((String)map.get("price"));
			}
		}
		
		try {
			dao.saveBill(id,items, courseId, classId, startDate, endDate,now);
			/*
			 * 1.查询此班级下所有的用户userid
			 * 2.为每个用户保存账单信息
			 * 3.发送消息-给每个用户发送消息通知账单生成
			 * */
			List<Map<String, Object>> totalOfAttendanceDay = attendanceDao.getTotalOfAttendanceDay(classId, startDate, endDate);  //查询班级下所有的用户id
			dao.insertBillInfoOfUsers(totalOfAttendanceDay,id,LocalDateTime.now(),totalOfUnitPrice,totalOfOncePrice);
			String title="新的课程账单";
			String content="您的课程有新的账单生成,请按时结算.点击查看详情";
			List<String> addresseeList = new ArrayList<>();
			for (Map<String, Object> map : totalOfAttendanceDay) {
				addresseeList.add((String)map.get("user_id"));
			}
			int pushType=1;
			int messageType=103;
			int addresseeType=1;
			messageTool.sendMessageToList(title, content, addresseeList, pushType, messageType, addresseeType, null);
		} catch (Exception e) {
			logger.error("保存数据库发生异常", e);
			code=401;
			message="failure";
		}
		json.put("code", code);
		json.put("message", message);
		
		return json;
	}
	
	//获取机构下,所有的班级列表
	@Override
	public Map<String, Object> getClassList(int page,int rows){
		//获取机构id
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> grid=new HashMap<>();
		if (orgId!=null) {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> classList = attendanceDao.getClassList(orgId);
			for (Map<String, Object> map : classList) {
				LocalDate lastBillDate = attendanceDao.getLastBillDate((String)map.get("class_id"));
				map.put("lastBillDate", lastBillDate+"");
			}
			
			long total = ((Page<Map<String, Object>>) classList).getTotal();
			grid.put("rows", classList);
			grid.put("total", total);
		}else {
			logger.error("获取机构id失败");
		}
		return grid;
	}
	
	//查询所有的账期,以及最新的一期账期中,考勤和费用情况.
	@Override
	public Map<String, Object> getAllBills(String classId){
		
		Map<String, Object> json=new HashMap<>();
		Map<String, Object> data=new HashMap<>();
		int code=600;
		String message="success";
		
		List<Map<String, Object>> allPeriods = dao.getAllPeriods(classId);  //查询出所有的账期
		if (allPeriods.size()==0) {
			code=500;
			message="noDate";
		} else {
			Map<String, Object> theNewestBill = allPeriods.get(0); //最新一期的账期对象
			LocalDate startDate= LocalDate.parse((String)theNewestBill.get("start_date"), DateTimeFormatter.ISO_LOCAL_DATE);
			LocalDate endDate=LocalDate.parse((String)theNewestBill.get("end_date"), DateTimeFormatter.ISO_LOCAL_DATE);
			//获取最新的一期账单的考勤情况
			List<Map<String, Object>> totalOfAttendance = attendanceDao.getTotalOfAttendanceDay(classId, startDate, endDate); 
			//获取指定账期内的费用项(items).如果有账期,但是items是空,说明用户没有存items
			String items = dao.getItems((String)theNewestBill.get("id"));
			data.put("rows",totalOfAttendance );
			data.put("items",items);
		}
		data.put("allPeriods", allPeriods);
		json.put("code", code);
		json.put("message", message);
		json.put("data", data);
		return json;
	}
	
	//查询单个账期下的账单
	@Override
	public Map<String, Object> getBill(String billId,String classId,String startDate,String endDate){
		Map<String, Object> json=new HashMap<>();
		Map<String, Object> data=new HashMap<>();
		int code=600;
		try {
			String items = dao.getItems(billId);
			LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
			LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
			List<Map<String, Object>> totalOfAttendanceDay = attendanceDao.getTotalOfAttendanceDay(classId, start, end);
			data.put("items", items);
			data.put("rows", totalOfAttendanceDay);
		} catch (Exception e) {
			code=400;
			logger.error("查询单个账单出现异常", e);
		}
		json.put("code", code);
		json.put("data",data);
		return json;
	}
}

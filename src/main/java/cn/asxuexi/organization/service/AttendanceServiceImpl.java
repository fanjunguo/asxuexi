package cn.asxuexi.organization.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.asxuexi.organization.dao.AttendanceDao;
import cn.asxuexi.tool.GetOrgIdFromRedis;

/**
 * @author fanjunguo
 * @version 2019年3月8日 下午8:55:39
 * @description 
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

	@Resource
	private AttendanceDao dao;
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	//获取班级名单
	@Override
	public Map<String, Object> getClassList(int page,int rows){
		//获取机构id
		String orgId = GetOrgIdFromRedis.getOrgId();
		Map<String, Object> grid=new HashMap<>();
		if (orgId!=null) {
			PageHelper.startPage(page, rows);
			List<Map<String, Object>> classList = dao.getClassList(orgId);
			List<Map<String, Object>> classOfAttendance = dao.isCheckedAttendance(LocalDate.now());
			if (classOfAttendance.size()>0) {
				for (Map<String, Object> eachClass : classList) {
						for (Map<String, Object> classOfAttended : classOfAttendance) {
							if (classOfAttended.get("class_id").equals(eachClass.get("class_id"))) {
								eachClass.put("attendance", 1); //1表示已经考勤过了
								break;
							}else {
								eachClass.put("attendance", 0); //0表示没有考勤过
							}
						}
						
				}
			}
			else {
				for (Map<String, Object> eachClass : classList) {
					eachClass.put("attendance", 0);
				}
			}
			
			
			long total = ((Page<Map<String, Object>>) classList).getTotal();
			grid.put("rows", classList);
			grid.put("total", total);
			
		}else {
			logger.error("获取机构id失败");
		}

		return grid;
	}
	
	/**
	 * 获取班级下,所有 在校 的学生名单
	 */
	@Override
	public Map<String, Object> getStuents(int page,int rows,String classId){
		Map<String, Object> result=new HashMap<>();
		PageHelper.startPage(page, rows);
		List<Map<String, Object>> stuents = dao.getStuents(classId);
		long total = ((Page<Map<String, Object>>) stuents).getTotal();
		result.put("rows", stuents);
		result.put("total", total);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> saveAttendance(String attendance,String classId){
		List<HashMap> parseArray = JSONObject.parseArray(attendance, HashMap.class);
		LocalDate today=LocalDate.now();
		Map<String, Object> json=new HashMap<String, Object>();
		int code;
		String message;
		try {
			int result = dao.saveAttendance(parseArray, classId, today,LocalDateTime.now());
			if (result!=0) {
				code=600;
				message="success";
			}else {
				code=400;
				message="数据库存储失败";
			}
			
		} catch (Exception e) {
			code=401;
			message="出现异常:参数错误或者数据库存储错误";
			logger.error("异常", e);
		}
		json.put("code", code);
		json.put("message", message);
		
		return json;
	}
	
	@Override
	public Map<String, Object> getAttendance(String classId){
		Map<String, Object> json=new HashMap<>();
		int code=600;
		String message="success";
		
		//先查看上期的结账日期,上次结账日期如果为空,说明之前没有生成或账单.这个时候,取课程的开始日期
		LocalDate lastBillDate = dao.getLastBillDate(classId);
		LocalDate startDate; //新账期的开始日期为上次结束日期的下一天
		if (lastBillDate==null) {
			startDate = dao.getStarTimeOfClass(classId);
		}else {
			startDate=lastBillDate.plusDays(1);
		}
		List<Map<String, Object>> data=null;
		try {
			
			LocalDate theLastAttendanceDay = dao.getTheLastAttendanceDay(classId);
			data = dao.getTotalOfAttendanceDay(classId,startDate,theLastAttendanceDay);
			String billPeriod=startDate+"——"+theLastAttendanceDay;
			for (Map<String, Object> map : data) {
				int totalHour=(int)map.get("classhour_eachtime")*(int)map.get("totalTimes");
				map.put("billPeriod", billPeriod); //账期
				map.put("totalHour", totalHour); //总课时,如果课程不按课时算,总课时=0
			}
		} catch (Exception e) {
			logger.error("查询考勤出现异常",e);
		}
		
		json.put("code", code);
		json.put("message", message);
		json.put("data", data);
		return json;
	}
	
	/**
	 * @author fanjunguo
	 * @description 根据学生id,获取此学生的考勤明细
	 * @param studentId
	 * @param classId
	 * @param billPeriod 账单的开始日期和结束日期,需要处理一下,转成LocalDate类型
	 * @return 所有的考勤日期以及出勤情况
	 */
	@Override
	public Map<String, Object> getAttendanceDetail(String studentId,String classId,String billPeriod){
		Map<String, Object> json=new HashMap<String, Object>();
		int code=600;
		String message="success";
		String[] split = billPeriod.split("——");
		if (split.length>0) {
			LocalDate startDate = LocalDate.parse(split[0]);
			LocalDate endDate = LocalDate.parse(split[1]);
			List<Map<String, Object>> attendanceDetail = dao.getAttendanceDetail(studentId, classId, startDate,endDate);
			json.put("data", attendanceDetail);
		} else {
			code=402;
			message="参数错误";
		}
		json.put("code", code);
		json.put("message",message);
		return json;
	}
}

package cn.asxuexi.organization.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.organization.service.AttendanceService;

/**
 * @author fanjunguo
 * @version 2019年3月8日 下午8:55:39
 * @description 机构中心-学生考勤
 */

@Controller
public class AttendanceController {

	@Resource
	AttendanceService service;
	
	/**
	 * @author fanjunguo
	 * @description 获取机构下所有的班级名单
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="attendance/getClassList.action")
	@ResponseBody
	public Map<String, Object> getClassList(int page,int rows){
		Map<String, Object> resultJson = service.getClassList(page, rows);
		return resultJson;
	}
	
	/**
	 * @author fanjunguo
	 * @description 获取班级下所有的学生名单
	 * @param page
	 * @param rows
	 * @param classId 班级id
	 * @return 符合easyUI格式的数据
	 */
	@RequestMapping(value="attendance/getStuents.action")
	@ResponseBody
	public Map<String, Object> getStuents(int page,int rows,String classId){
		return  service.getStuents(page, rows, classId);
		
	}
	
	/**
	 * 
	 * @author fanjunguo
	 * @description 将学生考勤情况,以班级为单位,存到考勤表里
	 * @param attendance
	 * @return
	 */
	@RequestMapping(value="attendance/saveAttendance.action")
	@ResponseBody
	public Map<String, Object> saveAttendance(String attendance,String classId){
		
		Map<String, Object> json = service.saveAttendance(attendance,classId);
		return json;
	}
	
	/**
	 * @author fanjunguo
	 * @description 获取每个班级里面的学生考勤汇总情况
	 * @param classId
	 * @return
	 */
	@RequestMapping("attendance/getAttendance.action")
	@ResponseBody
	public Map<String, Object> getAttendance(String classId){
		Map<String, Object> result = service.getAttendance(classId);
		return result;
	}
	
	/**
	 * 获取每个学生的考勤详情,根据班级id+账单周期
	 *  
	 * @author fanjunguo
	 * @param studentId 学生id(全网唯一)
	 * @param classId 班级id
	 * @param billPeriod String 账单周期
	 * @return
	 */
	@RequestMapping("attendance/getAttendanceDetail.action")
	@ResponseBody
	public Map<String, Object> getAttendanceDetail(String studentId,String classId,String billPeriod){
		Map<String, Object> attendanceDetail = service.getAttendanceDetail(studentId, classId, billPeriod);
		return attendanceDetail;
	}
	
	/**
	 * 获取每个学生的考勤详情,根据账单id
	 * 
	 * 
	 * @author fanjunguo
	 * @param studentId 学生id
	 * @param billId 账单id
	 * @return 
	 */
	@RequestMapping("attendance/getAttendanceDetailByBillId.action")
	@ResponseBody
	public List<Map<String, Object>> getAttendanceDetailByBillId(String studentId,String billId){
		
		return null;
	}
}

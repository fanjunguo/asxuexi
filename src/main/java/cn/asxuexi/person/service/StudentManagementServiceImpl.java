package cn.asxuexi.person.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.asxuexi.person.dao.StudentManagementDao;
import cn.asxuexi.person.entity.StudentEntity;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Token_JWT;


@Service
public class StudentManagementServiceImpl implements StudentManagementService {

	private Logger logger=LoggerFactory.getLogger(this.getClass());

	@Resource
	private StudentManagementDao dao;
	
	@Override
	public Map<String, Object> getAllStudents(){
		Map<String, Object> json=new HashMap<>();
		int code=600;
		String message="success";
		String userId = (String)Token_JWT.verifyToken().get("userId");
		List<Map<String, Object>> allStudents = dao.getAllStudents(userId);
		json.put("data", allStudents);
		json.put("code", code);
		json.put("message", message);
		return json;
	}
	
	
	/**
	 * 增加新的上课人
	 * 
	 * @author fanjunguo
	 * @param student 学生信息实体类
	 * @return json
	 */
	@Override
	public Map<String, Object> insertNewStudent(StudentEntity student){
		Map<String, Object> json=new HashMap<String, Object>();
		int code=600;
		String message="success";
		String userId = (String)Token_JWT.verifyToken().get("userId");
		student.setUserId(userId);
		student.setGmt_created(LocalDateTime.now());
		String studentId = RandomTool.randomId("student");
		student.setId(studentId);
		int result = dao.insertNewStudent(student);
		if (result==0) {
			code=400;
			message="failure";
		}
		json.put("data", studentId);
		json.put("code", code);
		json.put("message", message);
		return json;
	}
	
	/**
	 * 删除上课人
	 * 
	 * @param studentId 上课人id
	 * @return json删除结果
	 */
	@Override
	public JsonData deleteStudent(String studentId) {
		JsonData json = JsonData.error();
		try {
			int result = dao.deleteStudent(studentId);
			if (result>0) {
				json=JsonData.success();
			}
		} catch (Exception e) {
			logger.error("发生异常",e);
			json=JsonData.exception();
			
		}
		return json;
	}

	//修改上课人信息
	@Override
	public JsonData updateStudent(StudentEntity student) {
		JsonData json=null;
		student.setGmt_modefied(LocalDateTime.now());
		int result = dao.updateStudent(student);
		if (result>0) {
			json=JsonData.success();
		} else {
			json=JsonData.error();
		}
		return json;
	}
}

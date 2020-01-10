package cn.asxuexi.person.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.person.entity.StudentEntity;
import cn.asxuexi.person.service.StudentManagementService;
import cn.asxuexi.tool.JsonData;

@Controller
public class StudentManagementController {

	@Resource
	private StudentManagementService service;
	
	//查询所有的上课人信息
	@RequestMapping("studentManagement/getAllStudents.action")
	@ResponseBody
	public Map<String, Object> getAllStudents(){
		Map<String, Object> json = service.getAllStudents();
		return json;
	}
	
	/**
	 * 增加新的上课人
	 * 
	 * @author fanjunguo
	 * @param name 姓名
	 * @param tel 联系电话
	 * @return json
	 */
	@RequestMapping("studentManagement/insertNewStudent.action")
	@ResponseBody
	public Map<String, Object> insertNewStudent(@Validated StudentEntity student,BindingResult bindingResult){
		Map<String, Object> json =new HashMap<>();
		if (bindingResult.hasErrors()) {
			json.put("code", 402);
			json.put("message", "参数错误");
		}else {
			json = service.insertNewStudent(student);
		}
		return json;
	}
	
	/**
	 * 删除上课人
	 * 
	 * @param studentId 上课人id
	 */
	@RequestMapping("studentManagement/deleteStudent.action")
	@ResponseBody
	public JsonData deleteStudent(@RequestParam(required=true)String studentId) {
		JsonData json = service.deleteStudent(studentId);
		return json;
	}
	
	/**
	 * 修改上课人信息
	 * 
	 * @param name 姓名
	 * @param tel 联系电话
	 * @param id 上课人id
	 */
	@RequestMapping("studentManagement/updateStudent.action")
	@ResponseBody
	public JsonData updateStudent(@Validated StudentEntity student,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return JsonData.result(402, "参数错误");
		}
		return service.updateStudent(student);
	}
	
	
}

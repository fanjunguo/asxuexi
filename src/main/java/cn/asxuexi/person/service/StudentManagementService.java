package cn.asxuexi.person.service;

import java.util.Map;

import cn.asxuexi.person.entity.StudentEntity;
import cn.asxuexi.tool.JsonData;

/**
 *
 *
 * @author fanjunguo
 * @version 2019年6月3日 下午2:45:27
 */
public interface StudentManagementService {

	Map<String, Object> getAllStudents();

	/**
	 * 增加新的上课人
	 * 
	 * @author fanjunguo
	 * @param name 姓名
	 * @param tel 联系电话
	 * @return json
	 */
	Map<String, Object> insertNewStudent(StudentEntity student);

	/**
	 * 删除上课人
	 * 
	 * @param studentId 上课人id
	 * @return json删除结果
	 */
	JsonData deleteStudent(String studentId);

	/**
	 * 修改上课人信息
	 * 
	 * @param name 姓名
	 * @param tel 联系电话
	 */
	JsonData updateStudent(StudentEntity student);

}
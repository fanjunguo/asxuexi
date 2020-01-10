package cn.asxuexi.person.dao;

import java.util.List;
import java.util.Map;

import cn.asxuexi.person.entity.StudentEntity;

public interface StudentManagementDao {

	/**
	 * 查询用户已保存的,所有的上课人信息
	 * 
	 * @return
	 */
	 List<Map<String, Object>> getAllStudents(String userId);

	 /**
		 * 增加新的上课人
		 * 
		 * @author fanjunguo
		 * @param student 学生信息实体类
		 * @return int 影响的行数
		 */
	int insertNewStudent(StudentEntity student);
	
	/**
	 * 删除上课人
	 * 
	 * @param studentId 上课人id
	 * @return json删除结果
	 */
	int deleteStudent(String studentId);
	
	/**
	 * 修改上课人信息
	 * 
	 * @param name 姓名
	 * @param tel 联系电话
	 * @return 受影响的行数
	 */
	int updateStudent(StudentEntity student);
}

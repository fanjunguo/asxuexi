package cn.asxuexi.person.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PersonalTimetableDao {
	/**
	 * 获取用户某段时间内的课程安排
	 * 
	 * @param userId
	 *            用户ID
	 * @param begin
	 *            开始日期(yyyy-mm-dd)
	 * @param end
	 *            结束日期(yyyy-mm-dd)
	 * @return
	 */
	List<Map<String, Object>> listPersonalTimetables(@Param("userId") String userId, @Param("begin") String begin,
			@Param("end") String end);

}

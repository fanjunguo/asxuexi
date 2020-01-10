package cn.asxuexi.organization.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.asxuexi.organization.entity.CourseDO;

public interface CourseManagementDao {

	/**
	 * 获取机构课程（不包括已删除课程）
	 * 
	 * @param orgId
	 *            机构Id
	 * @return
	 */
	List<Map<String, Object>> listCourses(@Param("orgId") String orgId);

	/**
	 * 根据课程ID获取机构对应的课程
	 * 
	 * @param courseId
	 *            课程ID
	 * @param userId
	 *            用户ID
	 * @return
	 */
	CourseDO getCourse(@Param("userId") String userId, @Param("courseId") String courseId);

	/**
	 * 获取课程的套餐信息
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listCoursePackages(@Param("courseId") String courseId);

	/**
	 * 获取课程的图片信息
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listImgs(@Param("courseId") String courseId);

	/**
	 * 获取课程的视频信息
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listCourseVideos(@Param("courseId") String courseId);

	/**
	 * 获取机构所有课程的分类
	 * 
	 * @param courseId
	 *            课程ID
	 * 
	 * @return
	 */
	List<Map<String, Object>> listCourseSorts(@Param("courseId") String courseId);

	/**
	 * 获取课程的课程安排信息
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	List<Map<String, Object>> listCourseTimetable(@Param("courseId") String courseId);

	/**
	 * 将课程记录的状态更改为删除（-1）
	 * 
	 * @param list
	 *            课程Id列表
	 * @return
	 */
	int updateCourseStatus(@Param("list") List<String> list);

	/**
	 * 向课程表插入新的记录
	 * 
	 * @param courseId
	 *            课程id
	 * @param basicInfo
	 *            课程基本信息 {courseName:"课程名称",courseSort:"课程分类",chargingMode:"收费方式",
	 *            courseBegin:"开始日期",courseEnd:"结束日期",courseTeacher:"授课老师",
	 *            courseDescription:"课程简介"}
	 * @param orgId
	 *            机构id
	 * @param now
	 *            创建时间
	 * @return
	 */
	int insertCourse(@Param("courseId") String courseId, @Param("basicInfo") Map<String, Object> basicInfo,
			@Param("orgId") String orgId, @Param("now") LocalDateTime now);

	/**
	 * 向课程套餐表插入新的记录
	 * 
	 * @param courseId
	 *            课程ID
	 * @param packageList
	 *            套餐信息列表
	 *            [{packageId:"套餐id",courseLength:"课时数量",packageName:"套餐名称",packagePrice:"套餐价格"}]
	 * @param now
	 *            创建时间
	 * @return
	 */
	int insertCoursePackage(@Param("courseId") String courseId,
			@Param("packageList") List<Map<String, Object>> packageList, @Param("now") LocalDateTime now);

	/**
	 * 向课程图片表插入新的记录
	 * 
	 * @param courseId
	 *            课程ID
	 * @param pictureList
	 *            图片信息列表 [{index:"图片顺序",picture:"图片地址"}]
	 * @param now
	 *            创建时间
	 * @return
	 */
	int insertImg(@Param("courseId") String courseId, @Param("pictureList") List<Map<String, Object>> pictureList,
			@Param("now") LocalDateTime now);

	/**
	 * 向课程视频表插入新的记录
	 * 
	 * @param courseId
	 *            课程ID
	 * @param pictureList
	 *            视频信息列表 [{index:"视频顺序",video:"视频地址"}]
	 * @param now
	 *            创建时间
	 * @return
	 */
	int insertCourseVideo(@Param("courseId") String courseId, @Param("videoList") List<Map<String, Object>> videoList,
			@Param("now") LocalDateTime now);

	/**
	 * 向课程安排表插入新的记录
	 * 
	 * @param courseId
	 *            课程ID
	 * @param courseTimetableList
	 *            课程安排信息数组[{ chapterId: "章节ID", chapterNumber: "章节序号", chapterName:
	 *            "章节名称", chapterDate: "上课日期，(yyyy-mm-dd)字符串", chapterBegin:
	 *            "开始时间，(hh:mm)字符串", chapterEnd: "结束时间，(hh:mm)字符串", chapterLength:
	 *            "课时数"}]
	 * @param now
	 *            创建时间
	 * @return
	 */
	int insertCourseTimetable(@Param("courseId") String courseId,
			@Param("courseTimetableList") List<Map<String, Object>> courseTimetableList,
			@Param("now") LocalDateTime now);

	/**
	 * 修改课程表某条记录的coursename,sort_id,teacher,des字段值
	 * 
	 * @param courseId
	 *            课程id
	 * 
	 * @param basicInfoMap
	 *            修改后的课程基本信息对象， {courseName:"课程名称",courseSort:"课程分类",
	 *            courseTeacher:"授课老师",courseDescription:"课程简介"}
	 * @param now
	 *            记录修改时间
	 * @return
	 */
	int updateCourse(@Param("courseId") String courseId, @Param("basicInfoMap") Map<String, Object> basicInfoMap,
			@Param("now") LocalDateTime now);

	/**
	 * 修改课程套餐表中套餐的状态字段为删除（-1）
	 * 
	 * @param deletedPackageList
	 *            要删除的套餐id数组， ["套餐id",...]
	 * @param now
	 *            更改时间
	 */
	int updatePackageStatus(@Param("deletePackageList") List<String> deletePackageList,
			@Param("now") LocalDateTime now);

	/**
	 * 清除课程图片表中某个课程的所有图片记录
	 * 
	 * @param courseId
	 *            课程id
	 * @return
	 */
	int deleteImg(@Param("courseId") String courseId);

	/**
	 * 清除课程视频表中某个课程的所有视频记录
	 * 
	 * @param courseId
	 * @return
	 */
	int deleteCourseVideo(@Param("courseId") String courseId);

	/**
	 * 修改课程套餐
	 * 
	 * @param courseId
	 *            课程ID
	 * @param updatePackageList
	 *            要修改的套餐列表
	 * @param now
	 *            修改时间
	 * @return
	 */
	int updatePackage(@Param("courseId") String courseId,
			@Param("updatePackageList") List<Map<String, Object>> updatePackageList, @Param("now") LocalDateTime now);

	/**
	 * 修改课程安排信息
	 * 
	 * @param courseId
	 *            课程ID
	 * @param updateCourseTimetableList
	 *            要修改的课程安排列表
	 * @param now
	 *            修改时间
	 * @return
	 */
	int updateCourseTimetable(@Param("courseId") String courseId,
			@Param("updateCourseTimetableList") List<Map<String, Object>> updateCourseTimetableList,
			@Param("now") LocalDateTime now);

	/**
	 * 修改课程安排表中的状态字段为删除（-1）
	 * 
	 * @param deleteCourseTimetableList
	 *            要删除的课程安排id数组， ["课程安排id",...]
	 * @param now
	 *            更改时间
	 */
	int updateCourseTimetableStatus(@Param("deleteCourseTimetableList") List<String> deleteCourseTimetableList,
			@Param("now") LocalDateTime now);

	/**
	 * 新增课程分类的记录
	 * 
	 * @param courseId
	 *            课程ID
	 * @param courseSortArray
	 *            三级分类ID列表
	 * @return
	 */
	int insertCourseSort(@Param("courseId") String courseId, @Param("courseSortArray") List<String> courseSortArray);

	/**
	 * 根据用户ID获取用户的所有机构
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	List<Map<String, Object>> listOrgsByUserId(@Param("userId") String userId);

	/**
	 * 删除课程所属分类
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	int deleteCourseSort(@Param("courseId") String courseId);

}

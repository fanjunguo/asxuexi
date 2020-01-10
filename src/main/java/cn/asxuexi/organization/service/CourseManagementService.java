package cn.asxuexi.organization.service;

import java.util.List;
import java.util.Map;

public interface CourseManagementService {
	/**
	 * 获取机构的课程（不包括已删除课程）
	 * 
	 * @param page
	 *            分页数据，当前页码
	 * @param rows
	 *            分页数据，每页行数
	 * @param orgId
	 *            校区ID
	 * @return
	 */
	Map<String, Object> listCourses(int page, int rows, String orgId);

	/**
	 * 根据课程ID获取机构对应的课程
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	Map<String, Object> getCourse(String courseId);

	/**
	 * 删除机构的课程（并非删除记录，而是将课程记录的状态标识为删除）
	 * 
	 * @param courseIdList
	 *            JSON字符串,课程Id列表
	 * @return
	 */
	Map<String, Object> deleteCourses(String courseIdList);

	/**
	 * 新增课程，保存课程的基本信息，套餐信息，图片和视频信息
	 * 
	 * @param basicInfo
	 *            JSON字符串，课程基本信息对象
	 *            {courseName:"课程名称",courseSort:"课程分类",chargingMode:"收费方式",
	 *            courseBegin:"开始日期",courseEnd:"结束日期",courseTeacher:"授课老师",
	 *            courseDescription:"课程简介"}
	 * @param packageArray
	 *            JSON字符串，套餐信息数组
	 *            [{courseLength:"课时数量",packageName:"套餐名称",packagePrice:"套餐价格"}]
	 * @param pictureArray
	 *            JSON字符串，图片信息数组 [{index:"图片顺序",picture:"图片地址"}]
	 * @param videoArray
	 *            JSON字符串，视频信息数组 [{index:"视频顺序",video:"视频地址"}]
	 * @param courseTimetable
	 *            JSON字符串，课程安排信息数组 [{chapterNumber: "章节序号", chapterName: "章节名称",
	 *            chapterDate: "上课日期，(yyyy-mm-dd)字符串", chapterBegin:
	 *            "开始时间，(hh:mm)字符串", chapterEnd: "结束时间，(hh:mm)字符串", chapterLength:
	 *            "课时数"}]
	 * @param orgArray
	 * 			  JSON字符串，校区ID数组["校区ID"]
	 * @return
	 */
	Map<String, Object> addCourse(String basicInfo, String packageArray, String pictureArray, String videoArray,
			String courseTimetable, String orgArray);

	/**
	 * 修改课程，保存修改后的基本信息，套餐信息，图片视频信息，课程安排信息
	 * 
	 * @param courseId
	 *            课程ID
	 * @param basicInfo
	 *            JSON字符串，修改后的课程基本信息对象， 空对象代表没有修改基本信息
	 *            {courseDescription: "图文描述",courseName: "课程名称",
	 *            courseSort: "课程分类，第三级分类ID",courseTeacher: "授课老师"} 或  {}
	 * @param insertPackageArray
	 *            JSON字符串，待新增的套餐信息的数组，空数组代表没有待新增套餐
	 *            [{courseLength: "课时数量",packageName: "套餐名称",
	 *            packagePrice: "套餐价格"},...] 或 []
	 * @param deletePackageArray
	 *            JSON字符串，待删除的套餐ID的数组，空数组代表没有待删除套餐
	 *            ["套餐ID",...] 或 []
	 * @param updatePackageArray
	 *            JSON字符串，待修改套餐信息的数组，空数组代表没有待修改套餐
	 *            [{courseLength: "课时数量",packageName: "套餐名称",packageId: "套餐ID",
	 *            packagePrice: "套餐价格"},...] 或 []
	 * @param pictureArray
	 *            JSON字符串，修改后的所有图片信息的数组 ，空数组代表删除所有图片
	 *            [{index:"图片顺序",picture:"图片地址"},...] 或 []
	 * @param videoArray
	 *            JSON字符串，修改后的所有视频信息的数组，空数组代表删除所有视频
	 *            [{index:"视频顺序",video:"视频地址"},...] 或 []
	 * @param insertCourseTimetableArray
	 *            JSON字符串，待新增的课程安排的数组，空数组代表没有待新增课程安排
	 *            [{chapterNumber: "章节序号", chapterName: "章节名称",
	 *            chapterDate: "上课日期，(yyyy-mm-dd)字符串", 
	 *            chapterBegin: "开始时间，(hh:mm)字符串", 
	 *            chapterEnd: "结束时间，(hh:mm)字符串", chapterLength: "课时数"},...] 或 []
	 * @param deleteCourseTimetableArray
	 *            JSON字符串，待删除的课程安排ID的数组，空数组代表没有待删除课程安排
	 *            ["课程安排ID",...] 或 []
	 * @param updateCourseTimetableArray
	 *            JSON字符串，待修改课程安排的数组，空数组代表没有待修改课程安排
	 *            [{chapterId: "章节ID", chapterName: "章节名称",
	 *            chapterDate: "上课日期，(yyyy-mm-dd)字符串", 
	 *            chapterBegin: "开始时间，(hh:mm)字符串", 
	 *            chapterEnd: "结束时间，(hh:mm)字符串", chapterLength: "课时数"},...] 或 []
	 * @return
	 */
	Map<String, Object> editCourse(String courseId, String basicInfo, String insertPackageArray,
			String deletePackageArray, String updatePackageArray, String pictureArray, String videoArray,
			String insertCourseTimetableArray, String deleteCourseTimetableArray, String updateCourseTimetableArray);

	/**
	 * 获取当前用户的所有机构
	 * @return [{orgId:"机构ID", orgName:"机构名称"},...]
	 */
	List<Map<String, Object>> listOrgs();

}

package cn.asxuexi.organization.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.organization.service.CourseManagementService;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.JsonData;

@Controller
public class CourseManagementController {
	@Resource
	private CourseManagementService courseManagementService;

	@RequestMapping("courseRelease.do")
	public String courseRelease(String courseType, String courseId, Model model) {
		model.addAttribute("courseType", courseType);
		model.addAttribute("courseId", courseId);
		String orgId = GetOrgIdFromRedis.getOrgId();
		String value = "login/log_in";
		if (orgId != null) {
			value = "org/courseRelease";
		}
		return value;
	}

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
	@ResponseBody
	@RequestMapping("orgCourse/listCourses.action")
	public Map<String, Object> listCourses(int page, int rows, String orgId) {
		Map<String, Object> listCourses = courseManagementService.listCourses(page, rows, orgId);
		return listCourses;
	}

	/**
	 * 根据课程ID获取机构对应的课程
	 * 
	 * @param courseId
	 *            课程ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("orgCourse/getCourse.action")
	public Map<String, Object> getCourse(String courseId) {
		Map<String, Object> getCourse = courseManagementService.getCourse(courseId);
		return getCourse;
	}

	/**
	 * 获取当前用户的所有机构
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("orgCourse/listOrgs.action")
	public JsonData listOrgs() {
		List<Map<String, Object>> data = courseManagementService.listOrgs();
		return JsonData.success(data);
	}

	/**
	 * 删除机构的课程（并非删除记录，而是将课程记录的状态标识为删除）
	 * 
	 * @param courseIdList
	 *            JSON字符串,课程Id列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("orgCourse/deleteCourses.action")
	public Map<String, Object> deleteCourses(String courseIdList) {
		Map<String, Object> result = courseManagementService.deleteCourses(courseIdList);
		return result;
	}

	/**
	 * 新增课程，保存课程的基本信息，套餐信息，图片和视频信息，课程安排信息
	 * 
	 * @param basicInfo
	 *            JSON字符串，课程基本信息对象
	 *            {courseName:"课程名称",courseSort:"JSON字符串，第三级分类ID数组",chargingMode:"收费方式，1或2",
	 *            courseBegin:"开始日期，(yyyy-mm-dd)字符串",courseEnd:"结束日期，(yyyy-mm-dd)字符串",
	 *            courseTeacher:"授课老师",courseDescription:"图文描述"}
	 * @param packageArray
	 *            JSON字符串，套餐信息数组
	 *            [{courseLength:"课时数量",packageName:"套餐名称",packagePrice:"套餐价格"}]
	 * @param pictureArray
	 *            JSON字符串，图片信息数组 [{index:"图片顺序",picture:"图片地址"}]
	 * @param videoArray
	 *            (如果没有视频,传空数组) JSON字符串，视频信息数组 [{index:"视频顺序",video:"视频地址"}]
	 * @param courseTimetable
	 *            JSON字符串，课程安排信息数组 [{chapterNumber: "章节序号", chapterName: "章节名称",
	 *            chapterDate: "上课日期，(yyyy-mm-dd)字符串", chapterBegin:
	 *            "开始时间，(hh:mm)字符串", chapterEnd: "结束时间，(hh:mm)字符串", chapterLength:
	 *            "课时数"}]
	 * @param orgArray
	 *            JSON字符串，校区ID数组["校区ID"]
	 * @return
	 */
	@ResponseBody
	@RequestMapping("orgCourse/addCourse.action")
	public Map<String, Object> addCourse(String basicInfo, String packageArray, String pictureArray, String videoArray,
			String courseTimetable, String orgArray) {
		Map<String, Object> result = courseManagementService.addCourse(basicInfo, packageArray, pictureArray,
				videoArray, courseTimetable, orgArray);
		return result;
	}

	/**
	 * 修改课程，保存修改后的基本信息，套餐信息，图片视频信息，课程安排信息 (参数:array形式的参数,如果不传,默认是[])
	 * 
	 * @param courseId
	 *            课程ID
	 * @param basicInfo
	 *            JSON字符串，修改后的课程基本信息对象， 空对象代表没有修改基本信息 {courseDescription:
	 *            "图文描述",courseName: "课程名称", courseSort:"JSON字符串，第三级分类ID数组",
	 *            courseTeacher: "授课老师"} 或 {}
	 * @param insertPackageArray
	 *            JSON字符串，待新增的套餐信息的数组，空数组代表没有待新增套餐 [{courseLength:
	 *            "课时数量",packageName: "套餐名称", packagePrice: "套餐价格"},...] 或 []
	 * @param deletePackageArray
	 *            JSON字符串，待删除的套餐ID的数组，空数组代表没有待删除套餐 ["套餐ID",...] 或 []
	 * @param updatePackageArray
	 *            JSON字符串，待修改套餐信息的数组，空数组代表没有待修改套餐 [{courseLength:
	 *            "课时数量",packageName: "套餐名称",packageId: "套餐ID", packagePrice:
	 *            "套餐价格"},...] 或 []
	 * @param pictureArray
	 *            JSON字符串，修改后的所有图片信息的数组 ，空数组代表删除所有图片
	 *            [{index:"图片顺序",picture:"图片地址"},...] 或 []
	 * @param videoArray
	 *            JSON字符串，修改后的所有视频信息的数组，空数组代表删除所有视频
	 *            [{index:"视频顺序",video:"视频地址"},...] 或 []
	 * @param insertCourseTimetableArray
	 *            JSON字符串，待新增的课程安排的数组，空数组代表没有待新增课程安排 [{chapterNumber: "章节序号",
	 *            chapterName: "章节名称", chapterDate: "上课日期，(yyyy-mm-dd)字符串",
	 *            chapterBegin: "开始时间，(hh:mm)字符串", chapterEnd: "结束时间，(hh:mm)字符串",
	 *            chapterLength: "课时数"},...] 或 []
	 * @param deleteCourseTimetableArray
	 *            JSON字符串，待删除的课程安排ID的数组，空数组代表没有待删除课程安排 ["课程安排ID",...] 或 []
	 * @param updateCourseTimetableArray
	 *            JSON字符串，待修改课程安排的数组，空数组代表没有待修改课程安排 [{chapterId: "章节ID",
	 *            chapterName: "章节名称", chapterDate: "上课日期，(yyyy-mm-dd)字符串",
	 *            chapterBegin: "开始时间，(hh:mm)字符串", chapterEnd: "结束时间，(hh:mm)字符串",
	 *            chapterLength: "课时数"},...] 或 []
	 * @return
	 */
	@ResponseBody
	@RequestMapping("orgCourse/editCourse.action")
	public Map<String, Object> editCourse(String courseId, String basicInfo,
			@RequestParam(defaultValue = "[]") String insertPackageArray,
			@RequestParam(defaultValue = "[]") String deletePackageArray,
			@RequestParam(defaultValue = "[]") String updatePackageArray,
			@RequestParam(defaultValue = "[]") String pictureArray,
			@RequestParam(defaultValue = "[]") String videoArray,
			@RequestParam(defaultValue = "[]") String insertCourseTimetableArray,
			@RequestParam(defaultValue = "[]") String deleteCourseTimetableArray,
			@RequestParam(defaultValue = "[]") String updateCourseTimetableArray) {
		Map<String, Object> result = courseManagementService.editCourse(courseId, basicInfo, insertPackageArray,
				deletePackageArray, updatePackageArray, pictureArray, videoArray, insertCourseTimetableArray,
				deleteCourseTimetableArray, updateCourseTimetableArray);
		return result;
	}

}

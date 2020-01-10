package cn.asxuexi.mvc;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.service.PersonalCollectionsService;

@Controller
public class PersonalCollectionsController {
	
	
	@Resource
	PersonalCollectionsService ser;
	
	/**
	 * @author fanjunguo
	 * @description 获取收藏的课程数据
	 * @param currentTime
	 */
	@RequestMapping(value="getcourse_collection.action")
	@ResponseBody
	public Map<String, Object> getCourseCollections(String currentTime) {	
		Map<String, Object> course = ser.getCourseCollections(currentTime);
		return course;
	}
	
	//删除收藏课程或者机构,根据参数决定
	@RequestMapping(value="personalCollections/deleteCollection.action")
	@ResponseBody
	public int delete(String courseId,String orgId) {
		int result = ser.deleteCollection(courseId, orgId);
		return result;
	}
	
	@RequestMapping(value="getschool_collection.action")
	@ResponseBody
	public List<List<Map<String, Object>>> getSchoolCollections(String currentTime){
		List<List<Map<String, Object>>> school = ser.getSchoolCollections(currentTime);
		return school;
	}

}

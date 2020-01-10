package cn.asxuexi.android;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.tool.JsonData;

/**
 * @version 1.0
 * @author fanjunguo
 * @editor zhangshun
 * @description 用于安卓端请求全部分类数据
 */
@Controller
public class getAllSortController {
	@Resource
	getClassSortService service;
	
	@RequestMapping(value="asxuexi/android/getSorts.do")
	@ResponseBody
	public List<SortOperateTree> getClassSort() {
		List<SortOperateTree> classSort = service.getClassSort();
		return classSort;
	}
	
	/**
	 * 根据父级分类ID,查询其所有的子类
	 * 
	 * @version 2019.7.22
	 * @author fanjunguo
	 * @param pid 父类id
	 * @return 请求成功返回数据示例:
	 * 	{
		    "code": 600,
		    "data": [
		        {
		            "sortId": "sort_1541918062868",
		            "sortName": "英语类"
		        },
		        {
		            "sortId": "sort_1541917801163",
		            "sortName": "国际游学"
		        },
		        {
		            "sortId": "sort_1541839707518",
		            "sortName": "考研考博"
		        },
		        {
		            "sortId": "sort_1541840599735",
		            "sortName": "出国留学"
		        }
		    ],
		    "message": "success"
		}
		
	 */
	@RequestMapping(value="android/getChildrenSort.do")
	@ResponseBody
	public JsonData getChildrenSort(@RequestParam(required=true)String pid) {
		return service.getChildrenSort(pid);
	}
}

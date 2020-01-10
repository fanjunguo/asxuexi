package cn.asxuexi.android;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.asxuexi.all.dao.NavigationDao;
import cn.asxuexi.tool.JsonData;


@Service
public class getClassSortServiceImpl implements getClassSortService {
	@Resource
	NavigationDao dao;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	
	
	
	@Override
	public List<SortOperateTree> getClassSort() {
		List<SortOperate> classSort = dao.listSorts();
		List<SortOperateTree> sortOperateTree  = TreeSort.getFatherNode(classSort);
		return sortOperateTree;
	}
	
	
	//根据父级分类ID,查询其所有的子类
	@Override
	public JsonData getChildrenSort(String pid) {
		try {
			List<Map<String, Object>> sid= dao.getChildrenSort(pid);
			if (sid==null) {
				return JsonData.illegalParam();
			} else {
				return JsonData.success(sid);
			}
		} catch (Exception e) {
			logger.error("查询子分类出现异常",e);
			return JsonData.exception("查询子分类出现异常");
		}
	}

}

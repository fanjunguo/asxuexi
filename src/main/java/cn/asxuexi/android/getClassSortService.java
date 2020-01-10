package cn.asxuexi.android;

import java.util.List;

import cn.asxuexi.tool.JsonData;



public interface getClassSortService {

	List<SortOperateTree> getClassSort();

	/**
	 * 根据父级分类ID,查询其所有的子类
	 * 
	 * 
	 * @author fanjunguo
	 * @param pid 父类id
	 * @return 返回数据示例:
	 */
	JsonData getChildrenSort(String pid);
	

}

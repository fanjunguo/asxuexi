package cn.asxuexi.android;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 张顺 @作用： 对分类变换成树的操作
 */
public class TreeSort {
	/**
	 * @author 张顺
	 * @作用 获取父级的id，在其中调用子类的id放在父类下
	 */
	public final static List<SortOperateTree> getFatherNode(List<SortOperate> treesList) {
		List<SortOperateTree> sortOperateTree = new ArrayList<SortOperateTree>();
		for (SortOperate sortOperate : treesList) {
			if ("0".equals(sortOperate.getSortParentid())) {
				SortOperateTree sortTree = new SortOperateTree();
				sortTree.setId(sortOperate.getSortId());
				sortTree.setPid(sortOperate.getSortParentid());
				sortTree.setSortGrade(sortOperate.getSortGrade());
				sortTree.setText(sortOperate.getSortName());
				sortTree.setChildren(getChildrenNode(sortOperate.getSortId(), treesList));
				sortOperateTree.add(sortTree);
			}
		}
		return sortOperateTree;
	}
/**
 * @author 张顺
 * @作用 得到父级id，获取子级id
 * */
	public final static List<SortOperateTree> getChildrenNode(String sortParentid,List<SortOperate> treesList) {
		List<SortOperateTree> sortOperateTree = new ArrayList<SortOperateTree>();
		for (SortOperate sortOperate : treesList) {
			if (sortParentid.equals(sortOperate.getSortParentid())) {
				SortOperateTree sortTree = new SortOperateTree();
				sortTree.setId(sortOperate.getSortId());
				sortTree.setPid(sortOperate.getSortParentid());
				sortTree.setSortGrade(sortOperate.getSortGrade());
				sortTree.setText(sortOperate.getSortName());
				sortTree.setChildren(getChildrenNode(sortOperate.getSortId(), treesList));
				sortOperateTree.add(sortTree);
			}
		}
		return sortOperateTree;
		
	}
}

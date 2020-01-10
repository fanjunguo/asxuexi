package cn.asxuexi.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.asxuexi.entity.PersonalMenu;

/**
 * @author 张顺
 * @作用 树节点
 */

public class PersonalMenuSort {
	public List<PersonalMenu> getFatherNode(List<Map<String, Object>> personalMenus) {
		List<PersonalMenu> list = new ArrayList<PersonalMenu>();
		for (Map<String, Object> personalMenu : personalMenus) {
			PersonalMenu personalMenu2 = new PersonalMenu();
			if ("root".equals(personalMenu.get("parent"))) {
				personalMenu2.setId((Integer) personalMenu.get("id"));
				personalMenu2.setName((String) personalMenu.get("name"));
				personalMenu2.setUrl((String) personalMenu.get("url"));
				personalMenu2.setIcon((String) personalMenu.get("icon"));
				personalMenu2.setParent("root");
				personalMenu2.setChildren(getChildrenNode((Integer) personalMenu.get("id"), personalMenus));
				list.add(personalMenu2);
			}
		}
		return list;
	}

	private List<PersonalMenu> getChildrenNode(Integer pId, List<Map<String, Object>> personalMenus) {
		List<PersonalMenu> list = new ArrayList<PersonalMenu>();
		for (Map<String, Object> personalMenu : personalMenus) {
			PersonalMenu personalMenu2 = new PersonalMenu();
			if (Integer.toString(pId).equals(personalMenu.get("parent"))) {
				personalMenu2.setId((Integer) personalMenu.get("id"));
				personalMenu2.setName((String) personalMenu.get("name"));
				personalMenu2.setUrl((String) personalMenu.get("url"));
				personalMenu2.setIcon((String) personalMenu.get("icon"));
				personalMenu2.setParent(Integer.toString(pId));
				personalMenu2.setChildren(getChildrenNode((Integer) personalMenu.get("id"), personalMenus));
				list.add(personalMenu2);
			}
		}
		return list;
	}
}

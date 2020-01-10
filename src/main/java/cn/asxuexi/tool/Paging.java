package cn.asxuexi.tool;

/**
 * 分页工具类
 * 
 * @author bu760
 *
 */
public class Paging {
	/**
	 * 计算数据应该显示多少页
	 * 
	 * @param records
	 *            数据的总条目数
	 * @param rows
	 *            每页要展示的数据条目数
	 * @return 分页的页数
	 */
	public static int totalpages(int records, int rows) {
		int total = 1;// 至少展示一页
		if (records > rows) {
			if (records % rows == 0) {
				total = records / rows;
			} else {
				total = records / rows + 1;
			}
		}
		return total;
	}

	/**
	 * 计算数据应该显示多少页
	 * 
	 * @param records
	 *            数据的总条目数
	 * @param rows
	 *            每页要展示的数据条目数
	 * @return 分页的页数
	 */
	public static int totalpages(String records, int rows) {
		int total = 1;// 至少展示一页
		int recordsInt = Integer.parseInt(records);
		if (recordsInt > rows) {
			if (recordsInt % rows == 0) {
				total = recordsInt / rows;
			} else {
				total = recordsInt / rows + 1;
			}
		}
		return total;
	}

	/**
	 * 计算数据应该显示多少页
	 * 
	 * @param records
	 *            数据的总条目数
	 * @param rows
	 *            每页要展示的数据条目数
	 * @return 分页的页数
	 */
	public static int totalpages(int records, String rows) {
		int total = 1;// 至少展示一页
		int rowsInt = Integer.parseInt(rows);
		if (records > rowsInt) {
			if (records % rowsInt == 0) {
				total = records / rowsInt;
			} else {
				total = records / rowsInt + 1;
			}
		}
		return total;
	}

	/**
	 * 计算数据应该显示多少页
	 * 
	 * @param records
	 *            数据的总条目数
	 * @param rows
	 *            每页要展示的数据条目数
	 * @return 分页的页数
	 */
	public static int totalpages(String records, String rows) {
		int total = 1;// 至少展示一页
		int recordsInt = Integer.parseInt(records);
		int rowsInt = Integer.parseInt(rows);
		if (recordsInt > rowsInt) {
			if (recordsInt % rowsInt == 0) {
				total = recordsInt / rowsInt;
			} else {
				total = recordsInt / rowsInt + 1;
			}
		}
		return total;
	}

	/**
	 * 计算数据应该显示多少页
	 * 
	 * @param records
	 *            数据的总条目数
	 * @param rows
	 *            每页要展示的数据条目数
	 * @return 分页的页数
	 */
	public static long totalpages(long records, String rows) {
		long total = 1;// 至少展示一页
		int rowsInt = Integer.parseInt(rows);
		if (records > rowsInt) {
			if (records % rowsInt == 0) {
				total = records / rowsInt;
			} else {
				total = records / rowsInt + 1;
			}
		}
		return total;
	}
}

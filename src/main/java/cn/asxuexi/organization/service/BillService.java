package cn.asxuexi.organization.service;

import java.util.Map;

/**
 * @author fanjunguo
 * @version 2019年4月1日 下午4:09:47
 * @description 
 */
public interface BillService {

	Map<String, Object> saveBill(String items,String courseId,String classId,String period);


	Map<String, Object> getClassList(int page, int rows);


	/**
	 * @description 查询所有的账期,以及最新的一期账期中,考勤和费用情况.
	 */
	Map<String, Object> getAllBills(String classId);


	/**
	 * @description 查询班级下,单个账期内的账单
	 * @param billId 账期id
	 */
	Map<String, Object> getBill(String billId, String classId, String startDate, String endDate);

}
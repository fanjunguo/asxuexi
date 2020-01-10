package cn.asxuexi.organization.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author fanjunguo
 * @version 2019年4月1日 下午8:00:30
 * @description 
 */
public interface BillDao {

	/**
	 * @description 保存账单信息到bill_period表,包含费用项信息,以及账期开始和结束
	 * @param items
	 * @param courseId
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	void saveBill(@Param("id")String id,@Param("items")String items,@Param("courseId")String courseId,@Param("classId")String classId,@Param("startDate")LocalDate startDate,@Param("endDate")LocalDate endDate,@Param("now")LocalDateTime now);
	
	/**
	 * @description  查询所有的账单
	 */
	public List<Map<String, Object>> getAllBills(String classId);
	
	/**
	 * @description 查询所有的账期
	 */
	List<Map<String, Object>> getAllPeriods(String classId);
	
	/**
	 * @description 获取指定账期内的费用项(items)
	 * @param id 账期的id
	 */
	String getItems(String id);
	
	/**
	 * 机构保存账单时,保存每个学生用户的账单信息
	 * @version 2019.5.16
	 * @param now 创建时间
	 * @param billId 账单id
	 * @param list 包含用户id的list
	 * @param totalOfOncePrice 所有一次性收费的费用的和
	 * @param totalOfUnitPrice 所有按次计费的单价的和
	 * 
	 */
	int insertBillInfoOfUsers(@Param("list")List<Map<String, Object>> list, @Param("billId")String billId, @Param("now")LocalDateTime now,@Param("totalOfUnitPrice") double totalOfUnitPrice,@Param("totalOfOncePrice") double totalOfOncePrice);

}

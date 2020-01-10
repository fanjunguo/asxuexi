package cn.asxuexi.order.entity;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import cn.asxuexi.order.dao.OrderDao;

/**
 * 定时任务类. 定期扫描订单,更改订单状态
 *
 * @author fanjunguo
 * @version 2019年7月16日 下午4:02:57
 */
public class cancelOrderAutomaticlyJob implements org.quartz.Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail().getJobDataMap().get("applicationContext");
		applicationContext.getBean(OrderDao.class).cancelOrderAuto(new DateTime().minusMinutes(30).toDate());
	}

	

}

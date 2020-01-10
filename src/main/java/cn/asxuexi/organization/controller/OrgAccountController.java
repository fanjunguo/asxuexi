package cn.asxuexi.organization.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.organization.entity.CashFlowEntity;
import cn.asxuexi.organization.entity.GetCashOut;
import cn.asxuexi.organization.service.OrgAccountService;
import cn.asxuexi.tool.JsonData;

/**
 * 机构账户管理controller层
 *
 * @author fanjunguo
 * @version 2019年6月20日 下午4:58:22
 */
@Controller
public class OrgAccountController {
	
	@Resource
	private OrgAccountService service;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	/**
	 * 查询机构账户信息,余额、押金等
	 * 
	 * 
	 * @author fanjunguo
	 * @return 示例数据:
	 * {
	 * 	code:600,
	 * 	message:sucess,
	 * 	data:{
	 * 		amount: 2000.00
			deposit: 1000.00
			gmt_created: "2019-06-20"
			gmt_modified: "2019-06-20"
			org_id: "org_1548941562949"
	 * 	}
	 * }
	 */
	@RequestMapping("orgAccount/getAccountInfo.action")
	@ResponseBody
	public JsonData getAccountInfo() {
		return service.getAccountInfo();
	}
	
	/**
	 * 查询所有的资金流信息
	 * 
	 * @version 2019/6/28
	 * @return 数据结构:
	 */
	@RequestMapping("orgAccount/getCashFlowInfo.action")
	@ResponseBody
	public JsonData getCashFlowInfo() {
		return service.getCashFlowInfo();
	}
	
	
	/**
	 * 提现申请
	 * 
	 * @param String amount-提现金额
	 * @param String bankCardNum-银行卡号
	 * @param String ownerName-持卡人姓名
	 * @param String bankName-银行名称
	 * @return
	 */
	@RequestMapping("orgAccount/getCashOut.action")
	@ResponseBody
	public JsonData getCashOut(@Validated(GetCashOut.class) CashFlowEntity cashInfo,BindingResult result) {
		if (result.hasErrors()) {
			return JsonData.illegalParam();
		}else {
			try {
				cashInfo.setFlowDirection(-1);
				cashInfo.setOperation(2);
				return service.getCashOut(cashInfo);
			} catch (Exception e) {
				logger.error("处理提现申请时发生异常",e);
				return JsonData.exception();
			}
		}
	}
}

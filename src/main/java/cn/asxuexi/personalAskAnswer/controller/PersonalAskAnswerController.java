package cn.asxuexi.personalAskAnswer.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.asxuexi.personalAskAnswer.service.PersonalAskAnswerService;

@Controller
public class PersonalAskAnswerController {

	@Resource
	private PersonalAskAnswerService personalAskAnswerService;

	/**
	 * 请求个人对机构或课程的问答
	 * @param rows 每页的数量
	 */
	@ResponseBody
	@RequestMapping("PersonalAskAnswer/listAskAnswer.action")
	public Map<String, Object> listAskAnswer(String table, int page,int rows) {
		Map<String, Object> listAnswer = personalAskAnswerService.listAnswer(table, page,rows);
		return listAnswer;
	}
}

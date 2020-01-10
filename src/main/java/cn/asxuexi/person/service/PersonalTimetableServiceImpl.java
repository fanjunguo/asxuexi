package cn.asxuexi.person.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.asxuexi.person.dao.PersonalTimetableDao;
import cn.asxuexi.tool.DateTimeTool;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.Token_JWT;

@Service
public class PersonalTimetableServiceImpl implements PersonalTimetableService {
	@Resource
	private PersonalTimetableDao personalTimetableDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public JsonData listPersonalTimetables(String begin, String end) {
		String userId = (String) Token_JWT.verifyToken().get("userId");
		JsonData data = JsonData.error();
		try {
			List<Map<String, Object>> listPersonalTimetables = personalTimetableDao.listPersonalTimetables(userId, begin,  end);
			for (Map<String, Object> timetable : listPersonalTimetables) {
				Date chapterDate = (Date) timetable.get("chapter_date");
				String chapterDateStr = DateTimeTool.getDateStr(chapterDate.getTime() / 1000);
				timetable.put("chapter_date", chapterDateStr);
			}
			data = JsonData.success(listPersonalTimetables);
		} catch (Exception e) {
			data = JsonData.exception();
			logger.error("发生异常", e);
		}
		return data;
	}

}

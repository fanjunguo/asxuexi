package cn.asxuexi.activity.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.asxuexi.activity.dao.ActivityDao;
import cn.asxuexi.tool.FileTool;
import cn.asxuexi.tool.JsonData;

/**
 *
 *
 * @author fanjunguo
 * @version 2019年6月15日 上午10:34:50
 */
@Service
public class ActivityServiceImpl implements ActivityService {
	@Resource
	private ActivityDao dao;
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());

	
	private String ad_img="/ad_img"; //存放广告、活动等图片等文件夹

	@Override
	public JsonData getAdInfo(String id) {
		JsonData adInfo = dao.getAdInfo(id);
		adInfo.put("url", "/asxuexi_resource/ad_img/"+id+".png");  //图片文件夹路径
		//获取图片高度和宽度
		File file=new File(FileTool.getRootPath()+FileTool.asxuexi_resource+ad_img,id+".png");
		try {
			BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
			adInfo.put("imgHeight",sourceImg.getHeight() );
			adInfo.put("imgWidth", sourceImg.getWidth());
		} catch (Exception e) {
			return JsonData.exception();
		}
		return JsonData.success(adInfo);
	}

	//提交报名信息
	@Override
	public JsonData submit(String id,String name, String tel) {
		JsonData json=JsonData.success();
		try {
			dao.submit(id,name,tel,LocalDateTime.now());
		} catch (Exception e) {
			logger.error("活动报名发生异常",e);
			json=JsonData.exception();
		}
		return json;
	}

}

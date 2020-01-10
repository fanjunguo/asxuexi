package cn.asxuexi.guardian.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.tool.JsonData;

/**
 * 私人使用,用后作废或删除
 *
 * @author fanjunguo
 * @version 2019年10月11日 下午1:47:45
 */
@RestController
public class MyGuardian {

	@RequestMapping("guardian/uploadVideo.do")
	public JsonData uploadVideo(MultipartFile file,String videoName,HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String folderName="../asxuexi_resource/guardian/";
		File folder=new File(realPath,folderName+videoName);
		try {
			file.transferTo(folder);
		} catch (Exception e) {
			return JsonData.exception("上传视频出现异常:"+e.getLocalizedMessage());
		}
		
		return JsonData.success();
	}
	
}

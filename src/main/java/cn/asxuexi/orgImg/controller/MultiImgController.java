package cn.asxuexi.orgImg.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.orgImg.service.MultiImgService;
import cn.asxuexi.tool.JsonData;

@RestController
public class MultiImgController {
	@Resource
	private MultiImgService multiImgService;

	@RequestMapping("img/addFiles.action")
	public JsonData addFiles(List<MultipartFile> fileList) {
		List<String> addFiles = multiImgService.addFiles(fileList);
		return JsonData.success(addFiles);
	}
}

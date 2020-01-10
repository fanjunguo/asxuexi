package cn.asxuexi.orgImg.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface MultiImgService {
	/**
	 * 保存多张图片
	 * 
	 * @param fileList
	 *            图片列表
	 * @return 保存成功的图片地址列表
	 */
	List<String> addFiles(List<MultipartFile> fileList);

}

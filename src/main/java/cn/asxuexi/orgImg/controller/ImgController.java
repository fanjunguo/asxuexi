package cn.asxuexi.orgImg.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.orgImg.service.ImgSvevice;

@Controller
public class ImgController {

	@Resource
	private ImgSvevice imgSvevice;

	/**
	 * 获取路径下的所有文件和文件夹
	 * 
	 * @param fileDir orgId下的路径
	 * @return map key：size fileList：文件列表
	 */
	@ResponseBody
	@RequestMapping("img/getFileDir.action")
	public Map<String, Object> getFileDir(@RequestParam(required=true)String fileDir) {
		Map<String, Object> file = imgSvevice.getFile(fileDir);
		return file;
	}

	/**
	 * 获取系统图片
	 */
	@ResponseBody
	@RequestMapping("img/getSysFileDir.action")
	public List<String> getSysFileDir() {
		List<String> sysFileDir = imgSvevice.getSysFileDir();
		return sysFileDir;
	}

	/**
	 * 删除文件夹或文件
	 * @param type 1 文件夹 2 文件
	 * @param path 要删除的文件(夹)路径. 
	 */
	@ResponseBody
	@RequestMapping("img/deleteFile.action")
	public Map<String, Object> deleteFile(int type,String path) {
		Map<String, Object> deleteFile = imgSvevice.deleteFile(type,path);
		return deleteFile;
	}

	/**
	 * 增加文件夹
	 * 
	 * @param path
	 *            新建文件夹
	 */
	@ResponseBody
	@RequestMapping("img/addFileDir.action")
	public int addFileDir(String path) {
		int addFileDir = imgSvevice.addFileDir(path);
		return addFileDir;
	}

	/**
	 * 增加文件
	 * 
	 * @param file
	 *            图片视频文件
	 * @param path
	 *            当前路径
	 * @param fileName
	 *            文件名
	 */
	@ResponseBody
	@RequestMapping("img/addFile.action")
	public Map<String, Object> addFile(@RequestParam("file") MultipartFile file, String path, String fileName) {
		Map<String, Object> addFile = imgSvevice.addFile(file, path, fileName);
		return addFile; 
	}
}

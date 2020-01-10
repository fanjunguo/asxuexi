package cn.asxuexi.orgImg.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ImgSvevice {
	/**
	 * 获取路径下的所有文件和文件夹
	 * 
	 * @param fileDir
	 *            orgId下的路径
	 * @return map key：size fileList：文件列表
	 */
	Map<String, Object> getFile(String fileDir);

	/**
	 * 获取系统图片
	 */
	List<String> getSysFileDir();

	/**
	 * 删除文件夹或文件
	 * @param type 1 文件夹 2 文件
	 * @param path
	 *            删除路径
	 */
	Map<String, Object> deleteFile(int type,String path);

	/**
	 * 增加文件夹
	 * 
	 * @param path
	 *            新建文件夹
	 * @return int 0失败 1 成功
	 */
	int addFileDir(String path);

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
	Map<String, Object> addFile(MultipartFile file, String path, String fileName);
}

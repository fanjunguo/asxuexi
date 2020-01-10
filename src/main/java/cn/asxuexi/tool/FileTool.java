package cn.asxuexi.tool;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 * 
 * @author bu760
 *
 */
public class FileTool {
	/**
	 * 在此定义存储静态图片资源的地址,其他类中用到的时候,从此类中调用.统一管理和修改
	 */
	public static final String asxuexi_resource = "../asxuexi_resource";
	private static Logger logger = LoggerFactory.getLogger(FileTool.class);

	/**
	 * 统一获取当前项目所在的服务器路径
	 * 
	 * 
	 * @author fanjunguo
	 * @return 项目路径
	 */
	public static String getRootPath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		return rootPath;
	}

	/**
	 * 保存文件
	 * 
	 * @param fileDirectory
	 *            保存文件的目录
	 * @param newFileName
	 *            文件名
	 * @param file
	 *            文件实体
	 * @return true 保存成功 ;false 保存失败
	 */
	public static boolean saveFile(String fileDirectory, String newFileName, MultipartFile file) {
		File fileDirectories = new File(fileDirectory);
		if (!fileDirectories.exists()) {
			fileDirectories.mkdirs();
		}
		File newFile = new File(fileDirectory + newFileName);
		try {
			file.transferTo(newFile);
		} catch (IllegalStateException | IOException e) {
			logger.error("保存文件发生异常", e);
			return false;
		}
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileDirectory
	 *            文件所在目录
	 * @param fileName
	 *            文件名
	 * @return true 删除成功;false 删除失败
	 */
	public static boolean delFile(String fileDirectory, String fileName) {
		File file = new File(fileDirectory + fileName);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 生成文件的新文件名
	 * 
	 * @param file
	 *            文件对象
	 * @return
	 */
	public static String createNewFileName(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(originalFilename);
		String newFileName = UUID.randomUUID().toString() + "." + extension;
		return newFileName;
	}
}

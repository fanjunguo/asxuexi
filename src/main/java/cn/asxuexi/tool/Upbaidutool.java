package cn.asxuexi.tool;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Upbaidutool {
	private static Logger logger=LoggerFactory.getLogger(Upbaidutool.class);
	
	
	
	/**
	 * 修改文件名称
	 * 
	 * @param file
	 *            {@link MultipartFile} 上传的文件
	 * @param destDir
	 *            {@link String} 文件存放路径
	 * @return {@link String} 新名称
	 */
	public static String uploads(MultipartFile file, String destDir) {
		String photoNewName = newName(file.getOriginalFilename());
		String ss = destDir + File.separator + photoNewName;

		try {
			File f = new File(ss);
			file.transferTo(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			photoNewName="";
		}
		return photoNewName;

	}
	/**
	 * 修改文件名称
	 * 
	 * @param file
	 *            {@link MultipartFile} 上传的文件
	 * @param destDir
	 *            {@link String} 文件存放路径
	 * @return {@link String} 新名称
	 */
	public static String uploads(MultipartFile file, String destDir,String newName) {
		String ss = destDir + File.separator + newName;

		try {
			File f = new File(ss);
			file.transferTo(f);
		} catch (Exception e) {
			logger.error("uploader上传图片失败",e);
			newName="0";
		}
		return newName;

	}
	/**
	 * 编辑新名称
	 * 
	 * @param srcName
	 *            {@link String} 原来名称 a.jpg
	 * @return {@link String} 现在的名称 a20190913152819.jpg
	 */
	public static String newName(String srcName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dt = sdf.format(new Date());
		String rd = Math.round(Math.random() * 900) + 100 + "";
		int wz = srcName.lastIndexOf(".");
		String mz = "";
		String xinnname = "";
		if (wz == -1) {
			xinnname = srcName + "_" + dt + rd;
		} else {
			String ext = srcName.substring(wz);
			mz = srcName.substring(0, wz);
			xinnname = mz + "_" + dt + rd + ext;
		}
		return xinnname;
	}
	
	/**
	 * 
	 * @author fanjunguo
	 * @description 获取文件的后缀名,包含“.”
	 * @param file
	 */
	public static String getFileType(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		String typeName = originalFilename.substring(originalFilename.lastIndexOf("."));
		return typeName;
	}

}

package cn.asxuexi.orgImg.service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import cn.asxuexi.orgImg.dao.ImgDao;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.JsonData;
import cn.asxuexi.tool.RandomTool;
import cn.asxuexi.tool.Upbaidutool;

/**
 * @version 1.0 by 张顺 2018
 * 	实现图片管理器的增删改查功能
 *
 * @version 2.0 by 范俊国 2019年6月10日 下午5:11:31
 * 	1.修复bug:后缀名称忽略大小写
 * 	2.优化文件匹配方法:正则表达式
 * 	3.业务需求变更:需要解析审核未通过的图片:未通过的图片统一在invalid文件夹中,需要将此文件夹中的图片加上特殊标记
 * 
 * @说明: 关于文件类型fileType
 * 	1-文件夹 2-图片 3-视频 4-审核未通过文件
 */
@Service
public class ImgSveviceImpl implements ImgSvevice {

	@Resource
	private ImgDao imgDao;
	@Autowired
	private HttpServletRequest request;

	private String asxuexi_resource = "../asxuexi_resource/";
	private String asxuexiResource = "/asxuexi_resource/";
	private final String FLASH="/";
	//正则表达式(忽略大小写)
	private final String IMG_TYPE="(?i)jpg|png|jpeg|gif";
	private final String VIDEO_TYPE="(?i)mov|mp4|flv";

	
	
	/**
	 * 获取路径下的所有文件和文件夹
	 * 
	 * @param fileDir orgId下的路径
	 * @return map key：size fileList：文件列表
	 */
	public Map<String, Object> getFile(String fileDir) {
		// 返回值
		Map<String, Object> returnvalues = new HashMap<String, Object>(16);
		//分别 存放文件和文件夹的list
		ArrayList<Object> fileList = new ArrayList<Object>(20);
		ArrayList<Object> directoryList = new ArrayList<Object>(10);
		// 获取orgId
		String orgId = GetOrgIdFromRedis.getOrgId();
		// http地址
		String httpPath = asxuexiResource + orgId;
		// 服务器本地地址
		String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + orgId;
		// 获取文件大小
		File file = new File(rootPath);
		double dirSize = getDirSize(file);
		BigDecimal b = new BigDecimal(dirSize);
		double size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		// 如果是根目录
		if (fileDir.equals(" ")) {
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>(16);
				String name = file2.getName();
				boolean directory = file2.isDirectory();
				int fielType = 0;
				hashMap.put("filePath", httpPath + "/" + name);
				// 如果是文件夹
				if (directory) {
					fielType=1;
					hashMap.put("fileType", fielType);
					directoryList.add(hashMap);
				} 
				// 如果是文件(多了一个time字段)
				else {
					int lastIndexOf = name.lastIndexOf(".");
					String type = name.substring(lastIndexOf + 1);
					if (type.matches(IMG_TYPE)) {
						fielType=2;
					} else if (type.matches(VIDEO_TYPE)) {
						fielType=3;
					}
					
					hashMap.put("fileType", fielType);
					hashMap.put("time", file2.lastModified());
					fileList.add(hashMap);
				}
				
			}
		} 
		//如果不是根目录
		else {
			file = new File(rootPath + "/" + fileDir);
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>(16);
				String name = file2.getName();
				long lastModified = file2.lastModified();
				//敏感图片处理
				if ("invalid".equals(name)&& ( "img".equals(fileDir)||"video".equals(fileDir) )) {
					File[] invalidFiles = file2.listFiles();
					for (File eachFile : invalidFiles) {
						hashMap=new HashMap<>(16);
						String name2 = eachFile.getName();
						hashMap.put("filePath", httpPath + FLASH + fileDir + FLASH + name+FLASH+name2);
						hashMap.put("fileType", 4);
						hashMap.put("time", lastModified);
						fileList.add(hashMap);
					}
				} else {
					boolean directory = file2.isDirectory();  //文件对象是否是文件夹
					int fileType = 0;
					hashMap.put("filePath", httpPath + "/" + fileDir + "/" + name);
					if (directory) {// 如果是文件夹
						fileType=1;
						hashMap.put("fileType", fileType);
						directoryList.add(hashMap);
					} else {
						int lastIndexOf = name.lastIndexOf(".");
						String type = name.substring(lastIndexOf + 1);
						if (type.matches(IMG_TYPE)) {
							fileType=2;
						} else if (type.matches(VIDEO_TYPE)) {
							fileType=3;
						}
						hashMap.put("fileType", fileType);
						hashMap.put("time", lastModified);
						fileList.add(hashMap);
					}
				}
			}
		}
		// add by fanjunguo:增加了list的排序,根据fileType和上传时间排序
//		Comparator<? super Object> c = new Comparator<Object>() {
//			@Override
//			public int compare(Object o1, Object o2) {
//				Map<String, Object> map1 = JSONObject.parseObject(JSONObject.toJSONString(o1), new TypeReference<Map<String, Object>>(){});
//				Map<String, Object> map2 = JSONObject.parseObject(JSONObject.toJSONString(o2), new TypeReference<Map<String, Object>>(){});
//				int m1 = (int) map1.get("fileType");
//				int m2 = (int) map2.get("fileType");
//				
//				long t1 = (long) map1.get("time");
//				long t2 = (long) map2.get("time");
//				if (m1 > m2) {
//					return 1;
//				}else if(t1>t2) {
//					return -1;
//				}
//				else {
//					return 0;
//				}
//			}
//		};
//		arrayList.sort(c);
		

		Collections.sort(fileList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Map<String, Object> map1 = JSONObject.parseObject(JSONObject.toJSONString(o1), new TypeReference<Map<String, Object>>(){});
				Map<String, Object> map2 = JSONObject.parseObject(JSONObject.toJSONString(o2), new TypeReference<Map<String, Object>>(){});
				long t1 = (long) map1.get("time");
				long t2 = (long) map2.get("time");
				if (t1>t2) {
					return -1;
				} else if (t1<t2){
					return 1;
				}else {
					return 0;
				}
			}
		});
		directoryList.addAll(fileList);
		
		returnvalues.put("size", size);
		returnvalues.put("fileList", directoryList);
		return returnvalues;
	}

	
	/**
	 * 获取系统图片
	 */
	public List<String> getSysFileDir() {
		// 存放文件和文件夹
		ArrayList<String> arrayList = new ArrayList<String>(10);
		// http地址
		String httpPath = asxuexiResource + "system/";
		// 服务器地址
		String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + "system/";
		File file = new File(rootPath);
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {
			String name = file2.getName();
			int lastIndexOf = name.lastIndexOf(".");
			String type = name.substring(lastIndexOf + 1);
			if (type.matches(IMG_TYPE)) {
				arrayList.add(httpPath + name);
			}
		}
		return arrayList;
	}

	/**
	 * 删除文件夹或文件
	 * 
	 * @param type 1 文件夹 2 文件
	 * @param path http路径删除路径
	 * @return 
	 */
	public Map<String, Object> deleteFile(int type, String path) {
		JsonData json;
		String rootPath = "";
		String orgId = GetOrgIdFromRedis.getOrgId();
		if (1 == type) {
			rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + orgId + "/"
					+ path;
			int imgFile = imgDao.isImgFile(orgId,path);
			if (imgFile > 0) {
				imgDao.deleteImgFIle(orgId,path);
			}
		} else if (2 == type) {
			int indexOf = path.indexOf(orgId);
			String pathOrg = path.substring(indexOf);
			int img = imgDao.isImg(orgId, asxuexiResource + pathOrg);
			if (img > 0) {
				imgDao.deleteImg(orgId, asxuexiResource + pathOrg);
			}
			rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + pathOrg;
		}
		boolean delDir = delDir(rootPath);
		if (delDir) {
			json=JsonData.success();
		} else {
			json=JsonData.error();
		}
		String rootPatha = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + orgId;
		File file = new File(rootPatha);
		BigDecimal b = new BigDecimal(getDirSize(file));
		double size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		json.put("data", size);
		return json;
	}

	/**
	 * 增加文件夹
	 * 
	 * @param path
	 *            新建文件夹
	 * @return int 0失败 1 成功
	 */
	public int addFileDir(String path) {
		int returnvalues = 0;
		String orgId = GetOrgIdFromRedis.getOrgId();
		String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + "/" + orgId
				+ "/" + path;
		File file = new File(rootPath);
		boolean mkdirs = file.mkdirs();
		if (mkdirs) {
			returnvalues = 1;
		}
		return returnvalues;
	}

	/**
	 * 增加文件
	 * 
	 * @param file
	 *            图片视频文件
	 * @param path
	 *            org之后的路径
	 * @param fileName
	 *            文件名
	 * @return map: result表示是否成功-成功,result返回图片路径;失败,result返回“0”
	 */
	public Map<String, Object> addFile(MultipartFile file, String path, String fileName) {
		Map<String, Object> hash = new HashMap<String, Object>(16);
		String returnvalues = "0";
		int insertImg = 0;
		String typeImg[] = { "jpg", "gif", "png" };
		String orgId = GetOrgIdFromRedis.getOrgId();
		String rootPath = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + "/" + orgId
				+ "/" + path;
		File file2 = new File(rootPath);
		String httpPath = asxuexiResource + orgId+FLASH+path;
		String uploads = Upbaidutool.uploads(file, file2.toString(), fileName);
		if (!uploads.equals("0")) {
			returnvalues = httpPath + FLASH + fileName;
			String createFileID = RandomTool.randomId("orgImg");
			LocalDateTime now = LocalDateTime.now();

			int lastIndexOf = fileName.lastIndexOf(".");
			String type = fileName.substring(lastIndexOf + 1);
			boolean contains = Arrays.asList(typeImg).contains(type);
			if (contains) {
				insertImg = imgDao.insertImg(createFileID, asxuexiResource + orgId +FLASH+ path+FLASH+fileName, orgId, now);
			} else {
				insertImg = imgDao.insertVideo(createFileID, asxuexiResource + orgId + FLASH+ path+FLASH+fileName, orgId,now);
			}
			if (insertImg != 1) {
				new File(rootPath +FLASH+ fileName).delete();
			}
		}

		hash.put("result", returnvalues);
		String rootPatha = request.getSession().getServletContext().getRealPath("/") + asxuexi_resource + orgId;
		File file4 = new File(rootPatha);
		BigDecimal b = new BigDecimal(getDirSize(file4));
		double size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		hash.put("size", size);
		return hash;
	}

	/**
	 * 删除文件（递归删除）
	 */
	public static boolean delDir(String path) {
		boolean delete = false;
		File dir = new File(path);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] tmp = dir.listFiles();
				for (int i = 0; i < tmp.length; i++) {
					if (tmp[i].isDirectory()) {
						delDir(path + "/" + tmp[i].getName());
					} else {
						tmp[i].delete();
					}
				}
				boolean delete2 = dir.delete();
				delete = delete2;
			} else {
				delete = dir.delete();
			}
		}
		return delete;
	}

	// 可以存放在工具类中
	/**
	 * 获取文件路径下总大小
	 * 
	 * @param file
	 *            {@link File} 文件路径
	 * @return {@link Double} 文件大小
	 */
	public double getDirSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			return 0.0;
		}
	}
}

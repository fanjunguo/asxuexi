package cn.asxuexi.orgImg.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.asxuexi.orgImg.dao.ImgDao;
import cn.asxuexi.orgImg.entity.OrgImgEntity;
import cn.asxuexi.tool.FileTool;
import cn.asxuexi.tool.GetOrgIdFromRedis;
import cn.asxuexi.tool.RandomTool;

@Service
public class MultiImgServiceImpl implements MultiImgService {
	public static final String ASXUEXI_RESOURCE = "/asxuexi_resource/";
	public static final String REGEX = "^(jpg|jpeg|png|gif)$";
	@Resource
	private ImgDao imgDao;

	@Override
	public List<String> addFiles(List<MultipartFile> fileList) {
		String orgId = GetOrgIdFromRedis.getOrgId();
		String rootPath = FileTool.getRootPath();
		String orgPath = ASXUEXI_RESOURCE + orgId + "/img/";
		String fileDirectory = rootPath + ".." + orgPath;
		List<MultipartFile> imgList = new ArrayList<MultipartFile>();
		List<OrgImgEntity> orgImgList = new ArrayList<OrgImgEntity>();
		List<String> successImgList = new ArrayList<String>();
		for (MultipartFile multipartFile : fileList) {
			String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			if (extension.toLowerCase().matches(REGEX)) {
				// 提取上传文件中的图片文件
				imgList.add(multipartFile);
			}
		}
		for (MultipartFile img : imgList) {
			String newFileName = FileTool.createNewFileName(img);
			// 保存图片文件
			boolean saved = FileTool.saveFile(fileDirectory, newFileName, img);
			if (saved) {
				String imgId = RandomTool.randomId("orgImg");
				String imgName = orgPath + newFileName;
				orgImgList.add(new OrgImgEntity(imgId, imgName, orgId, LocalDateTime.now(), 0));
			}
		}
		// 将保存成功的图片记录插入数据库
		int[] insertImgList = imgDao.insertImgList(orgImgList);
		for (int i = 0; i < insertImgList.length; i++) {
			if (insertImgList[i] == 1) {
				// 成功插入数据库，返回图片地址
				successImgList.add(orgImgList.get(i).getImgName());
			} else {
				// 未成功插入数据库，删除该图片
				FileTool.delFile(rootPath + "..", orgImgList.get(i).getImgName());
			}
		}
		return successImgList;
	}

}

package cn.asxuexi.login.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.asxuexi.entity.UserInfo;

public interface LogInDao {

	Map<String, Object> getIdAndPassword(String tel);

	String getOrgId(String userId); // todo:测试,如果数据库没有对应的数据,会报错还是返回空

	int updatePassword(UserInfo login_as);

	Map<String, Object> getUserId(@Param("tel") String tel, @Param("password") String password);

	int updateClientId(@Param("userId") String userId, @Param("clientId") String clientId);
}
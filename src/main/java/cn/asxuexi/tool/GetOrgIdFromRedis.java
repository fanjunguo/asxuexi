package cn.asxuexi.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author fanjunguo
 * @description 在已知存储规则的前提下,从缓存中提取特定的信息,如orgId
 */
@Component
public class GetOrgIdFromRedis {
	
	private static RedisTool redis;
	
	@Autowired(required=true)
	public void SetRedis(RedisTool redis) {
		GetOrgIdFromRedis.redis=redis;
	}
	
	/**
	 * @author fanjunguo
	 * @description 从缓存中获取orgId
	 * @return String orgId
	 */
	public static String getOrgId() {
		String userId = (String)Token_JWT.verifyToken().get("userId");
		String orgId=(String) redis.getHashValue(userId, "orgId");
		/*如果缓存失效,重新在缓存中存入信息. 
		 * 但是可能会有个问题:在这个地方重新存储的,只能是用户对应的orgid,没有别的信息.如果后期缓存中会存入别的信息,还是会有问题
		 * 之所以现在这么做,想法是只在失效时重新缓存,可以降低对redis对操作频率.但是如果以后要存其他信息,没办法,还是要修改逻辑:每次用户使用,都重置缓存的有效时间*/
		if (orgId==null) {
			Token_JWT.storeOrgIdToRedis(userId);
			orgId=(String)redis.getHashValue(userId, "orgId");
		}
		return orgId;
	}
}

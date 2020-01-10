package cn.asxuexi.tool;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RedisTool {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	

	/**
	 * 判断给定键对应的缓存是否存在
	 * 
	 * @param key
	 *            键,不能为空。若为空时,返回false
	 * @return true 存在,false 不存在
	 */
	public boolean isExisted(String key) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		return redisTemplate.hasKey(key);
	}

	/**
	 * 修改缓存的有效时长,单位为秒
	 * 
	 * @param key
	 *            键,不能为空。若为空时,返回false
	 * @param timeout
	 *            有效时长
	 * @return true 成功,false 失败
	 */
	public boolean setExpirySeconds(String key, Long timeout) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 修改缓存的有效时长,单位为分
	 * 
	 * @param key
	 *            键,不能为空。若为空时,返回false
	 * @param timeout
	 *            有效时长
	 * @return true 成功,false 失败
	 */
	public boolean setExpiryMinutes(String key, Long timeout) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		return redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
	}

	/**
	 * 修改缓存的有效时长,单位为小时
	 * 
	 * @param key
	 *            键,不能为空。若为空时,返回false
	 * @param timeout
	 *            有效时长
	 * @return true 成功,false 失败
	 */
	public boolean setExpiryHours(String key, Long timeout) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		return redisTemplate.expire(key, timeout, TimeUnit.HOURS);
	}

	/**
	 * 修改缓存的有效时长,单位为天
	 * 
	 * @param key
	 *            键,不能为空。若为空时,返回false
	 * @param timeout
	 *            有效时长
	 * @return true 成功,false 失败
	 */
	public boolean setExpiryDays(String key, Long timeout) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		return redisTemplate.expire(key, timeout, TimeUnit.DAYS);
	}

	/**
	 * 修改类型为string的缓存的值
	 * 
	 * @param key
	 *            缓存的键,不能为空。若为空时,返回false;若key不存在时,返回false
	 * @param value
	 *            缓存的值
	 * @return true 成功,false 失败
	 */
	public boolean setString(String key, String value) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		if (redisTemplate.hasKey(key)) {
			try {
				redisTemplate.opsForValue().set(key, value);
				return true;
			} catch (Exception e) {
				logger.error("发生异常。 {}",e);
				return false;
			}
		} else {
			logger.warn("key不存在");
			return false;
		}

	}

	/**
	 * 增加类型为string的缓存
	 * 
	 * @param key
	 *            缓存的键,不能为空。若为空时,返回false;若key存在时返回false
	 * @param value
	 *            缓存的值
	 * @param timeout
	 *            缓存的有效时长,为负数时,代表无限时长
	 * @param unit
	 *            (枚举类型TimeUnit)有效时长的单位.
	 * @return true 成功,false 失败
	 */
	public boolean addString(String key, String value, Long timeout, TimeUnit unit) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		if (redisTemplate.hasKey(key)) {
			logger.warn("key重复");
			return false;
		}
		try {
			if (timeout < 0) {
				timeout = -1L;
			}
			redisTemplate.opsForValue().set(key, value, timeout, unit);
			return true;
		} catch (Exception e) {
			logger.error("发生异常。 {}", e);
			return false;
		}
	}

	/**
	 * 修改类型为hash的缓存的值
	 * 
	 * @param key
	 *            缓存的键,不能为空。若为空时,返回false
	 * @param map
	 *            缓存的值。Map对象，当map的键存在时，覆盖原来的值；当map的键不存在时，创建该键并赋值。
	 * @return true 成功,false 失败
	 */
	public boolean setHash(String key, Map<? extends Object, ? extends Object> map) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		if (redisTemplate.hasKey(key)) {
			try {
				redisTemplate.opsForHash().putAll(key, map);
				return true;
			} catch (Exception e) {
				logger.error("发生异常。 {}",e);
				return false;
			}
		} else {
			logger.warn("key不存在");
			return false;
		}

	}

	/**
	 * 增加类型为hash的缓存
	 * 
	 * @param key
	 *            缓存的键,不能为空。若为空时,返回false;若key存在时,返回false
	 * @param map
	 *            缓存的值
	 * @param timeout
	 *            缓存的有效时长,为负数时,代表无限时长
	 * @param unit
	 *            有效时长的单位
	 * @return true 成功,false 失败
	 */
	public boolean addHash(String key, Map<? extends Object, ? extends Object> map, Long timeout, TimeUnit unit) {
		if (key == null) {
			logger.warn("key为空");
			return false;
		}
		if (redisTemplate.hasKey(key)) {
			logger.warn("key重复");
			return false;
		}
		try {
			if (timeout < 0) {
				timeout = -1L;
			}
			redisTemplate.opsForHash().putAll(key, map);
			redisTemplate.expire(key, timeout, unit);
			return true;
		} catch (Exception e) {
			logger.error("发生异常。 {}",e);
			return false;
		}
	}

	/**
	 * 获取键对应的缓存的类型
	 * 
	 * @param key
	 *            键,不能为空。为空时,返回DataType.NONE
	 * @return 缓存的类型,不存在时为DataType.NONE
	 */
	public DataType getType(String key) {
		if (key == null) {
			logger.warn("key为空");
			return DataType.NONE;
		}
		DataType type = redisTemplate.type(key);
		return type;
	}

	/**
	 * 获取类型为string的缓存的值
	 * 
	 * @param key
	 *            缓存的键,不能为空。若为空时,返回null;若键不存在时，返回null
	 * @return 缓存的值。若键对应的缓存存在,但类型不为string,则返回null
	 */
	public String getString(String key) {
		if (key == null) {
			logger.warn("key为空");
			return null;
		}
		try {
			Object value = redisTemplate.opsForValue().get(key);
			return (String) value;
		} catch (Exception e) {
			logger.error("发生异常。 {}",e);
			return null;
		}
	}

	/**
	 * 获取类型为hash的缓存
	 * 
	 * @param key
	 *            缓存的键,不能为空。若为空时,返回null;若键不存在时，返回null
	 * @return 缓存的值。若键对应的缓存存在,但类型不为hash,则返回null
	 */
	public Map<Object, Object> getHash(String key) {
		if (key == null) {
			logger.warn("key为空");
			return null;
		}
		if (redisTemplate.hasKey(key)) {
			try {
				Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
				return entries;
			} catch (Exception e) {
				logger.error("发生异常。 {}",e);
				return null;
			}
		} else {
			logger.warn("key不存在");
			return null;
		}

	}

	/**
	 * 获取类型为hash的缓存中指定键的值
	 * 
	 * @param key
	 *            缓存的键,不能为空。若为空时,返回null;若键不存在时，返回null
	 * @param hashKey
	 *            hash缓存对象的键
	 * @return 缓存的值。若键对应的缓存存在,但类型不为hash,则返回null
	 */
	public Object getHashValue(String key, Object hashKey) {
		if (key == null) {
			logger.warn("key为空");
			return null;
		}
		if (redisTemplate.hasKey(key)) {
			try {
				Object value = redisTemplate.opsForHash().get(key, hashKey);
				return value;
			} catch (Exception e) {
				logger.error("发生异常。 {}",e);
				return null;
			}
		} else {
			logger.warn("key不存在");
			return null;
		}

	}
	
	/**
	 * @author fanjunguo
	 * @description 根据key,清除缓存中的数据 
	 * @param key 不能为空
	 */
	public void delRecord(String key) {
		if (key!=null) {
			redisTemplate.delete(key);
		}
	}
	
	/**
	 * @author fanjunguo
	 * @description 用于注销登录:删除缓存中的token数据
	 */
	public void delToken() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String token = request.getHeader("access-token");
		//(兼容pc)如果请求头中获取不到token,从cookie中获取
		if (null==token) {
			Cookie[] cookies = request.getCookies();
			if(cookies != null && cookies.length>0) {
				for (Cookie cookie : cookies) {
					if ("access-token".equals(cookie.getName())) {
						token=cookie.getValue();
					}
				}
			}
		}
		if (token!=null) {
			redisTemplate.delete("token:"+token);
		}
	}

}

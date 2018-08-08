package com.ysc.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysc.cache.JedisUtil;
import com.ysc.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {
	
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Override
	public void removeFromCache(String keyPrefix) {
		Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
		for (String key : keySet) {
			jedisKeys.del(key);
		}

	}

}

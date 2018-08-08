package com.ysc.o2o.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ysc.o2o.dto.AwardExecution;
import com.ysc.o2o.entity.Award;

public interface AwardService {
	/**
	 * 根据传入的条件分页返回奖品列表，并返回该查询条件下的总数
	 * @param awardCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize);
	
	/**
	 * 根据awardId查询奖品信息
	 * @param awardId
	 * @return
	 */
	Award getAwardById(long awardId);
	
	AwardExecution addAward(Award award, CommonsMultipartFile thumbnail);
	
	/**
	 * 根据传入的奖品实例修改对应的奖品信息，若传入的图片则替换掉原先的图片
	 * @param award
	 * @param thumbnail
	 * @return
	 */
	AwardExecution modifyAward(Award award, CommonsMultipartFile thumbnail);
}

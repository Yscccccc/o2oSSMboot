package com.ysc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ysc.o2o.dao.AwardDao;
import com.ysc.o2o.dto.AwardExecution;
import com.ysc.o2o.entity.Award;
import com.ysc.o2o.enums.AwardStateEnum;
import com.ysc.o2o.exceptions.AwardOperationException;
import com.ysc.o2o.service.AwardService;
import com.ysc.o2o.util.ImageUtil;
import com.ysc.o2o.util.PageCalculator;
import com.ysc.o2o.util.PathUtil;

@Service
public class AwardServiceImpl implements AwardService{
	@Autowired
	private AwardDao awardDao;
	@Override
	public AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize) {
		//页转行
		int rowIndex = PageCalculator.calculaterRowIndex(pageIndex, pageSize);
		//根据查询条件分页取出奖品列表信息
		List<Award> awardList = awardDao.queryAwardList(awardCondition, rowIndex, pageSize);
		int count = awardDao.queryAwardCount(awardCondition);
		AwardExecution ae = new AwardExecution();
		ae.setAwardList(awardList);
		ae.setCount(count);
		return ae;
	}

	@Override
	public Award getAwardById(long awardId) {
		// TODO Auto-generated method stub
		return awardDao.queryAwardByAwardId(awardId);
	}

	@Override
	@Transactional
	public AwardExecution addAward(Award award, CommonsMultipartFile thumbnail) {
		if (award != null && award.getShopId() != null) {
			award.setCreateTime(new Date());
			award.setLastEditTime(new Date());
			award.setEnableStatus(1);
			if (thumbnail != null) {
				addThumbnail(award, thumbnail);
			}
			try {
				int effectedNum = awardDao.insertAward(award);
				if (effectedNum <= 0) {
					throw new RuntimeException("创建商品失败");
				}
			} catch (Exception e) {
				throw new RuntimeException("创建商品失败:" + e.toString());
			}
			return new AwardExecution(AwardStateEnum.SUCCESS, award);
		} else {
			return new AwardExecution(AwardStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	//若缩略图参数有值，则处理缩略图
	//若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给award
	//更新tb-award
	public AwardExecution modifyAward(Award award, CommonsMultipartFile thumbnail) {
		//空值判断
		if (award != null && award.getAwardId() !=null) {
			award.setLastEditTime(new Date());
			if (thumbnail != null) {
				//通过awardId取出对应的信息
				Award tempAward = awardDao.queryAwardByAwardId(award.getAwardId());
				//如果传输过程中存在图片流，则删除原有图片
				if (tempAward.getAwardImg() != null) {
					ImageUtil.deleteFileOrPath(tempAward.getAwardImg());
				}
				//存储图片流。获取相对路径
				addThumbnail(award,thumbnail);
			}
			try {
				//根据传入的实体类修改相应的信息
				int effectedNum = awardDao.updateAward(award);
				if (effectedNum <= 0) {
					throw new AwardOperationException("更新商品信息失败");
				}
				return new AwardExecution(AwardStateEnum.SUCCESS, award);
			} catch (Exception e) {
				throw new AwardOperationException("更新商品信息失败： " + e.toString());
			}
		} else {
			return new AwardExecution(AwardStateEnum.EMPTY);
		}
	}

	private void addThumbnail(Award award, CommonsMultipartFile thumbnail) {
		String dest = PathUtil.getShopImagePath(award.getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		award.setAwardImg(thumbnailAddr);
	}
	
}

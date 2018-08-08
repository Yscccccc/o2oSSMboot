$(function() {
	var userAwardId = getQueryString('userAwardId');
	//获取奖品信息的URL
	var awardUrl = '/o2o/frontend/getawardbyuserawardid?userAwardId='
			+ userAwardId;

	$.getJSON(awardUrl,function(data) {
		if (data.success) {
			//获取奖品信息
			var product = data.product;
			$('#award-img').attr('src', award.awardImg);
			$('#product-time').text(
					new Date(data.userAwardMap.createTime)
							.Format("yyyy-MM-dd"));
			//奖品名称
			$('#award-name').text(award.awardName);
			//奖品简介
			$('award-desc').text(award.awardDesc);
			var imgListHtml = '';
			//若为去实体店兑换实体奖品，生成兑换礼品的二维码供商家扫描
			if (data.usedStatus == 0){
				imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4award?userAwardId='
						+ userAwardId + '" width="100%" /></div>';
				$('#imgList').html(imgListHtml);
			}	
		}
	});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
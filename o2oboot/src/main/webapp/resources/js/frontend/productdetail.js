$(function() {
	var productId = getQueryString('productId');
	//获取商品信息的URL
	var productUrl = '/o2oboot/frontend/listproductdetailpageinfo?productId='
			+ productId;

	$.getJSON(productUrl,function(data) {
		if (data.success) {
			//获取商品信息
			var product = data.product;
			//给商品信息相关HTML控件赋值
			//商品缩略图
			$('#product-img').attr('src', getContextPath() + product.imgAddr);
			//商品更新时间
			$('#product-time').text(
					new Date(product.lastEditTime)
							.Format("yyyy-MM-dd"));
			if (product.point != undefined){
				$('#product-point').text('购买可得' + product.point + '积分');
			}
			//商品名称
			$('#product-name').text(product.productName);
			//商品简介
			$('#product-desc').text(product.productDesc);
			//商品价格展示逻辑，主要判断原件现价是否为空，所有都为空则不显示价格
			if (product.normalPrice != undefined && product.promotionPrice != undefined){
				//如果现价和原价都不为空则都显示，并且给原价加个删除符号
				$('#price').show();
				$('#normalPrice').html('<del>' + '￥' + product.normalPrice + '</del>');
				$('#promotionPrice').text('￥' + product.promotionPrice);
			}
			var imgListHtml = '';
			//遍历商品详情图列表，并生成批量img标签
			product.productImgList.map(function(item, index) {
				imgListHtml += '<div> <img src="'
						+ getContextPath() + item.imgAddr + '" width="100%" /></div>';
			});
			// 生成购买商品的二维码供商家扫描
//			imgListHtml += '<div> <img src="/o2oboot/frontend/generateqrcode4product?productId='
//					+ product.productId + '"/></div>';
			$('#imgList').html(imgListHtml);
		}
	});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});

$(function(){
	var loading = false;
	//分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 999;
	//一页返回的最大条数
	var pageSize = 10;
	//获取店铺列表的URL
	var listUrl = '/o2o/frontend/listusershopmapsbycustomer';
	//获取店铺类别列表以及区域列表的URL
	//页码
	var pageNum = 1;
	var shopName = '';
	//预先加载10条
	addItems(pageSize, pageNum);

	function addItems(pageSize, pageIndex) {
		//拼接出查询的URL,赋空值默认就去掉这个条件的限制，有值就代表按这个条件去
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&shopName=' + shopName;
		//设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		//访问后台获取相应查询条件的店铺列表
		$.getJSON(url, function(data) {
			if (data.success) {
				//获取当前查询条件下总数
				maxItems = data.count;
				var html = '';
				//遍历店铺列表，拼接出卡片集合
				data.userShopList.map(function(item, index) {
					html += '' + '<div class="card" data-shop-id="'
							+ item.shop.shopId + '">' + '<div class="card-header">'
							+ item.shop.shopName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">本店积分' + item.point
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '</p>' + '</div>'
							+ '</div>';
				});
				//将卡片集合添加到目标html组件里
				$('.list-div').append(html);
				//获取目前为止已经显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				//若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) {
					// 加载完毕，则注销无限加载事件，以防不必要的加载
					//$.detachInfiniteScroll($('.infinite-scroll'));
					// 删除加载提示符
					//$('.infinite-scroll-preloader').remove();
					//隐藏提示符
					$('.infinite-scroll-preloader').hide();
				} else {
					$('.infinite-scroll-preloader').show();
				}
				//否则页码加1,继续load出新的店铺
				pageNum += 1;
				//加载结束，可以再次加载了
				loading = false;
				//刷新页面，显示新加载的店铺
				$.refreshScroller();
			}
		});
	}
	
	//下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function(){
		if (loading) return;
		addItem(pageSize, pageNum);
	});
	
	
	$('#search').on('change', function(e) {
		shopName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	
	
	//点击打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});

	$.init();
})
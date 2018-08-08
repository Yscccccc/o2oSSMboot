$(function(){
	var loading = false;
	var maxItem = 999;
	var pageSize = 10;
	//获取奖品列表的URL
	var listUrl = '/o2o/frontend/listawardsbyshop';
	//兑换奖品的URL
	var exchangeUrl = '/o2o/frontend/adduserawardmap';
	var pageNum = 1;
	//从地址栏URL获取shopId
	var shopId = getQuertString("shopId");
	var awardName = '';
	var canProceed = false;
	var totalPoint = 0;
	//预先加载20条
	addItems(pageSize, pageNum);
	//按照查询条件获取奖品列表，并生成对应的HTML元素添加到页面中
	function addItems(pageSize,pageIndex){
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
			+ pageSize + '&shopId=' + shopId + '&awardName=' + awardName;
		loading = true;
		$.getJSON(url, function(data){
			if (data.success){
				html += '' + '<div class="card" data-award-id="'
					+ item.awardId + '" data-point="' + item.point
					+ '">' + '<div class="card-header">'
					+ item.awardName + '<span calss="pull-right">需要积分'
					+ item.point + '</span></div>'
					+ '<div class="card-contend">'
					+ '<div class="list-block media-list">' + '<ul>'
					+ '<li class="item-content">'
					+ '<div class="item-media">' + '<img src="'
					+ item.awardImg
					+ '" width="44"' + '</div>'
					+ '<div class="item-inner">'
					+ '<div class="item-subtitle">' + item.awardDesc
					+ '</div>' + '</div>' + '</li>' + '</ul>'
					+ '</div>' + '</div>' + '<div class="card-footer">'
					+ '<p class="cool-gray">'
					+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
					+ '更新</p>';
				if (data.totalPoint != undefined){
					//若用户有积分则显示领取按钮
					html += '<span>点击领取</span></div></div>'
				} else {
					html += '</div></div>'
				}
			}
		});
		$('.list-div').append(html);
		if (data.totalPoint != undefined){
			//若用户在该店铺有积分，则显示
			canProceed = true;
			$("#title").text("当前积分" + data.totalPoint);
			totalPoint = data.totalPoint;
		}
		var total = $('.list-div .card').length;
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
	
	$(document).on('infinite', '.infinite-scroll-bottom', function(){
		if (loading) return;
		addItem(pageSize, pageNum);
	});
	
	$(".award-list").on("click", ".card", function(e){
		//若用户在该店铺有积分并且积分数大于该奖品需要消耗的积分
		if (canProceed && totalPoint > e.current.dataset.point){
			//则弹出操作确认框
			$.confirm('需要消耗' + e.current.dataset.point + '积分，确定操作吗', function(){
				//访问后台，领取奖品
				$.ajax({
					url :exchangeUrl,
					type: "POST",
					data : {
						awardId : e.current.dataset.awardId
					},
					dataType: 'json',
					success: function (data){
						if (data.success) {
							$.toast("操作成功");
							totalPoint = totalPoint - e.current.dataset.point;
							$("#title").text("当前积分" + totalPoint);
	
						}else {
							$.toast("操作失败")
						}
					}
				});
			});
		} else {
			$.toast("积分不足或无权限操作")
		}
	});
	
	//需要查询的店铺名字发生变化后，
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
});
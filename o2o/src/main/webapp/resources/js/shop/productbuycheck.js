$(function(){
	var productName = '';
	getList();
	getProductSellDailyList();
	function getList(){
		//获取用户购买信息的URL
		var listUrl = '/o2o/shopadmin/listuserproductmapsbyshop?pageIndex=1&pageSize=9999&productName=' + productName;
		//访问后台，获取该店铺的购买信息列表
		$.getJSON(listUrl, function(data){
			if (data.success){
				var userProductMapList = data.userProductMapList;
				var tempHtml = '';
				//遍历购买信息列表，拼接出列信息
				userProductMapList.map(function(item, index){
					tempHtml += '' + '<div class="row row-productbuycheck">'
						+ '<div class="col-10">' + item.product.productName
						+ '</div>'
						+ '<div class="col-40 productbuycheck-time">'
						+ new Date(item.createTime).Format("yyyy-MM-dd hh:mm:ss")
						+ '</div>' + '<div class="col-20">'
						+ item.user.name + '</div>'
						+ '<div class="col-10">' + item.point + '</div>'
						+ '<div class="col-20">' + item.operator.name + '</div>'
						+ '</div>';
				});
				$('.productbuycheck-wrap').html(tempHtml);
			}
		});
	}
	/**
	 * 获取7天的销量
	 */
	function getProductSellDailyList(){
		//获取该店铺7天销量的URL
		var listProductSellDailyUrl = '/o2o/shopadmin/listproductselldailyinfobyshop';
		//访问后台，该店铺商品7天销量的URL
		$.getJSON(listProductSellDailyUrl, function(data){
			if (data.success){
				var myChat = echarts.init(document.getElementById('chart'));
				//生成静态的Echat信息部分
				var option = generateStaticEchartPart();
				//遍历销量统计列表，动态设定echarts的值
				option.legend.data = data.legendData;
				option.xAxis = data.xAxis;
				option.series = data.series;
				myChat.setOption(option);
			}
		})
	}
	
	$('#search').on('change', function(e){
		//当在搜索框里输入信息的时候
		//依据输入的商品名模糊查询该商品的购买记录
		productName= e.target.value;
		//清空商品购买记录列表
		$(".productbutcheck-wrap").empty();
		//再次加载
		getList(0);
	});
	
	function generateStaticEchartPart(){
		var option = {
				//提示框，鼠标悬浮交互时的信息提示
					tooltip: {
						trigger: 'axis',
						axisPointer: {//坐标轴提示器，坐标轴触发有效
							type: 'shadow' //鼠标移动到轴的时候，显示阴影
						}
					},
					//图例，每个图表最多仅有一个图例
					legend: {
						//图例内容数组，数组项通常为{String},每一项代表一个系列的name
						//data: ['111','222','333']
					},
					//直角坐标系内绘图网格
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					//直角坐标系中横轴数组，数组中每一项代表一条横坐标轴
					xAxis: [{
						//类目型：需要指定类目列表，坐标轴内有且仅有这些指定类目坐标
						//type: 'category',
						//data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
					}],
					//直角坐标系中纵轴数组，数组中每一项代表一条纵轴坐标轴
					yAxis: [{
						type: 'value'
					}],
					//驱动图表生成的是数据内容数组，数组中每一项为一个系列的选项及数据
					series: [
//						{
//						name: '111',
//						type: 'bar',
//						data: [120, 132, 101, 134, 190, 230, 220]
//					},{
//						name: '222',
//						type: 'bar',
//						data: [60, 72, 71, 74, 190, 130, 110]
//					},{
//						name: '333',
//						type: 'bar',
//						data: [62, 82, 91, 84, 109, 110, 120]
//					}
					]
			};
		return option;
	}
});
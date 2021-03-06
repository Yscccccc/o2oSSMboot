$(function() {
	var awardId = getQueryString('awardId');
	var infoUrl = '/o2o/shopadmin/getawardbyid?awardId=' + awardId;
	var awardPostUrl = '/o2o/shopadmin/modifyaward';
	var isEdit = false;
	if (awardId) {
		getInfo(awardId);
		isEdit = true;
	} else {
		awardPostUrl = '/o2o/shopadmin/addaward';
	}


	function getInfo(id) {
		$.getJSON(infoUrl, function(data) {
			if (data.success) {
				var award = data.award;
				$('#award-name').val(award.awardName);
				$('#priority').val(award.priority);
				$('#award-desc').val(award.awardDesc);
				$('#point').val(award.point);
			}
		});
	}

	$('#submit').click(function() {
		var award = {};
		award.awardName = $('#award-name').val();
		award.priority = $('#priority').val();
		award.awardDesc = $('#award-desc').val();
		award.point = $('#point').val();
		award.awardId = awardId ? awardId : '';
		var thumbnail = $('#small-img')[0].files[0];
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		formData.append('awardStr', JSON.stringify(award));
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : awardPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					$('#captcha_img').click();
				} else {
					$.toast('提交失败！');
					$('#captcha_img').click();
				}
			}
		});
	});

});
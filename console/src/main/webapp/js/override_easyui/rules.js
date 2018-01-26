$.extend($.fn.validatebox.defaults.rules, {
	minLength : {
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : '请至少输入{0}字符'
	},
	middleLength : {
		validator : function(value, param) {
			return value.trim().length >= param[0]
					&& value.trim().length <= param[1];
		},
		message : '请输入大于{0}小于{1}个字符'
	},
	maxLength : {
		validator : function(value, param) {
			return  value.trim().length < param[0];
		},
		message : '最多输入{0}字符'
	},
	equalLength : {
		validator : function(value, param) {
			return value.length == param[0];
		},
		message : '请输入{0}位数字'
	},
	equalLengthValidate : {
		validator : function(value, param) {
			return value.length == param[0];
		},
		message : '请输入{0}位验证码'
	},
	checkMobile : {
		validator : function(value, param) {
			var partten = /^1[3,4,5,6,7,8,9]\d{9}$/;
	          var fl = false;
	          if(partten.test(value) && value.length == 11) {
	        	  fl = true;
	          } 
			return fl;
		},
		message : '请输入11位数字手机号码'
	},
	checkEuqals : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '两次输入密码不一致'
	},
	forbbinChinese : {
		validator : function(value, param) {
			var flag = value.trim().length >= param[0] && value.trim().length <= param[1];
			var reg = /[\u4E00-\u9FA5]/i;
		      if(!reg.test(value) && flag){
		    	  return true;
	    	  } 
		      return false;
		},
		message : '请输入大于{0}小于{1}个无中文字符'
	},
	checkImage : {
		validator : function(value, param) {
			var index = value.lastIndexOf(".");
			if(index == -1) {
				return false;
			}
			var perfix = value.substring(index+1, value.length).toLowerCase();
			if(perfix != "jpeg" && perfix != "jpg" && perfix != "png" && perfix != "bmp") {
				  return false; 
			} else {
				return true;
			}
		},
		message : "只能选择jpeg,jpg,png,bmp文件"
	}
});
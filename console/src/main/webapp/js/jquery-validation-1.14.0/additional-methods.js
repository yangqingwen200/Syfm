/*!
 * jQuery Validation Plugin v1.14.0
 *
 * http://jqueryvalidation.org/
 *
 * Copyright (c) 2015 Jörn Zaefferer
 * Released under the MIT license
 */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		define( ["jquery", "./jquery.validate"], factory );
	} else {
		factory( jQuery );
	}
}(function( $ ) {

$.validator.addMethod(
	"amtCheck",	function(value, element){
		return value && /^\d*\.?\d{0,2}$/.test(value);
	},
	"金额必须大于0且小数位数不超过2位"
);

//自定义方法，完成手机号码的验证
//name:自定义方法的名称，method：函数体, message:错误消息
$.validator.addMethod("phone", function(value, element, param){
	//方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
	//alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);
	return new RegExp(/^1[34578]\d{9}$/).test(value);

}, "格式不正确");

}));
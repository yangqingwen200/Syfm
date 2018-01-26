<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.4.4/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/themes/grid.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts-more.js"></script>

<script type="text/javascript">
    var chart;
    var chart1;
	var dataName;
	var datanNumber;
	var xlogintime;
	var loginpub;
	
	$(function(){
    	$.ajax({
 		   url: "${ctxajax}/getUserLoginPublish.do",
 		   async: false,
 		   cache: false,						   
 		   dataType: "json",
 		   success: function(data){
 			  dataName = data.names;
 			  datanNumber = data.datas;
 			 xlogintime = data.xlogintime;
 			loginpub = data.loginpub;
 		   }
 		});
        chart = new Highcharts.Chart({
            chart: {
                renderTo: "container",
                type: "column"
            },
            title: {
                text: "运维人员登录总次数柱行图"
            },
            subtitle: {
                text: "运维人员登录总数分布情况"
            },
            xAxis: {
            	categories: dataName,
            	 title: {
                    text: "登录人名称"
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: "次数"
                }
            },
            legend: {
                layout: "vertical",
                backgroundColor: "#FFFFFF",
                align: "left",
                verticalAlign: "top",
                x: 100,
                y: 70,
                floating: true,
                shadow: true
            },
            tooltip: {
                formatter: function() {
                    return this.x +": "+ this.y + "次";
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: datanNumber
        });

        $("#container1").highcharts({
            title: {
                text: "运维人员每天登录总次数折线图",
                x: -20 //center
            },
            subtitle: {
                text: "运维人员每天登录分布情况",
                x: -20
            },
            xAxis: {
                categories: xlogintime
            },
            yAxis: {
                title: {
                    text: "次数"
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: "#808080"
                }]
            },
            tooltip: {
                valueSuffix: "次"
            },
            legend: {
                layout: "vertical",
                align: "right",
                verticalAlign: "middle",
                borderWidth: 0
            },
            series: loginpub
        });
	});
        
</script>

<div id="container"
	style="width: 100%; height: 50%; margin: 0 auto"></div>
<div id="container1"
	style="width: 100%; height: 50%; margin: 0 auto"></div>
			

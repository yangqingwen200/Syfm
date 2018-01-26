<%@ page language="java" contentType="text/html; charset=utf-8" %>

<form class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-2 control-label">驾校名称</label>
        <div class="col-sm-10">
            <h5>${dds.name}</h5>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">联系电话</label>
        <div class="col-sm-10">
            <h5>${dds.linkTel}</h5>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">地址</label>
        <div class="col-sm-10">
            <h5>${dds.address}</h5>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">介绍</label>
        <div class="col-sm-10">
            <h5>${dds.introduction}</h5>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">经纬度</label>
        <div class="col-sm-10">
            <h5>${dds.lng}, ${dds.lat}</h5>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">LOGO</label>
        <div class="col-sm-10">
            <h5><img src="${dds.logo}" width="300px" height="150px"></h5>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">评星等级</label>
        <div class="col-sm-10">
            <h5>${dds.star_num}</h5>
        </div>
    </div>
</form>


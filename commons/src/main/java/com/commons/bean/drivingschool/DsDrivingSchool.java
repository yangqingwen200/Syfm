package com.commons.bean.drivingschool;

import javax.persistence.*;
import java.util.Date;

/**
 * author Yang
 * version v1.0
 * date 2017/3/22
 */
@Entity
@Table(name = "ds_driving_school")
public class DsDrivingSchool {
    private Long id;
    private String name;
    private String logo;
    private String provinceCode;
    private String cityCode;
    private String cityName;
    private Double starNum;
    private String introduction;
    private String tel;
    private String linkMan;
    private String linkTel;
    private String address;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;
    private String img7;
    private String img8;
    private String img9;
    private String img10;
    private Integer status;
    private Integer disabled;
    private Date createDate;
    private Double lng;
    private Double lat;
    private String areaCode;
    private String sellerId;
    private String sellerKey;
    private Integer isExamroom;

    @Id
    @Column(name = "id")
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "logo")
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Basic
    @Column(name = "province_code")
    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Basic
    @Column(name = "city_code")
    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Basic
    @Column(name = "city_name")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Basic
    @Column(name = "star_num")
    public Double getStarNum() {
        return starNum;
    }

    public void setStarNum(Double starNum) {
        this.starNum = starNum;
    }

    @Basic
    @Column(name = "introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Basic
    @Column(name = "tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Basic
    @Column(name = "link_man")
    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    @Basic
    @Column(name = "link_tel")
    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "img1")
    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    @Basic
    @Column(name = "img2")
    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    @Basic
    @Column(name = "img3")
    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    @Basic
    @Column(name = "img4")
    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    @Basic
    @Column(name = "img5")
    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    @Basic
    @Column(name = "img6")
    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    @Basic
    @Column(name = "img7")
    public String getImg7() {
        return img7;
    }

    public void setImg7(String img7) {
        this.img7 = img7;
    }

    @Basic
    @Column(name = "img8")
    public String getImg8() {
        return img8;
    }

    public void setImg8(String img8) {
        this.img8 = img8;
    }

    @Basic
    @Column(name = "img9")
    public String getImg9() {
        return img9;
    }

    public void setImg9(String img9) {
        this.img9 = img9;
    }

    @Basic
    @Column(name = "img10")
    public String getImg10() {
        return img10;
    }

    public void setImg10(String img10) {
        this.img10 = img10;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "disabled")
    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    @Basic
    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "lng")
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Basic
    @Column(name = "lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "area_code")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Basic
    @Column(name = "seller_id")
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Basic
    @Column(name = "seller_key")
    public String getSellerKey() {
        return sellerKey;
    }

    public void setSellerKey(String sellerKey) {
        this.sellerKey = sellerKey;
    }

    @Basic
    @Column(name = "is_examroom")
    public Integer getIsExamroom() {
        return isExamroom;
    }

    public void setIsExamroom(Integer isExamroom) {
        this.isExamroom = isExamroom;
    }

}

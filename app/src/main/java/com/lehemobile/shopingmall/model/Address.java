package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * 收货地址
 * Created by  on 22/7/16.
 */
public class Address implements Serializable {
    /**
     * "address_id": "1", //地址ID
     * "address_name": "", //地址名称
     * "consignee": "吴系挂", //收件人
     * "email": "",  //收件人邮箱
     * "country": 1 , //国家ID
     * "country_name": '中国' , //国家名称
     * "province": "13",  //省份ID
     * "province": "湖南省",  //省份名称
     * "city": "39",  //城市ID
     * "city_name": "长沙",  //城市名称
     * "district": '1129', //街道（县）ID
     * "district_name":'雨花区'，//街道名称
     * "address":"详细地址" //详细地址
     * "zipcode":"411400", //邮编
     * "tel":"13739076383", //电话
     * "mobile": "", //手机
     */

    private int id;
    private String name;
    private String mobile;

    private Region province;
    private Region city;
    private Region district;
    private String detailedAddress;

    private String zipCode;
    private String tel;
    private String email;

    private boolean isDefault = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Region getProvince() {
        return province;
    }

    public void setProvince(Region province) {
        this.province = province;
    }

    public Region getCity() {
        return city;
    }

    public void setCity(Region city) {
        this.city = city;
    }

    public Region getDistrict() {
        return district;
    }

    public void setDistrict(Region district) {
        this.district = district;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return province.getName() +
                city.getName() +
                district.getName();
    }

    public String getFullAddress() {
        return getRegion() + getDetailedAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        return id == address.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}

package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * 收货地址
 * Created by  on 22/7/16.
 */
public class Address implements Serializable {
    private int id;
    private String name;
    private String mobile;

    private Region province;
    private Region city;
    private Region district;
    private String detailedAddress;

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

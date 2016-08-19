package com.lehemobile.shopingmall.api;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lehemobile.shopingmall.api.base.ApiUtils;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.Address;
import com.lehemobile.shopingmall.model.Region;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanyq on 14/8/16.
 */
public class AddressApi {

    public static int LEVEL_PROVINCE = 1;
    public static int LEVEL_CITY = 2;
    public static int LEVEL_DISTRICT = 3;
    private static ApiUtils.ParseJsonObject<Address> parserAddress = new ApiUtils.ParseJsonObject<Address>() {
        @Override
        public Address parse(JSONObject jobj) throws Exception {
            Address address = new Address();
            address.setId(jobj.optInt("address_id"));
            address.setName(jobj.optString("consignee"));
            String tel = jobj.optString("tel");
            address.setTel(tel);
            String mobile = jobj.optString("mobile");
            mobile = TextUtils.isEmpty(tel) ? mobile : tel;
            address.setMobile(mobile);
            address.setZipCode(jobj.optString("zipcode"));
            address.setEmail(jobj.optString("email"));

            address.setDetailedAddress(jobj.optString("address"));

            Region province = new Region();
            province.setName(jobj.optString("province_name"));
            province.setId(jobj.optInt("province"));
            address.setProvince(province);

            Region city = new Region();
            city.setName(jobj.optString("city_name"));
            city.setId(jobj.optInt("city"));
            address.setCity(city);

            Region district = new Region();
            district.setName(jobj.optString("district_name"));
            district.setId(jobj.optInt("district"));
            address.setDistrict(district);
            //// TODO: 18/8/16  是否默认
            address.setDefault(false);
            return address;
        }
    };

    /**
     * @param parent
     * @param level         1:省，2：市，3：地区
     * @param listener
     * @param errorListener
     * @return
     */
    public static Request<List<Region>> getRegion(int parent, int level, Response.Listener<List<Region>> listener, AppErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("parent", String.valueOf(parent));
        params.put("level", String.valueOf(level));

        return new BaseRequest<List<Region>>("getarea", params, listener, errorListener) {
            @Override
            protected List<Region> treatResponse(JSONObject baseJson) throws Exception {
                JSONArray result = baseJson.optJSONArray("result");
                List<Region> regions = new ArrayList<>();
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jsonObject = result.getJSONObject(i);
                    Region region = new Region();
                    region.setId(jsonObject.optInt("region_id"));
                    region.setName(jsonObject.optString("region_name"));
                    regions.add(region);
                }
                return regions;
            }
        };
    }

    public static Request<List<Address>> getAddressList(int page, int pageNumber, Response.Listener<List<Address>> listener, AppErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(page));
        params.put("p_number", String.valueOf(pageNumber));

        return new BaseRequest<List<Address>>("useraddr", params, listener, errorListener) {
            @Override
            protected List<Address> treatResponse(JSONObject baseJson) throws Exception {
                JSONArray result = baseJson.optJSONArray("result");
                ArrayList<Address> addresses = new ArrayList<>();
                if (result == null) return addresses;
                addresses = ApiUtils.parseJsonArray(result, parserAddress);
                return addresses;
            }
        };
    }

    public static Request<Void> deleteAddress(int addressId, Response.Listener<Void> listener, AppErrorListener errorListener) {
        Map<String, String> params = ApiUtils.quickParams("id", String.valueOf(addressId));
        //// TODO: 18/8/16 删除收货地址
        return new BaseRequest<Void>("deleteaddr", params, listener, errorListener) {
            @Override
            protected Void treatResponse(JSONObject baseJson) throws Exception {
                return null;
            }
        };
    }

    public static Request<Address> saveAddress(final Address address, Response.Listener<Address> listener, AppErrorListener errorListener) {
        String operation = "saveaddr";
        Map<String, String> params = new HashMap<>();
        params.put("consignee", address.getName());
        params.put("mobile", address.getMobile());

        params.put("province", String.valueOf(address.getProvince().getId()));
        params.put("province_name", address.getProvince().getName());

        params.put("city", String.valueOf(address.getCity().getId()));
        params.put("city_name", address.getCity().getName());

        params.put("district", String.valueOf(address.getDistrict().getId()));
        params.put("district_name", address.getDistrict().getName());
        params.put("address", address.getDetailedAddress());
        if (address.getId() > 0) {
            params.put("address_id", String.valueOf(address.getId()));
            operation = "updateaddr"; //更新
        }

        return new BaseRequest<Address>(operation, params, listener, errorListener) {
            @Override
            protected Address treatResponse(JSONObject baseJson) throws Exception {
                //// TODO: 18/8/16 新增地址需要返回ID
                JSONObject jsonObject = baseJson.optJSONObject("result");
                int id = jsonObject.optInt("address_id");
                if (id > 0) {
                    address.setId(id);
                }
                return address;
            }
        };
    }

    /**
     * @param addressId
     * @param isDefault
     * @param listener
     * @param errorListener
     * @return
     */
    public static BaseRequest<Void> setDefault(int addressId, boolean isDefault, Response.Listener<Void> listener, AppErrorListener errorListener) {
        //// TODO: 19/8/16 设置默认收货地址
        Map<String, String> params = ApiUtils.quickParams("address_id", String.valueOf(addressId), "isdefault", String.valueOf(isDefault ? 1 : 0));
        return new BaseRequest<Void>("setdefault", params, listener, errorListener) {
            @Override
            protected Void treatResponse(JSONObject baseJson) throws Exception {
                return null;
            }
        };
    }

}

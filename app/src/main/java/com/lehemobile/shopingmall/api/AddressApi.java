package com.lehemobile.shopingmall.api;

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
            //// TODO: 18/8/16 解析收货地址
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

        return new BaseRequest<List<Address>>("getAddress", params, listener, errorListener) {
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
}

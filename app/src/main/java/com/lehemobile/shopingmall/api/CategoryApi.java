package com.lehemobile.shopingmall.api;

import com.android.volley.Response;
import com.lehemobile.shopingmall.api.base.ApiUtils;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tanyq on 21/8/16.
 */
public class CategoryApi {

    public static BaseRequest<List<Category>> getCategories(int categoryId, Response.Listener<List<Category>> listener, AppErrorListener errorListener) {
        Map<String, String> params = ApiUtils.quickParams("catid", String.valueOf(categoryId));
        return new BaseRequest<List<Category>>("category", params, listener, errorListener) {
            @Override
            protected List<Category> treatResponse(JSONObject baseJson) throws Exception {
                List<Category> categories = new ArrayList<>();
                JSONArray result = baseJson.optJSONArray("result");
                if (result == null) return categories;
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jsonObject = result.getJSONObject(i);
                    Category category = new Category();
                    category.setCategoryId(jsonObject.optInt("cat_id"));
                    category.setCategoryName(jsonObject.optString("cat_name"));
                    categories.add(category);
                }
                return categories;
            }
        };
    }
}

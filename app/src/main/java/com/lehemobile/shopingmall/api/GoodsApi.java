package com.lehemobile.shopingmall.api;

import com.android.volley.Response;
import com.lehemobile.shopingmall.api.base.ApiUtils;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.config.IPConfig;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.session.GoodsDetailSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tanyq on 23/8/16.
 */
public class GoodsApi {

    /**
     *
     */

    private static ApiUtils.ParseJsonObject<Goods> parseGoods = new ApiUtils.ParseJsonObject<Goods>() {

        @Override
        public Goods parse(JSONObject jobj) throws Exception {
            Goods goods = new Goods();
            goods.setId(jobj.optInt("goods_id"));
            goods.setName(jobj.optString("goods_name"));
            goods.setThumbnail(IPConfig.getFullImageUrl(jobj.optString("goods_thumb")));

            goods.setPrice(jobj.optDouble("shop_price",0));
            goods.setMarketPrice(jobj.optDouble("market_price",0));

            goods.setTradingCount(jobj.optInt("sales_count"));
            goods.setStock(jobj.optInt("stock"));

            ArrayList<String> images = new ArrayList<>();
            images.add(IPConfig.getFullImageUrl(jobj.optString("goods_img")));
            goods.setImages(images);

            goods.setDetailImages(images);
            return goods;
        }
    };

    public static BaseRequest<List<Goods>> getGoodsList(int categoryId, int p, int pageSize, Response.Listener<List<Goods>> listener, AppErrorListener errorListener) {
        Map<String, String> params = ApiUtils.quickParams("cat_id", String.valueOf(categoryId));
        return new BaseRequest<List<Goods>>("goods", params, listener, errorListener) {
            @Override
            protected List<Goods> treatResponse(JSONObject baseJson) throws Exception {
                List<Goods> goodsList = new ArrayList<>();
                JSONArray result = baseJson.optJSONArray("result");
                if (result == null) return goodsList;
                goodsList = ApiUtils.parseJsonArray(result, parseGoods);
                return goodsList;
            }
        };
    }

    public static BaseRequest<GoodsDetailSession> getGoodsDetail(int goodsId, Response.Listener<GoodsDetailSession> listener, AppErrorListener errorListener) {
        Map<String, String> params = ApiUtils.quickParams("goods_id", String.valueOf(goodsId));
        return new BaseRequest<GoodsDetailSession>("goods", params, listener, errorListener) {
            @Override
            protected GoodsDetailSession treatResponse(JSONObject baseJson) throws Exception {
                GoodsDetailSession goodsDetailSession = new GoodsDetailSession();
                JSONObject jsonObject = baseJson.optJSONObject("result");
                Goods goods = parseGoods.parse(jsonObject);
                goodsDetailSession.setGoods(goods);
                return goodsDetailSession;
            }
        };
    }
}

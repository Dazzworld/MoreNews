package com.dazzcoder.morenews.parser;

import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.model.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻列表解析类
 */
public class NewsListJsonParser implements JsonParser {

	ArrayList<News> newslist;
	String ID;

	public NewsListJsonParser(String ID) {
		super();
		this.ID = ID;
		newslist = new ArrayList<>();
	}

	@Override
	public List<?> parseJson(String json) {
		// TODO Auto-generated method stub
		newslist.clear();
		try {
			JSONArray jsonArray1 = new JSONObject(json).getJSONArray(ID);

			for (int i = 0; i < jsonArray1.length(); i++) {

				JSONObject jsonObj1 = jsonArray1.getJSONObject(i);
				if (!jsonObj1.has("hasHead")) {
					News news = new News();
					List<String> img = new ArrayList<>();
					news.setTitle(jsonObj1.getString("title"));
					img.add(jsonObj1.getString("imgsrc"));
					if (jsonObj1.has("digest")) {
						news.setDec(jsonObj1.getString("digest"));
					}
					if (jsonObj1.has("imgextra")) {
						JSONArray jsonArray2 = jsonObj1.getJSONArray("imgextra");
						for (int j = 0; j < jsonArray2.length(); j++) {
							JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
							img.add(jsonObject2.getString("imgsrc"));
						}
					}

					news.setPhotosetID(
							Utils.getPhotoID(jsonObj1.has("photosetID") ? jsonObj1.getString("photosetID") : null));
					news.setDocid(jsonObj1.getString("docid"));
					news.setImgurl(img);
					newslist.add(news);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newslist;
	}

}

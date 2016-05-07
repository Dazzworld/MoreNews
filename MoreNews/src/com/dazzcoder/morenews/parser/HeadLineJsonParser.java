package com.dazzcoder.morenews.parser;

import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.model.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 头条解析类
 */
public class HeadLineJsonParser implements JsonParser {

	ArrayList<News> headline;
	String ID;

	public HeadLineJsonParser(String ID) {
		super();
		// TODO Auto-generated constructor stub
		this.ID = ID;
		this.headline = new ArrayList<>();
	}

	@Override
	public List<?> parseJson(String json) {
		// TODO Auto-generated method stub
		headline.clear();
		try {
			JSONArray jsonArray1 = new JSONObject(json).getJSONArray(ID);

			for (int i = 0; i < jsonArray1.length(); i++) {

				JSONObject jsonObj1 = jsonArray1.getJSONObject(i);
				if (jsonObj1.has("hasHead")) {
					News news = new News();
					news.setTitle(jsonObj1.getString("title"));
					news.setDocid(jsonObj1.getString("docid"));
					news.setImageUrl(jsonObj1.getString("imgsrc"));
					news.setPhotosetID(
							Utils.getPhotoID(jsonObj1.has("photosetID") ? jsonObj1.getString("photosetID") : null));
					headline.add(news);
					if (jsonObj1.has("ads")) {
						JSONArray jsonArray2 = jsonObj1.getJSONArray("ads");
						for (int j = 0; j < jsonArray2.length(); j++) {
							JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
							News headnews = new News();
							String a = jsonObject2.getString("url");
							headnews.setTitle(jsonObject2.getString("title"));
							headnews.setPhotosetID(Utils.getPhotoID(Utils.isPhotoSet(a) ? a : null));
							headnews.setDocid(a);
							headnews.setImageUrl(jsonObject2.getString("imgsrc"));
							headline.add(headnews);
						}
					}
					break;
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return headline;
	}

}

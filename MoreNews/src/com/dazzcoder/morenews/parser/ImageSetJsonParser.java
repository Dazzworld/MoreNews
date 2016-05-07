package com.dazzcoder.morenews.parser;

import com.dazzcoder.morenews.bean.ImageNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 图集解析类
 */
public class ImageSetJsonParser implements JsonParser {

	ImageNews imageSet;

	public ImageSetJsonParser() {
		super();
		// TODO Auto-generated constructor stub
		imageSet = new ImageNews();
	}

	@Override
	public Object parseJson(String json) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(json);
			int allsum = Integer.valueOf(jsonObject.getString("imgsum"));
			JSONArray jsonArray = jsonObject.getJSONArray("photos");
			if (jsonObject.has("boardid")) {
				imageSet.setBoardid(jsonObject.getString("boardid"));
			}
			imageSet.setAllnum(allsum);
			imageSet.setPostid(jsonObject.getString("postid"));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<>();
				map.put("content", jsonObject2.getString("imgtitle") + jsonObject2.getString("note"));
				map.put("url", jsonObject2.getString("imgurl"));
				String key = i == 0 ? jsonObject.getString("setname") : jsonObject2.getString("imgtitle");
				map.put("title", key);
				imageSet.addImage(map);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageSet;
	}

}

package com.tolstoff.orthodoxyicons;

import android.content.Context;

public class YandexApiHelper {

	public static YandexApiHelper instance = new YandexApiHelper();
	private final String urlAlbumCollection = "http://api-fotki.yandex.ru/api/users/liblion/album/39474/photos/";

	FotkiConnectionAndÑaching fotkiConnectionAndÑaching;
	private Context context;

	private YandexApiHelper() {

	}

	public static YandexApiHelper getInstance() {

		return instance;

	}

	public void connectToGetCash() {
		fotkiConnectionAndÑaching = new FotkiConnectionAndÑaching(context);
		fotkiConnectionAndÑaching.execute(urlAlbumCollection);
	}

	public void setContex(Context context) {
		this.context = context;
	}
	


}

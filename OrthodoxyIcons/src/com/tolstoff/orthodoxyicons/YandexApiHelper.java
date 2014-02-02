package com.tolstoff.orthodoxyicons;

import android.content.Context;

public class YandexApiHelper {

	public static YandexApiHelper instance = new YandexApiHelper();
	private final String urlAlbumCollection = "http://api-fotki.yandex.ru/api/users/liblion/album/39474/photos/";

	FotkiConnectionAnd�aching fotkiConnectionAnd�aching;
	private Context context;

	private YandexApiHelper() {

	}

	public static YandexApiHelper getInstance() {

		return instance;

	}

	public void connectToGetCash() {
		fotkiConnectionAnd�aching = new FotkiConnectionAnd�aching(context);
		fotkiConnectionAnd�aching.execute(urlAlbumCollection);
	}

	public void setContex(Context context) {
		this.context = context;
	}
	


}

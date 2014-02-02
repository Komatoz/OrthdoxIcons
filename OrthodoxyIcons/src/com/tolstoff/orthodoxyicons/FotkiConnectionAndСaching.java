package com.tolstoff.orthodoxyicons;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class FotkiConnectionAndСaching extends AsyncTask<String, Void, Void> {

	private String titleImage = null, listImageUrls = null,
			galleryImageUrls = null, detailImageUrls = null;

	private ApiProcessicngListener listener;

	private String nextPageUrl = "";
	private int thisPage = 1;

	private Context context;

	DBcache mDBcache;

	public FotkiConnectionAndСaching(Context context) {
		this.context = context;

	}

	@Override
	protected Void doInBackground(String... url) {
		 mDBcache = new DBcache(context);
		mDBcache.open();
		mDBcache.clear();
		try {

			connectForUrlImages(url[0]);

			// LogT.log("previewImageURL: " + previewImageURL.size());
		} catch (IOException e) {

		}

		mDBcache.close();
		LogT.backupDB(context, "OrthodoxIconsDB");

		return null;
	}

	private void connectForUrlImages(String urlAlbumCollection)
			throws IOException {
		titleImage = null; 
		listImageUrls = null; 
		galleryImageUrls = null; 
		detailImageUrls = null;

		InputStream inputStream = null;

		try {
			URL url = new URL(urlAlbumCollection);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(100000);
			connection.setConnectTimeout(100000);
			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);

			int responseCode = connection.getResponseCode();

			LogT.log(HttpURLConnection.HTTP_OK);

			if (responseCode == HttpURLConnection.HTTP_OK) {

				inputStream = connection.getInputStream();

				xmlParser(inputStream);

			} else {
				LogT.log("Нет сединения");
			}

			connection.disconnect();
		} catch (MalformedURLException e) {
			LogT.log("MalformedURLException");
		} catch (IOException e) {
			LogT.log("IOExaption");
		}

		finally {
			if (inputStream != null) {
				inputStream.close();
			}

		}

	}

	// Выполнение парсера
	private void xmlParser(InputStream inputStream) {

		String tagName = "";
		nextPageUrl = "";
		XmlPullParser xpp;

		try {
			xpp = prepareXmlParser(inputStream);

			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {

				if (xpp.getEventType() == XmlPullParser.START_TAG) {
					tagName = xpp.getName();

					// Парсим названия картинок (третий уровень)
					if (tagName.equals("title")) {
						if (xpp.getDepth() == 3) {
							titleImage = xpp.nextText();
							// LogT.log(titleImage.get(titleImage.size()-1));
						}

					}

					// Ссылка на следующую страницу
					if (tagName.equals("link")
							&& xpp.getDepth() == 2
							&& xpp.getAttributeValue(null, "rel")
									.equalsIgnoreCase("next")) {
						nextPageUrl = xpp.getAttributeValue(null, "href");
						LogT.log(xpp.getAttributeValue(null, "href"));
					}

					// Парсим ссылки на превью картинок
					if (tagName.equalsIgnoreCase("img")) {

						if (xpp.getAttributeValue(null, "size")
								.equalsIgnoreCase("XS")) {
							listImageUrls = xpp.getAttributeValue(null, "href");

						}

						if (xpp.getAttributeValue(null, "size")
								.equalsIgnoreCase("M")) {
							galleryImageUrls = xpp.getAttributeValue(null,
									"href");

						}
						if (xpp.getAttributeValue(null, "size")
								.equalsIgnoreCase("orig")) {
							detailImageUrls = xpp.getAttributeValue(null,
									"href");

						}

					}

				}

				if (titleImage != null && listImageUrls != null
						&& galleryImageUrls != null && detailImageUrls != null) {
					// LogT.log(titleImage + " " + listImageUrls + " " +
					// galleryImageUrls + " " + detailImageUrls);

					mDBcache.WriteResult(titleImage, listImageUrls,
							galleryImageUrls, detailImageUrls);

					titleImage = null;
					listImageUrls = null;
					galleryImageUrls = null;
					detailImageUrls = null;

				}

				xpp.next();

			}
     
			LogT.log("END_DOCUMENT");

			// Переход на следующую странциу ссылок (всего 6 страниц)
			if (nextPageUrl != "" && thisPage != 6) {
				thisPage++;
				connectForUrlImages(nextPageUrl);
			}

		}

		catch (IOException e) {
			LogT.log("IOException");
		}

		catch (XmlPullParserException e) {
			LogT.log("XmlPullParserException");
		}

	}

	// Подготовка парсера
	private XmlPullParser prepareXmlParser(InputStream is)
			throws XmlPullParserException {

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// включаем поддержку namespace (по умолчанию выключена)  
		factory.setNamespaceAware(true);

		XmlPullParser xpp = factory.newPullParser();

		xpp.setInput(is, null);
		return xpp;

	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
		// listener.ApiProcessinDone();
	}

	public String getDetailImageUrls() {
		this.execute();
		return detailImageUrls;
	}

}

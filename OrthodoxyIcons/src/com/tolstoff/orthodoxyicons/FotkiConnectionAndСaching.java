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

public class FotkiConnectionAnd�aching extends AsyncTask<String, Void, Void> {

	private String titleImage = null, listImageUrls = null,
			galleryImageUrls = null, detailImageUrls = null;

	private ApiProcessicngListener listener;

	private Context context;

	public FotkiConnectionAnd�aching(Context context) {
		this.context = context;

	}

	@Override
	protected Void doInBackground(String... url) {
		try {

			connectForUrlImages(url[0]);

			// LogT.log("previewImageURL: " + previewImageURL.size());
		} catch (IOException e) {

		}
		return null;
	}

	private void connectForUrlImages(String urlAlbumCollection)
			throws IOException {

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
				LogT.log("��� ���������");
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

	// ���������� �������
	private void xmlParser(InputStream inputStream) {

		String tagName = "";

		XmlPullParser xpp;

		DBcache mDBcache = new DBcache(context);
		

		try {
			xpp = prepareXmlParser(inputStream);

			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {

				if (xpp.getEventType() == XmlPullParser.START_TAG) {
					tagName = xpp.getName();

					// ������ �������� �������� (������ �������)
					if (tagName.equals("title")) {
						if (xpp.getDepth() == 3) {
							titleImage = xpp.nextText();
							// LogT.log(titleImage.get(titleImage.size()-1));
						}

					}

					// ������ ������ �� ������ ��������
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
					
					mDBcache.WriteResultToDb(titleImage, listImageUrls, galleryImageUrls, detailImageUrls);
					titleImage = null;
					listImageUrls = null;
					galleryImageUrls = null;
					detailImageUrls = null;

				}

				xpp.next();

			}

			LogT.log("END_DOCUMENT");

			

		}

		catch (IOException e) {
			LogT.log("IOException");
		}

		catch (XmlPullParserException e) {
			LogT.log("XmlPullParserException");
		}

	}

	// ���������� �������
	private XmlPullParser prepareXmlParser(InputStream is)
			throws XmlPullParserException {

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// �������� ��������� namespace (�� ��������� ���������)
		factory.setNamespaceAware(true);

		XmlPullParser xpp = factory.newPullParser();

		xpp.setInput(is, null);
		return xpp;

	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub

		super.onPostExecute(result);
		listener.ApiProcessinDone();
	}


	public String  getDetailImageUrls() {
		this.execute();
		return detailImageUrls;
	}

}

package com.tolstoff.orthodoxyicons;

import java.util.ArrayList;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class FragmentGallery extends Fragment implements
		LoaderCallbacks<Cursor> {

	DisplayImageOptions options;
	protected AbsListView listView;
	protected ImageLoader imageLoader;
//	FotkiConnectionAndÑaching yandexFotkiApi;
	ImageAdapter imageAdapter;
	DBcache dbCache;

	ArrayList<String> imageUrls = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View galleryFragmentView = inflater.inflate(R.layout.gallery,
				container, false);

		dbCache = new DBcache(getActivity());
		dbCache.open();

		options = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.ic_stub)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));



		getLoaderManager().initLoader(0, null,  this);

		listView = (GridView) galleryFragmentView.findViewById(R.id.gridview);

		imageAdapter = new ImageAdapter();
		
		((GridView) listView).setAdapter(imageAdapter);
		 
		return galleryFragmentView;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bnd) {
		return new DBCursorLoader(getActivity(), dbCache);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		return cursor;	

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {


	}

	// @Override
	// public void ApiProcessinDone() {
	//
	// imageUrls = yandexFotkiApi.getPreviewImageURL();
	// ((GridView) listView).setAdapter(new ImageAdapter());
	// listView.setOnItemClickListener(new OnItemClickListener() {
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// startImagePagerActivity(position);
	// }
	// });
	//
	// }

	private void startImagePagerActivity(int position) {
		// Intent intent = new Intent(this, ImagePagerActivity.class);
		// intent.putExtra(Extra.IMAGES, imageUrls);
		// intent.putExtra(Extra.IMAGE_POSITION, position);
		// startActivity(intent);
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				// LogT.log("Ïåðåä èíôëàéòåðîì");
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// LogT.log("Ïîñëå èíôëàéòåðà");
				view = inflater
						.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view
						.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			try {
				imageLoader.displayImage(imageUrls.get(position),
						holder.imageView, options,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								holder.progressBar.setProgress(0);
								holder.progressBar.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								holder.progressBar.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								holder.progressBar.setVisibility(View.GONE);
							}
						},

						new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
								holder.progressBar.setProgress(Math
										.round(100.0f * current / total));
							}
						});

			} catch (Exception e) {
				LogT.log(e);
			}

			return view;

		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
		}
	}

	static class DBCursorLoader extends CursorLoader {
		DBcache dbCache;

		public DBCursorLoader (Context context, DBcache dbCache) {
			super(context);
			this.dbCache = dbCache;

		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = dbCache.getAllData();
			return cursor;

		}

	}

}

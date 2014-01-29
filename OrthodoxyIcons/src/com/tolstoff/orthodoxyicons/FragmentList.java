package com.tolstoff.orthodoxyicons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FragmentList extends Fragment implements ApiProcessicngListener {

	ArrayList<String> imageUrls = new ArrayList<String>();
	ArrayList<String> imageTitles = new ArrayList<String>();

	DisplayImageOptions options;
	protected AbsListView listView;
	protected ImageLoader imageLoader;
	FotkiConnectionAnd—aching yandexFotkiApi;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View listFragmentView = inflater.inflate(R.layout.list, container,
				false);

		options = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.ic_stub)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		yandexFotkiApi = FotkiConnectionAnd—aching.getInstance();
		yandexFotkiApi.setContext(getActivity());
		yandexFotkiApi.setListener(this);
		yandexFotkiApi.execute();

		listView = (ListView) listFragmentView.findViewById(android.R.id.list);

		return listFragmentView;
	}

	public void ApiProcessinDone() {

		imageUrls = yandexFotkiApi.getPreviewImageURL();
		imageTitles = yandexFotkiApi.getTitleImage();
		
		
		((ListView) listView).setAdapter(new ItemAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startImagePagerActivity(position);
			}
		});

	}

	private void startImagePagerActivity(int position) {
		// Intent intent = new Intent(this, ImagePagerActivity.class);
		// intent.putExtra(Extra.IMAGES, imageUrls);
		// intent.putExtra(Extra.IMAGE_POSITION, position);
		// startActivity(intent);
	}

	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater
						.inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.text);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText(imageTitles.get(position));

			imageLoader.displayImage(imageUrls.get(position), holder.image,
					options, animateFirstListener);

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}

	}
}

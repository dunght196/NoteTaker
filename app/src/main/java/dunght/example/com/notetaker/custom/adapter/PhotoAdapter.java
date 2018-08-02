package dunght.example.com.notetaker.custom.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import java.util.ArrayList;
import dunght.example.com.notetaker.R;
import dunght.example.com.notetaker.controller.OnClickInterface;


public class PhotoAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<String> listPhoto;
    private OnClickInterface onClickInterface;

    public PhotoAdapter(OnClickInterface onClickInterface,Activity activity, ArrayList<String> listPhoto) {
        this.activity = activity;
        this.listPhoto = listPhoto;
        this.onClickInterface = onClickInterface;
    }

    @Override
    public int getCount() {
        return listPhoto.size();
    }

    @Override
    public Object getItem(int i) {
        return listPhoto.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        try {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_photo, null);
            ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.iv_custom_photo);
            ImageView ivClose = (ImageView)convertView.findViewById(R.id.iv_custom_close);

            String url = listPhoto.get(i);

            //Using universalimageloader
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new FadeInBitmapDisplayer(100)).build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
            imageLoader.displayImage(url, ivPhoto);

            ivClose.setImageResource(R.drawable.ic_close);

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickInterface.click(i);
                }
            });

            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

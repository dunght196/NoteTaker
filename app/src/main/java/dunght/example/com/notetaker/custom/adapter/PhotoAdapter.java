package dunght.example.com.notetaker.custom.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

import dunght.example.com.notetaker.R;
import dunght.example.com.notetaker.config.OnClickInterface;


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
            Uri uri = Uri.parse(listPhoto.get(i));
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            InputStream imageStream = activity.getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            convertView = activity.getLayoutInflater().inflate(R.layout.custom_gridview_photo, null);
            ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.iv_custom_photo);
            ImageView ivClose = (ImageView)convertView.findViewById(R.id.iv_custom_close);

            ivPhoto.setImageBitmap(selectedImage);
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

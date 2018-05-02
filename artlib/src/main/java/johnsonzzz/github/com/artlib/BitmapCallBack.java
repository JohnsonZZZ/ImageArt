package johnsonzzz.github.com.artlib;

import android.graphics.Bitmap;

/**
 * Created by JohnsonFan on 2018/3/7.
 */

public interface BitmapCallBack {

	void onBitmapLoaded(Bitmap bitmap);

	void onBitmapFailed(Exception e);

}

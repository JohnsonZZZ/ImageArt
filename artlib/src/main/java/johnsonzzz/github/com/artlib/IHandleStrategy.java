package johnsonzzz.github.com.artlib;

import android.graphics.Bitmap;

/**
 * Created By Johnson on 2018/5/3.
 */
public interface IHandleStrategy {
	//todo
	boolean isRunningHandle = false;
	/**
	 *
	 * @param builder
	 * @return true: this Strategy can handle the image builder
	 */
	boolean canHandleImage(ArtBuilder builder);

	Bitmap loadBitmap(ArtBuilder builder);

//	boolean stop();
}

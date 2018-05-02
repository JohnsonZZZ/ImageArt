package johnsonzzz.github.com.artlib.compress;

import android.graphics.Bitmap;

/**
 *
 */
public interface ICompress {

	/**
	 *
	 * @param bitmap
	 * @param w
	 * @param h
	 * @param quality 压缩质量
	 * @param destFileName 目标文件路径
	 * @return
	 */
	boolean compressBitmap(Bitmap bitmap, int w, int h, int quality, String destFileName);

}

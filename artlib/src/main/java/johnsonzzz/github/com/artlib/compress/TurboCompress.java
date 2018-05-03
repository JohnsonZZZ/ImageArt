package johnsonzzz.github.com.artlib.compress;

import android.graphics.Bitmap;

import johnsonzzz.github.com.artlib.Utils;

public class TurboCompress implements ICompress{
	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("imageart");
	}


	/**
	 *
	 * @param bitmap bitmap
	 * @param w the width of image to generate
	 * @param h the height of image to generate
	 * @param quality the quality of compress
	 * @param fileNameBytes out path
	 * @param optimize Whether or not the optimal compression is opened
	 * @return true:success finally
	 */
	private static native boolean compressBitmap(Bitmap bitmap, int w, int h, int quality, byte[] fileNameBytes, boolean
			optimize);


	@Override
	public boolean compressBitmap(Bitmap bitmap, int w, int h, int quality, String destFileName) {
		return compressBitmap(bitmap, w, h, quality, destFileName.getBytes(), true);
	}
}

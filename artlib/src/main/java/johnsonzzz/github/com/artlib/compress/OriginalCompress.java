package johnsonzzz.github.com.artlib.compress;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import johnsonzzz.github.com.artlib.Utils;

public class OriginalCompress implements ICompress{

	@Override
	public boolean compressBitmap(Bitmap bitmap, int w, int h, int quality, String destFileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(destFileName));
			Bitmap scaleBitmap = Utils.scaleTo(bitmap, w, h);
			return scaleBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} finally {
			Utils.closeQuietly(fos);
		}
		return false;
	}

}

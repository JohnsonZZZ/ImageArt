package johnsonzzz.github.com.artlib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import static android.content.ContentResolver.SCHEME_FILE;

/**
 * Created By Johnson on 2018/5/3.
 */
public class FileHandleStrategy implements IHandleStrategy {
	@Override
	public boolean canHandleImage(ArtBuilder builder) {
		if (builder.uri == null && builder.sourceFile == null) {
			return false;
		}
		Uri uri = builder.uri == null ? Uri.fromFile(builder.sourceFile) : builder.uri;
		return SCHEME_FILE.equals(uri.getScheme());
	}

	@Override
	public Bitmap loadBitmap(ArtBuilder builder) {
		Uri uri = builder.uri == null ? Uri.fromFile(builder.sourceFile) : builder.uri;
		BitmapFactory.Options options = Utils.createBitmapOptions(builder);
		if (Utils.requiresInSampleSize(options)) {
			BitmapFactory.decodeFile(uri.getPath(), options);
			Utils.calculateInSampleSize(options, builder.targetWidth, builder.targetHeight);
		}
		Bitmap srcBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
		if (builder.isAutoRotate) {
			srcBitmap = Utils.rotateBitmapByDegree(srcBitmap, Utils.getBitmapDegree(uri.getPath()));
		}
		return Utils.scaleTo(srcBitmap, builder.targetWidth, builder.targetHeight);
	}
}

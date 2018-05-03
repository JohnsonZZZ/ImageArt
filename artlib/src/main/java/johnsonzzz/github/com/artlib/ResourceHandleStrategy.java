package johnsonzzz.github.com.artlib;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileNotFoundException;

/**
 * Created By Johnson on 2018/5/3.
 */
public class ResourceHandleStrategy implements IHandleStrategy {
	@Override
	public boolean canHandleImage(ArtBuilder builder) {
		if (builder.drawableResId != 0) {
			return true;
		}
		return ContentResolver.SCHEME_ANDROID_RESOURCE.equals(builder.uri.getScheme());
	}

	@Override
	public Bitmap loadBitmap(ArtBuilder builder) {
		try {
			Resources res = Utils.getResources(builder.context, builder);
			int id = Utils.getResourceId(res, builder);
			return decodeResource(res, id, builder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Bitmap decodeResource(Resources resources, int id, ArtBuilder builder) {
		final BitmapFactory.Options options = Utils.createBitmapOptions(builder);
		if (Utils.requiresInSampleSize(options)) {
			BitmapFactory.decodeResource(resources, id, options);
			Utils.calculateInSampleSize(options, builder.targetWidth, builder.targetHeight);
		}
		Bitmap srcBitmap = BitmapFactory.decodeResource(resources, id, options);
		return Utils.scaleTo(srcBitmap, builder.targetWidth, builder.targetHeight);
	}
}

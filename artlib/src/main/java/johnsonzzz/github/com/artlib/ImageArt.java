package johnsonzzz.github.com.artlib;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

public class ImageArt {
	private volatile static ImageArt sInstance;
	private List<IHandleStrategy> handleList;

	private ImageArt() {
		handleList.add(new FileHandleStrategy());
		handleList.add(new ResourceHandleStrategy());
		handleList.add(new NetHandleStrategy());
	}

	public static ImageArt getInstance() {
		if (sInstance == null) {
			synchronized (ImageArt.class) {
				if (sInstance == null) {
					sInstance = new ImageArt();
				}
			}
		}
		return sInstance;
	}

	public ArtBuilder createBuilder(Context context) {
		return new ArtBuilder(context);
	}

	protected void attachBuilder(ArtBuilder builder){
		if (builder.context == null) {
			throw new NullPointerException("you must set ArtBuilder's context at first");
		}
		if (builder.drawableResId == 0 && builder.sourceFile == null && builder.url == null) {
			throw new IllegalStateException("you must set ArtBuilder's imagesource");
		}
//			uri = Uri.parse(String.format("%s://%s/%d", ContentResolver.SCHEME_ANDROID_RESOURCE, builder.context
//					.getPackageName(), builder.drawableResId));
//			uri = Uri.fromFile(builder.sourceFile);
//			uri =  Uri.parse(builder.url);
		for (IHandleStrategy strategy : handleList) {
			if (strategy.canHandleImage(builder)) {
				Bitmap bitmap = strategy.loadBitmap(builder);
				break;
			}
		}
	}
}

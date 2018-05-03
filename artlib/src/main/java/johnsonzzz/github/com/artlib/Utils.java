package johnsonzzz.github.com.artlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Looper;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Utils {

	public static BitmapFactory.Options createBitmapOptions(ArtBuilder data) {
		final boolean justBounds = data.targetWidth != 0 || data.targetHeight != 0;
		BitmapFactory.Options options = null;
		if (justBounds || data.config != null) {
			options = new BitmapFactory.Options();
			options.inJustDecodeBounds = justBounds;
			options.inInputShareable = true;
			options.inPurgeable = true;
			options.inPreferredConfig = data.config;
		}
		return options;
	}

	public static Bitmap scaleTo(Bitmap bitmap, int reqWidth, int reqHeight) {
		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		float scale;
		if (originalWidth < originalHeight) {
			scale = (float) reqHeight / originalHeight;
		} else {
			scale = (float) reqWidth / originalWidth;
		}
		if (scale > 1 || reqWidth == 0 || reqHeight == 0) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap scaleBitmap =  Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return scaleBitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		//Raw width and height of image
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth || height > reqHeight) {
			final int halfWidth = width / 2;
			final int halfHeight = height / 2;

			//calculate the largest inSampleSize value that is a power of 2 and keeps both
			//width and height larger than the requested width and height
			while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
				inSampleSize *= 2;
			}
		}
		options.inJustDecodeBounds = false;
		return inSampleSize;
	}

	/**
	 * 读取图片的旋转的角度
	 * @param imagePath 图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String imagePath) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(imagePath);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
				case ExifInterface.ORIENTATION_TRANSPOSE:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
				case ExifInterface.ORIENTATION_TRANSVERSE:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * Close closable and hide possible {@link IOException}
	 * @param closeable closeable object
	 */
	public static void closeQuietly(Closeable... closeable) {
		if (closeable == null) {
			return;
		}
		try {
			for (Closeable close : closeable){
				close.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Ignored
		}
	}

	public static Resources getResources(Context context, ArtBuilder builder) throws FileNotFoundException {
		if (builder.drawableResId != 0 || builder.uri == null) {
			return context.getResources();
		}

		String pkg = builder.uri.getAuthority();
		if (pkg == null)  {
			throw new FileNotFoundException("No package provided: " + builder.uri);
		}
		try {
			PackageManager pm = context.getPackageManager();
			return pm.getResourcesForApplication(pkg);
		} catch (PackageManager.NameNotFoundException e) {
			throw new FileNotFoundException("Unable to obtain resources for package: " + builder.uri);
		}
	}


	public static int getResourceId(Resources resources, ArtBuilder builder) throws FileNotFoundException {
		if (builder.drawableResId != 0 || builder.uri == null) {
			return builder.drawableResId;
		}

		String pkg = builder.uri.getAuthority();
		if (pkg == null) throw new FileNotFoundException("No package provided: " + builder.uri);

		int id;
		List<String> segments = builder.uri.getPathSegments();
		if (segments == null || segments.isEmpty()) {
			throw new FileNotFoundException("No path segments: " + builder.uri);
		} else if (segments.size() == 1) {
			try {
				id = Integer.parseInt(segments.get(0));
			} catch (NumberFormatException e) {
				throw new FileNotFoundException("Last path segment is not a resource ID: " + builder.uri);
			}
		} else if (segments.size() == 2) {
			String type = segments.get(0);
			String name = segments.get(1);

			id = resources.getIdentifier(name, type, pkg);
		} else {
			throw new FileNotFoundException("More than two path segments: " + builder.uri);
		}
		return id;
	}

	public static boolean requiresInSampleSize(BitmapFactory.Options options) {
		return options != null && options.inJustDecodeBounds;
	}

	public static Bitmap rotateBitmapByDegree(Bitmap srcBitmap, int degree) {
		if (degree == 0) {
			return srcBitmap;
		}
		Bitmap newBitmap;
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.preRotate(degree);
		// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
		newBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);

		if (newBitmap == null) {
			newBitmap = srcBitmap;
		}
		if (srcBitmap != null && !srcBitmap.isRecycled()) {
			srcBitmap.recycle();
		}
		return newBitmap;
	}

	public static void checkNotMain() {
		if (isMain()) {
			throw new IllegalStateException("Method call should not happen from the main thread.");
		}
	}

	public static boolean isMain() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}
}

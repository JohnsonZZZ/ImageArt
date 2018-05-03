package johnsonzzz.github.com.artlib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created By Johnson on 2018/5/3.
 */
public class NetHandleStrategy implements IHandleStrategy {
	private static final String SCHEME_HTTP = "http";
	private static final String SCHEME_HTTPS = "https";

	@Override
	public boolean canHandleImage(ArtBuilder builder) {
		if (TextUtils.isEmpty(builder.url) || builder.uri == null) {
			return false;
		}
		Uri uri = builder.uri == null ? Uri.parse(builder.url) : builder.uri;
		return (SCHEME_HTTP.equals(uri.getScheme()) || SCHEME_HTTPS.equals(uri.getScheme()));
	}

	@Override
	public Bitmap loadBitmap(ArtBuilder builder) {
		Utils.checkNotMain();
		Uri uri = builder.uri == null ? Uri.parse(builder.url) : builder.uri;
		InputStream is = null;
		try {
			HttpURLConnection connection = openConnection(uri);
			int responseCode = connection.getResponseCode();
			if (responseCode >= 300) {
				connection.disconnect();
				throw new IOException("responseCode:" + responseCode + " " + connection.getResponseMessage());
			}
			is = connection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			Utils.closeQuietly(is);
		}

		final BitmapFactory.Options options = Utils.createBitmapOptions(builder);
		if (Utils.requiresInSampleSize(options)) {
			BitmapFactory.decodeStream(is, null, options);
			Utils.calculateInSampleSize(options, builder.targetWidth, builder.targetHeight);
		}
		Bitmap srcBitmap = BitmapFactory.decodeStream(is, null, options);
		return Utils.scaleTo(srcBitmap, builder.targetWidth, builder.targetHeight);
	}

	protected HttpURLConnection openConnection(Uri path) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(path.toString()).openConnection();
		connection.setConnectTimeout(15 * 1000);//15 * 1000; // 15s
		connection.setReadTimeout(20*1000);
		return connection;
	}
}

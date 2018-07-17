package johnsonzzz.github.com.artlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * Created By Johnson on 2018/5/2.
 */
public final class ArtBuilder {
	protected boolean isAutoRotate = true;//适配三星、小米等手机图片拍照后旋转问题
	protected int targetWidth;
	protected int targetHeight;
	protected int quality;//压缩质量
	protected long maxSize;//目标文件最大大小,单位b,如果目标是300kb，则maxSize需要填写300*1024
	protected String url;
	protected Uri uri;
	protected File sourceFile;//图片源文件
	protected String targetFilePath;//图片压缩后路径
	protected int drawableResId;
	protected CompressCallBack bitmapCallBack;
	protected Bitmap.Config config;
	protected Context context;
	protected boolean isAsync;//true:开启异步线程压缩 false:同步压缩
	protected int threadCount;//压缩图片的线程

	public ArtBuilder(Context context) {
		this.context = context.getApplicationContext();
	}

	public ArtBuilder url(String url) {
		this.url = url;
		return this;
	}

	public ArtBuilder uri(Uri uri) {
		this.uri = uri;
		return this;
	}

	public ArtBuilder sourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
		return this;
	}

	public ArtBuilder targetFilePath(String targetFilePath) {
		this.targetFilePath = targetFilePath;
		return this;
	}

	public ArtBuilder drawableResId(int drawableResId) {
		this.drawableResId = drawableResId;
		return this;
	}

	public ArtBuilder threadCount(int maxThreadsCount) {
		this.threadCount = maxThreadsCount;
		return this;
	}

	public ArtBuilder resize(int targetWidth, int targetHeight) {
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		return this;
	}

	public ArtBuilder autoRotate(boolean isAutoRotate) {
		this.isAutoRotate = isAutoRotate;
		return this;
	}

	public ArtBuilder quality(int quality) {
		this.quality = quality;
		return this;
	}

	public ArtBuilder maxSize(long maxSize) {
		this.maxSize = maxSize;
		return this;
	}

	public ArtBuilder config(Bitmap.Config config) {
		this.config = config;
		return this;
	}

	public ArtBuilder callback(CompressCallBack bitmapCallBack) {
		this.bitmapCallBack = bitmapCallBack;
		return this;
	}

	public ArtBuilder isAsync(boolean isAsync) {
		this.isAsync = isAsync;
		return this;
	}

	public void build() {
		//set default builder's config
		if (quality <= 0) {
			quality = 97;
		}
		if (maxSize <= 0) {
			maxSize = 300*1024;//默认大小300kb
		}
		if (config == null) {
			config = Bitmap.Config.RGB_565;
		}
		if (threadCount <= 0) {
			threadCount = 3;
		}
		ImageArt.getInstance().attachBuilder(this);
	}
}

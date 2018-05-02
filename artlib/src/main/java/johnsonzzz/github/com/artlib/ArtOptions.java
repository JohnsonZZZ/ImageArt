package johnsonzzz.github.com.artlib;

import java.io.File;

/**
 * Created By Johnson on 2018/5/2.
 */
public class ArtOptions {
	protected boolean isAutoRotate = true;//适配三星、小米等手机图片拍照后旋转问题
	protected int targetWidth;
	protected int targetHeight;
	protected int quality = 100;//压缩质量
	protected long maxSize;//目标文件最大大小,单位b,如果目标是300kb，则maxSize需要填写300*1024
	protected String url;
	protected File file;
	protected int drawableResId;
	protected BitmapCallBack bitmapCallBack;

	public ArtOptions(String url) {
		this.url = url;
	}

	public ArtOptions(File file) {
		this.file = file;
	}

	public ArtOptions(int drawableResId) {
		this.drawableResId = drawableResId;
	}

	public ArtOptions resize(int targetWidth, int targetHeight) {
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		return this;
	}

	public ArtOptions autoRotate(boolean isAutoRotate) {
		this.isAutoRotate = isAutoRotate;
		return this;
	}

	public ArtOptions quality(int quality) {
		this.quality = quality;
		return this;
	}

	public ArtOptions maxSize(long maxSize) {
		this.maxSize = maxSize;
		return this;
	}

	public ArtOptions callback(BitmapCallBack bitmapCallBack) {
		this.bitmapCallBack = bitmapCallBack;
		return this;
	}

	public void build() {
		if (file != null) {
//			ImageArt.getInstance().find(file).
		}
	}
}

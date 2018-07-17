package johnsonzzz.github.com.artlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import johnsonzzz.github.com.artlib.compress.ICompress;
import johnsonzzz.github.com.artlib.compress.OriginalCompress;
import johnsonzzz.github.com.artlib.compress.TurboCompress;

public class ImageArt {
	private volatile static ImageArt sInstance;
	private List<IHandleStrategy> handleList;
	private ExecutorService executors;

	private final static int MSG_COMPRESS_SUCCESS = 101;
	private final static int MSG_COMPRESS_FAIL = 102;


	private ImageArt() {
		handleList= new ArrayList<>();
		handleList.add(new FileHandleStrategy());
		handleList.add(new ResourceHandleStrategy());
		handleList.add(new NetHandleStrategy());
		Collections.unmodifiableList(handleList);
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
			throw new NullPointerException("You must set ArtBuilder's context or  at first");
		}
		if (TextUtils.isEmpty(builder.targetFilePath)) {
			throw new IllegalStateException("You should set ArtBuilder's targetFilePath");
		}
		if (builder.drawableResId == 0 && builder.sourceFile == null && builder.url == null) {
			throw new IllegalStateException("You must set your imagesource");
		}
		if (builder.isAsync) {
			if (executors == null) {
				executors = Executors.newFixedThreadPool(builder.threadCount);
			}
			executors.execute(new ArtRunnable(builder));
		} else {
			traverseAlltraverse(builder);
		}
	}

	public ICompress getDefaultCompress(Bitmap bitmap) {
		ICompress compress;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && !bitmap.hasAlpha()) {
			compress = new TurboCompress();
		} else {
			compress = new OriginalCompress();
		}
		return compress;
	}

	class  ArtRunnable implements Runnable{
		ArtBuilder builder;
		ArtRunnable(ArtBuilder builder) {
			this.builder = builder;
		}
		@Override
		public void run() {
			traverseAlltraverse(builder);
		}
	}

	private void traverseAlltraverse(ArtBuilder builder) {
		for (IHandleStrategy strategy : handleList) {
			if (strategy.canHandleImage(builder)) {
				Bitmap bitmap = strategy.loadBitmap(builder);
				ICompress compress = getDefaultCompress(bitmap);
				boolean result = compress.compressBitmap(bitmap, builder.targetWidth, builder.targetHeight,
						builder.quality, builder.targetFilePath);
				Message message = mainHandler.obtainMessage(result ? MSG_COMPRESS_SUCCESS : MSG_COMPRESS_FAIL, builder);
				mainHandler.sendMessage(message);
				break;
			}
		}
	}


	static final Handler mainHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_COMPRESS_SUCCESS : {
					ArtBuilder builder = (ArtBuilder) msg.obj;
					if (builder != null && builder.bitmapCallBack != null) {
						builder.bitmapCallBack.onCompressSuccess();
					}
				}
				break;
				case MSG_COMPRESS_FAIL : {
					ArtBuilder builder = (ArtBuilder) msg.obj;
					if (builder != null && builder.bitmapCallBack != null) {
						builder.bitmapCallBack.onCompressFailed();
					}
				}
				break;
				default:
					break;
			}
		}
	};
}

package johnsonzzz.github.com.artlib;

import java.io.File;

public class ImageArt {
	private volatile static ImageArt sInstance;

	private ImageArt() {

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

	public ArtOptions find(String url) {
		return new ArtOptions(url);
	}

	public ArtOptions find(int drawable) {
		return new ArtOptions(drawable);
	}

	public ArtOptions find(File file) {
		return new ArtOptions(file);
	}

	protected void attachOptions(ArtOptions options){

	}
}

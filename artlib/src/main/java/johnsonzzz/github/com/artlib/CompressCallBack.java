package johnsonzzz.github.com.artlib;

/**
 * Created by JohnsonFan on 2018/3/7.
 */

public interface CompressCallBack {

	void onCompressSuccess();

	void onCompressFailed();

	class EmptyCallBack implements CompressCallBack {

		@Override
		public void onCompressSuccess() {

		}

		@Override
		public void onCompressFailed() {

		}
	}
}

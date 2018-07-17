package johnsonzzz.github.com.imageart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import johnsonzzz.github.com.artlib.CompressCallBack;
import johnsonzzz.github.com.artlib.ImageArt;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		InputStream is = getResources().openRawResource(R.raw.test123);
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/source.jpg");
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startCompress();
			}
		});
	}

	private void startCompress() {
		ImageArt.getInstance()
				.createBuilder(this)
				.autoRotate(true)
				.config(Bitmap.Config.RGB_565)
				.isAsync(true)
				.maxSize(300 * 1024)
				.threadCount(3)
				//源图片文件
				.sourceFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/source.jpg"))
				//压缩后图片路径
				.targetFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/target.jpg")
				.callback(new CompressCallBack() {
					@Override
					public void onCompressSuccess() {
						Toast.makeText(MainActivity.this, "压缩成功", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCompressFailed() {
						Toast.makeText(MainActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
					}
				})
				.build();
	}

}

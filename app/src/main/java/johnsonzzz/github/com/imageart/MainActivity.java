package johnsonzzz.github.com.imageart;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

import johnsonzzz.github.com.artlib.CompressCallBack;
import johnsonzzz.github.com.artlib.ImageArt;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageArt.getInstance()
				.createBuilder(this)
				.autoRotate(true)
				.config(Bitmap.Config.RGB_565)
				.isAsync(true)
				.maxSize(300 * 1024)
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

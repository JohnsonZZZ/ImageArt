# ImageArt
compress image by JPEG-TURBO 


### How to use
```
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
		}})
		.build();
```

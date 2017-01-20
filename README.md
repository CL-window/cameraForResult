只是一个简单的模拟
给其他应用提供数据
做一个拍照功能,系统可以调用我的拍照,返回数据给调用者
google : https://developer.android.com/training/basics/intents/filters.html
文章介绍的很详细，但是我并没有找到我想要的
其实文章也还是很有用的,最起码告诉你 需要 setResult(RESULT_OK);
问题是，怎么把拍照数据返回回去，没错 通过启动我们应用的Intent
Uri saveUri = getIntent().getExtras().getParcelable(MediaStore.EXTRA_OUTPUT);

OutputStream outputStream = null;
try {
    outputStream = getContentResolver().openOutputStream(saveUri);
    if (outputStream != null ) {
        photo.compress(Bitmap.CompressFormat.JPEG,100,outputStream);// write your bitmap here
        setResult(RESULT_OK);
    }
} catch (IOException e) {
    e.printStackTrace();
}

怎么指定我们的应用程序拍照
app/src/main/AndroidManifest.xml
<intent-filter>
    <action android:name="android.media.action.IMAGE_CAPTURE"/>
    <category android:name="android.intent.category.DEFAULT"/>
</intent-filter>

记录一个很奇怪的问题
设置app为默认拍照软件后,CapturePictureActivity 一直循环在新建，导致崩溃
只是新建，没有destroy,但是取消这个设置后就没有问题
问题原因没有找到  感觉猜测是 startActivityForResult 的缘故,因为启动的这个界面是startActivityForResult启动的
然后这个界面又 startActivityForResult 一个新的界面，只是猜测
附上一段日志，简直要疯了的节奏
01-20 16:35:34.930 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@9de6483
01-20 16:35:34.930 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:34.955 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@3bb28e2
01-20 16:35:34.955 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:34.978 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@f00ec1d
01-20 16:35:34.978 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.000 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@9afb24
01-20 16:35:35.000 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.022 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@4470ca7
01-20 16:35:35.022 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.049 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@a98016
01-20 16:35:35.049 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.072 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@1cbf8a1
01-20 16:35:35.072 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.093 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@f314438
01-20 16:35:35.093 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.115 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@b4b3e8b
01-20 16:35:35.115 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.138 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@2e0200a
01-20 16:35:35.138 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.163 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@1a868e5
01-20 16:35:35.163 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.183 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@4f1580c
01-20 16:35:35.183 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
01-20 16:35:35.204 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: onCreate...com.cl.slack.cameraforresult.CapturePictureActivity@ed59e2f
01-20 16:35:35.204 19392-19392/com.cl.slack.cameraforresult I/CapturePictureActivity: openSystemCamera...
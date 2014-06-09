NoticeManager
=============

NoticeManager implements a quick android notification component. When the android client boots up, it will start repeating query to get the new message from server and display the new message as a notice. 


### sample-server:    

Based on rails. need to start it by rails s 


### notice_manager:

One sample project to use the library. Based maven to build the project.


==================

### 组件的调用API

组件类名
```java
public class KCMessagePushManager{}
```

设置监听消息的地址
```java
public void set_listen_url(String url)
```

设置轮询多久以后开始执行
```java
public void set_delay(int millisecond)
```

每隔几秒重复一次
```java
public void set_period(int millisecond)
```

设置通知的图标
```java
// 设置通知信息显示在通知栏上时的图标，如果不设置默认用当前应用的图标
public void set_notification_icon(int res_id)
```

设置获取信息的监听器
```java
public void add_message_listener(MessageListener listener)

interface MessageListener{
  // 当从 listen_url 获取到信息时，会通过这个方法构建通知栏要显示的信息
  // message_response 是从服务端获取的信息元数据
  // 当点击通知栏信息时，会根据这个方法返回的 PendingIntent 打开界面
  public PendingIntent build_pending_intent(String message_response)
}

```

启动监听
```java
  public void start();
```


AndroidManiFest 设置
```java
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

同时需要注册
<service android:name="com.mindpin.android.noticemanager.MessageNoticeService" android:label="Message Notice Service">
    <intent-filter>
        <action android:name="com.message.mylib.MessageNoticeService" />
    </intent-filter>
</service>

<receiver
    android:name="com.mindpin.android.noticemanager.StartMessageNoticeServiceAtBootReceiver"
    android:enabled="true"
    android:exported="true"
    android:label="StartMyServiceAtBootReceiver">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>

```

使用示例
```java
KCMessagePushManager manager = new KCMessagePushManager(context);

manager.set_listen_url("http://xxxx.com/yyyy");
manager.set_delay(100000);
manager.set_period(50000);
manager.add_message_listener(new MessageListener(){

  public PendingIntent build_pending_intent(String message_response){
    // 根据 message_response 构建 PendingIntent 对象
  };
});

manager.start();
```

==========




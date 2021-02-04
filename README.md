# Sensorlibrary

​		射频调度库，针对于高频读卡和短距射频处理封装，单单公司使用版本。



### Gradle

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```
dependencies {
	      implementation 'com.github.Sheedon:sensorlibrary:1.0.3'
}
```



### Maven

**Step 1.** Add the JitPack repository to your build file

```xml
<repositories>
	<repository>
		   <id>jitpack.io</id>
		   <url>https://jitpack.io</url>
	</repository>
</repositories>
```

**Step 2.** Add the dependency

```xml
	<dependency>
	    <groupId>com.github.Sheedon</groupId>
	    <artifactId>sensorlibrary</artifactId>
	    <version>1.0.3</version>
	</dependency>
```



### Use Library

#### 1. Init Library

```java
// 射频读卡片类型：RS/RFID默认版本
// 端口号
// 波特率
// 初始化结果监听
SensorFactory.init(SensorConstant.TYPE_RS485, SensorConstant.PORT, 9600, new InitializeListener() {
			// 初始化完成		
  		@Override
      public void OnInitializeComplete() {
                
      }

  		// 射频读卡片类型
      @Override
      public int onSensorType() {
           return SensorConstant.TYPE_RS485;
      }

  		// 端口号
      @Override
      public String onPort() {
           return "/dev/ttyS1";
      }

  		// 波特率
      @Override
      public int onBaudRate() {
           return 9600;
      }
});
```



#### 2. Hold a card reader and monitor

```java
// 获取读卡控制中心
CardReaderCenter readerCenter = SensorFactory.getCardReaderCenter();

// 绑定异常
readerCenter.bindException(new OnExceptionListener() {
     @Override
     public void onException(boolean isException) {
     			// 是否异常
     }
});

// 添加读卡监听
readerCenter.addListener(new OnCardReaderListener() {
     
  	 /**
      * 读卡信息
      * @param lblNum 卡片编号 
      * @param isContinuousSend 是否是连续发送
      */
      @Override
      public void onCardInfo(String lblNum, boolean isContinuousSend) {
					// do something
      }

      /**
       * 读卡器是否正常
       * @param isNormal 是否正常
       */
      @Override
      public void onCardIsNormal(boolean isNormal) {
					// do something
      }
});

// 离开页面
readerCenter.removeListener(this);
```

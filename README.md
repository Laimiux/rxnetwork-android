# rxnetwork-android
A simple util and example of how to track connectivity changes in Android applications...


### How to use
In Android Manifest add 
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

Call to receive stream
```java
RxNetwork.stream(context)
```


### Download
```java
compile 'com.laimiux.rxnetwork:rxnetwork:0.0.4'
```

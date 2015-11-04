# FuckBaiduWormHole
检测当前手机是否存在百度WormHole后门。
## 检测原理：
百度WormHole会在本地的6259和40310端口开启http服务，所以检测本机是否开启了这两个端口的http服务即可判断当前是否存在百度WormHole后门，由于是直接检测端口，所以任何APK只要有这个后门都会被检测的，即使这个APK并不是百度产品，但缺陷就是无法判断到底是哪个APK开启了这个服务，所以项目没有提供卸载方法。
主要检测方法就是[CheckWormHole.java](https://github.com/aesean/FuckBaiduWormHole/blob/master/wromhole/src/main/java/com/aesean/wromhole/CheckWormHole.java)这个类，直接调用CheckWormHole的check方法即可，由于是网络访问，所以需要异步回调。由于只是一个简单的应用，所以网络访问直接用的HttpUrlConnection，用线程池做了简单的网络封装。详细的可以参考示例[app](https://github.com/aesean/FuckBaiduWormHole/tree/master/app)

安装包下载：[release apk](https://github.com/aesean/FuckBaiduWormHole/blob/master/release/FuckBaiduWormHole-app-release.apk?raw=true)

##Linux用户
Linux用户也可以使用Nmap的如下命令来检测目标地址是否开启了40310和6259端口服务。
<pre><code>sudo nmap -sS -p 40310,6259 192.168.1.1/24 | grep -e "Nmap" -e "open"</code></pre>

<img src="https://github.com/aesean/FuckBaiduWormHole/blob/master/screenshot/Screenshot_20151104-090151.png" alt="GitHub" title="危险，有后门。" width="360" height="640" />
<img src="https://github.com/aesean/FuckBaiduWormHole/blob/master/screenshot/Screenshot_20151104-090213.png" alt="GitHub" title="安全，没有发现后门。" width="360" height="640" />



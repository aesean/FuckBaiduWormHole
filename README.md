# FuckBaiduWormHole
检测当前手机是否存在百度WormHole后门。
## 检测原理：
百度WormHole会在本地的6259和40310端口开启http服务，所以检测本机是否开启了这两个端口的http服务即可判断当前是否存在百度WormHole后门，由于是直接检测端口，所以任何APK只要有这个后门都会被检测的，即使这个APK并不是百度产品，但缺陷就是无法判断到底是哪个APK开启了这个服务，所以项目没有提供卸载方法。

[release apk](https://github.com/aesean/FuckBaiduWormHole/blob/master/release/FuckBaiduWormHole-app-release.apk?raw=true)

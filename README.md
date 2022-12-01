# @zhangliang_code/react-native-xfspeech
讯飞语音听写插件
## Installation

```sh
npm i @zhangliang_code/react-native-xfspeech
```

## Install

### Android

```
# @zhangliang_code/react-native-xfspeech
-keep class com.iflytek.**{*;}
-keepattributes Signature
```

### IOS
#### 添加库
将开发工具包中lib目录下的iflyMSC.framework添加到工程中。同时请将Demo中依赖的其他库也添加到工程中。 按下图示例添加 SDK 所需要的 iOS系统库
```
库名称	                添加范围	功能
iflyMSC.framework	必要	讯飞开放平台静态库。 （这个在node_modules/@zhangliang_code/react-native-xfspeech/ios/lib下面）
libz.tbd	必要	用于压缩、加密算法。
AVFoundation.framework	必要	用于系统录音和播放 。
SystemConfiguration.framework	系统库	用于系统设置。
Foundation.framework	必要	基本库。
CoreTelephony.framework	必要	用于电话相关操作。
AudioToolbox.framework	必要	用于系统录音和播放。
UIKit.framework	必要	用于界面显示。
CoreLocation.framework	必要	用于定位。
Contacts.framework	必要	用于联系人。
AddressBook.framework	必要	用于联系人。
QuartzCore.framework	必要	用于界面显示。
CoreGraphics.framework	必要	用于界面显示。
libc++.tbd	必要	用于支持C++。
```

#### 项目target的Framework Search Paths中添加
```
"$(SRCROOT)/../node_modules/@zhangliang_code/react-native-xfspeech/ios/lib"
```

#### 设置Bitcode
在Xcode 7,8默认开启了Bitcode，而Bitcode 需要工程依赖的所有类库同时支持。MSC SDK暂时还不支持Bitcode，可以先临时关闭。后续MSC SDK支持Bitcode 时，会在讯飞开放平台上进行SDK版本更新，请关注。关闭此设置，只需在Targets - Build Settings 中搜索Bitcode 即可，找到相应选项，设置为NO。

<img src="https://www.xfyun.cn/doc/old_imges/msc_ios_image/4.jpg">

#### 用户隐私权限配置
iOS 10发布以来，苹果为了用户信息安全，加入隐私权限设置机制，让用户来选择是否允许。 隐私权限配置可在info.plist 新增相关privacy字段，MSC SDK中需要用到的权限主要包括麦克风权限、联系人权限和地理位置权限：
```
<key>NSMicrophoneUsageDescription</key>
<string></string>
<key>NSLocationUsageDescription</key>
<string></string>
<key>NSLocationAlwaysUsageDescription</key>
<string></string>
<key>NSContactsUsageDescription</key>
<string></string>
```
## Usage

```js
import React, { useEffect } from 'react';

import { StyleSheet, Text, TouchableOpacity } from 'react-native';
import {
  init,
  onBeginOfSpeech,
  onEndOfSpeech,
  onError,
  onResult,
  start,
} from 'react-native-xfspeech';

export default function App() {
  useEffect(() => {
    init('你的appId').then(() => {
      console.log('initSdk');
    });
    let onBeginOfSpeechListener = onBeginOfSpeech(() => {
      console.log('onBeginOfSpeech');
    });
    let onEndOfSpeechListener = onEndOfSpeech(() => {
      console.log('onEndOfSpeech');
    });
    let onResultListener = onResult((event) => {
      console.log('onResult', event);
    });
    let onErrorListener = onError((event) => {
      console.log('onError', event);
    });
    return () => {
      onBeginOfSpeechListener.remove();
      onEndOfSpeechListener.remove();
      onResultListener.remove();
      onErrorListener.remove();
    };
  }, []);
  return (
    <TouchableOpacity
      onPress={() => {
        start({}).then(() => {
          console.log('start');
        });
      }}
      style={styles.container}
    >
      <Text>start</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});

```

```
默认参数
resultType    json
engineType    cloud
language      zh_cn
accent        mandarin
vadBos        4000
vadEos        1000
asrPtt        1
```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

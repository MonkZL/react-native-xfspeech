package com.reactnativexfspeech;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = XfspeechModule.NAME)
public class XfspeechModule extends ReactContextBaseJavaModule implements RecognizerListener {
  public static final String NAME = "Xfspeech";
  private ReactContext context;

  public XfspeechModule(ReactApplicationContext reactContext) {
    super(reactContext);
    context = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  @ReactMethod
  public void init(String appId, Promise promise) {
    SpeechUtility.createUtility(getReactApplicationContext(), SpeechConstant.APPID + "=" + appId);
    promise.resolve(true);
  }

  @ReactMethod
  public void start(ReadableMap config, Promise promise) {
    Activity currentActivity = getCurrentActivity();
    if (currentActivity == null) {
      return;
    }

    String cloudGrammar = config.getString(SpeechConstant.CLOUD_GRAMMAR);
    String subject = config.getString(SpeechConstant.SUBJECT);
    String resultType = config.getString(SpeechConstant.RESULT_TYPE);
    String engineType = config.getString(SpeechConstant.ENGINE_TYPE);
    String language = config.getString(SpeechConstant.LANGUAGE);
    String accent = config.getString(SpeechConstant.ACCENT);
    String vadBos = config.getString(SpeechConstant.VAD_BOS);
    String vadEos = config.getString(SpeechConstant.VAD_EOS);
    String asrPtt = config.getString(SpeechConstant.ASR_PTT);

//    resultType = resultType == null ? "json" : resultType;
//    engineType = engineType == null ? "cloud" : engineType;
//    language = language == null ? "zh_cn" : language;
//    accent = accent == null ? "mandarin" : accent;
//    vadBos = vadBos == null ? "4000" : vadBos;
//    vadEos = vadEos == null ? "1000" : vadEos;
//    asrPtt = asrPtt == null ? "1" : asrPtt;

    //初始化识别无UI识别对象
    //使用SpeechRecognizer对象，可根据回调消息自定义界面；
    SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(currentActivity, i -> promise.resolve(true));
    //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
    mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, cloudGrammar);
    mIat.setParameter(SpeechConstant.SUBJECT, subject);
    //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
    mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);
    //此处engineType为“cloud”
    mIat.setParameter(SpeechConstant.ENGINE_TYPE, engineType);
    //设置语音输入语言，zh_cn为简体中文
    mIat.setParameter(SpeechConstant.LANGUAGE, language);
    //设置结果返回语言
    mIat.setParameter(SpeechConstant.ACCENT, accent);
    // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
    //取值范围{1000～10000}
    mIat.setParameter(SpeechConstant.VAD_BOS, vadBos);
    //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
    //自动停止录音，范围{0~10000}
    mIat.setParameter(SpeechConstant.VAD_EOS, vadEos);
    //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
    mIat.setParameter(SpeechConstant.ASR_PTT, asrPtt);
    //开始识别，并设置监听器
    mIat.startListening(this);
  }

  @Override
  public void onVolumeChanged(int i, byte[] bytes) {
    //todo
  }

  @Override
  public void onBeginOfSpeech() {
    sendEvent("onBeginOfSpeech", null);
  }

  @Override
  public void onEndOfSpeech() {
    sendEvent("onEndOfSpeech", null);
  }

  @Override
  public void onResult(RecognizerResult recognizerResult, boolean b) {
    WritableMap params = Arguments.createMap();
    params.putString("recognizerResult", recognizerResult.getResultString());
    sendEvent("onResult", params);
  }

  @Override
  public void onError(SpeechError speechError) {
    sendEvent("onError", null);
  }

  @Override
  public void onEvent(int i, int i1, int i2, Bundle bundle) {
    //todo
  }

  /**
   * 发送消息到js端
   *
   * @param eventName
   * @param params
   */
  private void sendEvent(String eventName, @Nullable WritableMap params) {
    context
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }

  @Nullable
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("CLOUD_GRAMMAR", SpeechConstant.CLOUD_GRAMMAR);
    constants.put("SUBJECT", SpeechConstant.SUBJECT);
    constants.put("RESULT_TYPE", SpeechConstant.RESULT_TYPE);
    constants.put("ENGINE_TYPE", SpeechConstant.ENGINE_TYPE);
    constants.put("LANGUAGE", SpeechConstant.LANGUAGE);
    constants.put("ACCENT", SpeechConstant.ACCENT);
    constants.put("VAD_BOS", SpeechConstant.VAD_BOS);
    constants.put("VAD_EOS", SpeechConstant.VAD_EOS);
    constants.put("ASR_PTT", SpeechConstant.ASR_PTT);
    return constants;
  }
}

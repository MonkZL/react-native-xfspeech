import { NativeEventEmitter, NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-xfspeech' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const Xfspeech = NativeModules.Xfspeech
  ? NativeModules.Xfspeech
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const eventEmitter = new NativeEventEmitter(NativeModules.Xfspeech);

/**
 * 初始化sdk的方法
 * @param appId
 */
export function init(appId: string): Promise<boolean> {
  return Xfspeech.init(appId);
}

type StartConfigType = {
  cloud_grammar: string;
  subject: string;
  result_type: string;
  engine_type: string;
  language: string;
  accent: string;
  vad_bos: string;
  vad_eos: string;
  asr_ptt: string;
};

/**
 * 开始的方法
 * @param config
 */
export function start(config: Partial<StartConfigType>): Promise<boolean> {
  config.result_type = config.result_type || 'json';
  config.engine_type = config.engine_type || 'cloud';
  config.language = config.language || 'zh_cn';
  config.accent = config.accent || 'mandarin';
  config.vad_bos = config.vad_bos || '4000';
  config.vad_eos = config.vad_eos || '1000';
  config.asr_ptt = config.asr_ptt || '1';
  return Xfspeech.start(config);
}

type NoValueCallBackType = () => void;
type ValueCallBackType = (event: any) => void;

/**
 * 开始的回调
 * @param callback
 */
export function onBeginOfSpeech(callback: NoValueCallBackType) {
  return eventEmitter.addListener('onBeginOfSpeech', callback);
}

/**
 * 结束的回调
 * @param callback
 */
export function onEndOfSpeech(callback: NoValueCallBackType) {
  return eventEmitter.addListener('onEndOfSpeech', callback);
}

/**
 * 返回结果的回调
 * @param callback
 */
export function onResult(callback: ValueCallBackType) {
  return eventEmitter.addListener('onResult', callback);
}

/**
 * 报错的回调
 * @param callback
 */
export function onError(callback: ValueCallBackType) {
  return eventEmitter.addListener('onError', callback);
}

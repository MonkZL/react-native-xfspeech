#import "Xfspeech.h"
#import "IFlySpeechRecognizerDelegate.h"
#import "IFlySpeechRecognizer.h"
#import "IFlySpeechConstant.h"
#import "IFlySpeechUtility.h"

@implementation Xfspeech
RCT_EXPORT_MODULE()

// Example method
// See // https://reactnative.dev/docs/native-modules-ios
RCT_REMAP_METHOD(init,
                 initWithAppid:(NSString *)appId
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    NSString * initIFlytekString = [[NSString alloc] initWithFormat: @"appid=%@", appId];
    
    [IFlySpeechUtility createUtility: initIFlytekString];
    resolve(@YES);
}

RCT_REMAP_METHOD(start,
                 initWithConfig:(NSDictionary *)config
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    if(_iFlySpeechRecognizer != nil && [_iFlySpeechRecognizer isListening]){
        [_iFlySpeechRecognizer cancel];
    }
    
    NSString * cloudGrammar = config[[IFlySpeechConstant CLOUD_GRAMMAR]];
    NSString * subject = config[[IFlySpeechConstant SUBJECT]];
    NSString * resultType = config[[IFlySpeechConstant RESULT_TYPE]];
    NSString * engineType = config[[IFlySpeechConstant ENGINE_TYPE]];
    NSString * language = config[[IFlySpeechConstant LANGUAGE]];
    NSString * accent = config[[IFlySpeechConstant ACCENT]];
    NSString * vadBos = config[[IFlySpeechConstant VAD_BOS]];
    NSString * vadEos = config[[IFlySpeechConstant VAD_EOS]];
    NSString * asrPtt = config[[IFlySpeechConstant ASR_PTT]];

    //创建语音识别对象
    _iFlySpeechRecognizer = [IFlySpeechRecognizer sharedInstance];
    [_iFlySpeechRecognizer setParameter:cloudGrammar forKey:[IFlySpeechConstant CLOUD_GRAMMAR]];
    [_iFlySpeechRecognizer setParameter:subject forKey:[IFlySpeechConstant SUBJECT]];
    [_iFlySpeechRecognizer setParameter:resultType forKey:[IFlySpeechConstant RESULT_TYPE]];
    [_iFlySpeechRecognizer setParameter:engineType forKey:[IFlySpeechConstant ENGINE_TYPE]];
    [_iFlySpeechRecognizer setParameter:language forKey:[IFlySpeechConstant LANGUAGE]];
    [_iFlySpeechRecognizer setParameter:accent forKey:[IFlySpeechConstant ACCENT]];
    [_iFlySpeechRecognizer setParameter:vadBos forKey:[IFlySpeechConstant VAD_BOS]];
    [_iFlySpeechRecognizer setParameter:vadEos forKey:[IFlySpeechConstant VAD_EOS]];
    [_iFlySpeechRecognizer setParameter:asrPtt forKey:[IFlySpeechConstant ASR_PTT]];
    _iFlySpeechRecognizer.delegate = self;
    [_iFlySpeechRecognizer startListening];
    resolve(@YES);
}

RCT_EXPORT_METHOD(cancel) {
    if ([_iFlySpeechRecognizer isListening]) {
        [_iFlySpeechRecognizer cancel];
    }
}

- (NSArray<NSString *> *)supportedEvents{
    return @[@"onBeginOfSpeech",@"onEndOfSpeech",@"onResult",@"onError"];
}

- (void) onBeginOfSpeech{
    [self sendEventWithName: @"onBeginOfSpeech" body: nil];
}

- (void) onEndOfSpeech{
    [self sendEventWithName: @"onEndOfSpeech" body: nil];
}

- (void) onResults:(NSArray *)results isLast:(BOOL)isLast{
    NSMutableString * resultString = [NSMutableString new];
    NSDictionary * dic = results[0];
    
    for (NSString * key in dic) {
        [resultString appendFormat:@"%@",key];
    }

    NSString * resultFromJson = [self stringFromJson:resultString];
    
    [self.result appendString: resultFromJson];
    NSDictionary * result = @{
                              @"recognizerResult": resultFromJson,
                              @"isLast": [NSNumber numberWithBool: isLast]
                              };
    [self sendEventWithName: @"onResult" body: result];
}

- (void)onCompleted:(IFlySpeechError *)errorCode {
    NSDictionary * result = @{
                              @"code": errorCode
                              };
    [self sendEventWithName: @"onError" body: result];
}

- (void) onVolumeChanged: (int)volume {
}

- (void) onEvent:(int)eventType arg0:(int)arg0 arg1:(int)arg1 data:(NSData *)eventData{
}

- (void) onCancel {
}

- (NSString *) stringFromJson: (NSString *) params {
    if (params == NULL) {
        return nil;
    }
    
    NSMutableString *tempStr = [[NSMutableString alloc] init];
    NSDictionary *resultDic  = [NSJSONSerialization JSONObjectWithData:
                                [params dataUsingEncoding:NSUTF8StringEncoding] options:kNilOptions error:nil];
    
    if (resultDic!= nil) {
        NSArray *wordArray = [resultDic objectForKey:@"ws"];
        
        for (int i = 0; i < [wordArray count]; i++) {
            NSDictionary *wsDic = [wordArray objectAtIndex: i];
            NSArray *cwArray = [wsDic objectForKey:@"cw"];
            
            for (int j = 0; j < [cwArray count]; j++) {
                NSDictionary *wDic = [cwArray objectAtIndex:j];
                NSString *str = [wDic objectForKey:@"w"];
                [tempStr appendString: str];
            }
        }
    }
    return tempStr;
}

@end

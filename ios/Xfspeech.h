#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import "lib/iflyMSC.framework/Headers/IFlyMSC.h"

@interface Xfspeech : RCTEventEmitter <RCTBridgeModule, IFlySpeechRecognizerDelegate> 

//不带界面的识别对象
@property (nonatomic, strong) IFlySpeechRecognizer *iFlySpeechRecognizer;

@end

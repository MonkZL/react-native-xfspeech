#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import "IFlySpeechRecognizerDelegate.h"
#import "IFlySpeechRecognizer.h"
#import "IFlySpeechConstant.h"
#import "IFlySpeechUtility.h"
#import "IFlySpeechError.h"


@interface Xfspeech : RCTEventEmitter <RCTBridgeModule, IFlySpeechRecognizerDelegate>

@property (nonatomic, strong) IFlySpeechRecognizer * iFlySpeechRecognizer;
@property (nonatomic) NSTimeInterval startTime;
@property (nonatomic) NSTimeInterval endTime;
@property (nonatomic, strong) NSMutableString * result;

@end

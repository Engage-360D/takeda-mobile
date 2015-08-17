//
//  AlertSimple.h
//  iMedicum
//
//  Created by Alexander Rudenko on 24.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "TKAlertView.h"

@interface AlertSimple : NSObject<UIAlertViewDelegate>

@property(nonatomic, strong) TKAlertView *alertViewTextInputWithButtons;
@property(nonatomic, strong) TKAlertView *alertViewWithButtons;
@property(nonatomic, strong) TKAlertView *alertViewWithOkBtnBlock;
@property(nonatomic, strong) TKAlertView *alertViewSimple;

@property (nonatomic, strong) void (^resultTextBlock) (int index, NSString*str);
@property (nonatomic, strong) void (^resultBlock) (int);
@property (nonatomic, strong) void (^simpleResBlock) (void);

- (void)showMessageWithTextInput:(NSString*)placeholder msg:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns params:(NSDictionary*)params result:(void (^)(int index, NSString*text))ResultBlock;
- (void)showMessage:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns params:(NSDictionary*)params result:(void (^)(int))ResultBlock;
- (void)showMessage:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns result:(void (^)(int))ResultBlock;
- (void)showMessage:(NSString*)msg title:(NSString*)title result:(void (^)(void))ResultBlock;
- (void)showMessage:(NSString*)msg title:(NSString*)title;

@end

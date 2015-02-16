//
//  UIViewController+MMVC.h
//  takeda
//
//  Created by Alexander Rudenko on 26.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIViewController (MMVC)

-(void)showMessageWithTextInput:(NSString*)placeholder msg:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns params:(NSDictionary*)params result:(void (^)(int index, NSString *text))ResultBlock;
-(void)showMessage:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns result:(void (^)(int))ResultBlock;
-(void)showMessage:(NSString*)msg title:(NSString*)title result:(void (^)(void))ResultBlock;
-(void)showMessage:(NSString*)msg title:(NSString*)title;

-(void)showActivityIndicatorWithString:(NSString*)string;
-(void)showActivityIndicatorWithString:(NSString*)string inContainer:(UIView*)container;
-(void)removeActivityIdicator;

@end

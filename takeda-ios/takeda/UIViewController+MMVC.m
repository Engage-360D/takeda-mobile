//
//  UIViewController+MMVC.m
//  takeda
//
//  Created by Alexander Rudenko on 26.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UIViewController+MMVC.h"

@implementation UIViewController (MMVC)

-(void)showMessageWithTextInput:(NSString*)placeholder msg:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns params:(NSDictionary*)params result:(void (^)(int index, NSString *text))ResultBlock{
    __block AlertSimple *als = [AlertSimple new];
    [als showMessageWithTextInput:placeholder msg:msg title:title btns:btns params:(NSDictionary*)params result:^(int indx, NSString *txt ){
        als = nil;
        ResultBlock(indx,txt);
    }];
}

-(void)showMessage:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns params:(NSDictionary*)params result:(void (^)(int))ResultBlock{
    __block AlertSimple *als = [AlertSimple new];
    [als showMessage:msg title:title btns:btns params:params result:^(int index) {
        als = nil;
        ResultBlock(index);
    }];
}


-(void)showMessage:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns result:(void (^)(int))ResultBlock{
    __block AlertSimple *als = [AlertSimple new];
    [als showMessage:msg title:title btns:btns result:^(int index){
        als = nil;
        ResultBlock(index);
    }];
}


-(void)showMessage:(NSString*)msg title:(NSString*)title result:(void (^)(void))ResultBlock{
    __block AlertSimple *als = [AlertSimple new];
    [als showMessage:msg title:title result:^{
        als = nil;
        ResultBlock();
    }];
    
}

-(void)showMessage:(NSString*)msg title:(NSString*)title{
    __block AlertSimple *als = [AlertSimple new];
    [als showMessage:msg title:title result:^{
        als = nil;
    }];
}

-(void)hideKeyb{
    if (Global.keybHolder){
        [Global.keybHolder resignFirstResponder];
        Global.keybHolder = nil;
    }
}


-(void)showActivityIndicatorWithString:(NSString*)string{
    if ([DejalActivityView isActive]) {
        return;
    }
    [DejalBezelActivityView activityViewForView:self.view];
    [DejalActivityView currentActivityView].showNetworkActivityIndicator = YES;
    [DejalActivityView currentActivityView].activityLabel.text = string;
    
}

-(void)showActivityIndicatorWithString:(NSString*)string inContainer:(UIView*)container{
    if ([DejalActivityView isActive]) {
        return;
    }
    [DejalBezelActivityView activityViewForView:container];
    [DejalActivityView currentActivityView].showNetworkActivityIndicator = YES;
    [DejalActivityView currentActivityView].activityLabel.text = string;
    
}


-(void)removeActivityIdicator{
    [DejalBezelActivityView removeViewAnimated:YES];
}


@end

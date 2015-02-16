//
//  AlertSimple.m
//  iMedicum
//
//  Created by Alexander Rudenko on 24.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "AlertSimple.h"

@implementation AlertSimple
@synthesize alertViewTextInputWithButtons;
@synthesize alertViewWithButtons;
@synthesize alertViewWithOkBtnBlock;
@synthesize alertViewSimple;

-(void)showMessageWithTextInput:(NSString*)placeholder msg:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns params:(NSDictionary*)params result:(void (^)(int index, NSString*text))ResultBlock{
    
    alertViewTextInputWithButtons = [[UIAlertView alloc] init];
    
    alertViewTextInputWithButtons.alertViewStyle = UIAlertViewStylePlainTextInput;
    UITextField * alertTextField = [alertViewTextInputWithButtons textFieldAtIndex:0];
    alertTextField.placeholder = placeholder;
    self.resultTextBlock = ResultBlock;
    [alertViewTextInputWithButtons setTitle:title];
    [alertViewTextInputWithButtons setMessage:msg];
    alertViewTextInputWithButtons.delegate = self;
    
    for (NSString *key in params.allKeys){
        
        if ([key isEqualToString:@"secured"]){
            if ([params[key] boolValue]){
                alertViewTextInputWithButtons.alertViewStyle = UIAlertViewStyleSecureTextInput;
            } else {
                alertViewTextInputWithButtons.alertViewStyle = UIAlertViewStylePlainTextInput;
            }
        }
    }
    
    
    
    for (int i = 0; i<btns.count; i++) {
        [alertViewTextInputWithButtons addButtonWithTitle:btns[i]];
    }
    [alertViewTextInputWithButtons show];
}


-(void)showMessage:(NSString*)msg title:(NSString*)title btns:(NSArray*)btns result:(void (^)(int))ResultBlock{
    alertViewWithButtons = [[UIAlertView alloc] init];
    self.resultBlock = ResultBlock;
    [alertViewWithButtons setTitle:title];
    [alertViewWithButtons setMessage:msg];
    alertViewWithButtons.delegate = self;
    
    for (int i = 0; i<btns.count; i++) {
        [alertViewWithButtons addButtonWithTitle:btns[i]];
    }
    //   [alertViewWithButtons setCancelButtonIndex:10];
    [alertViewWithButtons show];
}

-(void)showMessage:(NSString*)msg title:(NSString*)title result:(void (^)(void))ResultBlock{
    alertViewWithOkBtnBlock = [[UIAlertView alloc] init];
    self.simpleResBlock = ResultBlock;
    [alertViewWithOkBtnBlock setTitle:title];
    [alertViewWithOkBtnBlock setMessage:msg];
    [alertViewWithOkBtnBlock addButtonWithTitle:@"Закрыть"];
    
    alertViewWithOkBtnBlock.delegate = self;
    [alertViewWithOkBtnBlock setCancelButtonIndex:0];
    [alertViewWithOkBtnBlock show];
}

-(void)showMessage:(NSString*)msg title:(NSString*)title{
    alertViewSimple = [[UIAlertView alloc] init];
    [alertViewSimple setTitle:title];
    [alertViewSimple setMessage:msg];
    alertViewSimple.delegate = self;
    [alertViewSimple addButtonWithTitle:@"Закрыть"];
    [alertViewSimple setCancelButtonIndex:0];
    [alertViewSimple show];
}



-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (alertView == alertViewWithButtons){
        if (self.resultBlock) self.resultBlock(buttonIndex);
    } else if (alertView == alertViewWithOkBtnBlock){
        if (self.simpleResBlock) self.simpleResBlock();
    } else if (alertView == alertViewTextInputWithButtons){
        NSString *text = [[alertView textFieldAtIndex:0] text];
        if (self.resultTextBlock) self.resultTextBlock(buttonIndex, text);
    } else if (alertView == alertViewSimple){
        
    }
    
}



@end

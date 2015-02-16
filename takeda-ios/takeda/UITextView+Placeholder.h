//
//  UITextView+Placeholder.h
//  iMedicum
//
//  Created by Alexander Rudenko on 28.10.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UITextView (Placeholder)

@property (nonatomic, strong) UITextView *placeholder;

-(void)setupPlaceholder:(NSString*)text withFont:(UIFont*)font;
-(void)textChanged:(UITextView *)textView;
-(void)updatePlaceholder;

@end

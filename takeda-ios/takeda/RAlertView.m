//
//  RAlertView.m
//  takeda
//
//  Created by Alexander Rudenko on 12.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "RAlertView.h"

@implementation RAlertView

- (id)init
{
    self = [[[NSBundle mainBundle] loadNibNamed:@"RAlertView" owner:nil options:nil] lastObject];
    if (self) {
        self.frame = CGRectMake(0, 0, appDelegate.window.width, 110);
    }
    return self;
}

-(void)setupWithTitle:(NSString*)titleTxt text:(NSString*)textTxt img:(NSString*)imageImg{
    self.frame = CGRectMake(0, 0, appDelegate.window.width, 110);
    _titleLabel.font = [UIFont fontWithName:@"SegoeWP-Bold" size:12];
    _titleLabel.textColor = RGB(53, 65, 71);
    _textLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    _textLabel.textColor = RGB(53, 65, 71);
    if (titleTxt) _titleLabel.text = titleTxt;
    if (textTxt) _textLabel.text = textTxt;
    if (imageImg.length>0) _iconView.image = [UIImage imageNamed:imageImg];
    self.backgroundColor = [UIColor clearColor];
}


@end

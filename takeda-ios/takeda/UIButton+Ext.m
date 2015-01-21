//
//  UIButton+Ext.m
//  takeda
//
//  Created by Alexander Rudenko on 21.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UIButton+Ext.h"

@implementation UIButton (Ext)

-(void)setupStandartBordered{
    [self setBackgroundImage:[[UIImage imageNamed:@"button_arrow_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 30)] forState:UIControlStateNormal];
    self.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0];
    [self setTitleColor:RGB(54, 65, 71) forState:UIControlStateNormal];
    self.contentEdgeInsets = UIEdgeInsetsMake(-3, 0, 0, 0);
}

@end

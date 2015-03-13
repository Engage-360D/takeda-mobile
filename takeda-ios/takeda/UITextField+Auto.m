//
//  UITextField+Auto.m
//  mmim
//
//  Created by SashOK on 7/21/14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "UITextField+Auto.h"

@implementation UITextField (Auto)

-(void)setPlaceholderColor:(UIColor*)color{
    if ([self respondsToSelector:@selector(setAttributedPlaceholder:)]) {
        if (self.placeholder)
        self.attributedPlaceholder = [[NSAttributedString alloc] initWithString:self.placeholder attributes:@{NSForegroundColorAttributeName: color}];
    }
}

@end

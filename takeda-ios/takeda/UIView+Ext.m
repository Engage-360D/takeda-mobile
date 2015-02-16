//
//  UIView+Ext.m
//  takeda
//
//  Created by Alexander Rudenko on 21.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UIView+Ext.h"

@implementation UIView (Ext)

-(void)addSeparator{
    UIImageView *sp = [[UIImageView alloc] initWithFrame:CGRectMake(0, self.height - 0.5, self.width, 0.5f)];
    sp.backgroundColor = RGB(170, 170, 170);
    [self addSubview:sp];
}

-(void)addSeparatorColor:(UIColor*)color{
    UIImageView *sp = [[UIImageView alloc] initWithFrame:CGRectMake(0, self.height - 0.5, self.width, 0.5f)];
    sp.backgroundColor = color;
    [self addSubview:sp];
}


-(void)addTopSeparator{
    UIImageView *sp = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.width, 0.5f)];
    sp.backgroundColor = RGB(170, 170, 170);
    [self addSubview:sp];
}

-(void)addTopSeparatorColor:(UIColor*)color{
    UIImageView *sp = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.width, 0.5f)];
    sp.backgroundColor = color;
    [self addSubview:sp];
}


@end

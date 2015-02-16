//
//  CActionSheet.m
//  takeda
//
//  Created by Alexander Rudenko on 02.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "CActionSheet.h"
#define hh 250

@implementation CActionSheet

-(id)initInView:(UIView*)the_view{
    self = [super init];
    self.tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dissmiss)];
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, the_view.width, the_view.height)];
    [self.backView addGestureRecognizer:self.tap];
    self.backView.backgroundColor = [UIColor colorWithWhite:0.5 alpha:0.5];
    self.parent_view = the_view;
    self.height = hh;
    self.frame = CGRectMake(0, the_view.height, the_view.width, self.height);
    return self;
}

-(void)show{
    [self.parent_view addSubview:self.backView];
    self.y = self.parent_view.height;
    
    [self.parent_view addSubview:self];
    [UIView animateWithDuration: 0.3f delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
        self.y = self.parent_view.height-self.height;
        self.alpha = 1.0f;
    } completion:^(BOOL finished){
        
    }];
}

-(void)dissmiss{
    [self.backView removeFromSuperview];
    [UIView animateWithDuration: 0.3f delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
        self.y = self.parent_view.height;
        self.alpha = 0.0f;
    } completion:^(BOOL finished){
        [self removeFromSuperview];
    }];
}



@end

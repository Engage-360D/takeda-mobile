//
//  UIScrollView+Auto.h
//  mmim
//
//  Created by SashOK on 7/17/14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIScrollView (Auto)

-(void)setup_autosize;
-(void)setup_horizontal_autosize;
-(void)setup_autosizeWithBottomDistance:(float)bottomDistance;

@end

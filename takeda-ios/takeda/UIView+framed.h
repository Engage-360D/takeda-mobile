//
//  UIView+framed.h
//  iMedicum
//
//  Created by Alexander Rudenko on 19.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (framed)

@property (nonatomic) CGFloat height;
@property (nonatomic) CGFloat width;
@property (nonatomic) CGFloat x;
@property (nonatomic) CGFloat y;
@property (nonatomic, readonly) CGFloat bottom;
@property (nonatomic, readonly) CGFloat right;

-(void)removeSubviews;
-(void)setupAutosizeBySubviews;
-(void)setupAutosizeWidthBySubviews;
-(void)arrangeViewsVertically;
-(void)arrangeViewsVerticallyWithInterval:(float)interval;
-(void)arrangeViewsHorizontallyWithInterval:(float)interval;
-(void)setupAutosizeBySubviewsWithBottomDistance:(CGFloat)bottomDistance;

@end

//
//  UIView+framed.m
//  iMedicum
//
//  Created by Alexander Rudenko on 19.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "UIView+framed.h"

@implementation UIView (framed)

-(CGFloat)height{
    return self.frame.size.height;
}

-(void)setHeight:(CGFloat)height{
    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, height);
}

-(CGFloat)width{
    return self.frame.size.width;
}

-(void)setWidth:(CGFloat)width{
    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, width, self.frame.size.height);
}

-(CGFloat)x{
    return self.frame.origin.x;
}

-(void)setX:(CGFloat)x{
    self.frame = CGRectMake(x, self.frame.origin.y, self.frame.size.width, self.frame.size.height);
}

-(CGFloat)y{
    return self.frame.origin.y;
}

-(void)setY:(CGFloat)y{
    self.frame = CGRectMake(self.frame.origin.x, y, self.frame.size.width, self.frame.size.height);
}

-(CGFloat)bottom{
    return self.y+self.height;
}

-(CGFloat)right{
    return self.x+self.width;
}

-(CGPoint)middleCenter{
    return CGPointMake(self.width/2, self.height/2);
}


-(void)removeSubviews{
    for (UIView *a in self.subviews){
        [a removeFromSuperview];
    }
}

-(void)setupAutosizeBySubviews{
    float maxHeight = 0;
    for (UIView *view in self.subviews){
        float bottomPointY = view.frame.origin.y+ view.frame.size.height;
        if (maxHeight < bottomPointY){
            maxHeight = bottomPointY;
        }
    }
    self.height = maxHeight;
}

-(void)setupAutosizeWidthBySubviews{
    float maxWidth = 0;
    for (UIView *view in self.subviews){
        float pointX = view.frame.origin.x+ view.frame.size.width;
        if (maxWidth < pointX){
            maxWidth = pointX;
        }
    }
    self.width = maxWidth;
}

-(void)setupAutosizeBySubviewsWithBottomDistance:(CGFloat)bottomDistance{
    float maxHeight = 0;
    for (UIView *view in self.subviews){
        float bottomPointY = view.frame.origin.y+ view.frame.size.height;
        if (maxHeight < bottomPointY){
            maxHeight = bottomPointY;
        }
    }
    self.height = maxHeight+bottomDistance;
}


-(void)arrangeViewsVertically{
    float bottomPointY = 0;
    for (UIView *view in self.subviews){
        view.y = bottomPointY;
        bottomPointY = view.bottom;
    }
}

-(void)arrangeViewsVerticallyWithInterval:(float)interval{
    float bottomPointY = 0;
    for (UIView *view in self.subviews){
        view.y = bottomPointY;
        bottomPointY = view.bottom + ((view != self.subviews.lastObject)?interval:0);
    }
}


-(void)arrangeViewsHorizontallyWithInterval:(float)interval{
    float bottomPointX = 0;
    for (UIView *view in self.subviews){
        view.x = bottomPointX;
        bottomPointX = view.right + ((view != self.subviews.lastObject)?interval:0);
    }
}

@end

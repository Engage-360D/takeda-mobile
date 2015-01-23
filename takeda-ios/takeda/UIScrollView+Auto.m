//
//  UIScrollView+Auto.m
//  mmim
//
//  Created by SashOK on 7/17/14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "UIScrollView+Auto.h"

@implementation UIScrollView (Auto)

-(void)setup_autosize{
    float maxHeight = 0;
    for (UIView *view in self.subviews){
        float bottomPointY = view.frame.origin.y+ view.frame.size.height;
        if (maxHeight < bottomPointY){
            maxHeight = bottomPointY;
        }
    }
    self.contentSize = CGSizeMake(self.bounds.size.width, maxHeight+20);
}

-(void)setup_autosizeWithBottomDistance:(float)bottomDistance{
    float maxHeight = 0;
    for (UIView *view in self.subviews){
        float bottomPointY = view.frame.origin.y+ view.frame.size.height;
        if (maxHeight < bottomPointY){
            maxHeight = bottomPointY;
        }
    }
    self.contentSize = CGSizeMake(self.bounds.size.width, maxHeight+bottomDistance);
}

-(void)setup_horizontal_autosize{
    float maxWidth = 0;
    for (UIView *view in self.subviews){
        float bottomPointX = view.frame.origin.x+ view.frame.size.width;
        if (maxWidth < bottomPointX){
            maxWidth = bottomPointX;
        }
    }
    self.contentSize = CGSizeMake(maxWidth, self.bounds.size.height);
}

@end

//
//  UIView+ViewsParentUIViewController.m
//  takeda
//
//  Created by Alexander Rudenko on 20.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UIView+ViewsParentUIViewController.h"

@implementation UIView (ViewsParentUIViewController)

- (UIViewController *) firstAvailableUIViewController {
    // convenience function for casting and to "mask" the recursive function
    return (UIViewController *)[self traverseResponderChainForUIViewController];
}

- (id) traverseResponderChainForUIViewController {
    id nextResponder = [self nextResponder];
    if ([nextResponder isKindOfClass:[UIViewController class]]) {
        return nextResponder;
    } else if ([nextResponder isKindOfClass:[UIView class]]) {
        return [nextResponder traverseResponderChainForUIViewController];
    } else {
        return nil;
    }
}

@end

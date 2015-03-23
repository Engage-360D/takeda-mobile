//
//  UIView+ViewsParentUIViewController.h
//  takeda
//
//  Created by Alexander Rudenko on 20.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (ViewsParentUIViewController)

- (UIViewController *) firstAvailableUIViewController;
- (id) traverseResponderChainForUIViewController;

@end

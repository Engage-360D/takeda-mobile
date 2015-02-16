//
//  RAlertView.h
//  takeda
//
//  Created by Alexander Rudenko on 12.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RAlertView : UIView
@property (nonatomic, strong) IBOutlet UILabel *titleLabel;
@property (nonatomic, strong) IBOutlet UITextView *textLabel;
@property (nonatomic, strong) IBOutlet UIImageView *iconView;

- (id)init;
- (void)setupWithTitle:(NSString*)titleTxt text:(NSString*)textTxt img:(NSString*)imageImg;

@end

//
//  ForgetPage.h
//  takeda
//
//  Created by Serg on 3/31/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PLTextField.h"


@interface ForgetPage : UIViewController
@property (nonatomic,retain) IBOutlet UIButton *btn_send;
@property (nonatomic,retain) IBOutlet UIView *bg_block;
@property (nonatomic,retain) IBOutlet PLTextField *email_field;
@end

//
//  AuthPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PLTextField.h"


@interface AuthPage : UIViewController
@property (nonatomic,retain) IBOutletCollection(UIView) NSArray *bg_block;
@property (nonatomic,retain) IBOutlet PLTextField *email_field;
@property (nonatomic,retain) IBOutlet PLTextField *pass_field;

@property (nonatomic,retain) IBOutlet UIButton *login_btn;
@property (nonatomic,retain) IBOutlet UIButton *forget_btn;
@property (nonatomic,retain) IBOutlet UIButton *registration_btn;

@property (nonatomic,retain) IBOutlet UILabel *description_text;

@property (nonatomic,retain) IBOutlet UINavigationBar *nav_bar;

@end

//
//  RegistrationPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PLTextField.h"


@interface RegistrationPage : UIViewController
@property (nonatomic,retain) IBOutlet UIScrollView *scrollView;
@property (nonatomic,retain) IBOutlet UIButton *btn_register;
@property (nonatomic,retain) IBOutletCollection(UIView) NSArray *bg_block;

@property (nonatomic,retain) IBOutlet PLTextField *name_field;
@property (nonatomic,retain) IBOutlet PLTextField *email_field;
@property (nonatomic,retain) IBOutlet PLTextField *pass_field;
@end

//
//  AuthPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PLTextField.h"
#import "Vkontakte.h"
#import "OKRequest.h"
#import "OKSession.h"
#import "Odnoklassniki.h"


@interface AuthPage : VControllerExt <VkontakteDelegate,OKSessionDelegate, OKRequestDelegate>{
    Vkontakte *_vkontakte;
}

@property (nonatomic,retain) IBOutletCollection(UIView) NSArray *bg_block;
@property (nonatomic,retain) IBOutlet PLTextField *email_field;
@property (nonatomic,retain) IBOutlet PLTextField *pass_field;

@property (nonatomic,retain) IBOutlet UIButton *login_btn;
@property (nonatomic,retain) IBOutlet UIButton *forget_btn;
@property (nonatomic,retain) IBOutlet UIButton *registration_btn;

@property (nonatomic,retain) IBOutlet UILabel *description_text;
@property (nonatomic,retain) IBOutlet UILabel *danger_text;
@property (nonatomic,retain) IBOutlet UIScrollView *scrollView;

@property (nonatomic,retain) IBOutlet UINavigationBar *nav_bar;
@property (nonatomic,retain) IBOutlet UIView *containerView;

@end

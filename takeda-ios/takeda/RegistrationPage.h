//
//  RegistrationPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PLTextField.h"
#import "Vkontakte.h"
#import "Odnoklassniki.h"

@interface RegistrationPage : UIViewController<UIPickerViewDelegate, UIPickerViewDataSource,VkontakteDelegate,OKSessionDelegate, OKRequestDelegate>{
    Vkontakte *_vkontakte;
}

@property (nonatomic,retain) IBOutlet UIScrollView *scrollView;
@property (nonatomic,retain) IBOutlet UIButton *btn_register;
@property (nonatomic,retain) IBOutletCollection(UIView) NSArray *bg_block;

@property (nonatomic,retain) IBOutlet PLTextField *name_field;
@property (nonatomic,retain) IBOutlet PLTextField *email_field;
@property (nonatomic,retain) IBOutlet PLTextField *pass_field;


@property bool user_is_doctor;
@property bool user_is_agree_personal_data;
@property bool user_is_agree_email_subscribe_data;
@property bool user_is_agree_information_is_recomemd_style;

@property (nonatomic,retain) IBOutlet UIButton *btn_region;
@property (nonatomic,retain) IBOutlet UIButton *btn_birthday;



@property (nonatomic,retain) IBOutlet UIImageView *user_is_doctor_img;
@property (nonatomic,retain) IBOutlet UIImageView *user_is_agree_personal_data_img;
@property (nonatomic,retain) IBOutlet UIImageView *user_is_agree_email_subscribe_data_img;
@property (nonatomic,retain) IBOutlet UIImageView *user_is_agree_information_is_recomemd_style_img;
@end

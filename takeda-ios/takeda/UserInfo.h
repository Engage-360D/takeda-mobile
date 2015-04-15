//
//  UserInfo.h
//  takeda
//
//  Created by Alexander Rudenko on 03.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DPicker.h"
#import "AllSocials.h"

@interface UserInfo : VControllerExt<OKSessionDelegate, OKRequestDelegate, VKSdkDelegate, GPPSignInDelegate>


@property (nonatomic, strong) NSDate *birthdayDate;
@property (nonatomic) float experience;
@property (nonatomic) int region;
@property (nonatomic) BOOL receiveSpam;
@property (nonatomic, strong) NSDate *educYearDate;



@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;

@property (nonatomic,retain) IBOutletCollection(UIView) NSArray *bg_block;
@property (nonatomic,retain) IBOutletCollection(UILabel) NSArray *blockLabels;
@property (nonatomic,retain) IBOutletCollection(UIButton) NSArray *fontButtons;
@property (nonatomic,retain) IBOutletCollection(PLTextField) NSArray *textFields;

@property (nonatomic,retain) IBOutlet PLTextField *name_field;
@property (nonatomic,retain) IBOutlet PLTextField *last_name_field;
@property (nonatomic,retain) IBOutlet PLTextField *email_field;
@property (nonatomic,retain) IBOutlet PLTextField *pass_field;
@property (nonatomic,retain) IBOutlet PLTextField *spec_field;
@property (nonatomic,retain) IBOutlet PLTextField *address_field;
@property (nonatomic,retain) IBOutlet PLTextField *phone_field;
@property (nonatomic,retain) IBOutlet PLTextField *educ_institute_field;
@property (nonatomic,retain) IBOutlet UIButton *forgot_pass_btn;
@property (nonatomic,retain) IBOutlet UIButton *region_select_btn;
@property (nonatomic,retain) IBOutlet UIButton *birthday_btn;
@property (nonatomic,retain) IBOutlet UIButton *experience_btn;
@property (nonatomic,retain) IBOutlet UIButton *educ_year_btn;
@property (nonatomic,retain) IBOutlet UIButton *saveBtn;
@property (nonatomic,retain) IBOutlet UIView *socContainer;
@property (nonatomic,retain) IBOutlet UIView *docFieldsContainer;
@property (nonatomic,retain) IBOutlet UIView *bottomContainer;
@property (nonatomic,retain) IBOutlet UIImageView *receiveSpamCheck;

@end

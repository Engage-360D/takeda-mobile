//
//  UserInfo.m
//  takeda
//
//  Created by Alexander Rudenko on 03.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UserInfo.h"

@interface UserInfo (){
    NSMutableArray *regions_data;
    NSMutableArray *usedSocials;
}

@end

@implementation UserInfo

- (void)viewDidLoad {
    [super viewDidLoad];
    self.mainElement = self.scrollView;
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
    [self showInfo];
    [self.scrollView setup_autosize];
}

-(void)initData{
    regions_data = [GlobalData regionsList];
    [ServData loadRegionsWithCompletion:^(BOOL success, id result){
        if (success){
            regions_data = [GlobalData regionsList];
        }
    }];
}


-(void)setupInterface{
    
    [self drawBorders:self.bg_block];
    
    for (UILabel *lb in self.blockLabels){
        lb.font = [UIFont fontWithName:@"SegoeWP-Light" size:17];
    }
    
    for (UIButton *bb in self.fontButtons){
        bb.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    }
    
    for (PLTextField *tf in self.textFields){
        tf.placeholderColor = RGB(53, 65, 71);
        tf.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
        tf.font = [UIFont fontWithName:@"SegoeWP" size:14.0];
    }
    
    self.saveBtn.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0];
    self.saveBtn.layer.borderColor = RGB(53, 65, 71).CGColor;
    self.saveBtn.layer.borderWidth = 0.5f;
    self.saveBtn.layer.cornerRadius = 5.0f;
}

-(void)showInfo{
    self.name_field.text = User.userData[@"firstname"];
    self.last_name_field.text = User.userData[@"lastname"];
    self.email_field.text = User.userData[@"email"];

    self.spec_field.text = User.userData[@"specializationName"];
    self.phone_field.text = User.userData[@"phoneNumber"];
    self.educ_institute_field.text = User.userData[@"specializationInstitutionName"];
    self.spec_field.text = User.userData[@"specializationName"];
    self.address_field.text = User.userData[@"specializationInstitutionAddress"];
    self.birthdayDate = (User.userData[@"birthday"])?[Global parseDateTime:User.userData[@"birthday"]]:nil;
    self.experience = (User.userData[@"specializationExperienceYears"])?[User.userData[@"specializationExperienceYears"] floatValue]:NSNotFound;
    self.educYearDate = (User.userData[@"specializationGraduationDate"])?[Global parseDateTime:User.userData[@"specializationGraduationDate"]]:nil;
    self.region = (User.userData[@"links"][@"region"])?[User.userData[@"links"][@"region"] intValue]:NSNotFound;
    self.receiveSpam = [User.userData[@"isSubscribed"] boolValue];
    
    usedSocials = [NSMutableArray new];
    
//    User.userData[@"vkontakteId"]= @"1";
//    User.userData[@"facebookId"]= @"1";
//    User.userData[@"odnoklassnikiId"]= @"1";
    
    if (User.userData[@"vkontakteId"]){
        [usedSocials addObject:@{@"id":User.userData[@"vkontakteId"],@"type":[NSNumber numberWithInt:sVK]}];
    }
    if (User.userData[@"facebookId"]){
        [usedSocials addObject:@{@"id":User.userData[@"facebookId"],@"type":[NSNumber numberWithInt:sFB]}];
    }
    
    if (User.userData[@"odnoklassnikiId"]){
        [usedSocials addObject:@{@"id":User.userData[@"odnoklassnikiId"],@"type":[NSNumber numberWithInt:sOK]}];
    }
    
    [self showSocials];
    
    if ([User checkForRole:tDoctor]){
        [self.docFieldsContainer setupAutosizeBySubviews];
        self.docFieldsContainer.hidden = NO;
        self.bottomContainer.y = self.docFieldsContainer.bottom;
        
    } else {
        self.docFieldsContainer.height = 0;
        self.docFieldsContainer.hidden = YES;
        self.bottomContainer.y = self.docFieldsContainer.y;
    }
    [self.scrollView setup_autosize];
}

-(IBAction)saveAction:(id)sender{
    
    NSString *name = self.name_field.text;
    NSString *email = self.email_field.text;
    NSString *lastName = self.last_name_field.text;
    NSString *instAddress = self.address_field.text;
    NSString *instName = self.educ_institute_field.text;
    NSString *instPhone = self.phone_field.text;
    NSString *specialization = self.spec_field.text;

    NSDictionary *params = @{          @"email": email,
                                       @"firstname": name,
                                       @"lastname" : lastName,
                                       @"birthday":[Global strDateTime: _birthdayDate],
                                       @"specializationExperienceYears" : _experience!=NSNotFound?[NSNumber numberWithFloat:_experience]:[NSNull null],
                                       @"specializationGraduationDate" : _educYearDate?[Global strDateTime:_educYearDate]:[NSNull null],
                                       @"specializationInstitutionAddress" : instAddress.length>0?instAddress:[NSNull null],
                                       @"specializationInstitutionName" : instName.length>0?instName:[NSNull null],
                                       @"specializationInstitutionPhone" : instPhone.length>0?instPhone:[NSNull null],
                                       @"specializationName" : specialization.length>0?specialization:[NSNull null]
                                    //   @"isSubscribed" : [NSNumber numberWithBool:_receiveSpam],
                                    //   @"links": @{@"region":[NSString stringWithFormat:@"%i", _region]}
                                       };
    [ServData updateUser:User.user_id withData:params completion:^(BOOL result, NSError *error, NSString* textError){
        if (result){
            [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
                [User setCurrentUser:User.user_login];
                if (result){
                    NSLog(@"userData - %@",[[UserData sharedObject] userData]);
                    UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
                    [vc.navigationController setNavigationBarHidden:NO];
                    [ApplicationDelegate setRootViewController:vc];
                } else {
                    [Helper fastAlert:@"Ошибка загрузки данных пользователя"];
                }
            }];
        } else {
            [Helper fastAlert:@"Ошибка сохранения"];
        }
        
    }];
    
    
    

}

/*
 "specializationExperienceYears"
 "specializationGraduationDate"
 "specializationInstitutionAddress"
 "specializationInstitutionName"
 "specializationInstitutionPhone"
 "specializationName"
 */


-(IBAction)changePass:(id)sender{
    [self showActivityIndicatorWithString:@"" inContainer:appDelegate.window];
    [ServData resetUserPassword:User.user_login withCompletion:^(BOOL success, NSError* error){
        [self removeActivityIdicator];
        [self showMessage:@"На вашу почту было отправлено письмо с новым паролем" title:@"Сброс пароля"];
    }];
}

-(IBAction)selectExperience:(id)sender{
    NSMutableArray *expArray = [NSMutableArray new];
//    for (float i = 0; i<50; i+=1){
//        [expArray addObject:[NSNumber numberWithFloat:i]];
//    }
    [expArray fillIntegerFrom:0 to:50 step:1];
    DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.parentVC.view completition:^(BOOL apply, int index){
        if (apply){
            self.experience = [expArray[index] floatValue];
        }
    }];
    [exPicker preselectValue:[NSNumber numberWithFloat:self.experience]];
    [exPicker show];
}

-(IBAction)changeDateBirthday:(id)sender{
    DPicker *exPicker = [[DPicker alloc] initDateinView:self.parentVC.view completition:^(BOOL apply, NSDate *selDate){
        if (apply){
            self.birthdayDate = selDate;
        }
        
    }];
    
    [exPicker preselectValue:_birthdayDate];
    [exPicker show];

}

-(IBAction)changeFinishDateEducation:(id)sender{
    DPicker *exPicker = [[DPicker alloc] initDateinView:self.parentVC.view completition:^(BOOL apply, NSDate *selDate){
        if (apply){
            self.educYearDate = selDate;
        }
    }];
    
    [exPicker preselectValue:_educYearDate];
    [exPicker show];
    
}

-(IBAction)changeRegion:(id)sender{
    
    DPicker *exPicker = [[DPicker alloc] initListWithArray:[regions_data valueForKey:@"name"] inView:self.parentVC.view completition:^(BOOL apply, int index){
        if (apply){
            self.region = [regions_data[index][@"id"] intValue];
        }
    }];
    
    NSMutableDictionary *dd = [Global dictionaryWithValue:[NSNumber numberWithInt:self.region] ForKey:@"id" InArray:regions_data];
    [exPicker preselectValue:dd[@"name"]];
    [exPicker show];

}


-(void)showSocials{
    [self.socContainer removeSubviews];
    for (int i = 0; i<usedSocials.count; i++){
        [self.socContainer addSubview:[self btnForSocial:[usedSocials[i][@"type"] intValue]]];
    }
    [self.socContainer arrangeViewsHorizontallyWithInterval:3];
}

-(void)setReceiveSpam:(BOOL)receiveSpam{
    _receiveSpam = receiveSpam;
    self.receiveSpamCheck.highlighted = receiveSpam;
}

-(void)setBirthdayDate:(NSDate *)birthdayDate{
    _birthdayDate = birthdayDate;
    [self.birthday_btn setTitle:[NSString stringWithFormat:@"Дата рождения: %@",[birthdayDate stringWithFormat:@"dd MMMM yyyy"]] forState:UIControlStateNormal];
}

-(void)setExperience:(float)experience{
    _experience = experience;
    [self.experience_btn setTitle:[NSString stringWithFormat:@"Стаж:  %@",experience!=NSNotFound?[NSString stringWithFormat:@"%.0f", experience]:@"-"] forState:UIControlStateNormal];
}

-(void)setEducYearDate:(NSDate *)educYearDate{
    _educYearDate = educYearDate;
    [self.educ_year_btn setTitle:[NSString stringWithFormat:@"Год окончания: %@",educYearDate?[educYearDate stringWithFormat:@"dd MMMM yyyy"]:@"-"] forState:UIControlStateNormal];
}

-(void)setRegion:(int)region{
    _region = region;
    NSMutableDictionary *dd = [Global dictionaryWithValue:[NSNumber numberWithInt:region] ForKey:@"id" InArray:regions_data];
    [self.region_select_btn setTitle:[NSString stringWithFormat:@"Регион: %@", dd?[NSString stringWithFormat:@"%@", dd[@"name"]]:@"-"] forState:UIControlStateNormal];
}

-(UIButton*)btnForSocial:(SocialServices)socServ{
    UIImage *img;
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 34, 34)];
    
    switch (socServ) {
        case sVK:{
            img = [UIImage imageNamed:@"vk_logo"];
            break;
        }
        case sFB:{
            img = [UIImage imageNamed:@"fb_logo"];
            break;
        }
        case sOK:{
            img = [UIImage imageNamed:@"ok_logo"];
            break;
        }
    }
    
    [btn setImage:img forState:UIControlStateNormal];
    btn.tag = socServ;
    return btn;
}

-(IBAction)spamSwitch:(id)sender{
    self.receiveSpam = !self.receiveSpam;
}

@end

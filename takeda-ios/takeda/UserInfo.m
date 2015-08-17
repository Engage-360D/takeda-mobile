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
    
    NSString *vkId;
    NSString *vkToken;
    NSString *vkFullName;
    
    NSString *fbId;
    NSString *fbToken;
    NSString *fbFullName;

    NSString *okId;
    NSString *okToken;
    NSString *okFullName;

    NSString *gpId;
    NSString *gpToken;
    NSString *gpFullName;

    
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
    regions_data = [GData regionsList];
    [ServData loadRegionsWithCompletion:^(BOOL success, id result){
        if (success){
            regions_data = [GData regionsList];
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

-(void)updateSocialList{
    usedSocials = [NSMutableArray new];
    
    if ((User.userData[@"vkontakteId"]&&[[NSString stringWithFormat:@"%@",User.userData[@"vkontakteId"]] length]>0)||vkToken.length>0){
        [usedSocials addObject:@{@"id":User.userData[@"vkontakteId"]?User.userData[@"vkontakteId"]:vkId,@"type":[NSNumber numberWithInt:sVK]}];
    }
    if ((User.userData[@"facebookId"]&&[[NSString stringWithFormat:@"%@",User.userData[@"facebookId"]] length])||fbToken.length>0){
        [usedSocials addObject:@{@"id":User.userData[@"facebookId"]?User.userData[@"facebookId"]:fbId,@"type":[NSNumber numberWithInt:sFB]}];
    }
    
    if ((User.userData[@"odnoklassnikiId"]&&[[NSString stringWithFormat:@"%@",User.userData[@"odnoklassnikiId"]] length]>0)||okToken.length>0){
        [usedSocials addObject:@{@"id":User.userData[@"odnoklassnikiId"]?User.userData[@"odnoklassnikiId"]:okId,@"type":[NSNumber numberWithInt:sOK]}];
    }
    
    if ((User.userData[@"googleId"]&&[[NSString stringWithFormat:@"%@",User.userData[@"googleId"]] length]>0)||gpToken.length>0){
        [usedSocials addObject:@{@"id":User.userData[@"googleId"]?User.userData[@"googleId"]:gpId,@"type":[NSNumber numberWithInt:sGp]}];
    }
    
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

    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithDictionary:
                                    @{ @"email": email,
                                       @"firstname": name,
                                       @"lastname" : lastName,
                                       @"birthday":[Global strDateTime: _birthdayDate],
                                       @"specializationExperienceYears" : _experience!=NSNotFound?[NSNumber numberWithFloat:_experience]:[NSNull null],
                                       @"specializationGraduationDate" : _educYearDate?[Global strDateTime:_educYearDate]:[NSNull null],
                                       @"specializationInstitutionAddress" : instAddress.length>0?instAddress:[NSNull null],
                                       @"specializationInstitutionName" : instName.length>0?instName:[NSNull null],
                                       @"specializationInstitutionPhone" : instPhone.length>0?instPhone:[NSNull null],
                                       @"specializationName" : specialization.length>0?specialization:[NSNull null],
                                    //   @"isSubscribed" : [NSNumber numberWithBool:_receiveSpam],
                                       @"links": @{@"region":[NSString stringWithFormat:@"%i", _region]}
                                       }];
    
    if (vkId) [params setObject:vkId forKey:@"vkontakteId"];
    if (vkToken) [params setObject:vkToken forKey:@"vkontakteToken"];
    if (fbId) [params setObject:fbId forKey:@"facebookId"];
    if (fbToken) [params setObject:fbToken forKey:@"facebookToken"];
    if (okId) [params setObject:okId forKey:@"odnoklassnikiId"];
    if (okToken) [params setObject:okToken forKey:@"odnoklassnikiToken"];
    if (gpId) [params setObject:gpId forKey:@"googleId"];
    if (gpToken) [params setObject:gpToken forKey:@"googleToken"];

    
    [self showActivityIndicatorWithString:@"Сохранение"];
    [ServData updateUser:User.user_id withData:params completion:^(BOOL result, NSError *error, NSString* textError){
        vkId = nil;
        vkToken = nil;
        vkFullName = nil;
        fbId = nil;
        fbToken = nil;
        fbFullName = nil;
        okId = nil;
        okToken = nil;
        okFullName = nil;
        gpId = nil;
        gpToken = nil;
        gpFullName = nil;

        [self removeActivityIdicator];
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
    exPicker.unitCaption = @"лет";
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
    [self updateSocialList];
    [self.socContainer removeSubviews];
    for (int i = 0; i<usedSocials.count; i++){
        [self.socContainer addSubview:[self btnForSocial:[usedSocials[i][@"type"] intValue]]];
    }
    [self.socContainer arrangeViewsHorizontallyWithInterval:2];
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
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 35, 35)];
    
    switch (socServ) {
        case sVK:{
            img = [UIImage imageNamed:@"vk_logo_dark"];
            break;
        }
        case sFB:{
            img = [UIImage imageNamed:@"fb_logo_dark"];
            break;
        }
        case sOK:{
            img = [UIImage imageNamed:@"ok_logo_dark"];
            break;
        }
        case sGp:{
            img = [UIImage imageNamed:@"gp_logo_dark"];
            break;
        }

    }
    
    [btn setImage:img forState:UIControlStateNormal];
    btn.tag = socServ;
    [btn addTarget:self action:@selector(usedSocialAction:) forControlEvents:UIControlEventTouchUpInside];
    return btn;
}

-(void)usedSocialAction:(UIButton*)sender{
    NSString *msgTitle = [self nameSocial:sender.tag];
    NSString *msgText;
    NSString *socKey;

    switch (sender.tag) {
        case sVK:{
            msgText = vkFullName;
            socKey = @"vkontakteId";

            break;
        }
        case sFB:{
            msgText = fbFullName;
            socKey = @"facebookId";

            break;
        }
        case sOK:{
            msgText = okFullName;
            socKey = @"odnoklassnikiId";

            break;
        }
        case sGp:{
            msgText = gpFullName;
            socKey = @"googleId";

            break;
        }
            
    }

    if (User.userData[socKey]!=nil){
        return;
    }
    NSArray *btns = @[@"Закрыть",@"Удалить",@"Выйти"];
    
    [self showMessage:msgText title:msgTitle btns:btns result:^(int result){
        switch (result) {
            case 0:{
                
                break;
            }
            case 1:{
                [self deleteSocNetwork:sender.tag];
                break;
            }
            case 2:{
                [self logoutSocial:sender.tag];
                break;
            }
        }
        
    }];
}

-(void)logoutSocial:(SocialServices)socType{
    switch (socType) {
            
        case sVK:{
            vkId = nil;
            vkToken = nil;
            vkFullName = nil;
            [VKSdk forceLogout];
            break;
        }
        case sFB:{
            fbId = nil;
            fbToken = nil;
            fbFullName = nil;
            [FBSession.activeSession closeAndClearTokenInformation];
            break;
        }
        case sOK:{
            okId = nil;
            okToken = nil;
            okFullName = nil;
            [User.ok_api logout];
            break;
        }
        case sGp:{
            gpId = nil;
            gpToken = nil;
            gpFullName = nil;
            [[GPPSignIn sharedInstance] signOut];
        }
    }
}


-(void)deleteSocNetwork:(SocialServices)socType{
    switch (socType) {
            
        case sVK:{
            vkId = nil;
            vkToken = nil;
            vkFullName = nil;
            break;
        }
        case sFB:{
            fbId = nil;
            fbToken = nil;
            fbFullName = nil;
            break;
        }
        case sOK:{
            okId = nil;
            okToken = nil;
            okFullName = nil;
            break;
        }
        case sGp:{
            gpId = nil;
            gpToken = nil;
            gpFullName = nil;
        }
    }
    [self showSocials];
}

-(NSString*)nameSocial:(SocialServices)socType{
    switch (socType) {
            
        case sVK:{
            return @"ВКонтакте";
            break;
        }
        case sFB:{
            return @"Фейсбук";
            break;
        }
        case sOK:{
            return @"Одноклассники";
            break;
        }
        case sGp:{
            return @"Google+";
        }
    }

}

-(IBAction)spamSwitch:(id)sender{
    self.receiveSpam = !self.receiveSpam;
}

-(IBAction)addedSocialAction:(UIButton*)sender{
    SocialServices socType = sender.tag;
    
    [self showMessage:@"Выберите действие" title:[self nameSocial:socType] btns:@[@"Отмена",@"Удалить"] result:^(int result){
        NSString *socKey;
        
        switch (socType) {
                
            case sVK:{
                socKey = @"vkontakteId";
                break;
            }
            case sFB:{
                socKey = @"facebookId";
                break;
            }
            case sOK:{
                socKey = @"odnoklassnikiId";
                break;
            }
            case sGp:{
                socKey = @"googleId";
                break;
            }

        }

        
        switch (result) {
                
            case 0:{

                break;
            }
            case 1:{
                [User.userData setObject:@"" forKey:socKey];
                // удалить соцсеть
                
                [self showInfo];
                break;
            }
            case 2:{
                
                break;
            }
        }
    }];
    
}

#pragma mark - Social Networks


#pragma mark - FACEBOOK

-(IBAction)loginWithFb:(id)sender{
    ShowNetworkActivityIndicator();
    if (FBSession.activeSession.isOpen) {
        // login is integrated with the send button -- so if open, we send
        [self sendRequests];
    } else {
        NSArray *permissions = FB_SCOPE;
        [FBSession openActiveSessionWithReadPermissions:permissions
                                           allowLoginUI:YES
                                      completionHandler:^(FBSession *session,
                                                          FBSessionState status,
                                                          NSError *error) {
                                          // if login fails for any reason, we alert
                                          if (error) {
                                              UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                                              message:error.localizedDescription
                                                                                             delegate:nil
                                                                                    cancelButtonTitle:@"OK"
                                                                                    otherButtonTitles:nil];
                                              [alert show];
                                          } else if (FB_ISSESSIONOPENWITHSTATE(status)) {
                                              [self sendRequests];
                                          }
                                      }];
    }
}



- (void)sendRequests {
    NSArray *fbids = @[@"me"];
    FBRequestConnection *newConnection = [[FBRequestConnection alloc] init];
    for (NSString *fbid in fbids) {
        FBRequestHandler handler =
        ^(FBRequestConnection *connection, id result, NSError *error) {
            // output the results of the request
            [self requestCompleted:connection forFbID:fbid result:result error:error];
        };
        FBRequest *request = [[FBRequest alloc] initWithSession:FBSession.activeSession
                                                      graphPath:fbid];
        [newConnection addRequest:request completionHandler:handler];
    }
    [User.requestConnection cancel];
    
    User.requestConnection = newConnection;
    [newConnection start];
}

// Report any results.  Invoked once for each request we make.
- (void)requestCompleted:(FBRequestConnection *)connection
                 forFbID:fbID
                  result:(id)result
                   error:(NSError *)error {
    HideNetworkActivityIndicator();
    // not the completion we were looking for...
    if (User.requestConnection &&
        connection != User.requestConnection) {
        return;
    }
    
    // clean this up, for posterity
    User.requestConnection = nil;
    
    NSString *text;
    if (error) {
        text = error.localizedDescription;
    } else {
        NSDictionary *dictionary = (NSDictionary *)result;
        [self setUserDataFromFB:dictionary];
        text = (NSString *)[dictionary objectForKey:@"name"];
    }
    /*
     NSLog(@"%@",[NSString stringWithFormat:@"%@: %@\r\n",[fbID stringByTrimmingCharactersInSet:
     [NSCharacterSet whitespaceAndNewlineCharacterSet]],
     text]);*/
}

- (void)viewDidUnload {
    [User.requestConnection cancel];
    User.requestConnection = nil;
}

-(void)setUserDataFromFB:(NSDictionary*)userData{
    
    NSMutableString *fbFullNameStr = [NSMutableString new];
    
    if ([userData hasKey:@"first_name"] ) {
        if ([[userData objectForKey:@"first_name"] length]>0) {
            [fbFullNameStr appendFormat:@"%@",userData[@"first_name"]];
            if (self.name_field.text.length==0){
                self.name_field.text = [userData objectForKey:@"first_name"];
            }
        }
    }
    
    if ([userData hasKey:@"last_name"] ) {
        if ([[userData objectForKey:@"last_name"] length]>0) {
            [fbFullNameStr appendFormat:@"%@ ",userData[@"last_name"]];
            if (self.last_name_field.text.length==0){
                self.last_name_field.text = [userData objectForKey:@"last_name"];
            }
        }
    }

    if ([userData hasKey:@"email"] ) {
        if ([[userData objectForKey:@"email"] length]>0) {
            if (self.email_field.text.length==0){
                self.email_field.text = [userData objectForKey:@"email"];
            }
        }
    }
    
    if ([userData hasKey:@"birthday"] ) {
        if ([[userData objectForKey:@"birthday"] length]>0) {
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"yyyy-MM-dd"];
            NSDate *dateFromString = [[NSDate alloc] init];
            dateFromString = [dateFormatter dateFromString:[userData objectForKey:@"birthday"]];
            if (dateFromString) {
                if (!_birthdayDate){
                    self.birthdayDate = dateFromString;
                }
            }
        }
    }
    
    fbId = [userData objectForKey:@"id"];
    fbToken = FBSession.activeSession.accessTokenData.accessToken;
    fbFullName = fbFullNameStr;
    
    [self showSocials];

}








#pragma mark -
#pragma mark - VK

- (IBAction)loginWithVK:(id)sender
{
    
    [VKSdk initializeWithDelegate:self andAppId:vkAppId];
    [VKSdk authorize:VK_SCOPE revokeAccess:YES];

//    if ([VKSdk wakeUpSession])
//    {
//        [self getVKUserInfo];
//    } else {
//        [VKSdk authorize:VK_SCOPE revokeAccess:YES];
//    }
//    
}


-(void)getVKUserInfo{
    VKRequest *request = [[VKApi users] get:@{@"fields":@"bdate, sex"}];
    [request executeWithResultBlock: ^(VKResponse *response) {
        NSDictionary *dict;
        
        if ([response.json isKindOfClass:[NSArray class]]&&[response.json count]>0){
            dict = response.json[0];
        } else if ([response.json isKindOfClass:[NSDictionary class]]){
            dict = response.json;
        }
        
        NSLog(@"Result: %@", response);
        [self vkontakteDidFinishGettinUserInfo:dict];
    } errorBlock: ^(NSError *error) {
        
    }];
    
}

- (void)vkSdkNeedCaptchaEnter:(VKError *)captchaError {
    VKCaptchaViewController *vc = [VKCaptchaViewController captchaControllerWithError:captchaError];
    [vc presentIn:self.navigationController.topViewController];
}

- (void)vkSdkTokenHasExpired:(VKAccessToken *)expiredToken {
    [VKSdk authorize:VK_SCOPE revokeAccess:YES];
}

- (void)vkSdkReceivedNewToken:(VKAccessToken *)newToken {
    [self getVKUserInfo];
}

- (void)vkSdkShouldPresentViewController:(UIViewController *)controller {
    [self.navigationController.topViewController presentViewController:controller animated:YES completion:nil];
}

- (void)vkSdkAcceptedUserToken:(VKAccessToken *)token {
    [self getVKUserInfo];
}
- (void)vkSdkUserDeniedAccess:(VKError *)authorizationError {
    [VKSdk initializeWithDelegate:self andAppId:vkAppId];
    [VKSdk authorize:VK_SCOPE revokeAccess:YES];
}
-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    [self.navigationController popToRootViewControllerAnimated:YES];
}


- (void)vkontakteDidFinishGettinUserInfo:(NSDictionary *)info{
    
    NSMutableString *vkFullNameStr = [NSMutableString new];

    if ([info hasKey:@"first_name"] ) {
        if ([[info objectForKey:@"first_name"] length]>0) {
            [vkFullNameStr appendFormat:@"%@ ",info[@"first_name"]];
            if (self.name_field.text.length==0){
                self.name_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"first_name"]];
            }
        }
    }
    
    if ([info hasKey:@"last_name"] ) {
        if ([[info objectForKey:@"last_name"] length]>0) {
            [vkFullNameStr appendFormat:@"%@",info[@"last_name"]];

            if (self.last_name_field.text.length==0){
                self.last_name_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"last_name"]];
            }
        }
    }
    
    if ([info hasKey:@"bdate"] ) {
        if ([[info objectForKey:@"bdate"] length]>0) {
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"dd.MM.yyyy"];
            NSDate *dateFromString = [[NSDate alloc] init];
            dateFromString = [dateFormatter dateFromString:[info objectForKey:@"bdate"]];
            if (dateFromString) {
                if (!_birthdayDate){
                    self.birthdayDate = dateFromString;
                }
            }
        }
    }
    
    vkId = [[VKSdk getAccessToken] userId];
    vkToken = [[VKSdk getAccessToken] accessToken];
    vkFullName = vkFullNameStr;
    
    [self showSocials];
}


#pragma mark -



#pragma mark - OdnokloasnikiMethods

/*** Odnoklassniki Delegate methods ***/
#pragma mark - OdnokloasnikiMethods
-(IBAction)loginWithOK:(id)sender{
    
    if (!User.ok_api){
        User.ok_api = [[Odnoklassniki alloc] initWithAppId:ok_appId appSecret:ok_appSecret appKey:ok_appKey delegate:self];
    }
    // if access_token is valid
    // если access_token действителен
    
    if (!User.ok_api.isSessionValid) {
        [User.ok_api authorizeWithPermissions:@[@"VALUABLE ACCESS"]];
    } else {
        [User.ok_api refreshToken];
    }
}



/*
 * API request without params.
 * Запрос к API без параметров.
 */

/*
 * API request with params.
 * Запрос к API с параметрами.
 */
- (void)getOKUserInfo{
    OKRequest *newRequest = [Odnoklassniki requestWithMethodName:@"users.getCurrentUser" params:@{@"fields": @"first_name,last_name,birthday"}];
    [newRequest executeWithCompletionBlock:^(NSDictionary *data) {
        if (![data isKindOfClass:[NSDictionary class]]) {
            return;
        }
        NSLog(@"USERDATA = %@",data);
        
        
        
        [self odnoklassnikiDidFinishGettinUserInfo:data];
        //        [self authUserBySocial:kOK user:okId token:okToken];
        
        
        
    } errorBlock:^(NSError *error) {
        NSLog(@"[%@ %@] %@", NSStringFromClass(self.class), NSStringFromSelector(_cmd), error);
    }];
}

- (void)odnoklassnikiDidFinishGettinUserInfo:(NSDictionary *)info{
    
    NSMutableString *okFullNameStr = [NSMutableString new];

    if ([info hasKey:@"first_name"] ) {
        if ([[info objectForKey:@"first_name"] length]>0) {
            [okFullNameStr appendFormat:@"%@ ",info[@"first_name"]];

            if (self.name_field.text.length==0){
                self.name_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"first_name"]];
            }
        }
    }
    
    if ([info hasKey:@"last_name"] ) {
        if ([[info objectForKey:@"last_name"] length]>0) {
            [okFullNameStr appendFormat:@"%@ ",info[@"last_name"]];

            if (self.last_name_field.text.length==0){
                self.last_name_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"last_name"]];
            }
        }
    }
    
    if ([info hasKey:@"birthday"] ) {
        if ([[info objectForKey:@"birthday"] length]>0) {
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"yyyy-MM-dd"];
            NSDate *dateFromString = [[NSDate alloc] init];
            dateFromString = [dateFormatter dateFromString:[info objectForKey:@"birthday"]];
            if (dateFromString) {
                if (!_birthdayDate){
                    self.birthdayDate = dateFromString;
                }
            }
        }
    }
    
    okToken = User.ok_api.session.accessToken;
    okId = info[@"uid"];
    okFullName = okFullNameStr;
    
    [self showSocials];

}


#pragma mark - Odnoklassniki Delegate methods

- (void)okShouldPresentAuthorizeController:(UIViewController *)viewController {
    [self presentViewController:viewController animated:YES completion:nil];
}

- (void)okWillDismissAuthorizeControllerByCancel:(BOOL)canceled {
    NSLog(@"autorization canceled by user");
}


/*
 * Method will be called after success login ([_api authorize:])
 * Метод будет вызван после успешной авторизации ([_api authorize:])
 */
- (void)okDidLogin {
    
    [self getOKUserInfo];
}

/*
 * Method will be called if login faild (cancelled == YES if user cancelled login, NO otherwise)
 * Метод будет вызван, если при авторизации произошла ошибка (cancelled == YES если пользователь прервал авторизацию, NO во всех остальных случаях)
 */
- (void)okDidNotLogin:(BOOL)canceled {
    
}

/*
 * Method will be called if login faild and server returned an error
 * Метод будет вызван, если сервер вернул ошибку авторизации
 */
- (void)okDidNotLoginWithError:(NSError *)error {
    
}

/*
 * Method will be called if [_api refreshToken] called and new access_token was got
 * Метод будет вызван в случае, если вызван [_api refreshToken] и получен новый access_token
 */
- (void)okDidExtendToken:(NSString *)accessToken {
    [self okDidLogin];
}

/*
 * Method will be called if [_api refreshToken] called and new access_token wasn't got
 * Метод будет вызван в случае, если вызван [_api refreshToken] и новый access_token не получен
 */
- (void)okDidNotExtendToken:(NSError *)error {
    
}

/*
 * Method will be called after logout ([_api logout])
 * Метод будет вызван после выхода пользователя ([_api logout])
 */
- (void)okDidLogout {
    //    self.sessionStatusLabel.text = @"Not logged in";
    //    [self.authButton setTitle:@"Login" forState:UIControlStateNormal];
    
}


#pragma mark - Google+ auth

-(IBAction)loginWithGPlus:(id)sender{
    [self signInToGoogle];
}

- (void)signInToGoogle {

    [GPPSignIn sharedInstance].clientID = kGoogleClientIDKey;
    [GPPSignIn sharedInstance].delegate = self;
    [GPPSignIn sharedInstance].shouldFetchGoogleUserEmail = YES;
    [GPPSignIn sharedInstance].shouldFetchGoogleUserID = YES;
    [GPPSignIn sharedInstance].scopes = [NSArray arrayWithObjects:
                                         @"https://www.googleapis.com/auth/plus.login",
                                         @"https://www.googleapis.com/auth/plus.me",
                                         @"https://www.googleapis.com/auth/userinfo.profile",
                                         @"https://www.googleapis.com/auth/userinfo.email", nil];

    [[GPPSignIn sharedInstance] authenticate];
    
}

-(void)finishedWithAuth:(GTMOAuth2Authentication *)auth error:(NSError *)error{
    [self getGpUserInfo];
}

-(void)getGpUserInfo{

    GTLQueryPlus *query = [GTLQueryPlus queryForPeopleGetWithUserId:@"me"];
    
    GTLServicePlus* plusService = [[GTLServicePlus alloc] init] ;
    plusService.retryEnabled = YES;

    [plusService setAuthorizer:[GPPSignIn sharedInstance].authentication];
    
    plusService.apiVersion = @"v1";
    [plusService executeQuery:query
            completionHandler:^(GTLServiceTicket *ticket,
                                GTLPlusPerson *person,
                                NSError *error) {
                if (error) {
                    //Handle Error
                } else {
                    NSMutableString *gpFullNameStr = [NSMutableString new];

                    NSLog(@"Email= %@", [GPPSignIn sharedInstance].authentication.userEmail);
                    NSLog(@"GoogleID=%@", person.identifier);
                    NSLog(@"User Name=%@", [person.name.givenName stringByAppendingFormat:@" %@", person.name.familyName]);
                    NSLog(@"Gender=%@", person.gender);
                    
                    if (person.name.givenName&&person.name.givenName.length>0){
                        [gpFullNameStr appendFormat:@"%@ ",person.name.givenName];
                        if (self.name_field.text.length==0){
                            self.name_field.text = [NSString stringWithFormat:@"%@",person.name.givenName];
                        }
                    }


                    if (person.name.familyName&&person.name.familyName.length>0){
                        [gpFullNameStr appendFormat:@"%@ ",person.name.familyName];
                        if (self.last_name_field.text.length==0){
                            self.last_name_field.text = [NSString stringWithFormat:@"%@",person.name.familyName];
                        }
                    }
                    
                    
                    if ([GPPSignIn sharedInstance].authentication.userEmail&&[GPPSignIn sharedInstance].authentication.userEmail.length>0){
                        [gpFullNameStr appendFormat:@"%@ ",[GPPSignIn sharedInstance].authentication.userEmail];
                        
                        if (self.email_field.text.length==0){
                            self.email_field.text = [NSString stringWithFormat:@"%@",[GPPSignIn sharedInstance].authentication.userEmail];
                        }
                    }
                    
                    if (person.birthday&&person.birthday.length>0){
                        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
                        NSDate *dateFromString = [[NSDate alloc] init];
                        dateFromString = [dateFormatter dateFromString:person.birthday];
                        if (dateFromString) {
                            if (!_birthdayDate){
                                self.birthdayDate = dateFromString;
                            }
                        }
                    }
                    
                    gpFullName = gpFullNameStr;
                    
                }
            }];
    
    gpId = [GPPSignIn sharedInstance].userID;
    gpToken = [GPPSignIn sharedInstance].authentication.accessToken;

    [self showSocials];
}


@end

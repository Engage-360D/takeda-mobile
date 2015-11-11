//
//  RegistrationPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "RegistrationPage.h"
#import <QuartzCore/QuartzCore.h>



@interface RegistrationPage (){
    CActionSheet *picker_cover;
    NSMutableArray *regions_data;
    UIPickerView *region_picker;
    UIDatePicker *date_picker;
    NSDate *birthdayDate;
    NSMutableDictionary *selectedRegion;
    
    NSString *vkId;
    NSString *vkToken;
    
    NSString *fbId;
    NSString *fbToken;
    
    NSString *okId;
    NSString *okToken;
    
    NSString *gpId;
    NSString *gpToken;

}

@end

@implementation RegistrationPage{
    
}
@synthesize scrollView;
@synthesize btn_register;
@synthesize bg_block;
@synthesize name_field;
@synthesize email_field;
@synthesize pass_field;

@synthesize user_is_doctor;
@synthesize user_is_agree_personal_data;
@synthesize user_is_agree_email_subscribe_data;
@synthesize user_is_agree_information_is_recomemd_style;

@synthesize user_is_doctor_img;
@synthesize user_is_agree_personal_data_img;
@synthesize user_is_agree_email_subscribe_data_img;
@synthesize user_is_agree_information_is_recomemd_style_img;


@synthesize btn_birthday;
@synthesize btn_region;

int sel_index_region = 0;






NSString *specialization = @"";
NSString *experience = @"";
NSString *address = @"";
NSString *phone = @"";
NSString *institution = @"";
NSString *birthday = @"";
NSString *graduation = @"";

NSString *sentPassword;
NSString *sentEmail;



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [self setDefaultData];
    }
    return self;
}


-(void)setDefaultData{
    self.user_is_doctor = NO;
    self.user_is_agree_personal_data = NO;
    self.user_is_agree_email_subscribe_data = NO;
    self.user_is_agree_information_is_recomemd_style = NO;
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
        bb.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17];
    }

    
    [self.btn_register setBackgroundImage:[[UIImage imageNamed:@"button_arrow_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 30)] forState:UIControlStateNormal];
    
    self.email_field.placeholderColor = RGB(53, 65, 71);
    self.email_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.email_field.font = [UIFont fontWithName:@"SegoeWP" size:14.0];
    
    self.pass_field.placeholderColor = RGB(53, 65, 71);
    self.pass_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.pass_field.font = [UIFont fontWithName:@"SegoeWP" size:14.0];
    
    self.name_field.placeholderColor = RGB(53, 65, 71);
    self.name_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.name_field.font = [UIFont fontWithName:@"SegoeWP" size:14.0];

    self.lastname_field.placeholderColor = RGB(53, 65, 71);
    self.lastname_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.lastname_field.font = [UIFont fontWithName:@"SegoeWP" size:14.0];

    
    self.danger_text.text = @"Имеются противопоказания. \n Необходимо ознакомиться с инструкцией по применению.";
    
    self.btn_register.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0];

}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupInterface];
    [self.view addSubview:self.scrollView];
    [self.scrollView setup_autosize];
    self.navigationItem.rightBarButtonItems = nil;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.scrollView.frame = CGRectMake(0, 45, self.view.width, self.view.height - 45);
    [self updateFields];
    [self initData];
}



-(BOOL)checkFields{
    if (name_field.text.length==0||email_field.text.length==0||pass_field.text.length==0) return NO;
    if (birthdayDate==nil||selectedRegion==nil) return NO;
    if (user_is_agree_personal_data==NO||user_is_agree_information_is_recomemd_style==NO) return NO;
    return YES;
}


-(void)setBirthDayDataForSend:(NSDate*)date{
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"YYYY'-'MM'-'dd'T'HH':'mm':'ss'Z'"];
    NSString *prettyVersion = [dateFormat stringFromDate:date];
    birthday = prettyVersion;
}


-(IBAction)registerUser:(id)sender{

    if (![self checkFields]) {
        [self showMessage:@"Заполните все поля" title:@"Уведомление"];
        return;
    }
        
        NSString *firstname = self.name_field.text;
        NSString *lastname = self.lastname_field.text;
        NSString *email = self.email_field.text;
        NSString *password = self.pass_field.text;
        NSString *region = self.btn_region.titleLabel.text;
        region = [NSString stringWithFormat:@"%@", selectedRegion[@"id"]];
        password = pass_field.text;
        birthday = [Global strDateTime:birthdayDate];
        //birthday = [birthdayDate stringWithFormat:@"yyyy-MM-dd'T'HH:mm:ssZZZ"];
        NSMutableDictionary *params = [NSMutableDictionary dictionaryWithDictionary:
                                         @{@"email": email,
                                           @"firstname": firstname,
                                           @"lastname" : lastname,
                                           @"birthday":birthday,
                                           @"specializationExperienceYears" : [NSNull null],
                                           @"specializationGraduationDate" : [NSNull null],
                                           @"specializationInstitutionAddress" : [NSNull null],
                                           @"specializationInstitutionName" : [NSNull null],
                                           @"specializationInstitutionPhone" : [NSNull null],
                                           @"specializationName" : [NSNull null],
                                           @"plainPassword":password,
                                           @"isDoctor" : [NSNumber numberWithBool:self.user_is_doctor],
                                           @"isSubscribed" : [NSNumber numberWithBool:self.user_is_agree_email_subscribe_data],
                                           @"links": @{@"region":region
                                        }}];
    
    if (vkId) [params setObject:[NSString stringWithFormat:@"%@",vkId] forKey:@"vkontakteId"];
    if (vkToken) [params setObject:[NSString stringWithFormat:@"%@",vkToken] forKey:@"vkontakteToken"];
    if (fbId) [params setObject:[NSString stringWithFormat:@"%@",fbId] forKey:@"facebookId"];
    if (fbToken) [params setObject:[NSString stringWithFormat:@"%@",fbToken] forKey:@"facebookToken"];
    if (okId) [params setObject:[NSString stringWithFormat:@"%@",okId] forKey:@"odnoklassnikiId"];
    if (okToken) [params setObject:[NSString stringWithFormat:@"%@",okToken] forKey:@"odnoklassnikiToken"];
    if (gpId) [params setObject:[NSString stringWithFormat:@"%@",gpId] forKey:@"googleId"];
    if (gpToken) [params setObject:[NSString stringWithFormat:@"%@",gpToken] forKey:@"googleToken"];

    
//        {
//            "data" : {
//                "email" : "a.b@c.d",
//                "firstname" : "",
//                "lastname" : "",
//                "birthday" : "1990-10-10T00:00:00+0000",
        
//                "specializationExperienceYears" : null,
        
//                "specializationGraduationDate" : null,
//                "specializationInstitutionAddress" : null,
//                "specializationInstitutionName" : null,
//                "specializationInstitutionPhone" : null,
//                "specializationName" : null,
//                "plainPassword" : "xxx",
//                "isDoctor" : false,
//                "isSubscribed" : true,
//                "links" : {
//                    "region" : ""
//                }
//            }
//        }
//        
    [self showActivityIndicatorWithString:@""];
        [ServData registrationUserWithData:params completion:^(BOOL result, NSError *error, NSString* textError){
            [self removeActivityIdicator];
            if (result){
                [appDelegate logoutUser];
                [self showActivityIndicatorWithString:@""];
                [ServData authUserWithLogin:self.email_field.text password:self.pass_field.text completion:^(BOOL result, NSError *error) {
                    [self removeActivityIdicator];

                    if (result) {
                        [User setCurrentUser:User.user_login];
                        [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
                            [User setCurrentUser:User.user_login];
                            if (result){
                                [[rootMenuController sharedInstance] resetControllers];
                                UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
                                [vc.navigationController setNavigationBarHidden:NO];
                                [ApplicationDelegate setRootViewController:vc];
                            } else {
                                [Helper fastAlert:@"Ошибка загрузки данных пользователя"];
                            }
                        }];
                        
                    } else {
                        [Helper fastAlert:@"Ошибка авторизации"];
                    }
                }];

                
            } else {
                if (error.code==409){
                    [Helper fastAlert:@"Пользователь уже существует"];
                } else {
                    [Helper fastAlert:@"Ошибка регистрации"];
                }
                
            }
            
        }];
        
        
 
}

-(void)updateFields{
    if (self.user_is_doctor) {
        self.user_is_doctor_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_doctor_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
    
    if (self.user_is_agree_personal_data) {
        self.user_is_agree_personal_data_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_agree_personal_data_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
    
    if (self.user_is_agree_email_subscribe_data) {
        self.user_is_agree_email_subscribe_data_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_agree_email_subscribe_data_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
    
    if (self.user_is_agree_information_is_recomemd_style) {
        self.user_is_agree_information_is_recomemd_style_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_agree_information_is_recomemd_style_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
}





-(IBAction)selectField:(id)sender{
    switch ((int)[sender tag]) {
        case 1:{
            self.user_is_doctor = !self.user_is_doctor;
            break;}
        case 2:{
            self.user_is_agree_personal_data = !self.user_is_agree_personal_data;
            break;}
        case 3:{
            self.user_is_agree_email_subscribe_data = !self.user_is_agree_email_subscribe_data;
            break;}
        case 4:{
            self.user_is_agree_information_is_recomemd_style = !self.user_is_agree_information_is_recomemd_style;
            break;}
        default:
            break;
    }
    [self updateFields];
    
}




#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}
#pragma mark -




#pragma mark - UIPickerView

-(IBAction)showPickerRegion:(id)sender{
    [self hideKeyb];
    picker_cover = nil;

    picker_cover = [[CActionSheet alloc] initInView:self.view];
    
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    toolbar.frame = CGRectMake(0, 0, ScreenWidth, 44);
    NSMutableArray *items = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *button1 = [[UIBarButtonItem alloc] initWithTitle:@"Закрыть" style:UIBarButtonItemStyleDone target:self action:@selector(closePicker)];
    
    UIBarButtonItem *button2 = [[UIBarButtonItem alloc] initWithTitle:@"Применить" style:UIBarButtonItemStyleDone target:self action:@selector(applyPicker:)];
    
    UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    
    [items addObject:button1];
    [items addObject:flexibleSpaceLeft];
    [items addObject:button2];
    [toolbar setItems:items animated:NO];
    
    float picker_width = ScreenWidth;
    region_picker = [[UIPickerView alloc] initWithFrame:CGRectMake(0,40,picker_width,210)];
    region_picker.delegate = self;
    region_picker.dataSource = self;
    region_picker.showsSelectionIndicator = YES;
    [picker_cover addSubview:toolbar];
    [picker_cover addSubview:region_picker];
    picker_cover.backgroundColor = [UIColor whiteColor];
    picker_cover.tag = 1;
    [region_picker selectRow:sel_index_region inComponent:0 animated:NO];
    [picker_cover show];
}

-(void)closePicker{
    [picker_cover dissmiss];
}


-(void)applyPicker:(id)sender{
    if ((int)[picker_cover tag]==1) {
        [self.btn_region setTitle:[NSString stringWithFormat:@"Страна: %@",[regions_data objectAtIndex:sel_index_region][@"name"]] forState:UIControlStateNormal];
        selectedRegion = [regions_data objectAtIndex:sel_index_region];
    }else{
        if ((int)[picker_cover tag]==2) {
            birthdayDate = date_picker.date;
            [self setDateBirthDay:birthdayDate];
        }
    }
    [picker_cover dissmiss];
}


-(void)setDateBirthDay:(NSDate*)myDate{
    birthdayDate = myDate;
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"dd MMMM yyyy"];
    NSString *prettyVersion = [dateFormat stringFromDate:myDate];
    [self.btn_birthday setTitle:[NSString stringWithFormat:@"Дата рождения: %@",prettyVersion] forState:UIControlStateNormal];
}


#pragma mark PickerView DataSource

- (NSInteger)numberOfComponentsInPickerView:
(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
numberOfRowsInComponent:(NSInteger)component
{
    return [regions_data count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    return [regions_data objectAtIndex:row][@"name"];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component{
    sel_index_region = (int)row;
    NSLog(@"index = %i text = %@",sel_index_region,[regions_data objectAtIndex:row]);
}



#pragma mark -


#pragma mark - UIDatePicker

-(IBAction)showTimePicker:(id)sender{
    [self hideKeyb];
    picker_cover = nil;
    picker_cover = [[CActionSheet alloc] initInView:self.view];
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    toolbar.frame = CGRectMake(0, 0, ScreenWidth, 44);
    NSMutableArray *items = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *button1 = [[UIBarButtonItem alloc] initWithTitle:@"Закрыть" style:UIBarButtonItemStyleDone target:self action:@selector(closePicker)];
    
    UIBarButtonItem *button2 = [[UIBarButtonItem alloc] initWithTitle:@"Применить" style:UIBarButtonItemStyleDone target:self action:@selector(applyPicker:)];
    
    UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    
    [items addObject:button1];
    [items addObject:flexibleSpaceLeft];
    [items addObject:button2];
    [toolbar setItems:items animated:NO];
    
    float picker_width = ScreenWidth;
    
    date_picker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0,40,picker_width,210)];
    date_picker.datePickerMode = UIDatePickerModeDate;
    //[date_picker addTarget:self action:@selector(changeDate) forControlEvents:UIControlEventValueChanged];
    
    [date_picker setDate:birthdayDate?birthdayDate:[Helper getAgoYear:25 fromDate:[NSDate date]]];
    
    [picker_cover addSubview:toolbar];
    [picker_cover addSubview:date_picker];
    picker_cover.backgroundColor = [UIColor whiteColor];
    //[picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
    picker_cover.tag = 2;
    [picker_cover show];
}


#pragma mark -



#pragma mark - Social Networks


#pragma mark - FACEBOOK

-(IBAction)loginWithFb:(id)sender{
    ShowNetworkActivityIndicator();
    if (FBSession.activeSession.isOpen) {
        // login is integrated with the send button -- so if open, we send
        [self sendRequests];
    } else {
        NSArray *permissions = [[NSArray alloc] initWithObjects:
                                @"email", @"read_stream", @"user_about_me", @"user_birthday",
                                nil];
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
    
    
    if ([userData hasKey:@"first_name"] ) {
        if ([[userData objectForKey:@"first_name"] length]>0) {
            if (self.name_field.text.length==0){
                self.name_field.text = [userData objectForKey:@"first_name"];
            }
        }
    }
    
    if ([userData hasKey:@"last_name"] ) {
        if ([[userData objectForKey:@"last_name"] length]>0) {
            if (self.lastname_field.text.length==0){
                self.lastname_field.text = [userData objectForKey:@"last_name"];
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
            if (!birthdayDate){
                [self setUserDateBirthDay:[userData objectForKey:@"birthday"]];
            }
        }
    }
    
    fbId = [userData objectForKey:@"id"];
    fbToken = FBSession.activeSession.accessTokenData.accessToken;

    
}








#pragma mark -
#pragma mark - VK

- (IBAction)loginWithVK:(id)sender
{
    
    [VKSdk initializeWithDelegate:self andAppId:vkAppId];
    [VKSdk authorize:VK_SCOPE revokeAccess:YES];
//
//    if ([VKSdk wakeUpSession])
//    {
//        [self getVKUserInfo];
//    } else {
//        [VKSdk authorize:VK_SCOPE revokeAccess:YES];
//    }
    
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
    [[[UIAlertView alloc] initWithTitle:nil message:@"Access denied" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
}
-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    [self.navigationController popToRootViewControllerAnimated:YES];
}


- (void)vkontakteDidFinishGettinUserInfo:(NSDictionary *)info{
    
    if ([info hasKey:@"first_name"] ) {
        if ([[info objectForKey:@"first_name"] length]>0) {
            if (self.name_field.text.length==0){
                self.name_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"first_name"]];
            }
        }
    }
    
    if ([info hasKey:@"last_name"] ) {
        if ([[info objectForKey:@"last_name"] length]>0) {
            if (self.lastname_field.text.length==0){
                self.lastname_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"last_name"]];
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
                if (!birthdayDate){
                    [self setDateBirthDay:dateFromString];
                }
            }
        }
    }
    
    vkId = [[VKSdk getAccessToken] userId];
    vkToken = [[VKSdk getAccessToken] accessToken];

    NSLog(@"%@", info);
}



- (void)vkontakteDidFinishGettinCountry:(NSDictionary *)responce{
    if ([responce hasKey:@"name"] ) {
        if ([[responce objectForKey:@"name"] length]>0) {
            [self.btn_region.titleLabel setText:[NSString stringWithFormat:@"Регион: %@",[responce objectForKey:@"name"]]];
        }
    }
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

#pragma mark - API requests



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
    
    if ([info hasKey:@"first_name"] ) {
        if ([[info objectForKey:@"first_name"] length]>0) {
            if (self.name_field.text.length==0){
                self.name_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"first_name"]];
            }
        }
    }
    
    if ([info hasKey:@"last_name"] ) {
        if ([[info objectForKey:@"last_name"] length]>0) {
            if (self.lastname_field.text.length==0){
                self.lastname_field.text = [NSString stringWithFormat:@"%@",[info objectForKey:@"last_name"]];
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
                if (!birthdayDate){
                    [self setDateBirthDay:dateFromString];
                }
            }
        }
    }
    
    okToken = User.ok_api.session.accessToken;
    okId = info[@"uid"];
    
    NSLog(@"%@", info);
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
    [[GPPSignIn sharedInstance] signOut];
    
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
                    
                    if ([GPPSignIn sharedInstance].authentication.userEmail&&[GPPSignIn sharedInstance].authentication.userEmail.length>0){
                        if (self.email_field.text.length==0){
                            self.email_field.text = [NSString stringWithFormat:@"%@",[GPPSignIn sharedInstance].authentication.userEmail];
                        }
                    }
                    
                    if (person.name.givenName&&person.name.givenName.length>0){
                        if (self.name_field.text.length==0){
                            self.name_field.text = [NSString stringWithFormat:@"%@",person.name.givenName];
                        }
                    }
                    
                    if (person.name.familyName&&person.name.familyName.length>0){
                        if (self.lastname_field.text.length==0){
                            self.lastname_field.text = [NSString stringWithFormat:@"%@",person.name.familyName];
                        }
                    }
                    
                    if (person.birthday&&person.birthday.length>0){
                        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
                        NSDate *dateFromString = [[NSDate alloc] init];
                        dateFromString = [dateFormatter dateFromString:person.birthday];
                        if (dateFromString) {
                            if (!birthdayDate){
                                [self setDateBirthDay:dateFromString];
                            }
                        }
                    }
                    
                    
                    
                    
                }
            }];
    
    gpId = [GPPSignIn sharedInstance].userID;
    gpToken = [GPPSignIn sharedInstance].authentication.accessToken;
    
}







-(void)setUserDateBirthDay:(NSString*)dateString{
    __block NSDate *detectedDate;
//Detect.
    NSDataDetector *detector = [NSDataDetector dataDetectorWithTypes:NSTextCheckingAllTypes error:nil];
    [detector enumerateMatchesInString:dateString
                               options:kNilOptions
                                 range:NSMakeRange(0, [dateString length])
                            usingBlock:^(NSTextCheckingResult *result, NSMatchingFlags flags, BOOL *stop)
     {
         detectedDate = result.date;
         if (detectedDate) {
            [self setDateBirthDay:detectedDate];
         }
         
     }];
}




@end

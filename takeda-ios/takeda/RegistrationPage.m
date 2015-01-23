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
    UIActionSheet *picker_cover;
    NSMutableArray *regions_data;
    UIPickerView *region_picker;
    UIDatePicker *date_picker;
    NSDate *birthdayDate;
    NSMutableDictionary *selectedRegion;
    
    
}
@property (strong, nonatomic) FBRequestConnection *requestConnection;
@property(nonatomic, retain) Odnoklassniki *odnoklasniki_api;
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

#warning SET OK keys !!!
static NSString * Odnkl_appID = @"181911552";
static NSString * Odnkl_appSecret = @"D8B73BB1C3297CC1C6358650";
static NSString * Odnkl_appKey = @"CBABMINLABABABABA";





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
    self.danger_text.text = @"Имеются противопоказания \n необходимо ознакомиться с инструкцией по применению";
    
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
    return YES;
}


-(void)setBirthDayDataForSend:(NSDate*)date{
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"YYYY'-'MM'-'dd'T'HH':'mm':'ss'Z'"];
    NSString *prettyVersion = [dateFormat stringFromDate:date];
    birthday = prettyVersion;
}


-(IBAction)registerUser:(id)sender{

    if (![self checkFields]) {return;}
        
        NSString *name = self.name_field.text;
        NSString *email = self.email_field.text;
        NSString *password = self.pass_field.text;
        NSString *region = self.btn_region.titleLabel.text;
        region = [NSString stringWithFormat:@"%@", selectedRegion[@"id"]];
        password = pass_field.text;
        birthday = [birthdayDate stringWithFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZ"];
        NSDictionary *params = @{          @"email": email,
                                           @"firstname": name,
                                           @"lastname" : @"",
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
                                    }};
        
        
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

        [ServData registrationUserWithData:params completion:^(BOOL result, NSError *error, NSString* textError){
            if (result){
            [ServData authUserWithLogin:email password:password completion:^(BOOL result, NSError *error) {
                if (result) {
                    [User setCurrentUser:User.user_name];
                    [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
                        [User setCurrentUser:User.user_name];
                        if (result){
                            NSLog(@"userData - %@",[[UserData sharedObject] getUserData]);
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
                [Helper fastAlert:@"Ошибка регистрации"];
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
    picker_cover = nil;
    picker_cover = [[UIActionSheet alloc] initWithTitle:nil
                                       delegate:nil
                              cancelButtonTitle:@""
                         destructiveButtonTitle:nil
                              otherButtonTitles:nil];
    
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
    [picker_cover showInView:self.view.superview];
    [picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
    picker_cover.tag = 1;
    
}

-(void)closePicker{
    [picker_cover dismissWithClickedButtonIndex:0 animated:YES];
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
    [picker_cover dismissWithClickedButtonIndex:0 animated:YES];
}


-(void)setDateBirthDay:(NSDate*)myDate{
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
    picker_cover = nil;
    picker_cover = [[UIActionSheet alloc] initWithTitle:nil
                                               delegate:nil
                                      cancelButtonTitle:@""
                                 destructiveButtonTitle:nil
                                      otherButtonTitles:nil];
    
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
    [picker_cover showInView:self.view.superview];
    [picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
    picker_cover.tag = 2;
}


#pragma mark -



#pragma mark - Socil Networks


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
    [self.requestConnection cancel];
    
    self.requestConnection = newConnection;
    [newConnection start];
}

// Report any results.  Invoked once for each request we make.
- (void)requestCompleted:(FBRequestConnection *)connection
                 forFbID:fbID
                  result:(id)result
                   error:(NSError *)error {
    HideNetworkActivityIndicator();
    // not the completion we were looking for...
    if (self.requestConnection &&
        connection != self.requestConnection) {
        return;
    }
    
    // clean this up, for posterity
    self.requestConnection = nil;
    
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
    [self.requestConnection cancel];
    self.requestConnection = nil;
}

-(void)setUserDataFromFB:(NSDictionary*)userData{
    if ([userData hasKey:@"name"] ) {
        if ([[userData objectForKey:@"name"] length]>0) {
            self.name_field.text = [userData objectForKey:@"name"];
        }
    }
    if ([userData hasKey:@"email"] ) {
        if ([[userData objectForKey:@"email"] length]>0) {
            self.email_field.text = [userData objectForKey:@"email"];
        }
        
    }
    
    if ([userData hasKey:@"birthday"] ) {
        if ([[userData objectForKey:@"birthday"] length]>0) {
            [self setUserDateBirthDay:[userData objectForKey:@"birthday"]];
        }
        
    }

}








#pragma mark -
#pragma mark - VK

- (IBAction)loginWithVK:(id)sender
{
    ShowNetworkActivityIndicator();
    _vkontakte = [Vkontakte sharedInstance];
    _vkontakte.delegate = self;
    if (![_vkontakte isAuthorized])
    {
        [_vkontakte authenticate];
    }
    else
    {
        [_vkontakte getUserInfo];
    }
}

#pragma mark - VkontakteDelegate

- (void)vkontakteDidFailedWithError:(NSError *)error{
    HideNetworkActivityIndicator();
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)showVkontakteAuthController:(UIViewController *)controller{
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
    {
        controller.modalPresentationStyle = UIModalPresentationFormSheet;
    }
    controller.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    [self presentViewController:controller animated:YES completion:nil];
}

- (void)vkontakteAuthControllerDidCancelled{
    HideNetworkActivityIndicator();
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)vkontakteDidFinishLogin:(Vkontakte *)vkontakte{
    [self dismissViewControllerAnimated:YES completion:nil];
    [_vkontakte getUserInfo];
}

- (void)vkontakteDidFinishLogOut:(Vkontakte *)vkontakte{
    HideNetworkActivityIndicator();
}

- (void)vkontakteDidFinishGettinUserInfo:(NSDictionary *)info{
    HideNetworkActivityIndicator();
    NSString *name = @"";
    if ([info hasKey:@"first_name"] ) {
        if ([[info objectForKey:@"first_name"] length]>0) {
            name = [NSString stringWithFormat:@"%@",[info objectForKey:@"first_name"]];
        }
    }
    
    if ([info hasKey:@"last_name"] ) {
        if ([[info objectForKey:@"last_name"] length]>0) {
            name = [NSString stringWithFormat:@"%@ %@",name,[info objectForKey:@"last_name"]];
        }
    }
    self.name_field.text = name;
    
    
    if ([info hasKey:@"bdate"] ) {
        if ([[info objectForKey:@"bdate"] length]>0) {
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"dd.MM.yyyy"];
            NSDate *dateFromString = [[NSDate alloc] init];
            dateFromString = [dateFormatter dateFromString:[info objectForKey:@"bdate"]];
            if (dateFromString) {
                
                [self setDateBirthDay:dateFromString];
            }else{
                [self setUserDateBirthDay:[info objectForKey:@"bdate"]];
            }
            
        }
    }
    
    if ([info hasKey:@"country"] ) {
        [_vkontakte getCountryByID:[info objectForKey:@"country"]];
        
    }
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
-(IBAction)loginWithOK:(id)sender{
    ShowNetworkActivityIndicator();
    if (!self.odnoklasniki_api) {
        self.odnoklasniki_api = [[Odnoklassniki alloc] initWithAppId:Odnkl_appID andAppSecret:Odnkl_appSecret andAppKey:Odnkl_appKey andDelegate:self];
    }
    
    self.odnoklasniki_api.delegate = self;
	if(self.odnoklasniki_api.isSessionValid){
		[self okDidLogin];
    }else{
        [self.odnoklasniki_api authorize:[NSArray arrayWithObjects:@"VALUABLE ACCESS", @"SET STATUS", nil]];
    }
    
	
    
}

/*** Odnoklassniki Delegate methods ***/
-(void)okDidLogin {
	OKRequest *userInfoRequest = [Odnoklassniki requestWithMethodName:@"users.getCurrentUser"
                                                        andParams:nil
                                                    andHttpMethod:@"GET"
                                                      andDelegate:self];
	[userInfoRequest load];
}

-(void)okDidNotLogin:(BOOL)canceled {
    HideNetworkActivityIndicator();
	//NSLog(@"%@",[NSString stringWithFormat:@"Did not login! Odnoklasniki  Canceled = %@", canceled ? @"YES" : @"NO"]);
}

-(void)okDidNotLoginWithError:(NSError *)error {
    HideNetworkActivityIndicator();
	//NSLog(@"Odnoklasniki login error = %@", error.userInfo);
}

-(void)okDidExtendToken:(NSString *)accessToken {
	[self okDidLogin];
}

-(void)okDidNotExtendToken:(NSError *)error {
    HideNetworkActivityIndicator();
	//NSLog(@"Error: Odnoklasniki did not extend token!!");
}

-(void)okDidLogout {
}

/*** Request delegate ***/
-(void)request:(OKRequest *)request didLoad:(id)result {
    HideNetworkActivityIndicator();
	
    
    

    if ([result hasKey:@"name"] ) {
        if ([[result objectForKey:@"name"] length]>0) {
            self.name_field.text = [result objectForKey:@"name"];
        }
    }

    
    
    
    if ([result hasKey:@"birthday"] ) {
        if ([[result objectForKey:@"birthday"] length]>0) {
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"yyyy-MM-ddTHH:mm:ssZ"];
            NSDate *dateFromString = [[NSDate alloc] init];
            dateFromString = [dateFormatter dateFromString:[result objectForKey:@"birthday"]];
            if (dateFromString) {
                [self setDateBirthDay:dateFromString];
            }else{
                [self setUserDateBirthDay:[result objectForKey:@"birthday"]];
            }
            
        }
    }
    

    /*dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
     NSURL *url = [NSURL URLWithString:[result valueForKey:@"pic_1"]];
     NSData *data = [NSData dataWithContentsOfURL:url];
     UIImage *img = [[UIImage alloc] initWithData:data];
     dispatch_sync(dispatch_get_main_queue(), ^{
     self.avatar.image = img;
     });//end block
     });*/
    
}

-(void)request:(OKRequest *)request didFailWithError:(NSError *)error {
    HideNetworkActivityIndicator();
    //	NSLog(@"Request failed with error = %@", error);
}

#pragma mark -















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

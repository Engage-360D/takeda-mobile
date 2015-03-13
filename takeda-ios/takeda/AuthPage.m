//
//  AuthPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "AuthPage.h"
#import "LeftMenu.h"
#import "RiskAnalysisPage.h"
#import "RegistrationPage.h"
#import "ForgetPage.h"
#import <QuartzCore/QuartzCore.h>



@interface AuthPage (){
    NSString *vkId;
    NSString *vkToken;
    
    NSString *fbId;
    NSString *fbToken;
    
    NSString *okId;
    NSString *okToken;
}

@property (strong, nonatomic) FBRequestConnection *requestConnection;
@property(nonatomic, retain) Odnoklassniki *odnoklasniki_api;

@end

@implementation AuthPage
@synthesize bg_block;
@synthesize email_field;
@synthesize pass_field;
@synthesize login_btn;
@synthesize forget_btn;
@synthesize registration_btn;
@synthesize description_text;
@synthesize nav_bar;


RegistrationPage *registrationPage;
ForgetPage *forgetPage;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    
    [self setNavImage];
    [self setNavigationPanel];

    [self setupInterface];
}


-(void)setupInterface{

    [self drawBorders:self.bg_block];
    
    self.description_text.font = [UIFont fontWithName:@"SegoeUI-Light" size:12.0];
    
    self.login_btn.titleLabel.font = [UIFont fontWithName:@"SegoeWP Light" size:17.0];
    [self.login_btn setBackgroundImage:[[UIImage imageNamed:@"button_arrow_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 30)] forState:UIControlStateNormal];
    
    
    self.registration_btn.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14.0];
    self.forget_btn.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14.0];
    
    self.email_field.placeholderColor = RGB(53, 65, 71);
    self.email_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.email_field.font = [UIFont fontWithName:@"SegoeWP" size:14.0];
    
    self.pass_field.placeholderColor = RGB(53, 65, 71);
    self.pass_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.pass_field.font = [UIFont fontWithName:@"SegoeWP" size:14.0];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    //[self.navigationItem setHidesBackButton:YES];
    User.userData = [NSMutableDictionary new];
    [self.scrollView setup_autosize];
    self.navigationController.navigationBarHidden = NO;
//        self.email_field.text = @"alexiosdeveloper@gmail.com";
//        self.pass_field.text = @"S3OioVLLgcQW-";

    self.email_field.text = @"alexruden+3@rambler.ru";
    self.pass_field.text = @"q";
    self.danger_text.text = @"Имеются противопоказания \n необходимо ознакомиться с инструкцией по применению";
    
}

-(void)autoFill{
//    if ([email_field.text isEqualToString:@"alexruden@rambler.ru"]){
//        self.pass_field.text = @"QsefPI4bFlAHw";
//    } else if ([email_field.text isEqualToString:@"alexiosdeveloper@gmail.com"]){
//        self.pass_field.text = @"S3OioVLLgcQW-";
//    }

}


-(IBAction)openGeneralApp:(id)sender{
    [[rootMenuController sharedInstance] resetControllers];
    UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
    [vc.navigationController setNavigationBarHidden:NO];
    [ApplicationDelegate setRootViewController:vc];
    
    /*[vc.navigationController setNavigationBarHidden:YES];
    [self.navigationController setNavigationBarHidden:YES];
    [self.navigationController pushViewController:vc animated:YES];*/
}


-(IBAction)openRegistrationPage:(id)sender{

    if (!registrationPage) {
        registrationPage = [[RegistrationPage alloc] initWithNibName:@"RegistrationPage" bundle:nil];
        registrationPage.navigationItem.leftBarButtonItem = nil;

    }
    [self.navigationController pushViewController:registrationPage animated:YES];
}

-(IBAction)openForgetPage:(id)sender{
    if (!forgetPage) {
        forgetPage = [[ForgetPage alloc] initWithNibName:@"ForgetPage" bundle:nil];
    }
    [self.navigationController pushViewController:forgetPage animated:YES];
}

-(IBAction)changePass:(id)sender{
    [self showMessageWithTextInput:@"E-mail" msg:@"Введите свой e-mail. На него придет новый пароль" title:@"Восстановление пароля" btns:@[@"Отмена",@"Отправить"] params:nil result:^(int index, NSString *text){
        if (index == 1){

            if (text.length > 0){
            NSString *email = text;
            
            [self showActivityIndicatorWithString:@"" inContainer:appDelegate.window];
            [ServData resetUserPassword:email withCompletion:^(BOOL success, NSError* error){
                [self removeActivityIdicator];
                [self showMessage:@"На вашу почту было отправлено письмо с новым паролем" title:@"Сброс пароля"];
            }];
            }
        }

    }];

}


#pragma mark - navigation panel
-(void)setNavImage{
    UINavigationBar *navBar = [[self navigationController] navigationBar];
    UIImage *backgroundImage;
    backgroundImage = [[UIImage imageNamed:@"nav_bar_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 10)] ;
    [navBar setBackgroundImage:backgroundImage forBarMetrics:UIBarMetricsDefault];
}

-(void)setNavigationPanel{
    UIImage *logoImage = [UIImage imageNamed:@"title_logo"];
    UIImageView *img_logo = [[UIImageView alloc] initWithFrame:CGRectMake(40, 8, logoImage.size.width, logoImage.size.height)];
    img_logo.image = logoImage;
    self.navigationItem.titleView = img_logo;
    
    UIButton *back_btn = [UIButton buttonWithType:UIButtonTypeCustom];
    UIBarButtonItem *back_item = [[UIBarButtonItem alloc] initWithCustomView:back_btn];
    self.navigationItem.leftBarButtonItem = back_item;
}


#pragma mark -

#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}
#pragma mark -




-(IBAction)authUser:(id)sender{
    [self showActivityIndicatorWithString:@"Авторизация"];
    [self autoFill];
    User.userData = nil;
    [User logoutUser];
    [ServData authUserWithLogin:self.email_field.text password:self.pass_field.text completion:^(BOOL result, NSError *error) {
        [self removeActivityIdicator];
        if (result) {
                [self setupUser];
            } else {
                [Helper fastAlert:@"Ошибка авторизации"];
            }
        }];
}

-(void)setupUser{
    [User setCurrentUser:User.user_login];
    [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
        [User setCurrentUser:User.user_login];
        [self showActivityIndicatorWithString:@"Синхронизация"];
        [User synchronizeUser:User.user_login completition:^(BOOL synchrSuccess, id synchrResult){
            [self removeActivityIdicator];
            if (result){
                [self openGeneralApp:self];
            } else {
                [Helper fastAlert:@"Ошибка загрузки данных пользователя"];
            }
        }];
    }];
}

-(void)authUserBySocial:(NSString*)social user:(NSString*)userId token:(NSString*)token{
    [self showActivityIndicatorWithString:@"Авторизация"];
    User.userData = nil;
    [User logoutUser];
    [ServData authUserWithSocial:social user:userId token:token completion:^(BOOL result, NSError *error) {
        [self removeActivityIdicator];
        if (result) {
            [self setupUser];
        } else {
            [Helper fastAlert:@"Ошибка авторизации"];
        }
    }];
}

#pragma mark - Social Networks


#pragma mark - FACEBOOK

-(IBAction)loginWithFb:(id)sender{
    ShowNetworkActivityIndicator();
    if (FBSession.activeSession.isOpen) {
        // login is integrated with the send button -- so if open, we send
        [self authorizeByFB];
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
                                              [self authorizeByFB];
                                          }
                                      }];
    }
}


-(void)authorizeByFB{
    fbToken = FBSession.activeSession.accessTokenData.accessToken;
    [self authUserBySocial:kFacebook user:nil token:fbToken];
}

//- (void)sendRequests {
//    NSArray *fbids = @[@"me"];
//    FBRequestConnection *newConnection = [[FBRequestConnection alloc] init];
//    for (NSString *fbid in fbids) {
//        FBRequestHandler handler =
//        ^(FBRequestConnection *connection, id result, NSError *error) {
//            // output the results of the request
//            [self requestCompleted:connection forFbID:fbid result:result error:error];
//        };
//        FBRequest *request = [[FBRequest alloc] initWithSession:FBSession.activeSession
//                                                      graphPath:fbid];
//        [newConnection addRequest:request completionHandler:handler];
//    }
//    [self.requestConnection cancel];
//    
//    self.requestConnection = newConnection;
//    [newConnection start];
//}
//
//// Report any results.  Invoked once for each request we make.
//- (void)requestCompleted:(FBRequestConnection *)connection
//                 forFbID:fbID
//                  result:(id)result
//                   error:(NSError *)error {
//    HideNetworkActivityIndicator();
//    // not the completion we were looking for...
//    if (self.requestConnection &&
//        connection != self.requestConnection) {
//        return;
//    }
//    
//    // clean this up, for posterity
//    self.requestConnection = nil;
//    
//    NSString *text;
//    if (error) {
//        text = error.localizedDescription;
//    } else {
//        NSDictionary *dictionary = (NSDictionary *)result;
//        [self setUserDataFromFB:dictionary];
//        text = (NSString *)[dictionary objectForKey:@"name"];
//    }
//    /*
//     NSLog(@"%@",[NSString stringWithFormat:@"%@: %@\r\n",[fbID stringByTrimmingCharactersInSet:
//     [NSCharacterSet whitespaceAndNewlineCharacterSet]],
//     text]);*/
//}
//
//- (void)viewDidUnload {
//    [self.requestConnection cancel];
//    self.requestConnection = nil;
//}
//
//-(void)setUserDataFromFB:(NSDictionary*)userData{
//    
//    
//    fbId = [userData objectForKey:@"id"];
//    fbToken = FBSession.activeSession.accessTokenData.accessToken;
//    
//    
//}
//







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
        // получаем токен и авторизируемся через него
        [self authorizeByVK];
    }
}

-(void)authorizeByVK{
    vkId = [UserDefaults objectForKey:@"VKUserID"];
    vkToken = [UserDefaults objectForKey:@"VKAccessTokenKey"];
    [self authUserBySocial:kVK user:vkId token:vkToken];
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
    [self authorizeByVK];
}

- (void)vkontakteDidFinishLogOut:(Vkontakte *)vkontakte{
    HideNetworkActivityIndicator();
    [UserDefaults removeObjectForKey:@"VKUserID"];
    [UserDefaults removeObjectForKey:@"VKAccessTokenKey"];
}

#pragma mark -



#pragma mark - OdnokloasnikiMethods
-(IBAction)loginWithOK:(id)sender{
    [self showMessage:@"Авторизация через Однокласники временно отключена" title:@"Уведомление"];
    return;
    
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
}

-(void)request:(OKRequest *)request didFailWithError:(NSError *)error {
    HideNetworkActivityIndicator();
    //	NSLog(@"Request failed with error = %@", error);
}

#pragma mark -




@end

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
@property (nonatomic, strong) Odnoklassniki *ok_api;

@end


NSString * const appId = @"1126090240";
NSString * const appKey = @"CBAHMPBEEBABABABA";
NSString * const appSecret = @"48E60A55CB4CF49C3E5D3762";


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
    [self.containerView setupAutosizeBySubviews];
    [self.scrollView setup_autosize];
    self.navigationController.navigationBarHidden = NO;

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
    vkId = [UserDefaults objectForKey: @"VKUserID"];
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
    
    if (!self.ok_api){
        self.ok_api = [[Odnoklassniki alloc] initWithAppId:appId appSecret:appSecret appKey:appKey delegate:self];
    }
    // if access_token is valid
    // если access_token действителен
    
    if (!self.ok_api.isSessionValid) {
        [self.ok_api authorizeWithPermissions:@[@"VALUABLE ACCESS"]];
    } else {
        [self.ok_api refreshToken];
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
- (void)getUserInfo{
    OKRequest *newRequest = [Odnoklassniki requestWithMethodName:@"users.getCurrentUser" params:@{@"fields": @"first_name,last_name,location,pic50x50"}];
    [newRequest executeWithCompletionBlock:^(NSDictionary *data) {
        if (![data isKindOfClass:[NSDictionary class]]) {
            return;
        }
        NSLog(@"USERDATA = %@",data);
        
        okToken = self.ok_api.session.accessToken;
        okId = data[@"uid"];

        [self authUserBySocial:kOK user:okId token:okToken];

        
        
    } errorBlock:^(NSError *error) {
        NSLog(@"[%@ %@] %@", NSStringFromClass(self.class), NSStringFromSelector(_cmd), error);
    }];
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
    
    [self getUserInfo];
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
    [self clearUserInfo];
}

#pragma mark - Google+ auth

-(IBAction)loginWithGPlus:(id)sender{
    [self showMessage:@"Авторизация через Google+ временно отключена" title:@"Уведомление"];
}





#pragma mark - interface

- (void)clearUserInfo {

}


#pragma mark -




@end

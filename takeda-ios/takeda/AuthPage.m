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



@interface AuthPage ()

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
    self.email_field.text = @"alexruden@rambler.ru";
    self.pass_field.text = @"QsefPI4bFlAHw";
    self.danger_text.text = @"Имеются противопоказания \n необходимо ознакомиться с инструкцией по применению";
    
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
    NSString *email;
    if (User.user_login){
        email = User.user_login;
    } else {
        email = self.email_field.text;
    }
    
    [self showActivityIndicatorWithString:@"" inContainer:appDelegate.window];
    [ServData resetUserPassword:email withCompletion:^(BOOL success, NSError* error){
        [self removeActivityIdicator];
        [self showMessage:@"На вашу почту было отправлено письмо с новым паролем" title:@"Сброс пароля"];
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
    User.userData = nil;
    [User logoutUser];
    [ServData authUserWithLogin:self.email_field.text password:self.pass_field.text completion:^(BOOL result, NSError *error) {
            if (result) {
                [User setCurrentUser:User.user_login];
                [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
                    [User setCurrentUser:User.user_login];
                    if (result){
                        [self openGeneralApp:self];
                    } else {
                        [Helper fastAlert:@"Ошибка загрузки данных пользователя"];
                    }
                }];

            } else {
                [Helper fastAlert:@"Ошибка авторизации"];
            }
        }];
}


@end

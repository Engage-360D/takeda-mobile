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
    self.title = @"";
    
    [self setNavImage];
    [self setNavigationPanel];
    self.navigationItem.leftBarButtonItem = nil;
    
    for (UIView *view in self.bg_block) {
        CALayer *TopBorder = [CALayer layer];
        TopBorder.frame = CGRectMake(0.0f, 0.0f, view.frame.size.width, 1.0f);
        TopBorder.contents = (id)[UIImage imageNamed:@"bg_separator"].CGImage;
        [view.layer addSublayer:TopBorder];
        CALayer *BottomBorder = [CALayer layer];
        BottomBorder.frame = CGRectMake(0.0f, view.frame.size.height, view.frame.size.width, 1.0f);
        BottomBorder.contents = (id)[UIImage imageNamed:@"bg_separator"].CGImage;
        [view.layer addSublayer:BottomBorder];
    }

    self.description_text.font = [UIFont fontWithName:@"SegoeUI-Light" size:12.0];
    
    self.login_btn.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:25.0];
    self.registration_btn.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14.0];
    self.forget_btn.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14.0];
    self.email_field.placeholderColor = RGB(53, 65, 71);
    self.email_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.pass_field.placeholderColor = RGB(53, 65, 71);
    self.pass_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationItem setHidesBackButton:YES];
    [self.navigationController setNavigationBarHidden:NO];
}



-(IBAction)openGeneralApp:(id)sender{
    UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
    [vc.navigationController setNavigationBarHidden:YES];
    [self.navigationController setNavigationBarHidden:YES];
    [self.navigationController pushViewController:vc animated:YES];
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
}


#pragma mark -

#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}
#pragma mark -


@end

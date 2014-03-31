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

@interface AuthPage ()

@end

@implementation AuthPage
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
    self.title = @"Cardiomagnil";
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationItem setHidesBackButton:YES];
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
    }
    [self.navigationController pushViewController:registrationPage animated:YES];
}

-(IBAction)openForgetPage:(id)sender{
    if (!forgetPage) {
        forgetPage = [[ForgetPage alloc] initWithNibName:@"ForgetPage" bundle:nil];
    }
    [self.navigationController pushViewController:forgetPage animated:YES];
}



@end

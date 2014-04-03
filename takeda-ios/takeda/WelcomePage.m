//
//  WelcomePage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "WelcomePage.h"
#import "AuthPage.h"

@interface WelcomePage ()

@end

@implementation WelcomePage
@synthesize welcome_text_1;
@synthesize welcome_text_2;
@synthesize welcome_text_3;


AuthPage *authPage;



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
    //self.title = @"Cardiomagnil";
    self.navigationController.navigationBarHidden = YES;
    self.welcome_text_1.font = [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
    self.welcome_text_2.font = [UIFont fontWithName:@"SegoeUI-Light" size:12.0];
    self.welcome_text_3.font = [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
    
    // Do any additional setup after loading the view from its nib.
}


-(IBAction)goAuthPage:(id)sender{
    if (!authPage) {
        authPage = [[AuthPage alloc] initWithNibName:@"AuthPage" bundle:nil];
    }
    [self.navigationController pushViewController:authPage animated:YES];
}





@end

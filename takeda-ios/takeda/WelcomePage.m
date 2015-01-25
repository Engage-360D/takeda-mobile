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
bool is_loading;
bool is_authorized;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        is_authorized = NO;
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
    [self autologin];
    // Do any additional setup after loading the view from its nib.
}

-(void)autologin{
    
    NSString *lastUser = [User getLastUser];
   // lastUser = nil; //temp
    if (lastUser){
        is_loading = YES;
        // вытягиваем информацию о этом юзере
        [User setCurrentUser:lastUser];
        [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
            [User setCurrentUser:lastUser];
            is_loading = NO;
            is_authorized = YES;
//            UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
//            [vc.navigationController setNavigationBarHidden:NO];
//            [ApplicationDelegate setRootViewController:vc];

        }];
    } else {
        is_loading = NO;
    }
    
    
    
    
//    
//    if ([[UserData sharedObject] getUserName] && [[UserData sharedObject] getUserPassword]) {
//        is_loading = YES;
//        [ServData authUserWithLogin:[[UserData sharedObject] getUserName] password:[[UserData sharedObject] getUserPassword] completion:^(BOOL result, NSError *error) {
//            
//            if (result) {
//                [ServData getUserDataWithCompletion:^(BOOL result, NSError *error) {
//                    is_loading = NO;
//                    if (result) {
//                        UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
//                        [vc.navigationController setNavigationBarHidden:NO];
//                        [ApplicationDelegate setRootViewController:vc];
//                        
//                        
//                    }else{
//                        
//                    }
//                }];
//            }else{
//                is_loading = NO;
//            }
//        }];
//    }else{
//        is_loading = NO;
//    }
}




-(IBAction)goAuthPage:(id)sender{
    if (is_loading) {
        [Helper fastAlert:@"Подождите, идет загрузка данных..."];
        return;
    } else if (is_authorized){
        UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
        [vc.navigationController setNavigationBarHidden:NO];
        [ApplicationDelegate setRootViewController:vc];
    } else {
        if (!authPage) {
            authPage = [[AuthPage alloc] initWithNibName:@"AuthPage" bundle:nil];
        }
        [self.navigationController pushViewController:authPage animated:YES];
    }
    
}





@end

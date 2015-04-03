//
//  WelcomePage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "WelcomePage.h"

@interface WelcomePage ()

@end

@implementation WelcomePage
@synthesize welcome_text_1;
@synthesize welcome_text_2;
@synthesize welcome_text_3;
@synthesize is_authorized;
@synthesize taked;
@synthesize is_loading;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        is_authorized = NO;
        taked = NO;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(networkChanged:) name:kReachabilityChangedNotification object:nil];
    }
    return self;
}



-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];

}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
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


- (void)networkChanged:(NSNotification *)notification
{
    if (taked) return;
    taked = YES;
    [self start];
}

-(void)start{
    NetworkStatus remoteHostStatus = [appDelegate.hostReachability currentReachabilityStatus];
    if (remoteHostStatus == NotReachable) {
        NSLog(@"not reachable");
        [self autologin];
    }
    else if (remoteHostStatus == ReachableViaWiFi) {
        NSLog(@"wifi");
        [self autologin];
    }
    else if (remoteHostStatus == ReachableViaWWAN) {
        NSLog(@"carrier");
        [self autologin];
    }
}

-(void)autologin{
    
    NSString *lastUser = [User getLastUser];

    if (lastUser||self.theUser){
        NSMutableDictionary *usData = self.theUser?self.theUser:[User getUserInfo:lastUser];
        if (!usData){
            is_loading = NO;
            is_authorized = NO;
            return;
        }
        User.userData = usData;
        is_loading = YES;
        // получаем информацию о этом юзере
       // [User setCurrentUser:lastUser];
        NSLog(@"Да, есть юзер - %@", usData);
        NSLog(@"получаем информацию по пользователю...");
        [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
            if (result){
                [User setCurrentUser:lastUser];
                NSLog(@"Информацию по пользователю получили, начинаем синхронизацию");
                [User synchronizeUser:User.user_login completition:^(BOOL synchrSuccess, id synchrResult){

                    if (result){
                        is_loading = NO;
                        is_authorized = YES;
                    } else {
                        [Helper fastAlert:@"Ошибка загрузки данных пользователя"];
                    }
                }];

            } else {
                if (error.code == 500){
                    is_loading = NO;
                    is_authorized = YES;
                } else {
                    // неверный токен
                    User.userData = nil;
                    is_loading = NO;
                    is_authorized = NO;
                  //  [appDelegate openAuthPage];
                }
            }
//            UIViewController *vc = [[rootMenuController sharedInstance] getMenuController];
//            [vc.navigationController setNavigationBarHidden:NO];
//            [ApplicationDelegate setRootViewController:vc];

        }];
    } else {
        is_loading = NO;
    }
    
    

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
        [appDelegate openAuthPage];
    }
    
}




@end

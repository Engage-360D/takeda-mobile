//
//  AppDelegate.h
//  takeda
//
//  Created by Serg on 3/25/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GoogleMaps/GoogleMaps.h>
#import "Reachability.h"
#import "WelcomePage.h"
#import "AuthPage.h"
#import "GAI.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

-(void)setRootViewController:(UIViewController*)vc;

@property (nonatomic, retain) Reachability *hostReachability;
@property (nonatomic) NetworkStatus hostConnection;

@property (nonatomic, strong) UITapGestureRecognizer *tapToHide;
@property (nonatomic, strong) WelcomePage *welcomePage;
@property (nonatomic, strong) AuthPage *authPage;
@property(nonatomic, strong) id<GAITracker> tracker;

-(void)openWelcomePage;
-(void)openAuthPage;
-(void)resetUser:(NSMutableDictionary*)user;
-(void)logoutUser;


@end

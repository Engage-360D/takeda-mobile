//
//  AppDelegate.m
//  takeda
//
//  Created by Serg on 3/25/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "AppDelegate.h"
#import "TestFairy.h"
#import "AllSocials.h"
#import "NotifPlanner.h"
#import "Path.h"
#import "APIInfo.m"

static NSString *const kAllowTracking = @"allowTracking";


@implementation AppDelegate{
    DejalActivityView *currentActivityView;
}

@synthesize hostReachability;
@synthesize welcomePage;
@synthesize authPage;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    [Path checkDirectories];
    
    [self setupServices];

    if ([UIApplication instancesRespondToSelector:@selector(registerUserNotificationSettings:)]) {
        [[UIApplication sharedApplication] registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeAlert|UIUserNotificationTypeSound
                                                                                                              categories:nil]];
    }
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];

    [self openWelcomePage];
    
    application.applicationIconBadgeNumber = 0;

    self.window.backgroundColor = [UIColor whiteColor];
  //  [self showAllFonts];
  // [self showFonts];
    [self.window makeKeyAndVisible];
    return YES;
}


-(void)setupServices{

    NSDictionary *appDefaults = @{kAllowTracking: @(YES)};
    [[NSUserDefaults standardUserDefaults] registerDefaults:appDefaults];
    // User must be able to opt out of tracking
    [GAI sharedInstance].optOut = ![[NSUserDefaults standardUserDefaults] boolForKey:kAllowTracking];
    
    [GAI sharedInstance].dispatchInterval = 120;
    
    [GAI sharedInstance].trackUncaughtExceptions = YES;
    self.tracker = [[GAI sharedInstance] trackerWithName:@"Takeda app"
                                              trackingId:kGATrackingId];
    
//    NSMutableDictionary *event =
//    [[GAIDictionaryBuilder createEventWithCategory:@"UI"
//                                            action:@"WelcomePageLoaded"
//                                             label:@"Screen"
//                                             value:nil] build];
//    [[GAI sharedInstance].defaultTracker send:event];
    [[GAI sharedInstance] dispatch];

    
    // Reachability
    NSString *remoteHostName = kReachabilityHost;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(networkChanged:) name:kReachabilityChangedNotification object:nil];
    self.hostReachability = [Reachability reachabilityWithHostName:remoteHostName];
    [self.hostReachability startNotifier];

    // Google analytics

    
    
    
    // TestFairy
    [TestFairy begin:kTestFairyId];

    // Google maps
    [GMSServices provideAPIKey:kGoogleMapsId];
}

-(void)openWelcomePage{
    [self initSingletons];
    welcomePage = [[WelcomePage alloc] initWithNibName:@"WelcomePage" bundle:nil];
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:welcomePage];
}

-(void)openAuthPage{
    authPage = [AuthPage new];
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:authPage];
}

-(void)resetUser:(NSMutableDictionary*)user{
    [self logoutUser];
    [UserData saveBasicalUserInfo:user];
    
    welcomePage = [[WelcomePage alloc] initWithNibName:@"WelcomePage" bundle:nil];
    welcomePage.taked = YES;
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:welcomePage];
    [welcomePage start];
}

-(void)logoutUser{
    [User logoutUser];
    [self resetSingletons];
    [self initSingletons];
}

-(void)initSingletons{
    [UserData sharedObject];
    [GlobalData sharedObject];
    [AllSingle sharedInstance];
    [rootMenuController sharedInstance];
}

-(void)resetSingletons{
    [UserData resetData];
    [GlobalData resetData];
    [AllSingle resetData];
    [rootMenuController resetData];
}

- (void)networkChanged:(NSNotification *)notification
{
    NetworkStatus remoteHostStatus = [hostReachability currentReachabilityStatus];
    if (remoteHostStatus == NotReachable) { NSLog(@"not reachable");}
    else if (remoteHostStatus == ReachableViaWiFi) { NSLog(@"wifi"); }
    else if (remoteHostStatus == ReachableViaWWAN) { NSLog(@"carrier"); }
}

-(NetworkStatus)hostConnection{
    NetworkStatus remoteHostStatus = [hostReachability currentReachabilityStatus];
    if (remoteHostStatus == NotReachable){
        [self.hostReachability stopNotifier];
        [self.hostReachability startNotifier];
    }
    return remoteHostStatus;
}


-(void)showAllFonts{

    NSArray *fontFamilies = [UIFont familyNames];   
    
    for (int i = 0; i < [fontFamilies count]; i++)
    {
        NSString *fontFamily = [fontFamilies objectAtIndex:i];
        NSArray *fontNames = [UIFont fontNamesForFamilyName:[fontFamilies objectAtIndex:i]];
        NSLog (@"%@: %@", fontFamily, fontNames);
    }
}

-(void)showFonts{
    
    
    NSMutableArray *fontNames = [[NSMutableArray alloc] init];
    
    NSArray *fontFamilyNames = [UIFont familyNames];
    for (NSString *familyName in fontFamilyNames)
    {
           NSLog(@"Font Family Name = %@", familyName);
        //font names under family
        NSArray *names = [UIFont fontNamesForFamilyName:familyName];
          NSLog(@"Font Names = %@", fontNames);
        // add to array
        [fontNames addObjectsFromArray:names];
    }
    
}




-(void)setRootViewController:(UIViewController*)vc{
    self.window.rootViewController = vc;
    if (vc) {
        [UIView
         transitionWithView:self.window
         duration:0.5
         options:UIViewAnimationOptionTransitionCrossDissolve
         animations:^(void) {
             BOOL oldState = [UIView areAnimationsEnabled];
             [UIView setAnimationsEnabled:NO];
             self.window.rootViewController = vc;
             [UIView setAnimationsEnabled:oldState];
         } 
         completion:nil];
    }
}


-(void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification{
    // if (![notification.fireDate isInPast]){
        NSString *messageText = notification.alertBody;
        FastAlert(@"Напоминание", messageText);
  //  }
   // [[NotifPlanner sharedInstance] removeNotification:notification];
    
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    [UserDefaults setObject:[NSDate date] forKey:@"closedDate"];
    
    
    
    
//    [UserDefaults setObject:[[NSDate date] dateBySubtractingHours:1] forKey:@"closedDate"];

    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    currentActivityView = ([DejalBezelActivityView currentActivityView]);
    [DejalBezelActivityView removeViewAnimated:NO];

    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{

    
    NSDate *closeDate = [UserDefaults objectForKey:@"closedDate"];
    if (closeDate!=nil&&[[closeDate dateByAddingHours:1] isInPast]&&[User is_authorized]){
    // need update after one hour
        [Global showActivityIndicatorWithString:@"Синхонизация" inContainer:self.window];
        [ServData getUserIdData:User.user_id withCompletion:^(BOOL result, NSError* error){
            if (result){
                [User synchronizeUser:User.user_login completition:^(BOOL synchrSuccess, id synchrResult){
                    [Global removeActivityIdicator];

                    if (result){

                    } else {
                        [Helper fastAlert:@"Ошибка загрузки данных пользователя"];
                    }
                }];
                
            } else {
                [Global removeActivityIdicator];
            }
            
        }];
        
    }
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    [GAI sharedInstance].optOut = ![[NSUserDefaults standardUserDefaults] boolForKey:kAllowTracking];

    [FBAppEvents activateApp];
    [FBAppCall handleDidBecomeActive];
    
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    [FBSession.activeSession close];
    
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


// FBSample logic
// In the login workflow, the Facebook native application, or Safari will transition back to
// this applicaiton via a url following the scheme fb[app id]://; the call to handleOpenURL
// below captures the token, in the case of success, on behalf of the FBSession object
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    
    if (![DejalActivityView isActive]&&currentActivityView!=nil) {
        [Global showActivityIndicatorWithString:currentActivityView.activityLabel.text inContainer:currentActivityView.originalView];
    }

    if ([url.absoluteString hasPrefix:@"fb"]){

        [FBAppCall handleOpenURL:url
               sourceApplication:sourceApplication];

    } else if ([url.absoluteString hasPrefix:@"ok"]){
        
        [OKSession.activeSession handleOpenURL:url];

    } else if ([url.absoluteString hasPrefix:@"vk"]){
    
        [VKSdk processOpenURL:url fromApplication:sourceApplication];
        
    } else {
        
        [GPPURLHandler handleURL:url
               sourceApplication:sourceApplication
                      annotation:annotation];

    }
    
    


   // return [OKSession.activeSession handleOpenURL:url];

    
    return YES;
}

//- (BOOL)application:(UIApplication *)application
//            openURL:(NSURL *)url
//  sourceApplication:(NSString *)sourceApplication
//         annotation:(id)annotation
//{
//    return [OKSession.activeSession handleOpenURL:url];
//}


@end

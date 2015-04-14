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

@implementation AppDelegate
@synthesize hostReachability;
@synthesize welcomePage;
@synthesize authPage;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{

    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(networkChanged:) name:kReachabilityChangedNotification object:nil];
   
    NSString *remoteHostName = @"www.apple.com";
    [TestFairy begin:@"0a5b866d28f84bb9bbea5a2948683e8e0be28546"];
    self.hostReachability = [Reachability reachabilityWithHostName:remoteHostName];
    [self.hostReachability startNotifier];

    [Path checkDirectories];
    
    if ([UIApplication instancesRespondToSelector:@selector(registerUserNotificationSettings:)]) {
        [[UIApplication sharedApplication] registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeAlert|UIUserNotificationTypeSound
                                                                                                              categories:nil]];
    }
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    [GMSServices provideAPIKey:@"AIzaSyCJbaqLyduDBLnzodgcq5WdD7ebS2tU2DM"];

    [self openWelcomePage];
    
    application.applicationIconBadgeNumber = 0;

    self.window.backgroundColor = [UIColor whiteColor];
  //  [self showAllFonts];
  // [self showFonts];
    [self.window makeKeyAndVisible];
    return YES;
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
    [User logoutUser];
    [self resetSingletons];
    [self initSingletons];
    [UserData saveBasicalUserInfo:user];
    
    welcomePage = [[WelcomePage alloc] initWithNibName:@"WelcomePage" bundle:nil];
    welcomePage.taked = YES;
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:welcomePage];
    [welcomePage start];
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
    

    
    [FBAppCall handleOpenURL:url
                  sourceApplication:sourceApplication];
    
    [OKSession.activeSession handleOpenURL:url];

    [VKSdk processOpenURL:url fromApplication:sourceApplication];

    [GPPURLHandler handleURL:url
           sourceApplication:sourceApplication
                  annotation:annotation];
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

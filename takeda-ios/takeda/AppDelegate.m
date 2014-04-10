//
//  AppDelegate.m
//  takeda
//
//  Created by Serg on 3/25/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "AppDelegate.h"
#import "WelcomePage.h"
#import "Odnoklassniki.h"
#import "Path.h"

@implementation AppDelegate
WelcomePage *welcomePage;


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [Path checkDirectories];
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    welcomePage = [[WelcomePage alloc] initWithNibName:@"WelcomePage" bundle:nil];
    
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:welcomePage];
    
    self.window.backgroundColor = [UIColor whiteColor];
    [self showAllFonts];
    [self.window makeKeyAndVisible];
    return YES;
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









- (void)applicationWillResignActive:(UIApplication *)application
{
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
    
    return YES;
}


@end

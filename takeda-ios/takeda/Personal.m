//
//  Personal.m
//  takeda
//
//  Created by Alexander Rudenko on 24.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "Personal.h"

@interface Personal ()

@end

@implementation Personal
@synthesize resultRiskAnal;
@synthesize userInfo;
@synthesize settingsVC;
@synthesize controllers;
@synthesize currentVC;
@synthesize selectedTab;

- (void)viewDidLoad
{
    [super viewDidLoad];

    [self setupInterface];
    [self init_controllers];
    [self changeTabToIndex:0];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    UIBarButtonItem *mS = [self highlightedPersonalButton];
    mS.enabled = NO;
    self.navigationItem.rightBarButtonItems = @[mS,[self alarmButton]];
    
}

-(void)init_controllers{
    
    resultRiskAnal = [ResultRiskAnal new];
    userInfo = [UserInfo new];
    settingsVC = [SettingsVC new];
    
    
    controllers = [Global recursiveMutable:@[
                                             @{@"image":@"",@"selected_image":@"", @"title":@"Личный кабинет",@"badge":@"",@"vc":resultRiskAnal},
                                             @{@"image":@"",@"selected_image":@"", @"title":@"Личный кабинет",@"badge":@"",@"vc":userInfo},
                                             @{@"image":@"",@"selected_image":@"",@"title":@"Личный кабинет",@"badge":@"",@"vc":settingsVC},
                                             ]];
}

-(void)setupInterface{

}

-(IBAction)switchTabTo:(UISegmentedControl*)sender{
    [self changeTabToIndex:sender.selectedSegmentIndex];
}

-(void)changeTabToIndex:(int)index{
    self.selectedTab = index;
    [self showVC:index];
}

-(void)showVC:(int)index{
    [currentVC removeFromParentViewController];
    [currentVC.view removeFromSuperview];
    currentVC = [controllers objectAtIndex:index][@"vc"];
    currentVC.parentVC = self;
    // self.title = [controllers objectAtIndex:index][@"title"];
    // currentVC.title = [controllers objectAtIndex:index][@"title"];
    [self addChildViewController:currentVC];
    [self.container addSubview:currentVC.view];
    currentVC.mainElement.frame = self.container.bounds;
    [currentVC didMoveToParentViewController:self];
    currentVC.titleContainer.hidden = YES;

    // [currentVC viewWillAppear:NO];  /// нужно принудительно вызывать (((
}



@end

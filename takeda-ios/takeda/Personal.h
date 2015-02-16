//
//  Personal.h
//  takeda
//
//  Created by Alexander Rudenko on 24.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ResultRiskAnal.h"
#import "UserInfo.h"
#import "SettingsVC.h"

@interface Personal : VControllerExt

@property (nonatomic, strong) IBOutlet UIView *container;
@property (nonatomic, strong) IBOutlet VControllerExt *currentVC;
@property (nonatomic, strong) IBOutlet UISegmentedControl *segmentSwitch;

@property (nonatomic, strong) NSArray *controllers;
@property (nonatomic) int selectedTab;

@property (nonatomic, strong) ResultRiskAnal *resultRiskAnal;
@property (nonatomic, strong) UserInfo *userInfo;
@property (nonatomic, strong) SettingsVC *settingsVC;


@end

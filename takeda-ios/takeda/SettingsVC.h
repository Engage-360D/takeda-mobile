//
//  SettingsVC.h
//  takeda
//
//  Created by Alexander Rudenko on 03.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RAlertView.h"
#import "AddIncident.h"
#import "DrugsList.h"

@interface SettingsVC : VControllerExt
@property (nonatomic, strong) IBOutlet UIButton *deleteAllResults;
@property (nonatomic, strong) IBOutlet UIButton *logoutBtn;
@property (nonatomic, strong) IBOutlet UIButton *addIncident;
@property (nonatomic, strong) IBOutlet UIView *incidentsContainer;
@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (nonatomic,retain) IBOutletCollection(UILabel) NSArray *blockLabels;
@property (nonatomic,retain) IBOutletCollection(UIButton) NSArray *blockBtns;
@property (nonatomic,retain) IBOutletCollection(UIView) NSArray *bg_block;
@property (nonatomic, strong) AddIncident *addIncidentVC;
@property (nonatomic, strong) DrugsList *drugsList;


@end

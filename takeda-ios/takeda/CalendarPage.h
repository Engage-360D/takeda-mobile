//
//  CalendarPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddPills.h"
#import "Drugs.h"
#import "EGORefreshTableHeaderView.h"

@interface CalendarPage : VControllerExt<UITableViewDataSource, UITableViewDelegate, EGORefreshTableHeaderDelegate>

@property (nonatomic, strong) IBOutlet UISegmentedControl *fillEmptySwitch;
@property (nonatomic, assign) UITableView *tableView;
@property (nonatomic, strong) IBOutlet UITableView *tableNewView;
@property (nonatomic, strong) IBOutlet UITableView *tableFillView;

@property (nonatomic) DIndex state;
@property (nonatomic, strong) AddPills *addPills;
@property (nonatomic, strong) Drugs *drugs;

@property (nonatomic, strong) NSMutableArray *days;
@property (nonatomic, strong) NSMutableArray *records;

@property (nonatomic, strong) NSMutableArray *emptyRecords;
@property (nonatomic, strong) NSMutableArray *filledRecords;
@property (nonatomic, strong) NSMutableDictionary *tasks;
@property (nonatomic, strong) EGORefreshTableHeaderView *refreshHeaderView;

+(CombyCellType)cellTypeForTask:(NSString*)taskType;

@end

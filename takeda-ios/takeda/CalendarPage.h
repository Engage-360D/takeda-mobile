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

typedef enum {
    dNew = 1,
    dFilled = 2
} DIndex;


@interface CalendarPage : VControllerExt

@property (nonatomic, strong) IBOutlet UISegmentedControl *fillEmptySwitch;
@property (nonatomic, strong) IBOutlet UITableView *tableView;

@property (nonatomic) DIndex state;
@property (nonatomic, strong) AddPills *addPills;
@property (nonatomic, strong) Drugs *drugs;

@property (nonatomic, strong) NSMutableArray *days;
@property (nonatomic, strong) NSArray *records;

@property (nonatomic, strong) NSMutableArray *emptyRecords;
@property (nonatomic, strong) NSMutableArray *filledRecords;
@property (nonatomic, strong) NSMutableDictionary *tasks;


@end

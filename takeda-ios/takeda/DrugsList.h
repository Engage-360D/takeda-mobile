//
//  DrugsList.h
//  takeda
//
//  Created by Alexander Rudenko on 25.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddPills.h"

@interface DrugsList : VControllerExt<EGORefreshTableHeaderDelegate>

@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSMutableArray *drugs;
@property (nonatomic, strong) AddPills *addPills;

@property (nonatomic, strong) EGORefreshTableHeaderView *refreshHeaderView;

@end

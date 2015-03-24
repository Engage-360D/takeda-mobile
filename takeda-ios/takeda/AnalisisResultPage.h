//
//  AnalisisResultPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EGORefreshTableHeaderView.h"

@interface AnalisisResultPage : VControllerExt<EGORefreshTableHeaderDelegate>
@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) EGORefreshTableHeaderView *refreshHeaderView;

@end

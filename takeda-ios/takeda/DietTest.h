//
//  DietTest.h
//  takeda
//
//  Created by Alexander Rudenko on 26.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DietTestTableCell.h"

@interface DietTest : VControllerExt

@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) IBOutlet UILabel *tLabel;
@property (nonatomic, strong) IBOutlet UIButton *getRecommForDiet;

@end

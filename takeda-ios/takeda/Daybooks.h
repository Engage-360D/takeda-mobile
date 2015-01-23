//
//  Daybooks.h
//  takeda
//
//  Created by Alexander Rudenko on 22.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Daybooks : VControllerExt

@property (nonatomic, strong) IBOutlet UISegmentedControl *typeSwitch;
@property (nonatomic, strong) IBOutlet UITableView *tableView;

@end

//
//  Drugs.h
//  takeda
//
//  Created by Alexander Rudenko on 21.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "StandartCombyCell.h"

@interface Drugs : VControllerExt
@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) IBOutlet UILabel *bottomText;
@property (nonatomic, strong) IBOutlet UIButton *goToDoctorBtn;

@end

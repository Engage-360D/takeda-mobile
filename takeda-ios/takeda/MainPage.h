//
//  MainPage.h
//  takeda
//
//  Created by Alexander Rudenko on 26.12.14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "StandartCombyCell.h"

@interface MainPage : UIViewController

@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) IBOutlet UILabel *percentLabel;
@property (nonatomic, strong) IBOutlet UILabel *indexCaptionLabel;
@property (nonatomic, strong) IBOutlet UILabel *datePeriod;
@property (nonatomic, strong) IBOutlet UILabel *todayLabel;
@property (nonatomic,retain) IBOutlet UILabel *danger_text;

@end

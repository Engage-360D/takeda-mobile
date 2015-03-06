//
//  DietTestResults.h
//  takeda
//
//  Created by Alexander Rudenko on 05.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "riskCell.h"
#import "StandartCombyCell.h"

@interface DietTestResults : VControllerExt

@property (nonatomic, strong) NSMutableDictionary *result;
@property (nonatomic, strong) NSMutableArray *resultArray;

@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) IBOutlet UIView *headerView;
@property (nonatomic, strong) IBOutlet UITextView *titleText;
@property (nonatomic, strong) IBOutlet UITextView *messageText;
@property (nonatomic, strong) IBOutlet UIButton *makeTestRep;
@property (nonatomic, strong) IBOutlet UIButton *getRecommForDiet;

@end

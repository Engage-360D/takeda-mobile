//
//  ResultRiskAnalysis.h
//  takeda
//
//  Created by Serg on 4/6/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ResultRiskAnalysis : VControllerExt
@property (nonatomic,retain) IBOutlet UITableView *tableView;
@property (nonatomic,retain) IBOutlet UIView *headerView;



@property (nonatomic,retain) IBOutlet UILabel  *scoreValue;
@property (nonatomic,retain) IBOutlet UIImageView *scoreCircle;

@property BOOL needUpdate;
@end

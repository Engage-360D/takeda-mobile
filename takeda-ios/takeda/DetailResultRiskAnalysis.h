//
//  DetailResultRiskAnalysis.h
//  takeda
//
//  Created by Serg on 4/9/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailResultRiskAnalysis : UIViewController
@property (nonatomic,retain) NSDictionary *data_result;
@property (nonatomic,retain) NSDictionary *data_banner;
@property (nonatomic,retain) NSDictionary *data_page;
@property (nonatomic,retain) IBOutlet UILabel  *titleResult;
@end

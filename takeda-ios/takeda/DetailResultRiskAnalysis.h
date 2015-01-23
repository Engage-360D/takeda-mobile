//
//  DetailResultRiskAnalysis.h
//  takeda
//
//  Created by Serg on 4/9/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailResultRiskAnalysis : VControllerExt
@property (nonatomic,retain) NSDictionary *data_result;
@property (nonatomic,retain) NSDictionary *data_banner;
@property (nonatomic,retain) NSDictionary *data_page;
@property (nonatomic,retain) IBOutlet UIScrollView *scrol;
@end

//
//  RiskAnalysisPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RiskAnalysisPage : UIViewController
@property (nonatomic,retain) IBOutlet UIScrollView *scroll;
@property (nonatomic,retain) IBOutletCollection(UIImageView) NSArray *page_indicator;
@end

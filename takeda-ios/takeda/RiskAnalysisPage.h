//
//  RiskAnalysisPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AnalizDataUserPage.h"


@interface RiskAnalysisPage : UIViewController<UIPickerViewDelegate, UIPickerViewDataSource,AnalizDataUserPageDelegate>
@property (nonatomic,retain) IBOutlet UIScrollView *scroll;
@property (nonatomic,retain) IBOutletCollection(UIImageView) NSArray *page_indicator;
@end

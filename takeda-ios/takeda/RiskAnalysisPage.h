//
//  RiskAnalysisPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AnalizDataUserPage.h"
#import "ResultRiskAnal.h"
#import "CActionSheet.h"

@interface RiskAnalysisPage : VControllerExt<UIPickerViewDelegate, UIPickerViewDataSource,AnalizDataUserPageDelegate>
@property (nonatomic,retain) IBOutlet UIScrollView *scroll;
@property (nonatomic,retain) IBOutletCollection(UIImageView) NSArray *page_indicator;
@property (nonatomic,retain) ResultRiskAnal *resultRiskAnalysis;

-(void)analizDataUserPage:(AnalizDataUserPage*)analizPage openList:(NSString*)type sourceData:(NSMutableDictionary*)sd;

@end

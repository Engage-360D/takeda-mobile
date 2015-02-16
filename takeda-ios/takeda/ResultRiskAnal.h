//
//  ResultRiskAnal.h
//  takeda
//
//  Created by Alexander Rudenko on 25.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SearchInstitutionPage.h"
#import "DietTest.h"
#import "ResultRiskAnalDetail.h"
#import "UsefulKnowPage.h"

typedef enum {
    sOk = 1,
    sAlert = 2,
    sBell = 3,
    sDoctor = 4
} RecommendationState;

@interface ResultRiskAnal : VControllerExt

@property (nonatomic,retain) IBOutlet UILabel *mainRecomendation;
@property (nonatomic,retain) IBOutlet UILabel *scoreNoteText;
@property (nonatomic,retain) IBOutlet UIView *scoreLineContainer;
@property (nonatomic,retain) IBOutlet UIView *headerView;
@property (nonatomic,retain) IBOutlet UILabel *scoreValue;
@property (nonatomic,retain) IBOutlet UIImageView *scoreCircle;
@property (nonatomic,retain) IBOutlet UITableView *tableView;
@property (nonatomic,retain) IBOutlet UIButton *profilacticCalendarBtn;
@property (nonatomic,retain) IBOutlet UIButton *medSearchBtn;
@property (nonatomic, strong) SearchInstitutionPage *searchInstitutionPage;
@property (nonatomic, strong) DietTest *dietTest;
@property (nonatomic, strong) ResultRiskAnalDetail *resultRiskAnalDetail;
@property (nonatomic, strong) UsefulKnowPage *usefulKnowPage;

@property (nonatomic, strong) NSMutableDictionary *results_data;

@property BOOL needUpdate;

@end

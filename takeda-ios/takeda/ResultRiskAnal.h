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
#import "InfoPage.h"
#import "DietTestResults.h"


typedef enum {
    sOk = 1,
    sAlert = 2,
    sBell = 3,
    sDoctor = 4
} RecommendationState;

@interface ResultRiskAnal : VControllerExt

@property (nonatomic,retain) IBOutlet UILabel *mainRecomendation;
@property (nonatomic,retain) IBOutlet UILabel *scoreNoteText;
@property (nonatomic,retain) IBOutlet UIView *headerView;

@property (nonatomic,retain) IBOutlet UIView *scoreLineContainer;
@property (nonatomic,retain) IBOutlet UILabel *scoreValue;
@property (nonatomic,retain) IBOutlet UIImageView *scoreCircle;

@property (nonatomic,retain) IBOutlet UIImageView *stateImage;
@property (nonatomic,retain) IBOutlet UITableView *tableView;
@property (nonatomic,retain) IBOutlet UIButton *profilacticCalendarBtn;
@property (nonatomic,retain) IBOutlet UIButton *medSearchBtn;
@property (nonatomic,retain) IBOutlet UIButton *infoBtn;
@property (nonatomic, strong) SearchInstitutionPage *searchInstitutionPage;
@property (nonatomic, strong) ResultRiskAnalDetail *resultRiskAnalDetail;
@property (nonatomic, strong) InfoPage *infoPage;
@property (nonatomic, strong) DietTest *dietTest;
@property (nonatomic, strong) DietTestResults *dietTestResults;
@property (nonatomic, strong) NSMutableDictionary *results_data;

@property BOOL needUpdate;
@property BOOL disableBack;

@property (nonatomic,retain) IBOutlet UIImageView *stateImageRed;
@property (nonatomic,retain) IBOutlet UILabel *mainRecomendationRed;
@property (nonatomic,retain) IBOutlet UILabel *scoreNoteTextRed;
@property (nonatomic,retain) IBOutlet UIView *headerViewRed;
@property (nonatomic,retain) IBOutlet UIView *scoreLineContainerRed;
@property (nonatomic,retain) IBOutlet UILabel *scoreValueRed;
@property (nonatomic,retain) IBOutlet UIImageView *scoreCircleRed;
@property (nonatomic,retain) IBOutlet UIButton *medSearchBtnRed;
@property (nonatomic,retain) IBOutlet UIScrollView *scrollViewRed;
@property (nonatomic,retain) IBOutlet UIImageView *separ1;
@property (nonatomic,retain) IBOutlet UIImageView *separ2;
@property (nonatomic,retain) IBOutlet UIView *containerAll;

-(void)goToTest;
-(void)goToResult:(NSMutableDictionary*)result;



@end

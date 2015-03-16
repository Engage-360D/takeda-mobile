//
//  MainPage.h
//  takeda
//
//  Created by Alexander Rudenko on 26.12.14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "StandartCombyCell.h"
#import "Drugs.h"
#import "ResultRiskAnal.h"
#import "EGORefreshTableHeaderView.h"
#import "CalendarPage.h"
#import "AnalisisResultPage.h"

@interface MainPage : VControllerExt<EGORefreshTableHeaderDelegate>

@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) IBOutlet UILabel *percentLabel;
@property (nonatomic, strong) IBOutlet UILabel *indexCaptionLabel;
@property (nonatomic, strong) IBOutlet UILabel *datePeriod;
@property (nonatomic, strong) IBOutlet UILabel *todayLabel;
@property (nonatomic, strong) Drugs *drugs;
@property (nonatomic, strong) ResultRiskAnal *resultRiskAnal;
@property (nonatomic, strong) CalendarPage *calendarPage;
@property (nonatomic, strong) AnalisisResultPage *analisisResultPage;

@property (nonatomic,retain) IBOutlet UIImageView *stateImageRed;
@property (nonatomic,retain) IBOutlet UIImageView *separ1;
@property (nonatomic,retain) IBOutlet UIImageView *separ2;

@property (nonatomic,retain) IBOutlet UILabel *mainRecomendationRed;
@property (nonatomic,retain) IBOutlet UILabel *scoreNoteTextRed;
@property (nonatomic,retain) IBOutlet UIView *headerViewRed;
@property (nonatomic,retain) IBOutlet UIView *normHeader;
@property (nonatomic,retain) IBOutlet UIButton *medSearchBtnRed;
@property (nonatomic,retain) IBOutlet UIScrollView *scrollViewRed;

@property (nonatomic, strong) NSMutableArray *days;
@property (nonatomic, strong) NSMutableArray *records;
//
//@property (nonatomic, strong) NSMutableArray *emptyRecords;
//@property (nonatomic, strong) NSMutableArray *filledRecords;
@property (nonatomic, strong) NSMutableDictionary *tasks;
@property (nonatomic,retain) IBOutlet UIActivityIndicatorView *indLoading;
@property (nonatomic, strong) EGORefreshTableHeaderView *refreshHeaderView;


@end

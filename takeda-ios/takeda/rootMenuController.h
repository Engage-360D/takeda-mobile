//
//  rootMenuController.h
//  takeda
//
//  Created by Serg on 3/28/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>


@class LeftMenu;
@class RiskAnalysisPage;
@class SearchInstitutionPage;
@class RecomendationPage;
@class AnalisisResultPage;
@class CalendarPage;
@class UsefulKnowPage;
@class PublicationPage;
@class ReportsPage;
@class NVSlideMenuController;
@class MainPage;
@class ResultRiskAnal;

@interface rootMenuController : NSObject

@property (nonatomic, strong) RiskAnalysisPage *riskAnalysisPage;
@property (nonatomic, strong) SearchInstitutionPage* searchInstitutionPage;
@property (nonatomic, strong) RecomendationPage *recomendationPage;
@property (nonatomic, strong) AnalisisResultPage *analisisResultPage;
@property (nonatomic, strong) CalendarPage *calendarPage;
@property (nonatomic, strong) UsefulKnowPage *usefulKnowPage;
@property (nonatomic, strong) PublicationPage *publicationPage;
@property (nonatomic, strong) ReportsPage *reportsPage;
@property (nonatomic, strong) LeftMenu *leftMenu;
@property (nonatomic, strong) MainPage *mainPage;
@property (nonatomic, strong) ResultRiskAnal *resultRiskAnal;
@property (nonatomic, strong) NVSlideMenuController *slideMenuVC;

@property (nonatomic, strong) UIViewController *riskAnalysis_vc;
@property (nonatomic, strong) UIViewController *mainPage_vc;


+(rootMenuController*)sharedInstance;
+(void)resetData;

-(LeftMenu*)getLeftMenu;
-(RiskAnalysisPage*)getRiskAnalysisPage;
-(SearchInstitutionPage*)getSearchInstitutionPage;
-(RecomendationPage*)getRecomendationPage;
-(AnalisisResultPage*)getAnalisisResultPage;
-(CalendarPage*)getCalendarPage;
-(UsefulKnowPage*)getUsefulKnowPage;
-(PublicationPage*)getPublicationPage;
-(ReportsPage*)getReportsPage;
-(NVSlideMenuController*)getMenuController;
-(MainPage*)getMainPage;
-(ResultRiskAnal*)getResultRiskAnal;

-(UIViewController*)riskAnalysis_vc;
-(void)resetControllers;

@end

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
+(rootMenuController*)sharedInstance;
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

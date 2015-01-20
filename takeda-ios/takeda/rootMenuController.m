//
//  rootMenuController.m
//  takeda
//
//  Created by Serg on 3/28/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "rootMenuController.h"

#import "RiskAnalysisPage.h"
#import "SearchInstitutionPage.h"
#import "RecomendationPage.h"
#import "AnalisisResultPage.h"
#import "CalendarPage.h"
#import "UsefulKnowPage.h"
#import "PublicationPage.h"
#import "ReportsPage.h"
#import "MainPage.h"

#import "LeftMenu.h"



@implementation rootMenuController{
    RiskAnalysisPage *riskAnalysisPage;
    SearchInstitutionPage* searchInstitutionPage;
    RecomendationPage *recomendationPage;
    AnalisisResultPage *analisisResultPage;
    CalendarPage *calendarPage;
    UsefulKnowPage *usefulKnowPage;
    PublicationPage *publicationPage;
    ReportsPage *reportsPage;
    LeftMenu *leftMenu;
    MainPage *mainPage;

    NVSlideMenuController *slideMenuVC;
    
    UIViewController *riskAnalysis_vc;
    UIViewController *mainPage_vc;

}
static rootMenuController *sharedInst = NULL;


+(rootMenuController*)sharedInstance{
    if (!sharedInst || sharedInst == NULL) {
        sharedInst = [rootMenuController new];
    }
    return sharedInst;
}


-(NVSlideMenuController*)getMenuController{
    if (!slideMenuVC) {
        slideMenuVC = [[NVSlideMenuController alloc] initWithMenuViewController:[self getLeftMenu] andContentViewController:[self mainPage_vc]];
    }
    
    return slideMenuVC;
}


-(UIViewController*)riskAnalysis_vc{
    if (!riskAnalysis_vc) {
        riskAnalysis_vc = [[UINavigationController alloc] initWithRootViewController:[self getRiskAnalysisPage]];
    }
    return riskAnalysis_vc;
}

-(UIViewController*)mainPage_vc{
    if (!mainPage_vc) {
        mainPage_vc = [[UINavigationController alloc] initWithRootViewController:[self getMainPage]];
    }
    return mainPage_vc;
}

-(LeftMenu*)getLeftMenu{
    if (!leftMenu) {
        leftMenu = [[LeftMenu alloc] initWithNibName:@"LeftMenu" bundle:nil];
    }
    return leftMenu;
}


-(RiskAnalysisPage*)getRiskAnalysisPage{
    if (!riskAnalysisPage) {
        riskAnalysisPage = [[RiskAnalysisPage alloc] initWithNibName:@"RiskAnalysisPage" bundle:nil];
    }
    return riskAnalysisPage;
}


-(SearchInstitutionPage*)getSearchInstitutionPage{
    if (!searchInstitutionPage) {
        searchInstitutionPage = [[SearchInstitutionPage alloc] initWithNibName:@"SearchInstitutionPage" bundle:nil];
    }
    return searchInstitutionPage;
}

-(RecomendationPage*)getRecomendationPage{
    if (!recomendationPage) {
        recomendationPage = [[RecomendationPage alloc] initWithNibName:@"RecomendationPage" bundle:nil];
    }
    return recomendationPage;
}

-(MainPage*)getMainPage{
    if (!mainPage) {
        mainPage = [[MainPage alloc] initWithNibName:@"MainPage" bundle:nil];
    }
    return mainPage;
}

-(AnalisisResultPage*)getAnalisisResultPage{
    if (!analisisResultPage) {
        analisisResultPage = [[AnalisisResultPage alloc] initWithNibName:@"AnalisisResultPage" bundle:nil];
    }
    return analisisResultPage;
}

-(CalendarPage*)getCalendarPage{
    if (!calendarPage) {
        calendarPage = [[CalendarPage alloc] initWithNibName:@"CalendarPage" bundle:nil];
    }
    return calendarPage;
}

-(UsefulKnowPage*)getUsefulKnowPage{
    if (!usefulKnowPage) {
        usefulKnowPage = [[UsefulKnowPage alloc] initWithNibName:@"UsefulKnowPage" bundle:nil];
    }
    return usefulKnowPage;
}

-(PublicationPage*)getPublicationPage{
    if (!publicationPage) {
        publicationPage = [[PublicationPage alloc] initWithNibName:@"PublicationPage" bundle:nil];
    }
    return publicationPage;
}

-(ReportsPage*)getReportsPage{
    if (!reportsPage) {
        reportsPage = [[ReportsPage alloc] initWithNibName:@"ReportsPage" bundle:nil];
    }
    return reportsPage;
}

@end

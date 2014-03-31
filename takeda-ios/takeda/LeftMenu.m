//
//  LeftMenu.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "LeftMenu.h"
#import "menu_cell.h"

#import "RiskAnalysisPage.h"
#import "SearchInstitutionPage.h"
#import "RecomendationPage.h"
#import "AnalisisResultPage.h"
#import "CalendarPage.h"
#import "UsefulKnowPage.h"
#import "PublicationPage.h"
#import "ReportsPage.h"

@interface LeftMenu (){
    StateMenu last_stateMenu;
    NSArray *menuData;
    
    UIViewController *riskAnalysis_vc;
    UIViewController *searchInstitution_vc;
    UIViewController *recomendation_vc;
    UIViewController *analisisResult_vc;
    UIViewController *calendarPage_vc;
    UIViewController *usefulKnowPage_vc;
    UIViewController *publication_vc;
    UIViewController *reportsPage_vc;
    
}

@end

@implementation LeftMenu
@synthesize tableView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    last_stateMenu = State_Risk_Analysis;
    menuData =
    @[@{@"name" :@"Анализ риска"},
      @{@"name" :@"Поиск учреждений"},
      @{@"name" :@"Рекомендации"},
      @{@"name" :@"Результаты анализа"},
      @{@"name" :@"Календарь"},
      @{@"name" :@"Полезно знать"},
      @{@"name" :@"Публикации"},
      @{@"name" :@"Отчеты"}
      ];
    //self.na
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [menuData count];
    self.tableView.scrollEnabled = NO;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"menu_cell";
    
    menu_cell *cell = (menu_cell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"menu_cell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[menu_cell class]])
            {
                cell = (menu_cell *)currentObject;
                break;
            }
        }
    }

    cell.name_group.textColor = [UIColor whiteColor]; ///RGB(60.0, 184.0, 120.0);
    [cell.name_group setFont:[UIFont fontWithName:@"HelveticaNeueCyr-Thin" size:23]];
    cell.name_group.text = [[menuData objectAtIndex:indexPath.row] objectForKey:@"name"];
    
    
    
    cell.backgroundColor = [UIColor clearColor];
    
    
    UIView *selectedView = [UIView new];
    selectedView.backgroundColor = [UIColor clearColor];
    cell.selectedBackgroundView = selectedView;
    
    
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    switch ((indexPath.row+1)) {
        case State_Risk_Analysis:{
            [GlobalSettings sharedInstance].stateMenu = State_Risk_Analysis;
            
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                
                
                if (!riskAnalysis_vc) {
                    riskAnalysis_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getRiskAnalysisPage]];
                }
                
                [self.slideMenuController closeMenuBehindContentViewController:riskAnalysis_vc animated:YES completion:nil];
            }
            break;}
        case State_Search_Institution:{
            [GlobalSettings sharedInstance].stateMenu = State_Search_Institution;
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                
                if (!searchInstitution_vc) {
                    searchInstitution_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getSearchInstitutionPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:searchInstitution_vc animated:YES completion:nil];
                
                
            }
            break;}
        case State_Recomendation:{
            [GlobalSettings sharedInstance].stateMenu = State_Recomendation;
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!recomendation_vc) {
                    recomendation_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getRecomendationPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:recomendation_vc animated:YES completion:nil];
                
            }
            
            break;}
        case State_Analysis_Result:{
            [GlobalSettings sharedInstance].stateMenu = State_Analysis_Result;
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!analisisResult_vc) {
                    analisisResult_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getAnalisisResultPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:analisisResult_vc animated:YES completion:nil];
                
            }
            break;}
        case State_Calendar:{
            [GlobalSettings sharedInstance].stateMenu = State_Calendar;
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!calendarPage_vc) {
                    calendarPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getCalendarPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:calendarPage_vc animated:YES completion:nil];
                
            }
            break;}
        case State_Useful_Know:{
            [GlobalSettings sharedInstance].stateMenu = State_Useful_Know;
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!usefulKnowPage_vc) {
                    usefulKnowPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getUsefulKnowPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:usefulKnowPage_vc animated:YES completion:nil];
                
            }
            break;}
        case State_Publication:{
            [GlobalSettings sharedInstance].stateMenu = State_Publication;
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!publication_vc) {
                    publication_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getPublicationPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:publication_vc animated:YES completion:nil];
                
            }
            break;}
        case State_Reports:{
            [GlobalSettings sharedInstance].stateMenu = State_Reports;
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!reportsPage_vc) {
                    reportsPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getReportsPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:reportsPage_vc animated:YES completion:nil];
                
            }
            break;}
        default:
            break;
    }
    
    
    [self.tableView reloadData];
}


-(BOOL)checkLastController{
    if ([GlobalSettings sharedInstance].stateMenu==last_stateMenu) {
        return YES;
    }else{
        return FALSE;
    }
}

@end

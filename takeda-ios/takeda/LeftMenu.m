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
#import "ResultRiskAnal.h"
#import "CalendarPage.h"
#import "UsefulKnowPage.h"
#import "PublicationPage.h"
#import "ReportsPage.h"
#import "MainPage.h"

@interface LeftMenu (){
    StateMenu last_stateMenu;
    NSArray *menuData;
    
    UIViewController *riskAnalysis_vc;
    UIViewController *searchInstitution_vc;
    UIViewController *recomendation_vc;
    UIViewController *analisisResult_vc;
    UIViewController *resultRiskAnal;
    UIViewController *calendarPage_vc;
    UIViewController *usefulKnowPage_vc;
    UIViewController *publication_vc;
    UIViewController *reportsPage_vc;
    UIViewController *mainPage_vc;
    BOOL needTest;
    /*
     RiskAnalysisPage *riskAnalysis_vc;
     SearchInstitutionPage *searchInstitution_vc;
     RecomendationPage *recomendation_vc;
     AnalisisResultPage *analisisResult_vc;
     ResultRiskAnal *resultRiskAnal;
     CalendarPage *calendarPage_vc;
     UsefulKnowPage *usefulKnowPage_vc;
     PublicationPage *publication_vc;
     ReportsPage *reportsPage_vc;
     MainPage *mainPage_vc;
     */
    
}

@end

@implementation LeftMenu
@synthesize tableView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [self updateMenuData];
    }
    return self;
}

-(void)updateMenuData{
    needTest = [[rootMenuController sharedInstance] checkToNeedTest];
    menuData = [Global recursiveMutable:
                @[@{@"name" :@"Главная", @"enabled":@"YES"},
                  @{@"name" :@"Анализ риска", @"enabled":needTest?@"YES":@"NO"},
                  @{@"name" :@"Поиск учреждений", @"enabled":@"YES"},
                  @{@"name" :@"Рекомендации", @"enabled":@"NO"},
                  @{@"name" :@"Результаты анализа", @"enabled":[[GlobalData resultAnalyses] count]?@"YES":@"NO"},
                  @{@"name" :@"Календарь", @"enabled":@"YES"},
                  @{@"name" :@"Полезно знать", @"enabled":@"YES"},
                  @{@"name" :@"Публикации", @"enabled":@"YES"},
                  @{@"name" :@"Отчеты", @"enabled":@"YES"}
                  ]];

}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self updateMenuData];
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
   // [GlobalSettings sharedInstance].stateMenu = State_MainPage;
    last_stateMenu = State_MainPage;
    }

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self updateMenuData];
    [self.tableView reloadData];
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
   
    cell.name_group.text = [[menuData objectAtIndex:indexPath.row] objectForKey:@"name"];
    [cell.name_group setFont:[UIFont fontWithName:@"Helvetica-Light" size:18]];
    
    
    if (indexPath.row == 0) {
        cell.top_separator.hidden = NO;
    }else{
        cell.top_separator.hidden = YES;
    }
    
    
    
    cell.backgroundColor = [UIColor clearColor];
    
    
    UIView *selectedView = [UIView new];
    selectedView.backgroundColor = [UIColor clearColor];
    cell.selectedBackgroundView = selectedView;
    
    BOOL enabled;
    
    if (needTest){
        enabled = NO;
    } else {
        enabled = [[[menuData objectAtIndex:indexPath.row] objectForKey:@"enabled"] boolValue];
    }
    
    if (enabled) {
        cell.name_group.textColor = [UIColor whiteColor];
    } else{
        cell.name_group.textColor = RGB(95, 95, 95);
    }

    if ([GlobalSettings sharedInstance].stateMenu == indexPath.row+1) {
        cell.name_group.textColor = RGB(150, 190, 190);
    }
    cell.disabled = !enabled;
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)table didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    BOOL enabled = !((menu_cell*)[table cellForRowAtIndexPath:indexPath]).disabled;
    if (enabled){
        [self selectMenuIndex:indexPath.row+1];
    } else {
        [self showErrorForCellIndex:indexPath.row+1];
    }
}

-(void)showErrorForCellIndex:(int)index{
    switch (index) {
        case State_MainPage:{
            break;
        }
            
        case State_Risk_Analysis:{
            if (![[rootMenuController sharedInstance] checkToNeedTest]){
                [self showMessage:@"Вы можете проходить тест только один раз в месяц" title:@"Отказ"];
            }

            if (appDelegate.hostConnection == NotReachable) {
                FastAlert(@"Ошибка", @"Нет соединения с интернетом");
            }
            
            break;
        }
        case State_Search_Institution:{
            break;
        }
            
        case State_Recomendation:{
            break;
        }
            
        case State_Analysis_Result:{
            if ([[GlobalData resultAnalyses] count]==0){
                FastAlert(@"Ошибка", @"У вас еще нет результатов анализа");
            }
            break;
        }
            
        case State_Calendar:{
            break;
        }
            
        case State_Useful_Know:{
            break;
        }
            
        case State_Publication:{
            break;
        }
            
        case State_Reports:{
            break;
        }
            
        default:
            break;
    }
    [self.tableView reloadData];

}

-(void)selectMenuIndex:(int)index{
    switch (index) {
        case State_MainPage:{
            [GlobalSettings sharedInstance].stateMenu = State_MainPage;
            
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            } else{
                if (!mainPage_vc) {
                    mainPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getMainPage]];
                }
                
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:mainPage_vc animated:YES completion:nil];
                
            }
            break;}
            
        case State_Risk_Analysis:{
            
            if (appDelegate.hostConnection == NotReachable) {
                FastAlert(@"Ошибка", @"Нет соединения с интернетом");
                return;
            }
            
            if (![[rootMenuController sharedInstance] checkToNeedTest]){
                // рано еще тест проходить
                [self showMessage:@"Вы можете проходить тест только один раз в месяц" title:@"Отказ"];
                return;

            }
            
            
            [GlobalSettings sharedInstance].stateMenu = State_Risk_Analysis;
            
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                
                [self.slideMenuController closeMenuBehindContentViewController:[[rootMenuController sharedInstance] riskAnalysis_vc] animated:YES completion:nil];
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
                if (![User checkForRole:tDoctor]){
                    if (!resultRiskAnal) {
                        resultRiskAnal = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getResultRiskAnal]];
                    }
                    last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                    ((ResultRiskAnal*)((UINavigationController*)resultRiskAnal).viewControllers.firstObject).isFromMenu = YES;
                    [self.slideMenuController closeMenuBehindContentViewController:resultRiskAnal animated:YES completion:nil];
                } else {
                    if (!analisisResult_vc) {
                        analisisResult_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getAnalisisResultPage]];
                    }
                    last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                    ((AnalisisResultPage*)((UINavigationController*)analisisResult_vc).viewControllers.firstObject).isFromMenu = YES;
                    [self.slideMenuController closeMenuBehindContentViewController:analisisResult_vc animated:YES completion:nil];
                }
                
                
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

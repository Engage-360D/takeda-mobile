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
    BOOL NetworkLoss;
    
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
//    User.cashed_checkToNeedTest; //needTest
//    User.userBlocked;  //userIsBlocked
    [User updateCashed];
    
    menuData = [Global recursiveMutable: [LeftMenu menu_data]];
}

+(NSArray*)menu_data{
    return @[@{@"name" :@"Главная", @"item":[NSNumber numberWithInt:State_MainPage], @"enabled":@"YES"},
             @{@"name" :@"Анализ усугубляющих факторов", @"item":[NSNumber numberWithInt:State_Risk_Analysis], @"enabled":(!User.cashed_userBlocked&&User.cashed_checkToNeedTest)||[User checkForRole:tDoctor]?@"YES":@"NO"},
             @{@"name" :@"Поиск учреждений", @"item":[NSNumber numberWithInt:State_Search_Institution], @"enabled":@"YES"},
             //  @{@"name" :@"Рекомендации", @"item":[NSNumber numberWithInt:State_Recomendation], @"enabled":User.cashed_userBlocked?@"NO":@"NO"},
             @{@"name" :@"Как модифицировать усугубляющие факторы?", @"item":[NSNumber numberWithInt:State_Analysis_Result], @"enabled":[[GlobalData resultAnalyses] count]?@"YES":@"NO"},
             @{@"name" :@"Дневник", @"item":[NSNumber numberWithInt:State_Calendar], @"enabled":User.cashed_userBlocked?@"NO":@"YES"},
             @{@"name" :@"Полезно знать", @"item":[NSNumber numberWithInt:State_Useful_Know], @"enabled":User.cashed_userBlocked?@"NO":@"YES"},
             // @{@"name" :@"Публикации", @"item":[NSNumber numberWithInt:State_Publication], @"enabled":User.cashed_userBlocked?@"NO":@"YES"},
             @{@"name" :@"Отчеты", @"item":[NSNumber numberWithInt:State_Reports], @"enabled":User.cashed_userBlocked?@"NO":@"YES"}
             ];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self updateMenuData];
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    }

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self updateMenuData];
    [self.tableView reloadData];
    [self updateScreenName];
}

-(void)openMainPage{
    [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_MainPage];

            mainPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getMainPage]];
    
        [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
        [self.slideMenuController closeMenuBehindContentViewController:mainPage_vc animated:NO completion:nil];
}

-(void)openScreenOnIncidentFrom:(id)from{

    [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_MainPage];
    
//    if (!mainPage_vc) {
        mainPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getMainPage]];
//    }
    
    [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
    [from closeMenuBehindContentViewController:mainPage_vc animated:NO completion:nil];
}

-(void)updateScreenName{
    NSString *scrname = menuData[[GlobalSettings sharedInstance].stateMenu][@"name"];
    
    if (![scrname isEqualToString:self.screenName]){
        self.screenName = scrname;
        NSLog(@"scrname = %@", scrname);

    }
    
//    self.screenName = @"";
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
    
    if (User.cashed_checkToNeedTest&&!User.cashed_userBlocked){
        enabled = NO;
    } else {
        enabled = [[[menuData objectAtIndex:indexPath.row] objectForKey:@"enabled"] boolValue];
    }
    
    if (enabled) {
        cell.name_group.textColor = [UIColor whiteColor];
    } else{
        cell.name_group.textColor = RGB(95, 95, 95);
    }

    if ([GlobalSettings sharedInstance].stateMenu == indexPath.row) {
        cell.name_group.textColor = RGB(150, 190, 190);
    }
    cell.disabled = !enabled;
    
    float h = [Global getHeight:cell.name_group.text font:cell.name_group.font width:self.tableView.width-44];
    cell.name_group.frame = CGRectMake(22, 11, self.tableView.width-44, h);
    cell.height = h+22;

    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    menu_cell *cell = (menu_cell*)[self tableView:self.tableView cellForRowAtIndexPath:indexPath];
    float h = [Global getHeight:cell.name_group.text font:cell.name_group.font width:self.tableView.width-44];
    cell.name_group.frame = CGRectMake(22, 11, self.tableView.width-44, h);
    return h+22;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)table didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    BOOL enabled = !((menu_cell*)[table cellForRowAtIndexPath:indexPath]).disabled;
    if (enabled){
        [self selectMenuIndex:indexPath.row];
    } else {
        [self showErrorForCellIndex:indexPath.row];
    }
    [self updateScreenName];
}

-(void)showErrorForCellIndex:(int)index{
    switch ([menuData[index][@"item"] intValue]) {
        case State_MainPage:{
            break;
        }
            
        case State_Risk_Analysis:{
            if (!User.cashed_checkToNeedTest&&![User checkForRole:tDoctor]){
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
    [self openMenuPage:[menuData[index][@"item"] intValue]];
    [self.tableView reloadData];
    
}

-(void)openMenuPage:(StateMenu)menuPage{
    switch (menuPage) {
        case State_MainPage:{
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_MainPage];
            
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            } else{
                if (!mainPage_vc) {
                    mainPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getMainPage]];
                }
                
                [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:mainPage_vc animated:YES completion:nil];
                
            }
            break;}
            
        case State_Risk_Analysis:{
            
            if (appDelegate.hostConnection == NotReachable) {
                FastAlert(@"Ошибка", @"Нет соединения с интернетом");
                return;
            }
            
            if (!User.cashed_checkToNeedTest&&![User checkForRole:tDoctor]){
                // рано еще тест проходить
                [self showMessage:@"Вы можете проходить тест только один раз в месяц" title:@"Отказ"];
                return;
                
            }
            
            
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Risk_Analysis];
            
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                
                if ([[((UINavigationController*)[[rootMenuController sharedInstance] riskAnalysis_vc]).viewControllers lastObject] isKindOfClass:[ResultRiskAnal class]]){
                    [rootMenuController sharedInstance].riskAnalysis_vc = nil;
                    [rootMenuController sharedInstance].riskAnalysisPage = nil;
                    [analizData resetData];
                }
                
                [self.slideMenuController closeMenuBehindContentViewController:[[rootMenuController sharedInstance] riskAnalysis_vc] animated:YES completion:nil];
            }
            break;}
        case State_Search_Institution:{
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Search_Institution];
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            } else{
                
                if (!searchInstitution_vc) {
                    searchInstitution_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getSearchInstitutionPage]];
                }
                
                [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:searchInstitution_vc animated:YES completion:nil];
                
                
            }
            break;}
        case State_Recomendation:{
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Recomendation];
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!recomendation_vc) {
                    recomendation_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getRecomendationPage]];
                }
                
                [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:recomendation_vc animated:YES completion:nil];
                
            }
            
            break;}
        case State_Analysis_Result:{
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Analysis_Result];
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (![User checkForRole:tDoctor]){
                    if (!resultRiskAnal) {
                        resultRiskAnal = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getResultRiskAnal]];
                    }
                    [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                    ((ResultRiskAnal*)((UINavigationController*)resultRiskAnal).viewControllers.firstObject).isFromMenu = YES;
                    [self.slideMenuController closeMenuBehindContentViewController:resultRiskAnal animated:YES completion:nil];
                } else {
                    if (!analisisResult_vc) {
                        analisisResult_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getAnalisisResultPage]];
                    }
                    [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                    ((AnalisisResultPage*)((UINavigationController*)analisisResult_vc).viewControllers.firstObject).isFromMenu = YES;
                    [self.slideMenuController closeMenuBehindContentViewController:analisisResult_vc animated:YES completion:nil];
                }
                
                
            }
            break;}
        case State_Calendar:{
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Calendar];
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!calendarPage_vc) {
                    calendarPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getCalendarPage]];
                }
                
                [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:calendarPage_vc animated:YES completion:nil];
                
            }
            break;}
        case State_Useful_Know:{
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Useful_Know];
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!usefulKnowPage_vc) {
                    usefulKnowPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getUsefulKnowPage]];
                }
                
                [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:usefulKnowPage_vc animated:YES completion:nil];
                
            }
            break;}
        case State_Publication:{
            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Publication];
            if ([self checkLastController]) {
                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            }else{
                if (!publication_vc) {
                    publication_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getPublicationPage]];
                }
                
                [GlobalSettings sharedInstance].last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
                [self.slideMenuController closeMenuBehindContentViewController:publication_vc animated:YES completion:nil];
                
            }
            break;}
        case State_Reports:{
            //            [GlobalSettings sharedInstance].stateMenu = [self indexOfItem:State_Reports];
            [self openSiteReport];
            //            if ([self checkLastController]) {
            //                [self.slideMenuController closeMenuAnimated:YES completion:nil];
            //            }else{
            //                if (!reportsPage_vc) {
            //                    reportsPage_vc = [[UINavigationController alloc] initWithRootViewController:[[rootMenuController sharedInstance] getReportsPage]];
            //                }
            //
            //                last_stateMenu = [GlobalSettings sharedInstance].stateMenu;
            //                [self.slideMenuController closeMenuBehindContentViewController:reportsPage_vc animated:YES completion:nil];
            //                
            //            }
            
            break;
        }
        default:
            break;
    }
}

-(int)indexOfItem:(int)itemName{
    for (int i = 0; i<menuData.count; i++){
        if ([menuData[i][@"item"] intValue]==itemName) {
            return i;
        }
    }
    return 0;
}

+(int)indexOfItem:(int)itemName inMenu:(NSArray*)menuArray{
    for (int i = 0; i<menuArray.count; i++){
        if ([menuArray[i][@"item"] intValue]==itemName) {
            return i;
        }
    }
    return 0;
}


-(void)openSiteReport{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@?token=%@",kReportURL,User.access_token]]];
}


-(BOOL)checkLastController{
    if ([GlobalSettings sharedInstance].stateMenu==[GlobalSettings sharedInstance].last_stateMenu) {
        return YES;
    }else{
        return FALSE;
    }
}

@end

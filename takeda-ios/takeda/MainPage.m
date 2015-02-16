//
//  MainPage.m
//  takeda
//
//  Created by Alexander Rudenko on 26.12.14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "MainPage.h"


enum {
    physExercises = 1,
    haveDrugs = 2,
    eatToday = 3,
    myRecommendations = 4,
    mySuccess = 5,
    commonReport = 6
};
typedef NSUInteger MenuItem;


@interface MainPage (){
    NSMutableArray *menu_data;
    NSMutableDictionary *results_data;
}

@end

@implementation MainPage
@synthesize resultRiskAnal;

- (void)viewDidLoad
{
    [super viewDidLoad];
    menu_data = [NSMutableArray arrayWithArray: @[@[@{@"title":@"Физическая нагрузка",@"rightTitle":@"мин", @"type":[NSNumber numberWithInt:ctLeftCaptionRightCaption],@"action":[NSNumber numberWithInt:physExercises]},
                                                    @{@"title":@"Принять таблетки",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:haveDrugs]},
                                                    @{@"title":@"Что Вы сегодня ели?",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:eatToday]}],
                                                  @[@{@"title":@"Мои рекомендации",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:myRecommendations]},
                                                    @{@"title":@"Мои успехи",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:mySuccess]},
                                                    @{@"title":@"Сводный отчет", @"subtitle":@"Переход на сайт" ,@"type":[NSNumber numberWithInt:ctCaptionSubtitleRightArrow],@"action":[NSNumber numberWithInt:commonReport]}]]];
    
    [self setupInterface];

}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = NO;
    [self initData];
    [self showInfo];
    [self.tableView reloadData];
}

-(void)setupInterface{
    self.tableView.backgroundColor = RGB(243, 243, 243);
    _percentLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:50];
    _indexCaptionLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    _datePeriod.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    _todayLabel.font = [UIFont fontWithName:@"SegoeWP" size:14];
    
}

-(void)initData{
    NSMutableArray *allResults = [GlobalData resultAnalyses];
    if (allResults.count>0){
        results_data = [[GlobalData resultAnalyses] lastObject];
    }
}

-(void)showInfo{
    self.percentLabel.text = [NSString stringWithFormat:@"%.f%@",[results_data[@"score"] floatValue],@"%"];
    NSDate *fromDate = [Global parseDateTime:results_data[@"createdAt"]];
    if (!fromDate) fromDate = [[NSDate date] dateBySubtractingMonths:1];
    NSDate *nowDate = [NSDate date];
    
    self.datePeriod.text = [NSString stringWithFormat:@"%@ - %@",[fromDate stringWithFormat:@"dd MMMM"], [nowDate stringWithFormat:@"dd MMMM"]];
    self.todayLabel.text = [NSString stringWithFormat:@"Сегодня (%@)",[[NSDate date] stringWithFormat:@"EEEE"]];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return menu_data.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [menu_data[section] count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section == 0){
        return 0;
    } else {
        return 75.f;
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"StandartCombyCell";
    
    StandartCombyCell *cell = (StandartCombyCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"StandartCombyCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[StandartCombyCell class]])
            {
                cell = (StandartCombyCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *menu = menu_data[indexPath.section][indexPath.row];
    cell.cellType = [menu[@"type"] intValue];
    cell.caption.text = menu[@"title"];
    cell.subTitle.text = menu[@"subtitle"];
    cell.rightCaption.text = menu[@"rightTitle"];
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    NSMutableDictionary *item = menu_data[indexPath.section][indexPath.row];
    switch ([item[@"action"] intValue]) {
       
        case physExercises:{
            
            break;
        }
        case haveDrugs:{
            [self goToDrugs];
            break;
        }
        case eatToday:{
            
            break;
        }
        case myRecommendations:{
            [self openResultsAnal];
            break;
        }
        case mySuccess:{
            
            break;
        }
        case commonReport:{
            [self openSiteReport];
            break;
        }


    }
    
    
    [self.tableView reloadData];
}

-(void)goToDrugs{
    if (!_drugs){
        _drugs = [Drugs new];
    }
    [self.navigationController pushViewController:_drugs animated:YES];
}

-(void)openResultsAnal{
    if (!resultRiskAnal) {
        resultRiskAnal = [ResultRiskAnal new];
    }
    resultRiskAnal.needUpdate = YES;
    [self.navigationController pushViewController:resultRiskAnal animated:YES];

}

-(void)openSiteReport{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:kReportURL]];
}



@end

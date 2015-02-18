//
//  CalendarPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "CalendarPage.h"

@interface CalendarPage ()

@end

@implementation CalendarPage
@synthesize days;
@synthesize tasks;
@synthesize emptyRecords;
@synthesize filledRecords;
@synthesize fillEmptySwitch;
@synthesize state;

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
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
}

-(void)initData{
    [GlobalData loadTimelineCompletition:^(BOOL success, id result){
        tasks = [Global recursiveMutable:[result[@"linked"][@"tasks"] groupByKey:@"id"]];
        days = result[@"data"];
        
        for (NSMutableDictionary* day in days){
            NSMutableArray *tasksNewArray = [NSMutableArray new];
            for (NSString* taskId in day[@"links"][@"tasks"]){
                [tasksNewArray addObject:tasks[taskId]];
            }
            [day[@"links"] setObject:tasksNewArray forKey:@"tasks"];
        }
        
        [self.tableView reloadData];
    }];
}

-(void)setupInterface{
    self.fillEmptySwitch.clipsToBounds = YES;
    self.fillEmptySwitch.layer.cornerRadius = 4.0f;
    self.tableView.backgroundColor = RGB(243, 243, 243);
    self.navigationItem.rightBarButtonItems = nil;
    self.navigationItem.rightBarButtonItems = @[[self menuBarBtnWithImageName:@"addWhiteInCircle" selector:@selector(addPillsAction) forTarget:self],[self alarmButton]];

}

-(void)addPillsAction{
    _addPills = [AddPills new];
    [self.navigationController pushViewController:_addPills animated:YES];
}


-(IBAction)selectViewType:(UISegmentedControl*)sender{
    [self selectView:sender.selectedSegmentIndex];
}

-(void)selectView:(int)index{
    switch (index) {
        case 0:{

            break;
        }
        case 1:{

            break;
        }
    }
}



-(void)showInfo{

}

-(NSArray*)records{
//    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF.links.tasks.isCompleted == %i",1];
//    return [days filteredArrayUsingPredicate:predicate];
//
    return days;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return self.records.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.records[section][@"links"][@"tasks"] count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 46;
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    //2015-01-24"
    NSDate *ddt = [Global parseDate:self.records[section][@"date"]];
    UILabel *sectionTitle = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, tableView.width, 46)];
    sectionTitle.font = [UIFont fontWithName:@"SegoeWP" size:14];
    sectionTitle.textAlignment = NSTextAlignmentCenter;
    sectionTitle.textColor = RGB(95, 95, 95);
    sectionTitle.backgroundColor = RGB(243, 243, 243);
    sectionTitle.text = [NSString stringWithFormat:@"%@ (%@)",[ddt isToday]?@"Сегодня":[ddt stringWithFormat:@"dd.MM.yyyy"],[ddt stringWithFormat:@"EEEE"]];
    [self drawBordersInView:sectionTitle];
    return sectionTitle;
    
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
    
  //  NSString *itemId = self.records[indexPath.section][@"links"][@"tasks"][indexPath.row];
    NSMutableDictionary *item = self.records[indexPath.section][@"links"][@"tasks"][indexPath.row];
    
    cell.cellType = [self cellTypeForTask:item[@"type"]];
    
    SWITCH(item[@"type"]){
        CASE(@"exercise"){
          cell.caption.text = @"Физическая нагрузка";
          cell.rightCaption.text = @"мин";
            break;
        }
        CASE(@"diet"){
            cell.caption.text = @"Вы соблюдаете диету?";
            break;
        }
        CASE(@"pill"){
            cell.caption.text = @"Принять таблетки";
            break;
        }
        DEFAULT{
            break;
        }
    }
    
    cell.backgroundColor = [UIColor whiteColor];
    if ([item[@"isCompleted"] boolValue]) cell.backgroundColor = [UIColor greenColor];
    
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    NSMutableDictionary *item = menu_data[indexPath.section][indexPath.row];
//    switch ([item[@"action"] intValue]) {
//            
//        case physExercises:{
//            
//            break;
//        }
//        case haveDrugs:{
//            [self goToDrugs];
//            break;
//        }
//        case eatToday:{
//            
//            break;
//        }
//        case myRecommendations:{
//            [self openResultsAnal];
//            break;
//        }
//        case mySuccess:{
//            
//            break;
//        }
//        case commonReport:{
//            [self openSiteReport];
//            break;
//        }
//            
//            
//    }
    
    
    [self.tableView reloadData];
}

-(CombyCellType)cellTypeForTask:(NSString*)taskType{
    NSDictionary *r = @{@"exercise":[NSNumber numberWithInt:ctLeftCaptionRightCaptionArrow],
                        @"diet":[NSNumber numberWithInt:ctCaptionChecked],
                        @"pill":[NSNumber numberWithInt:ctLeftCaptionRightBadgeArrow]};
    return [r[taskType] intValue];
}


/*
 ctSimpleRightCaption = 1,
 ctLeftCaptionRightCaption = 2,
 ctLeftCaptionRightArrow = 3,
 ctCaptionSubtitleRightArrow = 4,
 ctCaptionSubtitleChecked = 5

 */

@end

//
//  CalendarPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "CalendarPage.h"

@interface CalendarPage (){
    BOOL reloading;
    BOOL pullTorefreshVisible;
}

@end

@implementation CalendarPage
@synthesize days;
@synthesize tasks;
@synthesize emptyRecords;
@synthesize filledRecords;
@synthesize fillEmptySwitch;
@synthesize state;
@synthesize records;


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
    state = dNew;
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
}

-(void)initData{
    if (!pullTorefreshVisible){
        [self showActivityIndicatorWithString:@""];
    }
    [GlobalData loadTimelineCompletition:^(BOOL success, id result){
        [self removeActivityIdicator];
        if (success){
            NSLog(@"Получили данные");
            tasks = [Global recursiveMutable:[result[@"linked"][@"tasks"] groupByKey:@"id"]];
            days = result[@"data"];
            
            [GlobalData casheTimelineTasks:tasks];
            [GlobalData casheTimeline:days];

            [self startData];
        } else {
            NSLog(@"Ошибка получения данных");
            tasks = [GlobalData cashedTimelineTasks];
            days = [GlobalData cashedTimeline];
            
            [self startData];
        }
        
    }];
}


-(void)startData{
    if (![self makePillsSuccess]){
        [GlobalData loadPillsCompletition:^(BOOL completition, id result){
            if ([self makePillsSuccess]){
                [self filtrRecords];
                [self.tableView reloadData];
            }
        }];
    } else {
        [self filtrRecords];
        [self.tableView reloadData];
    }
    [self doneLoadingTableViewData];

}


-(void)updateTask:(NSMutableDictionary*)task{
    NSMutableDictionary *d = [GlobalData cashedTimelineTasks];
    [d setObject:task forKey:task[@"id"]];
    [GlobalData casheTimelineTasks:d];
}

-(void)filtrRecords{
    self.tableNewView.hidden = YES;
    self.tableFillView.hidden = YES;
    self.tableView.hidden = NO;
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    [_refreshHeaderView removeFromSuperview];

    
    records = [NSMutableArray new];
    NSMutableArray *ddc = [Global recursiveMutable:[days mutableCopy]];
    
    switch (state) {
        case dNew:{
            for (NSMutableDictionary* day in ddc){
                NSMutableDictionary *dayCopy = [NSMutableDictionary dictionaryWithDictionary:day];
                NSMutableArray *tasksNewArray = [NSMutableArray new];
                for (NSString* taskId in dayCopy[@"links"][@"tasks"]){
                    if (!tasks[taskId][@"isCompleted"]){
                        [tasksNewArray addObject:tasks[taskId]];
                    }
                }
                if (tasksNewArray.count>0){
                    [dayCopy[@"links"] setObject:tasksNewArray forKey:@"tasks"];
                    [records addObject:dayCopy];
                }
            }
            
            break;
        }
        case dFilled:{
            for (NSMutableDictionary* day in ddc){
                NSMutableDictionary *dayCopy = [NSMutableDictionary dictionaryWithDictionary:day];
                NSMutableArray *tasksNewArray = [NSMutableArray new];
                for (NSString* taskId in dayCopy[@"links"][@"tasks"]){
                    if (tasks[taskId][@"isCompleted"]){
                        [tasksNewArray addObject:tasks[taskId]];
                    }
                }
                if (tasksNewArray.count>0){
                    [dayCopy[@"links"] setObject:tasksNewArray forKey:@"tasks"];
                    [records addObject:dayCopy];
                }
            }
            
            break;
        }
    }
    
    records = [Global recursiveMutable:[records sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
                return [obj2[@"date"] compare:obj1[@"date"]];
               }]];
    [self.tableView addSubview:_refreshHeaderView];
    [_refreshHeaderView refreshLastUpdatedDate];
    
}

-(BOOL)makePillsSuccess{
    NSMutableDictionary *pillsInfo = [GlobalData pillsDict];
    BOOL success = YES;
    for (NSMutableDictionary*task in tasks.allValues){
        if ([task[@"type"] isEqualToString:@"pill"]){
            int pillId = [task[@"links"][@"pill"] intValue];
            if (pillsInfo[[NSString stringWithFormat:@"%i",pillId]]){
                [task setObject:pillsInfo[[NSString stringWithFormat:@"%i",pillId]] forKey:@"pillInfo"];
            } else {
                success = NO;
            }
            
        }
    }
    return success;
}


-(void)setupInterface{
    if (_refreshHeaderView == nil) {
        EGORefreshTableHeaderView *view = [[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.height, self.tableView.width, self.tableView.height)];
        view.delegate = self;
        _refreshHeaderView = view;
    }
    
    //  update the last update date
    [_refreshHeaderView refreshLastUpdatedDate];

    
    self.tableNewView.tableHeaderView = self.tableNewView.separ;
    self.tableFillView.tableHeaderView = self.tableNewView.separ;

    self.tableNewView.sectionHeaderHeight = 0.0;
    self.tableNewView.sectionFooterHeight = 0.0;
    self.tableFillView.sectionHeaderHeight = 0.0;
    self.tableFillView.sectionFooterHeight = 0.0;
    
    
    self.fillEmptySwitch.clipsToBounds = YES;
    self.fillEmptySwitch.layer.cornerRadius = 4.0f;
    self.tableView.backgroundColor = RGB(243, 243, 243);
    self.navigationItem.rightBarButtonItems = nil;
    
    self.navigationItem.rightBarButtonItems = @[[self addPills],[self personalButton]];

}

-(UIBarButtonItem*)addPills{
    UIImage *alarmImage = [UIImage imageNamed:@"addWhiteInCircle"];
    UIButton *cButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cButton setImage:alarmImage forState:UIControlStateNormal];
    cButton.frame = CGRectMake(0.0,0.0,alarmImage.size.width+10,alarmImage.size.height);
    cButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    [cButton addTarget:self action:@selector(addPillsAction) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:cButton];
}

-(void)addPillsAction{
    _addPills = [AddPills new];
    [self.navigationController pushViewController:_addPills animated:YES];
}


-(IBAction)selectViewType:(UISegmentedControl*)sender{
    [self selectView:sender.selectedSegmentIndex];
}

-(void)selectView:(int)index{
    [self.tableView setContentOffset:self.tableView.contentOffset animated:NO];

    switch (index) {
        case 0:{
            state = dNew;
            break;
        }
        case 1:{
            state = dFilled;
            break;
        }
    }

    [self filtrRecords];
    [self.tableView reloadData];
}

-(UITableView*)tableView{
    switch (state) {
        case dNew:{
            return self.tableNewView;
            break;
        }
        case dFilled:{
            return self.tableFillView;
            break;
        }
    }
    return nil;
}

-(void)showInfo{

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
    UIView *rV = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.width, 46)];
    //2015-01-24"
    NSDate *ddt = [Global parseDate:self.records[section][@"date"]];
    UILabel *sectionTitle = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, tableView.width, 46)];
    sectionTitle.clipsToBounds = YES;
    sectionTitle.font = [UIFont fontWithName:@"SegoeWP" size:14];
    sectionTitle.textAlignment = NSTextAlignmentCenter;
    sectionTitle.textColor = RGB(95, 95, 95);
    sectionTitle.backgroundColor = RGB(243, 243, 243);
    sectionTitle.text = [NSString stringWithFormat:@"%@ (%@)",[ddt isToday]?@"Сегодня":[ddt stringWithFormat:@"dd.MM.yyyy"],[ddt stringWithFormat:@"EEEE"]];
   // [self drawBordersInView:sectionTitle];
    [rV addSubview:sectionTitle];
    return rV;
    
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
    
    if (item[@"isCompleted"]){
        cell.cellType = [self cellTypeForTask:item[@"type"]];
    } else {
        cell.cellType = ctLeftCaptionRightArrow;
    }
    
    SWITCH(item[@"type"]){
        CASE(@"exercise"){
          cell.caption.text = @"Физическая нагрузка";
            NSString *mins = item[@"isCompleted"]?item[@"exerciseMins"]:@"";
                cell.rightCaption.text = [NSString stringWithFormat:@"%@ мин",mins];
            break;
        }
        CASE(@"diet"){
            cell.caption.text = @"Вы соблюдаете диету?";
            cell.checkBtn.userInteractionEnabled = NO;
            if (item[@"isCompleted"]){
                cell.checkBtn.selected = [item[@"isCompleted"] boolValue];
            }
            break;
        }
        CASE(@"pill"){
        
            NSMutableString *capt = [NSMutableString new];
            [capt appendString:@"Принять"];
            
            if (item[@"pillInfo"]){
                [capt appendFormat:@" %@",item[@"pillInfo"][@"name"]];
            }

            cell.caption.text = capt;
            cell.checkBtn.userInteractionEnabled = NO;
            if (item[@"isCompleted"]){
                cell.checkBtn.selected = [item[@"isCompleted"] boolValue];
            }

            break;
        }
        DEFAULT{
            break;
        }
    }
    
    cell.backgroundColor = [UIColor whiteColor];
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [self selectItem:indexPath];
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
    
    
 //   [self.tableView reloadData];
}

- (void)reloadTableViewDataSource{
    //  should be calling your tableviews data source model to reload
    //  put here just for demo
    reloading = YES;
    [self initData];
}

- (void)doneLoadingTableViewData{
    //  model should call this when its done loading
    reloading = NO;
    [_refreshHeaderView performSelector:@selector(egoRefreshScrollViewDataSourceDidFinishedLoading:) withObject:self.tableView afterDelay:0.3f];

//    [_refreshHeaderView egoRefreshScrollViewDataSourceDidFinishedLoading:self.tableView];
    
}

#pragma mark EGORefreshTableHeaderDelegate Methods

- (void)egoRefreshTableHeaderDidTriggerRefresh:(EGORefreshTableHeaderView*)view{
    [self reloadTableViewDataSource];
 //   [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:1.0];
    
}

- (BOOL)egoRefreshTableHeaderDataSourceIsLoading:(EGORefreshTableHeaderView*)view{
    pullTorefreshVisible = YES;
    return reloading; // should return if data source model is reloading
    
}

- (NSDate*)egoRefreshTableHeaderDataSourceLastUpdated:(EGORefreshTableHeaderView*)view{
    pullTorefreshVisible = NO;
    
    return [NSDate date]; // should return date data source was last changed
    
}


#pragma mark -
#pragma mark UIScrollViewDelegate Methods


- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    [_refreshHeaderView egoRefreshScrollViewDidEndDragging:scrollView];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    [_refreshHeaderView egoRefreshScrollViewDidScroll:scrollView];
}

#pragma mark -


-(void)selectItem:(NSIndexPath*)indexPath{
    if (state == dFilled) return;
    if (appDelegate.hostConnection == NotReachable) {
        [self showMessage:@"Отсутствует соединение с Интернетом" title:@"Ошибка"];
        return;
    }
    
    NSMutableDictionary *item = self.records[indexPath.section][@"links"][@"tasks"][indexPath.row];
    
    SWITCH(item[@"type"]){
        CASE(@"exercise"){
            [self setupPhysicalActivity:item];
            break;
        }
        CASE(@"diet"){
            [self setupDietСompliance:item];
            break;
        }
        CASE(@"pill"){
            [self setupPill:item];
            break;
        }
        DEFAULT{
            break;
        }
    }
    
    if (item[@"isCompleted"]){
        
    }
    
}


-(void)setupPhysicalActivity:(NSMutableDictionary*)task{
    NSMutableDictionary *params = [NSMutableDictionary new];
    NSMutableArray *expArray = [NSMutableArray new];
        [expArray fillIntegerFrom:80 to:200 step:1];
        DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.view completition:^(BOOL apply, int index){
            if (apply){
                [params setObject:[NSNumber numberWithInt:[expArray[index] intValue]] forKey:@"exerciseMins"];
                [ServData updateTask:task[@"id"] params:params completion:^(BOOL success, NSError* error, id result){
                    if (success){
                        BOOL isCompleted = [result[@"data"][@"isCompleted"] boolValue];
                        int minutes = [result[@"data"][@"exerciseMins"] intValue];
                        [tasks[task[@"id"]] setObject:[NSNumber numberWithBool:isCompleted] forKey:@"isCompleted"];
                        [tasks[task[@"id"]] setObject:[NSNumber numberWithInt:minutes] forKey:@"exerciseMins"];
                        
                        [self updateTask:result[@"data"]];
                        [self filtrRecords];
                        [self.tableView reloadData];
                    }
                }];
            }
        }];
        exPicker.applyBtn.title = @"Отправить";
        exPicker.closeBtn.title = @"Отменить";
    
        [exPicker show];
}

-(void)setupDietСompliance:(NSMutableDictionary*)task{
    NSMutableDictionary *params = [NSMutableDictionary new];
    NSArray *expArray = @[@"Соблюдал диету",@"Не соблюдал диету"];

    DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.view completition:^(BOOL apply, int index){
        if (apply){
            
            [params setObject:[NSNumber numberWithBool:(index == 0)] forKey:@"isCompleted"];
            [ServData updateTask:task[@"id"] params:params completion:^(BOOL success, NSError* error, id result){
                if (success){
                    BOOL isCompleted = [result[@"data"][@"isCompleted"] boolValue];
                    [tasks[task[@"id"]] setObject:[NSNumber numberWithBool:isCompleted] forKey:@"isCompleted"];

                    [self updateTask:result[@"data"]];

                    [self filtrRecords];
                    [self.tableView reloadData];
                }
            }];
        }
    }];
    exPicker.applyBtn.title = @"Отправить";
    exPicker.closeBtn.title = @"Отменить";
    
    [exPicker show];
}

-(void)setupPill:(NSMutableDictionary*)task{
    NSMutableDictionary *params = [NSMutableDictionary new];
    NSArray *expArray = @[@"Принял",@"Не принял"];
    
    DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.view completition:^(BOOL apply, int index){
        if (apply){
            
            [params setObject:[NSNumber numberWithBool:(index == 0)] forKey:@"isCompleted"];
            [ServData updateTask:task[@"id"] params:params completion:^(BOOL success, NSError* error, id result){
                if (success){
                    BOOL isCompleted = [result[@"data"][@"isCompleted"] boolValue];
                    [tasks[task[@"id"]] setObject:[NSNumber numberWithBool:isCompleted] forKey:@"isCompleted"];
                    [self updateTask:result[@"data"]];

                    [self filtrRecords];
                    [self.tableView reloadData];
                }
            }];
        }
    }];
    exPicker.applyBtn.title = @"Отправить";
    exPicker.closeBtn.title = @"Отменить";
    
    [exPicker show];
}








-(CombyCellType)cellTypeForTask:(NSString*)taskType{
    NSDictionary *r = @{@"exercise":[NSNumber numberWithInt:ctLeftCaptionRightCaption],
                        @"diet":[NSNumber numberWithInt:ctCaptionChecked],
                        @"pill":[NSNumber numberWithInt:ctCaptionChecked]};
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

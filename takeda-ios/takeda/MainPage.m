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
    NSMutableArray *bottom_menu_data;
    NSMutableDictionary *daybook_menu_data;
    NSMutableDictionary *results_data;
    BOOL reloading;
    BOOL pullTorefreshVisible;
}

@end

@implementation MainPage
@synthesize resultRiskAnal;
@synthesize tasks;
@synthesize days;
@synthesize records;
@synthesize calendarPage;
@synthesize analisisResultPage;

- (void)viewDidLoad
{
    [super viewDidLoad];
    bottom_menu_data = [NSMutableArray arrayWithArray:     @[@{@"title":@"Мои рекомендации",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:myRecommendations]},
                                                             @{@"title":@"Мои успехи",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:mySuccess]},
                                                             @{@"title":@"Сводный отчет", @"subtitle":@"Переход на сайт" ,@"type":[NSNumber numberWithInt:ctCaptionSubtitleRightArrow],@"action":[NSNumber numberWithInt:commonReport]}]];
    
    [self setupInterface];
    [self refreshData];

}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = NO;
    [self initLocalData];
}

-(void)setupInterface{
    self.tableView.backgroundColor = RGB(243, 243, 243);
    self.medSearchBtnRed.layer.borderColor = RGB(53, 65, 71).CGColor;
    self.medSearchBtnRed.layer.borderWidth = 1.0f;
    self.medSearchBtnRed.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
    self.medSearchBtnRed.clipsToBounds = YES;
    self.medSearchBtnRed.layer.cornerRadius = 5.0f;
    
    self.mainRecomendationRed.textColor = [UIColor whiteColor];
    self.mainRecomendationRed.font = [UIFont fontWithName:@"SegoeUI-Light" size:15];
    
    self.scoreNoteTextRed.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
    self.scoreNoteTextRed.textColor = [UIColor whiteColor];
    
    _percentLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:50];
    _indexCaptionLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    _datePeriod.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    _todayLabel.font = [UIFont fontWithName:@"SegoeWP" size:14];
    
    if (_refreshHeaderView == nil) {
        EGORefreshTableHeaderView *view = [[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.height, self.tableView.width, self.tableView.height)];
        view.delegate = self;
        _refreshHeaderView = view;
    }
    [self.tableView addSubview:_refreshHeaderView];
    //  update the last update date
    [_refreshHeaderView refreshLastUpdatedDate];

}

-(void)initLocalData{
    
    NSMutableArray *allResults = [GlobalData resultAnalyses];
    if (allResults.count>0){
        results_data = [[GlobalData resultAnalyses] lastObject];
      [self showData];
    } else {
        [self showNormInfo];
    }

    [self showData];

    if (User.userBlocked){
        
    } else {
        tasks = [GlobalData cashedTimelineTasks];
        days = [GlobalData cashedTimeline];
        [self startData];
    }
}

-(void)refreshData{
        [_indLoading startAnimating];
        [[Synchronizer sharedInstance] startSynchronizeTasks:@[jLoadRiskAnalResults,jLoadISP,jLoadIncidents, jLoadTimeLine] completition:^(BOOL success, id result) {
            [self initLocalData];
        }];
}

-(void)showNormInfo{
    NSString *isp = [GlobalData ISP];
    if (isp!=nil&&isp.length>0){
        self.percentLabel.text = [NSString stringWithFormat:@"%.f%@",[isp floatValue],@"%"];
    } else {
        self.percentLabel.text = @"0%";
    }
   // self.percentLabel.text = [NSString stringWithFormat:@"%i%@",GlobalData.missedEventsCount ,@"%"];

    
    NSDate *fromDate = [Global parseDateTime:results_data[@"createdAt"]];
    if (!fromDate) fromDate = [[NSDate date] dateBySubtractingMonths:1];
    NSDate *nowDate = [NSDate date];
    
    self.datePeriod.text = [NSString stringWithFormat:@"%@ - %@",[fromDate stringWithFormat:@"dd MMMM"], [nowDate stringWithFormat:@"dd MMMM"]];
    self.todayLabel.text = [NSString stringWithFormat:@"Сегодня (%@)",[[NSDate date] stringWithFormat:@"EEEE"]];
}

-(void)showData{

    if (User.userBlocked){
        [self showMainInfoRed];
        self.tableView.hidden = YES;
        self.normHeader.hidden = YES;
        self.scrollViewRed.hidden = NO;
    } else {
        [self showNormInfo];
        [self.tableView reloadData];
        self.tableView.hidden = NO;
        self.scrollViewRed.hidden = YES;
    }
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
    BOOL needFullReload = NO;
    NSMutableDictionary *daysDict = [Global recursiveMutable:[days groupByKey:@"date"]];
    if ([daysDict objectForKey:[[NSDate date] stringWithFormat:@"yyyy-MM-dd"]]!=nil){
        daybook_menu_data = [daysDict objectForKey:[[NSDate date] stringWithFormat:@"yyyy-MM-dd"]];
    }
    
    
    NSMutableArray *tasksNewArray = [NSMutableArray new];
    for (NSString* taskId in daybook_menu_data[@"links"][@"tasks"]){
        if (tasks[taskId]){
            [tasksNewArray addObject:tasks[taskId]];
        } else {
            needFullReload = YES;
        }
    }
    
    if (needFullReload){
        [self refreshData];
    }
    
    [daybook_menu_data[@"links"] setObject:tasksNewArray forKey:@"tasks"];

    
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



#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    switch (section) {
        case 0: return [daybook_menu_data[@"links"][@"tasks"] count];
        case 1: return [bottom_menu_data count];
    }
    return 0;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section == 0){
            return 50;
    } else {
        return 75.f;
    }
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{

    if (section == 1){
        UIView *rM = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.width, 46)];
        rM.backgroundColor = [UIColor clearColor];
        _indLoading = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
        _indLoading.center = rM.middleCenter;
        _indLoading.hidesWhenStopped = YES;
        
        if ([daybook_menu_data[@"links"][@"tasks"] count]==0){
//            UILabel *noRecords = [[UILabel alloc] initWithFrame:CGRectMake(0, sectionTitle.bottom, sectionTitle.width, 50)];
            UILabel *noRecords = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, rM.width, 14)];

            noRecords.textAlignment = NSTextAlignmentCenter;
            noRecords.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
            noRecords.textColor = RGB(54, 65, 71);
            
            noRecords.text = @"У Вас нет задач на сегодня";
            [rM addSubview:noRecords];
            
        }

        return rM;
    }
    
    
    UIView *rV = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.width, 46)];
    rV.clipsToBounds = NO;
    NSDate *ddt;
    if (daybook_menu_data){
        ddt = [Global parseDate:daybook_menu_data[@"date"]];
    } else {
        ddt = [NSDate date];
    }
    UILabel *sectionTitle = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, tableView.width, 50)];
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
    if (indexPath.section == 0){
        return [self tableView:tableView cellForRowAtIndexPathDaybook:indexPath];
    } else {
        return [self tableView:tableView cellForRowAtIndexPathBottom:indexPath];
    }

    return nil;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathBottom:(NSIndexPath *)indexPath
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
    
    NSMutableDictionary *menu = bottom_menu_data[indexPath.row];
    cell.cellType = [menu[@"type"] intValue];
    cell.caption.text = menu[@"title"];
    cell.subTitle.text = menu[@"subtitle"];
    cell.rightCaption.text = menu[@"rightTitle"];
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathDaybook:(NSIndexPath *)indexPath
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

    NSMutableDictionary *item = daybook_menu_data[@"links"][@"tasks"][indexPath.row];
    
    if (item[@"isCompleted"]){
        cell.cellType = [CalendarPage cellTypeForTask:item[@"type"]];
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
        CASE(@"arterialPressure"){
            cell.caption.text = @"Артериальное давление";
            NSString *mms = item[@"isCompleted"]?item[@"arterialPressure"]:@"";
            cell.rightCaption.text = [NSString stringWithFormat:@"%@ мм",mms];
            break;
        }
        CASE(@"weight"){
            cell.caption.text = @"Вес";
            NSString *kg = item[@"isCompleted"]?item[@"weight"]:@"";
            cell.rightCaption.text = [NSString stringWithFormat:@"%@ кг",kg];
            break;
        }

        DEFAULT{
            break;
        }
    }
    if (item[@"isCompleted"]){

    }
    
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}




#pragma mark - Table view delegate

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.section == 0){
        [self selectDaybookItem:indexPath];
    } else {
        [self selectBottomItem:indexPath];
    }
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [self.tableView reloadData];
}

-(void)selectBottomItem:(NSIndexPath*)indexPath{
    NSMutableDictionary *item = bottom_menu_data[indexPath.row];
    switch ([item[@"action"] intValue]) {
        case myRecommendations:{
            [self openResultsAnal];
            break;
        }
        case mySuccess:{
            [self openDaybook];
            break;
        }
        case commonReport:{
            [self openSiteReport];
            break;
        }
    }
}

-(void)selectDaybookItem:(NSIndexPath*)indexPath{
    NSMutableDictionary *item = daybook_menu_data[@"links"][@"tasks"][indexPath.row];
    DIndex state = [Global isNotNull:item[@"isCompleted"]]?dFilled:dNew;

    if (state == dFilled) return;
    if (appDelegate.hostConnection == NotReachable) {
        [self showMessage:@"Отсутствует соединение с Интернетом" title:@"Ошибка"];
        return;
    }
    
    
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
        CASE(@"arterialPressure"){
            [self setupArterial:item];
            break;
        }
        CASE(@"weight"){
            [self setupWeight:item];
            break;
        }

        DEFAULT{
            break;
        }
    }
}



#pragma mark RED -
-(void)showMainInfoRed{
    
    if ([results_data[@"recommendations"][@"fullScreenAlert"] isKindOfClass:[NSDictionary class]]){
        self.stateImageRed.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@_small",results_data[@"recommendations"][@"fullScreenAlert"][@"state"]]];
    
        if ([results_data[@"recommendations"][@"fullScreenAlert"][@"text"] isKindOfClass:[NSString class]]&&[results_data[@"recommendations"][@"fullScreenAlert"][@"text"] length]>0){
            self.scoreNoteTextRed.text = results_data[@"recommendations"][@"fullScreenAlert"][@"text"];
        } else {
            self.scoreNoteTextRed.text = @"";
        }
    
    } else {
        self.scoreNoteTextRed.text = @"";
    }
    
    if ([results_data[@"recommendations"][@"mainRecommendation"][@"text"] isKindOfClass:[NSString class]]&&[results_data[@"recommendations"][@"mainRecommendation"][@"text"] length]>0){
        self.mainRecomendationRed.text = results_data[@"recommendations"][@"mainRecommendation"][@"text"];
    } else {
        self.mainRecomendationRed.text = @"";
    }
    
    //    self.mainRecomendationRed.text = @"";
    
    
    self.medSearchBtnRed.hidden = ![results_data[@"recommendations"][@"placesLinkShouldBeVisible"] boolValue];
    
    self.mainRecomendationRed.height = [Global heightLabel:self.mainRecomendationRed];
    self.scoreNoteTextRed.height = [Global heightLabel:self.scoreNoteTextRed];
    
    self.separ1.y = self.mainRecomendationRed.bottom+15;
    self.scoreNoteTextRed.y = self.mainRecomendationRed.bottom+30;
    self.medSearchBtnRed.y = self.scoreNoteTextRed.bottom+50;
    self.stateImageRed.y = self.scoreNoteTextRed.y;
    self.separ2.y = self.scoreNoteTextRed.bottom+15;
    [self.headerViewRed setupAutosizeBySubviewsWithBottomDistance:60];
    [self.scrollViewRed setup_autosizeWithBottomDistance:0];
}

#pragma mark -



-(void)goToDrugs{
    if (!_drugs){
        _drugs = [Drugs new];
    }
    [self.navigationController pushViewController:_drugs animated:YES];
}

-(void)openResultsAnal{

    if (![User checkForRole:tDoctor]){
            if (!resultRiskAnal) {
                resultRiskAnal = [ResultRiskAnal new];
            }
            [self.navigationController pushViewController:resultRiskAnal animated:YES];
        } else {
            if (!analisisResultPage) {
                analisisResultPage = [AnalisisResultPage new];
            }
            [self.navigationController pushViewController:analisisResultPage animated:YES];
        }
}

-(void)openDaybook{
    if (!calendarPage) {
        calendarPage = [CalendarPage new];
    }

    [self.navigationController pushViewController:calendarPage animated:YES];
    
}

-(void)openSiteReport{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@?token=%@",kReportURL,User.access_token]]];
}

//////////////
#pragma mark -


- (void)reloadTableViewDataSource{
    //  should be calling your tableviews data source model to reload
    //  put here just for demo
    reloading = YES;
    [self refreshData];
}

- (void)doneLoadingTableViewData{
    //  model should call this when its done loading
    reloading = NO;
    [_refreshHeaderView performSelector:@selector(egoRefreshScrollViewDataSourceDidFinishedLoading:) withObject:self.tableView afterDelay:0.3f];
}

#pragma mark EGORefreshTableHeaderDelegate Methods

- (void)egoRefreshTableHeaderDidTriggerRefresh:(EGORefreshTableHeaderView*)view{
    [self reloadTableViewDataSource];
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

-(void)setupArterial:(NSMutableDictionary*)task{
    NSMutableDictionary *params = [NSMutableDictionary new];
    NSMutableArray *expArray = [NSMutableArray new];
    [expArray fillIntegerFrom:80 to:200 step:1];
    DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.view completition:^(BOOL apply, int index){
        if (apply){
            [params setObject:[NSNumber numberWithInt:[expArray[index] intValue]] forKey:@"arterialPressure"];
            [ServData updateTask:task[@"id"] params:params completion:^(BOOL success, NSError* error, id result){
                if (success){
                    BOOL isCompleted = [result[@"data"][@"isCompleted"] boolValue];
                    int minutes = [result[@"data"][@"arterialPressure"] intValue];
                    [tasks[task[@"id"]] setObject:[NSNumber numberWithBool:isCompleted] forKey:@"isCompleted"];
                    [tasks[task[@"id"]] setObject:[NSNumber numberWithInt:minutes] forKey:@"arterialPressure"];
                    
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
-(void)setupWeight:(NSMutableDictionary*)task{
    NSMutableDictionary *params = [NSMutableDictionary new];
    NSMutableArray *expArray = [NSMutableArray new];
    [expArray fillIntegerFrom:30 to:700 step:1];
    DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.view completition:^(BOOL apply, int index){
        if (apply){
            [params setObject:[NSNumber numberWithInt:[expArray[index] intValue]] forKey:@"weight"];
            [ServData updateTask:task[@"id"] params:params completion:^(BOOL success, NSError* error, id result){
                if (success){
                    BOOL isCompleted = [result[@"data"][@"isCompleted"] boolValue];
                    int minutes = [result[@"data"][@"weight"] intValue];
                    [tasks[task[@"id"]] setObject:[NSNumber numberWithBool:isCompleted] forKey:@"isCompleted"];
                    [tasks[task[@"id"]] setObject:[NSNumber numberWithInt:minutes] forKey:@"weight"];
                    
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


@end

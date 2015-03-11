//
//  ResultRiskAnal.m
//  takeda
//
//  Created by Alexander Rudenko on 25.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "ResultRiskAnal.h"
#import "riskCell.h"

@interface ResultRiskAnal (){
    
    NSMutableArray *results_banners_array;
    NSMutableArray *allResults;
}

@end

@implementation ResultRiskAnal
@synthesize searchInstitutionPage;
@synthesize dietTest;
@synthesize dietTestResults;
@synthesize results_data;

-(NSDictionary*)hardCodedItems{
    return @{@"additionalDietTest":@{@"state":@"question", @"action":@"openAdditionalFoodTest",@"title":@"Дополнительная корректировка диеты", @"note":@"Пройти опрос"}};
}

-(NSArray*)results_banners_model{
    return    @[@{@"category":@"isSmoker",@"title":@"Курение", @"keys":@[@"isSmoker"]},
                @{@"category":@"physicalActivity",@"title":@"Физическая активность", @"keys":@[@"physicalActivityMinutes",@"bmi"]},
                @{@"category":@"commonRisks",@"title":@"Основные риски", @"keys":@[@"cholesterolLevel",@"arterialPressure",@"hadSugarProblems",@"isArterialPressureDrugsConsumer", @"isCholesterolDrugsConsumer"]},
                @{@"category":@"diet",@"title":@"Диета", @"keys":@[@"isAddingExtraSalt",@"additionalDietTest"]}
                ];
}

-(UIFont*)fontDescription{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}

-(UIFont*)cellTitleFont{
    return [UIFont fontWithName:@"SegoeUI-Light" size:14.0];
}

-(UIFont*)cellTextFont{
    return [UIFont fontWithName:@"SegoeUI-Light" size:12.0];
}

-(UIColor*)normCellColor{
    return RGB(101, 147, 194);
}

-(UIColor*)attentionCellColor{
    return RGB(108, 174, 241);
}


- (void)viewDidLoad {
    [super viewDidLoad];
    self.mainElement = self.containerAll;
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (self.isAppearFromBack){
        [self removeController:dietTestResults];
        [self removeController:dietTest];
    }
    [self initData];
    [self showData];
}

-(void)setupInterface{
    
    
    self.medSearchBtnRed.layer.borderColor = RGB(53, 65, 71).CGColor;
    self.medSearchBtnRed.layer.borderWidth = 1.0f;
    self.medSearchBtnRed.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
    self.medSearchBtnRed.clipsToBounds = YES;
    self.medSearchBtnRed.layer.cornerRadius = 5.0f;
    
    self.mainRecomendationRed.textColor = [UIColor whiteColor];
    self.mainRecomendationRed.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
   
    self.scoreNoteTextRed.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
    self.scoreNoteTextRed.textColor = [UIColor whiteColor];
    
    self.scoreValueRed.font = [UIFont fontWithName:@"SegoeUI-Light" size:30];
//////
    
    
    self.medSearchBtn.layer.borderColor = RGB(152, 198, 245).CGColor;
    self.medSearchBtn.layer.borderWidth = 0.5f;
    self.medSearchBtn.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];

    self.profilacticCalendarBtn.layer.borderColor = [UIColor whiteColor].CGColor;
    self.profilacticCalendarBtn.layer.borderWidth = 1.0f;
    self.profilacticCalendarBtn.layer.cornerRadius = 4.0f;
    self.profilacticCalendarBtn.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:17];
    
    self.mainRecomendation.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
    self.scoreNoteText.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
    
    self.mainRecomendation.textColor = [UIColor whiteColor];
    self.scoreNoteText.textColor = [UIColor whiteColor];

    self.scoreValue.font = [UIFont fontWithName:@"SegoeUI-Light" size:30];
    self.scoreValue.textColor = RGB(108, 174, 241);
}

-(void)initData{
    if (!results_data){
        allResults = [GlobalData resultAnalyses];
        if (allResults.count>0){
            results_data = [[GlobalData resultAnalyses] lastObject];
        }
    }
    
    results_banners_array = [NSMutableArray new];
    NSDictionary *res_data_banners = results_data[@"recommendations"][@"banners"];
    for (int i = 0; i<[self results_banners_model].count; i++){
        NSMutableDictionary *item = [Global recursiveMutable:[[self results_banners_model][i] mutableCopy]];
        NSMutableArray *itemKeys = [NSMutableArray new];
        for (NSString *key in item[@"keys"]){
            if ([res_data_banners hasKey:key]&&[res_data_banners[key] isKindOfClass:[NSDictionary class]]){
                [res_data_banners[key] setObject:key forKey:@"type"];
                [res_data_banners[key] setObject:results_data[@"id"] forKey:@"testId"];

                [itemKeys addObject:res_data_banners[key]];
            } else {
                if ([[self hardCodedItems] hasKey:key]&&[[self hardCodedItems][key] isKindOfClass:[NSDictionary class]]){
                    [itemKeys addObject:[self hardCodedItems][key]];
                }
            }
        }
        [item setObject:itemKeys forKey:@"keys"];
        [results_banners_array addObject:item];
    }
}

-(void)showData{
    if (self.disableBack){
        self.navigationItem.leftBarButtonItem = [self menuButton];
    } else {
        self.navigationItem.leftBarButtonItem = [self backBtn];
    }
    self.infoBtn.enabled = !User.userBlocked;

    if (results_data[@"recommendations"][@"fullScreenAlert"]!=nil&&[results_data[@"recommendations"][@"fullScreenAlert"]isKindOfClass:[NSDictionary class]]){
        [self showScoreLineRed];
        [self showMainInfoRed];
        self.tableView.hidden = YES;
        self.scrollViewRed.hidden = NO;
        self.containerAll.backgroundColor = self.scrollViewRed.backgroundColor;
    } else {
        [self showScoreLine];
        [self showMainInfo];
        [self.tableView reloadData];
        self.tableView.tableHeaderView = self.headerView;
        self.tableView.hidden = NO;
        self.scrollViewRed.hidden = YES;
        self.containerAll.backgroundColor = self.headerView.backgroundColor;

    }
    
}

#pragma mark - normResults

-(void)showMainInfo{
    
    self.stateImage.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@_small",results_data[@"recommendations"][@"scoreNote"][@"state"]]];

    if ([results_data[@"recommendations"][@"mainRecommendation"][@"text"] isKindOfClass:[NSString class]]&&[results_data[@"recommendations"][@"mainRecommendation"][@"text"] length]>0){
        self.mainRecomendation.text = results_data[@"recommendations"][@"mainRecommendation"][@"text"];
    } else {
        self.mainRecomendation.text = @"";
    }
    
    if ([results_data[@"recommendations"][@"scoreNote"][@"text"] isKindOfClass:[NSString class]]&&[results_data[@"recommendations"][@"scoreNote"][@"text"] length]>0){
        self.scoreNoteText.text = results_data[@"recommendations"][@"scoreNote"][@"text"];
    } else {
        self.scoreNoteText.text = @"";
    }
    
    self.mainRecomendation.height = [Global heightLabel:self.mainRecomendation];
    self.scoreNoteText.height = [Global heightLabel:self.scoreNoteText];
    
    self.scoreLineContainer.y = self.mainRecomendation.bottom;
    self.scoreNoteText.y = self.scoreLineContainer.bottom;
    self.medSearchBtn.y = self.scoreNoteText.bottom+20;
    self.stateImage.y = self.scoreNoteText.y;

    [self.headerView setupAutosizeBySubviewsWithBottomDistance:0];
}

-(void)showScoreLine{
    if ([results_data[@"score"] isEqual:[NSNull null]] ||
        !results_data[@"score"] ||
        [results_data[@"score"] isEqual:[NSNull null]] ||
        !results_data[@"score"]) {
        self.scoreValue.hidden = YES;
        self.scoreCircle.hidden = YES;
    } else{
        self.scoreValue.hidden = NO;
        self.scoreCircle.hidden = NO;
    }
    float margin_l = 12.0;
    float margin_r = 12.0;
    float d = 51.0;
    float work_len = 320 - margin_l - margin_r - d;
    float scoreLevel = [results_data[@"score"] floatValue];
    float maxValue = ([results_data[@"sex"] isEqualToString:@"male"]?47:20);
    if (scoreLevel < 0) {
        scoreLevel = 0;
    }
    if (maxValue<0) {
        maxValue = 0;
    }
    if (scoreLevel>maxValue) {
        scoreLevel = maxValue;
    }
    float posX = scoreLevel * work_len / maxValue;
    posX = posX + margin_l;
    float percent = (100/maxValue)*scoreLevel;
    self.scoreCircle.frame = RectSetX(self.scoreCircle.frame, posX);
    self.scoreValue.center = self.scoreCircle.center;
    self.scoreValue.y = self.scoreCircle.y-3;
    self.scoreValue.text = [NSString stringWithFormat:@"%.0f",percent];
}

#pragma mark - redAlert results

-(void)showMainInfoRed{
    
    self.stateImageRed.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@_small",results_data[@"recommendations"][@"fullScreenAlert"][@"state"]]];
    
    if ([results_data[@"recommendations"][@"mainRecommendation"][@"text"] isKindOfClass:[NSString class]]&&[results_data[@"recommendations"][@"mainRecommendation"][@"text"] length]>0){
        self.mainRecomendationRed.text = results_data[@"recommendations"][@"mainRecommendation"][@"text"];
    } else {
        self.mainRecomendationRed.text = @"";
    }

//    self.mainRecomendationRed.text = @"";
    
    if ([results_data[@"recommendations"][@"fullScreenAlert"][@"text"] isKindOfClass:[NSString class]]&&[results_data[@"recommendations"][@"fullScreenAlert"][@"text"] length]>0){
        self.scoreNoteTextRed.text = results_data[@"recommendations"][@"fullScreenAlert"][@"text"];
    } else {
        self.scoreNoteTextRed.text = @"";
    }
    
    self.medSearchBtnRed.hidden = ![results_data[@"recommendations"][@"placesLinkShouldBeVisible"] boolValue];
    self.mainRecomendationRed.height = [Global heightLabel:self.mainRecomendationRed];
    self.scoreNoteTextRed.height = [Global heightLabel:self.scoreNoteTextRed];
    
    self.scoreLineContainerRed.y = self.mainRecomendationRed.bottom+20;
    self.scoreNoteTextRed.y = self.scoreLineContainerRed.bottom;
    self.medSearchBtnRed.y = self.scoreNoteTextRed.bottom+50;
    self.stateImageRed.y = self.scoreNoteTextRed.y;
    self.separ1.y = self.mainRecomendationRed.bottom+15;
    self.separ2.y = self.scoreNoteTextRed.bottom+15;
    [self.headerViewRed setupAutosizeBySubviewsWithBottomDistance:60];
    [self.scrollViewRed setup_autosizeWithBottomDistance:0];
}

-(void)showScoreLineRed{
    if ([results_data[@"score"] isEqual:[NSNull null]] ||
        !results_data[@"score"] ||
        [results_data[@"score"] isEqual:[NSNull null]] ||
        !results_data[@"score"]) {
        self.scoreValueRed.hidden = YES;
        self.scoreCircleRed.hidden = YES;
    } else{
        self.scoreValueRed.hidden = NO;
        self.scoreCircleRed.hidden = NO;
    }
    float margin_l = 12.0;
    float margin_r = 12.0;
    float d = 51.0;
    float work_len = 320 - margin_l - margin_r - d;
    float scoreLevel = [results_data[@"score"] floatValue];
    float maxValue = ([results_data[@"sex"] isEqualToString:@"male"]?47:20);
    if (scoreLevel < 0) {
        scoreLevel = 0;
    }
    if (maxValue<0) {
        maxValue = 0;
    }
    if (scoreLevel>maxValue) {
        scoreLevel = maxValue;
    }
    float posX = scoreLevel * work_len / maxValue;
    posX = posX + margin_l;
    float percent = (100/maxValue)*scoreLevel;
    self.scoreCircleRed.frame = RectSetX(self.scoreCircleRed.frame, posX);
    self.scoreValueRed.center = self.scoreCircleRed.center;
    self.scoreValueRed.y = self.scoreCircleRed.y-3;
    self.scoreValueRed.text = [NSString stringWithFormat:@"%.0f",percent];
}




#pragma mark - UITableView delegate&DataSource


-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    float h_header = [Helper heightText:[results_banners_array[section] objectForKey:@"title"] withFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0] withWidth:250.0] + 20;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.tableView.frame.size.width, h_header)];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, 250, h_header - 20 + 2)];
    label.textColor = [UIColor whiteColor];
    [label setFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0]];
    NSString *string =[results_banners_array[section] objectForKey:@"title"];
    [label setText:string];
    [view addSubview:label];
    UIImageView *topSepar = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, view.width, 0.5)];
    topSepar.backgroundColor = RGB(152, 198, 245);
    
    UIImageView *bottomSepar = [[UIImageView alloc] initWithFrame:CGRectMake(0, h_header-0.5f, view.width, 0.5)];
    bottomSepar.backgroundColor = RGB(152, 198, 245);
    
    [view addSubview:topSepar];
    [view addSubview:bottomSepar];
    
    [view setBackgroundColor:[self attentionCellColor]];
    return view;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return [Helper heightText:[results_banners_array[section] objectForKey:@"title"] withFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0] withWidth:250.0] + 20;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    riskCell *cell = (riskCell*)[self tableView:tableView cellForRowAtIndexPath:indexPath];
    float titleH = [Global heightLabel:cell.text_name];
    float textH = [Global heightLabel:cell.text_data];
    float cellH = titleH + textH + 23;
    
    return cellH;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return results_banners_array.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [results_banners_array[section][@"keys"] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"riskCell";
    riskCell *cell = nil;
    cell = (riskCell *) [self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"riskCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[riskCell class]])
            {
                cell = (riskCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *item = results_banners_array[indexPath.section][@"keys"][indexPath.row];
    cell.img_icon.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@_small",item[@"state"]]];

    NSString *titleText;
    NSString *subtitleText;
    NSString *textText;
    
    if (item[@"title"]&&[item[@"title"] isKindOfClass:[NSString class]]){
        titleText = item[@"title"];
    } else {
        titleText = @"";
    }
    
    if (item[@"subtitle"]&&[item[@"subtitle"] isKindOfClass:[NSString class]]){
        subtitleText = item[@"subtitle"];
    } else {
        subtitleText = @"";
    }
    
    if (item[@"note"]&&[item[@"note"] isKindOfClass:[NSString class]]){
        textText = item[@"note"];
    } else {
        textText = @"";
    }
    
    
    
    cell.text_name.text = titleText;
    cell.text_data.text = [NSString stringWithFormat:@"%@%@%@",subtitleText, (subtitleText.length>0&&textText.length>0)?@"\n":@"", textText];
    
    cell.text_name.font = [self cellTitleFont];
    cell.text_data.font = [self cellTextFont];
    
    cell.text_name.height = [Global heightLabel:cell.text_name];
    cell.text_data.height = [Global heightLabel:cell.text_data];
    cell.text_data.y = cell.text_name.bottom +4;
    
    cell.contentView.backgroundColor = [UIColor clearColor];
    
//    RecommendationState  recState;
//    
//    SWITCH(item[@"state"]){
//        CASE(@"ok"){
//            
//        }
//        CASE(@"attention"){
//            
//        }
//        CASE(@"bell"){
//            
//        }
//        CASE(@"doctor"){
//            
//        }
//    }
    
    if ([Global isNotNull:item[@"pageUrl"]]){
        cell.backgroundColor = [self normCellColor];
        cell.arrowRight.hidden = NO;
    } else {
        cell.backgroundColor = [self attentionCellColor];
        cell.arrowRight.hidden = YES;
    }
    
    cell.backgroundColor = [self normCellColor];

    
    UIView *sel_view = [[UIView alloc] init];
    sel_view.backgroundColor = [UIColor clearColor];
    cell.selectedBackgroundView = sel_view;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSMutableDictionary *item = results_banners_array[indexPath.section][@"keys"][indexPath.row];
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    
    
    
    if (item[@"action"]){
        SEL selMenu = NSSelectorFromString(item[@"action"]);
        typedef void (*methodPtr)(id, SEL);
        methodPtr command = (methodPtr)[self methodForSelector:selMenu];
        if ([self respondsToSelector:selMenu]){
            command(self, selMenu);
        }

    } else {
        if ([Global isNotNull:item[@"pageUrl"]]){
            [self openDetail:item];
        }
    }

}

#pragma mark - Actions

-(IBAction)shareAction:(id)sender{
    [self showMessageWithTextInput:@"E-mail" msg:@"Отправить результат тестирования по почте" title:@"Share" btns:@[@"Отмена",@"Отправить"] params:@{@"text":User.userData[@"email"]} result:^(int index, NSString *text){
        if (index == 1){
            if (text.length == 0) { text = User.userData[@"email"];}
            [ServData shareTest:[results_data[@"id"] intValue] viaEmail:text completition:^(BOOL success, id result){
                NSLog(@"%@",result);
                if (success){
                     [self showMessage:[NSString stringWithFormat:@"Результат теста \nотправлен на почту \n%@",text] title:@"Уведомление"];
                } else {
                    [self showMessage:@"Ошибка отправки" title:@"Ошибка"];
                }
            }];
        }
    }];
}

-(void)openDetail:(NSMutableDictionary*)info{
    _resultRiskAnalDetail = [ResultRiskAnalDetail new];
    _resultRiskAnalDetail.blockInfo = info;
    [self.navigationController pushViewController:_resultRiskAnalDetail animated:YES];
}

-(IBAction)infoAction:(id)sender{
    _usefulKnowPage = [UsefulKnowPage new];
    [self.navigationController pushViewController:_usefulKnowPage animated:YES];
}

-(void)openAdditionalFoodTest{
    int testId = [results_data[@"id"] intValue];
    if ([GlobalData resultDietForTestId:testId]!=nil&&!self.isAppearFromBack){
        NSMutableDictionary* result = [GlobalData resultDietForTestId:testId];
        [self goToResult:result];
    } else {
        [self goToTest];
    }
}

-(void)goToTest{
    [self removeController:dietTestResults];
    dietTest = [DietTest new];
    int testId = [results_data[@"id"] intValue];
    dietTest.testId = testId;
    [self.navigationController pushViewController:dietTest animated:YES];

}

-(void)goToResult:(NSMutableDictionary*)result{
    [self removeController:dietTest];
    dietTestResults = [DietTestResults new];
    dietTestResults.result = result;
    [self.navigationController pushViewController:dietTestResults animated:YES];
}

-(void)removeController:(id)contr{
    NSMutableArray *allViewControllers = [NSMutableArray arrayWithArray: self.navigationController.viewControllers];
    [allViewControllers removeObjectIdenticalTo: contr];
    self.navigationController.viewControllers = allViewControllers;
}

-(IBAction)openProfilacticCalendarAction:(id)sender{
    
}

-(IBAction)searchMedsAction:(id)sender{
    searchInstitutionPage = [SearchInstitutionPage new];
    [self.navigationController pushViewController:searchInstitutionPage animated:YES];
}


@end

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
    self.mainElement = self.tableView;
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
    [self showData];
}

-(void)setupInterface{
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
    [self showScoreLine];
    [self showMainInfo];
    [self.tableView reloadData];
    self.tableView.tableHeaderView = self.headerView;
}

-(void)showMainInfo{
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
    self.scoreCircle.frame = RectSetX(self.scoreCircle.frame, posX);
    self.scoreValue.center = self.scoreCircle.center;
    self.scoreValue.y = self.scoreCircle.y-3;
    self.scoreValue.text = [NSString stringWithFormat:@"%.0f",scoreLevel];
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
    
    if (item[@"pageUrl"]){
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
        if (item[@"pageUrl"]){
            [self openDetail:item];
        }
    }

    
//
//    NSString *key = item[@"pageUrl"];
//    NSDictionary *data_page = [self getDicPage:key];
//    
//    if (!data_page || [data_page isEqual:[NSNull null]]) {
//        [Helper fastAlert:@"Нет данных"];
//        return;
//    }
//    
//    NSDictionary *data_banner = nil;
//    if (key && ![key isEqual:[NSNull null]]) {
//        data_banner = [self getDicBanner:key];
//    }
//    
//    if (!detailResultRiskAnalysis) {
//        detailResultRiskAnalysis = [[DetailResultRiskAnalysis alloc] initWithNibName:@"DetailResultRiskAnalysis" bundle:nil];
//    }
//    
//    detailResultRiskAnalysis.data_page = data_page;
//    detailResultRiskAnalysis.data_banner = data_banner;
//    
//    [self.navigationController pushViewController:detailResultRiskAnalysis animated:YES];
}

#pragma mark - Actions

-(IBAction)shareAction:(id)sender{
    [self showMessageWithTextInput:User.userData[@"email"] msg:@"Отправить результат тестирования по почте" title:@"Share" btns:@[@"Отмена",@"Отправить"] params:nil result:^(int index, NSString *text){
        if (index == 1){
            if (text.length == 0) { text = User.userData[@"email"];}
            [ServData shareTest:[results_data[@"id"] intValue] viaEmail:text completition:^(BOOL success, id result){
                NSLog(@"%@",result);
                [self showMessage:[NSString stringWithFormat:@"Результат теста \nотправлен на почту \n%@",text] title:@"Уведомление"];
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
    dietTest = [DietTest new];
    [self.navigationController pushViewController:dietTest animated:YES];
}

-(IBAction)openProfilacticCalendarAction:(id)sender{
    
}

-(IBAction)searchMedsAction:(id)sender{
    searchInstitutionPage = [SearchInstitutionPage new];
    [self.navigationController pushViewController:searchInstitutionPage animated:YES];
}


@end

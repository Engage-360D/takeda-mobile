//
//  ResultRiskAnalysis.m
//  takeda
//
//  Created by Serg on 4/6/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "ResultRiskAnalysis.h"
#import "riskCell.h"
#import "DetailResultRiskAnalysis.h"


@interface ResultRiskAnalysis (){
    NSMutableArray *resultData;
    id data;
}
@end

@implementation ResultRiskAnalysis
@synthesize tableView;
@synthesize headerView;
@synthesize scoreCircle;
@synthesize scoreValue;
@synthesize needUpdate;
DetailResultRiskAnalysis *detailResultRiskAnalysis;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        resultData = [[NSMutableArray alloc] init];
        // Custom initialization
    }
    return self;
}



-(void)getSavedData{
    if (!resultData) {
        resultData = [[NSMutableArray alloc] init];
    }
    [resultData removeAllObjects];
    
    data = [[UserData sharedObject] getLastSavedAnalisRiskData];
    if (data) {
        if ([data isKindOfClass:[NSDictionary class]]) {
            if (![[data objectForKey:@"recommendations"] isEqual:[NSNull null]] && [data hasKey:@"recommendations"]) {
                
                [self makeHeaderUI:[data objectForKey:@"recommendations"]];
                
                if (![[[data objectForKey:@"recommendations"] objectForKey:@"banners"] isEqual:[NSNull null]] && [[data objectForKey:@"recommendations"] hasKey:@"banners"]) {
                    [self makeDataResultArray:[[data objectForKey:@"recommendations"] objectForKey:@"banners"]];
                }
            }
        }
        
        [self setScoreData:data];
        
    }
}

-(void)setScoreData:(NSDictionary*)dic{
    
    if ([[dic objectForKey:@"scoreValue"] isEqual:[NSNull null]] ||
        ![dic objectForKey:@"scoreValue"] ||
        [[dic objectForKey:@"sex"] isEqual:[NSNull null]] ||
        ![dic objectForKey:@"sex"]) {
        self.scoreValue.hidden = YES;
        self.scoreCircle.hidden = YES;
    }else{
        self.scoreValue.hidden = NO;
        self.scoreCircle.hidden = NO;
    }
    float margin_l = 12.0;
    float margin_r = 12.0;
    float d = 51.0;
    float work_len = 320 - margin_l - margin_r - d;
    float scoreLevel = [[dic objectForKey:@"scoreValue"] floatValue];
    float maxValue = ([[dic objectForKey:@"sex"] isEqualToString:@"male"]?47:20);
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
    self.scoreValue.text = [NSString stringWithFormat:@"%.0f",scoreLevel];
}


-(void)makeHeaderUI:(NSDictionary*)dic{
    for (UIView *view in [headerView subviews]) {
        if (view.tag > 100) {
            [view removeFromSuperview];
        }
    }
    headerView.frame = RectSetHeight(headerView.frame, 100);
    
    float current_Y = 110;
    
    NSArray *keys = @[@"scoreDescription",@"dangerAlerts",@"mainRecommendation"];
    for (int i = 0; i < [keys count]; i++) {
        if ([dic objectForKey:[keys objectAtIndex:i]] && ![[dic objectForKey:[keys objectAtIndex:i]] isEqual:[NSNull null]]) {
            
            NSDictionary *data_d = [dic objectForKey:[keys objectAtIndex:i]];
            
            if ([data_d objectForKey:@"text"] && ![[data_d objectForKey:@"text"] isEqual:[NSNull null]]) {
                
                UILabel* textLabel = [[UILabel alloc] init];
                textLabel.text = [data_d objectForKey:@"text"];
                textLabel.tag = 100 + i;
                textLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14.0];
                textLabel.numberOfLines = 0;
                textLabel.textColor = [UIColor whiteColor];
                
                if ([data_d objectForKey:@"state"] && ![[data_d objectForKey:@"state"] isEqual:[NSNull null]]) {
                    UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(12, current_Y + 10, 32, 28)];
                    
                    imgView.image = [UIImage imageNamed:[self getNameImage:[data_d objectForKey:@"state"]]];//[UIImage imageNamed:@"danger_big.png"];
                    
                    
                    
                    
                    imgView.contentMode = UIViewContentModeCenter;
                    imgView.tag = 150 + i;
                    [headerView addSubview:imgView];
                    float width = 246;
                    float h_text = [Helper heightText:textLabel.text withFont:textLabel.font withWidth:width] + 2;
                    textLabel.frame = CGRectMake(imgView.frame.origin.x + imgView.frame.size.width + 18, current_Y, width, h_text);
                    
                }else{
                    float width = 280;
                    float h_text = [Helper heightText:textLabel.text withFont:textLabel.font withWidth:width] + 2;
                    textLabel.frame = CGRectMake(20, current_Y, width, h_text);
                    
                }
                [headerView addSubview:textLabel];
                current_Y = current_Y + textLabel.frame.size.height + 20;
            }
            
        }
    }
    
    self.tableView.tableHeaderView = nil;
    headerView.frame = RectSetHeight(headerView.frame, current_Y);
    self.tableView.tableHeaderView = headerView;
}

























-(void)makeDataResultArray:(NSDictionary*)data_d{
    if ([data_d isEqual:[NSNull null]] || !data_d) {
        return;
    }
    
    if (!resultData) {
        resultData = [[NSMutableArray alloc] init];
    }
    NSArray *keys = [data_d allKeys];
    for (int i = 0; i < [keys count]; i++) {
        NSDictionary *dic = [data_d objectForKey:[keys objectAtIndex:i]];
        
        if (dic && ![dic isEqual:[NSNull null]]) {
            [resultData addObject:@{@"title":[self getTitle:[keys objectAtIndex:i]],
                                    @"data":@[
                                            @{@"img":[self getNameImage:[dic objectForKey:@"state"]],
                                              @"text":[self getTextCell:dic],
                                              @"key":[keys objectAtIndex:i]}
                                            ]}];
        }
        
        
        
    }
    [self.tableView reloadData];
}


-(NSString*)getTitle:(NSString*)key{
    if ([key isEqual:[NSNull null]]) {
        return @"";
    }
    SWITCH(key){
        CASE(@"arterialPressure"){
            return @"Артериальное давление";
            break;
        }
        CASE(@"arterialPressureDrugs"){
            return @"Препараты для нормаллизации давления";
            break;
        }
        CASE(@"bmi"){
            return @"Сердце";
            break;
        }
        CASE(@"cholesterolDrugs"){
            return @"Препараты для снижения холестирина";
            break;
        }
        CASE(@"cholesterolLevel"){
            return @"Холестирин";
            break;
        }
        CASE(@"extraSalt"){
            return @"Соль";
            break;
        }
        CASE(@"physicalActivity"){
            return @"Физическая активность";
            break;
        }
        CASE(@"smoking"){
            return @"Курение";
            break;
        }
        CASE(@"sugarProblems"){
            return @"Сахар";
            break;
        }
        DEFAULT{
            return @"";
            break;
        }
    }
}



-(NSString*)getNameImage:(NSString*)state{
    if ([state isEqual:[NSNull null]]) {
        return @"";
    }
    SWITCH(state){
        CASE(@"attention"){
            return @"danger_icon";
            break;
        }
        CASE(@"ok"){
            return @"good_result_icon";
            break;
        }
        CASE(@"bell"){
            return @"bell_icon";
            break;
        }
        CASE(@"doctor"){
            return @"doc_tools_icon";
            break;
        }
        CASE(@"ask"){
            return @"undefined_result_icon";
            break;
        }
        DEFAULT{
            return @"";
            break;
        }
    }
}


-(NSString*)getTextCell:(NSDictionary*)dic{
    NSString *text = @"";
    if ([dic objectForKey:@"title"] && ![[dic objectForKey:@"title"] isEqual:[NSNull null]]) {
        text = [dic objectForKey:@"title"];
    }
    
    if ([dic objectForKey:@"aberration"] && ![[dic objectForKey:@"aberration"] isEqual:[NSNull null]]) {
        text = [NSString stringWithFormat:@"%@\n%@",text,[dic objectForKey:@"aberration"]];
    }
    
    if ([dic objectForKey:@"note"] && ![[dic objectForKey:@"note"] isEqual:[NSNull null]]) {
        text = [NSString stringWithFormat:@"%@\n%@",text,[dic objectForKey:@"note"]];
    }
    return text;
}





/*
-(void)initTMPArray{
    return;
    
    
    if (!resultData) {
        resultData = [[NSMutableArray alloc] init];
    }
    [resultData addObject:@{@"title": @"Рекомендации по SCORE",
                            @"data":@[
                                    @{@"img": @"danger_icon",@"text":@"Диабет.\nГруппа риска."},
                                    @{@"img": @"danger_icon",@"text":@"Инфаркт.\nГруппа риска."},
                                    @{@"img": @"bell_icon",@"text":@"Профилактика.\nКатегория: \"норма\"\nНеобходимо улучшение."}
                                    ]}];
    [resultData addObject:@{@"title": @"Курение",
                            @"data":@[
                                    @{@"img": @"danger_icon",@"text":@"Курение.\nГруппа риска."},
                                    ]}];
    [resultData addObject:@{@"title": @"Физическая активность",
                            @"data":@[
                                    @{@"img": @"bell_icon",@"text":@"Физическая активность\nОтклонение от нормы: 10%\nНеобходимо улучшение."},
                                    @{@"img": @"good_result_icon",@"text":@"Вес\nОтклонение от нормы: 2%\nВсе хорошо"}
                                    ]}];
    [resultData addObject:@{@"title": @"Основные риски",
                            @"data":@[
                                    @{@"img": @"bell_icon",@"text":@"Уровень холестирина\nОтклонение от нормы: 10%\nНеобходимо улучшение."},
                                    @{@"img": @"danger_icon",@"text":@"Систолическое давление\n Отклонение от нормы: 30%\nВсе хорошо"},
                                    @{@"img": @"doc_tools_icon",@"text":@"Отмечалось повышение\nуровня сахара в крови\nОбратитесь к врачу"},
                                    @{@"img": @"doc_tools_icon",@"text":@"Ваше давление выше нормы\nОбратитесь к врачу"},
                                    @{@"img": @"doc_tools_icon",@"text":@"Ваш холестерин выше нормы\nОбратитесь к врачу"}
                                    ]}];
    [resultData addObject:@{@"title": @"Диета",
                            @"data":@[
                                    @{@"img": @"bell_icon",@"text":@"Потребление соли\nОтклонение от нормы: 10%\nНеобходимо улучшение."},
                                    @{@"img": @"undefined_result_icon",@"text":@"Дополнительная\nкорректировка диеты\nПройти опрос"}
                                    ]}];
}


*/


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setNavigationPanel];
    [self setNavImage];
    //[self initTMPArray];
    self.tableView.tableHeaderView = self.headerView;
    [self.tableView reloadData];
    self.tableView.backgroundColor = [UIColor clearColor];
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (needUpdate) {
        [self getSavedData];
        needUpdate = NO;
    }
    
}


#pragma mark - navigation panel
-(void)setNavImage{
    UINavigationBar *navBar = [[self navigationController] navigationBar];
    UIImage *backgroundImage;
    backgroundImage = [[UIImage imageNamed:@"nav_bar_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 10)] ;
    [navBar setBackgroundImage:backgroundImage forBarMetrics:UIBarMetricsDefault];
}

-(void)setNavigationPanel{
    UIView *view = [[UIView alloc] initWithFrame:[self.navigationController.navigationBar frame]];
    
    UIImage *logoImage = [UIImage imageNamed:@"title_logo"];
    
    
    UIImageView *img_logo = [[UIImageView alloc] initWithFrame:CGRectMake(40, 8, logoImage.size.width, logoImage.size.height)];
    img_logo.image = logoImage;
    [view addSubview:img_logo];
    view.backgroundColor = [UIColor clearColor];
    self.navigationItem.titleView = view;
    
    
    
    UIImage *menuImage = [UIImage imageNamed:@"menu_icon"];
    UIButton *aButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [aButton setImage:menuImage forState:UIControlStateNormal];
    aButton.frame = CGRectMake(0.0,0.0,menuImage.size.width+20,menuImage.size.height);
    aButton.contentEdgeInsets = (UIEdgeInsets){.left=-20};
    [aButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *menuButton = [[UIBarButtonItem alloc] initWithCustomView:aButton];
    self.navigationItem.leftBarButtonItem = menuButton;
    
    
    UIImage *peopleImage = [UIImage imageNamed:@"people_icon"];
    UIButton *bButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [bButton setImage:peopleImage forState:UIControlStateNormal];
    bButton.frame = CGRectMake(0.0,0.0,peopleImage.size.width+10,peopleImage.size.height);
    bButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    //[bButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *peopleButton = [[UIBarButtonItem alloc] initWithCustomView:bButton];
    
    
    UIImage *alarmImage = [UIImage imageNamed:@"alarm_icon"];
    UIButton *cButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cButton setImage:alarmImage forState:UIControlStateNormal];
    cButton.frame = CGRectMake(0.0,0.0,alarmImage.size.width+10,alarmImage.size.height);
    cButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    //[cButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *alarmButton = [[UIBarButtonItem alloc] initWithCustomView:cButton];
    
    self.navigationItem.rightBarButtonItems = @[peopleButton,alarmButton];
    
}

-(void)openLeftMenu{
    if ([self.slideMenuController isMenuOpen]) {
        [self.slideMenuController closeMenuAnimated:YES completion:nil];
    }else{
        [self.slideMenuController openMenuAnimated:YES completion:nil];
    }
}
#pragma mark -








#pragma mark - Table view data source

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    
    float h_header = [Helper heightText:[[resultData objectAtIndex:section] objectForKey:@"title"] withFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0] withWidth:250.0] + 20;
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.tableView.frame.size.width, h_header)];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, 250, h_header - 20 + 2)];
    label.textColor = [UIColor whiteColor];
    [label setFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0]];
    NSString *string =[[resultData objectAtIndex:section] objectForKey:@"title"];
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]];
    return view;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return [Helper heightText:[[resultData objectAtIndex:section] objectForKey:@"title"] withFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0] withWidth:250.0] + 20;
}



- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [resultData count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[[resultData objectAtIndex:section] objectForKey:@"data"] count];
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

    cell.img_icon.image = [UIImage imageNamed:[[[[resultData objectAtIndex:indexPath.section] objectForKey:@"data"] objectAtIndex:indexPath.row] objectForKey:@"img"]];
    
    
    cell.text_data.text = [[[[resultData objectAtIndex:indexPath.section] objectForKey:@"data"] objectAtIndex:indexPath.row] objectForKey:@"text"];
    cell.text_data.font = [self getFontDescription];
    cell.contentView.backgroundColor = [UIColor clearColor];
    
    
    
    if (indexPath.row == 0) {
        cell.top_separator.hidden = NO;
    }else{
        cell.top_separator.hidden = YES;
    }
    
    
    
    cell.backgroundColor = [UIColor colorWithRed:123.0/255 green:182.0/255 blue:242.0/255 alpha:1];
    
    UIView *sel_view = [[UIView alloc] init];
    sel_view.backgroundColor = [UIColor clearColor];
    cell.selectedBackgroundView = sel_view;
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [Helper heightText:[[[[resultData objectAtIndex:indexPath.section] objectForKey:@"data"] objectAtIndex:indexPath.row] objectForKey:@"text"] withFont:[self getFontDescription] withWidth:244.0] + 14;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    NSString *key = [[[[resultData objectAtIndex:indexPath.section] objectForKey:@"data"] objectAtIndex:indexPath.row] objectForKey:@"key"];
    
    
    NSDictionary *data_page = [self getDicPage:key];
    
    if (!data_page || [data_page isEqual:[NSNull null]]) {
        [Helper fastAlert:@"Нет данных"];
        return;
    }
    
    
    
    
    NSDictionary *data_banner = nil;
    if (key && ![key isEqual:[NSNull null]]) {
        data_banner = [self getDicBanner:key];
    }
    
    
    if (!detailResultRiskAnalysis) {
        detailResultRiskAnalysis = [[DetailResultRiskAnalysis alloc] initWithNibName:@"DetailResultRiskAnalysis" bundle:nil];
    }
    
    
    
    detailResultRiskAnalysis.data_page = data_page;
    detailResultRiskAnalysis.data_banner = data_banner;
    
    [self.navigationController pushViewController:detailResultRiskAnalysis animated:YES];
}




-(NSDictionary*)getDicBanner:(NSString*)key{
    if (data) {
        if ([data isKindOfClass:[NSDictionary class]]) {
            if (![[data objectForKey:@"recommendations"] isEqual:[NSNull null]] && [data hasKey:@"recommendations"]) {
                if (![[[data objectForKey:@"recommendations"] objectForKey:@"banners"] isEqual:[NSNull null]] && [[data objectForKey:@"recommendations"] hasKey:@"banners"]) {
                    if ([[[data objectForKey:@"recommendations"] objectForKey:@"banners"] objectForKey:key]) {
                        return [[[data objectForKey:@"recommendations"] objectForKey:@"banners"] objectForKey:key];
                    }
                }
            }
        }
    }
    return nil;
}



-(NSDictionary*)getDicPage:(NSString*)key{
    if (data) {
        if ([data isKindOfClass:[NSDictionary class]]) {
            if (![[data objectForKey:@"recommendations"] isEqual:[NSNull null]] && [data hasKey:@"recommendations"]) {
                if (![[[data objectForKey:@"recommendations"] objectForKey:@"pages"] isEqual:[NSNull null]] && [[data objectForKey:@"recommendations"] hasKey:@"pages"]) {
                    if ([[[data objectForKey:@"recommendations"] objectForKey:@"pages"] objectForKey:key]) {
                        return [[[data objectForKey:@"recommendations"] objectForKey:@"pages"] objectForKey:key];
                    }
                }
            }
        }
    }
    return nil;
}





-(UIFont*)getFontDescription{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}


@end

//
//  RecomendationPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "RecomendationPage.h"
#import "riskCell.h"


@interface RecomendationPage (){
    NSMutableArray *listData;
}

@end

@implementation RecomendationPage
@synthesize tableView;
@synthesize calendar_btn;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


-(void)initTMPArray{
   if (!listData) {
        listData = [[NSMutableArray alloc] init];
    }
    [listData addObject:@{@"title": @"Рекомендации по SCORE",
                            @"data":@[
                                    @{@"img": @"danger_icon_black",@"text":@"Диабет.\nГруппа риска."},
                                    @{@"img": @"danger_icon_black",@"text":@"Инфаркт.\nГруппа риска."},
                                    @{@"img": @"bell_icon_black",@"text":@"Профилактика.\nКатегория: \"норма\"\nНеобходимо улучшение."}
                                    ]}];
    [listData addObject:@{@"title": @"Курение",
                            @"data":@[
                                    @{@"img": @"danger_icon_black",@"text":@"Курение.\nГруппа риска."},
                                    ]}];
    [listData addObject:@{@"title": @"Физическая активность",
                            @"data":@[
                                    @{@"img": @"bell_icon_black",@"text":@"Физическая активность\nОтклонение от нормы: 10%\nНеобходимо улучшение."},
                                    @{@"img": @"good_result_icon_black",@"text":@"Вес\nОтклонение от нормы: 2%\nВсе хорошо"}
                                    ]}];
    [listData addObject:@{@"title": @"Основные риски",
                            @"data":@[
                                    @{@"img": @"bell_icon_black",@"text":@"Уровень холестирина\nОтклонение от нормы: 10%\nНеобходимо улучшение."},
                                    @{@"img": @"danger_icon_black",@"text":@"Систолическое давление\n Отклонение от нормы: 30%\nВсе хорошо"},
                                    @{@"img": @"doc_tools_icon_black",@"text":@"Отмечалось повышение\nуровня сахара в крови\nОбратитесь к врачу"},
                                    @{@"img": @"doc_tools_icon_black",@"text":@"Ваше давление выше нормы\nОбратитесь к врачу"},
                                    @{@"img": @"doc_tools_icon_black",@"text":@"Ваш холестерин выше нормы\nОбратитесь к врачу"}
                                    ]}];
    [listData addObject:@{@"title": @"Диета",
                            @"data":@[
                                    @{@"img": @"bell_icon_black",@"text":@"Потребление соли\nОтклонение от нормы: 10%\nНеобходимо улучшение."},
                                    @{@"img": @"undefined_result_icon_black",@"text":@"Дополнительная\nкорректировка диеты\nПройти опрос"}
                                    ]}];
}
 
 
 


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setNavImage];
    [self setNavigationPanel];
    [self initTMPArray];
    [self.tableView reloadData];
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
    
    float h_header = [Helper heightText:[[listData objectAtIndex:section] objectForKey:@"title"] withFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0] withWidth:250.0] + 20;
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.tableView.frame.size.width, h_header)];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, 250, h_header - 20 + 2)];
    label.textColor = [UIColor colorWithRed:53.0/255 green:65.0/255 blue:71.0/255 alpha:1];
    [label setFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0]];
    NSString *string =[[listData objectAtIndex:section] objectForKey:@"title"];
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]];
    return view;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return [Helper heightText:[[listData objectAtIndex:section] objectForKey:@"title"] withFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0] withWidth:250.0] + 20;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [listData count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[[listData objectAtIndex:section] objectForKey:@"data"] count];
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
    
    cell.img_icon.image = [UIImage imageNamed:[[[[listData objectAtIndex:indexPath.section] objectForKey:@"data"] objectAtIndex:indexPath.row] objectForKey:@"img"]];
    
    
    cell.text_data.text = [[[[listData objectAtIndex:indexPath.section] objectForKey:@"data"] objectAtIndex:indexPath.row] objectForKey:@"text"];
    cell.text_data.font = [self getFontDescription];
    cell.contentView.backgroundColor = [UIColor clearColor];
    
    cell.text_data.textColor = [UIColor colorWithRed:53.0/255 green:65.0/255 blue:71.0/255 alpha:1];
    
    
    if (indexPath.row == 0) {
        cell.top_separator.hidden = NO;
    }else{
        cell.top_separator.hidden = YES;
    }
    
    
    
    cell.backgroundColor = [UIColor whiteColor];
    
    UIView *sel_view = [[UIView alloc] init];
    sel_view.backgroundColor = [UIColor clearColor];
    cell.selectedBackgroundView = sel_view;
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [Helper heightText:[[[[listData objectAtIndex:indexPath.section] objectForKey:@"data"] objectAtIndex:indexPath.row] objectForKey:@"text"] withFont:[self getFontDescription] withWidth:244.0] + 14;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{

}



-(UIFont*)getFontDescription{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}

@end

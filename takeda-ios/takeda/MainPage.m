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
}

@end

@implementation MainPage

- (void)viewDidLoad
{
    [super viewDidLoad];
    menu_data = [NSMutableArray arrayWithArray: @[@[@{@"title":@"Физическая нагрузка",@"rightTitle":@"мин", @"type":[NSNumber numberWithInt:ctLeftCaptionRightCaption],@"action":[NSNumber numberWithInt:physExercises]},
                                                    @{@"title":@"Принять таблетки",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:haveDrugs]},
                                                    @{@"title":@"Что Вы сегодня ели?",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:eatToday]}],
                                                  @[@{@"title":@"Мои рекомендации",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:myRecommendations]},
                                                    @{@"title":@"Мои успехи",@"type":[NSNumber numberWithInt:ctLeftCaptionRightArrow],@"action":[NSNumber numberWithInt:mySuccess]},
                                                    @{@"title":@"Сводный отчет", @"subtitle":@"Переход на сайт" ,@"type":[NSNumber numberWithInt:ctCaptionSubtitleRightArrow],@"action":[NSNumber numberWithInt:commonReport]}]]];
    
    
   // [self.tableView.tableFooterView addSubview:self.tableView.separ];
   // [self.tableView.tableHeaderView addSubview: self.tableView.topSepar];
    self.navigationController.navigationBarHidden = NO;
    [self setNavigationPanel];
    [self setupInterface];

}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = NO;
    [self setNavImage];
    [self.tableView reloadData];
}

-(void)setupInterface{
    _percentLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:50];
    _indexCaptionLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    _datePeriod.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
    _todayLabel.font = [UIFont fontWithName:@"SegoeWP" size:14];
    _danger_text.text = @"Имеются противопоказания \n необходимо ознакомиться с инструкцией по применению";

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
//    switch ((indexPath.row)) {
//            break;
//    }
//        default:
//            break;
//    }
//    
//    
//    [self.tableView reloadData];
}





@end

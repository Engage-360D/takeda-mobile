//
//  RiskAnalysisPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "RiskAnalysisPage.h"
#import "AnalizDataUserPage.h"


@interface RiskAnalysisPage ()

@end

@implementation RiskAnalysisPage

@synthesize scroll;
@synthesize page_indicator;


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
    self.navigationController.navigationBarHidden = NO;
    [self setNavigationPanel];
    [self setFirstPageAnalize];
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = NO;
    [self setNavImage];
}



-(NSArray*)getQuestionsDataUser{
    return @[
      @{@"type": @"1",@"name":@"Пол",@"f_param":@"Мужчина",@"s_param":@"Женщина"},
      @{@"type": @"2",@"name":@"Возраст",@"description":@"лет"},
      @{@"type": @"2",@"name":@"Рост",@"description":@"см"},
      @{@"type": @"2",@"name":@"Вес",@"description":@"кг"},
      @{@"type": @"3",@"name":@"Курение"},
      @{@"type": @"2",@"name":@"Уровень общего холестирина",@"description":@"мг/дл"},
      @{@"type": @"3",@"name":@"Лекарства для снижения уровня холестерина"}
      ];
}

-(NSArray*)getQuestionsHistoryUser{
    return @[
             @{@"type": @"3",@"name":@"Диабет"},
             @{@"type": @"3",@"name":@"Повышения уровня сахара в крови"},
             @{@"type": @"2",@"name":@"Артериальное давнление",@"description":@"мм"},
             @{@"type": @"3",@"name":@"Препараты для понижения давления"},
             @{@"type": @"2",@"name":@"Ходьба",@"description":@"мин/нед"},
             @{@"type": @"2",@"name":@"Спорт",@"description":@"мин/нед"},
             @{@"type": @"3",@"name":@"Инфаркт/инсульт"},
             ];
}

-(NSArray*)getQuestionsDailyRation{
    return @[
             @{@"type": @"2",@"name":@"Соль",@"description":@"грамм/день"},
             @{@"type": @"3",@"name":@"Принимает ли препараты на основе ацетилсалициловой кислоты для профилактики риска тромбозов?"},
             ];
}













#pragma mark - init pages

-(void)setFirstPageAnalize{
    [self setPageindicator:1];
    AnalizDataUserPage*firstPage = [[AnalizDataUserPage alloc] initWithNibName:@"AnalizDataUserPage" bundle:nil];
    firstPage.view.frame = self.scroll.bounds;
    [self addChildViewController:firstPage];
    [self.scroll addSubview:firstPage.view];
    [firstPage willMoveToParentViewController:self];
    [firstPage.nextStepBtn addTarget:self action:@selector(goHistoryPacient:) forControlEvents:UIControlEventTouchDown];
    [firstPage.nextStepBtn setTitle:@"История пациента" forState:UIControlStateNormal];
    firstPage.titleRisk.text = @"Данные пациента";
    firstPage.sourceData = [self getQuestionsDataUser];
    [firstPage reloadData];
}

-(void)setSecondPageAnalize{
    [self setPageindicator:2];
    AnalizDataUserPage*historyPacientPage = [[AnalizDataUserPage alloc] initWithNibName:@"AnalizDataUserPage" bundle:nil];
    CGRect frame = self.scroll.bounds;
    frame.origin.x = self.view.frame.size.width;
    historyPacientPage.view.frame = frame;
    [self addChildViewController:historyPacientPage];
    [self.scroll addSubview:historyPacientPage.view];
    [historyPacientPage willMoveToParentViewController:self];
    [historyPacientPage.nextStepBtn addTarget:self action:@selector(goDailyRation:) forControlEvents:UIControlEventTouchDown];
    [historyPacientPage.nextStepBtn setTitle:@"Дневной рацион" forState:UIControlStateNormal];
    historyPacientPage.titleRisk.text = @"История пациента";
    [self.scroll setContentSize:CGSizeMake(320 * 2, 20)];
    
    historyPacientPage.sourceData = [self getQuestionsHistoryUser];
    [historyPacientPage reloadData];
}


-(void)setThirdPageAnalize{
    [self setPageindicator:3];
    AnalizDataUserPage*dailyRationPage = [[AnalizDataUserPage alloc] initWithNibName:@"AnalizDataUserPage" bundle:nil];
    CGRect frame = self.scroll.bounds;
    frame.origin.x = self.view.frame.size.width*2;
    dailyRationPage.view.frame = frame;
    [self addChildViewController:dailyRationPage];
    [self.scroll addSubview:dailyRationPage.view];
    [dailyRationPage willMoveToParentViewController:self];
    [dailyRationPage.nextStepBtn addTarget:self action:@selector(goResultPage:) forControlEvents:UIControlEventTouchDown];
    [dailyRationPage.nextStepBtn setTitle:@"Результаты" forState:UIControlStateNormal];
    dailyRationPage.titleRisk.text = @"Дневной рацион";
    [self.scroll setContentSize:CGSizeMake(320 * 3, 20)];
    
    dailyRationPage.sourceData = [self getQuestionsDailyRation];
    [dailyRationPage reloadData];
    
}

#pragma mark -



#pragma mark - scroll pages

-(IBAction)goHistoryPacient:(id)sender{
    [self setSecondPageAnalize];
    CGRect frame = self.scroll.frame;
    frame.origin.x = 320;
    frame.origin.y = 0;
    frame.size.height = 20;
    [self.scroll scrollRectToVisible:frame animated:YES];
}

-(IBAction)goDailyRation:(id)sender{
    [self setThirdPageAnalize];
    CGRect frame = self.scroll.frame;
    frame.origin.x = 320*2;
    frame.origin.y = 0;
    frame.size.height = 20;
    [self.scroll scrollRectToVisible:frame animated:YES];
}

-(IBAction)goResultPage:(id)sender{

}

#pragma mark -







-(void)setPageindicator:(int)index{
    
    for (UIImageView *imageView in self.page_indicator) {
        if ([imageView tag]==index) {
            imageView.image = [UIImage imageNamed:@"pageIndicator_enable"];
        }else{
            imageView.image = [UIImage imageNamed:@"pageIndicator_disable"];
        }
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
    [bButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *peopleButton = [[UIBarButtonItem alloc] initWithCustomView:bButton];
    
    
    UIImage *alarmImage = [UIImage imageNamed:@"alarm_icon"];
    UIButton *cButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cButton setImage:alarmImage forState:UIControlStateNormal];
    cButton.frame = CGRectMake(0.0,0.0,alarmImage.size.width+10,alarmImage.size.height);
    cButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    [cButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
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

@end

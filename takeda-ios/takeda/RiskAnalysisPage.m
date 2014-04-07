//
//  RiskAnalysisPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "RiskAnalysisPage.h"
#import "AnalizDataUserPage.h"
#import "analizData.h"

@interface RiskAnalysisPage (){
    UIActionSheet *picker_cover;
    UIPickerView *picker_view;
    NSArray *pickerDataSource;
}

@end

@implementation RiskAnalysisPage

@synthesize scroll;
@synthesize page_indicator;
@synthesize resultRiskAnalysis;
AnalizDataUserPage *selectedPage;
NSString *currentKey;
int selectedIndex = 0;

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
    firstPage.page = 1;
    firstPage.delegate = self;
    firstPage.sourceData = [[analizData sharedObject] getQuestionsDataUser];
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
    historyPacientPage.page = 2;
    historyPacientPage.delegate = self;
    historyPacientPage.sourceData = [[analizData sharedObject] getQuestionsHistoryUser];
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
    dailyRationPage.page = 3;
    dailyRationPage.delegate = self;
    dailyRationPage.sourceData = [[analizData sharedObject] getQuestionsDailyRation];
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
    /*
    [dic_data setObject:@"1" forKey:@"sex"]; // 0 - male, 1 - female
    [dic_data setObject:@"-" forKey:@"old"];
    [dic_data setObject:@"-" forKey:@"growth"];
    [dic_data setObject:@"-" forKey:@"weight"];
    [dic_data setObject:@"0" forKey:@"smoke"];
    [dic_data setObject:@"-" forKey:@"cholesterol"];
    [dic_data setObject:@"0" forKey:@"drags_cholesterol"];
    
    
    [dic_data setObject:@"0" forKey:@"diabet"];
    [dic_data setObject:@"0" forKey:@"higher_suger_blood"];
    [dic_data setObject:@"-" forKey:@"arterial_pressure"];
    [dic_data setObject:@"1" forKey:@"decrease_pressure_drags"];
    //[dic_data setObject:@"-" forKey:@"walking"];
    [dic_data setObject:@"-" forKey:@"sport"];
    [dic_data setObject:@"0" forKey:@"infarct"];
    
    
    [dic_data setObject:@"0" forKey:@"salt"];
    [dic_data setObject:@"1" forKey:@"accept_drags_risk_trombus"];
    
    */
    
    NSString *sex = @"male";
    if ([[[[UserData sharedObject] getUserData] objectForKey:@"sex"] boolValue]) {
        sex = @"female";
    }
    
    
    
    NSDictionary *params = @{@"acetylsalicylicDrugs": [[[analizData sharedObject] dicRiskData] objectForKey:@"accept_drags_risk_trombus"],
                             @"arterialPressure": [[[analizData sharedObject] dicRiskData] objectForKey:@"arterial_pressure"],
                             @"birthday": [[[UserData sharedObject] getUserData] objectForKey:@"birthday"],
                             @"cholesterolDrugs": [[[analizData sharedObject] dicRiskData] objectForKey:@"drags_cholesterol"],
                             @"cholesterolLevel":[[[analizData sharedObject] dicRiskData] objectForKey:@"cholesterol"],
                             @"diabetes":[[[analizData sharedObject] dicRiskData] objectForKey:@"diabet"],
                             @"extraSalt":[[[analizData sharedObject] dicRiskData] objectForKey:@"salt"],
                             @"growth":[[[analizData sharedObject] dicRiskData] objectForKey:@"growth"],
                             @"heartAttackOrStroke":[[[analizData sharedObject] dicRiskData] objectForKey:@"infarct"],
                             @"physicalActivity":[[[analizData sharedObject] dicRiskData] objectForKey:@"sport"],
                             @"sex":sex,
                             @"smoking":[[[analizData sharedObject] dicRiskData] objectForKey:@"smoke"],
                             @"weight":[[[analizData sharedObject] dicRiskData] objectForKey:@"weight"]
                             };
    
    
    
    
    
    
    [inetRequests sendAnalysisToServer:@{@"testResult": params} completion:^(BOOL result, NSError *error) {
        if (result) {
            if (!resultRiskAnalysis) {
                resultRiskAnalysis = [[ResultRiskAnalysis alloc] initWithNibName:@"ResultRiskAnalysis" bundle:nil];
            }
            [self.navigationController pushViewController:resultRiskAnalysis animated:YES];
        }else{
            [Helper fastAlert:@"Ошибка загрузки данных"];
        }
    }];
    
    
    

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



#pragma mark - AnalizDataUserPageDelegate

-(void)analizDataUserPage:(AnalizDataUserPage*)analizPage openList:(NSString*)type{
    currentKey = type;
    selectedPage = analizPage;
    
    SWITCH(type){
        CASE(@"old"){
            pickerDataSource = [[analizData sharedObject] getListYears];
            [self showPicker];
            break;
        }
        CASE(@"growth"){
            pickerDataSource = [[analizData sharedObject] getListGrowth];
            [self showPicker];
            break;
        }
        CASE(@"weight"){
            pickerDataSource = [[analizData sharedObject] getListWeight];
            [self showPicker];
            break;
        }
        CASE(@"cholesterol"){
            pickerDataSource = [[analizData sharedObject] getListCholesterol];
            [self showPicker];
            break;
        }
        CASE(@"arterial_pressure"){
            pickerDataSource = [[analizData sharedObject] getListArterial_pressure];
            [self showPicker];
            break;
        }
        CASE(@"walking"){
            pickerDataSource = [[analizData sharedObject] getListWalking];
            [self showPicker];
            break;
        }
        CASE(@"sport"){
            pickerDataSource = [[analizData sharedObject] getListSport];
            [self showPicker];
            break;
        }
        CASE(@"salt"){
            pickerDataSource = [[analizData sharedObject] getListSalt];
            [self showPicker];
            break;
        }
        DEFAULT{
            break;
        }
    }
    
    /*if ([type isEqualToString:@"old"]) {
        pickerDataSource = [[analizData sharedObject] getListYears];
        [self showPicker];
    }*/
}


#pragma mark - UIPickerView

-(IBAction)showPicker{
    picker_cover = nil;
    picker_cover = [[UIActionSheet alloc] initWithTitle:nil
                                               delegate:nil
                                      cancelButtonTitle:@""
                                 destructiveButtonTitle:nil
                                      otherButtonTitles:nil];
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    toolbar.frame = CGRectMake(0, 0, ScreenWidth, 44);
    NSMutableArray *items = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *button1 = [[UIBarButtonItem alloc] initWithTitle:@"Закрыть" style:UIBarButtonItemStyleDone target:self action:@selector(closePicker)];
    
    UIBarButtonItem *button2 = [[UIBarButtonItem alloc] initWithTitle:@"Применить" style:UIBarButtonItemStyleDone target:self action:@selector(applyPicker:)];
    
    UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    
    [items addObject:button1];
    [items addObject:flexibleSpaceLeft];
    [items addObject:button2];
    [toolbar setItems:items animated:NO];
    
    float picker_width = ScreenWidth;
    picker_view = [[UIPickerView alloc] initWithFrame:CGRectMake(0,40,picker_width,210)];
    picker_view.delegate = self;
    picker_view.dataSource = self;
    picker_view.showsSelectionIndicator = YES;
    [picker_cover addSubview:toolbar];
    [picker_cover addSubview:picker_view];
    picker_cover.backgroundColor = [UIColor whiteColor];
    [picker_cover showInView:self.view.superview];
    [picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
    picker_cover.tag = 1;
    
}

-(void)closePicker{
    [picker_cover dismissWithClickedButtonIndex:0 animated:YES];
    [selectedPage reloadData];
}


-(void)applyPicker:(id)sender{
    [picker_cover dismissWithClickedButtonIndex:0 animated:YES];
    
    [[[analizData sharedObject] dicRiskData] setObject:[pickerDataSource objectAtIndex:selectedIndex] forKey:currentKey];
    [selectedPage reloadData];
}



#pragma mark PickerView DataSource

- (NSInteger)numberOfComponentsInPickerView:
(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
numberOfRowsInComponent:(NSInteger)component
{
    return [pickerDataSource count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    return [pickerDataSource objectAtIndex:row];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component{
    selectedIndex = (int)row;
    //NSLog(@"index = %@ ",[pickerDataSource objectAtIndex:row]);
}






#pragma mark -

@end

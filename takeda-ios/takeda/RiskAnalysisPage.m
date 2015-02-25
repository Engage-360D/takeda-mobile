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
    CActionSheet *picker_cover;
    UIPickerView *picker_view;
    NSArray *pickerDataSource;
    UIDatePicker *date_picker;
    int currentIndex;
    AnalizDataUserPage *historyPacientPage;
    AnalizDataUserPage *firstPage;
    AnalizDataUserPage *dailyRationPage;
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
    [self setDefaultDateBirthday];
    [self setFirstPageAnalize];
    
    
    
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (![User checkForRole:tDoctor]){
        for (UIBarButtonItem *b in self.navigationItem.rightBarButtonItems){
            b.enabled = NO;
        }
        self.navigationItem.leftBarButtonItem.enabled = NO;
    }
}


-(void)setDefaultDateBirthday{
    if (User.userData[@"birthday"]) {
        
        NSDate *curDate = [Global parseDateTime: User.userData[@"birthday"]];
        if (curDate) {

            [[[analizData sharedObject] dicRiskData] setObject:User.userData[@"birthday"] forKey:@"birthday"];
            
            NSDateComponents* agecalcul = [[NSCalendar currentCalendar]
                                           components:NSYearCalendarUnit
                                           fromDate:curDate
                                           toDate:[NSDate date]
                                           options:0];
            //show the age as integer
            NSInteger age = [agecalcul year];
            [[[analizData sharedObject] dicRiskData] setObject:[NSString stringWithFormat:@"%i",(int)age] forKey:@"old"];

        }
        
    }

}


-(void)backAction{
    switch (currentIndex) {
        case 2:{
            [self setFirstPageAnalize];
            [self.scroll scrollRectToVisible:firstPage.view.frame animated:YES];

            break;
        }
            
        case 3:{
            [self setSecondPageAnalize];
            [self.scroll scrollRectToVisible:historyPacientPage.view.frame animated:YES];

            break;
        }
            
        case 4:{
            [self setThirdPageAnalize];
            [self.scroll scrollRectToVisible:dailyRationPage.view.frame animated:YES];
            break;
        }
            
    }
}


#pragma mark - init pages

-(void)setFirstPageAnalize{
    [self setPageindicator:1];
    if (!firstPage){
        firstPage = [[AnalizDataUserPage alloc] initWithNibName:@"AnalizDataUserPage" bundle:nil];
        firstPage.view.frame = self.scroll.bounds;
        [self addChildViewController:firstPage];
        [self.scroll addSubview:firstPage.view];
        [firstPage willMoveToParentViewController:self];
        [firstPage.nextStepBtn addTarget:self action:@selector(goHistoryPacient:) forControlEvents:UIControlEventTouchDown];
        [firstPage.nextStepBtn setTitle:@"История пациента" forState:UIControlStateNormal];
        firstPage.vcSubTitle.text = @"Данные пациента";
        [self.scroll setContentSize:CGSizeMake(320 * 1, 20)];
    }
    firstPage.page = 1;
    firstPage.delegate = self;
    firstPage.sourceData = [[analizData sharedObject] getQuestionsDataUser];
    [firstPage reloadData];
    
}

-(void)setSecondPageAnalize{
    [self setPageindicator:2];
    if (!historyPacientPage){
        historyPacientPage = [[AnalizDataUserPage alloc] initWithNibName:@"AnalizDataUserPage" bundle:nil];
        historyPacientPage.view.frame = self.scroll.bounds;
        historyPacientPage.view.x = self.scroll.width;
        [self addChildViewController:historyPacientPage];
        [self.scroll addSubview:historyPacientPage.view];
        [historyPacientPage willMoveToParentViewController:self];
        [historyPacientPage.nextStepBtn addTarget:self action:@selector(goDailyRation:) forControlEvents:UIControlEventTouchDown];
        [historyPacientPage.nextStepBtn setTitle:@"Дневной рацион" forState:UIControlStateNormal];
        historyPacientPage.vcSubTitle.text = @"История пациента";
        [self.scroll setContentSize:CGSizeMake(320 * 2, 20)];
    }
    historyPacientPage.page = 2;
    historyPacientPage.delegate = self;
    historyPacientPage.sourceData = [[analizData sharedObject] getQuestionsHistoryUser];
    [historyPacientPage reloadData];
}


-(void)setThirdPageAnalize{
    [self setPageindicator:3];
    if (!dailyRationPage){
        dailyRationPage = [[AnalizDataUserPage alloc] initWithNibName:@"AnalizDataUserPage" bundle:nil];
        dailyRationPage.view.frame = self.scroll.bounds;
        dailyRationPage.view.x = self.scroll.width*2;
        [self addChildViewController:dailyRationPage];
        [self.scroll addSubview:dailyRationPage.view];
        [dailyRationPage willMoveToParentViewController:self];
        [dailyRationPage.nextStepBtn addTarget:self action:@selector(goResultPage:) forControlEvents:UIControlEventTouchDown];
        [dailyRationPage.nextStepBtn setTitle:@"Результаты" forState:UIControlStateNormal];
        dailyRationPage.vcSubTitle.text = @"Дневной рацион";
    }
    [self.scroll setContentSize:CGSizeMake(320 * 3, 20)];
    dailyRationPage.page = 3;
    dailyRationPage.delegate = self;
    dailyRationPage.sourceData = [[analizData sharedObject] getQuestionsDailyRation];
    [dailyRationPage reloadData];
}

#pragma mark -




-(BOOL)checkHistoryPicientData{
    if ([[[[analizData sharedObject] dicRiskData] objectForKey:@"old"] isEqualToString:@"-"]) {
        return NO;
    }
    if ([[[[analizData sharedObject] dicRiskData] objectForKey:@"growth"] isEqualToString:@"-"]) {
        return NO;
    }
    if ([[[[analizData sharedObject] dicRiskData] objectForKey:@"weight"] isEqualToString:@"-"]) {
        return NO;
    }
    if ([[[[analizData sharedObject] dicRiskData] objectForKey:@"cholesterol"] isEqualToString:@"-"]) {
        return NO;
    }
    return YES;
}

-(BOOL)checkDailyRationData{
    if ([[[[analizData sharedObject] dicRiskData] objectForKey:@"arterial_pressure"] isEqualToString:@"-"]) {
        return NO;
    }
    if ([[[[analizData sharedObject] dicRiskData] objectForKey:@"sport"] isEqualToString:@"-"]) {
        return NO;
    }
    return YES;
}




#pragma mark - scroll pages

-(IBAction)goHistoryPacient:(id)sender{
    if (![self checkHistoryPicientData]) {
        [Helper fastAlert:@"Необходимо заполнить все поля"];
        return;
    }
    
    [self setSecondPageAnalize];
    CGRect frame = self.scroll.frame;
    frame.origin.x = 320;
    frame.origin.y = 0;
    frame.size.height = 20;
    [self.scroll scrollRectToVisible:frame animated:YES];
}

-(IBAction)goDailyRation:(id)sender{
    if (![self checkDailyRationData]) {
        [Helper fastAlert:@"Необходимо заполнить все поля"];
        return;
    }
    
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
    if ([[[[UserData sharedObject] userData] objectForKey:@"sex"] boolValue]) {
        sex = @"female";
    }
    
    
    
    NSDictionary *params = @{@"sex":sex,
                             @"birthday": [[[analizData sharedObject] dicRiskData] objectForKey:@"birthday"],
                             @"growth":[NSNumber numberWithInt:[[[[analizData sharedObject] dicRiskData] objectForKey:@"growth"] intValue]],
                             @"weight":[NSNumber numberWithInt:[[[[analizData sharedObject] dicRiskData] objectForKey:@"weight"] intValue]],
                             @"isSmoker":[NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"smoke"] boolValue]],
                             @"cholesterolLevel":[NSNumber numberWithFloat:[[[[analizData sharedObject] dicRiskData] objectForKey:@"cholesterol"]floatValue]],
                             @"isCholesterolDrugsConsumer":[[[analizData sharedObject] dicRiskData] objectForKey:@"drags_cholesterol"],
                             @"hasDiabetes": [NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"diabet"] boolValue]],
                             @"hadSugarProblems":[[[analizData sharedObject] dicRiskData] objectForKey:@"higher_suger_blood"],
                             @"isSugarDrugsConsumer":[[[analizData sharedObject] dicRiskData] objectForKey:@"accept_drags_suger"],
                             @"arterialPressure": [NSNumber numberWithInt:[[[[analizData sharedObject] dicRiskData] objectForKey:@"arterial_pressure"] intValue]],
                             @"isArterialPressureDrugsConsumer": [[[analizData sharedObject] dicRiskData] objectForKey:@"decrease_pressure_drags"],
                             @"physicalActivityMinutes":[NSNumber numberWithInt:[[[[analizData sharedObject] dicRiskData] objectForKey:@"sport"] intValue]],
                             @"hadHeartAttackOrStroke":[NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"infarct"]boolValue]],
                             @"isAddingExtraSalt":[NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"salt"]boolValue]],
                             @"isAcetylsalicylicDrugsConsumer": [NSNumber numberWithBool:[[[[analizData sharedObject] dicRiskData] objectForKey:@"accept_drags_risk_trombus"]boolValue]]
                             };
    
    
    
    
    [ServData sendAnalysisToServer:params completion:^(BOOL success, NSError *error, id result) {
        
        
        
        if (success) {
            [GlobalData saveResultAnalyses:result[@"data"]];
            
            if (!resultRiskAnalysis) {
                resultRiskAnalysis = [ResultRiskAnal new];
            }
            resultRiskAnalysis.needUpdate = YES;
            [self.navigationController pushViewController:resultRiskAnalysis animated:YES];
        } else {
            if ([result hasKey:@"errors"]){
                
                    
                    
            }
            
            
            
            [Helper fastAlert:@"Тест уже пройден"];
        }
    }];
    
    
    

}

#pragma mark -




-(void)setPageindicator:(int)index{
    currentIndex = index;
    if (index>1){
        self.navigationItem.leftBarButtonItem = [self backBtn];
    } else {
        self.navigationItem.leftBarButtonItem = [self menuButton];
    }
    
    for (UIImageView *imageView in self.page_indicator) {
        if ([imageView tag]==index) {
            imageView.image = [UIImage imageNamed:@"pageIndicator_enable"];
        }else{
            imageView.image = [UIImage imageNamed:@"pageIndicator_disable"];
        }
    }
}


#pragma mark - AnalizDataUserPageDelegate

-(void)analizDataUserPage:(AnalizDataUserPage*)analizPage openList:(NSString*)type{
    currentKey = type;
    selectedPage = analizPage;
    
    SWITCH(type){
        CASE(@"old"){
            pickerDataSource = [[analizData sharedObject] getListYears];
            [self showTimePicker];
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

//-(IBAction)showPicker{
//    picker_cover = nil;
//    picker_cover = [[UIActionSheet alloc] initWithTitle:nil
//                                               delegate:nil
//                                      cancelButtonTitle:@""
//                                 destructiveButtonTitle:nil
//                                      otherButtonTitles:nil];
//    UIToolbar *toolbar = [[UIToolbar alloc] init];
//    toolbar.frame = CGRectMake(0, 0, ScreenWidth, 44);
//    NSMutableArray *items = [[NSMutableArray alloc] init];
//    
//    UIBarButtonItem *button1 = [[UIBarButtonItem alloc] initWithTitle:@"Закрыть" style:UIBarButtonItemStyleDone target:self action:@selector(closePicker)];
//    
//    UIBarButtonItem *button2 = [[UIBarButtonItem alloc] initWithTitle:@"Применить" style:UIBarButtonItemStyleDone target:self action:@selector(applyPicker:)];
//    
//    UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
//    
//    [items addObject:button1];
//    [items addObject:flexibleSpaceLeft];
//    [items addObject:button2];
//    [toolbar setItems:items animated:NO];
//    
//    float picker_width = ScreenWidth;
//    picker_view = [[UIPickerView alloc] initWithFrame:CGRectMake(0,40,picker_width,210)];
//    picker_view.delegate = self;
//    picker_view.dataSource = self;
//    picker_view.showsSelectionIndicator = YES;
//    [picker_cover addSubview:toolbar];
//    [picker_cover addSubview:picker_view];
//    picker_cover.backgroundColor = [UIColor whiteColor];
//    [picker_cover showInView:self.view.superview];
//    [picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
//    picker_cover.tag = 1;
//    selectedIndex = 0;
//    
//    float value = [[[[analizData sharedObject] dicRiskData] valueForKey:currentKey] floatValue];
//    for (int i = 0; i<pickerDataSource.count; i++){
//        if (value == [pickerDataSource[i] floatValue]){
//            selectedIndex = i;
//            break;
//        }
//    }
//    
//    picker_view.backgroundColor = [UIColor greenColor];
//    [picker_view selectRow:selectedIndex inComponent:0 animated:NO];
//    
//
//    
//}

-(IBAction)showPicker{
    picker_cover = nil;

    picker_cover = [[CActionSheet alloc] initInView:self.view.superview];

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

    //[picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
    picker_cover.tag = 1;
    selectedIndex = 0;
    
    float value = [[[[analizData sharedObject] dicRiskData] valueForKey:currentKey] floatValue];
    for (int i = 0; i<pickerDataSource.count; i++){
        if (value == [pickerDataSource[i] floatValue]){
            selectedIndex = i;
            break;
        }
    }

    [picker_view selectRow:selectedIndex inComponent:0 animated:NO];
    
    [picker_cover show];
    
}


-(void)closePicker{
    [picker_cover dissmiss];
    [selectedPage reloadData];
}


-(void)applyPicker:(id)sender{
    [picker_cover dissmiss];
    if ((int)[picker_cover tag]==1) {
        [[[analizData sharedObject] dicRiskData] setObject:[pickerDataSource objectAtIndex:selectedIndex] forKey:currentKey];
    }else{
        
        if ((int)[picker_cover tag]==2) {
            NSDate *myDate = date_picker.date;
//            NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
//            [dateFormat setDateFormat:@"yyyy-MMMM-dd"];
//            
//            NSString *prettyVersion = [dateFormat stringFromDate:myDate];
            [[[analizData sharedObject] dicRiskData] setObject:[Global strDateTime:myDate] forKey:@"birthday"];
            
            NSDateComponents* agecalcul = [[NSCalendar currentCalendar]
                                           components:NSYearCalendarUnit
                                           fromDate:myDate
                                           toDate:[NSDate date]
                                           options:0];
            //show the age as integer
            NSInteger age = [agecalcul year];
            [[[analizData sharedObject] dicRiskData] setObject:[NSString stringWithFormat:@"%i",(int)age] forKey:currentKey];
            
        }
        
        
        //[[[analizData sharedObject] dicRiskData] setObject:[pickerDataSource objectAtIndex:selectedIndex] forKey:currentKey];
    }

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



#pragma mark - UIDatePicker

-(IBAction)showTimePicker{
    picker_cover = nil;
    picker_cover = [[CActionSheet alloc] initInView:self.view.superview];
    
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
    
    date_picker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0,40,picker_width,210)];
    date_picker.datePickerMode = UIDatePickerModeDate;
    //[date_picker addTarget:self action:@selector(changeDate) forControlEvents:UIControlEventValueChanged];
    
    if (User.userData[@"birthday"]) {
        
        NSDate *curDate = [Global parseDateTime:User.userData[@"birthday"]];

        if (curDate) {
            [date_picker setDate:curDate];
        }else{
            [date_picker setDate:[Helper getAgoYear:25 fromDate:[NSDate date]]];
        }

    }
    
    [picker_cover addSubview:toolbar];
    [picker_cover addSubview:date_picker];
    picker_cover.backgroundColor = [UIColor whiteColor];
    picker_cover.tag = 2;
    [picker_cover show];
}


#pragma mark -


@end

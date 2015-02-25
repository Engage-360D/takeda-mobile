//
//  AddPills.m
//  takeda
//
//  Created by Alexander Rudenko on 17.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "AddPills.h"

@interface AddPills (){
    BOOL filled;
}

@end

@implementation AddPills
@synthesize drug;


- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (!drug) drug = [NSMutableDictionary new];
    [self showInfo];
    
    for (UIButton *btn in self.incBtns){
        btn.userInteractionEnabled = !self.readOnly;
    }
    
    for (PLTextField *tf in self.textFields){
        tf.userInteractionEnabled = !self.readOnly;
    }

    for (UIImageView *iv in self.arrows){
        iv.hidden = self.readOnly;
    }
    
    self.navigationItem.rightBarButtonItems = nil;
    
    if (!self.readOnly){
        self.navigationItem.rightBarButtonItem = [self menuBarBtnWithTitle:@"Готово" selector:@selector(save) forTarget:self];

    }
    
}

-(void)setupInterface{

    for (UIButton *btn in self.incBtns){
        btn.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
        [btn setTitleColor:RGB(54, 65, 71) forState:UIControlStateNormal];
    }
    for (UILabel *lb in self.labels){
        lb.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
        lb.textColor = RGB(54, 65, 71);
    }
    for (PLTextField *tf in self.textFields){
        tf.placeholderColor = RGB(53, 65, 71);
        tf.placeholderFont = [UIFont fontWithName:@"SegoeWP-Light" size:14.0];
        tf.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
        tf.textColor = RGB(54, 65, 71);
    }
    
    
    [self.scrollView setup_autosize];
}

-(void)save{

    if (!filled){
        [self showMessage:@"Заполните все поля" title:@"Ошибка"];
        return;
    }
    
    [ServData addDrug:drug completion:^(BOOL success, NSError *error, id result){
        if (success){
            [self showMessage:@"Лекарство успешно добавлено" title:@"Успех"];
        } else {
            [self showMessage:@"Не получилось добавить лекарство" title:@"Ошибка"];
        }
    }];
}


-(void)showInfo{
    

    
    filled = YES;
    
    if (drug[@"name"]){
        _drugName.text = drug[@"name"];
    } else {
        filled = NO;
        _drugName.text = @"";
    }
    
    if ([[drug[@"quantity"] stringValue] length]>0){
        _quantityLabel.text = [drug[@"quantity"] stringValue];
    } else {
        filled = NO;
        _quantityLabel.text = @"";
    }
    
    if ([[Global dictionaryWithValue:drug[@"repeat"] ForKey:@"name" InArray:[AddPills expArray]][@"title"] length]>0){
        _repeatLabel.text = [Global dictionaryWithValue:drug[@"repeat"] ForKey:@"name" InArray:[AddPills expArray]][@"title"];
    } else {
        filled = NO;
        _repeatLabel.text = @"";
    }
    
    if ([drug[@"time"] length]>0){
        _timeLabel.text = [[Global parseTime:drug[@"time"]] stringWithFormat:@"HH:mm"];
    } else {
        filled = NO;
        _timeLabel.text = @"";
    }
    
    if ([drug[@"sinceDate"] length]>0){
        _sinceDateLabel.text = [[Global parseDateTime:drug[@"sinceDate"]] stringWithFormat:@"dd.MM.yyyy"];
    } else {
        filled = NO;
        _sinceDateLabel.text = @"";
    }
    
    if ([drug[@"tillDate"] length]>0){
        _tillDateLabel.text = [[Global parseDateTime:drug[@"tillDate"]] stringWithFormat:@"dd.MM.yyyy"];
    } else {
        filled = NO;
        _tillDateLabel.text = @"";
    }

}

#pragma mark actions

-(IBAction)saveName:(id)sender{
    [drug setObject:_drugName.text forKey:@"name"];
    [self showInfo];
}

-(IBAction)setupQuantity:(id)sender{
    NSMutableArray *expArray = [NSMutableArray new];
    [expArray fillIntegerFrom:1 to:50 step:1];
    DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.view completition:^(BOOL apply, int index){
        if (apply){
            [drug setObject:[NSNumber numberWithInt:[expArray[index] intValue]] forKey:@"quantity"];
            [self showInfo];
        }
    }];
    [exPicker preselectValue:drug[@"quantity"]];
    [exPicker show];
}


-(IBAction)setupRepeat:(UIButton*)sender{

    DPicker *exPicker = [[DPicker alloc] initListWithArray:[[AddPills expArray] valueForKey:@"title"] inView:self.view completition:^(BOOL apply, int index){
        if (apply){
            [drug setObject:[AddPills expArray][index][@"name"] forKey:@"repeat"];
            [self showInfo];
        }
    }];
    [exPicker preselectValue: [Global dictionaryWithValue:drug[@"repeat"] ForKey:@"name" InArray:[AddPills expArray]][@"title"]];
    [exPicker show];

}

-(IBAction)setupTime:(UIButton*)sender{
    DPicker *exPicker = [[DPicker alloc] initDateinView:self.view completition:^(BOOL apply, NSDate *selDate){
        if (apply){
            [drug setObject:[selDate stringWithFormat:@"HH:mm:00"] forKey:@"time"];
            [self showInfo];
        }
    }];
    exPicker.date_picker.datePickerMode = UIDatePickerModeTime;
    [exPicker.date_picker setLocale:[[NSLocale alloc] initWithLocaleIdentifier:@"ru_RU"]];
    [exPicker preselectValue:[Global parseTime:drug[@"time"]]];
    [exPicker show];
    
}

-(IBAction)setupSinceDate:(UIButton*)sender{
    DPicker *exPicker = [[DPicker alloc] initDateinView:self.view completition:^(BOOL apply, NSDate *selDate){
        if (apply){
           [drug setObject:[Global strDateTime:selDate] forKey:@"sinceDate"];
            [self showInfo];
        }
    }];
    exPicker.date_picker.minimumDate = [NSDate date];
    [exPicker preselectValue:[Global parseDateTime:drug[@"sinceDate"]]];
    [exPicker show];
}

-(IBAction)setupTillDate:(UIButton*)sender{
    DPicker *exPicker = [[DPicker alloc] initDateinView:self.view completition:^(BOOL apply, NSDate *selDate){
        if (apply){
            [drug setObject:[Global strDateTime:selDate] forKey:@"tillDate"];
            [self showInfo];
        }
    }];
    exPicker.date_picker.minimumDate = [Global parseDateTime:drug[@"sinceDate"]];
    [exPicker preselectValue:[Global parseDateTime:drug[@"tillDate"]]];
    [exPicker show];
}

-(IBAction)deleteDrug:(UIButton*)sender{
    
}

/*
-(IBAction)selectExperience:(id)sender{
    NSMutableArray *expArray = [NSMutableArray new];
    //    for (float i = 0; i<50; i+=1){
    //        [expArray addObject:[NSNumber numberWithFloat:i]];
    //    }
    [expArray fillIntegerFrom:0 to:50 step:1];
    DPicker *exPicker = [[DPicker alloc] initListWithArray:expArray inView:self.parentVC.view completition:^(BOOL apply, int index){
        if (apply){
            self.experience = [expArray[index] floatValue];
        }
    }];
    [exPicker preselectValue:[NSNumber numberWithFloat:self.experience]];
    [exPicker show];
}

-(IBAction)changeDateBirthday:(id)sender{
    DPicker *exPicker = [[DPicker alloc] initDateinView:self.parentVC.view completition:^(BOOL apply, NSDate *selDate){
        if (apply){
            self.birthdayDate = selDate;
        }
        
    }];
    
    [exPicker preselectValue:_birthdayDate];
    [exPicker show];
    
}

*/

+(NSArray*)expArray{
    return @[@{@"title":@"Ежедневно", @"name":@"DAILY"},@{@"title":@"Каждые два дня", @"name":@"EVERY_2_DAYS"}];
}


@end

//
//  analizData.m
//  takeda
//
//  Created by Serg on 4/6/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "analizData.h"

@implementation analizData
static analizData *sharedInstance;
NSMutableDictionary * dic_data;

+(analizData*)sharedObject{
    @synchronized(self){
        if (!sharedInstance) {
            sharedInstance = [analizData new];
            [sharedInstance setDefault_data_user_analize];
        }
        return sharedInstance;
    }
    
}

-(void)setDefault_data_user_analize{
    if (!dic_data) {
        dic_data = [[NSMutableDictionary alloc] init];
    }
    [dic_data setObject:@"0" forKey:@"sex"]; // 0 - male, 1 - female
    [dic_data setObject:@"-" forKey:@"old"];
    [dic_data setObject:@"" forKey:@"birthday"];
    [dic_data setObject:@"-" forKey:@"growth"];
    [dic_data setObject:@"-" forKey:@"weight"];
    [dic_data setObject:@"0" forKey:@"smoke"];
    [dic_data setObject:@"-" forKey:@"cholesterol"];
    [dic_data setObject:@"0" forKey:@"drags_cholesterol"];
    
    
    [dic_data setObject:@"0" forKey:@"diabet"];
    [dic_data setObject:@"0" forKey:@"higher_suger_blood"];
    [dic_data setObject:@"-" forKey:@"arterial_pressure"];
    [dic_data setObject:@"0" forKey:@"decrease_pressure_drags"];
    //[dic_data setObject:@"-" forKey:@"walking"];
    [dic_data setObject:@"-" forKey:@"sport"];
    [dic_data setObject:@"0" forKey:@"infarct"];
    
    
    [dic_data setObject:@"0" forKey:@"salt"];
    [dic_data setObject:@"0" forKey:@"accept_drags_risk_trombus"];
    
    
}


-(NSMutableDictionary*)dicRiskData{
    return dic_data;
}





-(NSArray*)getQuestionsDataUser{
    return @[
             @{@"object":@"sex", @"id":@"1",@"type": @"1",@"name":@"Пол",@"f_param":@"Мужчина",@"s_param":@"Женщина",@"value":[dic_data objectForKey:@"sex"]},
             @{@"object":@"old", @"id":@"2",@"type": @"2",@"name":@"Возраст",@"description":@"лет",@"value":[dic_data objectForKey:@"old"]},
             @{@"object":@"growth", @"id":@"3",@"type": @"2",@"name":@"Рост",@"description":@"см",@"value":[dic_data objectForKey:@"growth"]},
             @{@"object":@"weight", @"id":@"4",@"type": @"2",@"name":@"Вес",@"description":@"кг",@"value":[dic_data objectForKey:@"weight"]},
             @{@"object":@"smoke", @"id":@"5",@"type": @"3",@"name":@"Вы курите?",@"value":[dic_data objectForKey:@"smoke"]},
             @{@"object":@"cholesterol", @"id":@"6",@"type": @"2",@"name":@"Уровень общего холестирина",@"description":@"мг/дл",@"value":[dic_data objectForKey:@"cholesterol"]},
             @{@"object":@"drags_cholesterol", @"id":@"7",@"type": @"3",@"name":@"Лекарства для снижения уровня холестерина",@"value":[dic_data objectForKey:@"drags_cholesterol"]}
             ];
}

-(NSArray*)getQuestionsHistoryUser{
    return @[
             @{@"object":@"diabet", @"id":@"8",@"type": @"3",@"name":@"Страдаете ли вы диабетом?",@"value":[dic_data objectForKey:@"diabet"]},
             @{@"object":@"higher_suger_blood", @"id":@"9",@"type": @"3",@"name":@"Отмечалось ли повышение уровня сахара в крови?",@"value":[dic_data objectForKey:@"higher_suger_blood"]},
             @{@"object":@"arterial_pressure", @"id":@"10",@"type": @"2",@"name":@"Артериальное давнление",@"description":@"мм",@"value":[dic_data objectForKey:@"arterial_pressure"]},
             @{@"object":@"decrease_pressure_drags", @"id":@"11",@"type": @"3",@"name":@"Принимаете ли препараты для понижения давления?",@"value":[dic_data objectForKey:@"decrease_pressure_drags"]},
             //@{@"object":@"walking", @"id":@"12",@"type": @"2",@"name":@"Ходьба",@"description":@"мин/нед",@"value":[dic_data objectForKey:@"walking"]},
             @{@"object":@"sport", @"id":@"13",@"type": @"2",@"fractions":@"1",@"name":@"Физическая активность",@"description":@"минут/неделя",@"value":[dic_data objectForKey:@"sport"]},
             @{@"object":@"infarct", @"id":@"14",@"type": @"3",@"name":@"Инфаркт/инсульт",@"value":[dic_data objectForKey:@"infarct"]},
             ];
}

-(NSArray*)getQuestionsDailyRation{
    return @[
             //@{@"object":@"salt", @"id":@"15",@"type": @"2",@"name":@"Досаливаете ли вы пищу?",@"description":@"грамм/день",@"value":[dic_data objectForKey:@"salt"]},
             @{@"object":@"salt", @"id":@"15",@"type": @"3",@"name":@"Досаливаете ли вы пищу?",@"value":[dic_data objectForKey:@"salt"]},
             
             @{@"object":@"accept_drags_risk_trombus", @"id":@"16",@"type": @"3",@"name":@"Принимает ли препараты на основе ацетилсалициловой кислоты для профилактики риска тромбозов?",@"value":[dic_data objectForKey:@"accept_drags_risk_trombus"]},
             ];
}




-(NSArray*)getListYears{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 1; i < 100; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListGrowth{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 30; i < 300; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListWeight{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 30; i < 700; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListCholesterol{
    
    float delta = 0.5;
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (float i = 3.0; i < 9.5; i=i+delta ) {
        [array addObject:[NSString stringWithFormat:@"%.1f",i]];
    }
    return array;
}


-(NSArray*)getListArterial_pressure{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 80; i < 200; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}

-(NSArray*)getListWalking{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 50; i < 220; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}

-(NSArray*)getListSport{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 80; i < 200; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListSalt{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 50; i < 220; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


@end
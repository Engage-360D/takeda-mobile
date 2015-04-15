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

+(void)resetData{
    sharedInstance = nil;
}

-(void)setDefault_data_user_analize{
    if (!dic_data) {
        dic_data = [[NSMutableDictionary alloc] init];
    }
//    [dic_data setObject:[[[UserData sharedObject] userData] objectForKey:@"sex"] forKey:@"sex"]; // 0 - male, 1 - female
    [dic_data setObject:[self defValueFor:@"sex" standart:@"0"] forKey:@"sex"]; // 0 - male, 1 - female

    [dic_data setObject:@"-" forKey:@"old"];
    [dic_data setObject:@"" forKey:@"birthday"];
    [dic_data setObject:[self defValueFor:@"growth" standart:@"170"] forKey:@"growth"];
    [dic_data setObject:[self defValueFor:@"weight" standart:@"70"] forKey:@"weight"];
    [dic_data setObject:[self defValueFor:@"smoke" standart:@"0"] forKey:@"smoke"];
    [dic_data setObject:@"-" forKey:@"cholesterol"];
    [dic_data setObject:@"0" forKey:@"drags_cholesterol"];
    
    
    [dic_data setObject:@"0" forKey:@"diabet"];
    [dic_data setObject:@"0" forKey:@"higher_suger_blood"];
    [dic_data setObject:@"0" forKey:@"accept_drags_suger"];
    
    [dic_data setObject:@"-" forKey:@"arterial_pressure"];
    [dic_data setObject:@"0" forKey:@"decrease_pressure_drags"];
    //[dic_data setObject:@"-" forKey:@"walking"];
    [dic_data setObject:@"-" forKey:@"sport"];
    [dic_data setObject:@"0" forKey:@"infarct"];
    
    
    [dic_data setObject:@"-" forKey:@"salt"];
    [dic_data setObject:@"0" forKey:@"accept_drags_risk_trombus"];
    
    [self setDefaultDateBirthday];
}

-(void)setDefaultDateBirthday{
    if (User.userData[@"birthday"]) {
        
        NSDate *curDate = [Global parseDateTime: User.userData[@"birthday"]];
        if (curDate) {
            
            [dic_data setObject:User.userData[@"birthday"] forKey:@"birthday"];
            
            NSDateComponents* agecalcul = [[NSCalendar currentCalendar]
                                           components:NSYearCalendarUnit
                                           fromDate:curDate
                                           toDate:[NSDate date]
                                           options:0];
            //show the age as integer
            NSInteger age = [agecalcul year];
            [dic_data setObject:[NSString stringWithFormat:@"%i",(int)age] forKey:@"old"];
            
        }
        
    }
    
}


-(id)defValueFor:(NSString*)key standart:(NSString*)standartValue{
    
    if ([UserDefaults objectForKey:aKey(key)]){
        return [UserDefaults objectForKey:aKey(key)];
    }

    return standartValue;
}

-(void)saveValue:(id)value forAnalizKey:(NSString*)key{
    if (value!=nil&&![User checkForRole:tDoctor]){
        [UserDefaults setObject:value forKey:aKey(key)];
    }
}

-(NSMutableDictionary*)dicRiskData{
    return dic_data;
}

-(id) recursiveMutable:(id)object
{
    if([object isKindOfClass:[NSDictionary class]])
    {
        NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithDictionary:object];
        for(NSString* key in [dict allKeys])
        {
            [dict setObject:[self recursiveMutable:[dict objectForKey:key]] forKey:key];
        }
        return dict;
    }
    else if([object isKindOfClass:[NSArray class]])
    {
        NSMutableArray* array = [NSMutableArray arrayWithArray:object];
        for(int i=0;i<[array count];i++)
        {
            [array replaceObjectAtIndex:i withObject:[self recursiveMutable:[array objectAtIndex:i]]];
        }
        return array;
    }
    else if([object isKindOfClass:[NSString class]])
        return [NSMutableString stringWithString:object];
    return object;
}


-(NSArray*)getQuestionsDataUser{
    return @[
             @{@"object":@"sex", @"id":@"1",@"type": @"1",@"name":@"Пол",@"f_param":@"Мужчина",@"s_param":@"Женщина",@"value":[dic_data objectForKey:@"sex"]},
             @{@"object":@"old", @"id":@"2",@"type": @"2",@"name":@"Возраст",@"description":@"лет",@"value":[dic_data objectForKey:@"old"]},
             @{@"object":@"growth", @"id":@"3",@"type": @"2",@"name":@"Рост",@"description":@"см",@"value":[dic_data objectForKey:@"growth"]},
             @{@"object":@"weight", @"id":@"4",@"type": @"2",@"name":@"Вес",@"description":@"кг",@"value":[dic_data objectForKey:@"weight"]},
             @{@"object":@"smoke", @"id":@"5",@"type": @"3",@"name":@"Курение",@"value":[dic_data objectForKey:@"smoke"]},
             @{@"object":@"cholesterol", @"id":@"6",@"type": @"2",@"fractions":@"1",@"name":@"Уровень общего холестерина",@"description":@"ммоль/литр",@"value":[dic_data objectForKey:@"cholesterol"]},
             @{@"object":@"drags_cholesterol", @"id":@"7",@"type": @"3",@"name":@"Лекарства для снижения уровня холестерина",@"value":[dic_data objectForKey:@"drags_cholesterol"]}
             ];
}

-(NSArray*)getQuestionsHistoryUser{
    return @[
             @{@"object":@"diabet", @"id":@"8",@"type": @"3",@"name":@"Диабет",@"value":[dic_data objectForKey:@"diabet"]},
             
             @{@"object":@"higher_suger_blood", @"id":@"9",@"type": @"3",@"name":@"Повышенный уровень сахара в крови",@"value":[dic_data objectForKey:@"higher_suger_blood"]},
             
            @{@"object":@"accept_drags_suger", @"id":@"10",@"type": @"3",@"name":@"Препараты для снижения уровня сахара в крови",@"value":[dic_data objectForKey:@"accept_drags_suger"]},
             
             @{@"object":@"arterial_pressure", @"id":@"11",@"type": @"2",@"name":@"Артериальное давление",@"description":@"мм",@"value":[dic_data objectForKey:@"arterial_pressure"]},
             
             @{@"object":@"decrease_pressure_drags", @"id":@"12",@"type": @"3",@"name":@"Препараты для понижения давления",@"value":[dic_data objectForKey:@"decrease_pressure_drags"]},
             //@{@"object":@"walking", @"id":@"12",@"type": @"2",@"name":@"Ходьба",@"description":@"мин/нед",@"value":[dic_data objectForKey:@"walking"]},
             @{@"object":@"sport", @"id":@"13",@"type": @"2",@"fractions":@"1",@"name":@"Физическая активность",@"description":@"минут/неделя",@"value":[dic_data objectForKey:@"sport"]},
             @{@"object":@"infarct", @"id":@"14",@"type": @"3",@"name":@"Инфаркт/инсульт",@"value":[dic_data objectForKey:@"infarct"]},
             ];
}

-(NSArray*)getQuestionsDailyRation{
    return @[
            // @{@"object":@"salt", @"id":@"15",@"type": @"2",@"fractions":@"1",@"name":@"Соль",@"description":@"грамм/день",@"value":[dic_data objectForKey:@"salt"]},
             @{@"object":@"salt", @"id":@"15",@"type": @"3",@"name":@"Досаливаете ли вы пищу?",@"value":[dic_data objectForKey:@"salt"]},
             
             @{@"object":@"accept_drags_risk_trombus", @"id":@"16",@"type": @"3",@"name":@"Принимает ли препараты на основе ацетилсалициловой кислоты для профилактики риска тромбозов?",@"value":[dic_data objectForKey:@"accept_drags_risk_trombus"]},
             ];
}




-(NSArray*)getListYears{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 1; i <= 100; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListGrowth{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 30; i <= 300; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListWeight{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 30; i <= 700; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListCholesterol{
    
    float delta = 0.5;
    NSMutableArray *array = [[NSMutableArray alloc] init];
    [array addObject:@"-"];
    for (float i = 3.0; i < 9.5; i=i+delta ) {
        [array addObject:[NSString stringWithFormat:@"%.1f",i]];
    }
    return array;
}


-(NSArray*)getListArterial_pressure{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 80; i <= 200; i ++) {
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
    for (int i = 80; i <= 200; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


-(NSArray*)getListSalt{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i = 0; i < 50; i ++) {
        [array addObject:[NSString stringWithFormat:@"%i",i]];
    }
    return array;
}


@end

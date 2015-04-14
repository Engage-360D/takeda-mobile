//
//  NotifPlanner.m
//  takeda
//
//  Created by Alexander Rudenko on 27.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "NotifPlanner.h"

@implementation NotifPlanner{
    NSMutableArray *drugs;
}

static NotifPlanner *sharedInst = NULL;
static int maxEventsCount = 63;

+(NotifPlanner*)sharedInstance{
    if (!sharedInst || sharedInst == NULL) {
        sharedInst = [NotifPlanner new];
        sharedInst.brutUpdate = NO; // Режим простого/интелектуального режима обновления. По умолчанию включен интелектуальный
    }
    return sharedInst;
}

-(void)updateNotifications{
    [self loadPills];
    [self makeNotifList];
}

-(void)deleteAllNotifications{
     [[UIApplication sharedApplication] cancelAllLocalNotifications];
    
}

-(void)removeNotification:(UILocalNotification*)notif{
    [[UIApplication sharedApplication] cancelLocalNotification:notif];
    [UserDefaults setObject:[NSMutableArray new] forKey:@"cashedNotifications"];
}

-(void)loadPills{
    drugs = [GlobalData pills];
}

-(void)makeNotifList{
    NSMutableArray *allTasksList = [NSMutableArray new];
    
    for (int i = 0; i<drugs.count; i++){
        NSMutableDictionary *drug = drugs[i];
        NSDate *tillDate;
        NSDate *currentDate; //sinceDate;
        NSDate *fireTime = [Global parseTime:drug[@"time"]];
        
        NSCalendar *calendarCD = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
        NSDateComponents *componentsCD = [calendarCD components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:[NSDate date]];
        [componentsCD setHour:fireTime.hour];
        [componentsCD setMinute:fireTime.minute];
        [componentsCD setSecond:fireTime.seconds];
        currentDate = [calendarCD dateFromComponents:componentsCD];
        
        NSCalendar *calendarTD = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
        NSDateComponents *componentsTD = [calendarTD components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:[Global parseDateTime:drug[@"tillDate"]]];
        [componentsTD setHour:fireTime.hour];
        [componentsTD setMinute:fireTime.minute];
        [componentsTD setSecond:fireTime.seconds];
        tillDate = [calendarTD dateFromComponents:componentsTD];
        
        
        
        NSMutableArray *currentDrugList = [NSMutableArray new];
        int dayStep = 0;
        if ([drug[@"repeat"] isEqualToString:@"DAILY"]){
            dayStep = 1;
        } else if ([drug[@"repeat"] isEqualToString:@"EVERY_2_DAYS"]){
            dayStep = 2;
        }
        
        while ([currentDate isEarlierThanDate:tillDate]||[currentDate isEqualToDate:tillDate]) {
            if (![currentDate isInPast]){
                NSDate *alertFireDate;
                NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
                NSDateComponents *components = [calendar components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:currentDate];
                [components setHour:fireTime.hour];
                [components setMinute:fireTime.minute];
                [components setSecond:fireTime.seconds];
                alertFireDate = [calendar dateFromComponents:components];
                NSMutableDictionary *alertInfo = [NSMutableDictionary new];
                [alertInfo setObject:alertFireDate forKey:@"fireDate"];
                [alertInfo setObject:drug[@"name"] forKey:@"alertBody"];
                [alertInfo setObject:drug forKey:@"info"];
                [currentDrugList addObject:alertInfo];
            }
            currentDate = [currentDate dateByAddingDays:dayStep];
        }
        [allTasksList addObjectsFromArray:currentDrugList];
    }
    
 //   NSLog(@"%@",allTasksList);
    [self sortFilterAndMake:allTasksList];
    
}

-(void)sortFilterAndMake:(NSMutableArray*)nots{
    
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey: @"fireDate" ascending: YES];
    nots = [NSMutableArray arrayWithArray:[nots sortedArrayUsingDescriptors:[NSArray arrayWithObject:sortDescriptor]]];
    
    
    // [Global sortArray:nots byKey:@"fireDate" wayUp:YES];
    NSMutableArray *cashedVersion = [NSMutableArray arrayWithArray: [UserDefaults objectForKey:@"cashedNotifications"]];
    NSMutableArray *freshVersion = [NSMutableArray new];
    for (int i = 0; i<MIN(maxEventsCount,nots.count); i++){
        [freshVersion addObject:nots[i]];
    }
    
    if (!self.brutUpdate){ // Режим простого/интелектуального режима обновления. По умолчанию включен интелектуальный
        
        for (int i=0; i<cashedVersion.count; i++){
            if ([cashedVersion[i][@"fireDate"] isEarlierThanDate:[NSDate date]]){
                [cashedVersion removeObject:cashedVersion[i]];
                i--;
            }
        }
        
        [UserDefaults setObject:cashedVersion forKey:@"cashedNotifications"];
        
        if ([freshVersion isEqualToArray:cashedVersion]){
            NSLog(@"Задания не менялись");
            return;
        }
        
    }
    NSLog(@"Ставим новые задачи");
    
    [self deleteAllNotifications];
    
    for (int i = 0; i<freshVersion.count; i++){
        UILocalNotification *notif = [[UILocalNotification alloc] init];
        notif.fireDate = freshVersion[i][@"fireDate"]; //время, когда наступит время нотификатора
        notif.alertAction = @"Отметить"; //Текст кнопочки, вызывающей вашу программу из фонового режима
        notif.alertBody = [NSString stringWithFormat:@"%@ %i шт., время приема- %@", freshVersion[i][@"alertBody"],  [freshVersion[i][@"info"][@"quantity"] intValue], [freshVersion[i][@"fireDate"] stringWithFormat:@"yyyy-MM-dd HH:mm"]]; //Тело сообщения над кнопочкой
//        notif.soundName = UILocalNotificationDefaultSoundName; //дефолтный звук сообщения. Можно задать свой в папке проекта.
        notif.userInfo = freshVersion[i][@"info"];
        [[UIApplication sharedApplication] scheduleLocalNotification:notif]; //Размещаем наше локальное уведомление!
    }
    
    [UserDefaults setObject:freshVersion forKey:@"cashedNotifications"];
    
    if (freshVersion.count<nots.count){
        // нада добавить напоминалку для того, чтобы пнуть пользователя, чтобы открыл приложение для обновления задач
        [self addLastNotificationReminderForDate:[(NSDate*)[freshVersion lastObject][@"fireDate"] dateByAddingTimeInterval:20.0f]];
    }
}

-(void)addLastNotificationReminderForDate:(NSDate*)date{
    UILocalNotification *notif = [[UILocalNotification alloc] init];
    notif.fireDate = date; //время, когда наступит время нотификатора, у нас это текущая дата + 20 секунд. Можно прибегнуть к помощи NSDateComponents для установок своей даты.
    notif.alertAction = @"Открыть приложение"; //Текст кнопочки, вызывающей вашу программу из фонового режима
    notif.alertBody = @"Вам необходимо запустить приложение для обновления списка задач"; //Тело сообщения над кнопочкой
//    notif.soundName = UILocalNotificationDefaultSoundName; //дефолтный звук сообщения. Можно задать свой в папке проекта.
    [[UIApplication sharedApplication] scheduleLocalNotification:notif]; //Размещаем наше локальное уведомление!
}


@end

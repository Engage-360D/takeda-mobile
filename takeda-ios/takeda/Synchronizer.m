//
//  Synchronizer.m
//  takeda
//
//  Created by Alexander Rudenko on 02.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "Synchronizer.h"




@implementation Synchronizer{

}

static Synchronizer *sharedInst = NULL;

@synthesize completedJobsCount;
@synthesize tasks;

+(Synchronizer*)sharedInstance{
    if (!sharedInst || sharedInst == NULL) {
        sharedInst = [Synchronizer new];
    }
    return sharedInst;
}

+(void)resetData{
    sharedInst = nil;
}

+(NSArray*)fullTasksList{
    return [NSArray arrayWithObjects:jLoadRiskAnalResults,jLoadPills, jLoadCities, jLoadSpecializations, jLoadIncidents, jLoadISP, jLoadTimeLine, nil];
}

+(NSDictionary*)jobSelectors{
    return @{jLoadRiskAnalResults:[NSValue valueWithPointer:@selector(loadRiskAnalResults)],
             jLoadPills:[NSValue valueWithPointer:@selector(loadPills)],
             jLoadCities:[NSValue valueWithPointer:@selector(loadCities)],
             jLoadSpecializations:[NSValue valueWithPointer:@selector(loadSpecializations)],
             jLoadIncidents:[NSValue valueWithPointer:@selector(loadIncidents)],
             jLoadISP:[NSValue valueWithPointer:@selector(loadISP)],
             jLoadTimeLine:[NSValue valueWithPointer:@selector(loadTimeLine)]
             };
}

-(void)startSynchronizeTasks:(NSArray*)tasksArray completition:(void (^)(BOOL success, id result))completion{
    self.resultBlock = completion;
    if (!tasksArray||tasksArray.count == 0){
        tasks = [Synchronizer fullTasksList];
    } else {
        tasks = tasksArray;
    }
    [self startTasksMachine];
}

-(void)startSynchronizeCompletition:(void (^)(BOOL success, id result))completion{
    self.resultBlock = completion;
    tasks = [Synchronizer fullTasksList];
    [self startTasksMachine];
}


-(void)startTasksMachine{
    completedJobsCount = 0;
    for (int i = 0; i<tasks.count; i++){
        id ppp = [Synchronizer jobSelectors][tasks[i]];
        SEL selector = [ppp pointerValue];
        IMP imp = [self methodForSelector:selector];
        void (*func)(id, SEL) = (void *)imp;
        func(self, selector);
    }
}


-(void)loadRiskAnalResults{
    NSLog(@"start loadRisk anal");
    int lastId = [GlobalData lastResultDataId];
    [GlobalData loadAnalysisFromServerWithLastId:lastId completion:^(BOOL success, NSError *error, id result) {
        NSLog(@"finish loadRisk anal");
        [self finishJob];
    }];

}

-(void)loadPills{
    NSLog(@"start pills");
    [GlobalData loadPillsCompletition:^(BOOL completition, id result){
        NSLog(@"finish pills");
        [self finishJob];
    }];
}

-(void)loadTimeLine{
    [GlobalData loadTimelineCompletition:^(BOOL success, id result){
        NSLog(@"finish cities");
        [self finishJob];
    }];
}

-(void)loadCities{
    //NSDate *lastCitiesUpdateDate = [GlobalData lastloadCitiesDate];
    [GData loadCitiesList:^(BOOL success, id result){
        NSLog(@"finish cities");
        [self finishJob];
    }];
}

-(void)loadSpecializations{
    [GData loadSpecializationsList:^(BOOL success, id result){
        NSLog(@"finish specializations");
        [self finishJob];
    }];
}

-(void)loadIncidents{
    [ServData loadIncidentsCompletion:^(BOOL result, NSError *error) {
        NSLog(@"finish incidents");
        [self finishJob];
    }];
}

-(void)loadISP{
    [GlobalData updateISP:^(BOOL success, id result) {
        NSLog(@"finish ISP");
        [self finishJob];
    }];
}


-(void)finishJob{
    if (self.resultBlock){
        completedJobsCount++;
        NSLog(@"compl jobs count = %i",completedJobsCount);
        if (completedJobsCount == tasks.count){
            completedJobsCount = 0;
            [self finishSynchronize];
        }
    }
}

-(void)finishSynchronize{
    NSLog(@"finish");
    self.resultBlock(YES,nil);
}

@end

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

-(void)startSynchronizeCompletition:(void (^)(BOOL success, id result))completion{
    self.resultBlock = completion;
//    [self loadRiskAnalResults];
//    [self loadPills];
    tasks = [NSMutableArray arrayWithObjects:[NSValue valueWithPointer:@selector(loadRiskAnalResults)],[NSValue valueWithPointer:@selector(loadPills)], [NSValue valueWithPointer:@selector(loadCities)], [NSValue valueWithPointer:@selector(loadSpecializations)], [NSValue valueWithPointer:@selector(loadIncidents)], nil];
    [self startTasksMachine];
}

-(void)startTasksMachine{
    completedJobsCount = 0;
    for (int i = 0; i<tasks.count; i++){
        SEL selector = [tasks[i] pointerValue];
        IMP imp = [self methodForSelector:selector];
        void (*func)(id, SEL) = (void *)imp;
        func(self, selector);
    }
}


-(void)loadRiskAnalResults{
    NSLog(@"start loadRisk anal");
    int lastId = [GlobalData lastResultDataId];
    [ServData loadAnalysisFromServerWithLastId:lastId completion:^(BOOL success, NSError *error, id result) {
        NSLog(@"finish loadRisk anal");
        if (success) {
            [GlobalData saveResultAnalyses:result[@"data"]];
        } else {
        }
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

-(void)finishJob{
    completedJobsCount++;
    NSLog(@"compl jobs count = %i",completedJobsCount);
    if (completedJobsCount == tasks.count){
        completedJobsCount = 0;
        [self finishSynchronize];
    }
}

-(void)finishSynchronize{
    NSLog(@"finish");
    self.resultBlock(YES,nil);
}

@end

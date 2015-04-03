//
//  analizData.h
//  takeda
//
//  Created by Serg on 4/6/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface analizData : NSObject

+(analizData*)sharedObject;
+(void)resetData;

-(NSArray*)getQuestionsDataUser;
-(NSArray*)getQuestionsHistoryUser;
-(NSArray*)getQuestionsDailyRation;

-(NSMutableDictionary*)dicRiskData;
-(NSArray*)getListYears;
-(NSArray*)getListGrowth;
-(NSArray*)getListWeight;
-(NSArray*)getListCholesterol;
-(NSArray*)getListArterial_pressure;
-(NSArray*)getListWalking;
-(NSArray*)getListSport;
-(NSArray*)getListSalt;

-(id) recursiveMutable:(id)object;

@end

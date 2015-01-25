//
//  GlobalData.h
//  takeda
//
//  Created by Alexander Rudenko on 23.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GlobalData : NSObject

+(GlobalData*)sharedObject;
@property (nonatomic, strong) FMDatabase *database;

+(NSMutableArray*)regionsList;

+(void)loadRegionsList:(void (^)(BOOL success, id result))completion;

+(void)saveRegions:(NSMutableArray*)regions;

+(void)saveResultAnalyses:(NSMutableDictionary*)result;
+(NSMutableArray*)resultAnalyses;


@end

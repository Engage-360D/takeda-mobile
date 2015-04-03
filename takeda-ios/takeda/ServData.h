//
//  ServData.h
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ServData : NSObject

+(ServData*)sharedObject;
+(void)resetData;

+(void)authUserWithLogin:(NSString*)login password:(NSString*)password completion:(void (^)(BOOL result, NSError* error))completion;
+(void)authUserWithSocial:(NSString*)social user:(NSString*)user_id token:(NSString*)token completion:(void (^)(BOOL result, NSError* error))completion;
+(void)getUserIdData:(NSString*)user_id withCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)resetUserPassword:(NSString*)user_login withCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)resetUserParamsPassword:(NSString*)password completion:(void (^)(BOOL result, NSError* error))completion;
+(void)registrationUserWithData:(NSDictionary*)params  completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion;
+(void)sendIncident:(NSString*)incident comment:(NSString*)comment completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)loadIncidentsCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)updateUser:(NSString*)user_id withData:(NSDictionary*)params completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion;
+(void)loadRegionsWithCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)loadDietQuestions:(int)testId completion:(void (^)(NSError* error, id result))completion;
+(void)sendToServerDietResultsDiet:(int)testId testData:(NSDictionary*)testData completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)sendAnalysisToServer:(NSDictionary*)analysisData completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)loadAnalysisFromServerWithLastId:(int)lastId completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)resultAnalBlock:(NSString*)url completition:(void (^)(BOOL success, id result))completion;
+(void)shareTest:(int)testId viaEmail:(NSString*)email completition:(void (^)(BOOL success, id result))completion;
+(void)addDrug:(NSDictionary*)analysisData completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)updateDrug:(NSMutableDictionary*)drugData completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)deleteDrug:(NSMutableDictionary*)drugData completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)loadTimelineCompletition:(void (^)(BOOL success, id result))completion;
+(void)updateTask:(NSString*)taskId params:(NSDictionary*)taskParams completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)loadPillsCompletition:(void (^)(BOOL success, id result))completion;
+(void)loadCitiesCompletition:(void (^)(BOOL success, id result))completion;
+(void)loadSpecializationsCompletition:(void (^)(BOOL success, id result))completion;
+(void)loadMyCityByLocation:(CLLocation*)location copml:(void (^)(BOOL success, id result))completion;
+(void)loadLPUsListForCity:(NSString*)city spec:(NSString*)spec copml:(void (^)(BOOL success, id result))completion;

+(void)sendCommonPOST:(NSString*)urlStr params:(NSString*)HTMLStr success:(void (^)(id result, NSError *error))successIm;
+(void)sendCommonPOST:(NSString*)urlStr body:(NSData*)body success:(void (^)(id result, NSError *error))successIm;
+(void)sendCommon:(NSString*)urlStr success:(void (^)(id result, NSError *error))successIm;
+(void)sendCommonPUT:(NSString*)urlStr body:(NSData*)body success:(void (^)(id result, NSError *error))successIm;


@end

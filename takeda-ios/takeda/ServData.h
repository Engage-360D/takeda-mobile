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

+(void)authUserWithLogin:(NSString*)login
                password:(NSString*)password
              completion:(void (^)(BOOL result, NSError* error))completion;

+(void)getUserIdData:(NSString*)user_id withCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)resetUserPassword:(NSString*)user_login withCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)registrationUserWithData:(NSDictionary*)params  completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion;
+(void)updateUser:(NSString*)user_id withData:(NSDictionary*)params completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion;
+(void)loadRegionsWithCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)sendAnalysisToServer:(NSDictionary*)analysisData completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)loadAnalysisFromServerWithLastId:(int)lastId completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)resultAnalBlock:(NSString*)url completition:(void (^)(BOOL success, id result))completion;
+(void)shareTest:(int)testId viaEmail:(NSString*)email completition:(void (^)(BOOL success, id result))completion;

+(void)sendCommonPOST:(NSString*)urlStr params:(NSString*)HTMLStr success:(void (^)(id))successIm;
+(void)sendCommonDELETE:(NSString*)urlStr params:(NSString*)params success:(void (^)(id))successIm;
+(void)sendCommonPOST:(NSString*)urlStr body:(NSData*)body success:(void (^)(id))successIm;
+(void)sendCommon:(NSString*)urlStr success:(void (^)(id))successIm;
+(void)sendCommonPUT:(NSString*)urlStr body:(NSData*)body success:(void (^)(id))successIm;


@end

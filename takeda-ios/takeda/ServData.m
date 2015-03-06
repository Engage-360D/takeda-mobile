//
//  ServData.m
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//


#import "ServData.h"

#define kErrDomain @"myDomain"

@implementation ServData

static ServData *objectInstance = nil;

+(ServData*)sharedObject{
    @synchronized(self){
        if (!objectInstance) {
            objectInstance = [ServData new];
        }
        return objectInstance;
    }
}

+(NSData*)preparedParams:(NSDictionary*)params{
    NSMutableDictionary *p = [NSMutableDictionary new];
    [p setObject:[self setupNulls:params] forKey:@"data"];
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject: p
                                                       options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:nil];
    return jsonData;
}

+(id)setupNulls:(id)object{
    if([object isKindOfClass:[NSDictionary class]])
    {
        NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithDictionary:object];
        for(NSString* key in [dict allKeys])
        {
            [dict setObject:[self setupNulls:[dict objectForKey:key]] forKey:key];
        }
        return dict;
    }
    else if([object isKindOfClass:[NSArray class]])
    {
        NSMutableArray* array = [NSMutableArray arrayWithArray:object];
        for(int i=0;i<[array count];i++)
        {
            [array replaceObjectAtIndex:i withObject:[self setupNulls:[array objectAtIndex:i]]];
        }
        return array;
    }
    else if([object isKindOfClass:[NSString class]]){
        if ([object isEqualToString:@"-"]){
            // object = [NSNull null];
            object = [NSNull null];

            return object;
        }
        return [NSMutableString stringWithString:object];
    }
    
    return object;

}

#pragma mark - Network requests

+(void)authUserWithSocial:(NSString*)social user:(NSString*)user_id token:(NSString*)token
              completion:(void (^)(BOOL result, NSError* error))completion
{
    
    NSString *url = [NSString stringWithFormat:
                     @"%@%@/%@",kServerURL,kTokens,social];
    
    NSMutableDictionary *params = [NSMutableDictionary new];
    if (token.length>0) [params setObject:token forKey:@"access_token"];
    if (user_id.length>0) [params setObject:user_id forKey:@"user_id"];
    
    [self sendCommonPOST:url body:[self preparedParams: params] success:^(id result, NSError *error){
        if (result[@"data"][@"id"]){
            if ([result[@"linked"][@"users"] isKindOfClass:[NSArray class]]&&[result[@"linked"][@"users"] count]>0){
                NSString *login = result[@"linked"][@"users"][0][@"email"];
                User.userData = [NSMutableDictionary new];
                User.access_token = result[@"data"][@"id"];
                User.user_id = result[@"data"][@"links"][@"user"];
                User.user_login = login;
                // success
                
                NSMutableDictionary *usData = [User getUserInfo:login];
                if (usData){
                    [User updateUser:login userInfo:usData accessToken:result[@"data"][@"id"]];
                }
                
                completion(YES,nil);

                
            } else {
                completion(NO,nil);
            }
            
            
        } else {
            completion(NO,nil);
        }
    }];
}



+(void)authUserWithLogin:(NSString*)login
                password:(NSString*)password
              completion:(void (^)(BOOL result, NSError* error))completion
{
    
    NSString *url = [NSString stringWithFormat:
                     @"%@%@",kServerURL,kTokens];
    
    NSDictionary *params = @{@"email":login,@"plainPassword":password};

    [self sendCommonPOST:url body:[self preparedParams: params] success:^(id result, NSError *error){
        if (result[@"data"][@"id"]){
            User.userData = [NSMutableDictionary new];
            User.access_token = result[@"data"][@"id"];
            User.user_id = result[@"data"][@"links"][@"user"];
            User.user_login = login;
            // success
            
            NSMutableDictionary *usData = [User getUserInfo:login];
            if (usData){
                [User updateUser:login userInfo:usData accessToken:result[@"data"][@"id"]];
            }
            
            completion(YES,nil);
        } else {
            completion(NO,nil);
        }
    }];
}

+(void)getUserIdData:(NSString*)user_id withCompletion:(void (^)(BOOL result, NSError* error))completion
{
    if (appDelegate.hostConnection == NotReachable) {
        completion(NO,[NSError errorWithDomain:kErrDomain code:500 userInfo:nil]);
        return;
    }
    
    NSString *url = [NSString stringWithFormat:
                     @"%@%@",kServerURL,kAccount];
    
    [self sendCommon:url success:^(id res, NSError *error){
        if (res[@"data"]){
            // success
            [User updateUser:res[@"data"][@"email"] userInfo:res[@"data"] accessToken:User.access_token];
            completion(YES,nil);
        } else {
            completion(NO,nil);
        }
        
        
    }];
}


+(void)resetUserPassword:(NSString*)user_login withCompletion:(void (^)(BOOL result, NSError* error))completion
{
    if (appDelegate.hostConnection == NotReachable){
        completion(NO,[NSError errorWithDomain:kErrDomain code:500 userInfo:nil]);
        return;
    }
    
    NSString *url = [NSString stringWithFormat:
                     @"%@%@",kServerURL,kAccountResetPass];
    
    NSDictionary *params = @{@"email":user_login};
    
    [self sendCommonPOST:url body:[self preparedParams: params] success:^(id result, NSError *error){
        completion(YES,nil);

//        if (result[@"data"][@"id"]){
//            // success
//            completion(YES,nil);
//        } else {
//            completion(NO,nil);
//        }
    }];
}

+(void)loadRegionsWithCompletion:(void (^)(BOOL result, NSError* error))completion
{
    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kRegionsList];
    
    [self sendCommon:url success:^(id result, NSError *error){
        BOOL success = NO;
        if (result[@"data"]) {
            [GlobalData saveRegions:result[@"data"]];
            success = YES;
        } else {
            
        }
        completion(success, error);

    }];
    
}

+(void)sendIncident:(NSString*)incident comment:(NSString*)comment completion:(void (^)(BOOL success, NSError* error, id result))completion{
    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kAccountIncidents];
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithDictionary:@{incident:[NSNumber numberWithBool:YES]}];
    
    [self sendCommonPOST:url body:[self preparedParams:params] success:^(id res, NSError *errorr){
        completion([errorr answerOk], errorr, res);
    }];

}

+(void)registrationUserWithData:(NSDictionary*)params  completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion
{

    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kUsers];
    [self sendCommonPOST:url body:[self preparedParams:params] success:^(id res, NSError *error){
        BOOL success = NO;
        NSString *textError;
        if ([res isKindOfClass:[NSDictionary class] ]) {
                if (res[@"data"][@"id"]){
//                    User.user_id = res[@"data"][@"links"][@"user"];
//                    User.user_login = res[@"data"][@"email"];
                success = YES;
                completion(success, error, textError);
            } else {
//                if (response.statusCode == 500) {
//                    textError = @"Пользователь с текущим email-адрессом был ранее зарегистрирован";
//                }else{
//                    err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"Ошибка при регистрации": resp}];
//                }
                completion(success, error, textError);
            }
        } else {
//            if (!resp) {
//                resp = @"Ошибка регистрации";
//            }
//            err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"error": resp}];
            completion(success, error, textError);
        }
    }];
}


+(void)updateUser:(NSString*)user_id withData:(NSDictionary*)params completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion
{
    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kAccount];
    [self sendCommonPUT:url body:[self preparedParams:params] success:^(id res, NSError *error){
        BOOL success = NO;
        NSString *textError;
        if ([res isKindOfClass:[NSDictionary class] ]) {
            if (res[@"data"][@"id"]){
                success = YES;
                completion(success, error, textError);
            } else {
                //                if (response.statusCode == 500) {
                //                    textError = @"Пользователь с текущим email-адрессом был ранее зарегистрирован";
                //                }else{
                //                    err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"Ошибка при регистрации": resp}];
                //                }
                completion(success, error, textError);
            }
        } else {
            //            if (!resp) {
            //                resp = @"Ошибка регистрации";
            //            }
            //            err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"error": resp}];
            completion(success, error, textError);
        }
    }];
}

+(void)loadDietQuestions:(int)testId completion:(void (^)(NSError* error, id result))completion{
    NSString *url = [NSString stringWithFormat:@"%@%@/%i/%@",kServerURL,kTestResults,testId,kTestResultDietQuestions];
    
    [self sendCommon:url success:^(id result, NSError *error){
        if (result[@"data"]){
            completion(error, result);
        } else {
            completion(error, result);
        }
    }];
}

+(void)sendToServerDietResultsDiet:(int)testId testData:(NSDictionary*)testData completion:(void (^)(BOOL success, NSError* error, id result))completion{
    NSMutableString *url = [NSMutableString stringWithFormat:@"%@%@/%i/%@?token=%@&",kServerURL,kTestResults,testId,kTestResultDietRecommendations, User.access_token];
    [url appendString:[Global dictToStr:testData]];
    
    [self sendCommon:url success:^(id result, NSError *error) {
        if (result[@"data"]){
            completion(YES,error, result);
        } else {
            completion(NO,error, result);
        }

    }];
    
}

+(void)sendAnalysisToServer:(NSDictionary*)analysisData completion:(void (^)(BOOL success, NSError* error, id result))completion{
    NSString *url = [NSString stringWithFormat:
                     @"%@%@",kServerURL,kTestResults];
    
    [self sendCommonPOST:url body:[self preparedParams: analysisData] success:^(id result, NSError *error){
        if (result[@"data"][@"id"]){
            completion(YES,error, result);
        } else {
            completion(NO,error, result);
        }
    }];
}

+(void)loadAnalysisFromServerWithLastId:(int)lastId completion:(void (^)(BOOL success, NSError* error, id result))completion{
    NSString *url = [NSString stringWithFormat:@"%@%@?sinceId=%i",kServerURL,kTestResults,lastId];
    [self sendCommon:url success:^(id result, NSError *error){
        if ([result[@"data"] isKindOfClass:[NSArray class]]&&[result[@"data"] count]>0){
            completion(YES,nil, result);
        } else {
            completion(NO,nil, result);
        }
    }];
}

+(void)resultAnalBlock:(NSString*)url completition:(void (^)(BOOL success, id result))completion{
    NSString *urlstr = [NSString stringWithFormat:@"%@%@",kServerURL,url];
    [self sendCommon:urlstr success:^(id res, NSError *error){
        BOOL success = (res!=nil)&&([error answerOk]);
        if (success){
            [GlobalData casheRequest:res fromUrl:url];
        }
        completion(success, res);
    }];
    
}

+(void)loadTimelineCompletition:(void (^)(BOOL success, id result))completion{
    
    NSString *urlstr = [NSString stringWithFormat:@"%@%@",kServerURL,kAccountTimeline];
    [self sendCommon:urlstr success:^(id res, NSError *error){
        if (res!=nil&&[error answerOk]){
            completion(YES, res);
        } else {
            completion(NO, res);
        }
        
    }];
    
}

+(void)loadPillsCompletition:(void (^)(BOOL success, id result))completion{
    
    NSString *urlstr = [NSString stringWithFormat:@"%@%@",kServerURL,kAccountPills];
    [self sendCommon:urlstr success:^(id res, NSError *error){
        if (res!=nil&&[error answerOk]){
            completion(YES, res);
        } else {
            completion(NO, res);
        }
    }];
    
}


+(void)shareTest:(int)testId viaEmail:(NSString*)email completition:(void (^)(BOOL success, id result))completion{
    NSString *urlstr = [NSString stringWithFormat:@"%@%@/%i/%@",kServerURL,kTestResults,testId,kTestResultShareEmail];
    NSDictionary *params = @{@"email":email};
    [self sendCommonPOST:urlstr body:[self preparedParams: params] success:^(id result, NSError *error){
            completion([error answerOk], result);
    }];
    
}

+(void)addDrug:(NSDictionary*)drugData completion:(void (^)(BOOL success, NSError* error, id result))completion{

    NSString *url = [NSString stringWithFormat:
                     @"%@%@",kServerURL,kAccountPills];
    
    [self sendCommonPOST:url body:[self preparedParams: drugData] success:^(id result, NSError *error){
        if (result[@"data"][@"id"]){
            completion(YES,nil, result);
        } else {
            completion(NO,nil, result);
        }
    }];
}

+(void)updateDrug:(NSMutableDictionary*)drugData completion:(void (^)(BOOL success, NSError* error, id result))completion{
    
    NSString *url = [NSString stringWithFormat:
                     @"%@%@/%@",kServerURL,kAccountPills,drugData[@"id"]];
    
    NSMutableDictionary *dict = [Global recursiveMutable:[drugData mutableCopy]];
    [dict removeObjectForKey:@"id"];
    [dict removeObjectForKey:@"links"];

    [self sendCommonPUT:url body:[self preparedParams: dict] success:^(id result, NSError *error){
        if (result[@"data"][@"id"]){
            [GlobalData updatePill:result[@"data"]];
            completion(YES,nil, result);
        } else {
            completion(NO,nil, result);
        }
    }];
}

+(void)deleteDrug:(NSMutableDictionary*)drugData completion:(void (^)(BOOL success, NSError* error, id result))completion{
    
    NSString *url = [NSString stringWithFormat:
                     @"%@%@/%@",kServerURL,kAccountPills,drugData[@"id"]];
    
    [self sendCommonDELETE:url params:@"" success:^(id result, NSError *error) {
        if ([error answerOk]){
            [GlobalData deletePill:drugData];
            completion(YES,nil, result);
        } else {
            completion(NO,nil, result);
        }
    }];
}



+(void)updateTask:(NSString*)taskId params:(NSDictionary*)taskParams completion:(void (^)(BOOL success, NSError* error, id result))completion{
    
    NSString *url = [NSString stringWithFormat:@"%@%@/%@",kServerURL,kAccountTasks, taskId];
    
    [self sendCommonPUT:url body:[self preparedParams: taskParams] success:^(id result, NSError *error){
        if (result[@"data"][@"id"]){
            completion(YES,nil, result);
        } else {
            completion(NO,nil, result);
        }
    }];
}

#pragma mark - Common methods

+ (void)cleanJsonToObject:(id)data {
    [data Kez_removeNulls];
}


+(void)sendCommonPOST:(NSString*)urlStr params:(NSString*)HTMLStr success:(void (^)(id result, NSError *error))successIm{
    
    if (appDelegate.hostConnection == NotReachable) {
        successIm(nil,[NSError errorWithDomain:kErrDomain code:500 userInfo:nil]);
        return;
    }

    if (User.access_token.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }
    
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    HTMLStr = [self clearSendText:HTMLStr];
    
    NSData* HTTPBody = [HTMLStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"POST";
    urlRequest.HTTPBody = HTTPBody;
    
    [urlRequest setValue:@"application/vnd.api+json" forHTTPHeaderField: @"Content-Type"];
    [urlRequest setValue:@"*/*" forHTTPHeaderField: @"Accept"];

    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               if (!error) error = [[NSError alloc] initWithDomain:kErrDomain code:((NSHTTPURLResponse*)res).statusCode userInfo:@{}];
                               id jsonResponse;
                               if (response){
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   
                                   [self cleanJsonToObject:jsonResponse];
                                   
                                   NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       successIm (jsonResponse,error);
                                   } else {
                                       NSString *answStr = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
                                       NSLog(@"%@",answStr);
                                       
                                       successIm (nil,error);
                                       
                                   }
                               }
                               
                               
                           }];
    
}


+(void)sendCommonDELETE:(NSString*)urlStr params:(NSString*)params success:(void (^)(id result, NSError *error))successIm{
    if (User.access_token.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }

    if (appDelegate.hostConnection == NotReachable) {
        successIm(nil,[NSError errorWithDomain:kErrDomain code:500 userInfo:nil]);
        return;
    }

    
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    NSData* HTTPBody = [params dataUsingEncoding:NSUTF8StringEncoding];
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"DELETE";
    urlRequest.HTTPBody = HTTPBody;
    
    [urlRequest setValue:@"*/*" forHTTPHeaderField: @"Accept"];

    
    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               if (!error) error = [[NSError alloc] initWithDomain:kErrDomain code:((NSHTTPURLResponse*)res).statusCode userInfo:@{}];
                               id jsonResponse;
                               if (response){
                                   
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       successIm (jsonResponse,error);
                                   } else {
                                       successIm (nil,error);
                                       
                                   }
                               }
                               
                               
                           }];
    
}


+(void)sendCommonPOST:(NSString*)urlStr body:(NSData*)body success:(void (^)(id result, NSError *error))successIm{
    if (User.access_token.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }
    
    if (appDelegate.hostConnection == NotReachable) {
        successIm(nil,[NSError errorWithDomain:kErrDomain code:500 userInfo:nil]);
        return;
    }


    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"POST";
    urlRequest.HTTPBody = body;
    
    [urlRequest setValue:@"application/vnd.api+json" forHTTPHeaderField: @"Content-Type"];
    [urlRequest setValue:@"*/*" forHTTPHeaderField: @"Accept"];

    
    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               if (!error) error = [[NSError alloc] initWithDomain:kErrDomain code:((NSHTTPURLResponse*)res).statusCode userInfo:@{}];
                               id jsonResponse;
                               if (response){
                                   
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       successIm (jsonResponse,error);
                                   } else {
                                       NSString *err = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
                                       NSLog(@"err = %@",err);
                                       successIm (nil,error);
                                       
                                   }
                               }
                               
                               
                           }];
    
}

+(void)sendCommonPUT:(NSString*)urlStr body:(NSData*)body success:(void (^)(id result, NSError *error))successIm{
    if (User.access_token.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }
    
    if (appDelegate.hostConnection == NotReachable) {
        successIm(nil,[NSError errorWithDomain:kErrDomain code:500 userInfo:nil]);
        return;
    }

    
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"PUT";
    urlRequest.HTTPBody = body;
    
    [urlRequest setValue:@"application/vnd.api+json" forHTTPHeaderField: @"Content-Type"];
    [urlRequest setValue:@"*/*" forHTTPHeaderField: @"Accept"];

    
    
    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               if (!error) error = [[NSError alloc] initWithDomain:kErrDomain code:((NSHTTPURLResponse*)res).statusCode userInfo:@{}];
                               id jsonResponse;
                               if (response){
                                   
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       successIm (jsonResponse,error);
                                   } else {
                                       NSString *err = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
                                       NSLog(@"err = %@",err);
                                       successIm (nil,error);
                                       
                                   }
                               }
                               
                               
                           }];
    
}



+(void)sendCommon:(NSString*)urlStr success:(void (^)(id result, NSError *error))successIm{
    if (User.access_token.length>0&&[urlStr rangeOfString:User.access_token].location == NSNotFound){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }

    if (appDelegate.hostConnection == NotReachable) {
        successIm(nil,[NSError errorWithDomain:kErrDomain code:500 userInfo:nil]);
        return;
    }

    
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    
    // urlStr = [NSString stringWithFormat:@"%@&type=mobile",urlStr];
    
    [urlRequest setValue:@"*/*" forHTTPHeaderField: @"Accept"];

    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               if (!error) error = [[NSError alloc] initWithDomain:kErrDomain code:((NSHTTPURLResponse*)res).statusCode userInfo:@{}];
                               id jsonResponse;
                               if (response){
                                   
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   //  NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       [self cleanJsonToObject:jsonResponse];
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       successIm (jsonResponse,error);
                                   } else {
                                       successIm (nil,error);
                                       
                                   }
                               }
                               
                               
                           }];
    
}



+(NSString*)clearSendText:(NSString*)text{
    if (!text) return nil;
    NSMutableString *deutschEscaped = [NSMutableString stringWithString: text];//[text stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    //  [deutschEscaped replaceOccurrencesOfString:@"$" withString:@"%24" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"&" withString:@"%26" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    [deutschEscaped replaceOccurrencesOfString:@"+" withString:@"%2B" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"," withString:@"%2C" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"/" withString:@"%2F" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@":" withString:@"%3A" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@";" withString:@"%3B" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"=" withString:@"%3D" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"?" withString:@"%3F" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"@" withString:@"%40" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@" " withString:@"%20" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"\t" withString:@"%09" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"#" withString:@"%23" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"<" withString:@"%3C" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@">" withString:@"%3E" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"\"" withString:@"%22" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    //  [deutschEscaped replaceOccurrencesOfString:@"\n" withString:@"%0A" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [deutschEscaped length])];
    return deutschEscaped;
}


@end

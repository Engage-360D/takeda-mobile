//
//  ServData.m
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "ServData.h"

@implementation ServData

static ServData *objectInstance = nil;
@synthesize database;

+(ServData*)sharedObject{
    @synchronized(self){
        if (!objectInstance) {
            objectInstance = [ServData new];
            [objectInstance openDB];
        }
        return objectInstance;
    }
}

-(void)openDB{
    database = [FMDatabase databaseWithPath:[Global pathToDB]];
    [database open];
}

+(NSMutableDictionary*)preparedParams:(NSDictionary*)params{
    NSMutableDictionary *p = [NSMutableDictionary new];
    [p setObject:params forKey:@"data"];
    return p;
}

#pragma mark - Network requests

+(void)authUserWithLogin:(NSString*)login
                password:(NSString*)password
              completion:(void (^)(BOOL result, NSError* error))completion
{
    
    completion(YES, nil);

    
    
    NSString *url = [NSString stringWithFormat:
                     @"%@/oauth/v2/token?"
                     "&client_id=%@"
                     "&client_secret=%@"
                     "&grant_type=password"
                     "&username=%@"
                     "&password=%@",kServerURL,client_id,client_secret,login,password];
    [self sendCommon:url success:^(id result){
        if (result){
            BOOL success = NO;
            if ([result hasKey:@"access_token"]) {
                [[UserData sharedObject] setAccessToken:[result objectForKey:@"access_token"]];
                success = YES;
            }
            completion(success, nil);
        } else {
            completion(NO, nil);
        }
        
    }];
}


+(void)getUserDataWithCompletion:(void (^)(BOOL result, NSError* error))completion
{
    NSString *url = [NSString stringWithFormat:
                     @"%@/api/users/me?access_token=%@",
                     kServerURL,
                     User.oauthToken];
    [self sendCommon:url success:^(id result){
        BOOL success = NO;
        if (result&&[result hasKey:@"id"]) {
            [User setUserData:result];
            success = YES;
        } else {
            completion(success, nil);
        }
        
    }];
    
}

+(void)getRegionsWithCompletion:(void (^)(BOOL result, NSError* error))completion
{
    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kRegionsList];
    
    [self sendCommon:url success:^(id result){
        BOOL success = NO;
        if (result&&[result hasKey:@"id"]) {
            // save regions
            success = YES;
        } else {
            completion(success, nil);
        }
        
    }];
    
}



+(void)registrationUserWithData:(NSDictionary*)params  completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion
{

    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject: [self preparedParams:params]
                                                       options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:nil];

    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kUsers];
    [self sendCommonPOST:url body:jsonData success:^(id res){
        BOOL success = NO;
        NSString *textError;
        if ([res isKindOfClass:[NSDictionary class] ]) {
            if ([res hasKey:@"id"]) {
            //   [[UserData sharedObject] setUserData:res];
                success = YES;
                completion(success, nil, textError);
            } else {
//                if (response.statusCode == 500) {
//                    textError = @"Пользователь с текущим email-адрессом был ранее зарегистрирован";
//                }else{
//                    err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"Ошибка при регистрации": resp}];
//                }
                completion(success, nil, textError);

            }
        } else {
//            if (!resp) {
//                resp = @"Ошибка регистрации";
//            }
//            err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"error": resp}];
            completion(success, nil, textError);

        }

    }];
    
    
    
    
    
    
    
    
    
    
}






#pragma mark - Common methods

+ (void)cleanJsonToObject:(id)data {
    [data Kez_removeNulls];
}


+(void)sendCommonPOST:(NSString*)urlStr params:(NSString*)HTMLStr success:(void (^)(id))successIm{
    if (User.oauthToken.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.oauthToken];
    }
    
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    HTMLStr = [self clearSendText:HTMLStr];
    // HTMLStr = [NSString stringWithFormat:@"%@&type=mobile",HTMLStr];
    
    NSData* HTTPBody = [HTMLStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"POST";
    urlRequest.HTTPBody = HTTPBody;
    
    //    [urlRequest setValue:@"XMLHttpRequest" forHTTPHeaderField:@"X-Requested-With"];
    
    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               id jsonResponse;
                               if (response){
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   
                                   [self cleanJsonToObject:jsonResponse];
                                   
                                   NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       call_completion_block(successIm, jsonResponse);
                                   } else {
                                       NSString *answStr = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
                                       NSLog(@"%@",answStr);
                                       
                                       call_completion_block(successIm, [NSMutableDictionary new]);
                                       
                                   }
                               }
                               
                               
                           }];
    
}


+(void)sendCommonDELETE:(NSString*)urlStr params:(NSString*)params success:(void (^)(id))successIm{
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    NSData* HTTPBody = [params dataUsingEncoding:NSUTF8StringEncoding];
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"DELETE";
    urlRequest.HTTPBody = HTTPBody;
    
    
    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               id jsonResponse;
                               if (response){
                                   
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       call_completion_block(successIm, jsonResponse);
                                   } else {
                                       call_completion_block(successIm, [NSMutableDictionary new]);
                                       
                                   }
                               }
                               
                               
                           }];
    
}


+(void)sendCommonPOST:(NSString*)urlStr body:(NSData*)body success:(void (^)(id))successIm{
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"POST";
    urlRequest.HTTPBody = body;
    
    
    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               id jsonResponse;
                               if (response){
                                   
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       call_completion_block(successIm, jsonResponse);
                                   } else {
                                       call_completion_block(successIm, [NSMutableDictionary new]);
                                       
                                   }
                               }
                               
                               
                           }];
    
}



+(void)sendCommon:(NSString*)urlStr success:(void (^)(id))successIm{
    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    
    // urlStr = [NSString stringWithFormat:@"%@&type=mobile",urlStr];
    
    
    [NSURLConnection sendAsynchronousRequest:urlRequest
                                       queue:[NSOperationQueue mainQueue]
                           completionHandler:^(NSURLResponse * res,
                                               NSData * response, NSError * error){
                               id jsonResponse;
                               if (response){
                                   
                                   NSError *jsonParserError = nil;
                                   jsonResponse = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableContainers error:&jsonParserError];
                                   //  NSLog(@"json= %@", jsonResponse);
                                   if (jsonParserError == nil) {
                                       [self cleanJsonToObject:jsonResponse];
                                       jsonResponse = [Global recursiveMutable:jsonResponse];
                                       call_completion_block(successIm, jsonResponse);
                                   } else {
                                       call_completion_block(successIm, [NSMutableDictionary new]);
                                       
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

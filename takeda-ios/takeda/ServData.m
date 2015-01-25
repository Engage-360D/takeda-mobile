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

+(void)authUserWithLogin:(NSString*)login
                password:(NSString*)password
              completion:(void (^)(BOOL result, NSError* error))completion
{
    
    NSString *url = [NSString stringWithFormat:
                     @"%@%@",kServerURL,kTokens];
    
    NSDictionary *params = @{@"email":login,@"plainPassword":password};

    [self sendCommonPOST:url body:[self preparedParams: params] success:^(id result){
        if (result[@"data"][@"id"]){
            User.access_token = result[@"data"][@"id"];
            User.user_id = result[@"data"][@"links"][@"user"];
            User.user_name = login;
            // success
            completion(YES,nil);
        } else {
            completion(NO,nil);
        }
    }];
}


+(void)getUserIdData:(NSString*)user_id withCompletion:(void (^)(BOOL result, NSError* error))completion
{
    NSString *url = [NSString stringWithFormat:
                     @"%@%@/%@",kServerURL,kUsers,user_id];
    
    [self sendCommon:url success:^(id res){
        if (res[@"data"]){
            // success
            [User updateUser:res[@"data"][@"email"] userInfo:res[@"data"] accessToken:User.access_token];
            completion(YES,nil);
        } else {
            completion(NO,nil);
        }
        
        
    }];
}

+(void)loadRegionsWithCompletion:(void (^)(BOOL result, NSError* error))completion
{
    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kRegionsList];
    
    [self sendCommon:url success:^(id result){
        BOOL success = NO;
        if (result[@"data"]) {
            [GlobalData saveRegions:result[@"data"]];
            success = YES;
        } else {
            
        }
        completion(success, nil);

    }];
    
}



+(void)registrationUserWithData:(NSDictionary*)params  completion:(void (^)(BOOL result, NSError* error, NSString* textError))completion
{

    NSString *url = [NSString stringWithFormat:@"%@%@",kServerURL, kUsers];
    [self sendCommonPOST:url body:[self preparedParams:params] success:^(id res){
        BOOL success = NO;
        NSString *textError;
        if ([res isKindOfClass:[NSDictionary class] ]) {
                if (res[@"data"][@"id"]){
                    User.user_id = res[@"data"][@"links"][@"user"];
                    User.user_name = res[@"data"][@"email"];
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

+(void)sendAnalysisToServer:(NSDictionary*)analysisData completion:(void (^)(BOOL success, NSError* error, id result))completion{
    NSLog(@"before = %@",analysisData);
    NSString *url = [NSString stringWithFormat:
                     @"%@%@",kServerURL,kTestResults];
    
    NSLog(@"after = %@",[self setupNulls:analysisData]);
    
    [self sendCommonPOST:url body:[self preparedParams: analysisData] success:^(id result){
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


+(void)sendCommonPOST:(NSString*)urlStr params:(NSString*)HTMLStr success:(void (^)(id))successIm{
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
    if (User.access_token.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }

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
    if (User.access_token.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }

    NSURL *url = [NSURL URLWithString:[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    
    NSMutableURLRequest *urlRequest=[NSMutableURLRequest requestWithURL:url
                                                            cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                        timeoutInterval:30.0];
    urlRequest.HTTPMethod = @"POST";
    urlRequest.HTTPBody = body;
    
    [urlRequest setValue:@"application/vnd.api+json" forHTTPHeaderField: @"Content-Type"];

    
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
                                       NSString *err = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
                                       NSLog(@"err = %@",err);
                                       call_completion_block(successIm, [NSMutableDictionary new]);
                                       
                                   }
                               }
                               
                               
                           }];
    
}



+(void)sendCommon:(NSString*)urlStr success:(void (^)(id))successIm{
    if (User.access_token.length>0){
        urlStr = [NSString stringWithFormat:@"%@?token=%@",urlStr,User.access_token];
    }

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

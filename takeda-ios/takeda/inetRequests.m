//
//  inetRequests.m
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//







#import "inetRequests.h"

@implementation inetRequests


+(void)authUserWithLogin:(NSString*)login
                password:(NSString*)password
              completion:(void (^)(BOOL result, NSError* error))completion
{
    ShowNetworkActivityIndicator();
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSError*err = nil;
        NSError*_e = nil;
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:
                                           @"%@/oauth/v2/token?"
                                           "&client_id=%@"
                                           "&client_secret=%@"
                                           "&grant_type=password"
                                           "&username=%@"
                                           "&password=%@",api_url,client_id,client_secret,login,password]];
        
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url
                                                               cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                           timeoutInterval:60.0];
        
        NSHTTPURLResponse *response=nil;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        
        
        
        if (!err) {
            id resp = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&_e];
            
            BOOL success = NO;
            
            if ([resp hasKey:@"access_token"]) {
                [[UserData sharedObject] setAccessToken:[resp objectForKey:@"access_token"]];
                success = YES;
            }
            dispatch_sync(dispatch_get_main_queue(), ^{
                HideNetworkActivityIndicator();
                NSLog(@"error connection %@",err);
                if (completion) {
                    completion(success, nil);
                }
            });
            
            
        }else{
            dispatch_sync(dispatch_get_main_queue(), ^{
                HideNetworkActivityIndicator();
                NSLog(@"error connection %@",err);
                if (completion) {
                    completion(NO, nil);
                }
            });
            
            
        }
        
        
        
    });
    
    
    
    
}


+(void)getUserDataWithCompletion:(void (^)(BOOL result, NSError* error))completion
{
    ShowNetworkActivityIndicator();
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        BOOL success = NO;
        if ([[UserData sharedObject] is_authorized]) {
            NSError*err = nil;
            NSError*_e = nil;
            NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:
                                               @"%@/api/users/me?access_token=%@",
                                               api_url,
                                               [[UserData sharedObject] getAccessToken]]];
            
            NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url
                                                                   cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                               timeoutInterval:20.0];
            
            NSHTTPURLResponse *response=nil;
            NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
            if (!err) {
                id resp = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&_e];
                
                if ([resp hasKey:@"id"]) {
                    [[UserData sharedObject] setUserData:resp];
                    success = YES;
                }
                
            }else{
                NSLog(@"error connection %@",err);
            }
        }
        
        dispatch_sync(dispatch_get_main_queue(), ^{
            HideNetworkActivityIndicator();
            if (completion) {
                completion(success, nil);
            }
        });


    });
    


    

}


+(void)registrationUserWithData:(NSDictionary*)params  completion:(void (^)(BOOL result, NSError* error))completion
{
    ShowNetworkActivityIndicator();
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        BOOL success = NO;
        
        
        NSError*err = nil;
        NSError*_e = nil;
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:
                                           @"%@/api/users",
                                           api_url]];
        
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url
                                                               cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                           timeoutInterval:20.0];
        
        
        [request setHTTPMethod:@"POST"];
        [request setValue:@"application/json" forHTTPHeaderField: @"Content-Type"];
        NSMutableData *body = [NSMutableData data];
        [body appendData:[[params toJSONString] dataUsingEncoding:NSUTF8StringEncoding]];
        [request setHTTPBody:body];
        
        
        
        
        NSHTTPURLResponse *response=nil;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        if (!err) {
            id resp = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&_e];
            
            NSLog(@"%@",[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]);
            
            
            if ([resp isKindOfClass:[NSDictionary class] ]) {
                if ([resp hasKey:@"id"]) {
                    [[UserData sharedObject] setUserData:resp];
                    success = YES;
                }else{
                    err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"Ошибка при регистрации": resp}];
                }
            }else{
                if (!resp) {
                    resp = @"Ошибка регистрации";
                }
                err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"error": resp}];
            }
        }else{
            NSLog(@"error connection %@",err);
        }

        
        dispatch_sync(dispatch_get_main_queue(), ^{
            HideNetworkActivityIndicator();
            if (completion) {
                completion(success, nil);
            }
        });
    });
    

    
}






+(void)sendAnalysisToServer:(NSDictionary*)analysisData completion:(void (^)(BOOL result, NSError* error))completion{
    ShowNetworkActivityIndicator();
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        BOOL success = NO;
        
        
        NSError*err = nil;
        NSError*_e = nil;
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:
                                           @"%@/api/test-results/",
                                           api_url]];
        
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url
                                                               cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                           timeoutInterval:20.0];
        
        
        [request setHTTPMethod:@"POST"];
        
        NSString *data = @"{\"testResult\":{\"growth\":\"109\",\"heartAttackOrStroke\":\"0\",\"acetylsalicylicDrugs\":\"0\",\"sex\":\"male\",\"extraSalt\":\"1\",\"cholesterolDrugs\":\"0\",\"diabetes\":\"0\",\"smoking\":\"0\",\"weight\":\"89\",\"birthday\":\"2000-04-29T00:00:00+0400\",\"arterialPressure\":\"114\",\"cholesterolLevel\":\"6.0\",\"physicalActivity\":\"94\"}}";
        

        NSString *token = [NSString stringWithFormat:@"%@:%@",[[UserData sharedObject] getUserName],[[UserData sharedObject] getUserPassword]];
        token = [token base64String:token];
        token = @"dmFzamFrMDA0QHlhbmRleC5ydToxMjM0NTY=";
        
        
        [request setValue:[NSString stringWithFormat:@"Basic %@",token] forHTTPHeaderField: @"Authorization"];
        
        [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
  
        NSMutableData *body = [NSMutableData data];
        //[body appendData:[[analysisData toJSONString] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[data dataUsingEncoding:NSUTF8StringEncoding]];
        
        [request setHTTPBody:body];
        
        
        
        NSHTTPURLResponse *response=nil;
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&err];
        if (!err) {
            id resp = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&_e];
            
            NSLog(@"%@",[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]);
            
            
            if ([resp isKindOfClass:[NSDictionary class] ]) {
                success = YES;
                [[UserData sharedObject] saveAnalisRiskData:responseData];
            }else{
                if (!resp) {
                    resp = @"Ошибка регистрации";
                }
                err = [NSError errorWithDomain:@"com.takeda" code:1 userInfo:@{@"error": resp}];
            }
        }else{
            NSLog(@"error connection %@",err);
        }
        
        
        dispatch_sync(dispatch_get_main_queue(), ^{
            HideNetworkActivityIndicator();
            if (completion) {
                completion(success, nil);
            }
        });
    });
}


@end

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
    
    HideNetworkActivityIndicator();
    
    if (!err) {
        id resp = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&_e];
        
        BOOL success = NO;
        
        if ([resp hasKey:@"access_token"]) {
            [[UserData sharedObject] setAccessToken:[resp objectForKey:@"access_token"]];
            success = YES;
        }
        if (completion) {
            completion(success, nil);
        }
    }else{
        NSLog(@"error connection %@",err);
        if (completion) {
            completion(NO, nil);
        }
    }
}


+(void)getUserDataWithCompletion:(void (^)(BOOL result, NSError* error))completion
{
    ShowNetworkActivityIndicator();
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
                                                           timeoutInterval:60.0];
        
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
    HideNetworkActivityIndicator();
    if (completion) {
        completion(success, nil);
    }

}











@end

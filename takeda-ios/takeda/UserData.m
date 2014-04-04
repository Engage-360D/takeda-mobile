//
//  UserData.m
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "UserData.h"

@implementation UserData
static UserData *objectInstance = nil;

+(UserData*)sharedObject{
    @synchronized(self){
        if (!objectInstance) {
            objectInstance = [UserData new];
        }
        return objectInstance;
    }
}


-(BOOL)is_authorized{
    if ([oauthToken isEqual:[NSNull null]] || !oauthToken) {
        return NO;
    }
    return YES;
}

-(NSString*)getAccessToken{
    return oauthToken;
}

-(void)setAccessToken:(NSString*)token{
    oauthToken = token;
}


-(NSDictionary*)getUserData{
    return userData;
}

-(void)setUserData:(NSDictionary*)user_data{
    userData = user_data;
}







@end

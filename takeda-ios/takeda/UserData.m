//
//  UserData.m
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "UserData.h"
#import "jsonInFile.h"


@implementation UserData
static UserData *objectInstance = nil;

@synthesize access_token = access_token_;
@synthesize userData = userData_;
@synthesize user_id = user_id_;
@synthesize user_name = user_name_;

+(UserData*)sharedObject{
    @synchronized(self){
        if (!objectInstance) {
            objectInstance = [UserData new];
        }
        return objectInstance;
    }
}


-(BOOL)is_authorized{
    if ([access_token_ isEqual:[NSNull null]] || !access_token_) {
        return NO;
    }
    return YES;
}

-(NSString*)getAccessToken{
    return access_token_;
}

-(void)setAccessToken:(NSString*)token{
    access_token_ = token;
}

-(NSMutableDictionary*)getUserData{
    return userData_;
}

-(void)setUserData:(NSMutableDictionary*)user_data{
    userData_ = user_data;
}

-(void)savePassword:(NSString*)pass{
    [UserDefaults setObject:pass forKey:@"pass"];
}
-(void)saveUserName:(NSString*)username{
    [UserDefaults setObject:username forKey:@"username"];
}

-(void)saveAnalisRiskData:(NSData*)data{
    [jsonInFile writeJsonToFile:data fileName:@"riskAnalis"];
}

-(id)getLastSavedAnalisRiskData{
    return [jsonInFile getDataFromFile:@"riskAnalis"];
}

-(void)updateUser:(NSString*)login userInfo:(NSMutableDictionary*)userInfo accessToken:(NSString*)access_token{
    self.user_id = userInfo[@"id"];
    self.access_token = access_token;
    self.userData = userInfo;
    [jsonInFile createUser:login userInfo:userInfo accessToken:access_token];
}

-(NSMutableDictionary*)getUserInfo:(NSString*)login{
    NSMutableDictionary *userInfo = [jsonInFile getUserInfo:login];
    return userInfo;
}

-(void)setCurrentUser:(NSString*)login{
    self.user_name = login;
    [UserDefaults setValue:login forKey:@"lastUser"];
    NSMutableDictionary *usData = [self getUserInfo:login];
    if (usData){
        self.userData = usData;
        if (self.userData[@"access_token"]) self.access_token = self.userData[@"access_token"];
        if (self.userData[@"id"]) self.user_id = self.userData[@"id"];
    }
}

-(NSString*)getLastUser{
    return [UserDefaults valueForKey:@"lastUser"];
}

@end

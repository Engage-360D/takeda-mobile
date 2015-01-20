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

@synthesize oauthToken = oauthToken_;
@synthesize userData = userData_;

+(UserData*)sharedObject{
    @synchronized(self){
        if (!objectInstance) {
            objectInstance = [UserData new];
        }
        return objectInstance;
    }
}


-(BOOL)is_authorized{
    if ([oauthToken_ isEqual:[NSNull null]] || !oauthToken_) {
        return NO;
    }
    return YES;
}

-(NSString*)getAccessToken{
    return oauthToken_;
}

-(void)setAccessToken:(NSString*)token{
    oauthToken_ = token;
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

-(NSString*)getUserPassword{
    return [UserDefaults objectForKey:@"pass"];
}

-(NSString*)getUserName{
    return [UserDefaults objectForKey:@"username"];
}


-(void)saveAnalisRiskData:(NSData*)data{
    [jsonInFile writeJsonToFile:data fileName:@"riskAnalis"];
}
-(id)getLastSavedAnalisRiskData{
    return [jsonInFile getDataFromFile:@"riskAnalis"];
}


@end

//
//  UserData.h
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "jsonInFile.h"

typedef enum {
    tUser = 1,
    tDoctor = 2,
} UserType;

@interface UserData : NSObject{
    NSString *access_token_;
    NSString *user_id_;
    NSString *user_name_;
    NSMutableDictionary *userData_;
}
+(UserData*)sharedObject;

@property (nonatomic, strong) NSString *user_id;
@property (nonatomic, strong) NSString *user_name;
@property (nonatomic, strong) NSString *access_token;
@property (nonatomic, strong) NSMutableDictionary *userData;
@property (nonatomic) UserType userType;


-(BOOL)is_authorized;
    
-(void)setAccessToken:(NSString*)token;

-(NSDictionary*)getUserData;
-(void)setUserData:(NSMutableDictionary*)userData;

-(void)savePassword:(NSString*)pass;
-(void)saveUserName:(NSString*)username;

-(void)saveAnalisRiskData:(NSData*)data;
-(id)getLastSavedAnalisRiskData;

-(void)updateUser:(NSString*)login userInfo:(NSMutableDictionary*)userInfo accessToken:(NSString*)access_token;
-(NSMutableDictionary*)getUserInfo:(NSString*)login;
-(void)setCurrentUser:(NSString*)login;
-(NSString*)getLastUser;






@end

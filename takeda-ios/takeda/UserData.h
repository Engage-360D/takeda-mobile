//
//  UserData.h
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "jsonInFile.h"
#import "Synchronizer.h"

//typedef enum {
//    tUser = 1,
//    tDoctor = 2,
//} UserType;

@interface UserData : NSObject{
//    NSString *access_token_;
//    NSString *user_id_;
//    NSString *user_name_;
    NSMutableDictionary *userData_;
}
+(UserData*)sharedObject;

@property (nonatomic, strong) NSString *user_id;
@property (nonatomic, strong) NSString *user_login;
@property (nonatomic, strong) NSString *access_token;
@property (nonatomic, strong) NSMutableDictionary *userData;
@property (nonatomic, strong) NSArray *userRoles;

@property (nonatomic, strong) NSMutableArray *incidents;



-(void)setupUserDefaultsForUser:(NSString*)user_email;

-(BOOL)is_authorized;

//-(NSDictionary*)getUserData;
//-(void)setUserData:(NSMutableDictionary*)userData;

-(void)savePassword:(NSString*)pass;
-(void)saveUserName:(NSString*)username;
-(BOOL)checkForRole:(UserType)role;

-(void)updateUser:(NSString*)login userInfo:(NSMutableDictionary*)userInfo accessToken:(NSString*)access_token;
-(NSMutableDictionary*)getUserInfo:(NSString*)login;
-(void)setCurrentUser:(NSString*)login;
-(NSString*)getLastUser;
-(void)logoutUser;
-(void)saveIncidents;





@end

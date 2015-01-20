//
//  UserData.h
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface UserData : NSObject{
    NSString *oauthToken_;
    NSMutableDictionary *userData_;
}
+(UserData*)sharedObject;

@property (nonatomic, strong) NSString *oauthToken;
@property (nonatomic, strong) NSMutableDictionary *userData;


-(BOOL)is_authorized;
    
-(NSString*)getAccessToken;
-(void)setAccessToken:(NSString*)token;

-(NSDictionary*)getUserData;
-(void)setUserData:(NSMutableDictionary*)userData;

-(void)savePassword:(NSString*)pass;
-(void)saveUserName:(NSString*)username;

-(NSString*)getUserPassword;
-(NSString*)getUserName;

-(void)saveAnalisRiskData:(NSData*)data;
-(id)getLastSavedAnalisRiskData;







@end

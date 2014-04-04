//
//  UserData.h
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface UserData : NSObject{
    NSString *oauthToken;
    NSDictionary *userData;
}
+(UserData*)sharedObject;

-(BOOL)is_authorized;
    
-(NSString*)getAccessToken;
-(void)setAccessToken:(NSString*)token;

-(NSDictionary*)getUserData;
-(void)setUserData:(NSDictionary*)userData;
@end

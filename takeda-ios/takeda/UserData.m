//
//  UserData.m
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "UserData.h"
#import "jsonInFile.h"
#import "NotifPlanner.h"

@implementation UserData
static UserData *objectInstance = nil;

//@synthesize access_token = access_token_;
//@synthesize user_id = user_id_;
//@synthesize user_name = user_name_;

//@synthesize access_token;
//@synthesize user_id;
//@synthesize user_login;

@synthesize userData = userData_;

+(UserData*)sharedObject{
    @synchronized(self){
        if (!objectInstance) {
            objectInstance = [UserData new];
        }
        return objectInstance;
    }
}

+(void)resetData{
    objectInstance = nil;
}

+(void)saveBasicalUserInfo:(NSMutableDictionary*)userInfo{
    NSArray *fields = [userInfo allKeys]; // @[@"email", @"access_token", @"id", @"roles", @"", @""];
    NSMutableDictionary* dict = [NSMutableDictionary new];

    for (NSString *field in fields){
        if (userInfo[field]){
            [dict setObject:userInfo[field] forKey:field];
        }
    }
    
    if (dict){
        [[NSUserDefaults standardUserDefaults] setValue:dict[@"email"] forKey:@"lastUser"];
        [dict saveTofile:userSettingsFile];
    }

    
}

-(void)setupUserDefaultsForUser:(NSString*)user_email{
    [UserDefaults removeObjectForKey:@"incidents"];
    [UserDefaults removeObjectForKey:@"access_token"];
    [UserDefaults removeObjectForKey:@"lastUser"];
    [UserDefaults removeObjectForKey:uKey(@"lastResultDataId")];
    [UserDefaults removeObjectForKey:@"ISP"];
    [self resetDefaultsForKeySuffix:aKey(@"")];
    
}

-(void)resetDefaultsForKeySuffix:(NSString*)suffix{
    NSArray *allKeys = [UserDefaults dictionaryRepresentation].allKeys;

    for (id key in allKeys){
        if ([key isKindOfClass:[NSString class]]){
            if ([key hasSuffix:suffix]){
                [UserDefaults removeObjectForKey:key];
            }
        }
    }
    
}

-(BOOL)userBlocked{
    if ([self checkForRole:tDoctor]) return NO;
    
//    for (NSString *key in [self incidents].allKeys){
//        if ([[self incidents][key] boolValue]){
//            return YES;
//        }
//    }

    for (id incidentValue in [self incidents].allValues){
        if ([incidentValue boolValue]){
            return YES;
        }
    }
    NSMutableArray *allResults = [GlobalData resultAnalyses];
    if (allResults.count>0){
        NSMutableDictionary *results_data;
        results_data = [allResults lastObject];
        if (results_data[@"recommendations"][@"fullScreenAlert"]!=nil&&[results_data[@"recommendations"][@"fullScreenAlert"]isKindOfClass:[NSDictionary class]]){
            return YES;
        }
    }
    
    return NO;
}

-(BOOL)checkToNeedTest{
    NSDate *lastResultDate = [GlobalData lastResultDate];
    if (([[[NSDate date] dateBySubtractingMonths:1] isLaterThanDate:lastResultDate]||lastResultDate == nil)&&![User checkForRole:tDoctor]){
        // надо тест проходить
        // [self showMessage:@"Вам необходимо пройти тест" title:@""];
        return YES;
    }
    return NO;
}


-(BOOL)is_authorized{
    if ([self.access_token isEqual:[NSNull null]] || !self.access_token) {
        return NO;
    }
    return YES;
}

-(NSArray*)userRoles{
    NSArray *roles = self.userData[@"roles"];
    return roles;
}

-(BOOL)checkForRole:(UserType)role{
    for (NSString *roleStr in self.userRoles ){
        if (role == [self roleForStr:roleStr]){
            return YES;
        }
    }
    return NO;
}

-(NSMutableDictionary*)incidents{
  //  [[NSUserDefaults standardUserDefaults] setObject:[NSMutableArray new] forKey:@"incidents"];
    if (!_incidents) {
        _incidents = [UserDefaults valueForKey:@"incidents"];
    }
    if (!_incidents){
        _incidents = [GlobalData incidentModel];
        [self saveIncidents];
    }
    return _incidents;
}

-(void)saveIncidents{
    [UserDefaults setValue:_incidents forKey:@"incidents"];
}

-(NSString*)user_id{
    return self.userData[@"id"];
}

-(void)setUser_id:(NSString *)the_user_id{
    [self.userData setObject:the_user_id forKey:@"id"];
}

-(NSString*)user_login{
    return self.userData[@"email"];
}

-(void)setUser_login:(NSString *)the_user_name{
    [self.userData setObject:the_user_name forKey:@"email"];
}

-(NSString*)access_token{
    return self.userData[@"access_token"];
}

//-(NSString*)getAccessToken{
//    return self.userData[@"access_token"];
//}

-(void)setAccess_token:(NSString *)token{
    [self.userData setObject:token forKey:@"access_token"];
}

//
//-(void)setAccessToken:(NSString*)token{
//    [self.userData setObject:token forKey:@"access_token"];
//}

-(NSMutableDictionary*)getUserData{
    return userData_;
}

-(void)setUserData:(NSMutableDictionary*)user_data{
    userData_ = user_data;
    [GlobalData cleanOldResults:nil];
}

-(void)updateUser:(NSString*)login userInfo:(NSMutableDictionary*)userInfo accessToken:(NSString*)accessToken{
    NSString *AccessToken = [accessToken mutableCopy];
//    self.userData = userInfo;
//    self.access_token = AccessToken;
    
   /// [jsonInFile createUser:login userInfo:userInfo accessToken:access_token];
    
    [userInfo setObject:AccessToken forKey:@"access_token"];
    if (userInfo){
        NSString *filePath = userSettingsFile;
        [userInfo saveTofile:filePath];
    }

}

-(NSMutableDictionary*)getUserInfo:(NSString*)login{
    NSString *filePath = userSettingsFile;
    NSMutableDictionary *userInfo = [NSMutableDictionary readFromFile:filePath];
    return userInfo;
}

-(void)setCurrentUser:(NSString*)login{
    if (login.length==0) {
        return;
    }
    self.user_login = login;
    [[NSUserDefaults standardUserDefaults] setValue:login forKey:@"lastUser"];
    NSMutableDictionary *usData = [self getUserInfo:login];
    if (usData){
        self.userData = usData;
      //  if (self.access_token) [self.userData setObject:self.access_token forKey:@"access_token"];
      //  if (self.userData[@"access_token"]) self.access_token = self.userData[@"access_token"];
      //  if (self.userData[@"id"]) self.user_id = self.userData[@"id"];
    }
}

-(void)synchronizeUser:(NSString*)login completition:(void (^)(BOOL success, id result))completion{
    if (login.length==0) {
        completion(NO,nil);
        return;
    }
    [[Synchronizer sharedInstance] startSynchronizeCompletition:^(BOOL successSynchr, id resultSynchr){
        if (completion!=nil){
            completion(successSynchr, nil);
        }
    }];
    
}

-(void)logoutUser{
    [self resetUser];
    self.userData = nil;
  //  [NSUserDefaults resetStandardUserDefaults];
}

-(void)resetUser{
    [self setupUserDefaultsForUser:nil];
    [GlobalData clearFiles];
    [[NotifPlanner sharedInstance] deleteAllNotifications];
}

-(NSString*)getLastUser{
    return [[NSUserDefaults standardUserDefaults] valueForKey:@"lastUser"];
}

-(UserType)roleForStr:(NSString*)roleStr{
    if ([roleStr isEqualToString:@"ROLE_USER"]) return tUser;
    if ([roleStr isEqualToString:@"ROLE_DOCTOR"]) return tDoctor;
    return tUser;
}

@end

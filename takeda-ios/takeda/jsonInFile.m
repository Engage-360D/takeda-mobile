//
//  jsonInFile.m
//  Copyright (c) 2014 CPCS. All rights reserved.
//

#import "jsonInFile.h"

@implementation jsonInFile


+(void)removeOldData:(NSString*)path{
    if ([[NSFileManager defaultManager] fileExistsAtPath:path]){
        [[NSFileManager defaultManager] removeItemAtPath:path error:nil];
    }
    
}

+ (void)writeJsonToFile:(NSData*)data fileName:(NSString*)fileName{
    if (data){
        NSString *filePath = [NSString stringWithFormat:@"%@/%@.json", [Path JSONFolder],fileName];
        [self removeOldData:filePath];
        [data writeToFile:filePath atomically:YES];
    }
}

+(id)getDataFromFile:(NSString*)fileName{
    NSError *jsonError = nil;
    NSString *filePath = [NSString stringWithFormat:@"%@/%@.json", [Path JSONFolder],fileName];
    NSData *jsonData = [NSData dataWithContentsOfFile:filePath options:kNilOptions error:&jsonError ];
    if (jsonData) {
        return [NSJSONSerialization JSONObjectWithData:jsonData
                                               options:0 error:nil];
    }else{
        return nil;
    }
    
}


+(void)removeAllData{
    NSFileManager *fm = [NSFileManager defaultManager];
    NSString *directory = [Path JSONFolder];
    NSError *error = nil;
    for (NSString *file in [fm contentsOfDirectoryAtPath:directory error:&error]) {
        BOOL success = [fm removeItemAtPath:[NSString stringWithFormat:@"%@/%@", directory, file] error:&error];
        if (!success || error) {
            // it failed.
        }
    }
}

+(void)createUser:(NSString*)login userInfo:(NSMutableDictionary*)userInfo accessToken:(NSString*)access_token{
    NSMutableDictionary *user = [userInfo mutableCopy];
    [user setObject:access_token forKey:@"access_token"];
    
    if (userInfo){
        NSString *filePath = [NSString stringWithFormat:@"%@/%@.json", [Path UsersFolder],login];
        [self removeOldData:filePath];
        [user writeToFile:filePath atomically:YES];
    }
}

+(NSMutableDictionary*)getUserInfo:(NSString*)login{
    NSError *jsonError = nil;
    NSString *filePath = [NSString stringWithFormat:@"%@/%@.json", [Path UsersFolder],login];
    NSData *jsonData = [NSData dataWithContentsOfFile:filePath options:kNilOptions error:&jsonError ];
    if (jsonData) {
        NSMutableDictionary *info = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
        return info;
    } else{
        return nil;
    }
}

@end

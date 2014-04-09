//
//  jsonInFile.m
//  Copyright (c) 2014 CPCS. All rights reserved.
//

#import "jsonInFile.h"

@implementation jsonInFile




+(NSString*)jsonPath{
    return [LIBRARY stringByAppendingPathComponent:@"Private Documents/JSON"];
}

+(void)removeOldData:(NSString*)path{
    if ([[NSFileManager defaultManager] fileExistsAtPath:path]){
        [[NSFileManager defaultManager] removeItemAtPath:path error:nil];
    }
    
}

+ (void)writeJsonToFile:(NSData*)data fileName:(NSString*)fileName{
    if (data){
        NSString *filePath = [NSString stringWithFormat:@"%@/%@.json", [self jsonPath],fileName];
        [self removeOldData:filePath];
        [data writeToFile:filePath atomically:YES];
    }
}

+(id)getDataFromFile:(NSString*)fileName{
    NSError *jsonError = nil;
    NSString *filePath = [NSString stringWithFormat:@"%@/%@.json", [self jsonPath],fileName];
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
    NSString *directory = [self jsonPath];
    NSError *error = nil;
    for (NSString *file in [fm contentsOfDirectoryAtPath:directory error:&error]) {
        BOOL success = [fm removeItemAtPath:[NSString stringWithFormat:@"%@/%@", directory, file] error:&error];
        if (!success || error) {
            // it failed.
        }
    }
}

@end

//
//

#import "Path.h"
#include <sys/xattr.h>


@implementation Path


+(NSFileManager*)manager{
    return [NSFileManager defaultManager];
}


+(void)checkDirectories{
    [self createPrivateDocuments];
    [self createDBFolder];
    [self createFilesFolder];
    [self createJSONFolder];
    [self createJResultsFolder];
    [self createUsersFolder];
}


+(void)createPrivateDocuments{
    NSString*path = [LIBRARY stringByAppendingPathComponent:@"Private Documents"];
    if (![[self manager] fileExistsAtPath:path]) {
        [[self manager] createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        [self addSkipBackupAttributeToItemAtURL:[NSURL fileURLWithPath:path]];
    }
}

+(void)createDBFolder{
    NSString*path = [LIBRARY stringByAppendingPathComponent:@"Private Documents/DataBase"];
    if (![[self manager] fileExistsAtPath:path]) {
        [[self manager] createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        [self addSkipBackupAttributeToItemAtURL:[NSURL fileURLWithPath:path]];
    }
}

+(void)createFilesFolder{
    NSString*path = [LIBRARY stringByAppendingPathComponent:@"Private Documents/Files"];
    if (![[self manager] fileExistsAtPath:path]) {
        [[self manager] createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        [self addSkipBackupAttributeToItemAtURL:[NSURL fileURLWithPath:path]];
    }
}

+(void)createJSONFolder{
    NSString*path = [LIBRARY stringByAppendingPathComponent:@"Private Documents/JSON"];
    if (![[self manager] fileExistsAtPath:path]) {
        [[self manager] createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        [self addSkipBackupAttributeToItemAtURL:[NSURL fileURLWithPath:path]];
    }
}

+(void)createJResultsFolder{
    NSString*path = [LIBRARY stringByAppendingPathComponent:@"Private Documents/JSON/Results"];
    if (![[self manager] fileExistsAtPath:path]) {
        [[self manager] createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        [self addSkipBackupAttributeToItemAtURL:[NSURL fileURLWithPath:path]];
    }
}

+ (BOOL)addSkipBackupAttributeToItemAtURL:(NSURL*)URL {
    const char* filePath = [[URL path] fileSystemRepresentation];
    const char* attrName = "com.apple.MobileBackup";
    u_int8_t attrValue = 1;
    int result = setxattr(filePath, attrName, &attrValue, sizeof(attrValue), 0, 0);
    return result == 0;
}

+(void)createUsersFolder{
    NSString*path = [LIBRARY stringByAppendingPathComponent:@"Private Documents/USERS"];
    if (![[self manager] fileExistsAtPath:path]) {
        [[self manager] createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        [self addSkipBackupAttributeToItemAtURL:[NSURL fileURLWithPath:path]];
    }
}

//
+(NSString*)PrivateDocuments{
    return [LIBRARY stringByAppendingPathComponent:@"Private Documents"];
}

+(NSString*)DBFolder{
    return [LIBRARY stringByAppendingPathComponent:@"Private Documents/DataBase"];
}

+(NSString*)FilesFolder{
    return [LIBRARY stringByAppendingPathComponent:@"Private Documents/Files"];
}

+(NSString*)JSONFolder{
    return [LIBRARY stringByAppendingPathComponent:@"Private Documents/JSON"];
}

+(NSString*)JResultsFolder{
    return [LIBRARY stringByAppendingPathComponent:@"Private Documents/JSON/Results"];
}

+(NSString*)UsersFolder{
    return [LIBRARY stringByAppendingPathComponent:@"Private Documents/USERS"];
}





@end


#import <Foundation/Foundation.h>
@interface NSString (Extended)
-(NSString *)urlEncode;
-(NSString *)md5;
-(NSString*)md5FileName;
-(bool)isContainSubstring:(NSString*)_subtring;
-(NSString*)extractFilename;
-(NSString*)emptyForNil;
-(NSDictionary*)dictionaryWithLocalJSONPathString;
-(NSDictionary*)dictionaryWithRemoteJSONPathString;
-(NSURL*)toURL;
-(NSString*)toCachePath;
-(NSString*)toTmpPath;
-(NSString*)toDocuments;
-(NSString*)append:(NSString*)param, ...;
+(NSString*)dateNow;
-(NSString*)newTMPWithExtension;
-(NSString*)newUniqueFileWithExtension;
+(NSString*)guid;
-(NSDate*)toDateWithFormat:(NSString*)format;
-(CGFloat)getTextHeightForFont:(UIFont*)font forWidth:(CGFloat)rect_width;
- (NSString *)base64String:(NSString *)strl;
@end

@interface UIColor (Extended)
-(NSString *) toWeb;
@end

@interface NSDictionary (Extended)
-(NSData*)toJSON;
-(NSString*)toJSONString;
-(NSData*)toData;
@end

@interface NSDate (Extended)
-(BOOL)isEarlyLast:(NSInteger)days;
-(NSDate*)maxDate:(NSDate*)compareDate;
- (NSString *) toStringWithFormat:(NSString*)format;
@end

@interface NSData (Extended)
-(NSDictionary*)toDictionary;
-(NSString*)toStringUTF8;
@end

@interface UIImage (Extended)
- (UIImage *) toGrayscale;
@end


@interface NSIndexPath (Extended)
-(NSUInteger)hash;
@end

@interface UIView (Extended)
-(UIView*)clone;
@end

@interface NSArray (Extended)
-(NSArray*)findObjectWithKey:(NSString*)key Value:(NSObject*)Value;
-(NSObject*)findObjectFirstWithKey:(NSString*)key  Value:(NSObject*)Value;
-(BOOL)hasObjectWithKey:(NSString*)key Value:(NSObject*)Value;
@end
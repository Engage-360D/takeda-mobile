
#import "NSTypeExtended.h"
#import <CommonCrypto/CommonDigest.h>



@implementation NSString (Extended)

-(CGFloat)getTextHeightForFont:(UIFont*)font forWidth:(CGFloat)rect_width
{
    CGFloat minHeight = font.pointSize+4 ;
    CGSize size = [self sizeWithFont:font constrainedToSize:CGSizeMake(rect_width, CGFLOAT_MAX) lineBreakMode:NSLineBreakByWordWrapping];
    NSLog(@"reqest width = %f width = %f, height = %f",rect_width, size.width, size.height);
    CGFloat tmp =size.height+50;
    CGFloat rowHeight = MAX(tmp, minHeight);
    return rowHeight;
}

-(NSString *)urlEncode{
	return (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(NULL,
                                                               (CFStringRef)self,
                                                               NULL,
                                                               (CFStringRef)@"!*'\"();:@&=+$,/?%#[]% ",
                                                               CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding)));
}

-(NSString *) md5 {
    const char *cStr = [self UTF8String];
    unsigned char result[16];
    //CC_MD5( cStr, strlen(cStr), result );
    CC_MD5( cStr, (int)strlen(cStr), result );
    return [NSString stringWithFormat:
            @"%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X",
            result[0], result[1], result[2], result[3], 
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]
            ];
}

-(NSString*)md5FileName
{
    NSString*fileName = [self lastPathComponent];            
    return [[fileName md5] stringByAppendingString:[NSString stringWithFormat:@".%@",[fileName pathExtension]]];
}
-(bool)isContainSubstring:(NSString*)_subtring
{
    if (self==nil || [self length]==0 || _subtring==nil || [_subtring length]==0 ) {
        return false;
    }else if ([[self lowercaseString] rangeOfString:_subtring].location != NSNotFound){
        return true;
    }else
    return false;

}


-(NSString*)extractFilename{
    return [[self lastPathComponent] stringByDeletingPathExtension];
}

-(NSString*)emptyForNil
{
    return self==nil?@"-":self;
}

-(NSDictionary*)dictionaryWithLocalJSONPathString
{
    NSData* data = [NSData dataWithContentsOfFile:self ];
    __autoreleasing NSError* error = nil;
    id result = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
    if (error != nil)
    {
        NSLog(@"dictionaryWithLocalJSONPathString\n%@ \n%@",self,error);
        return nil;
    }
    return result;
}

-(NSDictionary*)dictionaryWithRemoteJSONPathString
{
    NSData* data = [NSData dataWithContentsOfURL:[NSURL URLWithString: self] ];
    __autoreleasing NSError* error = nil;
    id result = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
    if (error != nil)
    {
        NSLog(@"dictionaryWithRemoteJSONPathString\n%@ \n%@",self,error);
        return nil;
    }
    return result;
}

-(NSString*)toCachePath
{
    //NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *cachePath = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    return [cachePath stringByAppendingPathComponent:self];
}

-(NSString*)toTmpPath
{
    return [NSTemporaryDirectory() stringByAppendingPathComponent:self];
}

-(NSString*)toDocuments
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *basePath = ([paths count] > 0) ? (NSString *)[paths objectAtIndex:0] : nil;
    return [basePath stringByAppendingPathComponent:self];
}

-(NSURL*)toURL
{

        NSURL *     result;
        NSString *  trimmedStr;
        NSRange     schemeMarkerRange;
        NSString *  scheme;
        
        assert(self != nil);
        
        result = nil;
        
        trimmedStr = [self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        if ( (trimmedStr != nil) && (trimmedStr.length != 0) ) {
            schemeMarkerRange = [trimmedStr rangeOfString:@"://"];
            
            if (schemeMarkerRange.location == NSNotFound) {
                result = [NSURL URLWithString:[NSString stringWithFormat:@"http://%@", trimmedStr]];
            } else {
                scheme = [trimmedStr substringWithRange:NSMakeRange(0, schemeMarkerRange.location)];
                assert(scheme != nil);
                
                if ( ([scheme compare:@"http"  options:NSCaseInsensitiveSearch] == NSOrderedSame)
                    || ([scheme compare:@"https" options:NSCaseInsensitiveSearch] == NSOrderedSame) ) {
                    result = [NSURL URLWithString:trimmedStr];
                } else {
                    // It looks like this is some unsupported URL scheme.
                }
            }
        }
        
        return result;
}

-(NSString*)append:(NSString*)param, ...
{
    NSString* rez = self;
    id eachObject;
    va_list argumentList;
    if (param)
    {
        rez = [rez stringByAppendingString:param];
        va_start(argumentList, param);
        while ((eachObject = va_arg(argumentList, NSString*)))
            rez = [rez stringByAppendingString:eachObject];
        va_end(argumentList);
    }
    return rez;
}

+(NSString*)dateNow
{
    NSDate* date = [NSDate date];
    NSDateFormatter *dateFormatter =  [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"y-MM-d'T'HH:mm:ss.SSS'Z'"];
    [dateFormatter setLocale:[[NSLocale alloc] initWithLocaleIdentifier:@"en_US_POSIX"]];
    NSString *datastring = [dateFormatter stringFromDate:date];
    //NSLog(@"todayString: %@", datastring);
    dateFormatter = nil;
    return datastring;
}

-(NSDate*)toDateWithFormat:(NSString*)format
{
    NSDateFormatter *dateFormatter = [NSDateFormatter new];
    [dateFormatter setDateFormat:format];
    return [dateFormatter dateFromString:self];
}

-(NSString*)newTMPWithExtension
{
    NSString*rez = [[NSString dateNow] toTmpPath];
    return [rez stringByAppendingPathExtension:self];
}

-(NSString*)newUniqueFileWithExtension
{
    return [[NSString dateNow] stringByAppendingPathExtension:self];
}

+(NSString*)guid
{
        NSString *  result;
        CFUUIDRef   uuid;
        CFStringRef uuidStr;
        
        uuid = CFUUIDCreate(NULL);
        assert(uuid != NULL);
        
        uuidStr = CFUUIDCreateString(NULL, uuid);
        assert(uuidStr != NULL);
        
        result = (__bridge NSString *)(uuidStr);
        assert(result != nil);
    
        CFRelease(uuidStr);
        CFRelease(uuid);
        
        return result;
}


@end

@implementation UIColor (Extended)
-(NSString *) toWeb
{
    return [NSString stringWithFormat:@"#%02X%02X%02X", (int)((CGColorGetComponents(self.CGColor))[0]*255.0), (int)((CGColorGetComponents(self.CGColor))[1]*255.0), (int)((CGColorGetComponents(self.CGColor))[2]*255.0)];
}
@end

@implementation NSDictionary (Extended)
-(NSData*)toJSON
{
    NSError* error = nil;
    id result = [NSJSONSerialization dataWithJSONObject:self options:kNilOptions error:&error];
    if (error != nil) return nil;
    return result;
}

-(NSString*)toJSONString
{
    NSError* error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:self options:kNilOptions error:&error];
    if (error != nil) return nil;
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}

-(NSData*)toData
{
//   NSMutableData *data = [NSMutableData new];
//    NSKeyedArchiver *archiver = [[NSKeyedArchiver alloc] initForWritingWithMutableData:data];
//    [archiver encodeObject:self forKey:@"pack"];
//    [archiver finishEncoding];
    return [NSKeyedArchiver archivedDataWithRootObject:self];
}

@end

@implementation NSDate (Extended)
-(BOOL)isEarlyLast:(NSInteger)days
{
    NSDate *date = [[NSDate date] dateByAddingTimeInterval: -86400.0*days];
    return ([self compare:date] == NSOrderedAscending);
    
    // All intervals taken from Google
//    NSDate *yesterday = [today dateByAddingTimeInterval: -86400.0];
//    NSDate *thisWeek  = [today dateByAddingTimeInterval: -604800.0];
//    NSDate *lastWeek  = [today dateByAddingTimeInterval: -1209600.0];

}
-(NSDate*)maxDate:(NSDate*)compareDate
{
    if ([self compare:compareDate] == NSOrderedDescending) {
        //NSLog(@"date1 is later than date2");
        return self;
    }
    else
        return compareDate;
//    
//    } else if ([date1 compare:date2] == NSOrderedAscending) {
//        NSLog(@"date1 is earlier than date2");
//        
//    } else {
//        NSLog(@"dates are the same");
//        
//    }
}
- (NSString *) toStringWithFormat:(NSString*)format{
    NSDateFormatter *dateFormatter = [NSDateFormatter new];
    [dateFormatter setDateFormat:format];
    return [dateFormatter stringFromDate:self];
}

@end


@implementation NSData (Extended)
-(NSDictionary*)toDictionary{
    return (NSDictionary*) [NSKeyedUnarchiver unarchiveObjectWithData:self];
}

-(NSString*)toStringUTF8
{
//    return [NSString st]
//NSString *jsonString =
       return [[NSString alloc] initWithData:self encoding:NSUTF8StringEncoding];
}
@end



@implementation UIImage (Extended)

- (UIImage *) toGrayscale{
    const int RED = 1;
    const int GREEN = 2;
    const int BLUE = 3;
    
    // Create image rectangle with current image width/height
    CGRect imageRect = CGRectMake(0, 0, self.size.width * self.scale, self.size.height * self.scale);
    
    int width = imageRect.size.width;
    int height = imageRect.size.height;
    
    // the pixels will be painted to this array
    uint32_t *pixels = (uint32_t *) malloc(width * height * sizeof(uint32_t));
    
    // clear the pixels so any transparency is preserved
    memset(pixels, 0, width * height * sizeof(uint32_t));
    
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    
    // create a context with RGBA pixels
    CGContextRef context = CGBitmapContextCreate(pixels, width, height, 8, width * sizeof(uint32_t), colorSpace,
                                                 kCGBitmapByteOrder32Little | kCGImageAlphaPremultipliedLast);
    
    // paint the bitmap to our context which will fill in the pixels array
    CGContextDrawImage(context, CGRectMake(0, 0, width, height), [self CGImage]);
    
    for(int y = 0; y < height; y++) {
        for(int x = 0; x < width; x++) {
            uint8_t *rgbaPixel = (uint8_t *) &pixels[y * width + x];
            
            // convert to grayscale using recommended method: http://en.wikipedia.org/wiki/Grayscale#Converting_color_to_grayscale
            uint8_t gray = (uint8_t) ((30 * rgbaPixel[RED] + 59 * rgbaPixel[GREEN] + 11 * rgbaPixel[BLUE]) / 100);
            
            // set the pixels to gray
            rgbaPixel[RED] = gray;
            rgbaPixel[GREEN] = gray;
            rgbaPixel[BLUE] = gray;
        }
    }
    
    // create a new CGImageRef from our context with the modified pixels
    CGImageRef image = CGBitmapContextCreateImage(context);
    
    // we're done with the context, color space, and pixels
    CGContextRelease(context);
    CGColorSpaceRelease(colorSpace);
    free(pixels);
    
    // make a new UIImage to return
    UIImage *resultUIImage = [UIImage imageWithCGImage:image
                                                 scale:self.scale
                                           orientation:UIImageOrientationUp];
    
    // we're done with image now too
    CGImageRelease(image);
    
    return resultUIImage;
}
@end

@implementation NSIndexPath (Extended)
-(NSUInteger)hash{
    char str[11];
    int row = (int)self.row;
    int section = (int)self.section;
    sprintf(str, "%d%d", section,row);

    unsigned int val = 0;
    char *p;
    int i;
    p = str;
    for(i = 0; p[ i ]; i++){
        if (i ==0) {
            val = (unsigned char)p[i] << CHAR_BIT;
        }
        else
            val |= (unsigned char)p[i];
    }
    return val;
}
@end

@implementation UIView (Extended)
-(UIView*)clone{
    UIView*newview = [[UIView alloc] initWithFrame:self.bounds];
    newview = self;
    return newview;
//    NSData *tmp = [NSKeyedArchiver archivedDataWithRootObject:self];
//    UIView *controller = [NSKeyedUnarchiver unarchiveObjectWithData:tmp];
//    return controller;
}
@end

@implementation NSArray (Extended)
-(NSArray*)findObjectWithKey:(NSString*)key  Value:(NSObject*)Value{
    NSPredicate *p = [NSPredicate predicateWithFormat:@"%@=%@",key,Value];
    NSArray *filtered = [self filteredArrayUsingPredicate:p];
    return filtered;
}
-(NSObject*)findObjectFirstWithKey:(NSString*)key  Value:(NSObject*)Value{
    NSArray*filtered = [self findObjectWithKey:key Value:Value];
    if (filtered) {
        return filtered[0];
    }
    return 0;
}
-(BOOL)hasObjectWithKey:(NSString*)key  Value:(NSObject*)Value
{
    NSArray*filtered = [self findObjectWithKey:key Value:Value];
    if (filtered) {
        return YES;
    }
    return NO;
}
@end

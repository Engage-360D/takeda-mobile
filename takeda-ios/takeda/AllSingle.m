    //
//  AllSingle.m
//  ALL
//
//  Created by Serg on 12/20/13.
//  Copyright (c) 2013 soft-mobile. All rights reserved.
//

#import "AllSingle.h"
#import "Path.h"

@implementation AllSingle

@synthesize database;
@synthesize DataTree;
@synthesize hideKeyb_block;
@synthesize showKeyb_block;
id lastKeybHolder;

static AllSingle *dot = nil;

+(AllSingle*) sharedInstance{
    @synchronized (self){
        if (!dot||dot==nil){
            dot = [AllSingle new];
            [dot openAll];
            }
        return dot;
    }
}

-(void)openAll{
//    database = [FMDatabase databaseWithPath:[self pathToDB]];
//    [database open];
//
    
    self.hideKeyb_block = ^{
        // NSLog(@"Open");
        [dot globalHideKeyb];
    };
    self.showKeyb_block = ^{
        //   NSLog(@"Close");
        [dot globalShowKeyb];
    };
}

-(void)globalHideKeyb{
    if (self.keybHolder){
        lastKeybHolder = self.keybHolder;
        [self.keybHolder resignFirstResponder];
    }
}

-(void)globalShowKeyb{
    if (lastKeybHolder){
        [lastKeybHolder becomeFirstResponder];
    }
}

- (NSString *)pathToDB {
    NSString *dbName = @"takeda";
    NSString *originalDBPath = [[NSBundle mainBundle] pathForResource:dbName ofType:@"sqlite"];
    NSString *path = nil;
    
    NSString *dbNameDir = [LIBRARY stringByAppendingPathComponent:@"Private Documents/DataBase"]; //[NSString stringWithFormat:@"%@/Recipes", DOCUMENTS];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL isDir = NO;
    BOOL dirExists = [fileManager fileExistsAtPath:dbNameDir isDirectory:&isDir];
    NSString *dbPath = [NSString stringWithFormat:@"%@/%@.db", dbNameDir, dbName];
    if(dirExists && isDir) {
        BOOL dbExists = [fileManager fileExistsAtPath:dbPath];
        if(!dbExists) {
            NSError *error = nil;
            BOOL success = [fileManager copyItemAtPath:originalDBPath toPath:dbPath error:&error];
            if(!success) {
                //       NSLog(@"error = %@", error);
            } else {
                path = dbPath;
            }
        } else {
            path = dbPath;
        }
    } else if(!dirExists) {
        NSError *error = nil;
        BOOL success;
        
        [Path checkDirectories];
        
        success = [fileManager copyItemAtPath:originalDBPath toPath:dbPath error:&error];
        if(!success) {
            //     NSLog(@"error = %@", error);
        } else {
            path = dbPath;
        }
    }
    return path;
}

-(id) recursiveMutable:(id)object
{
	if([object isKindOfClass:[NSDictionary class]])
	{
		NSMutableDictionary* dict = [NSMutableDictionary dictionaryWithDictionary:object];
		for(NSString* key in [dict allKeys])
		{
			[dict setObject:[self recursiveMutable:[dict objectForKey:key]] forKey:key];
		}
		return dict;
	}
	else if([object isKindOfClass:[NSArray class]])
	{
		NSMutableArray* array = [NSMutableArray arrayWithArray:object];
		for(int i=0;i<[array count];i++)
		{
			[array replaceObjectAtIndex:i withObject:[self recursiveMutable:[array objectAtIndex:i]]];
		}
		return array;
	}
	else if([object isKindOfClass:[NSString class]])
		return [NSMutableString stringWithString:object];
	return object;
}

-(CLLocationCoordinate2D)locationFromString:(NSString*)string{
    double lat = 0;
    double lon = 0;
    string = [string stringByReplacingOccurrencesOfString:@" " withString:@""];
    if (contains(string, @",")){
        NSString *latS = [[string componentsSeparatedByString:@","] firstObject];
        NSString *lonS = [[string componentsSeparatedByString:@","] lastObject];
        lat = [latS doubleValue];
        lon = [lonS doubleValue];
    }
    CLLocationCoordinate2D loc = CLLocationCoordinate2DMake(lat, lon);
    return loc;
}

-(void)clearCashe{
   [[NSURLCache sharedURLCache] removeAllCachedResponses];
    for(NSHTTPCookie *cookie in [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies]) {
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] deleteCookie:cookie];
    }
}

-(NSString*)fixUrl:(NSString*)url{
    if (![url hasPrefix:@"http://"]){
        url = [NSString stringWithFormat:@"http://%@",url];
    }
    
    return url;
}

-(NSString*)dictToStr:(NSMutableDictionary*)dict{
    NSMutableString *str = [NSMutableString new];
    for (NSString *key in dict){
        [str appendFormat:@"%@=%@&",key, dict[key]];
    }
    if ([str hasSuffix:@"&"]){
        [str setString:[str substringToIndex:str.length-1]];
    }
    return str;
}

-(int)servToTimest:(NSString*)strData{
    NSDateFormatter *dateFormatter = [NSDateFormatter new];
    [dateFormatter setDateFormat:@"dd.MM.yyyy HH:mm"];
    NSDate *pubDate = [dateFormatter dateFromString:strData];
    int timestamp = [pubDate timeIntervalSince1970];
    return timestamp;
}

-(NSDate*)servToDate:(NSString*)strData{
    NSDateFormatter *dateFormatter = [NSDateFormatter new];
    [dateFormatter setDateFormat:@"dd.MM.yyyy HH:mm"];
    NSDate *pubDate = [dateFormatter dateFromString:strData];
    return pubDate;
}

-(NSDate*)servToDateTimeSec:(NSString*)strData{
    NSDateFormatter *dateFormatter = [NSDateFormatter new];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSDate *pubDate = [dateFormatter dateFromString:strData];
    return pubDate;
}

-(NSDate*) parseDate:(NSString *)dateString{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];    
    return [dateFormatter dateFromString:dateString];
}

-(NSDate*) parseTime:(NSString *)timeString{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"HH:mm:ss"];
    return [dateFormatter dateFromString:timeString];
}


-(NSDate*) parseDateTime:(NSString *)dateString{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //    formatter.locale = [NSLocale currentLocale]; // Necessary?
    dateFormatter.locale = [[NSLocale alloc] initWithLocaleIdentifier:@"ru_RU"];
    [dateFormatter setDateFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZ"];
    return [dateFormatter dateFromString:dateString];
}

-(NSString*) strDate:(NSDate *)date{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    return [dateFormatter stringFromDate:date];
}

-(NSString*) strDateTime:(NSDate *)date{
    return [date stringWithFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZ"];
}



-(void)removeSubviewsFrom:(UIView*)view{
    for (UIView *a in view.subviews){
        [a removeFromSuperview];
    }
}

- (UIColor*) randomColor{
    int r = arc4random() % 255;
    int g = arc4random() % 255;
    int b = arc4random() % 255;
    return [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:1];
}

-(int)index:(id)tValue inArray:(NSArray*)array{
    NSString *t1 = [NSString stringWithFormat:@"%@",tValue];

    for (int i = 0; i<array.count; i++){
        NSString *t2 = [NSString stringWithFormat:@"%@",array[i]];
        if ([t1 isEqualToString:t2]) {
            return i;
        }
    }
    
    return NSNotFound;
}

-(NSMutableDictionary*)values:(NSString*)values ForKeys:(NSString*)keys InArray:(NSArray*)array{
    NSMutableDictionary*dict = [NSMutableDictionary new];
    for (NSDictionary* arr_dict in array){
       if (arr_dict[values]) [dict setObject:arr_dict[values] forKey:arr_dict[keys]];
    }
    return dict;
}

-(NSMutableDictionary*)dictionaryWithValue:(id)value ForKey:(NSString*)key InArray:(NSArray*)array{

    for (NSDictionary* arr_dict in array){
        
        NSString *str1 = [NSString stringWithFormat:@"%@",arr_dict[key]];
        NSString *str2 = [NSString stringWithFormat:@"%@",value];
        BOOL eq = [str1 isEqualToString:str2];
        if (eq) return [self recursiveMutable:arr_dict];
        
//
//        if ([value isKindOfClass:[NSString class]]){
//            if ([arr_dict[key] isEqualToString:value]) return [self recursiveMutable:arr_dict];
//        } else if ([value isKindOfClass:[NSNumber class]]){
//            if ([arr_dict[key] isEqualToNumber: value]) return [self recursiveMutable:arr_dict];
//        } else {
//            if ([arr_dict[key] isEqual:value]) return [self recursiveMutable:arr_dict];
//        }
        
    }
    
    return nil;
}


#pragma mark -

- (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize {
    UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0);
    [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}


-(float)heightForImageConstWidth:(float)toWidth forSize:(CGSize)imgUrlSize{
    if (imgUrlSize.width==0||imgUrlSize.height==0) return 0;
    float imgH;
    float coef;
    coef = imgUrlSize.width/toWidth;
    imgH = imgUrlSize.height/coef;
    return imgH;
}

-(float)widthForImageConstHeight:(float)toHeight forSize:(CGSize)imgUrlSize{
    if (imgUrlSize.width==0||imgUrlSize.height==0) return 0;
    float imgW;
    float coef;
    coef = imgUrlSize.height/toHeight;
    imgW = imgUrlSize.width/coef;
    return imgW;
}

-(CGFloat)getHeight:(NSString*)text font:(UIFont*)font width:(float)width{
    CGSize maximumSizeLabel = CGSizeMake(width, 9999);
    CGSize labelSize = [text sizeWithFont:font
                        constrainedToSize:maximumSizeLabel
                            lineBreakMode:NSLineBreakByWordWrapping];
    CGFloat labelHeight = labelSize.height+1;
    return labelHeight;
}



-(CGFloat)heightTextView:(UITextView*)textView{
    UIFont *font = textView.font;
    CGSize size = CGSizeMake(textView.bounds.size.width, 1000);
    float h = [self text:textView.text sizeWithFont:font constrainedToSize:size].height;
   // NSLog(@"h = %f",h);
    return h;
}

-(CGFloat)heightLabel:(UILabel*)label{
    UIFont *font = label.font;
    CGSize size = CGSizeMake(label.bounds.size.width, CGFLOAT_MAX);
    float h = [self text:label.text sizeWithFont:font constrainedToSize:size].height;
    return h;
}

- (CGSize)text:(NSString *)text sizeWithFont:(UIFont *)font constrainedToSize:(CGSize)size
{
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 7)
    {
        CGRect frame = [text boundingRectWithSize:size
                                          options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading)
                                       attributes:@{NSFontAttributeName:font}
                                          context:nil];
        return frame.size;
    }
    else
    {
        return [text sizeWithFont:font constrainedToSize:size];
    }
}

- (CGFloat)measureHeightOfUITextView:(UITextView *)textView
{
    
    if (textView.text.length<1){
        return 0;
    }
    
    // textView.selectable = YES;
    
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 7){
       // textView.textContainerInset = UIEdgeInsetsMake(0,0,0,0);
    }
    
    if ([textView respondsToSelector:@selector(snapshotViewAfterScreenUpdates:)])
    {
        // This is the code for iOS 7. contentSize no longer returns the correct value, so
        // we have to calculate it.
        //
        // This is partly borrowed from HPGrowingTextView, but I've replaced the
        // magic fudge factors with the calculated values (having worked out where
        // they came from)
        
        CGRect frame = textView.bounds;
        
        // Take account of the padding added around the text.
        
        UIEdgeInsets textContainerInsets = textView.textContainerInset;
        UIEdgeInsets contentInsets = textView.contentInset;
        
        CGFloat leftRightPadding = textContainerInsets.left + textContainerInsets.right + textView.textContainer.lineFragmentPadding * 2 + contentInsets.left + contentInsets.right;
        CGFloat topBottomPadding = textContainerInsets.top + textContainerInsets.bottom + contentInsets.top + contentInsets.bottom;
        
        frame.size.width -= leftRightPadding;
        frame.size.height -= topBottomPadding;
        
        NSString *textToMeasure = textView.text;
        if ([textToMeasure hasSuffix:@"\n"])
        {
            textToMeasure = [NSString stringWithFormat:@"%@-", textView.text];
        }
        
        // NSString class method: boundingRectWithSize:options:attributes:context is
        // available only on ios7.0 sdk.
        
        NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
        [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
        
        NSDictionary *attributes = @{ NSFontAttributeName: textView.font, NSParagraphStyleAttributeName : paragraphStyle };
        
        CGRect size = [textToMeasure boundingRectWithSize:CGSizeMake(CGRectGetWidth(frame), MAXFLOAT)
                                                  options:NSStringDrawingUsesLineFragmentOrigin
                                               attributes:attributes
                                                  context:nil];
        
        CGFloat measuredHeight = ceilf(CGRectGetHeight(size) + topBottomPadding);
        
        return measuredHeight;//*1.27;
        
    }
    else
    {
        
        return [self heightForTextView:textView containingString:textView.text];
        //  return textView.contentSize.height;
    }
}

- (CGFloat)heightForTextView:(UITextView*)textView containingString:(NSString*)string
{
    float horizontalPadding = 24;
    float verticalPadding = 16;
    float widthOfTextView = textView.contentSize.width - horizontalPadding;
    float height = [string sizeWithFont:textView.font constrainedToSize:CGSizeMake(widthOfTextView, 999999.0f) lineBreakMode:NSLineBreakByWordWrapping].height + verticalPadding;
    
    return height;
}

- (CGFloat)textViewHeightForAttributedText:(NSAttributedString *)text andWidth:(CGFloat)width
{
    UITextView *textView = [[UITextView alloc] init];
    [textView setAttributedText:text];
    CGSize size = [textView sizeThatFits:CGSizeMake(width, FLT_MAX)];
    return size.height;
}

#pragma mark-

-(CGRect)rectView:(UIView*)theView inSuperView:(UIView*)theSuperview{
    CGRect theViewRect =  theView.frame;
    
    UIView *currentSuperview = theView;//.superview;
    while (theSuperview!=currentSuperview) {
        currentSuperview = currentSuperview.superview;
        theViewRect.origin.x += currentSuperview.frame.origin.x;
        theViewRect.origin.y += currentSuperview.frame.origin.y;
    }
    
    return theViewRect;
}


-(NSArray*)parseUrslFromString:(NSString*)string{
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:@"https?://([-\\w\\.]+)+(:\\d+)?(/([\\w/_\\.]*(\\?\\S+)?)?)?" options:NSRegularExpressionCaseInsensitive error:nil];
    
    NSArray *arrayOfAllMatches = [regex matchesInString:string options:0 range:NSMakeRange(0, [string length])];
    
    NSMutableArray *arrayOfURLs = [[NSMutableArray alloc] init];
    
    for (NSTextCheckingResult *match in arrayOfAllMatches) {
        NSString* substringForMatch = [string substringWithRange:match.range];        
        [arrayOfURLs addObject:substringForMatch];
    }
    
    // return non-mutable version of the array
    return [NSArray arrayWithArray:arrayOfURLs];
}

-(void)callPhone:(NSString*)number{
    number = [number stringByReplacingOccurrencesOfString:@" " withString:@""];
    number = [number stringByReplacingOccurrencesOfString:@"+" withString:@""];
    number = [number stringByReplacingOccurrencesOfString:@"(" withString:@""];
    number = [number stringByReplacingOccurrencesOfString:@")" withString:@""];
    number = [number stringByReplacingOccurrencesOfString:@"-" withString:@""];
    
    UIDevice *device = [UIDevice currentDevice];
    if ([[device model] isEqualToString:@"iPhone"] ) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",number]]];
    } else {
        UIAlertView *Notpermitted=[[UIAlertView alloc] initWithTitle:@"Ошибка!" message:@"Ваше устройство не поддерживает эту функцию." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [Notpermitted show];
    }
}

#pragma mark -

-(NSArray*)sortArray:(NSArray*)array byKey:(NSString*)key wayUp:(BOOL)up{
    NSSortDescriptor *brandDescriptor = [[NSSortDescriptor alloc] initWithKey:key ascending:up];
    NSArray *sortDescriptors = [NSArray arrayWithObject:brandDescriptor];
    return [NSArray arrayWithArray:[array sortedArrayUsingDescriptors:sortDescriptors]];
}

-(UIView*)superSuperView:(UIView*)view{
    UIView*supView;
    while (view.superview!=nil) {
        supView = [self superSuperView:view];
    }
    return view.superview;
}

-(NSString*)PathFromUrl:(NSString*)url{
    return [url MD5String];
}






#pragma mark Base64
-(NSString*)base64forData:(NSData*)theData {
    
    const uint8_t* input = (const uint8_t*)[theData bytes];
    NSInteger length = [theData length];
    
    static char table[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    NSMutableData* data = [NSMutableData dataWithLength:((length + 2) / 3) * 4];
    uint8_t* output = (uint8_t*)data.mutableBytes;
    
    NSInteger i;
    for (i=0; i < length; i += 3) {
        NSInteger value = 0;
        NSInteger j;
        for (j = i; j < (i + 3); j++) {
            value <<= 8;
            
            if (j < length) {
                value |= (0xFF & input[j]);
            }
        }
        
        NSInteger theIndex = (i / 3) * 4;
        output[theIndex + 0] =                    table[(value >> 18) & 0x3F];
        output[theIndex + 1] =                    table[(value >> 12) & 0x3F];
        output[theIndex + 2] = (i + 1) < length ? table[(value >> 6)  & 0x3F] : '=';
        output[theIndex + 3] = (i + 2) < length ? table[(value >> 0)  & 0x3F] : '=';
    }
    
    return [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding] ;
}



- (NSData *)base64DataFromString: (NSString *)string
{
    @try {
        
        unsigned long ixtext, lentext;
        unsigned char ch, inbuf[4], outbuf[3];
        short i, ixinbuf;
        Boolean flignore, flendtext = false;
        const unsigned char *tempcstring;
        NSMutableData *theData;
        
        if (string == nil)
        {
            return [NSData data];
        }
        
        ixtext = 0;
        
        tempcstring = (const unsigned char *)[string UTF8String];
        
        lentext = [string length];
        
        theData = [NSMutableData dataWithCapacity: lentext];
        
        ixinbuf = 0;
        
        while (true)
        {
            if (ixtext >= lentext)
            {
                break;
            }
            
            ch = tempcstring [ixtext++];
            
            flignore = false;
            
            if ((ch >= 'A') && (ch <= 'Z'))
            {
                ch = ch - 'A';
            }
            else if ((ch >= 'a') && (ch <= 'z'))
            {
                ch = ch - 'a' + 26;
            }
            else if ((ch >= '0') && (ch <= '9'))
            {
                ch = ch - '0' + 52;
            }
            else if (ch == '+')
            {
                ch = 62;
            }
            else if (ch == '=')
            {
                flendtext = true;
            }
            else if (ch == '/')
            {
                ch = 63;
            }
            else
            {
                flignore = true;
            }
            
            if (!flignore)
            {
                short ctcharsinbuf = 3;
                Boolean flbreak = false;
                
                if (flendtext)
                {
                    if (ixinbuf == 0)
                    {
                        break;
                    }
                    
                    if ((ixinbuf == 1) || (ixinbuf == 2))
                    {
                        ctcharsinbuf = 1;
                    }
                    else
                    {
                        ctcharsinbuf = 2;
                    }
                    
                    ixinbuf = 3;
                    
                    flbreak = true;
                }
                
                inbuf [ixinbuf++] = ch;
                
                if (ixinbuf == 4)
                {
                    ixinbuf = 0;
                    
                    outbuf[0] = (inbuf[0] << 2) | ((inbuf[1] & 0x30) >> 4);
                    outbuf[1] = ((inbuf[1] & 0x0F) << 4) | ((inbuf[2] & 0x3C) >> 2);
                    outbuf[2] = ((inbuf[2] & 0x03) << 6) | (inbuf[3] & 0x3F);
                    
                    for (i = 0; i < ctcharsinbuf; i++)
                    {
                        [theData appendBytes: &outbuf[i] length: 1];
                    }
                }
                
                if (flbreak)
                {
                    break;
                }
            }
        }
        
        
        return theData;
        
    } @catch  (NSException * e) {
        return nil;
    }
}

#pragma mark -



@end

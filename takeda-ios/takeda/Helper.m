//
//  Helper.m
//  takeda
//
//  Created by Serg on 4/3/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "Helper.h"

@implementation Helper

+(float)heightText:(NSString*)text withFont:(UIFont*)font withWidth:(float)width{
    CGSize constrainedSize = CGSizeMake(width  , 9999);
    
    
    if (IOS7_AND_LATER) {
        CGRect textRect = [text boundingRectWithSize:constrainedSize
                                             options:NSStringDrawingUsesLineFragmentOrigin
                                          attributes:@{NSFontAttributeName:font}
                                             context:nil];
        CGSize size = textRect.size;
        return size.height;
    }else{
        ////////// - для ios < 7
        /*CGSize labelSize = [text sizeWithFont:font
         constrainedToSize:constrainedSize
         lineBreakMode:NSLineBreakByWordWrapping];
         
         return labelSize.height;*/
        return 0;
    }
    
    

    

    
}

+(void)fastAlert:(NSString*)text{
    [[[UIAlertView alloc] initWithTitle:@"Уведомление" message:text delegate:nil cancelButtonTitle:@"Закрыть" otherButtonTitles: nil] show];
}

+ (NSDate *)getAgoYear:(int)count_yesar fromDate:(NSDate *)from{
    
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    
    NSDateComponents *offsetComponents = [[NSDateComponents alloc] init];
    [offsetComponents setYear:-count_yesar];
    
    return [gregorian dateByAddingComponents:offsetComponents toDate:from options:0];
    
}

@end


@implementation NSDictionary (Helper)
- (BOOL)hasKey:(NSString*)key{
    if (self) {
        if ([self isKindOfClass:[NSDictionary class]]) {
            if ([self objectForKey:key]) {
                return YES;
            }
        }
    }
    return NO;
}
@end











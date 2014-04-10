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
        CGSize labelSize = [text sizeWithFont:font
         constrainedToSize:constrainedSize
         lineBreakMode:NSLineBreakByWordWrapping];
         
         return labelSize.height;
    }
    
    

    

    
}

+(void)fastAlert:(NSString*)text{
    [[[UIAlertView alloc] initWithTitle:@"Уведомление" message:text delegate:nil cancelButtonTitle:@"Закрыть" otherButtonTitles: nil] show];
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











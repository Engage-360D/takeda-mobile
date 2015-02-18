//
//  NSMutableArray+DataFill.h
//  takeda
//
//  Created by Alexander Rudenko on 13.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSMutableArray (DataFill)

-(void)fillIntegerFrom:(int)start to:(int)finish step:(int)step;
-(void)fillFloatFrom:(float)start to:(float)finish step:(float)step;

-(NSDictionary *)groupByKey:(NSString *) key;
@end

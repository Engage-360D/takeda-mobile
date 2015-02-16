//
//  Synchronizer.h
//  takeda
//
//  Created by Alexander Rudenko on 02.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Synchronizer : NSObject

+(Synchronizer*)sharedInstance;

-(void)startSynchronize;


@end

//
//  NotifPlanner.h
//  takeda
//
//  Created by Alexander Rudenko on 27.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NotifPlanner : NSObject

+(NotifPlanner*)sharedInstance;
@property (nonatomic) BOOL brutUpdate;
-(void)updateNotifications;
-(void)deleteAllNotifications;
-(void)removeNotification:(UILocalNotification*)notif;

@end

//
//  LPUDetail.h
//  takeda
//
//  Created by Alexander Rudenko on 20.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LPUDetail : VControllerExt

@property (nonatomic, strong) IBOutlet UILabel *lpuName;
@property (nonatomic, strong) IBOutlet UILabel *lpuAddress;

@property (nonatomic, strong) NSMutableDictionary *lpu;

@end

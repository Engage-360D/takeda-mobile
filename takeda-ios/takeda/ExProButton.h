//
//  ExProButton.h
//  iMedicum
//
//  Created by Alexander Rudenko on 17.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ExProButton : UIButton

@property (nonatomic, strong) NSMutableDictionary *info;
@property (nonatomic, strong) id target;
@property (nonatomic, strong) NSIndexPath *indexPath;
@property (nonatomic, strong) NSString *text;

@end

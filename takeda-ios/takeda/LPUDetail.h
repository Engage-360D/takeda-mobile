//
//  LPUDetail.h
//  takeda
//
//  Created by Alexander Rudenko on 20.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LPUDetail : VControllerExt
@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (nonatomic, strong) IBOutlet UILabel *lpuName;
@property (nonatomic, strong) IBOutlet UILabel *lpuAddress;
@property (nonatomic, strong) IBOutlet UILabel *lpuInfoText;
@property (nonatomic, strong) IBOutlet UIView *lpuNameAdressBack;

@property (nonatomic, strong) IBOutlet UITextView *phone;
@property (nonatomic, strong) IBOutletCollection(UIImageView) NSMutableArray *textsCollection;




@property (nonatomic, strong) IBOutlet UIView *fieldsContainer;
@property (nonatomic, strong) IBOutlet UIView *lpuInfoContainer;

@property (nonatomic, strong) NSMutableDictionary *lpu;

@end

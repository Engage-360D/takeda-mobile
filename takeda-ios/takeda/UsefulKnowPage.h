//
//  UsefulKnowPage.h
//  takeda
//
//  Created by Alexander Rudenko on 19.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UsefulKnowCell.h"
#import "AllSocials.h"

@interface UsefulKnowPage : VControllerExt<VKSdkDelegate, GPPSignInDelegate, UIWebViewDelegate, OKRequestDelegate, OKSessionDelegate>
@property (nonatomic, assign) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSMutableArray *infoData;
@end
